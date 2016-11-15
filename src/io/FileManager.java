package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;

import org.json.JSONArray;

public class FileManager {
	
	private static final String CARDS_DATA_FILE = "AllSetsArray.json";
	private static final String DATA_PATH = "data//";
	private static final String DEFAULT_IMAGE_FILE = "DefaultImage.png";
	
	public static HashMap<String, List<String>> readDecklists(File path) {
		HashMap<String, List<String>> decklists = new HashMap<String, List<String>>();
		
		for (final File file : path.listFiles()) {
			
			try{
				String mime = Files.probeContentType(Paths.get(file.getPath()));
				if (mime == null || mime.equals("text/plain")){
					List<String> decklist = new ArrayList<>();
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line = br.readLine();
					while (line != null) {
						decklist.add(line);
						line = br.readLine();
					}
					br.close();
					String fileName = file.getName();
					decklists.put(fileName, decklist);
				}
			}
			catch (Exception e) {
				System.out.println("Decklist parse failed for file : " + 
						path.getPath() + "//" + file.getName());
				System.out.println(e.getMessage());	
			}							
		}
		return decklists;
	}
	
	public static ImageIcon getDefaultImage() {
		return new ImageIcon(DATA_PATH + DEFAULT_IMAGE_FILE);		
	}
	
	public static JSONArray getCardsData()
	{
		JSONArray cardsData = null;
		
		try(BufferedReader br = new BufferedReader(new FileReader(DATA_PATH + CARDS_DATA_FILE))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String jsonFileAsString = sb.toString();
		    cardsData = new JSONArray(jsonFileAsString);
		}
		catch (Exception e)
		{
			System.out.println("Loading card data failed: " + e.getMessage());
		}
		return cardsData;
	}
}

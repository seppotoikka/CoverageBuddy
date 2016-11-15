package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.FileManager;

public class CardWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final String WINDOW_NAME = "Card Image";
	
	private static final double GATHERER_IMAGE_PX_WIDTH = 223;
	private static final double GATHERER_IMAGE_PX_HEIGHT = 311;
	private static final double GATHERER_IMAGE_RATIO = GATHERER_IMAGE_PX_WIDTH / GATHERER_IMAGE_PX_HEIGHT;
			
	private JLabel imageContainer;
	private ImageIcon originalImage;
	private ImageIcon defaultImage;
	private JPanel panel;
		
	public CardWindow() {

		setTitle(WINDOW_NAME);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout(0, 0));
		setBounds(500, 100, 300, 400);	
		Dimension windowDimension = new Dimension(300, 400);
		setMinimumSize(windowDimension);	
		panel = new JPanel();
		panel.setLayout(new BorderLayout(0,0));
		panel.setBackground(Color.BLACK);
		panel.setBorder(BorderFactory.createEmptyBorder());
		defaultImage = FileManager.getDefaultImage();
		originalImage = defaultImage;
		imageContainer = new JLabel();
		imageContainer.setBorder(BorderFactory.createEmptyBorder());
		imageContainer.setIcon(originalImage);
		imageContainer.setHorizontalAlignment(JLabel.CENTER);
		add(panel, BorderLayout.CENTER);	
		panel.add(imageContainer, BorderLayout.CENTER);
		setVisible(true);	
	}
	
	public void scaleImage(){
		if (originalImage != null) {
			if (((double) panel.getWidth() / panel.getHeight()) > GATHERER_IMAGE_RATIO) {
				int width = new Double(panel.getHeight() * GATHERER_IMAGE_RATIO).intValue();
				int height = new Double(panel.getHeight()).intValue();
				imageContainer.setIcon(new ImageIcon(originalImage.getImage()
						.getScaledInstance(width,height, Image.SCALE_SMOOTH)));
			}
			else {
				int width = new Double(panel.getWidth()).intValue();
				int height = new Double(panel.getWidth() / GATHERER_IMAGE_RATIO).intValue();
				imageContainer.setIcon(new ImageIcon(originalImage.getImage()
						.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
			}
		}		
	}
	
	public void setCardImage(int multiverseId) {
		if (multiverseId > 0) {
			try {
				originalImage =  new ImageIcon(new URL("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=" + 
						multiverseId + "&type=card"));
			}
			catch (Exception e) {
				System.out.println("Loading image failed " + e.getMessage());
			}
		}
		else {
			originalImage = defaultImage;
		}	
	}

}

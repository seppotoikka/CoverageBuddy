package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.util.List;
import java.util.Set;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.JSONArray;
import org.json.JSONObject;

import io.FileManager;
import io.StringHelper;

public class DecklistWindow extends JFrame{
	
	private static final String CHOOSE_FOLDER_PROMPT = "Select Decklist Folder";
	private static final String WINDOW_NAME = "Coverage Buddy";
	
	private JButton selectFolderButton;
	private JButton clearImageButton;
	private JTextField cardTextSearchField;
	private JButton resizeImageButton;
	private JComboBox<String[]> player1DecklistChooser;
	private JComboBox<String[]> player2DecklistChooser;
	private JScrollPane player1DecklistContainer;
	private JScrollPane player2DecklistContainer;
	
	private JFileChooser chooser;
	
	private HashMap<String, List<String>> decklists;	
	private JSONArray cardList;
	
	private CardWindow cardWindow;
	private OracleWindow oracleWindow;
	
	private String latestRetrievedCard = "Card Name";
	private String oracleText = "Oracle Text";

	private static final long serialVersionUID = 1L;

	public DecklistWindow() {
		setTitle(WINDOW_NAME);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 600, 800);	
		Dimension windowDimension = new Dimension(600, 800);
		setMinimumSize(windowDimension);
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();		
		
		//natural height, maximum width, some vertical padding
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTH;
		c.weighty = 0.0;
		c.ipady = 10;
		c.insets = new Insets(5,5,0,5);
		
		selectFolderButton = new JButton("Select Folder");
		selectFolderButton.setFocusable(false);
		selectFolderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectFolder();
			}
		});
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		contentPane.add(selectFolderButton, c);
		
		clearImageButton = new JButton("Reset Image");
		clearImageButton.setFocusable(false);
		clearImageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showCard(0);
			}
		});
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		contentPane.add(clearImageButton, c);
		
		cardTextSearchField = new JTextField("Search For a Card");
		cardTextSearchField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
		        cardTextSearchField.selectAll();
		    }
			public void focusLost(FocusEvent e) {
				//do nothing
			}
		});
		cardTextSearchField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				int id = findCardMultiverseId(cardTextSearchField.getText(), true);
				showCard(id);
				cardTextSearchField.selectAll();
			}
		});
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		contentPane.add(cardTextSearchField, c);
		
		resizeImageButton = new JButton("Resize Image");
		resizeImageButton.setFocusable(false);
		resizeImageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (cardWindow != null)
					cardWindow.scaleImage();
			}
		});
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 1;
		contentPane.add(resizeImageButton, c);
		
		
		player1DecklistChooser = new JComboBox<String[]>();
		player1DecklistChooser.setFocusable(false);
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 2;
		player1DecklistChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                loadDecklist(player1DecklistChooser, player1DecklistContainer);
            }
        });	
		contentPane.add(player1DecklistChooser, c);
		
		player2DecklistChooser = new JComboBox<String[]>();
		player2DecklistChooser.setFocusable(false);
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 2;
		player2DecklistChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                loadDecklist(player2DecklistChooser, player2DecklistContainer);
            }
        });
		contentPane.add(player2DecklistChooser, c);
		
		player1DecklistContainer = new JScrollPane(new JTextArea());
		c.weightx = 0.5;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 3;
		player1DecklistContainer.setViewportView(new JTable());
		contentPane.add(player1DecklistContainer, c);
		
		player2DecklistContainer = new JScrollPane(new JTextArea());
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 3;
		player2DecklistContainer.setViewportView(new JTable());
		contentPane.add(player2DecklistContainer, c);
							
		setContentPane(contentPane);
		setVisible(true);
	}

	private void selectFolder() {
		chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle(CHOOSE_FOLDER_PROMPT);
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);  
	    chooser.setApproveButtonText("Select");
	    chooser.setApproveButtonToolTipText("Select this Folder");
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
	    	setDecklists(FileManager.readDecklists(chooser.getSelectedFile()));
	}
	
	//not typesafe, beware!
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setDecklists(HashMap<String, List<String>> decklists){
		this.decklists = decklists;
		Set<String> filenames = decklists.keySet();
		String[] filenamesArray = filenames.toArray(new String[filenames.size()]);
		if (filenames.size() > 0) {	
			DefaultComboBoxModel model1 = new DefaultComboBoxModel(filenamesArray);
			DefaultComboBoxModel model2 = new DefaultComboBoxModel(filenamesArray);
			player1DecklistChooser.setModel(model1);
			player2DecklistChooser.setModel(model2);
		}
	}
	
	//not typesafe, beware!
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadDecklist(JComboBox<String[]> jcb, JScrollPane jsp) {
		List<String> decklist = decklists.get(jcb.getSelectedItem().toString());
		if (decklist != null) {
			JList jList = new JList(decklist.toArray()){
				private static final long serialVersionUID = 1L;				
			};
			jList.setFocusable(false);
			jList.setFont(new Font("Monospaced", Font.PLAIN, 14));
			jList.setBackground(SystemColor.inactiveCaptionBorder);
			jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		    jList.addListSelectionListener(new ListSelectionListener() {
			      public void valueChanged(ListSelectionEvent e) {
			    	  if (!e.getValueIsAdjusting()) {
			              JList list = (JList) e.getSource();
			              Object selectionValue = list.getSelectedValue();
			              if (selectionValue != null) {
			            	  int cardMultiverseId = findCardMultiverseId(selectionValue.toString(), false);
			                  showCard(cardMultiverseId);
			                  list.clearSelection();
			              }		                  		                  
			          }  	      	
			      }
			    });
		    jsp.setViewportView(jList);
		}
	}
	

	private int findCardMultiverseId(String s, boolean useLevenshtein) {
		/* Find card multiverseId either by searching longest card name that is a substring 
		 * of the given string parameter or use Levenshtein algorithm to find best matching card name
		 * if there are many prints of the card, will return first match in JSONArray */
		
		int longestMatch = 0;
		int longestMatchId = 0;
		try {
			if (cardList != null) {
				for (int i = 0; i < cardList.length(); i++) {

					JSONArray cards = cardList.getJSONObject(i).getJSONArray("cards");
					if (cards != null) {
						for (int j = 0; j < cards.length(); j++) {
							JSONObject cardJSON = cards.getJSONObject(j);
							String cardName = cardJSON.getString("name");
							if (cardName != null && cardJSON.has("multiverseid") &&
									cardJSON.has("text")) {
								//check if this is a better match than previous best match
								if (useLevenshtein) {
									int distance = StringHelper.levenshteinDistance(
											s.toLowerCase(), cardName.toLowerCase());
									if ( distance < s.length() - longestMatch) {
										longestMatch = s.length() - distance;
										longestMatchId = cardJSON.getInt("multiverseid");
										oracleText = cardJSON.getString("text");
										latestRetrievedCard = cardName;
									}
								}
								else {
									if (s.toLowerCase().contains(cardName.toLowerCase()) 
											&& cardName.length() > longestMatch) {
										longestMatch = cardName.length();
										longestMatchId = cardJSON.getInt("multiverseid");
										oracleText = cardJSON.getString("text");
										latestRetrievedCard = cardName;
									}
								}
								
							}
						}
					}										
				}
			}
			else {
				System.out.println("Cardlist is null");
			}
		}
		catch (Exception e) {
			System.out.println("JSON parsing failed: " + e.getMessage());
		}
		return longestMatchId;					
	}
	
	private void showCard(int multiverseId) {
		if (cardWindow == null)
			cardWindow = new CardWindow();
		cardWindow.setCardImage(multiverseId);	
		cardWindow.scaleImage();
		if (oracleWindow == null)
			oracleWindow = new OracleWindow();
		oracleWindow.updateContent(latestRetrievedCard, oracleText);
		cardTextSearchField.setText(latestRetrievedCard);
	}
	
	
	public void setCardList(JSONArray cardList) {
		this.cardList = cardList;
	}
}

package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class OracleWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final String WINDOW_NAME = "Oracle Text";
	
	private JScrollPane scrollPane;
	private JTextArea cardName;
	private JTextArea textArea;
	
	public OracleWindow () {
		setTitle(WINDOW_NAME);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout(0, 0));
		setBounds(500, 300, 300, 400);	
		Dimension windowDimension = new Dimension(300, 400);
		setMinimumSize(windowDimension);
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//natural height, maximum width, some vertical padding
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTH;
		c.weighty = 0.0;
		c.weightx = 1.0;
		c.ipady = 10;
		c.insets = new Insets(5,5,0,5);
		
		cardName = new JTextArea();
		cardName.setEditable(false);
		cardName.setLineWrap(true);
		cardName.setWrapStyleWord(true);
		cardName.setFont(cardName.getFont().deriveFont(26f));
		
		c.gridx = 0;
		c.gridy = 0;
		
		contentPane.add(cardName, c);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(textArea.getFont().deriveFont(20f));		
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1;
		
		contentPane.add(scrollPane, c);
		
		setContentPane(contentPane);
		setVisible(true);
	}
	
	public void updateContent(String name, String oracleText) {
		cardName.setText(name);
		textArea.setText(oracleText);
	}

}

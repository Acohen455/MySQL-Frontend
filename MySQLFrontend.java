/*
 * Reference Resources:
 * https://stackoverflow.com/questions/26290738/how-does-gridwidth-and-gridheight-work-java-guid-gridbaglayout
 * https://stackoverflow.com/questions/17766217/gridbaglayout-how-to-fill-all-empty-spaces
 * 
 * https://stackoverflow.com/questions/11511864/java-can-a-gridbaglayout-be-used-to-organize-a-jpanel-used-in-a-cardlayout
 * 
 * https://stackoverflow.com/questions/19675854/java-swing-cant-add-multiple-panels-to-panel
 * 
 * 
 * https://mkyong.com/swing/java-swing-joptionpane-showinputdialog-example/
 * 
 * 
 * 
 * Structure is as follows:
 * CardLayout w/ GridBagLayout nested inside
 * 
 * 
 * 
 * 
 * 
 * For the table storage:
 * 
 * Each table will be stored as an ArrayList<Map>, where Key:Value correlates to Column:Column Value
 * Using a LinkedHashMap so the key:value pairs are ordered in the order put into the list -- this will correspond to the order in the table
 * 
 */


/*
 * 
 * TODO (FOR GITHUB PUBLISHING)
 * 
 * 
 * Allow user to enter connection location manually (WORKING ON NOW
 * Implement multiple-schema functionality? 
 *		Possibly implement a schema selection panel once connected 
 *			Can pull schema from the database info/metadata
 * 
 * 
 * 
 * 
 * 
 */


import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.text.NumberFormat;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;

import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class MySQLFrontend {

	private JFrame frame;

	
	
	final static String TABLES_PANEL = "Tables Panel";
	final static String CONNECTION_PANEL = "Database Connection Panel";
	final static String TABLE_CREATION_PANEL = "Table Creation Panel";
	final static String EDIT_TABLE_PANEL = "Table Editing Panel";
	final static String CONNECTION_INFO_PANEL = "Connection Info Panel";
	final static String PORT_PANEL = "Port Entry Panel";
	final static String URL_PANEL = "URL Entry Panel";
	final static String USER_PANEL = "Username Entry Panel";
	final static String PASS_PANEL = "Password Entry Panel";
	final static String SCHEMA_PANEL = "Schema Selection Panel";
	
	
	//setup connection for later
	static Connection con = null;
	
	//string array for data types that are selectable for columns
	//double unnecessary as float does this
	static final String[] columnOptions = {"SMALLINT", "TINYINT", "INT", "MEDIUMINT", "BIGINT", "DECIMAL",  "FLOAT",
			"BIT", "CHAR", "VARCHAR", "BINARY", "VARBINARY","TINYBLOB", "BLOB", "MEDIUM BLOB", "LONG BLOB", "TINY TEXT",
			 "TEXT", "MEDIUM TEXT", "LONG TEXT", "BOOL", "DATE", "TIME", "DATETIME",
			"TIMESTAMP", "YEAR"}; 
			
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//runs the swing code to create the UI
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MySQLFrontend window = new MySQLFrontend();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * I've modified this function to take a connection object, which will be passed to the initialization function
	 * this allows me to create the database connection in main and pass it along as necessary
	 */
	public MySQLFrontend() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {


		
		
		
		
		//strings for connecting to the database
		//for now these are hardcoded, later I'll attempt to make these user entered
		//TODO allow user input for these values
		
		//DEPRECATED HARDCODE
		//String url = "jdbc:mysql://localhost:3306/my_guitar_shop";
		//String username = "root";
		//String password = "password";
		
		//default value connects to localhost
		String url = null;
		String username = null;
		String password = null;
		
		//connector declaration

		
		
		
		
		
		
		
		

		//setting up the frame
		frame = new JFrame();
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//center the window
		centerWindow(frame);
		
		
		JPanel cardPanel = new JPanel();
		//opting to not manually set bounds so the frame automatically resizes w/ pack
		//default  bounds are now set from % of screen size
		cardPanel.setOpaque(true);
		

		
		
		
		//bind the panel to the frame
		frame.getContentPane().add(cardPanel);
		
		
		//create a card layout as the overarching container
		CardLayout cardLayout = new CardLayout();
		//set the panel layout
		cardPanel.setLayout(cardLayout);
		
		
	
		//the actual displaying will happen here
		urlCard(frame, cardLayout, cardPanel);
	}
	
	public static void urlCard(JFrame frame, CardLayout cardLayout, JPanel cardPanel) {
		
			//insets for the layouts for retrieving username, password, etc.
				Insets labelInsets = new Insets(0, 20, 0, 0);
				Insets fieldInsets = new Insets(0,0,0,25);
				Insets buttonInsets = new Insets(5,15,5,15);;
				
				//JPanel for getting connection info
				JPanel urlConnectionCard = new JPanel();
				
				//layout for connection info gathering
				GridBagLayout connectionInfoLayout = new GridBagLayout();
				//set the # and size of rows and columns
				connectionInfoLayout.columnWidths = new int[] {200,200};
				connectionInfoLayout.rowHeights = new int[] {50,50};
				//connect the layout to the card
				urlConnectionCard.setLayout(connectionInfoLayout);
				
				
			
				
				
				//label for field to enter URL
				JLabel urlLabel = new JLabel();
				urlLabel.setText("Database URL: ");
				//create constraints
				GridBagConstraints urlLabelConstraints = new GridBagConstraints();
				urlLabelConstraints.gridx = 0;
				urlLabelConstraints.gridy = 0;
				urlLabelConstraints.gridheight = 1;
				urlLabelConstraints.gridwidth = 1;
				//anchoring to the right side, then adding insets 
				urlLabelConstraints.anchor = GridBagConstraints.WEST;
				//insets are Insets(TOP, LEFT, BOTTOM, RIGHT)
				urlLabelConstraints.insets = labelInsets;
				//urlLabel.setHorizontalAlignment(SwingConstants.LEFT);
				//add the label with constraints to the card
				urlConnectionCard.add(urlLabel, urlLabelConstraints);
				
				
				//field for entering URL
				JTextField urlField = new JTextField();
				urlField.setEditable(true);
				//constraints for the text entry field
				GridBagConstraints urlFieldConstraints = new GridBagConstraints();
				urlFieldConstraints.gridx = 1;
				urlFieldConstraints.gridy = 0;
				urlFieldConstraints.gridheight = 1;
				urlFieldConstraints.gridwidth = 1;
				urlFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
				//insets are Insets(TOP, LEFT, BOTTOM, RIGHT)
				urlFieldConstraints.insets = fieldInsets;
				//add the field w/ constraints to the card
				urlConnectionCard.add(urlField, urlFieldConstraints);
				
				//confirm button for url grab
				JButton confButton = new JButton("Confirm");
				
				
				//these constraints will be used for all 4 panels with the same layout
				//that's the url, port, username, pass panels
				GridBagConstraints confButtonConstraints = new GridBagConstraints();
				confButtonConstraints.gridx = 0;
				confButtonConstraints.gridy = 1;
				confButtonConstraints.gridheight = 1;
				confButtonConstraints.gridwidth = 1;
				//take up all horizontal space in the cell
				confButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
				//insets are Insets(TOP, LEFT, BOTTOM, RIGHT)
				confButtonConstraints.insets = buttonInsets;
				urlConnectionCard.add(confButton, confButtonConstraints);
				
				//cancel button for backing out of the app
				//this button will prompt the user to exit the app (or not)
				JButton cancelButton = new JButton("Cancel");
				
				//these constraints will also be used for all 4 panels
				//user panel, pass panel, port panel, url panel
				GridBagConstraints cancelButtonConstraints = new GridBagConstraints();
				cancelButtonConstraints.gridx = 1;
				cancelButtonConstraints.gridy = 1;
				cancelButtonConstraints.gridheight = 1;
				cancelButtonConstraints.gridwidth = 1;
				cancelButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
				cancelButtonConstraints.insets = buttonInsets;
				urlConnectionCard.add(cancelButton, cancelButtonConstraints);
				
				
				frame.setTitle("URL");
				
				//listener for cancel button
				cancelButton.addActionListener(e ->{
					cnBtnLogic(frame, cardLayout, cardPanel);
				});

				
				//stringbuilder for creating the URL
				//I'm opting to not specify a schema in the URL, instead connecting directly
				//TODO: Allow for multiple schema?
				//TODO: Make a schema selection window later? -- could be good after successful connection
				StringBuilder urlBuilder = new StringBuilder();
				
				
				//listener for confirm button
				confButton.addActionListener(e -> {
					if (urlField.getText().equals("")){
						JOptionPane.showMessageDialog(frame, "Please enter a URL to connect to!", "Error", 
								JOptionPane.ERROR_MESSAGE);
					} else {
						urlBuilder.setLength(0);
						urlBuilder.append("jdbc:mysql://" + urlField.getText() + ":" );
						getPort(frame, cardPanel, cardLayout, labelInsets, fieldInsets, buttonInsets, urlBuilder, 
								confButtonConstraints, cancelButtonConstraints);
					}
				});
				
				
				//add the panel to the card layout
				cardPanel.add(urlConnectionCard, URL_PANEL);
				
				//show the card
				//this card gets info from the user to connect to the database with
				cardLayout.show(cardPanel, URL_PANEL);
				frame.pack();
				frame.setVisible(true);
	}
	
	
	//method to close application if database connection fails
	public static void closeApplication(Window frame) {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	
	
	//method for getting the port
	//passing constraints to clean the code up
	public static void getPort(JFrame frame, JPanel cardPanel, CardLayout cardLayout, Insets labelInsets, Insets fieldInsets,
			Insets buttonInsets, StringBuilder urlBuilder, GridBagConstraints confButtonConstraints, 
			GridBagConstraints cancelButtonConstraints) {
		
		
		frame.setTitle("Port");
		
		//panel for holding content
		JPanel portPanel = new JPanel();
		
		//layout for getting port
		GridBagLayout portCardLayout = new GridBagLayout();
		portCardLayout.columnWidths = new int[] {200,200};
		portCardLayout.rowHeights = new int[] {50,50};
		//connect layout to card
		portPanel.setLayout(portCardLayout);
		
		//label for input
		JLabel portPrompt = new JLabel();
		portPrompt.setText("Port: ");
		GridBagConstraints portPromptConstr = new GridBagConstraints();
		portPromptConstr.gridx = 0;
		portPromptConstr.gridy = 0;
		portPromptConstr.anchor = GridBagConstraints.WEST;
		portPromptConstr.insets = labelInsets;
		portPromptConstr.fill = GridBagConstraints.HORIZONTAL;
		portPanel.add(portPrompt, portPromptConstr);
		
		//number formatter for formatting text field
		NumberFormat numFormat = NumberFormat.getNumberInstance();
		
		
		JTextField portField = new JTextField();
		portField.setEditable(true);
		GridBagConstraints portFieldConstr = new GridBagConstraints();
		portFieldConstr.gridx = 1;
		portFieldConstr.gridy = 0;
		portFieldConstr.fill = GridBagConstraints.HORIZONTAL;
		portFieldConstr.insets = fieldInsets;
		portPanel.add(portField, portFieldConstr);
		
		JButton confButton = new JButton("Confirm");
		JButton cancelButton = new JButton("Cancel");
		
		portPanel.add(confButton, confButtonConstraints);
		portPanel.add(cancelButton, cancelButtonConstraints);
		
		
		//logic for cancel button is in a function for modularity purposes
		cancelButton.addActionListener( e ->{
			cnBtnLogic(frame, cardLayout, cardPanel);
		});
				
				
		
		
		
		confButton.addActionListener(e->{
			String enteredText = portField.getText();
				//check if entered text is empty
				if (enteredText.equals("")) {
					//pop an error pane if empty entry
					JOptionPane.showMessageDialog(frame, "Please enter a port to connect to!", "Error", 
							JOptionPane.ERROR_MESSAGE);
				} else {
					//check for numeric input
					if (numCheck(enteredText)) {
						//if the input is numeric, append to the stringbuilder for connection
						//THE STRING FOR CONNECTION IS NOW COMPLETE)
						urlBuilder.append(portField.getText());
						getUser(frame, cardPanel, cardLayout, labelInsets, fieldInsets, buttonInsets, 
								confButtonConstraints, cancelButtonConstraints, urlBuilder);
					}
				}
		});
		
		//add the card to the cardlayout panel
		cardPanel.add(portPanel, PORT_PANEL);
		
		//show the updated view
		cardLayout.show(cardPanel, PORT_PANEL);
		frame.pack();
		frame.setVisible(true);
	}
	
	//function for cancel button on connection info gathering frames
	public static void cnBtnLogic(JFrame frame, CardLayout cardLayout, JPanel cardPanel) {
		//variable for confirmation
		//using an int as this is what the dialog returns
		int conf = 1;
		//array for options
		String[] confOptions = {"Yes", "No"};
		//ask if user wants to return to url entry
		conf = JOptionPane.showOptionDialog(frame, "Would you like to return to URL entry?", 
				"Exit", 0, 0, null, confOptions, confOptions[1]);
		
		//if yes go back to the previous card
		//else exit program
		if (conf == 0) {
			urlCard(frame, cardLayout, cardPanel);
		} else {
			closeApplication(frame);
		}
	}
	
	public static boolean numCheck(String enteredText) {
		boolean numBool = false;
		if (enteredText.matches("^[a-zA-Z]*$") && enteredText != null) {
			JOptionPane.showMessageDialog(null, "That doesn't seem to be a number", "Error", JOptionPane.WARNING_MESSAGE);
			numBool = false;
		} else {
			numBool = true;
		}
		return numBool;
	}
	
	/*
	 * 
	 * Basic setup for all these panels is more or less the same across windows/purposes
	 * 
	 */
	public static void getUser(JFrame frame, JPanel cardPanel, CardLayout cardLayout, Insets labelInsets, 
			Insets fieldInsets, Insets buttonInsets, GridBagConstraints confBtnConstr, 
			GridBagConstraints cnBtnConstr, StringBuilder urlBuilder) {
		
		
		frame.setTitle("Username");
		
		StringBuilder userStringBuilder = new StringBuilder();
		
		//panel for grabbing the username
		JPanel userPanel = new JPanel();
	
		
		//panel setup
		GridBagLayout userCardLayout = new GridBagLayout();
		userCardLayout.columnWidths = new int[] {200, 200};
		userCardLayout.rowHeights = new int[] {50,50};
		userPanel.setLayout(userCardLayout);
		
		
		JLabel userPrompt = new JLabel();
		userPrompt.setText("Username: ");
		GridBagConstraints userPromptConstr = new GridBagConstraints();
		userPromptConstr.gridx = 0;
		userPromptConstr.gridy = 0;
		userPromptConstr.anchor = GridBagConstraints.WEST;
		userPromptConstr.insets = labelInsets;
		userPromptConstr.fill = GridBagConstraints.HORIZONTAL;
		userPanel.add(userPrompt, userPromptConstr);
		
		JTextField userField = new JTextField();
		userField.setEditable(true);
		GridBagConstraints userFieldConstr = new GridBagConstraints();
		userFieldConstr.gridx = 1;
		userFieldConstr.gridy = 0;
		userFieldConstr.fill = GridBagConstraints.HORIZONTAL;
		userFieldConstr.insets = fieldInsets;
		userPanel.add(userField, userFieldConstr);
		
		JButton confButton = new JButton("Confirm");
		JButton cnButton = new JButton("Cancel");
		
		//define the button positions with predetermined constr
		//we can do this because the panel is set up the same each time
		userPanel.add(confButton, confBtnConstr);
		userPanel.add(cnButton, cnBtnConstr);
				
		//listener
		confButton.addActionListener(e->{
			String enteredText = userField.getText();
			String userString;

			
				//check if entered text is empty
				if (enteredText.equals("")) {
					//pop an error pane if empty entry
					JOptionPane.showMessageDialog(frame, "Please enter a username!", "Error", 
							JOptionPane.ERROR_MESSAGE);
				} else {
						//use a stringbuilder to build the username
						userStringBuilder.append(enteredText);
						userString = userStringBuilder.toString();
						getPassword(frame, cardPanel, cardLayout, labelInsets, fieldInsets, buttonInsets,
								confBtnConstr, cnBtnConstr, urlBuilder, userString);
					}
		});
		
		//call function for cancel button logic
		cnButton.addActionListener(e->{
			cnBtnLogic(frame, cardLayout, cardPanel);
		});
		
		//add the card to the panel and show it in the layout
		cardPanel.add(userPanel, USER_PANEL);
		cardLayout.show(cardPanel, USER_PANEL);
		frame.pack();
		frame.setVisible(true);
		
		}
	
	public static void getPassword(JFrame frame, JPanel cardPanel, CardLayout cardLayout, 
			Insets labelInsets, Insets fieldInsets, Insets buttonInsets, 
			GridBagConstraints confBtnConstr, GridBagConstraints cnBtnConstr, StringBuilder urlBuilder,
			String usrStr) {
		String passwordString;
		
		JPanel passwordPanel = new JPanel();
		
		
		//layout for connection info gathering
		GridBagLayout passwordCardLayout = new GridBagLayout();
		//set the # and size of rows and columns
		passwordCardLayout.columnWidths = new int[] {200,200};
		passwordCardLayout.rowHeights = new int[] {50,50};
		//connect the layout to the card
		passwordPanel.setLayout(passwordCardLayout);
		 
		
		JLabel passPrompt = new JLabel();
		passPrompt.setText("Password: ");
		GridBagConstraints passPromptConstr = new GridBagConstraints();
		passPromptConstr.gridx = 0;
		passPromptConstr.gridy = 0;
		passPromptConstr.anchor = GridBagConstraints.WEST;
		passPromptConstr.insets = labelInsets;
		passPromptConstr.fill = GridBagConstraints.HORIZONTAL;
		passwordPanel.add(passPrompt, passPromptConstr);
		
		JTextField passField = new JTextField();
		passField.setEditable(true);
		GridBagConstraints passFieldConstr = new GridBagConstraints();
		passFieldConstr.gridx = 1;
		passFieldConstr.gridy = 0;
		passFieldConstr.gridwidth = 1;
		passFieldConstr.fill = GridBagConstraints.HORIZONTAL;
		passFieldConstr.insets = fieldInsets;
		passwordPanel.add(passField, passFieldConstr);
		
		JButton confirmButton = new JButton("Confirm");
		JButton cnButton = new JButton("Cancel");
		
		passwordPanel.add(confirmButton, confBtnConstr);
		passwordPanel.add(cnButton, cnBtnConstr);
		
		
		confirmButton.addActionListener(e -> {
			String enteredText = passField.getText();
			StringBuilder passBuilder = new StringBuilder();
			
			
			if (enteredText.equals("")) {
				//pop an error pane if empty entry
				JOptionPane.showMessageDialog(frame, "Please enter a username!", "Error", 
						JOptionPane.ERROR_MESSAGE);
			} else {
					//use a stringbuilder to build the password
					passBuilder.append(enteredText);
					String pass = passBuilder.toString();
					try {
						connectionView(frame, cardPanel, cardLayout, urlBuilder, usrStr, pass);
					} catch (SQLException e1) {
						//if there's an sql exception, print it in an error dialog
						JOptionPane.showMessageDialog(null, e1.toString(), "Error", 
								JOptionPane.ERROR_MESSAGE);
						closeApplication(frame);
					}
				}
		});
		
		
		cnButton.addActionListener(e ->{
			cnBtnLogic(frame, cardLayout, cardPanel);
		});
		
	cardPanel.add(passwordPanel, PASS_PANEL);
	cardLayout.show(cardPanel, PASS_PANEL);
	frame.pack();
	frame.setVisible(true);
		
	}
	
	

	
	public static void connectionView(JFrame frame, JPanel cardPanel, CardLayout cardLayout, StringBuilder urlBuilder, String username, String password) throws SQLException {
		//creating a card to popup for database connection success
				JPanel databaseConnectedCard = new JPanel();
				boolean databaseConnectionError = false;
				
				
				frame.setTitle("Connection");
				
				//setup a GBL for the successful connection prompt
				GridBagLayout connectedCardLayout = new GridBagLayout();
				connectedCardLayout.columnWidths = new int[] {200};
				connectedCardLayout.rowHeights = new int[] {50, 50};
				databaseConnectedCard.setLayout(connectedCardLayout);
				
				//add the card with the layout to the parent panel
				cardPanel.add(databaseConnectedCard, CONNECTION_PANEL);
			
				
				
				
				//add the label
				//this will later display either connection success or connection failure
				JLabel connectionLabel = new JLabel();
				//create constraints for the label
				GridBagConstraints connectionLabelConstraints = new GridBagConstraints();
				connectionLabelConstraints.gridx = 0;
				connectionLabelConstraints.gridy = 0;
				connectionLabelConstraints.insets = new Insets(0, 15, 0, 15);
				//add the label with constraints to the card
				databaseConnectedCard.add(connectionLabel, connectionLabelConstraints);
				
				
				//add a confirmation button
				JButton confirmButton = new JButton("Confirm");
				//create constraints
				GridBagConstraints confirmButtonConstraints = new GridBagConstraints();
				confirmButtonConstraints.gridx = 0;
				confirmButtonConstraints.gridy = 1;
				//add to card
				databaseConnectedCard.add(confirmButton, confirmButtonConstraints);
				
				
				
				
				
				
				/*
				 * 
				 * CODE FOR DATABASE CONNECTIVITY
				 * 
				 * Database connectivity code is here so a connection successful message can be added to the cardlayout
				 * 
				 * 
				 * TODO: Get user data for database connection
				 * TODO: Error check the url before connecting
				 * TODO: Add functionality to tell the user wrong username/password entered
				 * 
				 * 
				 */
				
				
				
				 try {
						con = DriverManager.getConnection(urlBuilder.toString(), username, password);
						connectionLabel.setText("Database Connected Successfully");
						frame.pack();
					} catch (Exception f) {
						connectionLabel.setText("Error: " + f);
						//set a boolean to be used later
						//this makes the confirm button work when there's an error
						databaseConnectionError = true;
						frame.pack();
					} 
				 
		
				
				
				//make the boolean final so its usable in the action listener logic
				 final boolean conErr = databaseConnectionError;
				/*
				 * 
				 * BUTTON FUNCTIONALITY FOR MAIN WINDOWS
				 * 
				 * 
				 * 
				 */
				
				//hook up an action listener to the confirmation button on the successful connection screen
				//we are doing this here so we can seamlessly use this to change cards to the next card that's been set up
				confirmButton.addActionListener(e-> {
					JOptionPane errorPane = new JOptionPane();
			
					//logic for connecting/not connecting based on error
					if (!conErr) {
						try {
							schemaView(frame, cardPanel, cardLayout, urlBuilder, username, password);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							errorPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
						}
					} else {
						//TODO: when taking user input, change this to pop an error message and reprompt for database details
						closeApplication(frame);
					}
				});
				
				//flip to the first card
				cardLayout.show(cardPanel, CONNECTION_PANEL);
				//make the frame visible
				frame.setVisible(true);
	}
	
	
	public static void schemaView(JFrame frame, JPanel cardPanel, CardLayout cardLayout, 
			StringBuilder urlString, String user, String pass) throws SQLException {
		JPanel schemaPanel = new JPanel();
		
		frame.setTitle("Schema");
		
		GridBagLayout schemaViewLayout = new GridBagLayout();
		Dimension schemaViewSize = getIdealSize(.25,.5);
		
		
		GridBagLayout schemaPanelLayout = new GridBagLayout();
		//let the columns fill space
		schemaPanelLayout.columnWidths = new int[]{(int) (schemaViewSize.getWidth() * .5),
				(int) (schemaViewSize.getWidth() * .5)};
		//manually set the row heights
		schemaPanelLayout.rowHeights = new int[]{(int) (schemaViewSize.getHeight() * .8) ,
				(int) (schemaViewSize.getHeight() * .2)};
		//weight the columns evenly 
		//this distributes the space evenly amongst them
		schemaPanelLayout.columnWeights = new double[]{1.0, 1, 1};
		//weight rows evenly
		schemaPanelLayout.rowWeights = new double[]{1.0, 0.1};
		//set the panel layout on the holder panel to be the gridbaglayout
		schemaPanel.setLayout(schemaPanelLayout);
		
		
		
		StringBuilder getSchemaBuilder = new StringBuilder();
		getSchemaBuilder.append("show databases where `Database` not regexp 'schema|sys|mysql';");
		
		PreparedStatement getSchema = con.prepareStatement(getSchemaBuilder.toString());
		ResultSet schemaSet = getSchema.executeQuery();
		
		List<String> schemaList = new ArrayList<String>();
		
		
		JButton useSchBtn = new JButton ("Use Schema");
		GridBagConstraints useSchBtnGBC = new GridBagConstraints();
		useSchBtnGBC.gridx = 0;
		useSchBtnGBC.gridy = 1;
		useSchBtnGBC.gridheight = 1;
		useSchBtnGBC.gridwidth = 1;
		useSchBtnGBC.anchor = GridBagConstraints.CENTER;
		schemaPanel.add(useSchBtn, useSchBtnGBC);
		
		JButton delSchema = new JButton("Delete Schema");
		GridBagConstraints useDelBtnGBC = new GridBagConstraints();
		useDelBtnGBC.gridx = 1;
		useDelBtnGBC.gridy = 1;
		useDelBtnGBC.gridheight = 1;
		useDelBtnGBC.gridwidth = 1;
		useDelBtnGBC.anchor = GridBagConstraints.CENTER;
		schemaPanel.add(delSchema, useDelBtnGBC);
		
		
		
		
		JScrollPane schemaPane = new JScrollPane();
		GridBagConstraints schemaPaneConstraints = new GridBagConstraints();
		schemaPaneConstraints.weightx = 1.0;
		schemaPaneConstraints.weighty = 1.0;
		schemaPaneConstraints.gridx = 0;
		schemaPaneConstraints.gridwidth = 3;
		schemaPaneConstraints.gridy = 0;
		schemaPaneConstraints.fill = GridBagConstraints.BOTH;
		//set the preferred screen size to 80% of the users screen
		schemaPane.setPreferredSize(schemaViewSize);
		schemaPanel.add(schemaPane, schemaPaneConstraints);
		
		while (schemaSet.next()){
			schemaList.add(schemaSet.getString(1));
		}
		
		 
		
			//StringBuilder useSchemaBuilder = new StringBuilder();
			//useSchemaBuilder.append("USE "+ schemaList.get(0));
			//con.close();
			//System.out.println(useSchemaBuilder.toString());
			
			//PreparedStatement useSchema = con.prepareStatement(useSchemaBuilder.toString());
			//useSchema.execute();
			
			//urlString.append("/" + schemaList.get(0));
			
			//reestablish connection using the only available database/schema
			//con = DriverManager.getConnection(urlString.toString(), user, pass);
			
		
			DefaultListModel schemasModel = new DefaultListModel();
			
			for (int i = 0; i < schemaList.size(); i++) {
				schemasModel.addElement(schemaList.get(i).toString());
			}
			
		JList schemaListView = new JList(schemasModel);
		schemaPane.setViewportView(schemaListView);
		

		useSchBtn.addActionListener(e ->{
			urlString.append("/" + (String)schemaListView.getSelectedValue());
			try {
				con = DriverManager.getConnection(urlString.toString(), user, pass);
				tableView(frame, cardPanel, cardLayout);
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(frame, e1.toString(), null, JOptionPane.ERROR_MESSAGE, null);
			}
		});
		
		delSchema.addActionListener(e ->{
			StringBuilder delSchemaBuilder = new StringBuilder();
			delSchemaBuilder.append("DROP DATABASE " + (String)schemaListView.getSelectedValue());
			
			int conf = 1;
			
			//array for options
			String[] confOptions = {"Yes", "No"};
			//ask if user wants to return to url entry
			conf = JOptionPane.showOptionDialog(frame, "Would you like to return to delete the database "
					+ (String)schemaListView.getSelectedValue() + "?", 
					"Exit", 0, 0, null, confOptions, confOptions[1]);
			
			if (conf == 0) {
				Statement stmt;
				try {
					stmt = con.createStatement();
					stmt.execute(delSchemaBuilder.toString());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(frame, e1.toString());
				}
				//get a result set to pass to the edit table method
				//data handling on the result set will be handled by the edit table method
				
			}
			
		});

		
		cardPanel.add(schemaPanel, SCHEMA_PANEL);
		cardLayout.show(cardPanel, SCHEMA_PANEL);
		frame.pack();
		frame.setVisible(true);
		centerWindow(frame);
	}
	
	
	//simple method for splitting the screen into X# of even rows/cols
	//int version
		public static int[] intSplitter(int numSplits, double numToSplit) {
			int[] splitArray = new int[numSplits];
			
			for (int i = 0; i < numSplits; i++) {
				splitArray[i] = (int)numToSplit/numSplits;
			}
			return splitArray;
		}
		
	
	//simple method for splitting the screen into X# of even rows/cols
		//double version
	public static double[] doubleSplitter(int numSplits, double numToSplit) {
		double[] splitArray = new double[numSplits];
		
		for (int i = 0; i < numSplits; i++) {
			splitArray[i] = numToSplit/numSplits;
		}
		
		
		return splitArray;
	}
	
	
	//method for grabbing screen size and basing window dimensions off it
	//returns the ideal window dimension
	//multiplier is the % of screen it should take up, as a decimal
	public static Dimension getIdealSize(double hMultiplier, double wMultiplier) {
		//get the window toolkit
		Toolkit tk = Toolkit.getDefaultToolkit();
		//get and store the screen sizes
		int xSize = ((int) tk.getScreenSize().getWidth());
		int ySize = ((int) tk.getScreenSize().getHeight());
		//ideal size for window is 75% of the screen in each direction
		Dimension idealSize = new Dimension((int)Math.round(xSize * wMultiplier), 
						(int)Math.round(ySize * hMultiplier));
		
		return idealSize;
	}
	
	//method for setting up the table view card
	public static void tableView(JFrame frame, JPanel cardPanel, CardLayout cardLayout) throws SQLException{
		//make a panel to hold the gridbag layout child
				JPanel card1 = new JPanel();
				
				//screen size grabbing has been moved to its own method
				Dimension idealSize = getIdealSize(.75, .9);

				
				
				//repacking for when we go back to this
				frame.pack();
				
				
				//set up a gridbaglayout for holding elements
				GridBagLayout gbl_newPanel = new GridBagLayout();
				//let the columns fill space
				gbl_newPanel.columnWidths = new int[]{166,166,166};
				//manually set the row heights
				gbl_newPanel.rowHeights = new int[]{450,50};
				//weight the columns evenly 
				//this distributes the space evenly amongst them
				gbl_newPanel.columnWeights = new double[]{1.0, 1, 1};
				//weight rows evenly
				gbl_newPanel.rowWeights = new double[]{1.0, 0.1};
				//set the panel layout on the holder panel to be the gridbaglayout
				card1.setLayout(gbl_newPanel);
				//add the card to the parent panel for the cardlayout
				//this makes the card accessible
				
				
				/*
				 * 
				 * ADDING BUTTONS AND ELEMENTS TO THE PANEL
				 */

				//create table button
				JButton createButton = new JButton("Create Table");
				
				//create new container to hold constraints for items in UI
				GridBagConstraints createButtonConstraints = new GridBagConstraints();
				createButtonConstraints.gridx = 0;
				createButtonConstraints.gridy = 1;
				createButtonConstraints.gridwidth = 1;
				createButtonConstraints.gridheight = 1;
				createButtonConstraints.fill = createButtonConstraints.HORIZONTAL;
				createButtonConstraints.insets = new Insets(0, 25, 0, 25);
				card1.add(createButton, createButtonConstraints);
				
				
				//edit table button
				JButton editButton = new JButton("Edit Table");
				//it is possible to use one GBC for multiple items; this does not work with WindowBuilder, however, so I'm opting to use seperate for each
				GridBagConstraints editButtonConstraints = new GridBagConstraints();
				editButtonConstraints.gridx = 1;
				editButtonConstraints.gridy = 1;
				editButtonConstraints.gridwidth = 1;
				editButtonConstraints.gridheight = 1;
				editButtonConstraints.fill = editButtonConstraints.HORIZONTAL;
				editButtonConstraints.insets = new Insets(0, 25, 0, 25);
				card1.add(editButton, editButtonConstraints);
				
				
				//delete table button
				JButton deleteButton = new JButton("Delete Table");
				GridBagConstraints deleteButtonConstraints = new GridBagConstraints();
				deleteButtonConstraints.gridx = 2;
				deleteButtonConstraints.gridy = 1;
				deleteButtonConstraints.gridwidth = 1;
				deleteButtonConstraints.gridheight = 1;
				deleteButtonConstraints.fill = deleteButtonConstraints.HORIZONTAL;
				deleteButtonConstraints.insets = new Insets(0, 25, 0, 25);
				card1.add(deleteButton, deleteButtonConstraints);
				
				
				
				JScrollPane scrollPane = new JScrollPane();
				GridBagConstraints tableScrollPaneConstraints = new GridBagConstraints();
				tableScrollPaneConstraints.insets = new Insets(0, 0, 5, 5);
				tableScrollPaneConstraints.fill = GridBagConstraints.BOTH;
				tableScrollPaneConstraints.gridx = 0;
				tableScrollPaneConstraints.gridy = 0;
				tableScrollPaneConstraints.gridwidth = 3;
				card1.add(scrollPane, tableScrollPaneConstraints);
				
				
				
				//TODO: Hook the DLM into a MySQL Database
				
				// DLM lets us populate the list as necessary
				DefaultListModel listPopulator = new DefaultListModel();
				
				Vector<String> tableNames = getTables();
				
				
				
				//populate the DLM from an arraylist
				for (int i = 0; i < tableNames.size(); i++) {
					listPopulator.addElement(tableNames.get(i).toString());
				}
				
				
				
				
				
				
				//bind the list to the DLM
				JList list = new JList(listPopulator);
				//set the view on the scrollpane to the list
				scrollPane.setViewportView(list);
				
				
				
				
				/*
				 * 
				 * LISTENER FOR CREATE TABLE BUTTON
				 * 
				 */
				
				createButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JOptionPane errorPane = new JOptionPane();
							try {
								//create option panes
								JOptionPane tableNameDialogue = new JOptionPane();
								
								//create a string for holding table name
								String tableNameString;
								//pop a dialogue box prompting for table name entry
								tableNameString = tableNameDialogue.showInputDialog(frame, "Please enter the table name", "Table Name Entry", JOptionPane.PLAIN_MESSAGE);
								//if null, handle
								//handler will pop an error message then go pack to the last window
								if (tableNameString == null || tableNameString.isEmpty()) {
									errorPane.showMessageDialog(null, "A table name was not entered", "Error", JOptionPane.WARNING_MESSAGE);
								//if not null, send to next card
								} else {
									tableCreationView(frame, cardPanel, cardLayout, tableNameString, listPopulator, idealSize);
								}
								//catch mysql exceptions
							} catch (SQLException e1) {
								errorPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
						}
					}
				});
				
				
				editButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String selectedTable;
						JOptionPane errorPane = new JOptionPane();
						
						selectedTable = (String) list.getSelectedValue();
						
						if (selectedTable != null) {
							try {
							Statement stmt = con.createStatement();
							//get a result set to pass to the edit table method
							//data handling on the result set will be handled by the edit table method
							ResultSet rs = stmt.executeQuery("SELECT * FROM " + selectedTable);
							editTableView(frame, cardPanel, cardLayout, selectedTable, rs, idealSize);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								errorPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
							}
						} else {
							errorPane.showMessageDialog(null, "No table selected", "Error", JOptionPane.WARNING_MESSAGE);
						}
		
					}
				});
				
				deleteButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JOptionPane confirmationPane = new JOptionPane();
						String tableToDelete = (String) list.getSelectedValue();
						JOptionPane errorPane = new JOptionPane();
						
						//make sure a table is selected
						if (tableToDelete != null) {
							int deleteConfirmation = confirmationPane.showConfirmDialog(null, "Would you like to delete the table " + tableToDelete + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
							
							if (deleteConfirmation == 0 && tableToDelete != null) {
								try {
									deleteTable(listPopulator, tableToDelete);
								} catch (SQLException e1) {
									errorPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
								}
							}
						} else {
							//error if table is not selected
							confirmationPane.showMessageDialog(null, "No table selected", "Error", JOptionPane.WARNING_MESSAGE);
						}
					}
				});
				
				cardPanel.add(card1, TABLES_PANEL);
				
				
				cardLayout.show(cardPanel, TABLES_PANEL);
				frame.pack();
				frame.setVisible(true);
				centerWindow(frame);
	}
	
	
	//MIGHT NEED TO HAVE JFRAME AS A PARAMETER?
	public static void editTableView(JFrame frame, JPanel cardPanel, CardLayout cardLayout,
			String tableName, ResultSet rs, Dimension screenSize) throws SQLException {
		
		JPanel editTablePanel = new JPanel();
		
		//layout for panel
		GridBagLayout editTableLayout = new GridBagLayout();
		editTablePanel.setLayout(editTableLayout);
		//add panel w/ layout to card layout
		cardPanel.add(editTablePanel, EDIT_TABLE_PANEL);
		
		JLabel tableNameLabel = new JLabel(tableName);
		GridBagConstraints tableNameLabelConstraints = new GridBagConstraints();
		tableNameLabelConstraints.gridx = 0;
		tableNameLabelConstraints.gridy = 0;
		tableNameLabelConstraints.gridwidth = 4;
		editTablePanel.add(tableNameLabel, tableNameLabelConstraints);
		
		JScrollPane tablePane = new JScrollPane();
		GridBagConstraints tablePaneConstraints = new GridBagConstraints();
		tablePaneConstraints.weightx = 1.0;
		tablePaneConstraints.weighty = 1.0;
		tablePaneConstraints.gridx = 0;
		tablePaneConstraints.gridwidth = 4;
		tablePaneConstraints.gridy = 1;
		//set the preferred screen size to 80% of the users screen
		tablePane.setPreferredSize(screenSize);
		editTablePanel.add(tablePane, tablePaneConstraints);
		
		
		
		JButton createEntryButton = new JButton("Create Entry");
		GridBagConstraints createButtonConstraints = new GridBagConstraints();
		createButtonConstraints.weightx = 1.0;
		createButtonConstraints.weighty = .1;
		createButtonConstraints.gridy = 2;
		createButtonConstraints.gridx = 0;
		createButtonConstraints.gridwidth = 1;
		editTablePanel.add(createEntryButton, createButtonConstraints);
		
		
		
		JButton editEntryButton = new JButton("Edit Entry");
		GridBagConstraints editButtonConstraints = new GridBagConstraints();
		editButtonConstraints.weightx = 1.0;
		editButtonConstraints.weighty = .1;
		editButtonConstraints.gridy = 2;
		editButtonConstraints.gridx = 1;
		editButtonConstraints.gridwidth = 1;
		editTablePanel.add(editEntryButton, editButtonConstraints);
		
		
		
		JButton deleteEntryButton = new JButton("Delete Entry");
		GridBagConstraints deleteButtonConstraints = new GridBagConstraints();
		deleteButtonConstraints.weightx = 1.0;
		deleteButtonConstraints.weighty = .1;
		deleteButtonConstraints.gridy = 2;
		deleteButtonConstraints.gridx = 2;
		deleteButtonConstraints.gridwidth = 1;
		editTablePanel.add(deleteEntryButton, deleteButtonConstraints);
		
		JButton cancelButton = new JButton("Cancel");
		GridBagConstraints cancelButtonConstraints = new GridBagConstraints();
		cancelButtonConstraints.weightx = 1.0;
		cancelButtonConstraints.weighty = .1;
		cancelButtonConstraints.gridy = 2;
		cancelButtonConstraints.gridx = 3;
		cancelButtonConstraints.gridwidth = 1;
		editTablePanel.add(cancelButton, cancelButtonConstraints);
		
		
		
		DefaultTableModel displayedTable = buildTableModel(rs);
		
		JTable tableView = new JTable(displayedTable);
		tableView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
				//uses a custom class from 
				//https://tips4java.wordpress.com/2008/11/10/table-column-adjuster/
				//this autoadjusts table size and includes the header
				TableColumnAdjuster tableAdjuster = new TableColumnAdjuster(tableView);
				
				tableAdjuster.adjustColumns();
				tableAdjuster.setColumnHeaderIncluded(true);
				 
		
		editEntryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = tableView.getSelectedRow();
				int selectedColumn = tableView.getSelectedColumn();
				Object selectedColName = tableView.getColumnModel().getColumn(selectedColumn).getHeaderValue();
				String primaryKeyCol = null;
				Statement stmt = null;
				
				String newValue = null;
				
				JOptionPane editPane = new JOptionPane();
				
				//get the new cell value
				newValue = (String) editPane.showInputDialog(null, "Enter new value for " + selectedColName + " at row " + (selectedRow + 1), "Edit Cell", JOptionPane.QUESTION_MESSAGE, null, null, tableView.getValueAt(selectedRow, selectedColumn));
					
			
			//if new value entry is cancelled, do nothing
			if (newValue != null) {

				
				
				int keyColIndex = -1;

				try {
					keyColIndex = getPrimaryKeyColumnIndexForTable(tableName);
					primaryKeyCol = getPrimaryKeyColumnsForTable(tableName);
				} catch (SQLException e2) {
					editPane.showMessageDialog(null, e2.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
				}
				
				
				Object primaryKey = tableView.getValueAt(selectedRow, keyColIndex);
				
				try {
					//create statement
					stmt = con.createStatement();
				} catch (SQLException e1) {
					//Auto-generated catch block
					editPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
				}
				
				StringBuilder updateString = new StringBuilder();
				
				if (primaryKeyCol != null && primaryKey != null && selectedColName != null && newValue != null) {
					updateString.append("UPDATE " + tableName + " SET " + selectedColName + " = '" + newValue + "' WHERE " + primaryKeyCol + " = " + primaryKey);
						//make sure statement isnt null
						if (stmt != null) {
							try {
								//prepared statements help prevent injector attacks
								PreparedStatement updateStringStatement = con.prepareStatement(updateString.toString());
								updateStringStatement.executeUpdate();
								displayedTable.setValueAt(newValue, selectedRow, selectedColumn);
							} catch (SQLException e1) {
								//Auto-generated catch block
								editPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
							}
						
							
						
						} else {
							editPane.showMessageDialog(null, "Statement was null", "Error", JOptionPane.WARNING_MESSAGE);
						}
				} else {
					editPane.showMessageDialog(null, "A necessary value was null", "Error", JOptionPane.WARNING_MESSAGE);
				}

				
			}
			}
		});
		
		createEntryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			JOptionPane newValuePane = new JOptionPane();
			Vector<String> columnValues = new Vector<String>();
			boolean cancelled = false;
				
			
				
				
				try {
				PreparedStatement queryStatement = con.prepareStatement("SELECT * FROM " + tableName + " WHERE 1<0");
				ResultSet newRowSet =  queryStatement.executeQuery();
				ResultSetMetaData rsmd = newRowSet.getMetaData();
				
				//map for holding added columns
				Map addedColumnsMap = new LinkedHashMap<Object, String>();
				
				
				//get column names from meta data
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String value;
					//columnNames.add(rsmd.getColumnLabel(i));
					//get values for row via input dialogs
					do {
						value = newValuePane.showInputDialog(null, "Enter new value for " + rsmd.getColumnLabel(i), "Add Row", JOptionPane.QUESTION_MESSAGE);
						if (value != null && !value.isEmpty()) {
							//printing is for testing
							//System.out.println(rsmd.getColumnLabel(i) + " : " + value);
							columnValues.add(value);
							//addedColumns.add(rsmd.getColumnLabel(i));
							addedColumnsMap.put(rsmd.getColumnLabel(i), value);
						} else if (value == null){
							cancelled = true;
							break;
						} else if (value.isEmpty()) {
							newValuePane.showMessageDialog(null, "Value was blank, please reenter", "Error", JOptionPane.QUESTION_MESSAGE);
						}
					} while (value.isEmpty());
					
					if (value == null) {
						break;
					}

				}
				
				
				
				//make sure values have been entered for some columns
				if (addedColumnsMap.size() != 0 && columnValues.size() != 0 && cancelled == false) {
					//get all rows and columns from table
					PreparedStatement newRowQuery = con.prepareStatement("SELECT * FROM " + tableName);
					ResultSet newRowPopulator = newRowQuery.executeQuery();
					ResultSetMetaData nrsmd = newRowPopulator.getMetaData();
					
					
					
					//SB for building a query
					StringBuilder addRowToTable = new StringBuilder();
					//start sql string for inserting into database
					addRowToTable.append("INSERT INTO " + tableName + " (");
					
					//loop over column count from metadata
					for (int i = 1; i <= nrsmd.getColumnCount(); i++) {
						//if the map contains an item for that column
						if (addedColumnsMap.containsKey(nrsmd.getColumnLabel(i))) {
								//add to query
								addRowToTable.append(nrsmd.getColumnLabel(i) + ", ");
								//here for testing
								//System.out.println(addRowToTable.toString());
						}
					}
					
					
					
					
					
					
					while (addRowToTable.charAt(addRowToTable.length() -1) == ',' || addRowToTable.charAt(addRowToTable.length() -1) == ' ') {
					//if (addRowToTable.charAt(sbLength) == ',') {
						addRowToTable.deleteCharAt(addRowToTable.length()-1);
					//}
					}
					
			
					
					addRowToTable.append(")");
					//this is for testing
					//System.out.println(addRowToTable.toString());
					
					
					addRowToTable.append(" VALUES (");
					//iterate over the values to be added
					//similar method to adding column names
					for (int i = 1; i <= nrsmd.getColumnCount(); i++) {
						if (addedColumnsMap.containsKey(nrsmd.getColumnLabel(i))) {
							addRowToTable.append("' " + addedColumnsMap.get(nrsmd.getColumnLabel(i)) + "', ");
						}
					}
					
					while (addRowToTable.charAt(addRowToTable.length() -1) == ',' || addRowToTable.charAt(addRowToTable.length() -1) == ' ') {
						//if (addRowToTable.charAt(sbLength) == ',') {
							addRowToTable.deleteCharAt(addRowToTable.length()-1);
						//}
						}
					
					
					addRowToTable.append(");");
					//here for testing
					//System.out.println(addRowToTable.toString());

					System.out.println(addRowToTable.toString());
					PreparedStatement addRowStmt = con.prepareStatement(addRowToTable.toString());
					addRowStmt.executeUpdate();
					
					//add the entered values to the object that updates the displayed table, providing the sql statement doesnt fail
					displayedTable.addRow(columnValues);
				//if there's no values entered but there are columns -- ie. if the okay button was pressed repeatedly with no input
				} else  if (addedColumnsMap.size() == 0){
					newValuePane.showMessageDialog(null, "No column values were entered", "Error", JOptionPane.WARNING_MESSAGE);
				//if the cancel button is pressed, do nothing
				} else {
					
				}

				} catch (SQLException e1) {
					//pop error if exception
					newValuePane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
				}
				
				
				
				
				
				
			}
		});
		
		
		
		deleteEntryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringBuilder deleteString = new StringBuilder();
				int selectedRow = tableView.getSelectedRow();
				int selectedColumn = tableView.getSelectedColumn();
				String primaryKeyCol = null;
				int keyColIndex = -1;
				JOptionPane errorPane = new JOptionPane();
				
				//make sure a row and column is selected
				//it should be impossible to have one without the other, but you can never be too careful...
				if (selectedRow != -1 && selectedColumn != -1) {
					Object selectedColName = tableView.getColumnModel().getColumn(selectedColumn).getHeaderValue();
				}
				
				

				try {
					keyColIndex = getPrimaryKeyColumnIndexForTable(tableName);
					primaryKeyCol = getPrimaryKeyColumnsForTable(tableName);
				} catch (SQLException e2) {
					// pop error if exception
					errorPane.showMessageDialog(null, e2.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
				}
				
				
				Object primaryKey = tableView.getValueAt(selectedRow, keyColIndex);
				
				JOptionPane confirmationPane = new JOptionPane();
				
				if (selectedRow != -1) {
					int deleteConfirmation = confirmationPane.showConfirmDialog(null, "Would you like to delete the row at key " + tableView.getValueAt(selectedRow, selectedColumn) + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
						if (deleteConfirmation == 0) {
							
							StringBuilder deletionString = new StringBuilder();
							deletionString.append("DELETE FROM " + tableName + " WHERE " + primaryKeyCol + " = " + primaryKey);
							
							try {
								PreparedStatement deleteRowStatement = con.prepareStatement(deletionString.toString());
								deleteRowStatement.executeUpdate();
								deleteRow(displayedTable, selectedRow);
							} catch (SQLException e3) {
								confirmationPane.showMessageDialog(null, e3.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
							}
						}
				} else {
					confirmationPane.showMessageDialog(null, "No row selected", "Error", JOptionPane.WARNING_MESSAGE);
				}
				
				
				
			}
		});
		
		
		//listener for cancel button
				//cancel button goes back to last window
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JOptionPane errorPane = new JOptionPane();
						try {
							//get rid of preferred size settings when changing panels
							tablePane.setPreferredSize(null);
							tableView(frame, cardPanel, cardLayout);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							errorPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
						}
					}
				});
		
		
		
		
		
		//make the table uneditable via double click
		tableView.setDefaultEditor(Object.class, null);
		
		
		tablePane.setViewportView(tableView);
		
		
		cardLayout.show(cardPanel, EDIT_TABLE_PANEL);
		frame.pack();
		centerWindow(frame);
	}
	
	public static void deleteRow(DefaultTableModel tableView, int selectedRow) {
		
	}
	
	
	//method idea from https://stackoverflow.com/questions/21328371/get-primary-key-column-from-resultset-java
	//my version of the method assumes that there's only one primary key and only one table selected
	//this version of the method returns the name
	public static String getPrimaryKeyColumnsForTable(String tableName) throws SQLException {
		String pkColumnName = null;
		
	    try(ResultSet pkColumns= con.getMetaData().getPrimaryKeys(null,null,tableName);) {
	      while(pkColumns.next()) {
	        pkColumnName = pkColumns.getString("COLUMN_NAME");
	      }
	    }
	    	return pkColumnName;
	    }
	
	
	//this version of the method returns the index of the primary key column
	 public static int getPrimaryKeyColumnIndexForTable(String tableName) throws SQLException {
		 	Integer primaryKeyPosition = -1;
		 
		    try(ResultSet primaryKeyColumns= con.getMetaData().getPrimaryKeys(null,null,tableName);) {
		      while(primaryKeyColumns.next()) {
		        primaryKeyPosition = primaryKeyColumns.getInt("KEY_SEQ");
		      }
		    }
		    //make sure to subtract 1 to avoid off by 1 error
		      return (primaryKeyPosition - 1);
		    }
	
	//method for creating and opening the table creation panel
	//takes the cardpanel, the layout, and the table name
	public static void tableCreationView(JFrame frame, JPanel cardPanel, CardLayout cardLayout, String tableName, DefaultListModel tableList, Dimension screenSize) throws SQLException {
		JPanel tableCreationPanel = new JPanel();
		
		
		
		//create a layout for the panel
		GridBagLayout tableCreationLayout = new GridBagLayout();
		
		
		//set the panel layout
		tableCreationPanel.setLayout(tableCreationLayout);
		//add the panel w/ layout with a tag for the card layout
		cardPanel.add(tableCreationPanel, TABLE_CREATION_PANEL);
	
		
		//create the text for the top of window
		JLabel newTableLabel = new JLabel(tableName);
		GridBagConstraints newTableLabelConstraints = new GridBagConstraints();
		newTableLabelConstraints.gridx = 0;
		newTableLabelConstraints.gridy = 0;
		newTableLabelConstraints.gridwidth = 2;
		tableCreationPanel.add(newTableLabel, newTableLabelConstraints);
		
		
		//create the scroll pane that will hold the entered fields
		JScrollPane fieldsPane = new JScrollPane();
		GridBagConstraints fieldsPaneConstraints = new GridBagConstraints();
		fieldsPaneConstraints.gridx = 0;
		fieldsPaneConstraints.gridy = 1;
		fieldsPaneConstraints.gridwidth = 3;
		fieldsPaneConstraints.weightx = 1.0;
		fieldsPaneConstraints.weighty = 1.0;
		fieldsPane.setPreferredSize(screenSize);
		fieldsPaneConstraints.fill = GridBagConstraints.BOTH;
		tableCreationPanel.add(fieldsPane, fieldsPaneConstraints);
		
		
		/*
		 * Create buttons and add constraints
		 * These will all be added to the GBL in the card
		 * 
		 */
		JButton addFieldButton = new JButton("Add Field");
		GridBagConstraints addButtonConstraints = new GridBagConstraints();
		addButtonConstraints.gridx = 0;
		addButtonConstraints.gridy = 2;
		addButtonConstraints.gridwidth = 1;
		addButtonConstraints.gridheight = 1;
		addButtonConstraints.weightx = 1.0;
		addButtonConstraints.weighty = .01;
		addButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
		addButtonConstraints.insets = new Insets(5, 10, 5, 10);
		tableCreationPanel.add(addFieldButton, addButtonConstraints);
		
		
		JButton removeFieldButton = new JButton("Remove Field");
		GridBagConstraints removeButtonConstraints = new GridBagConstraints();
		removeButtonConstraints.gridx = 1;
		removeButtonConstraints.gridy = 2;
		removeButtonConstraints.gridwidth = 1;
		removeButtonConstraints.gridheight = 1;
		removeButtonConstraints.weightx = 1.0;
		removeButtonConstraints.weighty = .01;
		removeButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
		removeButtonConstraints.insets = new Insets(5, 10, 5, 10);
		tableCreationPanel.add(removeFieldButton, removeButtonConstraints);
		
		JButton createTableButton = new JButton("Create Table");
		GridBagConstraints createButtonConstraints = new GridBagConstraints();
		createButtonConstraints.gridx = 0;
		createButtonConstraints.gridy = 3;
		createButtonConstraints.gridwidth = 1;
		createButtonConstraints.gridheight = 1;
		createButtonConstraints.weightx = 1.0;
		createButtonConstraints.weighty = .01;
		createButtonConstraints.insets = new Insets(5, 10, 5, 10);
		createButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
		tableCreationPanel.add(createTableButton, createButtonConstraints);
		
		
		JButton cancelButton = new JButton("Cancel");
		GridBagConstraints cancelButtonConstraints = new GridBagConstraints();
		cancelButtonConstraints.gridx = 1;
		cancelButtonConstraints.gridy = 3;
		cancelButtonConstraints.gridwidth = 1;
		cancelButtonConstraints.gridheight = 1;
		cancelButtonConstraints.weightx = 1.0;
		cancelButtonConstraints.weighty = .01;
		cancelButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
		cancelButtonConstraints.insets = new Insets(5, 10, 5, 10);
		tableCreationPanel.add(cancelButton, cancelButtonConstraints);
		
		
	
		
		DefaultListModel fieldsListPopulator = new DefaultListModel();
		
		
		//create a new list with the model as the generator
		JList fieldsList = new JList(fieldsListPopulator);
		
		//set the viewport to hold the list
		fieldsPane.setViewportView(fieldsList);
		
		
		
		
		//storage for selected data type
		//StringBuilder dataTypeSelectedBuilder = new StringBuilder();
		
		//option pane for selecting data type
		JOptionPane dataTypeSelector = new JOptionPane();
		
		
		//option pane for getting size
		JOptionPane sizeEntry = new JOptionPane();
		
		
		//option pane declaration
		JOptionPane fieldEntry = new JOptionPane();
		
		
		JOptionPane errorPane = new JOptionPane();
		
		//stringbuilder for creating new field
		StringBuilder fieldBuilder = new StringBuilder();
		
		String selectedDataTypeString;
		
		
		
		//hashmap for storing columns
		//key will be the data type, value will be a list of columns for that key
		//using a custom class for this now
		//LinkedHashMap<String, List<String>> columnMap = new LinkedHashMap<String, List<String>>();
		
		
		
		List<ColumnInformation> columnList = new LinkedList<ColumnInformation>();
		
		//listener for add field button
		//TODO: Create a dialog to prompt for column data type along with size
		addFieldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//pop up a dialogue box to prompt for input and a dialogue for input type
				//input dialogue tutorial @ https://mkyong.com/swing/java-swing-joptionpane-showinputdialog-example/
				
				//creating a ColumnInformation instance to store data
				ColumnInformation newColumn = new ColumnInformation();
				
				//get the data type from a dropdown
				//this casts the object from the dialogue to a string and stores it
				String dataTypeSelected = (String)dataTypeSelector.showInputDialog(null, "Please select a data type for the column", "Column Data Type", JOptionPane.QUESTION_MESSAGE, null, columnOptions, columnOptions[1]);
				if (dataTypeSelected == null) {
					dataTypeSelected = "";
				}
				newColumn.setDataType(dataTypeSelected);
				
				//variable for loop
				String dataSize = "";
				
				
				
				//need to do a seperate case for different data types
				
				if (!(dataTypeSelected.equals("")) && dataTypeSelected.equalsIgnoreCase("DECIMAL") || dataTypeSelected.equalsIgnoreCase("FLOAT")
						|| dataTypeSelected.equalsIgnoreCase("BIT") || dataTypeSelected.equalsIgnoreCase("CHAR") 
						|| dataTypeSelected.equalsIgnoreCase("VARCHAR") || dataTypeSelected.equalsIgnoreCase("BINARY")
						|| dataTypeSelected.equalsIgnoreCase("VARBINARY")) {
					do {
						//prompt for data size entry
						dataSize = sizeEntry.showInputDialog(null, "Please Enter Data Type Size (0-" + getSize(dataTypeSelected) + ")", "Data Size", JOptionPane.PLAIN_MESSAGE);
						//check to make sure the data is numeric
						//if not pop an error
						if (dataSize.matches("^[a-zA-Z]*$") && dataSize != null) {
							errorPane.showMessageDialog(null, "That doesn't seem to be a number", "Error", JOptionPane.WARNING_MESSAGE);
						} else if (Integer.parseInt(dataSize) > Integer.parseInt(getSize(dataTypeSelected)) && dataSize != null){
							errorPane.showMessageDialog(null, "That seems to be out of range", "Error", JOptionPane.WARNING_MESSAGE);
						}
					//as long as the string contains non-numeric data, keep prompting
					} while ((dataSize.matches("^[a-zA-Z]*$") || Integer.parseInt(dataSize) > Integer.parseInt(getSize(dataTypeSelected))) && dataSize != null);
					//set the size field on the column class
					newColumn.setSize(Integer.parseInt(dataSize));
				}
				
				
				
				//get the name of the column if data type has been selected
				if (dataTypeSelected != "" && !dataTypeSelected.isEmpty()) {
					String dataEntryString;
						 dataEntryString = fieldEntry.showInputDialog(frame, "Please enter a field", "Field Entry", JOptionPane.PLAIN_MESSAGE);
						//if input isnt null, append to stringbuilder and populate list with it
						//otherwise do nothing
						if (dataEntryString != null && !dataEntryString.isEmpty()) {
							newColumn.setName(dataEntryString);
							fieldBuilder.append(dataEntryString);
							fieldsListPopulator.addElement(fieldBuilder.toString());
							//wipe the stringbuilder
							fieldBuilder.setLength(0);
							//update the list being displayed
							fieldsList.repaint();
							columnList.add(newColumn);
						}  else if (dataEntryString == null) {
							//empty to do nothing
						} else {
							errorPane.showMessageDialog(null, "No field name entered", "Error", JOptionPane.WARNING_MESSAGE);
						}
				}
		}
	});
		
		
		//listener for remove button
		//logic to remove fields
		removeFieldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fieldsList.getSelectedIndex() != -1) {
					int selectedIndex;
					selectedIndex = fieldsList.getSelectedIndex();
					fieldsListPopulator.remove(selectedIndex);
					fieldsList.repaint();
				}
			}
		});
		
		
		//listener for cancel button
		//cancel button goes back to last window
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					fieldsPane.setPreferredSize(null);
					tableView(frame, cardPanel, cardLayout);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					errorPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		
		createTableButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					createTable(columnList, tableName);
					//this adds the name of the newly added table to the list to populate the table view with
					//this is a shortcut -- we could repopulate the list from the schema each time, but this does the same thing more simply -- and it should be impossible for a table to be added here without being added to the db
					tableList.addElement(tableName);
					fieldsPane.setPreferredSize(null);
					cardLayout.show(cardPanel, TABLES_PANEL);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					errorPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
				}	
			}
		});
		
		
		
		//show the correct card
		cardLayout.show(cardPanel, TABLE_CREATION_PANEL);
		//pack the frame
		frame.pack();
		//center the window
		centerWindow(frame);
	}
	
	

	//when testing for data types, we do not need to worry about extraneous data 
	//for example, 23.35 will become 23 if inserted into an INTEGER column
	
	
	
	
	public static void deleteTable(DefaultListModel tableList, String tableToDelete) throws SQLException {
		String tableDrop;
		Statement stmt = con.createStatement();
		
		StringBuilder deleteStatement = new StringBuilder();
		
		JOptionPane successBox = new JOptionPane();
		
		deleteStatement.append("DROP TABLE ");
		deleteStatement.append(tableToDelete);
		
		stmt.executeUpdate(deleteStatement.toString());
		
		for (int i = 0; i < tableList.size(); i++) {
			if (tableList.get(i).equals(tableToDelete)) {
				tableList.remove(i);
				break;
			}
		}
		
		successBox.showMessageDialog(null, "Table '" + tableToDelete + "' was deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
		
		
		
	}
	
	
	//simple method for testing whether a string is a valid number
	public static boolean isNumber(String input) {
		boolean dataTypeCorrect = false;
		
		try {
			Double.parseDouble(input);
			dataTypeCorrect = true;
		} catch (NumberFormatException e) {
			dataTypeCorrect = false;
		}
		
		return dataTypeCorrect;
	}
	
	
	//method for getting sizes to display
	//sizes from:
	//https://www.w3resource.com/mysql/mysql-data-types.php
	//https://dev.mysql.com/doc/refman/8.0/en
	//https://www.w3schools.com/mysql/mysql_datatypes.asp
	public static String getSize(String selectedDataType){
		String sizeHolder = "-1";
		
		//TODO handle decimal?
		if (selectedDataType.equalsIgnoreCase("DECIMAL")) {
			sizeHolder = "65";
		} else if (selectedDataType.equalsIgnoreCase("FLOAT")) {
			sizeHolder = "53";
		} else if (selectedDataType.equalsIgnoreCase("BIT")) {
			sizeHolder = "64";
		} else if (selectedDataType.equalsIgnoreCase("CHAR")) {
			sizeHolder = "255";
		} else if (selectedDataType.equalsIgnoreCase("VARCHAR")) {
			sizeHolder = "65535";
		} else if (selectedDataType.equalsIgnoreCase("BINARY")) {
			sizeHolder = "255";
		} else if (selectedDataType.equalsIgnoreCase("VARBINARY")) {
			sizeHolder = "65535";
		}
		return sizeHolder;
	}
	
	
	
	//simple method to test if the string input is in binary (ie. 0 or 1)
	public static boolean bitTest(String input) {
		boolean dataTypeCorrect = false;
		
		if (input.equals("0") || input.equals("1")) {
			dataTypeCorrect = true;
		}
		
		return dataTypeCorrect;
	}
	
	
	
	//simple method to clip strings down to a predetermined size
	//this is important to make sure the strings fit into the mysql columns
	public static String stringClipper(String input, int size) {
		StringBuilder clippedString = new StringBuilder();
		clippedString.append(input);
		clippedString.setLength(size);
		
		return clippedString.toString();
	}
	
	


	

	//simple function for checking index of array where string occurs
	public static int checkIndex(String[] dataTypes, String toMatch) {
		int index = -1;
		
		for (int k = 0; k < dataTypes.length; k++) {
			if (dataTypes[k].equalsIgnoreCase(toMatch)) {
				index = k;
				break;
			}
		}
		return index;
	}
	
	
	
	//method for creating table
	//using a for loop to programmatically create the MySQL statement from the ColumnList class objects
	//using a custom class to store columns
	//programmatically creates sql statements to add tables
	public static void createTable(List<ColumnInformation> columnList, String tableName) throws SQLException{
		
		StringBuilder tableCreationString = new StringBuilder();
		
		tableCreationString.append("CREATE TABLE ");
		tableCreationString.append(tableName);
		tableCreationString.append(" (");
		
		for (int i = 0; i < columnList.size(); i++) {
			tableCreationString.append(columnList.get(i).getName());
			tableCreationString.append(" ");
			tableCreationString.append(columnList.get(i).getDataType());
			
			
			if (columnList.get(i).getDataType().equalsIgnoreCase("DECIMAL") || columnList.get(i).getDataType().equalsIgnoreCase("FLOAT")
					|| columnList.get(i).getDataType().equalsIgnoreCase("BIT") || columnList.get(i).getDataType().equalsIgnoreCase("CHAR") 
					|| columnList.get(i).getDataType().equalsIgnoreCase("VARCHAR") || columnList.get(i).getDataType().equalsIgnoreCase("BINARY")
					|| columnList.get(i).getDataType().equalsIgnoreCase("VARBINARY")) {
			
						tableCreationString.append("(");
						tableCreationString.append(columnList.get(i).getSize());
						tableCreationString.append(")");
			}
			if (i < columnList.size() - 1) {
				tableCreationString.append(",");
			}
		}
		tableCreationString.append(");");
	
		
		//make the string from builder + execute statement
		String query = tableCreationString.toString();
		Statement stmt = con.createStatement();
		stmt.executeUpdate(query);
	}
	
	
	
	//method for getting column names and storing in a vector
	public static Vector<String> getColNames(String tableName) throws SQLException {
		Vector<String> columns = new Vector<String>();
		
		//this only works in a single schema database
		String databaseName = con.getCatalog();
		
		//create a query to fetch the table info
		String query = "SELECT * FROM " + tableName;
		
		//create a statement
		Statement stmt = con.createStatement();
		
		//get a result set
		ResultSet resultSet = stmt.executeQuery(query);
		
		//get the meta data off the result set
		ResultSetMetaData rsmd = resultSet.getMetaData();
		//variable for holding col count
		int colCount = rsmd.getColumnCount();
		
		//idea from https://stackoverflow.com/questions/696782/retrieve-column-names-from-java-sql-resultset
		//get column names and add to vector
		for (int i = 0; i <= colCount; i++) {
			String colName = rsmd.getColumnName(i);
			columns.add(colName);
		}
		
		return columns;
	}
	
	
	
	
	
	
	
	//method to pull table names from database
	//general method scheme from https://stackoverflow.com/questions/2780284/how-to-get-all-table-names-from-a-database
	//this method assumes the database has a single schema, and not multiple
	//a different method would have to be used for databases with multiple schema, as Connection.getCatalog(); only works properly on single schema databases
	public static Vector<String> getTables() throws SQLException {
		Vector<String> tables = new Vector<String>();
		
		//this fetches the name of the database for later use
		//this only works for databases with a single schema
		//could expand to include multiple schemas but this is modeled on single schema database
		String databaseName = con.getCatalog();
		//System.out.println(databaseName);
	
		//System.out.println(databaseName);
		
		//get the database metadata
		DatabaseMetaData metaData = con.getMetaData();
		
		//first parameter specifies the database
		ResultSet rs = metaData.getTables(databaseName, null, "%", null);
		
		
		while (rs.next()) {
			//column 3 is the TABLE_NAME in the metadata
			tables.add(rs.getString(3));
		}
		return tables;
	}
	
	
	
	//needed to look up method design 
	//originally i was trying to populate a table manually from dictionary sets
	//java has a ResultSet object designed specifically for this so you dont need to use dictionary sets
	//the idea for this method for building a table model off a result set was found at:
	//https://stackoverflow.com/questions/10620448/how-to-populate-jtable-from-resultset
	//I've modified it from the original to suit my own uses 
	public static DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException{
		
		ResultSetMetaData metaData = resultSet.getMetaData();
		
		//vector to hold column names
		Vector<String> columns = new Vector<String>();
		
		//get the # of columns in the table
		int numOfColumns = metaData.getColumnCount();
		
		//iterate over the columns and add each column name to the vector
		//vectors are a legacy collection that functions similarly to an arraylist
		//vectors and arrays are the only things that can be used for table models
		for (int i = 1; i <= numOfColumns; i++) {
			columns.add(metaData.getColumnName(i));
		}
		
		//collecting the data from the table itself
		//vector to store the table data
		Vector<Vector<Object>> tableData = new Vector<Vector<Object>>();
		//iterate over as long as there's data left in the result set
		while (resultSet.next()) {
			Vector<Object> newVector = new Vector<Object>();
				//iterate over the columns
				//grab the data from the table for each columns
				for (int i = 1; i <= numOfColumns; i++) {
					newVector.add(resultSet.getObject(i));
				}
				//add the row data to the vector holding the table data
				tableData.add(newVector);
		}
	
		//return the model
		return new DefaultTableModel(tableData, columns);
	}

	
	//method for centering the window in the screen on startup
	//credit for this method to Donal @ https://stackoverflow.com/questions/144892/how-to-center-a-window-in-java
	public static void centerWindow(Window frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}
	
	
	
	
}

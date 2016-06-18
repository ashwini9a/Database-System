package Screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


public class TableName extends JFrame {

	private JPanel contentPane;
	public String tnm;
	protected boolean done = false;

	/**
	 * Launch the application.
	 */
	/**
	 * Create the frame.
	 */
	public TableName(String S,String OP) {
		
		
		super(S);
		System.out.println("operation: "+OP);
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{63, 118, 0, 121, 163, 0};
		gbl_contentPane.rowHeights = new int[]{65, 0, 121, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblTableName = new JLabel("Select Table :");
		lblTableName.setFont(new Font("Calibri", Font.BOLD, 20));
		GridBagConstraints gbc_lblTableName = new GridBagConstraints();
		gbc_lblTableName.fill = GridBagConstraints.BOTH;
		gbc_lblTableName.insets = new Insets(0, 0, 5, 5);
		gbc_lblTableName.gridx = 1;
		gbc_lblTableName.gridy = 1;
		contentPane.add(lblTableName, gbc_lblTableName);
		
		JComboBox<String> comboBox = new JComboBox<String>();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 2;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 3;
		gbc_comboBox.gridy = 1;
		contentPane.add(comboBox, gbc_comboBox);
		
		try {
			File file = new File("Data/TableIndex.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			
			comboBox.addItem("Choose");
			
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
				stringBuffer.append("\n");
				comboBox.addItem(line);
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		JButton btnOk = new JButton("Ok");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//if(textField.getText().isEmpty())
				tnm = comboBox.getSelectedItem().toString();
				if(comboBox.getSelectedItem().toString().equals("Choose"))
				{
					Object frame;
					JOptionPane.showMessageDialog(null,"Please select a Table","Error",JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					if(GlobalData.allTables.contains(tnm))
					{
						//tnm =textField.getText() ;						
						switch(OP)
						{
						case "Insert":
							InsertWindow IW = new InsertWindow("Insert Tuple",tnm);
							break;
						case "Delete":
							break;
						case "Update":
							//UpdateRecord updateRecord = new UpdateRecord(tnm);
							break;
						case "Search":
							System.out.println("case search");
							SearchScreen searchScreen = new SearchScreen(tnm);
							//System.out.println("i am here");
							//searchScreen.search(tnm);
							break;
						}
						dispose();
					}
					else
					{
						JOptionPane.showInputDialog(tnm, "No such Table");
					}
					
				}
				
			}
		});
		

		btnOk.setFont(new Font("Calibri", Font.BOLD, 20));
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.insets = new Insets(0, 0, 0, 5);
		gbc_btnOk.gridx = 2;
		gbc_btnOk.gridy = 2;
		contentPane.add(btnOk, gbc_btnOk);
		

	}
	/**
	 * @wbp.parser.constructor
	 */
	public TableName(String S){

		super(S);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 250);
		contentPane = new JPanel();
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTableName = new JLabel("Select table :");
		//lblTableName.setBounds(84, 63, 113, 18);
		lblTableName.setFont(new Font("Perpetua", Font.BOLD, 10));
		contentPane.add(lblTableName);
		
		JComboBox<String> comboBox = new JComboBox<String>();
		//comboBox.setBounds(196, 62, 177, 18);
		
		comboBox.addItem("Choose..");
		contentPane.add(comboBox);
		
		try {
			
			File file = new File("Data/TableIndex.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
				stringBuffer.append("\n");
				comboBox.addItem(line);
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		JButton btnOk = new JButton("Ok");
		//btnOk.setBounds(205, 128, 57, 26);
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//if(textField.getText().isEmpty())
				tnm = comboBox.getSelectedItem().toString();
				if(comboBox.getSelectedItem().toString().isEmpty())
				{
					Object frame;
					JOptionPane.showInputDialog(tnm,"Please select a Table");
				}
				else
				{
					
					if(GlobalData.allTables.contains(tnm))
					{
						//tnm =textField.getText() ;						
						try {
							GlobalData.deleteTableFile(tnm);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dispose();
					}
					else
					{
						JOptionPane.showInputDialog(tnm, "No such Table");
					}	
				}				
			}
		});
		

		btnOk.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		contentPane.add(btnOk);
		

	
	}

}

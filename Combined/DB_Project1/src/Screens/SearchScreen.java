package Screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.awt.event.ActionEvent;

public class SearchScreen {

	private JFrame frame;
	private String tableKey;
	

	/**
	 * Launch the application.
	 */
	public static void search(String tableName) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {			
					System.out.println("TableName: "+tableName);
					//SearchScreen window = new SearchScreen();
					//window.frame.setTitle(tableName);					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SearchScreen(String tableName) {
		initialize(tableName);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String tableName){
		
		System.out.println("inside initialize");
		
		frame = new JFrame();
		frame.setBounds(100, 100, 691, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle(tableName);
		
		
			
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 0, 492, 230);
		frame.getContentPane().add(scrollPane);
		
		String [] columnNames = {"ColumnName" , "Value", "Operator"};		
		DefaultTableModel model = new DefaultTableModel();
		model.setDataVector(new Object[][] {{"Choose", "", "Choose"}}, columnNames); 
		
		final JTable table = new JTable(model);
		
		String [] operators = {">","=","<"};
		JComboBox operatorBox = new JComboBox(operators);
		
		// combox for columnNames
		String [] attributeNames = getAttributes(frame.getTitle());	
		JComboBox attributeBox = new JComboBox(attributeNames);
		
		TableColumn colAttribute = table.getColumnModel().getColumn(0);
		colAttribute.setCellEditor(new DefaultCellEditor(attributeBox));
		
		TableColumn col = table.getColumnModel().getColumn(2);
		col.setCellEditor(new DefaultCellEditor(operatorBox));
		
		table.setBackground(Color.WHITE);

		//default values for dataType:
		table.setBounds(20, 53, 584, 100);		 
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		
		scrollPane.setViewportView(table);
		frame.getContentPane().add(scrollPane); 
		
		JButton btnAddColumn = new JButton("Add Filter");
		btnAddColumn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel dm = (DefaultTableModel)table.getModel();		
				Object [] rowData = {"Choose","","Choose"};
				dm.addRow(rowData);
			}
		});
		
		btnAddColumn.setBounds(546, 27, 119, 23);
		frame.getContentPane().add(btnAddColumn);
		
		JButton btnRemoveFilter = new JButton("Remove Filter");
		btnRemoveFilter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){		
				
				DefaultTableModel dm = (DefaultTableModel)table.getModel();
				int index = table.getSelectedRow();
				if(index == -1){				
					JOptionPane.showMessageDialog(null, "Please select a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
					
				}else
				   dm.removeRow(table.getSelectedRow());
				
			}
		});
		
		btnRemoveFilter.setBounds(546, 78, 119, 23);
		frame.getContentPane().add(btnRemoveFilter);
		
		
		JButton btnSearch = new JButton("Search");
		
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
			    //get all the rows data and save it in HashMap;
				HashMap<String,HashMap<String,String>> columnDataMap = new HashMap<String,HashMap<String,String>>();
				
				//boolean isKeySelected = false;
				
				for(int i = 0 ; i < table.getRowCount();i++){	
					
				      String attributeName = (String)table.getValueAt(i, 0);
				      String attributeValue = (String)table.getValueAt(i,1);
				      System.out.println("Attrbute Value: "+attributeValue);
					  String operator = (String)table.getValueAt(i,2);
					  
					  HashMap<String,String> attriMap = new HashMap<String,String>();
					  
					  attriMap.put("Value", attributeValue);
					  attriMap.put("Operator", operator);					  
					  columnDataMap.put(attributeName, attriMap);
					 
					}	
				
				System.out.println(columnDataMap);
				SearchDisplay display = new SearchDisplay(columnDataMap,tableName);
				
			}			
		});
		
		
		btnSearch.setBounds(546, 207, 119, 23);
		frame.getContentPane().add(btnSearch);		
		frame.setVisible(true);
		
	}
	
	
	public String [] getAttributes(String tableName){	
		
		JSONParser parser = new JSONParser();		
		Object obj;
		String [] colnm = null;
		
		try {
			FileReader f1 = new FileReader("Data/Metadata/"+tableName+".json");
			obj = parser.parse(f1);			
			JSONObject json = (JSONObject) obj;			
			JSONArray headers = (JSONArray) json.get("headers");
			colnm = new String[headers.size()];		
			
			for (int i = 0; i < headers.size(); i++){	
				
				Object temp = parser.parse(headers.get(i).toString());
				JSONObject temp1 = (JSONObject) temp;			
				colnm[i] = (String) temp1.get("Column Name");
				boolean isKey = (Boolean)temp1.get("Key");
				
				if(isKey){					
					tableKey = colnm[i];
				}
			}		
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		} catch(ParseException e){
			e.printStackTrace();
		}	
		return colnm;
	}

	
}

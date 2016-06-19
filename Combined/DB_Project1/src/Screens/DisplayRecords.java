package Screens;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

public class DisplayRecords {

	private JFrame frame;
	private JTable table;
	String [] columnNames;
	
	/**
	 * Launch the application.
	 */
	public  void displayRecords(String tableName) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					//DisplayRecords window = new DisplayRecords();					
					frame.setTitle(tableName);
					populateRecords("Data/Records/"+tableName+".json");
					//display all the records of this table	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
	public void populateRecords(String name){
		
		JSONParser parser = new JSONParser();		
		
		try{
			
			Object obj = parser.parse(new FileReader(name));
			JSONObject json = (JSONObject) obj;
			JSONArray headers = (JSONArray)json.get("Records");
			System.out.println(headers.toString());
			
		    if(headers.size() == 0){		    	
		    	JOptionPane.showMessageDialog(null, "No Records to Display", "Warning", JOptionPane.INFORMATION_MESSAGE);
		    	
		    }else{
		    	
		    	Object temp = parser.parse(headers.get(0).toString());
		    	JSONObject currJson = (JSONObject)temp;

		    	Set<String> keys = currJson.keySet();
		    	columnNames = keys.toArray(new String[keys.size()]);

		    	table = new JTable();
		    	//table.setAutoResizeMode(table.AUTO_RESIZE_ALL_COLUMNS); 
		    	//table.setBounds(427, 0, -424, 83);

		    	table.setModel(new DefaultTableModel(new Object[][] {},columnNames){				
		    		@Override
		    		public boolean isCellEditable(int row, int col){
		    			
		    			return false;
		    		}				
		    	});

		    	DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
		    	for(int i = 0 ; i < headers.size() ; i++){

		    		temp = parser.parse(headers.get(i).toString());
		    		currJson = (JSONObject)temp;

		    		Object [] data = new Object [columnNames.length];
		    		int index = 0;
		    		for(String key : keys){					
		    			data[index] =  currJson.get(key);			
		    			index++;
		    		}				
		    		tableModel.addRow(data);
		    	}

		    	table.setFillsViewportHeight(true);
		    	JScrollPane scrollPane = new JScrollPane(table);
		    	scrollPane.setBounds(37, 5, 468, 100);
		    	scrollPane.setViewportView(table);
		    	scrollPane.setPreferredSize(new Dimension(468,100));
		    	frame.getContentPane().add(scrollPane);

		    	this.frame.setVisible(true);
		  }	
		
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(ParseException e) {
			e.printStackTrace();
		} 	
	}

	/**
	 * Create the application.
	 */
	public DisplayRecords(String tnm,boolean flag) {
		initialize(tnm,flag);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String tnm,boolean flag) {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Delete");
		btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnNewButton.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent e){					  
				  //delete record from the table				  
				  if(table.getRowCount() > 0){
					   //check if a row is selected:
					   int selectedRow = table.getSelectedRow();
					   if(selectedRow == -1){
						    JOptionPane.showMessageDialog(null, "Please select a record to delete", "Error", JOptionPane.ERROR_MESSAGE);
						   						   
					   }else{
						   
						   DefaultTableModel dm = (DefaultTableModel)table.getModel();
						   System.out.println("Selected row: "+ table.getSelectedRow());
						   int index = table.getSelectedRow();
						  
						   int colIndex = -1;
						   String key = GlobalData.tablePrimaryKeyMap.get(tnm);
						   
						   for(int i = 0 ; i < dm.getColumnCount() ; i++){
							   if(key.equals(columnNames[i])){
							    	colIndex = i;
							    	break;
							     }
						   }
						   
						   String value = (String)dm.getValueAt(index, colIndex);						   
						   dm.removeRow(index);						  					   
						   System.out.println("ColIndex: "+colIndex);						   
						   String tableName =  frame.getTitle();
						   deleteRecordFromJson(key,value,tableName);
						 
					   }					  
				       //
				  }else{					  
					  //show dialog box
					  JOptionPane.showMessageDialog(null,"No records to delete","Warning",JOptionPane.WARNING_MESSAGE);					 					  
				  }
			}
		});
		if(flag)
		{
			JButton btnNewUpdate = new JButton("Update");
			btnNewUpdate.setFont(new Font("Times New Roman", Font.PLAIN, 13));
			btnNewUpdate.addActionListener(new ActionListener() {
				  public void actionPerformed(ActionEvent e){					  
					  //delete record from the table				  
					  if(table.getRowCount() > 0){
						   //check if a row is selected:
						   int selectedRow = table.getSelectedRow();
						   if(selectedRow == -1){
							    JOptionPane.showMessageDialog(null, "Please select a record to Update", "Error", JOptionPane.ERROR_MESSAGE);							  						   
						   }else{
							   
							   DefaultTableModel dm = (DefaultTableModel)table.getModel();
							   System.out.println("Selected row: "+ table.getSelectedRow());
							   int index = table.getSelectedRow();
							   
							   
							  
							   int colIndex = -1;
							   String key = GlobalData.tablePrimaryKeyMap.get(tnm);
							   JSONObject json =new JSONObject();
							   for(int i = 0 ; i < dm.getColumnCount() ; i++){
								   json.put(columnNames[i], (String)dm.getValueAt(index, i));
								   
								   if(key.equals(columnNames[i])){
								    	colIndex = i;
								    	break;
								     }
							   }
							   System.out.println("Colums and data"+json.toJSONString());
							   String value = (String)dm.getValueAt(index, colIndex);
//							   
//							   //dm.removeRow(index);						  
//							   //search for this key in json and delete						   
//							   System.out.println("ColIndex: "+colIndex);						   
//							   // get value at that row and column
//							   
//							   String tableName =  frame.getTitle();
							   updateRecordFromJson(key,value,tnm);
							 
						   }					  
					       //
					  }else{					  
						  //show dialog box
						  JOptionPane.showMessageDialog(null,"No records to Update","Warning",JOptionPane.WARNING_MESSAGE);					 					  
					  }
				}
			});
			btnNewUpdate.setBounds(300, 214, 89, 23);
			frame.getContentPane().add(btnNewUpdate);
	}
		btnNewButton.setBounds(468, 214, 89, 23);
		frame.getContentPane().add(btnNewButton);
	}
	
	
	protected void updateRecordFromJson(String key,String value,String tableName)
	{ 
		InsertWindow IW = new InsertWindow("Update Tuple : "+tableName, tableName,  this,key,value);
	}
	protected void deleteRecordFromJson(String key, String value, String tableName) {

		JSONParser parser = new JSONParser();	
		
		try{
			
			Object obj = parser.parse(new FileReader("Data/Records/"+tableName+".json"));
			JSONObject json = (JSONObject) obj;
			JSONArray headers = (JSONArray)json.get("Records");
			
			for(int i = 0 ; i < headers.size() ; i++){
				
				JSONObject temp = (JSONObject)parser.parse(headers.get(i).toString());
			     
				if(temp.containsValue(value)){					
					headers.remove(i);
				}
			}
			
			// write the file back to disk
			json.put("Records", headers);
			System.out.println(json.toJSONString());

			File file = new File("Data/Records/" + tableName + ".json");
			FileWriter fw = null;
			BufferedWriter bw = null;
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);

			bw.write(json.toJSONString());
			bw.flush();
			bw.close();
		
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		} catch(ParseException e){
			e.printStackTrace();
		} 	
		
	}
	
}
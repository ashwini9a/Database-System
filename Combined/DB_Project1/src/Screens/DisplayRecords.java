package Screens;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DisplayRecords {

	private JFrame frame;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void displayRecords(String tableName) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					DisplayRecords window = new DisplayRecords();					
					System.out.println(tableName);
					
					// populate records in the table
					window.frame.setTitle(tableName);
					window.populateRecords("Data/Records/"+tableName+".json");
					
					
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
			String [] columnNames = keys.toArray(new String[keys.size()]);
			
			table = new JTable();
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
			
			//frame.pack();
		  }	
		
		}catch (FileNotFoundException e) {
			//System.out.println("FileNotFoundException");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} 	
	}

	/**
	 * Create the application.
	 */
	public DisplayRecords() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		
	}
}

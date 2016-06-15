package cse.buffalo.edu.swing;

import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFrame;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
					
					window.populateRecords("test.json");
					
					window.frame.setVisible(true);
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
			
			Object obj = parser.parse(new FileReader("test.json"));
			JSONObject json = (JSONObject) obj;
			// System.out.println(json.toJSONString());
			JSONArray headers = (JSONArray)json.get("records");
			System.out.println(headers.toString());
			
			for(int i = 0 ; i< headers.size() ; i++){
				
				
				
				
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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		table = new JTable();
		table.setBounds(427, 0, -424, 83);
		frame.getContentPane().add(table);
		
	}
}

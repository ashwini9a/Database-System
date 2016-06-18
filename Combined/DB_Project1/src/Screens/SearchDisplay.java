package Screens;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class SearchDisplay extends JFrame{

	private JPanel contentPane;
    String [] columnNames = null;
    private JTable table;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//SearchDisplay frame = new SearchDisplay();
					;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public SearchDisplay(HashMap<String,HashMap<String,String>> dataMap, 
			boolean keySelected,String keyColumn,String tableName){
		
		System.out.println("In search display");
		
		
		setBounds(100, 100, 577, 374);
		contentPane = new JPanel();
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    this.setTitle(tableName);
	   
		//search for record in json
		List<HashMap<String,String>> results = null;
		if(keySelected)
			results = getResultsBasedOnKey(dataMap,keySelected,keyColumn,tableName);
		else{
		   //get records for based on all columns selected			
		   results = getResults(dataMap,tableName);
			
		}
		
		System.out.println("Results: "+results);		
		// save the results in Jtable
		table = new JTable();
		
		table.setModel(new DefaultTableModel(new Object[][]{},columnNames){				
    		@Override
    		public boolean isCellEditable(int row, int col){		
    			return false;
    		}				
    	});
		
		if(results.size() > 0){

			populateTable(results);
			table.setFillsViewportHeight(true);
			table.setAutoCreateRowSorter(true);
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 0, 541, 262);
			scrollPane.setViewportView(table);
	    	scrollPane.setPreferredSize(new Dimension(468,100));
			contentPane.add(scrollPane);
			setVisible(true);
			
			
		}else{
			
			JOptionPane.showMessageDialog(null, "No Records Match Criteria", "Message", JOptionPane.INFORMATION_MESSAGE);
			
		}
	}
	
	
	private List<HashMap<String, String>> getResults(HashMap<String, HashMap<String, String>> dataMap,
													 String tableName){
		
		System.out.println("In getResults without key");
		
		JSONParser parser = new JSONParser();		
		List<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
		
		try{

			Object obj = parser.parse(new FileReader("Data/Records/"+tableName+".json"));
			JSONObject json = (JSONObject) obj;
			JSONArray headers = (JSONArray)json.get("Records");
			
			Object temp = parser.parse(headers.get(0).toString());
			JSONObject currJson = (JSONObject)temp;
			 
			Set<String> keys = currJson.keySet();
			columnNames = keys.toArray(new String[keys.size()]);

			for(int i = 0 ; i < headers.size() ; i++){				
				currJson = (JSONObject)parser.parse(headers.get(i).toString());
				boolean match = true;
				
				for(Map.Entry<String, HashMap<String, String>> entryMap : dataMap.entrySet()){
					
					 String attributeName = entryMap.getKey();
					 String value = (String)currJson.get(attributeName);
					 
					 //value entered by user
					 HashMap<String,String> attributeMap = entryMap.getValue();
					 String searchValue = attributeMap.get("Value");
					 String operatorValue = attributeMap.get("Operator");
					 
					 if(!"Choose".equals(operatorValue)){
						 
						 if(">".equals(operatorValue)){
						    
							 BigDecimal searchVal = new BigDecimal(searchValue);
		                	 BigDecimal actualVal = new BigDecimal(value);
		                	 if(actualVal.compareTo(searchVal) <= 0){
		                		  match = false;
		                		  break;
		                	 }		  
							 
						 }else if("<".equals(operatorValue)){
							 
							 BigDecimal searchVal = new BigDecimal(searchValue);
		                	 BigDecimal actualVal = new BigDecimal(value);               		
		                	 if(actualVal.compareTo(searchVal) >= 0){
		                		  match = false;
		                		  break;	
		                	 }
		       		         
						 }else if("=".equals(operatorValue)){
							 
							 BigDecimal searchVal = new BigDecimal(searchValue);
		                	 BigDecimal actualVal = new BigDecimal(value); 
		                	 
							 if(searchVal.compareTo(actualVal) != 0){
								 match = false;
								 break;
								
							 }
								  
						 }
						 
					 }else{					
						 if(searchValue.equalsIgnoreCase(value)){						 
							 continue;
						 }else{
							 match = false;
							 break;
						 }
					 }
				}
      				
				if(match){					
					HashMap<String,String> recordMap = new HashMap<String,String>();
					for(String colName : keys){						
						recordMap.put(colName,(String)currJson.get(colName));						
					}					
					result.add(recordMap);
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return result;
	
	}

	private void populateTable(List<HashMap<String,String>> data){
		
		DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
		
		for(int i = 0 ; i < data.size() ; i++){

            HashMap<String,String> map = data.get(i);
            int index = 0;
            Object [] arr = new Object [columnNames.length];	
            for(String colName: columnNames){    		    
    		    arr[index] =  map.get(colName);			
    			index++;
    		 }
            
             tableModel.addRow(arr);
    		    
            }   
    }
		


	private List<HashMap<String, String>> getResultsBasedOnKey(HashMap<String, HashMap<String,String>> dataMap, 
										  boolean keySelected,String keyColumn,String tableName){
		
		JSONParser parser = new JSONParser();		
		List<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();		
		try{

			Object obj = parser.parse(new FileReader("Data/Records/"+tableName+".json"));
			JSONObject json = (JSONObject) obj;
			JSONArray headers = (JSONArray)json.get("Records");
			
			Object temp = parser.parse(headers.get(0).toString());
			JSONObject currJson = (JSONObject)temp;
			 
			Set<String> keys = currJson.keySet();
			columnNames = keys.toArray(new String[keys.size()]);

			for(int i = 0 ; i < headers.size() ; i++){					
				currJson = (JSONObject)parser.parse(headers.get(i).toString());
                String value = (String)currJson.get(keyColumn);
                				
                HashMap<String,String> searchMap = dataMap.get(keyColumn);               
                String searchValue = searchMap.get("Value");
                String operator = searchMap.get("Operator");
                
                // case 1 : operator not selected
                if("Choose".equals(operator)){
                	
                	//check if value is equal
                	if(value.equals(searchValue)){
                		
                		HashMap<String,String> recordMap = new HashMap<String,String>();
    					for(String colName : keys){
    						
    						recordMap.put(colName, (String)currJson.get(colName));						
    					}    					
    					result.add(recordMap);               	                		
                	}                           	
                }else{
                	boolean match = true;
                	if(">".equals(operator)){   
                		System.out.println(">");
                	    //check if value is greater than search value
                		BigDecimal searchVal = new BigDecimal(searchValue);
                		BigDecimal actualVal = new BigDecimal(value);               		
                		if(actualVal.compareTo(searchVal) <= 0)
                			 match = false;
                		
                	}else if("<".equals(operator)){

                		System.out.println("<");
                		
                		BigDecimal searchVal = new BigDecimal(searchValue);
                		BigDecimal actualVal = new BigDecimal(value);               		
                		if(actualVal.compareTo(searchVal) >= 0){
                			System.out.println("i am here"+actualVal.compareTo(searchVal));
                			match = false;
                		}		 
                		
                	}else{                              
                		if(operator.equals("=") && value.equalsIgnoreCase(searchValue))				
                		    match = true;
                		else
                		    match = false;    	
                	}  
                	
                	if(match){
	                	// createHashMap and save it in result;
	            		HashMap<String,String> recordMap = new HashMap<String,String>();
	            		for(String colName : keys){
	
	            			recordMap.put(colName, (String)currJson.get(colName));						
	            		}
	
	            		result.add(recordMap);           		
                	}
                }
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return result;
	}

}

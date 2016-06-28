package Screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JOptionPane;

public class Insert {
	
	private Table tableName;
	private List<Column> columns;
	private List<Object> values;
	
	public List<Object> getValues() {
		return values;
	}


	public void setValues(List<Object> values) {
		this.values = values;
	}


	public Table getTableName() {
		return tableName;
	}


	public void setTableName(Table tableName) {
		this.tableName = tableName;
	}


	public List<Column> getColumns() {
		return columns;
	}


	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}


	
	

	


	public void parse(String[] words, String statement) {
	
	    statement = statement.toLowerCase();
		
		if(words.length < 3){			
			JOptionPane.showMessageDialog(null, "Invalid INSERT Statement", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		System.out.println(statement.indexOf("into"));
			
	    if(statement.indexOf("into") == -1){
	    	
	    	JOptionPane.showMessageDialog(null, "Missing INTO keyword", "Error", JOptionPane.ERROR_MESSAGE);
	    	return;
	    	
	    	
	    }else if(statement.indexOf("values") == -1){
	    	
	    	JOptionPane.showMessageDialog(null, "Missing VALUES keyword", "Error", JOptionPane.ERROR_MESSAGE);
	    	return;
	    	
	    	
	    }
	    
	    // if reached here , means into and values are present;
	    // check if at correct position
	    
	    if(words.length > 1 && !words[2].equalsIgnoreCase("INTO")){
	    	
	    	JOptionPane.showMessageDialog(null, "Invalid location of INTO clause", "Error", JOptionPane.ERROR_MESSAGE);
	    	
	    }
	    
	    // INSERT INTO correct	    
	    //String tableName = statement.substring(beginIndex, endIndex);	    
	    // check if values is at position 4 or 5
	    if(words.length >=5  && (!words[3].equalsIgnoreCase("values") || !words[4].equalsIgnoreCase("values"))){	    	
	    	JOptionPane.showMessageDialog(null, "Invalid location of values clause", "Error", JOptionPane.ERROR_MESSAGE);   
	    	return;
	    }
	   
	    //fetch tableName
	    String tableName = words[2];
	    setTableName(new Table(tableName));
	    
	    //fetch columnNames if present
	     
	   if(words.length == 5){
		   
		   // colNames present
		   int startIndex = words[3].indexOf("(");
		   int endIndex = words[3].indexOf("");
		   
		   if(startIndex == -1 || endIndex == -1){
			   JOptionPane.showMessageDialog(null, "Missing ( or ) bracket", "Error", JOptionPane.ERROR_MESSAGE);  
			   return;
		    				   
		   }else{
			   
			   String columnNames = words[5].substring(startIndex, endIndex);			   
			   String [] colNames = columnNames.split(",");
			   for(String names : colNames){
				   
				   
			   }
			   
			   
		   }
		   
		   
		   
	   }
	   
	    
	    
	    
	    
	    
	    
	    
	     
		
		
		
		
		
	
		
	}

}

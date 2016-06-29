package Screens;

import java.util.List;

import javax.swing.JOptionPane;

public class Delete {	
	
	String tableName;
	List<String> whereConditions;
	boolean conditionFlag = false;
	
	
	
	
	public void parse(String sql) {
		
		
		// from clause exists
		int fromIndex = (sql.indexOf("FROM") != -1) ? sql.indexOf("FROM") : sql.indexOf("from");
		
		if(fromIndex == -1){			
			JOptionPane.showMessageDialog(null, "Invalid Syntax,Missing FROM clause", "Error", JOptionPane.ERROR_MESSAGE);
			return;	
		}
		
		// where clause exists
		
		int whereIndex = (sql.indexOf("WHERE") != -1) ? sql.indexOf("WHERE") : sql.indexOf("where");
		
		/*if(whereIndex == -1){			
			JOptionPane.showMessageDialog(null, "Invalid Syntax,Missing WHERE clause", "Error", JOptionPane.ERROR_MESSAGE);
			return;	
		}*/
		
		
	    // check if from is at position 2
		String [] tokens = sql.split("\\s+");
		
		// is * present
	    
		if(tokens.length > 2 && !tokens[1].equals("*") && !tokens[1].equalsIgnoreCase("from")){
			
			JOptionPane.showMessageDialog(null, "Invalid Syntax, position of from clause is wrong", "Error", JOptionPane.ERROR_MESSAGE);
			return;
					
		}else if(tokens.length > 2 && tokens[2].equals("*") && !tokens[3].equalsIgnoreCase("from")){
			
			JOptionPane.showMessageDialog(null, "Invalid Syntax, position of from clause is wrong", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// DELETE FROM correct
		
		// check if table name is present
		
		String tableName = "";
		
		if(whereIndex != -1){			
			tableName = sql.substring(fromIndex, whereIndex).trim().substring(4).trim();				
		}else if(tokens.length > 2){
			
		   tableName = tokens[2];
			
		}else{		
			JOptionPane.showMessageDialog(null, "Invalid Syntax,table name missing", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
		if("".equals(tableName.trim())){	
			
			JOptionPane.showMessageDialog(null, "Invalid Syntax,table name missing", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		this.tableName = tableName;
		
		// delete from table correct
		
		// check the where clause
		
		
		
		
		
		
	}
	
	
	
	
	

}

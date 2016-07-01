package Screens;

import java.util.List;

import javax.swing.JOptionPane;

public class Update {
	
	String tableName;
	List<String> columns;
	List<String> values;
	boolean conditionsPresent;
	
	public boolean parse(String[] tokens, String sql) {

		int setIndex = (sql.indexOf("SET") != -1) ? sql.indexOf("SET") : sql.indexOf("set");	
		
		boolean whereClausePresent = false;
		
		if(setIndex == -1){
			JOptionPane.showMessageDialog(null, "Invalid Syntax,Missing SET clause", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		
		if(tokens.length > 3 && !"SET".equalsIgnoreCase(tokens[2])){			
			JOptionPane.showMessageDialog(null, "Invalid Syntax,position of SET Clause is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
			
		}
		
		/// check if tableName exists
		
		String tableName  =  sql.substring(0, setIndex).trim().substring(6).trim();
		
		
		if("".equalsIgnoreCase(tableName)){
			JOptionPane.showMessageDialog(null, "Invalid Syntax,Table Name is missing", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}else{
			this.tableName = tableName;
		}
	
		
		// between tableName and SET clause
		
		String btwTableSet = sql.substring(sql.indexOf(tableName)+tableName.length(),setIndex).trim();
		
		System.out.println(btwTableSet);
		
		if(!"".equalsIgnoreCase(btwTableSet)){		
			JOptionPane.showMessageDialog(null, "Invalid Syntax", "Error", JOptionPane.ERROR_MESSAGE);
			return false;			
		}
		
		//everything good till here
		
		if(tokens.length != 4){		
			
			JOptionPane.showMessageDialog(null, "Invalid Syntax: Missing columnData to set", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
			
		}else{
			
			//columnData is available
			
			// check if whereClause is present
			
			int whereIndex = (sql.indexOf("WHERE") != -1) ? sql.indexOf("WHERE") : sql.indexOf("where");	
			
			if(whereIndex != -1){
				whereClausePresent = true;
			}
			
			if(whereClausePresent){
				
				//check if columnData available
				String btwSetWhere = sql.substring(setIndex,whereIndex).trim().substring(3).trim();
				
				if("".equals(btwSetWhere)){
					JOptionPane.showMessageDialog(null, "Invalid Syntax, no columdata to set", "Error", JOptionPane.ERROR_MESSAGE);
					return false;						
				}
				
			}
			
			
			// fetch columnData
			if(tokens[3].contains(",")){
				
				// split it based on comma
				
				
			}
			
			
			
			
		}
		
		
		
		
		
		
		return true;
			
	}
	
	
	

}

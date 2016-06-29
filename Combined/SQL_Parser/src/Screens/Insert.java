package Screens;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JOptionPane;

public class Insert {

	private Table tableName;
	private List<String> columns;
	private List<String> values;
	private boolean columnsPresent = false;

	public List<String> getValues() {
		return values;
	}


	public void setValues(List<String> values) {
		this.values = values;
	}


	public Table getTableName() {
		return tableName;
	}


	public void setTableName(Table tableName) {
		this.tableName = tableName;
	}


	public List<String> getColumns() {
		return columns;
	}


	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public void parse(String[] tokens, String sql) {

		//statement = statement.toLowerCase();

		if(tokens.length != 5 || tokens.length != 6){			
			JOptionPane.showMessageDialog(null, "Invalid INSERT Statement", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}


		int intoIndex = (sql.indexOf("INTO") != -1) ? sql.indexOf("INTO") : sql.indexOf("into");
		int valueIndex = (sql.indexOf("VALUES") != -1) ? sql.indexOf("VALUES") : sql.indexOf("values");

		if(intoIndex == -1){			
			JOptionPane.showMessageDialog(null, "Invalid Syntax,Missing INTO clause", "Error", JOptionPane.ERROR_MESSAGE);
			return;			
		}

		if(valueIndex == -1){
			JOptionPane.showMessageDialog(null, "Invalid Syntax,Missing VALUES clause", "Error", JOptionPane.ERROR_MESSAGE);
			return;		
		}

		// if reached here , means into and values are present;
		// check if at correct position

		if(!tokens[2].equalsIgnoreCase("INTO")){

			JOptionPane.showMessageDialog(null, "Invalid location of INTO clause", "Error", JOptionPane.ERROR_MESSAGE);

		 }

		// INSERT INTO correct	 
		
		// check if columnNames present
		int firstBracketIndex = sql.indexOf("(");

		if(firstBracketIndex < valueIndex){	    	
			this.columnsPresent = true;
		}
	  
		// check if values is at position 4 or 5
		if(columnsPresent && !tokens[4].equalsIgnoreCase("values")){	    	
		    JOptionPane.showMessageDialog(null, "Invalid location of values clause", "Error", JOptionPane.ERROR_MESSAGE);   
		    return;
		}else if(!tokens[3].equalsIgnoreCase("values")){
			JOptionPane.showMessageDialog(null, "Invalid location of values clause", "Error", JOptionPane.ERROR_MESSAGE);   
		    return;
		}

		//fetch tableName

		String tableName = "";
		
		if(columnsPresent){
			tableName = sql.substring(intoIndex, firstBracketIndex).trim().substring(4).trim();
			
		}else	
		    tableName = sql.substring(intoIndex, valueIndex).trim().substring(4).trim();

		if("".equals(tableName)){
			JOptionPane.showMessageDialog(null, "Invalid Syntax,Missing tableName", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}else{
			System.out.println("TableName: "+tableName);
			setTableName(new Table(tableName));
		}	

		// check if the brackets are balanced
		boolean bracketsBalanced = balancedBrackets(sql);

		if(!bracketsBalanced){	    	
			JOptionPane.showMessageDialog(null, "Invalid Syntax: invalid brackets", "Error", JOptionPane.ERROR_MESSAGE);
			return;	    	
		}

		//fetch columnNames

		if(columnsPresent){

			// check if each column has singleQuote around it
			if(!tokens[3].startsWith("(") || !tokens[3].endsWith(")")){	    		
				JOptionPane.showMessageDialog(null, "Invalid Syntax: Column Names not surrounded with ()", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}else{

				int startIndex = tokens[3].indexOf("(");
				int endIndex = tokens[3].indexOf(")");

				String columnNames = tokens[3].substring(startIndex+1, endIndex).trim();

				if("".equals(columnNames)){
					
					JOptionPane.showMessageDialog(null, "Invalid Syntax: Column Names missing", "Error", JOptionPane.ERROR_MESSAGE);
					return;
					
				}else{
				
					String [] colNames = columnNames.split(",");

					this.columns = new ArrayList<String>();
					for(String name : colNames){		    				
						columns.add(name);  				
					}
				}
			}
		}

		// fetch columnValues

		String colValues = null;

		if(columnsPresent){			
			colValues = tokens[5];
		}else
			colValues = tokens[4];


		if(!colValues.startsWith("(") || !colValues.endsWith(")")){
			JOptionPane.showMessageDialog(null, "Invalid Syntax: Column Values not surrounded with ()", "Error", JOptionPane.ERROR_MESSAGE);
			return;			
		}else{

			int startIndex = colValues.indexOf("(");
			int endIndex = colValues.indexOf(")");

			this.values = new ArrayList<String>();

			String val = colValues.substring(startIndex+1, endIndex).trim();
			
			if("".equals(val)){				
				JOptionPane.showMessageDialog(null, "Invalid Syntax: Column Values missing", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// check if val is a comma separated string
			System.out.println("val: "+val);
			
			String [] columnValues = val.split(",");

			boolean flag = true;

			for(String value : columnValues){
				System.out.println("value: "+value);
				if(!validateColVal(value)){
					flag = false;
					break;
				}	    				
			}

			if(!flag){	    				
				JOptionPane.showMessageDialog(null, "Invalid Syntax: Missing \"'\" in column values", "Error", JOptionPane.ERROR_MESSAGE);
				return;	    				
			}else{
				for(String name : columnValues){		    				
					values.add(name);  				
				}
			}

         System.out.println(values);

		}

	}


	private boolean balancedBrackets(String sql){

		Stack<Character> stack = new Stack<Character>();

		for (int i = 0; i < sql.length(); i++) {

			if (sql.charAt(i) == '(')   stack.push('(');

			else if (sql.charAt(i) == ')') {
				if (stack.isEmpty())        return false;
				if (stack.pop() != '(') return false;
			}else if(sql.charAt(i) == '{' || sql.charAt(i) == '}' || sql.charAt(i) == '[' || sql.charAt(i) == ']')
				return false;

		}

		return stack.isEmpty();

	}


	private boolean validateColVal(String name){

		if(!name.startsWith("'") || !name.endsWith("'"))
			return false;

		return true;

	}



}

package Screens;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

public class QValidation {
	static boolean cond_flag;
	public static void validateQ1(String query)
	{
		String[] words = query.split("\\s+");
		int i = 0;
		while(true)
		{
			System.out.println(words[i].toLowerCase().toString());
			i++;
			if(i == words.length)
			{
				break;
			}
		}
		String type= words[0].toLowerCase();
		switch(type)
		{
		case "select":
			selectValidation(words);
			break;
		case "update":
			updateValidation(words,query);
			break;
		case "insert":
			insertValidation(words,query);
			break;
		case "delete":
			deleteValidation(query);
			break;
		default:
			JOptionPane.showMessageDialog(null, "Invalid database operation", "Error", JOptionPane.ERROR_MESSAGE);
		}
			
	}
	public static void selectValidation(String[] words)
	{
		cond_flag = false;
		ArrayList<String> projection = new ArrayList<>();
		ArrayList<String> tables = new ArrayList<>();
		ArrayList<String> conditions = new ArrayList<>();
		int i=1;
		while( i< words.length && !(words[i].equals("from")))
		{
			projection.add(words[i]);
			i++;
		}
		if(i==words.length)
		{
			JOptionPane.showMessageDialog(null, "Please Specify from clause", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		i++;
		while(i< words.length && !(words[i].equals("where")))
		{
			tables.add(words[i]);
			i++;
		}
		if(i!= words.length)
		{
			cond_flag =true;
			i++;
			while(i!= words.length)
			{
				conditions.add(words[i]);
				i++;
			}
			
		}
		System.out.println(tables.toString());
		if(!tableValidation(tables))
		{
			System.out.println(tables.toString());
			return;
		}
		
			//System.out.println(projection.toString());
			projectionValid(projection);
			//System.out.println(projection.toString());
		
		
	}
	public static boolean tableValidation(ArrayList<String> tables)
	{
		if(tables.size()==0)
		{
			JOptionPane.showMessageDialog(null, "Please specify from clause", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
			
		}

		if(tables.size()==1)
		{
			return true;
		}
		if(tables.contains(",") && (tables.get(tables.size()-1).equals(",") || tables.get(0).equals(",")))
		{
			JOptionPane.showMessageDialog(null, "Invalid location of \",\" after from", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(tables.contains(","))
		{
			int index = tables.indexOf(",");
			tables.remove(index);
		}
		Iterator itr1 =tables.iterator();
		String tempProj1="";
		while(itr1.hasNext())
		{
			tempProj1+=itr1.next();
			
		}
		if(tempProj1.contains(",,"))
		{
			JOptionPane.showMessageDialog(null, "Invalid sequence \",,\" after from", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		Iterator itr =tables.iterator();
		String tempProj="";
		while(itr.hasNext())
		{
			tempProj+=itr.next()+" ";
			
		}
		if(tempProj.charAt(0)==',' || tempProj.charAt(tempProj.length()-1)==',')
		{
			JOptionPane.showMessageDialog(null, "Invalid location of \",\" after from", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		//tempProj.replace(",", " ");
		String[] proj = tempProj.split(",");
		tables.clear();
		int len =0 ;
		while(len < proj.length)
		{
			if(proj[len].contains(" "))
			{
				String[] tempS = proj[len].split(" ");
				int ind = 0;
				while(ind<tempS.length)
				{
					tables.add(tempS[ind]);
					ind++;
				}
				
			}
			else
			{
				tables.add(proj[len]);
			}
			len++;
		}
		
		System.out.println(tables.toString());;
		return true;
	}
	
	public static void projectionValid(ArrayList<String> projection)
	{
		if(projection.size()==1 && projection.get(0).equals("*"))
		{
			return;
		}
		
		if(projection.contains(",") && (projection.get(projection.size()-1).equals(",") || projection.get(0).equals(",")))
		{
			JOptionPane.showMessageDialog(null, "Invalid location of \",\"", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(projection.contains(","))
		{
			int index = projection.indexOf(",");
			projection.remove(index);
		}
		
		Iterator itr1 =projection.iterator();
		String tempProj1="";
		while(itr1.hasNext())
		{
			tempProj1+=itr1.next();
			
		}
		if(tempProj1.contains(",,"))
		{
			JOptionPane.showMessageDialog(null, "Invalid sequence \",,\"", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Iterator itr =projection.iterator();
		String tempProj="";
		while(itr.hasNext())
		{
			tempProj+=itr.next()+" ";
			
		}
		if(tempProj.charAt(0)==',' || tempProj.charAt(tempProj.length()-1)==',')
		{
			JOptionPane.showMessageDialog(null, "Invalid location of \",\"", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//tempProj.replace(",", " ");
		String[] proj = tempProj.split(",");
		projection.clear();
		int len =0 ;
		while(len < proj.length)
		{
			if(proj[len].contains(" "))
			{
				String[] tempS = proj[len].split(" ");
				int ind = 0;
				while(ind<tempS.length)
				{
					projection.add(tempS[ind]);
					ind++;
				}
				
			}
			else
			{
				projection.add(proj[len]);
			}
			len++;
		}
		
		System.out.println(projection.toString());;
		
 	}
	
	public static void updateValidation(String[] words,String statement)
	{
		 Update update = new Update();
		 boolean result = update.parse(words, statement);
		
		
	}
	
	public static void insertValidation(String[] words, String statement)
	{
				
	     Insert insert = new Insert();
	     insert.parse(words, statement);	
		
		
		
	}
	
	
	public static void deleteValidation(String query)
	{
		
		Delete delete = new Delete();
		//validate the syntax and semantics
		boolean isValid = delete.parse(query);
		
		if(isValid){
			 delete.deleteRecords();
			 JOptionPane.showMessageDialog(null, "Records deleted successfully", "Error", JOptionPane.ERROR_MESSAGE);
			 return;
		}		 
		
		
	}
	
	
	
}

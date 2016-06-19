package Screens;

import java.awt.GridLayout;
import java.io.FileReader;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class InsertWindow extends JFrame {
	
	String tableName;
	int size;
	JTextField[] text;
	String[] colnm;
	
	/**
	 * Launch the application.
	 */
	
	public InsertWindow(String S, String TN){
		
		super(S);
		tableName = TN;
		
		getContentPane().setLayout(new GridLayout(2, 1));
		JPanel P1 =new JPanel();
		JPanel P2 =new JPanel();
		setBounds(100, 100, 528, 302);
		
		JSONParser parser = new JSONParser();
		try {
			FileReader f1= new FileReader("Data/Metadata/"+tableName+".json");
			Object obj = parser.parse(f1);
			JSONObject json = (JSONObject) obj;			
			JSONArray headers = (JSONArray) json.get("headers");			
			P1.setLayout(new GridLayout(headers.size(), 2));

			size = headers.size();
			
			text = new JTextField[headers.size()];
			
			colnm = new String[headers.size()];
			
			for (int i = 0; i < headers.size(); i++){
				
				Object temp = parser.parse(headers.get(i).toString());
				JSONObject temp1 = (JSONObject) temp;
				colnm[i] = (String) temp1.get("Column Name");
				//System.out.println((String) temp1.get("Column Name"));
				JLabel Col = new JLabel("  "+(String) temp1.get("Column Name")+" : ");
				
				JTextField textField = new JTextField();
				
				text[i] = textField;
				P1.add(Col);
				P1.add(text[i]);
			}	
			f1.close();
		}catch (Exception e) {	
			e.printStackTrace();
		}
	
		JButton ok =new JButton("Ok");
		
		ok.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				JSONObject json1 = new JSONObject();
				for(int j=0;j<size;j++)
				{
					System.out.println("Data"+colnm[j]+" "+text[j].getText());
					json1.put(colnm[j], text[j].getText());
					
				}
				
				System.out.println(json1.toString());
				if(OperationFunctions.validateJSON(tableName, json1,true))
				{
					OperationFunctions.insertInTable(tableName, json1);
					dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(null, OperationFunctions.validateMsg, "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		P2.add(ok);
		getContentPane().add(P1);
		getContentPane().add(P2);			
		setVisible(true);

	}
	public InsertWindow(String S, String TN,DisplayRecords DR,String key,String value){
		
		super(S);
		tableName = TN;
		
		getContentPane().setLayout(new GridLayout(2, 1));
		JPanel P1 =new JPanel();
		JPanel P2 =new JPanel();
		setBounds(100, 100, 528, 302);
		JSONObject json1 = null; 
		JSONParser parser = new JSONParser();
		try {
			FileReader f1 = new FileReader("Data/Metadata/"+tableName+".json");
			Object obj = parser.parse(f1);
			JSONObject json = (JSONObject) obj;			
			JSONArray headers = (JSONArray) json.get("headers");			
			P1.setLayout(new GridLayout(headers.size(), 2));
			//P1.setMaximumSize(new Dimension(100, 50));
			JSONParser parser1 = new JSONParser();
			try {
				FileReader f2 = new FileReader("Data/Records/" + tableName+ ".json");
				Object obj1 = parser1.parse(f2);
				JSONObject json12 = (JSONObject) obj1;
				
				
				
					JSONArray headers2 = (JSONArray) json12.get("Records");
					int rindex=-1;
					int size = headers2.size();
					for(int i=0;i<size;i++)
					{
						JSONObject temp = (JSONObject) headers2.get(i);
						if(temp.get(key).equals(value))
						{
							rindex = i;
							break;
						}
					}
					json1 = (JSONObject) headers2.get(rindex);
					f2.close();
			}
			catch(Exception e)
			{}
			size = headers.size();
			
			text = new JTextField[headers.size()];
			
			colnm = new String[headers.size()];
			
			for (int i = 0; i < headers.size(); i++){
				
				Object temp = parser.parse(headers.get(i).toString());
				JSONObject temp1 = (JSONObject) temp;
				colnm[i] = (String) temp1.get("Column Name");
				//System.out.println((String) temp1.get("Column Name"));
				JLabel Col = new JLabel("  "+(String) temp1.get("Column Name")+" : ");
				
				JTextField textField = new JTextField();
				textField.setText((String)json1.get(colnm[i]));
				text[i] = textField;
				if(((String) temp1.get("Column Name")).equals(key))
				{
					textField.setEditable(false);
				}
				P1.add(Col);
				P1.add(text[i]);
			}	
			f1.close();
		}catch (Exception e) {	
			e.printStackTrace();
		}
	
		JButton ok =new JButton("Ok");
		
		ok.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				JSONObject json2 = new JSONObject();
				for(int j=0;j<size;j++)
				{
					System.out.println("Data"+colnm[j]+" "+text[j].getText());
					json2.put(colnm[j], text[j].getText());
					
				}
				
				System.out.println(json2.toString());
				if(OperationFunctions.validateJSON(tableName, json2, false))
				{
					OperationFunctions.updateInTable(tableName, json2,key,value);
					DisplayRecords display = new DisplayRecords(tableName,true);
					display.displayRecords(tableName);
					DR.frame.dispose();
					dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(null, OperationFunctions.validateMsg, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		P2.add(ok);
		getContentPane().add(P1);
		getContentPane().add(P2);			
		setVisible(true);

	}
	
}

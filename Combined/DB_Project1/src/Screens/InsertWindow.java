package Screens;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class InsertWindow extends JFrame {

	
	
	String tableName;
	int size;
	JTextField[] text;
	String[] colnm;
	/**
	 * Launch the application.
	 */
	
	public InsertWindow(String S, String TN) {
		super(S);
		tableName = TN;
		
		setLayout(new GridLayout(2, 1));
		JPanel P1 =new JPanel();
		JPanel P2 =new JPanel();
		setBounds(100, 100, 450, 250);
		
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader("Data/Metadata/"+tableName+".json"));
			JSONObject json = (JSONObject) obj;
			
			JSONArray headers = (JSONArray) json.get("headers");
			
			P1.setLayout(new GridLayout(headers.size(), 2));
			//P1.setMaximumSize(new Dimension(100, 50));
			
			size= headers.size();
			
			text = new JTextField[headers.size()];
			
			colnm = new String[headers.size()];
			
			for (int i = 0; i < headers.size(); i++) {
				Object temp = parser.parse(headers.get(i).toString());
				JSONObject temp1 = (JSONObject) temp;
				colnm[i] = (String) temp1.get("Column Name");
				//System.out.println((String) temp1.get("Column Name"));
				JLabel Col = new JLabel("  "+(String) temp1.get("Column Name")+" : ");
				
				JTextField textField = new JTextField();
				
				///textField.setMaximumSize(new Dimension(100, 5));
				
				text[i] = textField;
				P1.add(Col);
				P1.add(text[i]);
			}			
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
				OperationFunctions.insertInTable(tableName, json1);
				dispose();
			}
		});
		
		P2.add(ok);
		add(P1);
		add(P2);
			
		setVisible(true);

	}
	
}

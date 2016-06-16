package Screens;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TableName extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	public String tnm;
	protected boolean done =false;

	/**
	 * Launch the application.
	 */
	/**
	 * Create the frame.
	 */
	public TableName(String S,String OP) {
		super(S);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{63, 118, 0, 121, 163, 0};
		gbl_contentPane.rowHeights = new int[]{65, 0, 121, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblTableName = new JLabel("Table Name :");
		lblTableName.setFont(new Font("Calibri", Font.BOLD, 20));
		GridBagConstraints gbc_lblTableName = new GridBagConstraints();
		gbc_lblTableName.fill = GridBagConstraints.BOTH;
		gbc_lblTableName.insets = new Insets(0, 0, 5, 5);
		gbc_lblTableName.gridx = 1;
		gbc_lblTableName.gridy = 1;
		contentPane.add(lblTableName, gbc_lblTableName);
		
		textField = new JTextField();
		textField.setFont(new Font("Calibri", Font.PLAIN, 20));
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.gridx = 3;
		gbc_textField.gridy = 1;
		contentPane.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(textField.getText().isEmpty())
				{
					Object frame;
					JOptionPane.showMessageDialog(textField,"Please enter Table Name");
				}
				else
				{
					
					if(GlobalData.allTables.contains(textField.getText()))
					{
						tnm =textField.getText() ;
						switch(OP)
						{
						case "Insert":
							InsertWindow IW = new InsertWindow("Insert Tuple",tnm);
							break;
						case "Delete":
							break;
						case "Update":
							break;
						case "":
							break;
						}
						dispose();
					}
					else
					{
						JOptionPane.showMessageDialog(textField, "No such Table");
					}
					
				}
				
			}
		});
		btnOk.setFont(new Font("Calibri", Font.BOLD, 20));
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.insets = new Insets(0, 0, 0, 5);
		gbc_btnOk.gridx = 2;
		gbc_btnOk.gridy = 2;
		contentPane.add(btnOk, gbc_btnOk);
	}

}

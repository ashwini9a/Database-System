package Screens;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import java.awt.Font;

public class Ops extends JFrame {

	private JPanel contentPane;

	
	public Ops(String S) {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super(S);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		setContentPane(contentPane);
		
		JButton btnNewButton_1 = new JButton("Insert");
		btnNewButton_1.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnNewButton_1.setBounds(165, 76, 105, 23);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableName frame = new TableName("Select Table","Insert");
				frame.setVisible(true);
				
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Update");
		btnNewButton_2.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnNewButton_2.setBounds(165, 122, 105, 23);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TableName frame = new TableName("Select Table","Update");
				frame.setVisible(true);			
			}
		});
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_4 = new JButton("Search");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				TableName frame = new TableName("Select Table","Search");
				frame.setVisible(true);				
			}
		});
		btnNewButton_4.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnNewButton_4.setBounds(165, 168, 105, 23);
		contentPane.add(btnNewButton_4);
		
		JLabel lblMenu = new JLabel("    MENU");
		lblMenu.setFont(new Font("Times New Roman", Font.BOLD, 19));
		lblMenu.setBounds(165, 31, 105, 23);
		contentPane.add(lblMenu);
	}
}

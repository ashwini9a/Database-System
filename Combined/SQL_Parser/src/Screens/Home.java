package Screens;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Home extends JFrame{

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Home() {
		setTitle("SQL");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 792, 342);
		getContentPane().setLayout(null);
		
		JLabel lblSqlQuery = new JLabel("SQL Query : ");
		lblSqlQuery.setBounds(10, 44, 116, 24);
		lblSqlQuery.setFont(new Font("Times New Roman", Font.BOLD, 20));
		getContentPane().add(lblSqlQuery);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(135, 30, 631, 174);
		textArea.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		getContentPane().add(textArea);
		
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(559, 232, 75, 31);
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String query = textArea.getText();
				if(query.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Please enter a Query", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else{
					QValidation.validateQ1(query);
				}
			}
		});
		btnOk.setFont(new Font("Times New Roman", Font.BOLD, 18));
		getContentPane().add(btnOk);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(644, 232, 84, 31);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
			}
		});
		btnClear.setFont(new Font("Times New Roman", Font.BOLD, 18));
		getContentPane().add(btnClear);
		contentPane = new JPanel(new GridLayout(5, 5));
		
		
	}

}

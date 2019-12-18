package waterDelivery;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.microsoft.sqlserver.jdbc.SQLServerException;

public class ChangePasswordView {

	JFrame ChangePasswordFrame;
	private JTextField usr;
	private JTextField pwd;
	private JTextField confirm;
	private static Statement stat = null;
    private static Connection conn = null;
	private static ResultSet rs = null;
	private static String userName;
	private static int authority;
	/**
	 * Create the application.
	 */
	public ChangePasswordView(Statement stat,Connection conn,String userName,int authority) {
		System.out.println("ChangePasswordFrame");
		ChangePasswordView.stat=stat;
		ChangePasswordView.conn=conn;
		ChangePasswordView.userName=userName;
		ChangePasswordView.authority=authority;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		ChangePasswordFrame = new JFrame();
		ChangePasswordFrame.setBounds(100, 100, 450, 300);
		ChangePasswordFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		ChangePasswordFrame.getContentPane().add(panel, null);
		panel.setLayout(null);
		
		JLabel label1 = new JLabel("\u7528\u6237\u540D");
		label1.setBounds(98, 42, 45, 18);
		panel.add(label1);
		
		JLabel label2 = new JLabel("\u5BC6\u7801");
		label2.setBounds(98, 73, 72, 18);
		panel.add(label2);
		
		JLabel label3 = new JLabel("\u786E\u8BA4\u5BC6\u7801");
		label3.setBounds(98, 104, 72, 18);
		panel.add(label3);
		
		usr = new JTextField();
		usr.setBounds(185, 39, 140, 24);
		panel.add(usr);
		usr.setColumns(10);
		
		pwd = new JPasswordField();
		pwd.setBounds(185, 70, 140, 24);
		panel.add(pwd);
		pwd.setColumns(10);
		
		confirm = new JPasswordField();
		confirm.setBounds(185, 102, 140, 24);
		panel.add(confirm);
		confirm.setColumns(10);
		
		JButton ChangePasswordButton = new JButton("修改");
		ChangePasswordButton.setBounds(108, 186, 82, 27);
		ChangePasswordButton.addActionListener(new ChangePasswordListener());
		panel.add(ChangePasswordButton);	
		
		JButton backButton = new JButton("\u8FD4\u56DE");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangePasswordFrame.dispose();
				if(authority==3) {
					AdministratorView window=new AdministratorView(stat,conn,userName);
					window.AdministratorFrame.setVisible(true);
				}
				else if(authority==1){
					ConsumerView window=new ConsumerView(stat,conn,userName);
					window.ConsumerFrame.setVisible(true);
				}
				else {//authority==2
					DelivererView window=new DelivererView(stat,conn,userName);
					window.DelivererFrame.setVisible(true);
				}
			}
		});
		backButton.setBounds(236, 186, 82, 27);
		panel.add(backButton);
	}
	public class ChangePasswordListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String usrStr=usr.getText();
			String pwdStr=pwd.getText();
			String sql = "use water_delivery\nexec changePassword "+'\''+userName+'\'' +','+'\''+pwdStr+'\'' ;
			System.out.println(sql);
			try {
				stat.executeUpdate(sql);
			} catch (SQLException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(
                        ChangePasswordFrame,
                        "请重新输入",
                        e1.toString().substring(48),
                        JOptionPane.INFORMATION_MESSAGE
                );
			}
			if(pwdStr.equals(confirm.getText())) {
				JOptionPane.showMessageDialog(
						ChangePasswordFrame,
                        "修改成功",
                        "修改成功",
                        JOptionPane.INFORMATION_MESSAGE
                );
				ChangePasswordFrame.dispose();
				if(authority==3) {
					AdministratorView window=new AdministratorView(stat,conn,userName);
					window.AdministratorFrame.setVisible(true);
				}
				else {
					LoginView window=new LoginView(stat,conn);
					window.loginFrame.setVisible(true);
				}
			}
			else {
				JOptionPane.showMessageDialog(
						ChangePasswordFrame,
                        "请重新输入",
                        "两次输入密码不同",
                        JOptionPane.INFORMATION_MESSAGE
                );
			}
		}
	}

}

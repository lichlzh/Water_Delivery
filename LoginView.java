package waterDelivery;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.sql.*;

public class LoginView {

	JFrame loginFrame;
	private JTextField usr;
	private JTextField pwd;
	private static Statement stat = null;
    private static Connection conn = null;
    private static ResultSet rs = null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	    String url = "jdbc:sqlserver://DESKTOP-SG5736S:1433;databaseName=water_delivery"; 
	    String DBuser = "sa";
	    String DBpassword = "159357lzh";
		try {
			//1.加载驱动
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		    System.out.println("加载驱动成功！");
		}catch(Exception e) {
		    e.printStackTrace();
		    System.out.println("加载驱动失败！");
		}
		try {  
	        //2.连接
	        conn = DriverManager.getConnection(url,DBuser,DBpassword);
	        stat = conn.createStatement();
	        System.out.println("连接数据库成功！");
		}catch(Exception e) {
		    e.printStackTrace();
		    System.out.println("连接数据库失败！");
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginView window = new LoginView(stat,conn);
					window.loginFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		/*if(stat!=null)
			try {
				stat.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		if(conn!=null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}*/

	}

	/**
	 * Create the application.
	 */
	public LoginView(Statement stat,Connection conn) {
		System.out.println("LoginFrame");
		LoginView.stat=stat;
		LoginView.conn=conn;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		loginFrame = new JFrame();
		loginFrame.setBounds(100, 100, 450, 300);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		loginFrame.getContentPane().add(panel, null);
		panel.setLayout(null);
		
		JLabel label1 = new JLabel("\u7528\u6237\u540D");
		label1.setBounds(98, 42, 45, 18);
		panel.add(label1);
		
		JLabel label2 = new JLabel("\u5BC6\u7801");
		label2.setBounds(98, 92, 72, 18);
		panel.add(label2);
		
		usr = new JTextField();
		usr.setBounds(185, 39, 140, 24);
		panel.add(usr);
		usr.setColumns(10);
		
		pwd = new JPasswordField();
		pwd.setBounds(185, 89, 140, 24);
		panel.add(pwd);
		pwd.setColumns(10);
		
		JButton loginButton = new JButton("\u767B\u5F55");
		loginButton.setBounds(124, 160, 63, 27);
		loginButton.addActionListener(new loginListener());
		panel.add(loginButton);
		
		JButton registerButton = new JButton("\u6CE8\u518C");
		registerButton.setBounds(239, 160, 63, 27);
		registerButton.addActionListener(new registerListener());
		panel.add(registerButton);
		
	}
	
	public class debugListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String buttonText=((JButton)e.getSource()).getText();
			System.out.println("press"+buttonText);
		}
	}
	public class loginListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String usrStr=usr.getText();
			String pwdStr=pwd.getText();
			String sql = "use water_delivery\nSELECT * FROM [User]\nWHERE userName = " + '\'' + usrStr + '\'';
			//System.out.println(usrStr);
			//System.out.println(pwdStr);
			//System.out.println(sql);
			try {
				rs = stat.executeQuery(sql);
				if(rs.next()) {
					String res=rs.getObject("password").toString();
					if(res.equals(pwdStr)) {
						System.out.println("username:"+usrStr+'\n'+"password:"+pwdStr+'\n'+"login success!");
						int authority=(int) rs.getObject("authority");
						loginFrame.dispose();
						if(authority==1) {
							ConsumerView window=new ConsumerView(stat,conn,usrStr);
							window.ConsumerFrame.setVisible(true);
						}else if(authority==2) {
							DelivererView window=new DelivererView(stat,conn,usrStr);
							window.DelivererFrame.setVisible(true);
						}else {
							AdministratorView window=new AdministratorView(stat,conn,usrStr);
							window.AdministratorFrame.setVisible(true);
						}
					}
					else {
						JOptionPane.showMessageDialog(
		                        loginFrame,
		                        "请重新输入",
		                        "密码错误",
		                        JOptionPane.INFORMATION_MESSAGE
		                );
					}
				}
				else {
					JOptionPane.showMessageDialog(
	                        loginFrame,
	                        "请重新输入",
	                        "用户名不存在",
	                        JOptionPane.INFORMATION_MESSAGE
	                );
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public class registerListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			loginFrame.dispose();
			RegisterView window=new RegisterView(stat,conn);
			window.registerFrame.setVisible(true);
		}
	}
}

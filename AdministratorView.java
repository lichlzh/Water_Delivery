package waterDelivery;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import waterDelivery.LoginView.loginListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;

public class AdministratorView {

	JFrame AdministratorFrame;
	private static Statement stat = null;
    private static Connection conn = null;
    private static ResultSet rs = null;
    private static String userName;
    private JTextField queryPerformanceField;
	/**
	 * Create the application.
	 */
	public AdministratorView(Statement stat,Connection conn,String userName) {
		System.out.println("AdministratorFrame");
		AdministratorView.stat=stat;
		AdministratorView.conn=conn;
		AdministratorView.userName=userName;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		AdministratorFrame = new JFrame("管理员");
		AdministratorFrame.setBounds(100, 100, 900, 600);
		AdministratorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		AdministratorFrame.getContentPane().add(panel, null);
		panel.setLayout(null);
		
		JButton deleteUserButton = new JButton("删除用户");
		deleteUserButton.setBounds(121, 92, 113, 27);
		deleteUserButton.addActionListener(new deleteUserListener());
		panel.add(deleteUserButton);
		
		JButton addUserButton = new JButton("添加用户");
		addUserButton.setBounds(121, 175, 113, 27);
		addUserButton.addActionListener(new addUserListener());
		panel.add(addUserButton);
		
		JButton changePasswordButton = new JButton("修改密码");
		changePasswordButton.setBounds(121, 260, 113, 27);
		changePasswordButton.addActionListener(new changePasswordListener());
		panel.add(changePasswordButton);
		
		JButton queryDelivererbutton = new JButton("查询师傅");
		queryDelivererbutton.addActionListener(new queryDelivererListener());
		queryDelivererbutton.setBounds(121, 347, 113, 27);
		panel.add(queryDelivererbutton);
		
		queryPerformanceField = new JTextField("输入编号");
		queryPerformanceField.setBounds(631, 93, 123, 24);
		panel.add(queryPerformanceField);
		queryPerformanceField.setColumns(8);
		
		JButton queryPerformanceButton = new JButton("\u67E5\u8BE2\u7EE9\u6548");
		queryPerformanceButton.addActionListener(new queryPerformanceListener());
		queryPerformanceButton.setBounds(462, 92, 113, 27);
		panel.add(queryPerformanceButton);
		
		JButton logoutButton = new JButton("\u6CE8\u9500");
		logoutButton.setBounds(755, 513, 113, 27);
		logoutButton.addActionListener(new logoutListener());
		panel.add(logoutButton);

	}
	public class debugListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String buttonText=((JButton)e.getSource()).getText();
			System.out.println("press"+buttonText);
		}
	}
	public class addUserListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			AdministratorFrame.dispose();
			AddUserView window=new AddUserView(stat,conn,userName,3);
			window.AddUserFrame.setVisible(true);
		}
	}
	public class deleteUserListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String deleteUsr=JOptionPane.showInputDialog(
					AdministratorFrame,
					"要删除的用户名:",
					""
			);
			System.out.println(deleteUsr);
			if(deleteUsr.equals(userName)) {
				JOptionPane.showMessageDialog(
						AdministratorFrame,
						"不能删除自己",
						"操作无效",
						JOptionPane.INFORMATION_MESSAGE
				);
			}
			else {
				String sql = "use water_delivery\nexec deleteUser "+'\''+deleteUsr+'\'';
				System.out.println(sql);
				try {
					stat.executeUpdate(sql);
					JOptionPane.showMessageDialog(
						AdministratorFrame,
						"删除成功",
						"删除成功",
						JOptionPane.INFORMATION_MESSAGE
					);
					
				}catch(SQLException e1){
					e1.printStackTrace();
					JOptionPane.showMessageDialog(
						AdministratorFrame,
						"删除失败",
						"操作无效",
						JOptionPane.INFORMATION_MESSAGE
					);
				}
				
			}
		}
	}
	public class changePasswordListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			AdministratorFrame.dispose();
			ChangePasswordView window=new ChangePasswordView(stat,conn,userName,3);
			window.ChangePasswordFrame.setVisible(true);
		}
	}
	public class queryDelivererListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			AdministratorFrame.dispose();
			QueryDelivererView window=new QueryDelivererView(stat,conn,userName,3);
			window.QueryDelivererFrame.setVisible(true);
		}
	}
	public class queryPerformanceListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String sql = "use water_delivery\nSELECT * FROM Performance WHERE delivererNumber="+'\''+queryPerformanceField.getText()+'\'';
			System.out.println(sql);
			try {
				rs=stat.executeQuery(sql);
				if(rs.next()) {
					JOptionPane.showMessageDialog(
							AdministratorFrame,
							"编号:"+queryPerformanceField.getText()+'\n'
							+"完成订单数:"+rs.getObject("orderCount").toString()+'\n'
							+"准时率:"+rs.getObject("accuracy").toString(),
							"查询成功",
							JOptionPane.INFORMATION_MESSAGE
						);
				}
				else
					JOptionPane.showMessageDialog(
							AdministratorFrame,
							"找不到该师傅",
							"查询失败",
							JOptionPane.INFORMATION_MESSAGE
						);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
		}
	}
	public class logoutListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			AdministratorFrame.dispose();
			LoginView window=new LoginView(stat,conn);
			window.loginFrame.setVisible(true);
		}
	}
}

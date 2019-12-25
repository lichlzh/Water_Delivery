package waterDelivery;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class DelivererView {

	JFrame DelivererFrame;
	private static Statement stat = null;
    private static Connection conn = null;
    private static ResultSet rs = null;
    private static String userName,delivererNumber;
	/**
	 * Create the application.
	 */
	public DelivererView(Statement stat,Connection conn,String userName) {
		System.out.println("DelivererFrame");
		DelivererView.stat=stat;
		DelivererView.conn=conn;
		DelivererView.userName=userName;
		String sql = "use water_delivery\nSELECT delivererNumber FROM Deliverer\nWHERE userName = " + '\'' + userName + '\'';
		System.out.println(sql);
		try {
			rs=stat.executeQuery(sql);
			if(rs.next()) 
				DelivererView.delivererNumber=(String) rs.getObject("delivererNumber");
			else
				System.out.println("ERROR");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		DelivererFrame = new JFrame("送水师傅");
		DelivererFrame.setBounds(100, 100, 900, 600);
		DelivererFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		DelivererFrame.getContentPane().add(panel, null);
		panel.setLayout(null);
		
		JButton fillInInfoButton = new JButton("完善信息");
		fillInInfoButton.setBounds(121, 92, 113, 27);
		fillInInfoButton.addActionListener(new fillInInfoListener());
		panel.add(fillInInfoButton);
		
		JButton changePasswordButton = new JButton("修改密码");
		changePasswordButton.setBounds(121, 260, 113, 27);
		changePasswordButton.addActionListener(new changePasswordListener());
		panel.add(changePasswordButton);
		
		JButton queryDelivererbutton = new JButton("查询师傅");
		queryDelivererbutton.setBounds(121, 347, 113, 27);
		queryDelivererbutton.addActionListener(new queryDelivererListener());
		panel.add(queryDelivererbutton);
		
		JButton queryOrderButton = new JButton("订单操作");
		queryOrderButton.setBounds(121, 171, 113, 27);
		queryOrderButton.addActionListener(new queryOrderListener());
		panel.add(queryOrderButton);
		
		JButton logoutButton = new JButton("退出登录");
		logoutButton.setBounds(755, 513, 113, 27);
		logoutButton.addActionListener(new logoutListener());
		panel.add(logoutButton);
	}
	public class changePasswordListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			DelivererFrame.dispose();
			ChangePasswordView window=new ChangePasswordView(stat,conn,userName,2);
			window.ChangePasswordFrame.setVisible(true);
		}
	}
	public class fillInInfoListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			DelivererFrame.dispose();
			DelivererFillInInfoView window=new DelivererFillInInfoView(stat,conn,userName,delivererNumber);
			window.DelivererFillInInfoFrame.setVisible(true);
		}
	}
	public class queryDelivererListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			DelivererFrame.dispose();
			QueryDelivererView window=new QueryDelivererView(stat,conn,userName,2);
			window.QueryDelivererFrame.setVisible(true);
		}
	}
	public class queryOrderListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			DelivererFrame.dispose();
			DealOrderView window=new DealOrderView(stat,conn,userName,delivererNumber);
			window.DealOrderFrame.setVisible(true);
		}
	}
	public class logoutListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			DelivererFrame.dispose();
			LoginView window=new LoginView(stat,conn);
			window.loginFrame.setVisible(true);
		}
	}
}

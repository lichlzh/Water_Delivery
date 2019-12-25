package waterDelivery;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;


public class ConsumerView {

	JFrame ConsumerFrame;
	private static Statement stat = null;
    private static Connection conn = null;
    private static ResultSet rs = null;
    private static String userName;
    private int selectedIndex,area,building,floor,room;

	/**
	 * Create the application.
	 */
	public ConsumerView(Statement stat,Connection conn,String userName) {
		System.out.println("ConsumerFrame");
		ConsumerView.stat=stat;
		ConsumerView.conn=conn;
		ConsumerView.userName=userName;
		
		String sql = "use water_delivery\nSELECT * FROM Domitory\nWHERE userName="+'\''+userName+'\'' ;
		System.out.println(sql);
		try {
			rs=stat.executeQuery(sql);
			if(rs.next()) {
				area=(Integer)rs.getObject("area");
				building=(Integer)rs.getObject("building");
				floor=(Integer)rs.getObject("floor");
				room=(Integer)rs.getObject("room");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		ConsumerFrame = new JFrame("用户");
		ConsumerFrame.setBounds(100, 100, 900, 600);
		ConsumerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		ConsumerFrame.getContentPane().add(panel, null);
		panel.setLayout(null);
		
		JButton changePasswordButton = new JButton("修改密码");
		changePasswordButton.setBounds(121, 260, 113, 27);
		changePasswordButton.addActionListener(new changePasswordListener());
		panel.add(changePasswordButton);
		
		JButton fillInInfoButton = new JButton("完善信息");
		fillInInfoButton.setBounds(121, 92, 113, 27);
		fillInInfoButton.addActionListener(new fillInInfoListener());
		panel.add(fillInInfoButton);
		
		JButton queryDelivererbutton = new JButton("查询师傅");
		queryDelivererbutton.addActionListener(new queryDelivererListener());
		queryDelivererbutton.setBounds(121, 347, 113, 27);
		panel.add(queryDelivererbutton);
		
		JComboBox<Integer> comboBox=new JComboBox<Integer>();
		comboBox.setBounds(424, 174, 75, 24);
		for(int i=0;i<=4;i++)
			comboBox.addItem(i);
		comboBox.setSelectedIndex(0);
		comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	selectedIndex=comboBox.getSelectedIndex();
                    System.out.println("选中: " + selectedIndex + " = " + comboBox.getSelectedItem());
                }
            }
        });
		panel.add(comboBox);
		
		JButton createOrderButton = new JButton("\u4E0B\u5355");
		createOrderButton.setBounds(121, 173, 113, 27);
		createOrderButton.addActionListener(new createOrderListener());
		panel.add(createOrderButton);
		
		JLabel label1 = new JLabel("\u9009\u62E9\u6876\u6570");
		label1.setBounds(294, 177, 72, 18);
		panel.add(label1);
		
		JLabel label2 = new JLabel("\u6876");
		label2.setBounds(519, 177, 72, 18);
		panel.add(label2);
		
		JButton logoutButton = new JButton("退出登录");
		logoutButton.setBounds(755, 513, 113, 27);
		logoutButton.addActionListener(new logoutListener());
		panel.add(logoutButton);
	}
	public boolean checkInfo() {
		return area!=0&&building!=0&&floor!=0&&room!=0;
	}
	public class changePasswordListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ConsumerFrame.dispose();
			ChangePasswordView window=new ChangePasswordView(stat,conn,userName,1);
			window.ChangePasswordFrame.setVisible(true);
		}
	}
	public class fillInInfoListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ConsumerFrame.dispose();
			ConsumerFillInInfoView window=new ConsumerFillInInfoView(stat,conn,userName,area,building,floor,room);
			window.ConsumerFillInInfoFrame.setVisible(true);
		}
	}
	public class queryDelivererListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ConsumerFrame.dispose();
			QueryDelivererView window=new QueryDelivererView(stat,conn,userName,1);
			window.QueryDelivererFrame.setVisible(true);
		}
	}
	public class createOrderListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(checkInfo()) {
				if(selectedIndex==0) {
					JOptionPane.showMessageDialog(
							ConsumerFrame,
							"请选择桶数",
							"下单失败",
							JOptionPane.INFORMATION_MESSAGE
						);
				}
				else {
					String sql = "use water_delivery\nexec createOrder "+area+','+building+','+floor+','+room+','+selectedIndex;
					System.out.println(sql);
					try {
						stat.executeUpdate(sql);
						JOptionPane.showMessageDialog(
								ConsumerFrame,
								"下单成功",
								"下单成功",
								JOptionPane.INFORMATION_MESSAGE
							);
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(
								ConsumerFrame,
								e1.toString().substring(48),
								"下单失败",
								JOptionPane.INFORMATION_MESSAGE
							);
						e1.printStackTrace();
					}
				}
			}
			else {
				JOptionPane.showMessageDialog(
						ConsumerFrame,
						"请完善信息",
						"下单失败",
						JOptionPane.INFORMATION_MESSAGE
					);
			}
		}
	}
	public class logoutListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ConsumerFrame.dispose();
			LoginView window=new LoginView(stat,conn);
			window.loginFrame.setVisible(true);
		}
	}
}

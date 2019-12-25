package waterDelivery;

import java.awt.EventQueue;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import waterDelivery.RegisterView.registerListener;

public class AddUserView {

	JFrame AddUserFrame;
	private JTextField usr;
	private JTextField pwd;
	private JTextField confirm;
	private static Statement stat = null;
    private static Connection conn = null;
	private static ResultSet rs = null;
	private static String userName;
	private static int authority;
	private static String[] listData = new String[]{"学生","送水师傅","管理员"};
	private static int selectedIndex;
	/**
	 * Create the application.
	 */
	public AddUserView(Statement stat,Connection conn,String userName,int authority) {
		System.out.println("AddUserFrame");
		AddUserView.stat=stat;
		AddUserView.conn=conn;
		AddUserView.userName=userName;
		AddUserView.authority=authority;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		AddUserFrame = new JFrame();
		AddUserFrame.setBounds(100, 100, 450, 300);
		AddUserFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		AddUserFrame.getContentPane().add(panel, null);
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
		
		JLabel label4 = new JLabel("\u7528\u6237\u7C7B\u578B");
		label4.setBounds(98, 142, 72, 18);
		panel.add(label4);
		
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
		
		JComboBox<String> comboBox=new JComboBox<String>();
		comboBox.setBounds(184, 139, 75, 24);
		for(String str:listData)
			if(str!="管理员"||authority==3)
				comboBox.addItem(str);
		comboBox.setSelectedIndex(selectedIndex=0);
		comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // 只处理选中的状态
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	selectedIndex=comboBox.getSelectedIndex();
                    System.out.println("选中: " + selectedIndex + " = " + comboBox.getSelectedItem());
                }
            }
        });
		panel.add(comboBox);
		
		JButton addUserButton = new JButton("添加");
		addUserButton.setBounds(185, 186, 63, 27);
		addUserButton.addActionListener(new addUserListener());
		panel.add(addUserButton);
	}
	public class addUserListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String usrStr=usr.getText();
			String pwdStr=pwd.getText();
			String sql = "use water_delivery\nSELECT * FROM [User] WHERE userName="+'\''+usr+'\'' ;
			System.out.println(sql);
			try {
				rs=stat.executeQuery(sql);
				if(!rs.next()) {
					sql = "use water_delivery\nexec addUser "+'\''+usrStr+'\'' +','+'\''+pwdStr+'\''+','+(selectedIndex+1)+','+authority ;
					System.out.println(sql);
					try {
						stat.executeUpdate(sql);
					} catch (SQLException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(
								AddUserFrame,
		                        "请重新输入",
		                        e1.toString().substring(48),
		                        JOptionPane.INFORMATION_MESSAGE
		                );
					}
					if(pwdStr.equals(confirm.getText())) {
						JOptionPane.showMessageDialog(
								AddUserFrame,
		                        "添加成功",
		                        "添加成功",
		                        JOptionPane.INFORMATION_MESSAGE
		                );
						AddUserFrame.dispose();
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
								AddUserFrame,
		                        "请重新输入",
		                        "两次输入密码不同",
		                        JOptionPane.INFORMATION_MESSAGE
		                );
					}
				}
				else {
					JOptionPane.showMessageDialog(
							AddUserFrame,
	                        "用户已存在",
	                        "添加失败",
	                        JOptionPane.INFORMATION_MESSAGE
	                );
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			
		}
	}

}

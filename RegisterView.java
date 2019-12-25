package waterDelivery;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.microsoft.sqlserver.jdbc.SQLServerException;


public class RegisterView {

	JFrame registerFrame;
	private JTextField usr;
	private JTextField pwd;
	private JTextField confirm;
	private static Statement stat = null;
    private static Connection conn = null;
	private static ResultSet rs = null;
	private static String[] listData = new String[]{"学生","送水师傅"};
	private static int selectedIndex;
	private JTextField textField;
	
	/**
	 * Create the application.
	 */
	public RegisterView(Statement stat,Connection conn) {
		System.out.println("RegisterFrame");
		RegisterView.stat=stat;
		RegisterView.conn=conn;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		registerFrame = new JFrame();
		registerFrame.setBounds(100, 100, 450, 300);
		registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		registerFrame.getContentPane().add(panel, null);
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
		
		JButton registerButton = new JButton("\u6CE8\u518C");
		registerButton.setBounds(185, 186, 63, 27);
		registerButton.addActionListener(new registerListener());
		panel.add(registerButton);	
		
		registerFrame.setVisible(true);
	}
	
	public class debugListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String buttonText=((JButton)e.getSource()).getText();
			System.out.println("press"+buttonText);
		}
	}
	public class registerListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String usrStr=usr.getText();
			String pwdStr=pwd.getText();
			String sql = "use water_delivery\nSELECT * FROM [User]\nWHERE userName = " + '\'' + usrStr + '\'';
			//System.out.println(usrStr);
			//System.out.println(pwdStr);
			System.out.println(sql);
			try {
				rs = stat.executeQuery(sql);
				if(!rs.next()) {
					try {
						sql = "use water_delivery\nexec addUser "+'\''+usrStr+'\'' +','+'\''+pwdStr+'\'' +','+(selectedIndex+1)+','+4;
						System.out.println(sql);
						stat.executeUpdate(sql);
					}catch(SQLServerException e1){
						System.out.println(e1);
						JOptionPane.showMessageDialog(
		                        registerFrame,
		                        "请重新输入",
		                        e1.toString().substring(48),
		                        JOptionPane.INFORMATION_MESSAGE
		                );
					}
					if(pwdStr.equals(confirm.getText())) {
						JOptionPane.showMessageDialog(
		                        registerFrame,
		                        "注册成功",
		                        "注册成功",
		                        JOptionPane.INFORMATION_MESSAGE
		                );
						registerFrame.dispose();
						LoginView window=new LoginView(stat,conn);
						window.loginFrame.setVisible(true);
					}
					else {
						JOptionPane.showMessageDialog(
		                        registerFrame,
		                        "请重新输入",
		                        "两次输入密码不同",
		                        JOptionPane.INFORMATION_MESSAGE
		                );
					}
				}
				else {
					JOptionPane.showMessageDialog(
	                        registerFrame,
	                        "请重新输入",
	                        "用户名已存在",
	                        JOptionPane.INFORMATION_MESSAGE
	                );
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
}

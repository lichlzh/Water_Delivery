package waterDelivery;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class AdministratorView {

	JFrame AdministratorFrame;
	private static Statement stat = null;
    private static Connection conn = null;
    private static ResultSet rs = null;
    private static String userName;
    private JTextField queryPerformanceField;
    /*private int fromYear,fromMonth,fromDay,toYear,toMonth,toDay;
    private static int[] yearList= new int[] {2019,2020,2011};
    private static int[] monthList= new int[] {1,2,3,4,5,6,7,8,9,10,11,12};
    private static int[] fromDayList= new int[] {1};
    private static int[] toDayList= new int[] {1};
    private static int[] day28= new int[28];
    private static int[] day29= new int[29];
    private static int[] day30= new int[30];
    private static int[] day31= new int[31];*/
    
	/**
	 * Create the application.
	 */
	public AdministratorView(Statement stat,Connection conn,String userName) {
		System.out.println("AdministratorFrame");
		AdministratorView.stat=stat;
		AdministratorView.conn=conn;
		AdministratorView.userName=userName;
		/*for(int i=0;i<28;i++)day28[i]=i+1;
		for(int i=0;i<29;i++)day29[i]=i+1;
		for(int i=0;i<30;i++)day30[i]=i+1;
		for(int i=0;i<31;i++)day31[i]=i+1;*/
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
		queryPerformanceField.setBounds(484, 93, 123, 24);
		panel.add(queryPerformanceField);
		queryPerformanceField.setColumns(8);
		
		JButton queryPerformanceButton = new JButton("\u67E5\u8BE2\u7EE9\u6548");
		queryPerformanceButton.addActionListener(new queryPerformanceListener());
		queryPerformanceButton.setBounds(313, 92, 113, 27);
		panel.add(queryPerformanceButton);
		
		
		/*JComboBox<Integer> fromDayComboBox=new JComboBox<Integer>();
		fromDayComboBox.setBounds(759, 176, 75, 24);
		fromDayComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	fromDay=(int) fromDayComboBox.getSelectedItem();
                    System.out.println("选中: " + fromDay + " = " + fromDayComboBox.getSelectedItem());
                }
            }
        });
		panel.add(fromDayComboBox);
		
		JComboBox<Integer> toDayComboBox=new JComboBox<Integer>();
		toDayComboBox.setBounds(759, 261, 75, 24);
		toDayComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	toDay=(int) toDayComboBox.getSelectedItem();
                    System.out.println("选中: " + toDay + " = " + toDayComboBox.getSelectedItem());
                }
            }
        });
		panel.add(toDayComboBox);
		
		JComboBox<Integer> fromYearComboBox=new JComboBox<Integer>();
		fromYearComboBox.setBounds(497, 176, 75, 24);
		for(int i:yearList)
			fromYearComboBox.addItem(i);
		fromYearComboBox.setSelectedIndex(0);
		fromYear=(int) fromYearComboBox.getSelectedItem();
		fromYearComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	fromYear=(int) fromYearComboBox.getSelectedItem();
                    System.out.println("选中: " + fromYear + " = " + fromYearComboBox.getSelectedItem());
                    fromDayList=modifyDayList(fromYear,fromMonth);
                    fromDayComboBox.removeAllItems();
                    for(int i:fromDayList)
                    	fromDayComboBox.addItem(i);
                }
            }
        });
		panel.add(fromYearComboBox);
		
		JComboBox<Integer> toYearComboBox=new JComboBox<Integer>();
		toYearComboBox.setBounds(497, 261, 75, 24);
		for(int i:yearList)
			toYearComboBox.addItem(i);
		toYearComboBox.setSelectedIndex(0);
		toYear=(int) toYearComboBox.getSelectedItem();
		toYearComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	toYear=(int) toYearComboBox.getSelectedItem();
                    System.out.println("选中: " + toDay + " = " + toYearComboBox.getSelectedItem());
                    toDayList=modifyDayList(toYear,toMonth);
                    toDayComboBox.removeAllItems();
                    for(int i:toDayList)
                    	toDayComboBox.addItem(i);
                }
            }
        });
		panel.add(toYearComboBox);
		
		JComboBox<Integer> fromMonthComboBox=new JComboBox<Integer>();
		fromMonthComboBox.setBounds(624, 176, 75, 24);
		for(int i:monthList)
			fromMonthComboBox.addItem(i);
		fromMonthComboBox.setSelectedIndex(0);
    	fromMonth=(int) fromMonthComboBox.getSelectedItem();
		fromMonthComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	fromMonth=(int) fromMonthComboBox.getSelectedItem();
                    System.out.println("选中: " + fromMonth + " = " + fromMonthComboBox.getSelectedItem());
                    fromDayList=modifyDayList(fromYear,fromMonth);
                    fromDayComboBox.removeAllItems();
                    for(int i:fromDayList)
                    	fromDayComboBox.addItem(i);
                }
            }
        });
		panel.add(fromMonthComboBox);
		
		JComboBox<Integer> toMonthComboBox=new JComboBox<Integer>();
		toMonthComboBox.setBounds(624, 261, 75, 24);
		for(int i:monthList)
			toMonthComboBox.addItem(i);
		toMonthComboBox.setSelectedIndex(0);
		toMonth=(int) toMonthComboBox.getSelectedItem();
		toMonthComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	toMonth=(int) toMonthComboBox.getSelectedItem();
                    System.out.println("选中: " + toMonth + " = " + toMonthComboBox.getSelectedItem());
                    toDayList=modifyDayList(toYear,toMonth);
                    toDayComboBox.removeAllItems();
                    for(int i:toDayList)
                    	toDayComboBox.addItem(i);
                }
            }
        });
		panel.add(toMonthComboBox);
		fromDayList=modifyDayList(fromYear,fromMonth);
		fromDayList=modifyDayList(toYear,toMonth);
		fromDayComboBox.removeAllItems();
        for(int i:fromDayList) {
        	fromDayComboBox.addItem(i);
        	System.out.printf("%d", i);
        }
		System.out.println();
		toDayComboBox.removeAllItems();
        for(int i:toDayList){
        	toDayComboBox.addItem(i);
        	System.out.printf("%d", i);
        }*/
		JButton queryMostButton = new JButton("\u8BA2\u5355\u6700\u591A");
		queryMostButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "use water_delivery\n" + 
						"SELECT delivererNumber FROM Performance WHERE orderCount=(\n" + 
						"	SELECT max(orderCount) FROM Performance\n" + 
						")";
				System.out.println(sql);
				try {
					rs=stat.executeQuery(sql);
					if(rs.next()) {
						String res="";
						do {res+=rs.getObject("delivererNumber").toString()+' ';}while(rs.next());
						JOptionPane.showMessageDialog(
								AdministratorFrame,
								res,
								"查询成功",
								JOptionPane.INFORMATION_MESSAGE
						);
					}
					else {
						JOptionPane.showMessageDialog(
								AdministratorFrame,
								"无",
								"查询失败",
								JOptionPane.INFORMATION_MESSAGE
						);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		queryMostButton.setBounds(313, 175, 113, 27);
		panel.add(queryMostButton);
		
		JButton queryBestButton = new JButton("\u51C6\u65F6\u7387\u6700\u9AD8");
		queryBestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "use water_delivery\n" + 
						"SELECT delivererNumber FROM Performance WHERE accuracy=(\n" + 
						"	SELECT max(accuracy) FROM Performance\n" + 
						")";
				System.out.println(sql);
				try {
					rs=stat.executeQuery(sql);
					if(rs.next()) {
						String res="";
						do {res+=rs.getObject("delivererNumber").toString()+' ';}while(rs.next());
						JOptionPane.showMessageDialog(
								AdministratorFrame,
								res,
								"查询成功",
								JOptionPane.INFORMATION_MESSAGE
						);
					}
					else {
						JOptionPane.showMessageDialog(
								AdministratorFrame,
								"无",
								"查询失败",
								JOptionPane.INFORMATION_MESSAGE
						);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		queryBestButton.setBounds(313, 260, 113, 27);
		panel.add(queryBestButton);
		
		JButton logoutButton = new JButton("退出登录");
		logoutButton.setBounds(755, 513, 113, 27);
		logoutButton.addActionListener(new logoutListener());
		panel.add(logoutButton);
		
		/*JLabel label_1 = new JLabel("\u5E74");
		label_1.setBounds(582, 179, 25, 18);
		panel.add(label_1);
		
		JLabel label_2 = new JLabel("\u5E74");
		label_2.setBounds(582, 264, 25, 18);
		panel.add(label_2);
		
		JLabel label_3 = new JLabel("\u6708");
		label_3.setBounds(713, 179, 32, 18);
		panel.add(label_3);
		
		JLabel label_4 = new JLabel("\u6708");
		label_4.setBounds(713, 264, 32, 18);
		panel.add(label_4);
		
		JLabel label_5 = new JLabel("\u65E5");
		label_5.setBounds(843, 179, 25, 18);
		panel.add(label_5);
		
		JLabel label_6 = new JLabel("\u65E5");
		label_6.setBounds(843, 264, 25, 18);
		panel.add(label_6);
		
		JLabel label_7 = new JLabel("\u4ECE");
		label_7.setBounds(450, 179, 33, 18);
		panel.add(label_7);
		
		JLabel label_8 = new JLabel("\u5230");
		label_8.setBounds(450, 264, 33, 18);
		panel.add(label_8);*/
	}
	/*public int[] modifyDayList(int y,int m) {
		System.out.println("modifyDayList");
		if (y!=0&&m!=0) {
			if(m==2) {
				if(y%400==0||(y%4==0&&y%100!=0))return day29;
				else return day28;
			}
			else if(m==1||m==3||m==5||m==7||m==8||m==10||m==12) 
				return day31;
			else 
				return day30;
		}
		else
			return new int[] {1};
	}*/
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

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

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.microsoft.sqlserver.jdbc.SQLServerException;


import javax.swing.JButton;

public class DelivererFillInInfoView {

	JFrame DelivererFillInInfoFrame;
	private static Statement stat = null;
    private static Connection conn = null;
    private static ResultSet rs = null;
    private static String userName,delivererNumber,name,telephone;
    private static int[] listData = new int[] {0,1,2,3,4,5,6,7,8};
    private static int selectedIndex;
    private JTextField nameField;
    private JTextField telephoneField;
	/**
	 * Create the application.
	 */
	public DelivererFillInInfoView(Statement stat,Connection conn,String userName,String delivererNumber) {
		System.out.println("DelivererFillInInfoFrame");
		DelivererFillInInfoView.stat=stat;
		DelivererFillInInfoView.conn=conn;
		DelivererFillInInfoView.userName=userName;
		DelivererFillInInfoView.delivererNumber=delivererNumber;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		DelivererFillInInfoFrame = new JFrame();
		DelivererFillInInfoFrame.setBounds(100, 100, 450, 300);
		DelivererFillInInfoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		DelivererFillInInfoFrame.getContentPane().add(panel, null);
		panel.setLayout(null);
		
		JLabel label1 = new JLabel("\u7535\u8BDD\u53F7\u7801");
		label1.setBounds(61, 93, 72, 18);
		panel.add(label1);
		
		telephoneField = new JTextField();
		telephoneField.setBounds(156, 90, 179, 24);
		panel.add(telephoneField);
		telephoneField.setColumns(11);
		
		JLabel label2 = new JLabel("\u8D1F\u8D23\u56F4\u5408");
		label2.setBounds(61, 149, 72, 18);
		panel.add(label2);
		
		JComboBox<Integer> comboBox=new JComboBox<Integer>();
		comboBox.setBounds(156, 146, 75, 24);
		for(int i:listData)
			comboBox.addItem(i);
		comboBox.setSelectedIndex(0);
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
		
		JLabel label3 = new JLabel("姓名");
		label3.setBounds(61, 47, 72, 18);
		panel.add(label3);
		
		nameField = new JTextField();
		nameField.setBounds(155, 44, 103, 24);
		panel.add(nameField);
		nameField.setColumns(10);
		
		JButton submitButton = new JButton("\u63D0\u4EA4");
		submitButton.setBounds(156, 198, 63, 27);
		submitButton.addActionListener(new submitListener());
		panel.add(submitButton);
		
		String sql = "use water_delivery\nSELECT * FROM Deliverer\nWHERE delivererNumber = " + '\'' + delivererNumber + '\'';
		System.out.println(sql);
		try {
			rs=stat.executeQuery(sql);
			if(rs.next()) {
				name=(String) rs.getObject("name");
				telephone=(String) rs.getObject("telephone");
				if(name != null) 
					nameField.setText(name);
				if(telephone != null)
					telephoneField.setText(telephone);
			}else {
				System.out.println("ERROR");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		sql = "use water_delivery\nSELECT * FROM InCharge\nWHERE delivererNumber = " + '\'' + delivererNumber + '\'';
		System.out.println(sql);
		try {
			rs=stat.executeQuery(sql);
			if(rs.next()) {
				selectedIndex=(int) rs.getObject("area");
				comboBox.setSelectedIndex(selectedIndex);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public class submitListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			name=nameField.getText();
			telephone=telephoneField.getText();
			System.out.println(name+' '+telephone);
			String sql = "use water_delivery\nUPDATE Deliverer\n set name="+'\''+name+'\''+','+"telephone="+'\''+telephone+'\''+"\nWHERE delivererNumber="+'\''+ delivererNumber +'\'';
			System.out.println(sql);
			try {
				stat.executeUpdate(sql);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			sql = "use water_delivery\nUPDATE InCharge\n set area="+selectedIndex+"\nWHERE delivererNumber="+'\''+delivererNumber+'\'';
			System.out.println(sql);
			try {
				stat.executeUpdate(sql);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			DelivererFillInInfoFrame.dispose();
			DelivererView window=new DelivererView(stat,conn,userName);
			window.DelivererFrame.setVisible(true);
		}
	}
}

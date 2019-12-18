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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

public class DealOrderView {

	JFrame DealOrderFrame;
	private static Statement stat = null;
    private static Connection conn = null;
    private static ResultSet rs = null;
    private static String userName,delivererNumber;
    private static String orderList=null;
    private static int selectedIndex=0;
    private static List<String> orderNumber;
	/**
	 * Create the application.
	 */
	public DealOrderView(Statement stat,Connection conn,String userName,String delivererNumber) {
		System.out.println("DealOrderFrame");
		DealOrderView.stat=stat;
		DealOrderView.conn=conn;
		DealOrderView.userName=userName;
		DealOrderView.delivererNumber=delivererNumber;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		DealOrderFrame = new JFrame();
		DealOrderFrame.setBounds(100, 100, 450, 300);
		DealOrderFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		DealOrderFrame.getContentPane().add(panel, null);
		panel.setLayout(null);
		
		JComboBox<String> comboBox=new JComboBox<String>();
		comboBox.setMaximumRowCount(100);
		comboBox.setBounds(125, 45, 293, 25);
		String sql ="SELECT [Order].orderNumber,[Order].orderTime,[Order].pridictTime,DeliverTo.area,DeliverTo.building,DeliverTo.floor,DeliverTo.room,DeliverTo.barrelNumber\n" + 
					"FROM [Order],DeliverTo,ReceiveOrder\n" + 
					"WHERE [Order].orderNumber=DeliverTo.orderNumber and [Order].orderNumber=ReceiveOrder.orderNumber \n" + 
					"and ReceiveOrder.delivererNumber="+'\''+delivererNumber+'\''+" and [Order].endTime is null\n" + 
					"Order by [Order].pridictTime " ;
		System.out.println(sql);
		try {
			rs=stat.executeQuery(sql);
			if(rs.next()) {
				orderNumber=new ArrayList<String>();
				do {
					orderNumber.add(rs.getObject("orderNumber").toString());
					String tmp=rs.getObject("area").toString()+"围合 "
							  +rs.getObject("building").toString()+"楼 "
							  +rs.getObject("floor").toString()+"层 "
							  +rs.getObject("room").toString()+"室 "+'\n'
							  +rs.getObject("pridictTime").toString().substring(11,19)+"  "
							  +rs.getObject("barrelNumber").toString()+"桶 ";
					comboBox.addItem(tmp);
				}while(rs.next());
			}
			else {
				comboBox.addItem("暂无");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
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
		
		JLabel label = new JLabel("\u5F85\u5904\u7406");
		label.setBounds(25, 48, 72, 18);
		panel.add(label);
		
		JButton arriveButton = new JButton("\u9001\u8FBE");
		arriveButton.setBounds(106, 166, 113, 27);
		arriveButton.addActionListener(new arriveListener());
		panel.add(arriveButton);
		
		JButton backButton = new JButton("\u8FD4\u56DE");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DealOrderFrame.dispose();
				DelivererView window=new DelivererView(stat,conn,userName);
				window.DelivererFrame.setVisible(true);
			}
		});
		backButton.setBounds(242, 166, 113, 27);
		
		panel.add(backButton);
	}
	public class arriveListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String sql = "use water_delivery\nexec serveOrder "+'\''+orderNumber.get(selectedIndex)+'\'' ;
			System.out.println(sql);
			try {
				stat.executeUpdate(sql);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			DealOrderFrame.dispose();
			DelivererView window=new DelivererView(stat,conn,userName);
			window.DelivererFrame.setVisible(true);
		}
	}
}

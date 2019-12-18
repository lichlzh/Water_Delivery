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
import javax.swing.JButton;

public class QueryDelivererView {

	JFrame QueryDelivererFrame;
	private static Statement stat = null;
    private static Connection conn = null;
    private static ResultSet rs = null;
    private static String userName;
    private static int authority,selectedArea;
	/**
	 * Create the application.
	 */
	public QueryDelivererView(Statement stat,Connection conn,String userName,int authority) {
		QueryDelivererView.stat=stat;
		QueryDelivererView.conn=conn;
		QueryDelivererView.userName=userName;
		QueryDelivererView.authority=authority;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		QueryDelivererFrame = new JFrame();
		QueryDelivererFrame.setBounds(100, 100, 450, 300);
		QueryDelivererFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		QueryDelivererFrame.getContentPane().add(panel, null);
		panel.setLayout(null);
		
		JLabel label1 = new JLabel("\u7F16\u53F7");
		label1.setBounds(202, 84, 72, 18);
		panel.add(label1);
		
		JLabel label2 = new JLabel("\u59D3\u540D");
		label2.setBounds(202, 134, 72, 18);
		panel.add(label2);
		
		JLabel label3 = new JLabel("");
		label3.setBounds(277, 84, 72, 18);
		panel.add(label3);
		
		JLabel label4 = new JLabel("");
		label4.setBounds(277, 134, 72, 18);
		panel.add(label4);
		
		JComboBox<Integer> queryComboBox=new JComboBox<Integer>();
		queryComboBox.setBounds(87, 104, 75, 24);
		for(int i=0;i<=8;i++)
			queryComboBox.addItem(i);
		queryComboBox.setSelectedIndex(0);
		queryComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	selectedArea=(int) queryComboBox.getSelectedItem();
                    System.out.println("选中: " + queryComboBox.getSelectedIndex() + " = " + queryComboBox.getSelectedItem());
                    String sql = "use water_delivery\nSELECT delivererNumber FROM InCharge WHERE area="+selectedArea;
                    System.out.println(sql);
                    try {
						rs=stat.executeQuery(sql);
						if(rs.next()) {
							String tmp=rs.getObject("delivererNumber").toString();
							label3.setText(tmp);
							sql = "use water_delivery\nSELECT name FROM Deliverer WHERE delivererNumber="+'\''+tmp+'\'';
		                    System.out.println(sql);
		                    rs=stat.executeQuery(sql);
		                    if(rs.next())
		                    	label4.setText(rs.getObject("name").toString());
		                    else
		                    	label4.setText("暂无");
						}
						else {
							label3.setText("暂无");
							label4.setText("暂无");
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
                }
            }
        });
		panel.add(queryComboBox);
		
		JLabel label5 = new JLabel("\u9009\u62E9\u56F4\u5408");
		label5.setBounds(14, 107, 72, 18);
		panel.add(label5);
		
		JButton backButton = new JButton("\u8FD4\u56DE");
		backButton.setBounds(138, 188, 113, 27);
		backButton.addActionListener(new backListener());
		panel.add(backButton);
	}
	public class backListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			QueryDelivererFrame.dispose();
			if(authority==1) {
				ConsumerView window=new ConsumerView(stat,conn,userName);
				window.ConsumerFrame.setVisible(true);
			}
			else if(authority==2) {
				DelivererView window=new DelivererView(stat,conn,userName);
				window.DelivererFrame.setVisible(true);
			}
			else {
				AdministratorView window=new AdministratorView(stat,conn,userName);
				window.AdministratorFrame.setVisible(true);
			}
		}
	}
}

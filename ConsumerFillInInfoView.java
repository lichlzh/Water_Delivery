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

public class ConsumerFillInInfoView {

	JFrame ConsumerFillInInfoFrame;
	private static Statement stat = null;
    private static Connection conn = null;
    private static ResultSet rs = null;
    private static String userName;
    private static int selectedArea,selectedBuilding,selectedFloor,selectedRoom;
	/**
	 * Create the application.
	 */
	public ConsumerFillInInfoView(Statement stat,Connection conn,String userName,int area,int building,int floor,int room) {
		System.out.println("ConsumerFillInInfoFrame");
		ConsumerFillInInfoView.stat=stat;
		ConsumerFillInInfoView.conn=conn;
		ConsumerFillInInfoView.userName=userName;
		ConsumerFillInInfoView.selectedArea=area;
		ConsumerFillInInfoView.selectedBuilding=building;
		ConsumerFillInInfoView.selectedFloor=floor;
		ConsumerFillInInfoView.selectedRoom=room;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		ConsumerFillInInfoFrame = new JFrame();
		ConsumerFillInInfoFrame.setBounds(100, 100, 450, 300);
		ConsumerFillInInfoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		ConsumerFillInInfoFrame.getContentPane().add(panel, null);
		panel.setLayout(null);
		
		
		JComboBox<Integer> roomComboBox=new JComboBox<Integer>();
		roomComboBox.setBounds(185, 166, 75, 24);
		roomComboBox.addItem(0);
        for(int i=1;i<=20;i++)
        	roomComboBox.addItem(selectedFloor*100+i);
		roomComboBox.setSelectedIndex(selectedRoom%100);
		roomComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	selectedRoom=(int) roomComboBox.getSelectedItem();
                    System.out.println("选中: " + roomComboBox.getSelectedIndex() + " = " + roomComboBox.getSelectedItem());
                }
            }
        });
		panel.add(roomComboBox);
		
		JComboBox<Integer> floorComboBox=new JComboBox<Integer>();
		floorComboBox.setBounds(185, 123, 75, 24);
		for(int i=0;i<=7;i++)
			floorComboBox.addItem(i);
		floorComboBox.setSelectedIndex(selectedFloor);
		floorComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	selectedFloor=(int) floorComboBox.getSelectedItem();
                    System.out.println("选中: " + floorComboBox.getSelectedIndex() + " = " + floorComboBox.getSelectedItem());
                    roomComboBox.removeAllItems();
                    roomComboBox.addItem(0);
                    for(int i=1;i<=20;i++)
                    	roomComboBox.addItem(selectedFloor*100+i);
                    roomComboBox.setSelectedIndex(0);
                }
            }
        });
		panel.add(floorComboBox);
		
		JComboBox<Integer> buildingComboBox=new JComboBox<Integer>();
		buildingComboBox.setBounds(185, 79, 75, 24);
		buildingComboBox.addItem(0);
		for(int i=(selectedArea-1)*4+1;i<=selectedArea*4;i++)
        	buildingComboBox.addItem(i);
		if (selectedBuilding==0)
			buildingComboBox.setSelectedIndex(0);
		else
			buildingComboBox.setSelectedIndex((selectedBuilding+3)%4+1);
		buildingComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	selectedBuilding=(int) buildingComboBox.getSelectedItem();
                    System.out.println("选中: " + buildingComboBox.getSelectedIndex() + " = " + buildingComboBox.getSelectedItem());
                }
            }
        });
		panel.add(buildingComboBox);

		JComboBox<Integer> areaComboBox=new JComboBox<Integer>();
		areaComboBox.setBounds(185, 35, 75, 24);
		for(int i=0;i<=8;i++)
			areaComboBox.addItem(i);
		areaComboBox.setSelectedIndex(selectedArea);
		areaComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	selectedArea=(int) areaComboBox.getSelectedItem();
                    System.out.println("选中: " + areaComboBox.getSelectedIndex() + " = " + areaComboBox.getSelectedItem());
                    buildingComboBox.removeAllItems();
                    buildingComboBox.addItem(0);
                    for(int i=(selectedArea-1)*4+1;i<=selectedArea*4;i++)
                    	buildingComboBox.addItem(i);
                    buildingComboBox.setSelectedIndex(0);
                }
            }
        });
		panel.add(areaComboBox);
		
		JLabel label1 = new JLabel("\u56F4\u5408");
		label1.setBounds(99, 38, 72, 18);
		panel.add(label1);
		
		JLabel label2 = new JLabel("\u697C\u53F7");
		label2.setBounds(99, 82, 72, 18);
		panel.add(label2);
		
		JLabel label3 = new JLabel("\u697C\u5C42");
		label3.setBounds(99, 126, 72, 18);
		panel.add(label3);
		
		JLabel label4 = new JLabel("\u623F\u95F4");
		label4.setBounds(99, 169, 72, 18);
		panel.add(label4);
		
		JButton submitButton = new JButton("\u63D0\u4EA4");
		submitButton.setBounds(178, 213, 63, 27);
		submitButton.addActionListener(new submitListener());
		panel.add(submitButton);
	}
	public class submitListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String sql = "use water_delivery\nUPDATE Domitory\n"
					+"set area="+ selectedArea +','
					+"building="+ selectedBuilding +','
					+"floor="+ selectedFloor +','
					+"room="+ selectedRoom +'\n'
					+"WHERE userName="+'\''+ userName +'\'';
			System.out.println(sql);
			try {
				stat.executeUpdate(sql);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			ConsumerFillInInfoFrame.dispose();
			ConsumerView window=new ConsumerView(stat,conn,userName);
			window.ConsumerFrame.setVisible(true);
		}
	}
}

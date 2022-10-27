package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JSplitPane;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Properties;
import java.awt.event.ActionEvent;
import javax.swing.ScrollPaneConstants;
import javax.swing.JList;
import javax.swing.AbstractListModel;

public class GUI extends JFrame {

	private CardLayout cardLayout;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		setPreferredSize(new Dimension(500, 400));
	    pack();
	    setLocationRelativeTo(null);
	    
		
		cardLayout = new CardLayout(0, 0);
		contentPane.setLayout(cardLayout);
		
		JPanel panelLogin = new JPanel();
		contentPane.add(panelLogin, "name_1005522058002500");
		
		JPanel panelManagerMainMenu = new JPanel();
		contentPane.add(panelManagerMainMenu, "name_1005555845259100");
		panelManagerMainMenu.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panelManagerMainMenu.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 9));
		panel_3.add(btnLogout);
		
		JPanel panelManagerShiftMenu = new JPanel();
		contentPane.add(panelManagerShiftMenu, "name_1007777309751200");
		panelManagerShiftMenu.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panelManagerShiftMenu.add(panel_1, BorderLayout.NORTH);
		
		JButton btnNewButton_4 = new JButton("Logout");
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 9));
		panel_1.add(btnNewButton_4);
		
		JPanel panelReleaseNewWorkShifts = new JPanel();
		contentPane.add(panelReleaseNewWorkShifts, "name_1008271951056900");
		panelReleaseNewWorkShifts.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_12 = new JPanel();
		panelReleaseNewWorkShifts.add(panel_12, BorderLayout.NORTH);
		
		JLabel lblNewLabel_3 = new JLabel("Release Shift");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_12.add(lblNewLabel_3);
		
		JPanel panel_13 = new JPanel();
		panelReleaseNewWorkShifts.add(panel_13, BorderLayout.WEST);
		GridBagLayout gbl_panel_13 = new GridBagLayout();
		gbl_panel_13.columnWidths = new int[]{0, 0};
		gbl_panel_13.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_13.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_13.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_13.setLayout(gbl_panel_13);
		
		JLabel lblNewLabel_2 = new JLabel("Date:");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 0;
		panel_13.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Day");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		 
		GridBagConstraints gbc_datePicker = new GridBagConstraints();
		gbc_datePicker.insets = new Insets(0, 0, 5, 0);
		gbc_datePicker.gridx = 0;
		gbc_datePicker.gridy = 1;
		panel_13.add(datePicker, gbc_datePicker);
		
		JLabel lblNewLabel_9 = new JLabel("Employee type:");
		GridBagConstraints gbc_lblNewLabel_9 = new GridBagConstraints();
		gbc_lblNewLabel_9.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_9.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_9.gridx = 0;
		gbc_lblNewLabel_9.gridy = 2;
		panel_13.add(lblNewLabel_9, gbc_lblNewLabel_9);
		
		JComboBox<String> comboBoxEmployeeType = new JComboBox<>();
		GridBagConstraints gbc_comboBoxEmployeeType = new GridBagConstraints();
		gbc_comboBoxEmployeeType.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxEmployeeType.fill = GridBagConstraints.BOTH;
		gbc_comboBoxEmployeeType.gridx = 0;
		gbc_comboBoxEmployeeType.gridy = 3;
		panel_13.add(comboBoxEmployeeType, gbc_comboBoxEmployeeType);
		comboBoxEmployeeType.addItem("");
		comboBoxEmployeeType.addItem("Part-time");
		comboBoxEmployeeType.addItem("Full-time");
		comboBoxEmployeeType.addItem("All");
		
		JLabel lblNewLabel_10 = new JLabel("From time:");
		GridBagConstraints gbc_lblNewLabel_10 = new GridBagConstraints();
		gbc_lblNewLabel_10.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_10.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_10.gridx = 0;
		gbc_lblNewLabel_10.gridy = 4;
		panel_13.add(lblNewLabel_10, gbc_lblNewLabel_10);
		
		JComboBox<String> comboBoxShiftFrom = new JComboBox<>();
		GridBagConstraints gbc_comboBoxShiftFrom = new GridBagConstraints();
		gbc_comboBoxShiftFrom.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxShiftFrom.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxShiftFrom.gridx = 0;
		gbc_comboBoxShiftFrom.gridy = 5;
		panel_13.add(comboBoxShiftFrom, gbc_comboBoxShiftFrom);
		comboBoxShiftFrom.addItem("");
		comboBoxShiftFrom.addItem("7:00");
		comboBoxShiftFrom.addItem("12:00");
		comboBoxShiftFrom.addItem("16:00");
		
		JLabel lblNewLabel_11 = new JLabel("To time:");
		GridBagConstraints gbc_lblNewLabel_11 = new GridBagConstraints();
		gbc_lblNewLabel_11.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_11.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_11.gridx = 0;
		gbc_lblNewLabel_11.gridy = 6;
		panel_13.add(lblNewLabel_11, gbc_lblNewLabel_11);
		
		JComboBox<String> comboBoxShiftTo = new JComboBox<>();
		GridBagConstraints gbc_comboBoxShiftTo = new GridBagConstraints();
		gbc_comboBoxShiftTo.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxShiftTo.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxShiftTo.gridx = 0;
		gbc_comboBoxShiftTo.gridy = 7;
		panel_13.add(comboBoxShiftTo, gbc_comboBoxShiftTo);
		comboBoxShiftTo.addItem("");
		comboBoxShiftTo.addItem("12:00");
		comboBoxShiftTo.addItem("16:00");
		comboBoxShiftTo.addItem("22:00");
		
		JButton btnNewButton_7 = new JButton("Add");
		GridBagConstraints gbc_btnNewButton_7 = new GridBagConstraints();
		gbc_btnNewButton_7.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_7.gridx = 0;
		gbc_btnNewButton_7.gridy = 9;
		panel_13.add(btnNewButton_7, gbc_btnNewButton_7);
		
		JButton btnDeleteShift = new JButton("Delete");
		GridBagConstraints gbc_btnDeleteShift = new GridBagConstraints();
		gbc_btnDeleteShift.insets = new Insets(0, 0, 5, 0);
		gbc_btnDeleteShift.gridx = 0;
		gbc_btnDeleteShift.gridy = 10;
		panel_13.add(btnDeleteShift, gbc_btnDeleteShift);
		
		// https://www.tutorialspoint.com/how-to-add-scrollbar-to-jlist-in-java
		// Link til tutorial der viser hvordan man evt. kan tilf�je elementer til sin liste.
		JList list = new JList<>();
		
		JScrollPane scrollPane = new JScrollPane();
		panelReleaseNewWorkShifts.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(list);
		
		
		panelLogin.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_14 = new JPanel();
		panelLogin.add(panel_14, BorderLayout.NORTH);
		
		JLabel lblNewLabel_4 = new JLabel("Login");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_14.add(lblNewLabel_4);
		
		JPanel panel_15 = new JPanel();
		panelLogin.add(panel_15, BorderLayout.CENTER);
		GridBagLayout gbl_panel_15 = new GridBagLayout();
		gbl_panel_15.columnWidths = new int[]{0, 0};
		gbl_panel_15.rowHeights = new int[]{0, 0, 0};
		gbl_panel_15.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_15.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panel_15.setLayout(gbl_panel_15);
		
		JPanel panel_16 = new JPanel();
		GridBagConstraints gbc_panel_16 = new GridBagConstraints();
		gbc_panel_16.anchor = GridBagConstraints.SOUTH;
		gbc_panel_16.insets = new Insets(0, 0, 5, 0);
		gbc_panel_16.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_16.gridx = 0;
		gbc_panel_16.gridy = 0;
		panel_15.add(panel_16, gbc_panel_16);
		
		JLabel lblNewLabel_5 = new JLabel("Username");
		panel_16.add(lblNewLabel_5);
		
		textField = new JTextField();
		panel_16.add(textField);
		textField.setColumns(10);
		
		JPanel panel_17 = new JPanel();
		GridBagConstraints gbc_panel_17 = new GridBagConstraints();
		gbc_panel_17.fill = GridBagConstraints.BOTH;
		gbc_panel_17.gridx = 0;
		gbc_panel_17.gridy = 1;
		panel_15.add(panel_17, gbc_panel_17);
		
		JLabel lblNewLabel_6 = new JLabel("Password");
		panel_17.add(lblNewLabel_6);
		
		textField_1 = new JTextField();
		panel_17.add(textField_1);
		textField_1.setColumns(10);
		
		JPanel panel_18 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_18.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		panelLogin.add(panel_18, BorderLayout.SOUTH);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getNextCard();
			}
		});
		panel_18.add(btnLogin);
		
		JPanel panel = new JPanel();
		panelManagerMainMenu.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.VERTICAL;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 0;
		panel.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JLabel lblNewLabel = new JLabel("Main Menu");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel_2.add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblNewLabel_7 = new JLabel("  ");
		GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
		gbc_lblNewLabel_7.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_7.gridx = 0;
		gbc_lblNewLabel_7.gridy = 1;
		panel_2.add(lblNewLabel_7, gbc_lblNewLabel_7);
		
		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 1;
		panel.add(panel_4, gbc_panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[]{0, 0};
		gbl_panel_4.rowHeights = new int[]{0, 0};
		gbl_panel_4.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);
		
		JPanel panel_4_1 = new JPanel();
		GridBagConstraints gbc_panel_4_1 = new GridBagConstraints();
		gbc_panel_4_1.fill = GridBagConstraints.BOTH;
		gbc_panel_4_1.gridx = 0;
		gbc_panel_4_1.gridy = 0;
		panel_4.add(panel_4_1, gbc_panel_4_1);
		
		JPanel panel_5 = new JPanel();
		panel_4_1.add(panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{0, 0};
		gbl_panel_5.rowHeights = new int[]{0, 0, 0};
		gbl_panel_5.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		
		JButton btnNewButton = new JButton("Work schedule");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		panel_5.add(btnNewButton, gbc_btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Employee");
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 1;
		panel_5.add(btnNewButton_1, gbc_btnNewButton_1);
		
		JPanel panel_6 = new JPanel();
		panel_4_1.add(panel_6);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[]{0, 0};
		gbl_panel_6.rowHeights = new int[]{0, 0, 0};
		gbl_panel_6.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_6.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panel_6.setLayout(gbl_panel_6);
		
		JButton btnNewButton_2 = new JButton("Work shifts");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getNextCard();
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_2.gridx = 0;
		gbc_btnNewButton_2.gridy = 0;
		panel_6.add(btnNewButton_2, gbc_btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Data");
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton_3.gridx = 0;
		gbc_btnNewButton_3.gridy = 1;
		panel_6.add(btnNewButton_3, gbc_btnNewButton_3);
		
		
		JPanel panel_7 = new JPanel();
		panelManagerShiftMenu.add(panel_7, BorderLayout.CENTER);
		GridBagLayout gbl_panel_7 = new GridBagLayout();
		gbl_panel_7.columnWidths = new int[]{0, 0};
		gbl_panel_7.rowHeights = new int[]{0, 0, 0};
		gbl_panel_7.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_7.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel_7.setLayout(gbl_panel_7);
		
		JPanel panel_8 = new JPanel();
		GridBagConstraints gbc_panel_8 = new GridBagConstraints();
		gbc_panel_8.insets = new Insets(0, 0, 5, 0);
		gbc_panel_8.fill = GridBagConstraints.VERTICAL;
		gbc_panel_8.gridx = 0;
		gbc_panel_8.gridy = 0;
		panel_7.add(panel_8, gbc_panel_8);
		GridBagLayout gbl_panel_8 = new GridBagLayout();
		gbl_panel_8.columnWidths = new int[]{0, 0};
		gbl_panel_8.rowHeights = new int[]{0, 0, 0};
		gbl_panel_8.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_8.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_8.setLayout(gbl_panel_8);
		
		JLabel lblNewLabel_1 = new JLabel("Shift Menu");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		panel_8.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		JLabel lblNewLabel_8 = new JLabel("  ");
		GridBagConstraints gbc_lblNewLabel_8 = new GridBagConstraints();
		gbc_lblNewLabel_8.gridx = 0;
		gbc_lblNewLabel_8.gridy = 1;
		panel_8.add(lblNewLabel_8, gbc_lblNewLabel_8);
		
		JPanel panel_9 = new JPanel();
		GridBagConstraints gbc_panel_9 = new GridBagConstraints();
		gbc_panel_9.fill = GridBagConstraints.BOTH;
		gbc_panel_9.gridx = 0;
		gbc_panel_9.gridy = 1;
		panel_7.add(panel_9, gbc_panel_9);
		
		JPanel panel_10 = new JPanel();
		panel_9.add(panel_10);
		GridBagLayout gbl_panel_10 = new GridBagLayout();
		gbl_panel_10.columnWidths = new int[]{0, 0};
		gbl_panel_10.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_10.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_10.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		panel_10.setLayout(gbl_panel_10);
		
		JButton btnNewButton_5 = new JButton("Release New");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getThisCard("ReleaseNewWorkShifts");
			}
		});
		btnNewButton_5.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnNewButton_5 = new GridBagConstraints();
		gbc_btnNewButton_5.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton_5.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_5.gridx = 0;
		gbc_btnNewButton_5.gridy = 0;
		panel_10.add(btnNewButton_5, gbc_btnNewButton_5);
		
		JButton btnSeeReleased = new JButton("See Released");
		btnSeeReleased.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnSeeReleased = new GridBagConstraints();
		gbc_btnSeeReleased.fill = GridBagConstraints.BOTH;
		gbc_btnSeeReleased.insets = new Insets(0, 0, 5, 0);
		gbc_btnSeeReleased.gridx = 0;
		gbc_btnSeeReleased.gridy = 1;
		panel_10.add(btnSeeReleased, gbc_btnSeeReleased);
		
		JButton btnNewButton_6 = new JButton("Take Shift");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getThisCard("TakeShift");
			}
		});
		btnNewButton_6.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnNewButton_6 = new GridBagConstraints();
		gbc_btnNewButton_6.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton_6.gridx = 0;
		gbc_btnNewButton_6.gridy = 2;
		panel_10.add(btnNewButton_6, gbc_btnNewButton_6);
		
		JPanel panel_19 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_19.getLayout();
		flowLayout_2.setAlignment(FlowLayout.RIGHT);
		panelReleaseNewWorkShifts.add(panel_19, BorderLayout.SOUTH);
		
		JButton btnCancelReleaseShift = new JButton("Cancel");
		panel_19.add(btnCancelReleaseShift);
		
		JButton btnCompleteReleaseShift = new JButton("Complete");
		panel_19.add(btnCompleteReleaseShift);
		
		JPanel panel_11 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_11.getLayout();
		flowLayout_3.setAlignment(FlowLayout.RIGHT);
		panelManagerShiftMenu.add(panel_11, BorderLayout.SOUTH);
		
		JButton btnCancelShiftMenu = new JButton("Cancel");
		btnCancelShiftMenu.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_11.add(btnCancelShiftMenu);
		
		JPanel panelTakeShift = new JPanel();
		contentPane.add(panelTakeShift, "name_1023562824879300");
		panelTakeShift.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_20 = new JPanel();
		panelTakeShift.add(panel_20, BorderLayout.NORTH);
		
		JLabel lblNewLabel_12 = new JLabel("Take Shift");
		lblNewLabel_12.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_20.add(lblNewLabel_12);
		
		JPanel panel_21 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_21.getLayout();
		flowLayout_4.setAlignment(FlowLayout.RIGHT);
		panelTakeShift.add(panel_21, BorderLayout.SOUTH);
		
		JButton btnCancelTakeShift = new JButton("Cancel");
		btnCancelTakeShift.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_21.add(btnCancelTakeShift);
		
		JButton btnCompleteTakeShift = new JButton("Complete");
		panel_21.add(btnCompleteTakeShift);
		
		JPanel panel_22 = new JPanel();
		panelTakeShift.add(panel_22, BorderLayout.CENTER);
		panel_22.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_24 = new JPanel();
		panel_22.add(panel_24, BorderLayout.EAST);
		GridBagLayout gbl_panel_24 = new GridBagLayout();
		gbl_panel_24.columnWidths = new int[]{0, 0};
		gbl_panel_24.rowHeights = new int[]{0, 0};
		gbl_panel_24.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_24.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_24.setLayout(gbl_panel_24);
		
		JButton btnTakeShift = new JButton("Take Shift");
		GridBagConstraints gbc_btnTakeShift = new GridBagConstraints();
		gbc_btnTakeShift.anchor = GridBagConstraints.SOUTH;
		gbc_btnTakeShift.gridx = 0;
		gbc_btnTakeShift.gridy = 0;
		panel_24.add(btnTakeShift, gbc_btnTakeShift);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_22.add(scrollPane_1, BorderLayout.CENTER);
		
		JList listOfShiftsToTake = new JList();
		scrollPane_1.setViewportView(listOfShiftsToTake);
		
		JPanel panel_23 = new JPanel();
		panel_22.add(panel_23, BorderLayout.SOUTH);
		
		JLabel lblNewLabel_13 = new JLabel("  ");
		panel_23.add(lblNewLabel_13);
		
		Container container = getContentPane();
		container.add("Login", panelLogin);
		container.add("MainMenu", panelManagerMainMenu);
		container.add("WorkShiftsMenu", panelManagerShiftMenu);
		container.add("ReleaseNewWorkShifts", panelReleaseNewWorkShifts);
		container.add("TakeShift", panelTakeShift);
		
	}
	
	/**
	 * Changes GUI layout to show next panel in container.
	 */
	private void getNextCard() {
		cardLayout.next(contentPane);
	}
	
	private void getThisCard(String cardName) {
		cardLayout.show(contentPane, cardName);
	}
}
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
import javax.swing.DefaultListModel;
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

import controller.EmployeeController;
import controller.ShiftController;
import model.Copy;
import model.Employee;
import utility.DataAccessException;
import utility.DateLabelFormatter;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.awt.event.ActionEvent;
import javax.swing.ScrollPaneConstants;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTextArea;

public class GUI extends JFrame {

	private CardLayout cardLayout;
	private JPanel contentPane;
	private JPanel panelMainMenu;
	private JPanel panelShiftMenu;
	private JPanel panelReleaseNewShifts;
	private JPanel panelTakeShift;
	private ShiftController shiftController;
	private EmployeeController employeeController;
	private JDatePickerImpl datePicker;
	private JComboBox<String> comboBoxShiftTo;
	private JComboBox<String> comboBoxShiftFrom;
	private JList<String> listOfShiftsToRelease;
	private DefaultListModel<String> listModelRelease;
	private JTextArea textAreaErrorHandling;
	private JTextArea textAreaCompleteReleaseNewShifts;
	private JPanel panelCompleteReleaseNewShifts;
	private JList<String> listOfShiftsToTake;
	private DefaultListModel<String> listModelTake;
	private JTextArea textAreaTakeNewShiftErrorHandling;

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
	 * @throws DataAccessException 
	 */
	public GUI() throws DataAccessException {
		shiftController = new ShiftController();
		employeeController = new EmployeeController();
		
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
		
		panelMainMenu = new JPanel();
		contentPane.add(panelMainMenu, "name_1005555845259100");
		panelMainMenu.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panelMainMenu.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 9));
		panel_3.add(btnLogout);
		
		panelShiftMenu = new JPanel();
		contentPane.add(panelShiftMenu, "name_1007777309751200");
		panelShiftMenu.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panelShiftMenu.add(panel_1, BorderLayout.NORTH);
		
		JButton btnNewButton_4 = new JButton("Logout");
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 9));
		panel_1.add(btnNewButton_4);
		
		panelReleaseNewShifts = new JPanel();
		contentPane.add(panelReleaseNewShifts, "name_1008271951056900");
		panelReleaseNewShifts.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_12 = new JPanel();
		panelReleaseNewShifts.add(panel_12, BorderLayout.NORTH);
		
		JLabel lblNewLabel_3 = new JLabel("Release Shift");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_12.add(lblNewLabel_3);
		
		JPanel panel_13 = new JPanel();
		panelReleaseNewShifts.add(panel_13, BorderLayout.WEST);
		GridBagLayout gbl_panel_13 = new GridBagLayout();
		gbl_panel_13.columnWidths = new int[]{0, 0};
		gbl_panel_13.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_13.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_13.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		panel_13.setLayout(gbl_panel_13);
		
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Day");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		
		JLabel lblNewLabel_2 = new JLabel("Date:");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 1;
		panel_13.add(lblNewLabel_2, gbc_lblNewLabel_2);
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		
		GridBagConstraints gbc_datePicker = new GridBagConstraints();
		gbc_datePicker.insets = new Insets(0, 0, 5, 0);
		gbc_datePicker.gridx = 0;
		gbc_datePicker.gridy = 2;
		panel_13.add(datePicker, gbc_datePicker);
		
		JLabel lblNewLabel_10 = new JLabel("From time:");
		GridBagConstraints gbc_lblNewLabel_10 = new GridBagConstraints();
		gbc_lblNewLabel_10.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_10.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_10.gridx = 0;
		gbc_lblNewLabel_10.gridy = 4;
		panel_13.add(lblNewLabel_10, gbc_lblNewLabel_10);
		
		comboBoxShiftFrom = new JComboBox<>();
		GridBagConstraints gbc_comboBoxShiftFrom = new GridBagConstraints();
		gbc_comboBoxShiftFrom.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxShiftFrom.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxShiftFrom.gridx = 0;
		gbc_comboBoxShiftFrom.gridy = 5;
		panel_13.add(comboBoxShiftFrom, gbc_comboBoxShiftFrom);
		comboBoxShiftFrom.addItem("");
		comboBoxShiftFrom.addItem("6:00");
		comboBoxShiftFrom.addItem("14:00");
		comboBoxShiftFrom.addItem("22:00");
		
		JLabel lblNewLabel_11 = new JLabel("To time:");
		GridBagConstraints gbc_lblNewLabel_11 = new GridBagConstraints();
		gbc_lblNewLabel_11.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_11.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_11.gridx = 0;
		gbc_lblNewLabel_11.gridy = 6;
		panel_13.add(lblNewLabel_11, gbc_lblNewLabel_11);
		
		comboBoxShiftTo = new JComboBox<>();
		GridBagConstraints gbc_comboBoxShiftTo = new GridBagConstraints();
		gbc_comboBoxShiftTo.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxShiftTo.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxShiftTo.gridx = 0;
		gbc_comboBoxShiftTo.gridy = 7;
		panel_13.add(comboBoxShiftTo, gbc_comboBoxShiftTo);
		comboBoxShiftTo.addItem("");
		comboBoxShiftTo.addItem("14:00");
		comboBoxShiftTo.addItem("22:00");
		comboBoxShiftTo.addItem("6:00");
		
		JButton btnAddShift = new JButton("Add");
		btnAddShift.addActionListener(e -> {
			try {
				addShiftButtonClicked(e);
			} catch (DataAccessException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		});
		GridBagConstraints gbc_btnAddShift = new GridBagConstraints();
		gbc_btnAddShift.anchor = GridBagConstraints.WEST;
		gbc_btnAddShift.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddShift.gridx = 0;
		gbc_btnAddShift.gridy = 8;
		panel_13.add(btnAddShift, gbc_btnAddShift);
		
		JButton btnDeleteShift = new JButton("Delete");
		btnDeleteShift.addActionListener(e -> {
			try {
				deleteShiftCopyButtonClicked(e);
			} catch (DataAccessException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
		}); {
		GridBagConstraints gbc_btnDeleteShift = new GridBagConstraints();
		gbc_btnDeleteShift.anchor = GridBagConstraints.WEST;
		gbc_btnDeleteShift.insets = new Insets(0, 0, 5, 0);
		gbc_btnDeleteShift.gridx = 0;
		gbc_btnDeleteShift.gridy = 9;
		panel_13.add(btnDeleteShift, gbc_btnDeleteShift);
		
		textAreaErrorHandling = new JTextArea();
		GridBagConstraints gbc_textAreaErrorHandling = new GridBagConstraints();
		gbc_textAreaErrorHandling.insets = new Insets(0, 0, 5, 0);
		gbc_textAreaErrorHandling.fill = GridBagConstraints.BOTH;
		gbc_textAreaErrorHandling.gridx = 0;
		gbc_textAreaErrorHandling.gridy = 10;
		panel_13.add(textAreaErrorHandling, gbc_textAreaErrorHandling);
		
		listModelRelease = new DefaultListModel<>();
		listOfShiftsToRelease = new JList<>(listModelRelease);
		
		JScrollPane scrollPane = new JScrollPane();
		panelReleaseNewShifts.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(listOfShiftsToRelease);
		
		JPanel panel = new JPanel();
		panelMainMenu.add(panel, BorderLayout.CENTER);
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
		gbl_panel_5.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_5.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
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
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 1;
		panel_5.add(btnNewButton_1, gbc_btnNewButton_1);
		
		JButton btnMeeting = new JButton("Meeting");
		btnMeeting.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnMeeting = new GridBagConstraints();
		gbc_btnMeeting.fill = GridBagConstraints.BOTH;
		gbc_btnMeeting.gridx = 0;
		gbc_btnMeeting.gridy = 2;
		panel_5.add(btnMeeting, gbc_btnMeeting);
		
		JPanel panel_6 = new JPanel();
		panel_4_1.add(panel_6);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[]{0, 0};
		gbl_panel_6.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_6.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_6.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		panel_6.setLayout(gbl_panel_6);
		
		JButton btnShifts = new JButton("Shifts");
		btnShifts.addActionListener(this::workShiftsButtonClicked);
		btnShifts.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnShifts = new GridBagConstraints();
		gbc_btnShifts.fill = GridBagConstraints.BOTH;
		gbc_btnShifts.insets = new Insets(0, 0, 5, 0);
		gbc_btnShifts.gridx = 0;
		gbc_btnShifts.gridy = 0;
		panel_6.add(btnShifts, gbc_btnShifts);
		
		JButton btnNewButton_3 = new JButton("Data");
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_3.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton_3.gridx = 0;
		gbc_btnNewButton_3.gridy = 1;
		panel_6.add(btnNewButton_3, gbc_btnNewButton_3);
		
		JButton btnHoliday = new JButton("Holiday");
		btnHoliday.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnHoliday = new GridBagConstraints();
		gbc_btnHoliday.fill = GridBagConstraints.BOTH;
		gbc_btnHoliday.gridx = 0;
		gbc_btnHoliday.gridy = 2;
		panel_6.add(btnHoliday, gbc_btnHoliday);
		
		
		JPanel panel_7 = new JPanel();
		panelShiftMenu.add(panel_7, BorderLayout.CENTER);
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
		
		JButton btnReleaseNew = new JButton("Release New");
		btnReleaseNew.addActionListener(e -> {
			try {
				releaseNewButtonClicked(e);
			} catch (DataAccessException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		});
		btnReleaseNew.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnReleaseNew = new GridBagConstraints();
		gbc_btnReleaseNew.fill = GridBagConstraints.BOTH;
		gbc_btnReleaseNew.insets = new Insets(0, 0, 5, 0);
		gbc_btnReleaseNew.gridx = 0;
		gbc_btnReleaseNew.gridy = 0;
		panel_10.add(btnReleaseNew, gbc_btnReleaseNew);
		
		JButton btnSeeReleased = new JButton("See Released");
		btnSeeReleased.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnSeeReleased = new GridBagConstraints();
		gbc_btnSeeReleased.fill = GridBagConstraints.BOTH;
		gbc_btnSeeReleased.insets = new Insets(0, 0, 5, 0);
		gbc_btnSeeReleased.gridx = 0;
		gbc_btnSeeReleased.gridy = 1;
		panel_10.add(btnSeeReleased, gbc_btnSeeReleased);
		
		JButton btnTakeShifts = new JButton("Take Shift");
		btnTakeShifts.addActionListener(e -> {
			try {
				takeShiftButtonClicked(e);
			} catch (DataAccessException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		});
		btnTakeShifts.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnTakeShifts = new GridBagConstraints();
		gbc_btnTakeShifts.fill = GridBagConstraints.BOTH;
		gbc_btnTakeShifts.gridx = 0;
		gbc_btnTakeShifts.gridy = 2;
		panel_10.add(btnTakeShifts, gbc_btnTakeShifts);
		
		JPanel panel_19 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_19.getLayout();
		flowLayout_2.setAlignment(FlowLayout.RIGHT);
		panelReleaseNewShifts.add(panel_19, BorderLayout.SOUTH);
		
		JButton btnCancelReleaseShift = new JButton("Cancel");
		panel_19.add(btnCancelReleaseShift);
		
		JButton btnCompleteReleaseShift = new JButton("Complete");
		btnCompleteReleaseShift.addActionListener(e -> {
			try {
				completeReleaseShiftsButtonClicked(e);
			} catch (DataAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		panel_19.add(btnCompleteReleaseShift);
		
		JPanel panel_11 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_11.getLayout();
		flowLayout_3.setAlignment(FlowLayout.RIGHT);
		panelShiftMenu.add(panel_11, BorderLayout.SOUTH);
		
		JButton btnCancelShiftMenu = new JButton("Cancel");
		btnCancelShiftMenu.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_11.add(btnCancelShiftMenu);
		
		panelTakeShift = new JPanel();
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
		gbl_panel_24.rowHeights = new int[]{0, 0, 0};
		gbl_panel_24.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_24.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		panel_24.setLayout(gbl_panel_24);
		
		JButton btnTakeShift = new JButton("Take Shift");
		btnTakeShift.addActionListener(e -> {
			try {
				takeNewShiftButtonClicked(e);
			} catch (DataAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		GridBagConstraints gbc_btnTakeShift = new GridBagConstraints();
		gbc_btnTakeShift.insets = new Insets(0, 0, 5, 0);
		gbc_btnTakeShift.anchor = GridBagConstraints.SOUTH;
		gbc_btnTakeShift.gridx = 0;
		gbc_btnTakeShift.gridy = 0;
		panel_24.add(btnTakeShift, gbc_btnTakeShift);
		
		textAreaTakeNewShiftErrorHandling = new JTextArea();
		GridBagConstraints gbc_textAreaTakeNewShiftErrorHandling = new GridBagConstraints();
		gbc_textAreaTakeNewShiftErrorHandling.fill = GridBagConstraints.BOTH;
		gbc_textAreaTakeNewShiftErrorHandling.gridx = 0;
		gbc_textAreaTakeNewShiftErrorHandling.gridy = 1;
		panel_24.add(textAreaTakeNewShiftErrorHandling, gbc_textAreaTakeNewShiftErrorHandling);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_22.add(scrollPane_1, BorderLayout.CENTER);
		
		listModelTake = new DefaultListModel<>();
		listOfShiftsToTake = new JList<>(listModelTake);
		scrollPane_1.setViewportView(listOfShiftsToTake);
		
		
		JPanel panel_23 = new JPanel();
		panel_22.add(panel_23, BorderLayout.SOUTH);
		
		JLabel lblNewLabel_13 = new JLabel("  ");
		panel_23.add(lblNewLabel_13);
		
		panelCompleteReleaseNewShifts = new JPanel();
		contentPane.add(panelCompleteReleaseNewShifts, "name_1607421999554700");
		panelCompleteReleaseNewShifts.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_14 = new JPanel();
		panelCompleteReleaseNewShifts.add(panel_14, BorderLayout.NORTH);
		
		JLabel lblNewLabel_4 = new JLabel("Complete Release");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_14.add(lblNewLabel_4);
		
		textAreaCompleteReleaseNewShifts = new JTextArea();
		panelCompleteReleaseNewShifts.add(textAreaCompleteReleaseNewShifts, BorderLayout.CENTER);
		
		JPanel panel_15 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_15.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		panelCompleteReleaseNewShifts.add(panel_15, BorderLayout.SOUTH);
		
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(this::completeReleaseNewShiftsOKButtonClicked);
		btnOK.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_15.add(btnOK);
		
		// Adds all panels to the cardlayout of the JFrame.
		addPanelsToCardLayout();
		}
		
	}
	
	private void addPanelsToCardLayout() {
		Container container = getContentPane();
		container.add("MainMenu", panelMainMenu);
		container.add("ShiftsMenu", panelShiftMenu);
		container.add("ReleaseNewShifts", panelReleaseNewShifts);
		container.add("TakeShift", panelTakeShift);
		container.add("CompleteReleaseNewShifts", panelCompleteReleaseNewShifts);
	}
	
	private void getThisCard(String cardName) {
		cardLayout.show(contentPane, cardName);
	}
	
	
	private void workShiftsButtonClicked(ActionEvent e) {
		if(checkLogin()) {
			getThisCard("ShiftsMenu");
		}
	}
	
	private void completeReleaseNewShiftsOKButtonClicked(ActionEvent e) {
		if(checkLogin()) {
			getThisCard("MainMenu");
		}
	}
	
	private void releaseNewButtonClicked(ActionEvent e) throws DataAccessException {
		startReleaseNewShifts();
	}
	

	private void addShiftButtonClicked(ActionEvent e) throws DataAccessException {
		addShift();
	}
	
	private void completeReleaseShiftsButtonClicked(ActionEvent e) throws DataAccessException {
		completeReleaseNewShifts();
	}
	
	private void deleteShiftCopyButtonClicked(ActionEvent e) throws DataAccessException {
		deleteShiftCopy();
	}
	
	private void takeShiftButtonClicked(ActionEvent e) throws DataAccessException {
		getThisCard("TakeShift");
		startTakeNewShift();
	}
	
	private void takeNewShiftButtonClicked(ActionEvent e) throws DataAccessException {
		takeNewShift();
	}
	
	private void startReleaseNewShifts() throws DataAccessException {
		shiftController.startReleaseNewShifts();
		if(checkLogin()) {
			getThisCard("ReleaseNewShifts");
		}
	}
	
	private void addShift() throws DataAccessException {
		String dateString = datePicker.getJFormattedTextField().getText();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(dateString, formatter);
		String fromHourString = (String) comboBoxShiftFrom.getSelectedItem();
		int fromHour = getIntTimeFromString(fromHourString);
		String toHourString = (String) comboBoxShiftTo.getSelectedItem();
		int toHour = getIntTimeFromString(toHourString);
		textAreaErrorHandling.setText("");
		if(fromHour > toHour && fromHour != 22) {
			textAreaErrorHandling.setText("Invalid time period has been chosen");
		}
		else if(fromHour == toHour) {
			textAreaErrorHandling.setText("Invalid time period has been chosen");
		}
		else if(toHour - fromHour > 8) {
			textAreaErrorHandling.setText("A shift can be no longer than 8 hours");
		}
		else if(date.isBefore(LocalDate.now())) {
			textAreaErrorHandling.setText("Invalid date has been chosen");
		}
		else {
			ArrayList<Copy> shiftCopies = shiftController.addShift(date, fromHour, toHour);
			showCopies(shiftCopies, listModelRelease);
			}
	}
	
	private void completeReleaseNewShifts() throws DataAccessException {
		getThisCard("CompleteReleaseNewShifts");
		if(shiftController.completeReleaseNewShifts()) {
			textAreaCompleteReleaseNewShifts.setText("Completion was successfull");
		}
		else {
			textAreaCompleteReleaseNewShifts.setText("Completion failed");
		}
	}
	
	private void deleteShiftCopy() throws DataAccessException {
		int index = getIndexOnSelectedListValue(listOfShiftsToRelease);
		if(shiftController.deleteShiftCopy(index)) {
			showCopies(shiftController.getShiftCopies(), listModelRelease);
		}
		else {
			textAreaErrorHandling.setText("Error! Shift could not be deleted");
		}
	}
	
	private void startTakeNewShift() throws DataAccessException {
		ArrayList<Copy> releasedCopies = shiftController.startTakeNewShift();
		if(!releasedCopies.isEmpty()) {
			showCopies(releasedCopies, listModelTake);
		}
	}
	
	private void takeNewShift() throws DataAccessException {
		int index = getIndexOnSelectedListValue(listOfShiftsToTake);
		boolean taken = shiftController.takeNewShift(index);
		if(taken) {
			textAreaTakeNewShiftErrorHandling.setText("Shift was successfully taken");
			showCopies(shiftController.getReleasedCopies(), listModelTake);
		}
		else {
			textAreaTakeNewShiftErrorHandling.setText("Error! Shift has already been taken");
		}
	}
	
	private int getIndexOnSelectedListValue(JList<String> list) {
		String copyList = (String) list.getSelectedValue();
		String substr = copyList.substring(7, 9);
		if(substr.substring(1,2).equals(" ")) {
			substr = substr.substring(0,1);
		}
		int index = Integer.parseInt(substr) - 1;
		return index;
	}
	
	private void showCopies(ArrayList<Copy> shiftCopies, DefaultListModel<String> listModel) throws DataAccessException {
		listModel.clear();
		for(int i = 0 ; i < shiftCopies.size() ; i++) {
			Copy copy = shiftCopies.get(i);
			String copyDate = copy.getDate().toString();
			String day = copyDate.substring(copyDate.length() - 2);
			String month = copyDate.substring(5, 7);
			String year = copyDate.substring(0, 4);
			String copyDateFormatted = day + "-" + month + "-" + year;
			listModel.addElement("Shift: " + (i + 1) + " Date: " + copyDateFormatted + " From: " + copy.getShift().getFromHour() + " To: " + copy.getShift().getToHour());
		}
	}
	
	private int getIntTimeFromString(String time) {
		String timeAsString;
		int timeAsInt;
		if(time.substring(1,2).equals(":")) {
			 timeAsString = time.substring(0,1);
			 timeAsInt = Integer.parseInt(timeAsString); 
		}
		else {
			timeAsString = time.substring(0,2);
			 timeAsInt = Integer.parseInt(timeAsString);
		}
		return timeAsInt;
	}
	
	private boolean checkLogin() {
		boolean manager = false;
		Employee employee = employeeController.getLoggedInEmployee();
		if(employee.getType().equals("Manager")) {
			manager = true;
		}
		return manager;
	}
	
}

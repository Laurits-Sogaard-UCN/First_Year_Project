package test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Taskbar.State;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.lang.model.element.ExecutableElement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.ShiftController;
import database.ConnectionFactory;
import database.DBConnection;
import database.DBConnectionMock;
import database.ShiftDB;
import model.Copy;
import model.Shift;
import model.WorkSchedule;
import utility.CopyState;
import utility.DataAccessException;
import utility.DatabaseType;
import utility.EmployeeType;

class TestShiftController {
	
	private Connection con;
	private ShiftController shiftController;
	private int addressID;
	private int shopID;
	private int shiftID;
	private int shiftID2;
	
	String ADD_ADDRESS_CITY = "INSERT INTO AddressCity (Zipcode, City, Country)\r\n"
			+ "VALUES (9000, 'Aalborg', 'Denmark')";
	PreparedStatement addAddressCity;
	
	String ADD_ADDRESS = "INSERT INTO Address (Street, StreetNumber, Zipcode)\r\n"
			+ "VALUES ('Jernbanegade', '42', 9000)";
	PreparedStatement addAddress;
	
	String ADD_SHOP = "INSERT INTO Shop (Name, AddressID)\r\n"
			+ "VALUES ('OK Plus Tordenvej', ?)";
	PreparedStatement addShop;
	
	String ADD_EMPLOYEE = "INSERT INTO Employee (CPR, Lname, Fname, Email, AddressID, Phone, Username, Password, EmployeeType, ShopID)\r\n"
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	PreparedStatement addEmployee;
	
	String ADD_WORKSCHEDULE = "INSERT INTO WorkSchedule(FromDate, ToDate, TotalHours, EmployeeCPR)\r\n"
			+ "VALUES (?, ?, ?, ?)";
	PreparedStatement addWorkSchedule;
	
	String ADD_COPY = "INSERT INTO Copy (ShiftID, WorkScheduleID, Date, State, ReleasedAt)\r\n"
			+ "VALUES (?, ?, ?, ?, GETDATE())";
	PreparedStatement addCopy;
	
	String ADD_SHIFT = "INSERT INTO Shift (FromHour, ToHour)\r\n"
			+ "VALUES (?, ?)";
	PreparedStatement addShift;
	
	String SET_NOCHECK_TO_FALSE = "EXEC sys.sp_msforeachtable 'ALTER TABLE ? NOCHECK CONSTRAINT ALL'";
	PreparedStatement setNoCheckToFalse;
	
	String DELETE_ALL_FOREACH_TABLE = "EXEC sys.sp_msforeachtable 'DELETE FROM ?'";
	PreparedStatement deleteAllForeachTable;
	
	String SET_NOCHECK_TO_TRUE = "EXEC sys.sp_MSForEachTable 'ALTER TABLE ? CHECK CONSTRAINT ALL'";
	PreparedStatement setNoCheckToTrue;
	

	@BeforeEach
	void setUp() throws Exception {
		shiftController = new ShiftController(DatabaseType.MOCKDATABASE);
		con = ConnectionFactory.createDatabase(DatabaseType.MOCKDATABASE).getConnection();
		ResultSet addressIDRS;
		ResultSet shopIDRS;
		ResultSet ShiftIDRS;
		
		//Adds addressCity to database
		addAddressCity = con.prepareStatement(ADD_ADDRESS_CITY);
		addAddressCity.executeUpdate();
		
		//Adds address to database
		addAddress = con.prepareStatement(ADD_ADDRESS, Statement.RETURN_GENERATED_KEYS);
		addAddress.executeUpdate();
		addressIDRS = addAddress.getGeneratedKeys();
		addressIDRS.next();
		addressID = addressIDRS.getInt(1);
		
		//Adds shop to database
		addShop = con.prepareStatement(ADD_SHOP, Statement.RETURN_GENERATED_KEYS);
		addShop.setInt(1, addressID);
		addShop.executeUpdate();
		shopIDRS = addShop.getGeneratedKeys();
		shopIDRS.next();
		shopID = shopIDRS.getInt(1);
		
		//Adds shift to database
		addShift = con.prepareStatement(ADD_SHIFT, Statement.RETURN_GENERATED_KEYS);
		addShift.setString(1, "06:00:00");
		addShift.setString(2, "14:00:00");
		addShift.executeUpdate();
		ShiftIDRS = addShift.getGeneratedKeys();
		ShiftIDRS.next();
		shiftID = ShiftIDRS.getInt(1);
		
		addShift.setString(1, "14:00:00");
		addShift.setString(2, "22:00:00");
		addShift.executeUpdate();
		ShiftIDRS = addShift.getGeneratedKeys();
		ShiftIDRS.next();
		shiftID2 = ShiftIDRS.getInt(1);
		
		//Reusable preparedstatements for employee, WorkSchedule and copy
		addEmployee = con.prepareStatement(ADD_EMPLOYEE);
		addWorkSchedule = con.prepareStatement(ADD_WORKSCHEDULE, Statement.RETURN_GENERATED_KEYS);
		addCopy = con.prepareStatement(ADD_COPY, Statement.RETURN_GENERATED_KEYS);
	}

	@AfterEach
	void tearDown() throws Exception {
		setNoCheckToFalse = con.prepareStatement(SET_NOCHECK_TO_FALSE);
		deleteAllForeachTable = con.prepareStatement(DELETE_ALL_FOREACH_TABLE);
		setNoCheckToTrue = con.prepareStatement(SET_NOCHECK_TO_TRUE);
		setNoCheckToFalse.execute();
		deleteAllForeachTable.execute();
		setNoCheckToTrue.execute();
	}

	@Test
	public void TakeNewShift() throws DataAccessException, SQLException {
		// Arrange
		int addedManager = 0;
		int addedManagerWorkSchedule = 0;
		int addedCopy = 0;
		boolean take = false;
		
		int copyID = 0;
		String fromHourString = "06:00";
		String toHourString = "14:00";
		String date = "2070-12-10";
		LocalTime fromHour = LocalTime.parse(fromHourString);
		LocalTime toHour = LocalTime.parse(toHourString);
		LocalDate localDate = LocalDate.parse(date); 
		
		ResultSet rs;
		
		Shift shift = new Shift(fromHour, toHour, 1);
		Copy copy = new Copy(copyID, shift, null,  localDate, CopyState.RELEASED.getState(), LocalDateTime.now());
		
		// Act
		
		//Add Employee-manager
		addEmployee.setString(1, "9876512345");
		addEmployee.setString(2, "Kallesen");
		addEmployee.setString(3, "Mathias");
		addEmployee.setString(4, "MathiasKS@mail.com");
		addEmployee.setInt(5, addressID);
		addEmployee.setString(6, "+4512344321");
		addEmployee.setString(7, "331");
		addEmployee.setString(8, "+opetdss2");
		addEmployee.setString(9, "Manager");
		addEmployee.setInt(10, shopID);
		addedManager = addEmployee.executeUpdate();
		
		//Add WorkSchedule for manager
		addWorkSchedule.setString(1, "2022-11-03");
		addWorkSchedule.setString(2, "2022-12-3");
		addWorkSchedule.setInt(3, 0);
		addWorkSchedule.setString(4, "9876512345");
		addedManagerWorkSchedule = addWorkSchedule.executeUpdate();
		
		//Add shift copy to take
		addCopy.setInt(1, shiftID);
		addCopy.setNull(2, java.sql.Types.NULL);
		addCopy.setString(3, "2070-12-10");
		addCopy.setString(4, "Released");
		addedCopy = addCopy.executeUpdate();
		rs = addCopy.getGeneratedKeys();
		rs.next();
		copyID = rs.getInt(1);
		copy.setId(copyID);
		take = shiftController.takeNewShift(copy);

		// Assert
		assertTrue(addedManager == 1);
		assertTrue(addedManagerWorkSchedule == 1);
		assertTrue(addedCopy == 1);
		assertTrue(take);
	}


	@Test
	public void DelegateShifts1() throws DataAccessException, SQLException {
		// Arrange
		int delegated = 0;
		int employeeWorkScheduleID = 0;
		int employeeWorkScheduleID2 = 0;
		boolean correctWorkSchedule = false;
		String getCopyWorkScheduleIDAndState = "Select WorkScheduleID, State\r\n"
				+ "From Copy\r\n"
				+ "Where Date = '2070-12-10'\r\n"
				+ "And State = 'Delegated'";
		PreparedStatement getCopyWorkScheduleIDAndStatePS = con.prepareStatement(getCopyWorkScheduleIDAndState);

		ResultSet employeeWorkScheduleIDRS = null;
		ResultSet copyWorkScheduleIDAndState = null;
		
		// Act
		
		try {
		DBConnectionMock.getInstance().startTransaction();
		//Inserts new employees
		addEmployee.setString(1, "7492657491");
		addEmployee.setString(2, "Tordenmolle");
		addEmployee.setString(3, "Karl");
		addEmployee.setString(4, "KarkTM@mail.com");
		addEmployee.setInt(5, addressID);
		addEmployee.setString(6, "+4574839268");
		addEmployee.setString(7, "888");
		addEmployee.setString(8, "321cbadl");
		addEmployee.setString(9, "Parttime");
		addEmployee.setInt(10, shopID);
		addEmployee.executeUpdate();
		
		addEmployee.setString(1, "8364751917");
		addEmployee.setString(2, "Vandmolle");
		addEmployee.setString(3, "Tom");
		addEmployee.setString(4, "TomVM@mail.com");
		addEmployee.setInt(5, addressID);
		addEmployee.setString(6, "+4594752916");
		addEmployee.setString(7, "999");
		addEmployee.setString(8, "123abcdw");
		addEmployee.setString(9, "Parttime");
		addEmployee.setInt(10, shopID);
		addEmployee.executeUpdate();
		
		//Inserts WorkSchedules and get their ID
		addWorkSchedule.setString(1, "2022-11-03");
		addWorkSchedule.setString(2, "2022-12-3");
		addWorkSchedule.setInt(3, 8);
		addWorkSchedule.setString(4, "8364751917");
		addWorkSchedule.executeUpdate();
		employeeWorkScheduleIDRS = addWorkSchedule.getGeneratedKeys();
		employeeWorkScheduleIDRS.next();
		employeeWorkScheduleID2 = employeeWorkScheduleIDRS.getInt(1);
		
		addWorkSchedule.setString(1, "2022-11-03");
		addWorkSchedule.setString(2, "2022-12-3");
		addWorkSchedule.setInt(3, 8);
		addWorkSchedule.setString(4, "7492657491");
		addWorkSchedule.executeUpdate();
		employeeWorkScheduleIDRS = addWorkSchedule.getGeneratedKeys();
		employeeWorkScheduleIDRS.next();
		employeeWorkScheduleID = employeeWorkScheduleIDRS.getInt(1);
		
		//Insert Shift copy that have been taken and one ready to take on the same day
		addCopy.setInt(1, shiftID);
		addCopy.setInt(2, employeeWorkScheduleID);
		addCopy.setString(3, "2070-12-10");
		addCopy.setString(4, "Occupied");
		addCopy.executeUpdate();
		
		addCopy.setInt(1, shiftID);
		addCopy.setNull(2, java.sql.Types.NULL);
		addCopy.setString(3, "2070-12-10");
		addCopy.setString(4, "Released");
		addCopy.executeUpdate();
		
		//Delegate
		delegated = shiftController.delegateShifts();
		
		//Checks if the correct employee have been delegated the correct copy
		copyWorkScheduleIDAndState = getCopyWorkScheduleIDAndStatePS.executeQuery();
		copyWorkScheduleIDAndState.next();
		if(copyWorkScheduleIDAndState.getInt("WorkScheduleID") == employeeWorkScheduleID2 && copyWorkScheduleIDAndState.getString("State").equals(CopyState.DELEGATED.getState())) {
				correctWorkSchedule = true;
		}
		
		DBConnectionMock.getInstance().commitTransaction();
		} catch(SQLException e) {
			DBConnectionMock.getInstance().rollbackTransaction();
			e.printStackTrace();
		}
	
		// Assert
		assertTrue(delegated == 0);
		assertTrue(correctWorkSchedule);
	}
	
	@Test
	public void DelegateShifts2() throws DataAccessException, SQLException {
		// Arrange
		int delegated = 0;
		int employeeWorkScheduleID = 0;
		int employeeWorkScheduleID2 = 0;
		boolean correctWorkSchedule = false;
		String getCopyWorkScheduleIDAndState = "Select WorkScheduleID, State\r\n"
				+ "From Copy\r\n"
				+ "Where Date = '2070-12-10'\r\n"
				+ "And State = 'Delegated'";
		PreparedStatement getCopyWorkScheduleIDAndStatePS = con.prepareStatement(getCopyWorkScheduleIDAndState);

		ResultSet employeeWorkScheduleIDRS = null;
		ResultSet copyWorkScheduleIDAndState = null;
		
		
		// Act
		try {
		DBConnectionMock.getInstance().startTransaction();
		//Inserts new employees
		addEmployee.setString(1, "7492657491");
		addEmployee.setString(2, "Tordenmolle");
		addEmployee.setString(3, "Karl");
		addEmployee.setString(4, "KarkTM@mail.com");
		addEmployee.setInt(5, addressID);
		addEmployee.setString(6, "+4574839268");
		addEmployee.setString(7, "888");
		addEmployee.setString(8, "321cbadl");
		addEmployee.setString(9, "Parttime");
		addEmployee.setInt(10, shopID);
		addEmployee.executeUpdate();
		
		addEmployee.setString(1, "8364751917");
		addEmployee.setString(2, "Vandmolle");
		addEmployee.setString(3, "Tom");
		addEmployee.setString(4, "TomVM@mail.com");
		addEmployee.setInt(5, addressID);
		addEmployee.setString(6, "+4594752916");
		addEmployee.setString(7, "999");
		addEmployee.setString(8, "123abcdw");
		addEmployee.setString(9, "Parttime");
		addEmployee.setInt(10, shopID);
		addEmployee.executeUpdate();
		
		//Inserts WorkSchedules and get their ID
		addWorkSchedule.setString(1, "2022-11-03");
		addWorkSchedule.setString(2, "2022-12-3");
		addWorkSchedule.setInt(3, 8);
		addWorkSchedule.setString(4, "8364751917");
		addWorkSchedule.executeUpdate();
		employeeWorkScheduleIDRS = addWorkSchedule.getGeneratedKeys();
		employeeWorkScheduleIDRS.next();
		employeeWorkScheduleID2 = employeeWorkScheduleIDRS.getInt(1);
		
		addWorkSchedule.setString(1, "2022-11-03");
		addWorkSchedule.setString(2, "2022-12-3");
		addWorkSchedule.setInt(3, 8);
		addWorkSchedule.setString(4, "7492657491");
		addWorkSchedule.executeUpdate();
		employeeWorkScheduleIDRS = addWorkSchedule.getGeneratedKeys();
		employeeWorkScheduleIDRS.next();
		employeeWorkScheduleID = employeeWorkScheduleIDRS.getInt(1);
		
		//Insert Shift copy that have been taken and one ready to take on the same day
		addCopy.setInt(1, shiftID);
		addCopy.setInt(2, employeeWorkScheduleID);
		addCopy.setString(3, "2070-12-11");
		addCopy.setString(4, "Occupied");
		addCopy.executeUpdate();
		
		addCopy.setInt(1, shiftID2);
		addCopy.setNull(2, java.sql.Types.NULL);
		addCopy.setString(3, "2070-12-10");
		addCopy.setString(4, "Released");
		addCopy.executeUpdate();
		
		//Delegate
		delegated = shiftController.delegateShifts();
		
		//Checks if the correct employee have been delegated the correct copy
		copyWorkScheduleIDAndState = getCopyWorkScheduleIDAndStatePS.executeQuery();
		copyWorkScheduleIDAndState.next();
		if(copyWorkScheduleIDAndState.getInt("WorkScheduleID") == employeeWorkScheduleID2 && copyWorkScheduleIDAndState.getString("State").equals(CopyState.DELEGATED.getState())) {
				correctWorkSchedule = true;
		}
		
		DBConnectionMock.getInstance().commitTransaction();
		} catch(SQLException e) {
			DBConnectionMock.getInstance().rollbackTransaction();
			e.printStackTrace();
		}
	
		// Assert
		assertTrue(delegated == 0);
		assertTrue(correctWorkSchedule);
	}
	

	@Test
	public void getReleasedShiftCopiesList() throws DataAccessException {
		//Arrange
		ArrayList<Copy> list = null;
		//Act
		list = shiftController.getReleasedShiftCopiesList();
		//Assert
		assertNotNull(list);
	}
	
	@Test
	public void addShift() throws DataAccessException {
		// Arrange
		boolean added = false;
		LocalTime fromHour = LocalTime.parse("06:00:00");
		LocalTime toHour = LocalTime.parse("14:00:00");
		LocalDate date = LocalDate.now();
		Copy copyToTest;
		
		// Act
		shiftController.clearShiftCopies();
		copyToTest = shiftController.addShift(date, fromHour, toHour).get(0);
		if(copyToTest.getDate().equals(date) && copyToTest.getShift().getFromHour().equals(fromHour) && copyToTest.getShift().getToHour().equals(toHour) && copyToTest.getShift().getID() == shiftID) {
			added = true;
		}
		
		
		// Assert
		assertTrue(added);
	}
	
	@Test
	public void clearShiftCopies() throws DataAccessException {
		// Arrange
		boolean cleared = false;
		LocalTime fromHour = LocalTime.parse("06:00:00");
		LocalTime toHour = LocalTime.parse("14:00:00");
		LocalDate date = LocalDate.now();
		
		// Act
		shiftController.addShift(date, fromHour, toHour);
		shiftController.clearShiftCopies();
		cleared = shiftController.getShiftCopies().isEmpty();
		
		
		// Assert
		assertTrue(cleared);
	}
	
	@Test
	public void startTakePlannedShift() throws DataAccessException {
		// Arrange
		boolean succes = true;
		int employeeWorkScheduleID = 0;
		boolean correctState = false;
		ResultSet employeeWorkScheduleIDRS;
		Copy copy = null;
		
		// Act
		try {
			addEmployee.setString(1, "7492657491");
			addEmployee.setString(2, "Tordenmolle");
			addEmployee.setString(3, "Karl");
			addEmployee.setString(4, "KarkTM@mail.com");
			addEmployee.setInt(5, addressID);
			addEmployee.setString(6, "+4574839268");
			addEmployee.setString(7, "888");
			addEmployee.setString(8, "321cbadl");
			addEmployee.setString(9, "Parttime");
			addEmployee.setInt(10, shopID);
			addEmployee.executeUpdate();
		
			addWorkSchedule.setString(1, "2022-11-03");
			addWorkSchedule.setString(2, "2022-12-3");
			addWorkSchedule.setInt(3, 8);
			addWorkSchedule.setString(4, "7492657491");
			addWorkSchedule.executeUpdate();
			employeeWorkScheduleIDRS = addWorkSchedule.getGeneratedKeys();
			employeeWorkScheduleIDRS.next();
			employeeWorkScheduleID = employeeWorkScheduleIDRS.getInt(1);
			
			addCopy.setInt(1, shiftID);
			addCopy.setInt(2, employeeWorkScheduleID);
			addCopy.setString(3, "2070-12-11");
			addCopy.setString(4, "Tradeable");
			addCopy.executeUpdate();
			
			succes = shiftController.startTakePlannedShift().isEmpty();
			copy = shiftController.startTakePlannedShift().get(0);
			correctState = copy.getState().equals("Tradeable");
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Assert
		assertFalse(succes);
		assertTrue(correctState);
	}
	
	@Test
	public void takePlannedShift() throws SQLException, DataAccessException {
		// Arrange
		boolean succes = false;
		int copyID = 0;
		int workScheduleID = 0;
		String fromHourString = "06:00";
		String toHourString = "14:00";
		String date = "2070-12-10";
		LocalTime fromHour = LocalTime.parse(fromHourString);
		LocalTime toHour = LocalTime.parse(toHourString);
		LocalDate localDate = LocalDate.parse(date); 
		
		ResultSet rs;
		
		Shift shift = new Shift(fromHour, toHour, 1);
		WorkSchedule workSchedule = new WorkSchedule(workScheduleID);
		Copy copy = new Copy(copyID, shift, workSchedule, localDate, CopyState.TRADEABLE.getState(), LocalDateTime.now());
		// Act
		addEmployee.setString(1, "9876512345");
		addEmployee.setString(2, "Kallesen");
		addEmployee.setString(3, "Mathias");
		addEmployee.setString(4, "MathiasKS@mail.com");
		addEmployee.setInt(5, addressID);
		addEmployee.setString(6, "+4512344321");
		addEmployee.setString(7, "331");
		addEmployee.setString(8, "+opetdss2");
		addEmployee.setString(9, "Manager");
		addEmployee.setInt(10, shopID);
		addEmployee.executeUpdate();
		
		//Add WorkSchedule for manager
		addWorkSchedule.setString(1, "2022-11-03");
		addWorkSchedule.setString(2, "2022-12-3");
		addWorkSchedule.setInt(3, 0);
		addWorkSchedule.setString(4, "9876512345");
		addWorkSchedule.executeUpdate();
		
		//Sets values for the copy workSchedule object
		rs =  addWorkSchedule.getGeneratedKeys();
		rs.next();
		copy.getWorkSchedule().setEmployeeCPR("9876512345");
		copy.getWorkSchedule().setID(rs.getInt(1));
		
		//Adds copy to the database
		addCopy.setInt(1, shiftID);
		addCopy.setNull(2, java.sql.Types.NULL);
		addCopy.setString(3, "2070-12-11");
		addCopy.setString(4, "Tradeable");
		addCopy.executeUpdate();
		rs = addCopy.getGeneratedKeys();
		rs.next();
		copy.setId(rs.getInt(1));
		
		succes = shiftController.takePlannedShift(copy);
		
		
		// Assert
		assertTrue(succes);
	}
	
}

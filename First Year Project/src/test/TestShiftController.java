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
	
	String ADD_ADDRESS_CITY = "INSERT INTO AddressCity (Zipcode, City, Country)\r\n"
			+ "VALUES (9000, 'Aalborg', 'Denmark')";
	PreparedStatement addAddressCity;
	
	String ADD_ADDRESS = "INSERT INTO Address (Street, StreetNumber, Zipcode)\r\n"
			+ "VALUES ('Jernbanegade', '42', 9000)";
	PreparedStatement addAddress;
	
	String ADD_SHOP = "INSERT INTO Shop (Name, AddressID)\r\n"
			+ "VALUES ('OK Plus Tordenvej', ?)";
	PreparedStatement addShop;
	
	String ADD_EMPLOYEE_MANAGER = "INSERT INTO Employee (CPR, Lname, Fname, Email, AddressID, Phone, Username, Password, EmployeeType, ShopID)\r\n"
			+ "VALUES ('9876512345', 'Kallesen', 'Mathias', 'MathiasKS@mail.com', ?, '+4512344321', '331', 'opetdss2', 'Manager', ?)";
	PreparedStatement addEmployeeManager;
	
	String ADD_WORKSCHEDULE_FOR_MANAGER = "INSERT INTO WorkSchedule(FromDate, ToDate, EmployeeCPR)\r\n"
			+ "VALUES ('2022-11-03', '2022-12-3', '9876512345')";
	PreparedStatement addWorkScheduleForManager;
	
	String ADD_COPY_TO_TAKE = "INSERT INTO Copy (ShiftID, Date, State, ReleasedAt)\r\n"
			+ "VALUES (?, '2070-12-10', 'Released', GETDATE())";
	PreparedStatement addCopyToTake;
	
	String ADD_SHIFT = "INSERT INTO Shift (FromHour, ToHour)\r\n"
			+ "VALUES ('06:00:00', '14:00:00')";
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
		
		addAddressCity = con.prepareStatement(ADD_ADDRESS_CITY);
		addAddressCity.executeUpdate();
		
		addAddress = con.prepareStatement(ADD_ADDRESS, Statement.RETURN_GENERATED_KEYS);
		addAddress.executeUpdate();
		addressIDRS = addAddress.getGeneratedKeys();
		addressIDRS.next();
		addressID = addressIDRS.getInt(1);
		
		addShop = con.prepareStatement(ADD_SHOP, Statement.RETURN_GENERATED_KEYS);
		addShop.setInt(1, addressID);
		addShop.executeUpdate();
		shopIDRS = addShop.getGeneratedKeys();
		shopIDRS.next();
		shopID = shopIDRS.getInt(1);
		
		addShift = con.prepareStatement(ADD_SHIFT, Statement.RETURN_GENERATED_KEYS);
		addShift.executeUpdate();
		ShiftIDRS = addShift.getGeneratedKeys();
		ShiftIDRS.next();
		shiftID = ShiftIDRS.getInt(1);
		
		
		
		
		addEmployeeManager = con.prepareStatement(ADD_EMPLOYEE_MANAGER);
		addWorkScheduleForManager = con.prepareStatement(ADD_WORKSCHEDULE_FOR_MANAGER);
		addCopyToTake = con.prepareStatement(ADD_COPY_TO_TAKE, Statement.RETURN_GENERATED_KEYS);
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
		Copy copy = new Copy(copyID, shift, localDate, CopyState.RELEASED.getState(), LocalDateTime.now());
		
		// Act
		addEmployeeManager.setInt(1, addressID);
		addEmployeeManager.setInt(2, shopID);
		addedManager = addEmployeeManager.executeUpdate();
		addedManagerWorkSchedule = addWorkScheduleForManager.executeUpdate();
		addCopyToTake.setInt(1, shiftID);
		addedCopy = addCopyToTake.executeUpdate();
		rs = addCopyToTake.getGeneratedKeys();
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

/*
	@Test
	public void DelegateShifts1() throws DataAccessException, SQLException {
		// Arrange
		ShiftController shiftController = new ShiftController();
		Connection con = ConnectionFactory.createDatabase(DatabaseType.MOCKDATABASE).getConnection();
		ArrayList<String> cprs = new ArrayList<>();
		ArrayList<Integer> copyIDs = new ArrayList<>();
		
		int copyid;
		int employeeWorkScheduleID = 0;
		int workScheduleAndEmployeesRemoved = 0;
		int employeesRemoved = 0;
		int copiesRemoved = 0;
		boolean correctWorkSchedule = false;
		String CPR = "";
		String listCPR = "Select CPR\r\n"
				+ "From Employee \r\n"
				+ "Where EmployeeType = 'Parttime'";
		String updateEmployeeType = "Update Employee\r\n"
				+ "Set EmployeeType = ?\r\n"
				+ "Where CPR = ?;";
		String insertNewPartTimeEmployee1 = "INSERT INTO Employee (CPR, Lname, Fname, Email, AddressID, Phone, Username, Password, EmployeeType, ShopID)\r\n"
				+ "VALUES ('7492657491', 'Tordenmolle', 'Karl', 'KarkTM@mail.com', 3, '+4574839268', '888', '321cbadl', 'Parttime', 1)";
		String insertNewPartTimeEmployee2 = "INSERT INTO Employee (CPR, Lname, Fname, Email, AddressID, Phone, Username, Password, EmployeeType, ShopID)\r\n"
				+ "VALUES ('8364751917', 'Vandmolle', 'Tom', 'TomVM@mail.com', 3, '+4594752916', '999', '123abcdw', 'Parttime', 1)";
		String insertNewWorkScheduleForEmployee1 = "INSERT INTO WorkSchedule(FromDate, ToDate, TotalHours, EmployeeCPR)\r\n"
				+ "VALUES ('2022-11-03', '2022-12-3', 8, '7492657491')";
		String insertNewWorkScheduleForEmployee2 = "INSERT INTO WorkSchedule(FromDate, ToDate, TotalHours, EmployeeCPR)\r\n"
				+ "VALUES ('2022-11-03', '2022-12-3', 8, '8364751917')";
		String insertShiftCopyOccupied = "INSERT INTO Copy(ShiftID, WorkScheduleID, Date, State, ReleasedAt)\r\n"
				+ "VALUES (1, ?, '2070-12-10', 'Occupied', GETDATE())";
		String insertShiftCopyReleased = "INSERT INTO Copy(ShiftID, Date, State, ReleasedAt)\r\n"
				+ "VALUES (1, '2070-12-10', 'Released', GETDATE())";
		String listCopyID = "Select ID\r\n"
				+ "From Copy\r\n"
				+ "Where Date = '2070-12-10'";
		String updateCopiesStateToOccupied = "Update Copy\r\n"
				+ "Set State = ?\r\n"
				+ "Where ID = ?;";
		String getEmployeeWorkScheduleID = "Select ID\r\n"
				+ "From WorkSchedule\r\n"
				+ "Where EmployeeCPR = ?";
		String updateWorkScheduleTotalHours = "Update WorkSchedule\r\n"
				+ "Set TotalHours = 8\r\n"
				+ "Where EmployeeCPR = '7492657491'\r\n"
				+ "And EmployeeCPR = '8364751917';";
		String getCopyWorkScheduleIDAndState = "Select WorkScheduleID, State\r\n"
				+ "From Copy\r\n"
				+ "Where Date = '2070-12-10'\r\n"
				+ "And State = 'Delegated'";
		String removeWorkScheduleByCPR1 = "Delete From WorkSchedule\r\n"
				+ "Where EmployeeCPR = '7492657491';\r\n";
		String removeWorkScheduleByCPR2 = "Delete From WorkSchedule\r\n"
				+ "Where EmployeeCPR = '8364751917';\r\n";
		
		String removeEmployeeByCPR1 = "Delete From Employee\r\n"
				+ "Where CPR = '7492657491'\r\n";
		String removeEmployeeByCPR2 = "Delete From Employee\r\n"
				+ "Where CPR = '8364751917';\r\n";
		String removeCopiesByDate = "Delete From Copy\r\n"
				+ "Where Date = '2070-12-10'";
		PreparedStatement listCPRPS = con.prepareStatement(listCPR);
		PreparedStatement updateEmployeeTypePS = con.prepareStatement(updateEmployeeType);
		PreparedStatement insertNewPartTimeEmployee1PS = con.prepareStatement(insertNewPartTimeEmployee1);
		PreparedStatement insertNewPartTimeEmployee2PS = con.prepareStatement(insertNewPartTimeEmployee2);
		PreparedStatement insertNewWorkScheduleForEmployee1PS = con.prepareStatement(insertNewWorkScheduleForEmployee1);
		PreparedStatement insertNewWorkScheduleForEmployee2PS = con.prepareStatement(insertNewWorkScheduleForEmployee2);
		PreparedStatement insertShiftCopyOccupiedPS = con.prepareStatement(insertShiftCopyOccupied);
		PreparedStatement insertShiftCopyReleasedPS = con.prepareStatement(insertShiftCopyReleased);
		PreparedStatement listCopyIDPS = con.prepareStatement(listCopyID);
		PreparedStatement updateCopiesStateToOccupiedPS = con.prepareStatement(updateCopiesStateToOccupied);
		PreparedStatement getEmployeeWorkScheduleIDPS = con.prepareStatement(getEmployeeWorkScheduleID);
		PreparedStatement updateWorkScheduleTotalHoursPS = con.prepareStatement(updateWorkScheduleTotalHours);
		PreparedStatement getCopyWorkScheduleIDAndStatePS = con.prepareStatement(getCopyWorkScheduleIDAndState);
		
		//Prepared Statements to restore the Datebase to origen
		PreparedStatement removeWorkScheduleByCPRPS = con.prepareStatement(removeWorkScheduleByCPR1);
		PreparedStatement removeEmployeeByCPRPS = con.prepareStatement(removeEmployeeByCPR1);
		PreparedStatement removeWorkScheduleByCPRPS2 = con.prepareStatement(removeWorkScheduleByCPR2);
		PreparedStatement removeEmployeeByCPRPS2 = con.prepareStatement(removeEmployeeByCPR2);
		PreparedStatement removeCopiesByDatePS = con.prepareStatement(removeCopiesByDate);
		ResultSet employeeCPR = null;
		ResultSet copyID = null;
		ResultSet employeeWorkScheduleIDRS = null;
		ResultSet copyWorkScheduleIDAndState = null;
		
		
		// Act
		try {
		DBConnectionMock.getInstance().startTransaction();
		employeeCPR = listCPRPS.executeQuery();
		copyID = listCopyIDPS.executeQuery(); 
		
		try {
			while(employeeCPR.next()) {
				CPR = employeeCPR.getString("CPR");
				cprs.add(CPR);
				updateEmployeeTypePS.setString(1, EmployeeType.FULLTIME.getType());
				updateEmployeeTypePS.setString(2, CPR);
				updateEmployeeTypePS.executeUpdate();
			}
			while(copyID.next()) {
				copyid = copyID.getInt("ID");
				copyIDs.add(copyid);
				updateCopiesStateToOccupiedPS.setString(1, CopyState.OCCUPIED.getState());
				updateCopiesStateToOccupiedPS.setInt(2, copyid);
				updateCopiesStateToOccupiedPS.executeUpdate();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		//Inserts new employees and their workschedules
		insertNewPartTimeEmployee1PS.executeUpdate();
		insertNewPartTimeEmployee2PS.executeUpdate();
		insertNewWorkScheduleForEmployee1PS.executeUpdate();
		insertNewWorkScheduleForEmployee2PS.executeUpdate();
		
		//Gets Workschedule ID on employee by CPR
		getEmployeeWorkScheduleIDPS.setString(1, "7492657491");
		employeeWorkScheduleIDRS = getEmployeeWorkScheduleIDPS.executeQuery();
		if(employeeWorkScheduleIDRS.next()) {
			employeeWorkScheduleID = employeeWorkScheduleIDRS.getInt("ID");
		}
		
		//Insert Shift copy that have been taken and one ready to take on the same day
		insertShiftCopyOccupiedPS.setInt(1, employeeWorkScheduleID);
		insertShiftCopyOccupiedPS.executeUpdate();
		insertShiftCopyReleasedPS.executeUpdate();
		
		//Sets workschedule totalHours to 8
		updateWorkScheduleTotalHoursPS.executeUpdate();
		
		shiftController.delegateShifts();
		
		//Checks if the correct employee have been delegated the correct copy
		copyWorkScheduleIDAndState = getCopyWorkScheduleIDAndStatePS.executeQuery();
		getEmployeeWorkScheduleIDPS.setString(1, "8364751917");
		employeeWorkScheduleIDRS = getEmployeeWorkScheduleIDPS.executeQuery();
		employeeWorkScheduleIDRS.next();
		employeeWorkScheduleID = employeeWorkScheduleIDRS.getInt("ID");
		
		if(copyWorkScheduleIDAndState.next() && copyWorkScheduleIDAndState.getInt("WorkScheduleID") == employeeWorkScheduleID && copyWorkScheduleIDAndState.getString("State").equals(CopyState.DELEGATED.getState())) {
				correctWorkSchedule = true;
		}
		
		//Restores the datebase
		copiesRemoved = removeCopiesByDatePS.executeUpdate();
		workScheduleAndEmployeesRemoved += removeWorkScheduleByCPRPS.executeUpdate();
		workScheduleAndEmployeesRemoved += removeWorkScheduleByCPRPS2.executeUpdate();
		employeesRemoved += removeEmployeeByCPRPS.executeUpdate();
		employeesRemoved += removeEmployeeByCPRPS2.executeUpdate();
		
		try {
			for(String element : cprs) {
				updateEmployeeTypePS.setString(1, EmployeeType.PARTTIME.getType());
				updateEmployeeTypePS.setString(2, element);
				updateEmployeeTypePS.executeUpdate();
			}
			for(Integer element : copyIDs) {
				updateCopiesStateToOccupiedPS.setString(1, CopyState.RELEASED.getState());
				updateCopiesStateToOccupiedPS.setInt(2, element);
				updateCopiesStateToOccupiedPS.executeUpdate();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		DBConnectionMock.getInstance().commitTransaction();
		} catch(SQLException e) {
			DBConnectionMock.getInstance().rollbackTransaction();
			e.printStackTrace();
		}
	
		// Assert
		assertTrue(correctWorkSchedule);
		assertTrue(workScheduleAndEmployeesRemoved == 2);
		assertTrue(employeesRemoved == 2);
		assertTrue(copiesRemoved == 2);
	}
	
	@Test
	public void DelegateShifts2() throws DataAccessException, SQLException {
		// Arrange
		ShiftController shiftController = new ShiftController();
		Connection con = ConnectionFactory.createDatabase(DatabaseType.MOCKDATABASE).getConnection();
		
		ArrayList<String> cprs = new ArrayList<>();
		ArrayList<Integer> copyIDs = new ArrayList<>();
		
		int copyid;
		int employeeWorkScheduleID = 0;
		int workScheduleAndEmployeesRemoved = 0;
		int employeesRemoved = 0;
		int copiesRemoved = 0;
		boolean correctWorkSchedule = false;
		String CPR = "";
		String listCPR = "Select CPR\r\n"
				+ "From Employee \r\n"
				+ "Where EmployeeType = 'Parttime'";
		String updateEmployeeType = "Update Employee\r\n"
				+ "Set EmployeeType = ?\r\n"
				+ "Where CPR = ?;";
		String insertNewPartTimeEmployee1 = "INSERT INTO Employee (CPR, Lname, Fname, Email, AddressID, Phone, Username, Password, EmployeeType, ShopID)\r\n"
				+ "VALUES ('7492657491', 'Tordenmolle', 'Karl', 'KarkTM@mail.com', 3, '+4574839268', '888', '321cbadl', 'Parttime', 1)";
		String insertNewPartTimeEmployee2 = "INSERT INTO Employee (CPR, Lname, Fname, Email, AddressID, Phone, Username, Password, EmployeeType, ShopID)\r\n"
				+ "VALUES ('8364751917', 'Vandmolle', 'Tom', 'TomVM@mail.com', 3, '+4594752916', '999', '123abcdw', 'Parttime', 1)";
		String insertNewWorkScheduleForEmployee1 = "INSERT INTO WorkSchedule(FromDate, ToDate, TotalHours, EmployeeCPR)\r\n"
				+ "VALUES ('2022-11-03', '2022-12-3', 8, '7492657491')";
		String insertNewWorkScheduleForEmployee2 = "INSERT INTO WorkSchedule(FromDate, ToDate, TotalHours, EmployeeCPR)\r\n"
				+ "VALUES ('2022-11-03', '2022-12-3', 8, '8364751917')";
		String insertShiftCopyOccupied = "INSERT INTO Copy(ShiftID, WorkScheduleID, Date, State, ReleasedAt)\r\n"
				+ "VALUES (1, ?, '2070-12-11', 'Occupied', GETDATE())";
		String insertShiftCopyReleased = "INSERT INTO Copy(ShiftID, Date, State, ReleasedAt)\r\n"
				+ "VALUES (2, '2070-12-10', 'Released', GETDATE())";
		String listCopyID = "Select ID\r\n"
				+ "From Copy\r\n"
				+ "Where Date = '2070-12-10'";
		String updateCopiesStateToOccupied = "Update Copy\r\n"
				+ "Set State = ?\r\n"
				+ "Where ID = ?;";
		String getEmployeeWorkScheduleID = "Select ID\r\n"
				+ "From WorkSchedule\r\n"
				+ "Where EmployeeCPR = ?";
		String updateWorkScheduleTotalHours = "Update WorkSchedule\r\n"
				+ "Set TotalHours = 8\r\n"
				+ "Where EmployeeCPR = '7492657491'\r\n"
				+ "And EmployeeCPR = '8364751917';";
		String getCopyWorkScheduleIDAndState = "Select WorkScheduleID, State\r\n"
				+ "From Copy\r\n"
				+ "Where Date = '2070-12-10'\r\n"
				+ "And State = 'Delegated'";
		String removeWorkScheduleByCPR1 = "Delete From WorkSchedule\r\n"
				+ "Where EmployeeCPR = '7492657491';\r\n";
		String removeWorkScheduleByCPR2 = "Delete From WorkSchedule\r\n"
				+ "Where EmployeeCPR = '8364751917';\r\n";
		
		String removeEmployeeByCPR1 = "Delete From Employee\r\n"
				+ "Where CPR = '7492657491'\r\n";
		String removeEmployeeByCPR2 = "Delete From Employee\r\n"
				+ "Where CPR = '8364751917';\r\n";
		String removeCopiesByDate = "Delete From Copy\r\n"
				+ "Where Date = '2070-12-10'";
		String removeCopiesByDate2 = "Delete From Copy\r\n"
				+ "Where Date = '2070-12-11'";
		PreparedStatement listCPRPS = con.prepareStatement(listCPR);
		PreparedStatement updateEmployeeTypePS = con.prepareStatement(updateEmployeeType);
		PreparedStatement insertNewPartTimeEmployee1PS = con.prepareStatement(insertNewPartTimeEmployee1);
		PreparedStatement insertNewPartTimeEmployee2PS = con.prepareStatement(insertNewPartTimeEmployee2);
		PreparedStatement insertNewWorkScheduleForEmployee1PS = con.prepareStatement(insertNewWorkScheduleForEmployee1);
		PreparedStatement insertNewWorkScheduleForEmployee2PS = con.prepareStatement(insertNewWorkScheduleForEmployee2);
		PreparedStatement insertShiftCopyOccupiedPS = con.prepareStatement(insertShiftCopyOccupied);
		PreparedStatement insertShiftCopyReleasedPS = con.prepareStatement(insertShiftCopyReleased);
		PreparedStatement listCopyIDPS = con.prepareStatement(listCopyID);
		PreparedStatement updateCopiesStateToOccupiedPS = con.prepareStatement(updateCopiesStateToOccupied);
		PreparedStatement getEmployeeWorkScheduleIDPS = con.prepareStatement(getEmployeeWorkScheduleID);
		PreparedStatement updateWorkScheduleTotalHoursPS = con.prepareStatement(updateWorkScheduleTotalHours);
		PreparedStatement getCopyWorkScheduleIDAndStatePS = con.prepareStatement(getCopyWorkScheduleIDAndState);
		
		//Prepared Statements to restore the Datebase to origen
		PreparedStatement removeWorkScheduleByCPRPS = con.prepareStatement(removeWorkScheduleByCPR1);
		PreparedStatement removeEmployeeByCPRPS = con.prepareStatement(removeEmployeeByCPR1);
		PreparedStatement removeWorkScheduleByCPRPS2 = con.prepareStatement(removeWorkScheduleByCPR2);
		PreparedStatement removeEmployeeByCPRPS2 = con.prepareStatement(removeEmployeeByCPR2);
		PreparedStatement removeCopiesByDatePS = con.prepareStatement(removeCopiesByDate);
		PreparedStatement removeCopiesByDatePS2 = con.prepareStatement(removeCopiesByDate2);
		ResultSet employeeCPR = null;
		ResultSet copyID = null;
		ResultSet employeeWorkScheduleIDRS = null;
		ResultSet copyWorkScheduleIDAndState = null;
		
		
		// Act
		try {
		DBConnectionMock.getInstance().startTransaction();
		employeeCPR = listCPRPS.executeQuery();
		copyID = listCopyIDPS.executeQuery(); 
		
		try {
			while(employeeCPR.next()) {
				CPR = employeeCPR.getString("CPR");
				cprs.add(CPR);
				updateEmployeeTypePS.setString(1, EmployeeType.FULLTIME.getType());
				updateEmployeeTypePS.setString(2, CPR);
				updateEmployeeTypePS.executeUpdate();
			}
			while(copyID.next()) {
				copyid = copyID.getInt("ID");
				copyIDs.add(copyid);
				updateCopiesStateToOccupiedPS.setString(1, CopyState.OCCUPIED.getState());
				updateCopiesStateToOccupiedPS.setInt(2, copyid);
				updateCopiesStateToOccupiedPS.executeUpdate();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		//Inserts new employees and their workschedules
		insertNewPartTimeEmployee1PS.executeUpdate();
		insertNewPartTimeEmployee2PS.executeUpdate();
		insertNewWorkScheduleForEmployee1PS.executeUpdate();
		insertNewWorkScheduleForEmployee2PS.executeUpdate();
		
		//Gets Workschedule ID on employee by CPR
		getEmployeeWorkScheduleIDPS.setString(1, "7492657491");
		employeeWorkScheduleIDRS = getEmployeeWorkScheduleIDPS.executeQuery();
		if(employeeWorkScheduleIDRS.next()) {
			employeeWorkScheduleID = employeeWorkScheduleIDRS.getInt("ID");
		}
		
		//Insert Shift copy that have been taken and one ready to take on the same day
		insertShiftCopyOccupiedPS.setInt(1, employeeWorkScheduleID);
		insertShiftCopyOccupiedPS.executeUpdate();
		insertShiftCopyReleasedPS.executeUpdate();
		
		//Sets workschedule totalHours to 8
		updateWorkScheduleTotalHoursPS.executeUpdate();
		
		shiftController.delegateShifts();
		
		//Checks if the correct employee have been delegated the correct copy
		copyWorkScheduleIDAndState = getCopyWorkScheduleIDAndStatePS.executeQuery();
		getEmployeeWorkScheduleIDPS.setString(1, "8364751917");
		employeeWorkScheduleIDRS = getEmployeeWorkScheduleIDPS.executeQuery();
		employeeWorkScheduleIDRS.next();
		employeeWorkScheduleID = employeeWorkScheduleIDRS.getInt("ID");
		
		if(copyWorkScheduleIDAndState.next() && copyWorkScheduleIDAndState.getInt("WorkScheduleID") == employeeWorkScheduleID && copyWorkScheduleIDAndState.getString("State").equals(CopyState.DELEGATED.getState())) {
				correctWorkSchedule = true;
		}
		
		//Restores the datebase
		copiesRemoved += removeCopiesByDatePS.executeUpdate();
		copiesRemoved += removeCopiesByDatePS2.executeUpdate();
		workScheduleAndEmployeesRemoved += removeWorkScheduleByCPRPS.executeUpdate();
		workScheduleAndEmployeesRemoved += removeWorkScheduleByCPRPS2.executeUpdate();
		employeesRemoved += removeEmployeeByCPRPS.executeUpdate();
		employeesRemoved += removeEmployeeByCPRPS2.executeUpdate();
		
		try {
			for(String element : cprs) {
				updateEmployeeTypePS.setString(1, EmployeeType.PARTTIME.getType());
				updateEmployeeTypePS.setString(2, element);
				updateEmployeeTypePS.executeUpdate();
			}
			for(Integer element : copyIDs) {
				updateCopiesStateToOccupiedPS.setString(1, CopyState.RELEASED.getState());
				updateCopiesStateToOccupiedPS.setInt(2, element);
				updateCopiesStateToOccupiedPS.executeUpdate();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		DBConnectionMock.getInstance().commitTransaction();
		} catch(SQLException e) {
			DBConnectionMock.getInstance().rollbackTransaction();
			e.printStackTrace();
		}
	
		// Assert
		assertTrue(correctWorkSchedule);
		assertTrue(workScheduleAndEmployeesRemoved == 2);
		assertTrue(employeesRemoved == 2);
		assertTrue(copiesRemoved == 2);
	}
	*/
	/*
	@Test
	public void getReleasedShiftCopiesList() throws DataAccessException {
		//Arrange
		ShiftController shiftController = new ShiftController();
		ArrayList<Copy> list = null;
		//Act
		list = shiftController.getReleasedShiftCopiesList();
		//Assert
		assertNotNull(list);
	}
	
	@Test
	public void addShift() throws DataAccessException {
		// Arrange
		ShiftController shiftController = new ShiftController();
		boolean added = false;
		LocalTime fromHour = LocalTime.parse("06:00:00");
		LocalTime toHour = LocalTime.parse("14:00:00");
		LocalDate date = LocalDate.now();
		Copy copyToTest;
		
		// Act
		shiftController.clearShiftCopies();
		copyToTest = shiftController.addShift(date, fromHour, toHour).get(0);
		if(copyToTest.getDate().equals(date) && copyToTest.getShift().getFromHour().equals(fromHour) && copyToTest.getShift().getToHour().equals(toHour) && copyToTest.getShift().getID() == 1) {
			added = true;
		}
		
		
		// Assert
		assertTrue(added);
	}
	
	@Test
	public void clearShiftCopies() throws DataAccessException {
		// Arrange
		ShiftController shiftController = new ShiftController();
		boolean cleared = false;
		
		// Act
		shiftController.clearShiftCopies();
		cleared = shiftController.getShiftCopies().isEmpty();
		
		
		// Assert
		assertTrue(cleared);
	}
	*/
	
}

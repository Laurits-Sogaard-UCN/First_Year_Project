package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.ShiftController;
import database.DBConnection;
import model.Copy;
import model.Shift;
import utility.CopyState;
import utility.DataAccessException;
import utility.EmployeeType;

class TestShiftController {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	public void TakeNewShift() throws DataAccessException, SQLException {
		// Arrange
		ShiftController shiftController = new ShiftController();
		Connection con = DBConnection.getInstance().getConnection();
		
		int added = 0;
		boolean take = false;
		int sqlRestore = 0;
		int copyID = 0;
		
		String addCopyToDatabase = "INSERT INTO Copy (ShiftID, Date, State, ReleasedAt)\r\n"
				+ "VALUES (1, '2070-12-10', 'Released', GETDATE())";
		String workschedule = "Select WorkScheduleID\r\n"
				+ "From Copy\r\n"
				+ "Where Date = '2070-12-10' and State = 'Occupied';";
		String restoreDateBase = "DELETE FROM Copy WHERE Date = '2070-12-10';";
		String restoreWorkscheduleHours = "Update WorkSchedule\r\n"
				+ "Set TotalHours = 0\r\n"
				+ "Where EmployeeCPR = ?;";
		String getIDOnCopy = "SELECT ID\r\n"
				+ "FROM Copy\r\n"
				+ "Where Date = '2070-12-10'";
		
		PreparedStatement ps = con.prepareStatement(addCopyToDatabase);
		PreparedStatement ps1 = con.prepareStatement(workschedule);
		PreparedStatement ps2 = con.prepareStatement(restoreDateBase);
		PreparedStatement ps3 = con.prepareStatement(getIDOnCopy);
		PreparedStatement ps4 = con.prepareStatement(restoreWorkscheduleHours);
		
		ResultSet rs;
		ResultSet rs2;
		
		String fromHourString = "06:00";
		String toHourString = "14:00";
		String date = "2070-12-10";
		LocalTime fromHour = LocalTime.parse(fromHourString);
		LocalTime toHour = LocalTime.parse(toHourString);
		LocalDate localDate = LocalDate.parse(date); 
		
		Shift shift = new Shift(fromHour, toHour, 1);
		Copy copy = new Copy(copyID, shift, localDate, CopyState.RELEASED.getState(), LocalDateTime.now());
		
		// Act
		added = ps.executeUpdate();
		rs2 = ps3.executeQuery();
		rs2.next();
		copyID = rs2.getInt("ID");
		copy.setId(copyID);
		take = shiftController.takeNewShift(copy);
		rs = ps1.executeQuery();
		sqlRestore = ps2.executeUpdate();
		ps4.setString(1, "9876512345");
		ps4.executeUpdate();

		// Assert
		assertTrue(added == 1);
		assertTrue(take);
		assertTrue(rs.next() && rs.getInt("WorkScheduleID") == 3);
		assertTrue(sqlRestore == 1);
	}


	@Test
	public void DelegateShifts() throws DataAccessException, SQLException {
		// Arrange
		ShiftController shiftController = new ShiftController();
		Connection con = DBConnection.getInstance().getConnection();
		
		ArrayList<String> cprs = new ArrayList<>();
		ArrayList<Integer> copyIDs = new ArrayList<>();
		
		int copyid;
		int employeeID = 0;
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
		DBConnection.getInstance().startTransaction();
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
		DBConnection.getInstance().commitTransaction();
		} catch(SQLException e) {
			DBConnection.getInstance().rollbackTransaction();
			e.printStackTrace();
		}
	
		// Assert
		assertTrue(correctWorkSchedule);
		assertTrue(workScheduleAndEmployeesRemoved == 2);
		assertTrue(employeesRemoved == 2);
		assertTrue(copiesRemoved == 2);
	}

}

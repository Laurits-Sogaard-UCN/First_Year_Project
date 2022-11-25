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
		String getIDOnCopy = "SELECT ID\r\n"
				+ "FROM Copy\r\n"
				+ "Where Date = '2070-12-10'";
		
		PreparedStatement ps = con.prepareStatement(addCopyToDatabase);
		PreparedStatement ps1 = con.prepareStatement(workschedule);
		PreparedStatement ps2 = con.prepareStatement(restoreDateBase);
		PreparedStatement ps3 = con.prepareStatement(getIDOnCopy);
		
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
		
		ArrayList<String> employeeCPRs = new ArrayList<>();
		ArrayList<Integer> copyIDs = new ArrayList<>();
		
		int copyid;
		int employeeID = 0;
		int employeeWorkScheduleID = 0;
		String CPR = "";
		String listCPR = "";
		String updateEmployeeType = "";
		String insertNewPartTimeEmployee1 = "";
		String insertNewPartTimeEmployee2 = "";
		String insertNewWorkScheduleForEmployee1 = "";
		String insertNewWorkScheduleForEmployee2 = "";
		String insertShiftCopyOccupied = "";
		String insertShiftCopyReleased = "";
		String listCopyID = "";
		String updateCopiesStateToOccupied = "";
		String getEmployeeWorkScheduleID = "";
		String updateWorkScheduleTotalHours = "";
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
		ResultSet employeeCPR = null;
		ResultSet copyID = null;
		ResultSet employeeWorkScheduleIDRS = null;
		
		
		// Act
		DBConnection.getInstance().startTransaction();
		employeeCPR = listCPRPS.executeQuery();
		copyID = listCopyIDPS.executeQuery(); 
		try {
			while(employeeCPR.next()) {
				CPR = employeeCPR.getString("CPR");
				employeeCPRs.add(CPR);
				updateEmployeeTypePS.setString(1, CPR);
				updateEmployeeTypePS.executeUpdate();
			}
			while(copyID.next()) {
				copyid = copyID.getInt("ID");
				copyIDs.add(copyid);
				updateCopiesStateToOccupiedPS.setInt(1, copyid);
				updateCopiesStateToOccupiedPS.executeUpdate();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		insertNewPartTimeEmployee1PS.executeUpdate();
		insertNewPartTimeEmployee2PS.executeUpdate();
		insertNewWorkScheduleForEmployee1PS.executeUpdate();
		insertNewWorkScheduleForEmployee2PS.executeUpdate();
		
		getEmployeeWorkScheduleIDPS.setString(1, "7492657491");
		employeeWorkScheduleIDRS = getEmployeeWorkScheduleIDPS.executeQuery();
		employeeWorkScheduleID = employeeWorkScheduleIDRS.getInt("ID");
		
		insertShiftCopyOccupiedPS.setInt(1, employeeWorkScheduleID);
		insertShiftCopyOccupiedPS.executeQuery();
		insertShiftCopyReleasedPS.executeQuery();
		
		updateWorkScheduleTotalHoursPS.setString(1, "7492657491");
		updateWorkScheduleTotalHoursPS.setString(1, "8364751917");
		updateWorkScheduleTotalHoursPS.executeUpdate();
		
		
	
		// Assert
		
	}

}

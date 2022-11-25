package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
	public void DelegateShifts() throws DataAccessException {
		// Arrange
		ShiftController shiftController = new ShiftController();
		Connection con = DBConnection.getInstance().getConnection();
		
		
		// Act
	
		// Assert
		
	}

}

package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.EmployeeController;
import controller.ShiftController;
import controller.WorkScheduleController;
import database.DBConnection;
import database.ShiftDB;
import model.Copy;
import model.Shift;
import utility.CopyState;
import utility.DataAccessException;

class TestShiftController {

	@Test
	public void TakeNewShift() throws DataAccessException, SQLException {
		// Arrange
		int index = 0;
		boolean release = false;
		boolean take = false;
		int sqlRestore = 0;
		ShiftController shiftController = new ShiftController();
		WorkScheduleController workScheduleController = new WorkScheduleController();
		EmployeeController employeeController = new EmployeeController();
		Connection con = DBConnection.getInstance().getConnection();
		ShiftDB shiftDB = new ShiftDB();
		String query = "DELETE FROM Copy WHERE Date = '2050-04-05' and State = 'Occupied';";
		String workschedule = "Select WorkScheduleID\r\n"
				+ "From Copy\r\n"
				+ "Where Date = '2050-04-05' and State = 'Occupied';";
		PreparedStatement ps = con.prepareStatement(query);
		PreparedStatement ps1 = con.prepareStatement(workschedule);
		ResultSet rs;
		String employeeCPR = employeeController.getLoggedInEmployee().getCPR();
		String fromHourString = "06:00";
		String toHourString = "14:00";
		LocalTime fromHour = LocalTime.parse(fromHourString);
		LocalTime toHour = LocalTime.parse(toHourString);
		Shift shift = new Shift(fromHour, toHour, 1);
		Copy copy = new Copy(1, shift, null, null, LocalDate.now().plusDays(10000), CopyState.RELEASED.getState(), LocalDateTime.now());
		ArrayList<Copy> releasedShiftCopies = shiftController.getReleasedCopies();
		ArrayList<Copy> shiftCopies = shiftController.getShiftCopies();
		shiftCopies.add(copy);
		releasedShiftCopies.add(copy);
		// Act
		release = shiftController.completeReleaseNewShifts();
		take = shiftController.takeNewShift(shiftController.getReleasedCopies().size() - 1);
		rs = ps1.executeQuery();
		sqlRestore = ps.executeUpdate();

		// Assert
		assertTrue(release);
		assertTrue(take);
		assertTrue(rs.next() && rs.getInt("WorkScheduleID") == 3);
		assertTrue(sqlRestore == 1);
		
	} 
	
	@Test
	public void BuildCopyObject() throws DataAccessException {
		// Arrange
		
		// Act
	
		// Assert
		
	}
	
	@Test
	public void DelegateShifts() throws DataAccessException {
		// Arrange
		
		// Act
	
		// Assert
		
	}
	
	@Test
	public void CheckRestPerriod() throws DataAccessException {
		// Arrange
		
		// Act
	
		// Assert
		
	}
	
	

}

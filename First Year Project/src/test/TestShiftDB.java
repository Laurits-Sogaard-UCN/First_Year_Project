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

import controller.EmployeeController;
import controller.ShiftController;
import controller.WorkScheduleController;
import database.DBConnection;
import database.ShiftDB;
import model.Copy;
import model.Shift;
import utility.CopyState;
import utility.DataAccessException;
import utility.DatabaseType;

class TestShiftDB {
	
	@Test
	public void findShiftOnFromAndTo() throws DataAccessException {
		// Arrange
		ShiftDB shiftDB = new ShiftDB(DatabaseType.MOCKDATABASE);
		Shift shift;
		LocalTime fromHour = LocalTime.parse("06:00:00");
		LocalTime toHour = LocalTime.parse("14:00:00");
		// Act
		shift = shiftDB.findShiftOnFromAndTo(fromHour, toHour);
		// Assert
		assertNotNull(shift);
		assertTrue(shift.getFromHour().equals(fromHour));
		assertTrue(shift.getToHour().equals(toHour));
	}
	
	
	
}

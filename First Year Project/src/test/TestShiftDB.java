package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.ConnectionFactory;
import database.ShiftDB;
import model.Shift;
import utility.DataAccessException;
import utility.DatabaseType;

class TestShiftDB {
	private Connection con;
	private ShiftDB shiftDB;
	
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
		shiftDB = new ShiftDB(DatabaseType.MOCKDATABASE);
		con = ConnectionFactory.createDatabase(DatabaseType.MOCKDATABASE).getConnection();
		
		//Adds shift to database
		addShift = con.prepareStatement(ADD_SHIFT);
		addShift.setString(1, "06:00:00");
		addShift.setString(2, "14:00:00");
		addShift.executeUpdate();
		
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
	public void findShiftOnFromAndTo() throws DataAccessException {
		// Arrange
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

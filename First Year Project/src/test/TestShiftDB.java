package test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import database.ShiftDB;
import model.Shift;
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

package database;

import model.Shift;
import utility.DataAccessException;

public interface ShiftDBIF {
	
	public Shift findShiftOnFromAndTo(int fromHour, int toHour) throws DataAccessException;

}

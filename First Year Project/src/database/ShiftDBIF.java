package database;

import java.util.ArrayList;

import model.Copy;
import model.Shift;
import utility.DataAccessException;

public interface ShiftDBIF {
	
	public Shift findShiftOnFromAndTo(int fromHour, int toHour) throws DataAccessException;
	
	public boolean completeReleaseWorkShifts(ArrayList<Copy> copies) throws DataAccessException;

}

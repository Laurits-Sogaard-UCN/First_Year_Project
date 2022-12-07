package database;

import java.time.LocalTime;
import java.util.ArrayList;
import model.Shift;
import model.ShiftCopy;
import utility.DataAccessException;

public interface ShiftDBIF {
	
	public Shift findShiftOnFromAndTo(LocalTime fromHour, LocalTime toHour) throws DataAccessException;
	
	public boolean completeReleaseNewShifts(ArrayList<ShiftCopy> copies) throws DataAccessException;
  
	public ArrayList<ShiftCopy> findShiftCopiesOnState(String state) throws DataAccessException;
	
	public boolean takeShift(ShiftCopy shiftCopy, int workScheduleID, String state) throws DataAccessException;

}

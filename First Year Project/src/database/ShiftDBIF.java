package database;

import java.time.LocalTime;
import java.util.ArrayList;

import model.Copy;
import model.Shift;
import utility.DataAccessException;

public interface ShiftDBIF {
	
	public Shift findShiftOnFromAndTo(LocalTime fromHour, LocalTime toHour) throws DataAccessException;
	
	public boolean completeReleaseNewShifts(ArrayList<Copy> copies) throws DataAccessException;
  
	public ArrayList<Copy> findShiftCopiesOnState(String state) throws DataAccessException;
	
	public boolean takeShift(Copy copy, int workScheduleID, String state) throws DataAccessException;

}

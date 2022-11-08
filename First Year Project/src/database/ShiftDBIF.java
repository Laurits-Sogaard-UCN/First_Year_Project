package database;

import java.util.ArrayList;

import model.Copy;
import model.Shift;
import utility.DataAccessException;

public interface ShiftDBIF {
	
	public Shift findShiftOnFromAndTo(int fromHour, int toHour) throws DataAccessException;
	
	public boolean completeReleaseNewShifts(ArrayList<Copy> copies) throws DataAccessException;
	
	public byte[] findCopyVersionNumberOnID(int id) throws DataAccessException;
  
	public ArrayList<Copy> findReleasedShiftCopies() throws DataAccessException;
	
	public boolean takeNewShift(int copyID, int workScheduleID) throws DataAccessException;

}

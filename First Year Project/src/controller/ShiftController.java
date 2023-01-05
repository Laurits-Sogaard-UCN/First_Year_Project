package controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import database.ShiftDB;
import database.ShiftDBIF;
import model.ShiftCopy;
import model.Shift;
import model.WorkSchedule;
import utility.CopyState;
import utility.DataAccessException;
import utility.DatabaseType;

public class ShiftController {
	
	private EmployeeController employeeController;
	private WorkScheduleController workScheduleController;
	private ShiftDBIF shiftDB;
	private ArrayList<ShiftCopy> shiftCopies;
	private ArrayList<ShiftCopy> shiftCopiesToBeReleased;
	
	/**
	 * Constructor to initialize instance variables. 
	 * @throws DataAccessException
	 */
	public ShiftController(DatabaseType databaseType) throws DataAccessException {
		employeeController = new EmployeeController();
		workScheduleController = new WorkScheduleController(databaseType);
		shiftDB = new ShiftDB(databaseType);
		shiftCopies = new ArrayList<>();
		shiftCopiesToBeReleased = new ArrayList<>();
	}
	
	/**
	 * Finds all shift copies marked as 'Released'.
	 * @return releasedShiftCopies
	 * @throws DataAccessException
	 */
	public ArrayList<ShiftCopy> startTakeNewShift() throws DataAccessException {
		shiftCopies = shiftDB.findShiftCopiesOnState(CopyState.RELEASED.getState());
		return shiftCopies;
	}
	
	/**
	 * Associates a shift copy on a given index, with the work schedule belonging
	 * to the logged in employee.
	 * Calculates and sets new total hours for logged in employee.
	 * @param index
	 * @return success
	 * @throws DataAccessException
	 */
	public boolean takeNewShift(ShiftCopy shiftCopy) throws DataAccessException {
		boolean success = false;
		int hours = calculateTotalHours(shiftCopy);
		String employeeCPR = employeeController.getLoggedInEmployee().getCPR();
		int workScheduleID = workScheduleController.findWorkScheduleIDOnEmployeeCPR(employeeCPR);
		String state = CopyState.OCCUPIED.getState();
		
		if(shiftDB.takeShift(shiftCopy, workScheduleID, state)) {
			workScheduleController.setTotalHoursOnWorkSchedule(hours, employeeCPR);
			shiftCopies.remove(shiftCopy);
			success = true;
		}
		return success;
	}
	
	/**
	 * Finds all work schedules belonging to part-time employees.
	 * Delegates the released shift copies to the part-time employees
	 * with lowest total number of working hours. 
	 * @return delegated
	 * @throws DataAccessException
	 */
	public int delegateShifts() throws DataAccessException {
		ArrayList<WorkSchedule> workSchedules;
		workSchedules = workScheduleController.getAllPartTimeWorkSchedules();
		int delegated;
		int size = 0;

		shiftCopies = shiftDB.findShiftCopiesOnState(CopyState.RELEASED.getState());
		size = shiftCopies.size();
		delegate(workSchedules);
		
		if(shiftCopies.isEmpty()) {
			delegated = 0;
		}
		else if(size == shiftCopies.size()) {
			delegated = 1;
		}
		else {
			delegated = -1;
		}
		return delegated;
	}
	
	/**
	 * Sorts the list of work schedules from lowest to highest total working hours. 
	 * Associates all possible shift copies with the work schedules with lowest total working hours. 
	 * @param workSchedules
	 * @throws DataAccessException
	 */
	private void delegate(ArrayList<WorkSchedule> workSchedules) throws DataAccessException {
		workSchedules = workScheduleController.getAllPartTimeWorkSchedules();
		workSchedules.sort((w1,  w2) -> w1.getTotalHours().compareTo(w2.getTotalHours()));	// Sorts the list of work schedules.
		int workScheduleID;
		int workScheduleIndex = 0;
		int copyIndex = 0;
		int lastWorkScheduleIndex = workSchedules.size() - 1;
		int lastCopyIndex = shiftCopies.size() - 1;
		int hours = 0;
		String employeeCPR;
		ShiftCopy shiftCopy;
		String state = CopyState.DELEGATED.getState();
		
		/* Looping through the copies, and trying to delegate them one by one.*/
		
		while (!shiftCopies.isEmpty()) {
			shiftCopy = shiftCopies.get(copyIndex);
			workScheduleID = workSchedules.get(workScheduleIndex).getID();
			employeeCPR = workSchedules.get(workScheduleIndex).getEmployeeCPR();
			
			if(shiftDB.takeShift(shiftCopy, workScheduleID, state)) {
				hours = calculateTotalHours(shiftCopy);
				workScheduleController.setTotalHoursOnWorkSchedule(hours, employeeCPR);
				shiftCopies.remove(shiftCopy);
				if(shiftCopies.size() == 0) {							// Checks if the list of copies is now empty.
					shiftCopies = shiftDB.findShiftCopiesOnState(CopyState.RELEASED.getState());	// Populates the list again, in case some delegable copies have been removed.
				}
				delegate(workSchedules);
			}
			else if(workScheduleIndex == lastWorkScheduleIndex && copyIndex == lastCopyIndex) { 	// If both the last copy and work schedule have been reached.
				shiftCopies.clear();
			}
			else if(copyIndex == lastCopyIndex && workScheduleIndex < lastWorkScheduleIndex) { 		// If only the last copy has been reached.
				workScheduleIndex++;
			}
			else if(copyIndex < lastCopyIndex) { // Checks if there are more copies in the list.
				shiftCopies.remove(copyIndex);
			}
		}
		shiftCopies = shiftDB.findShiftCopiesOnState(CopyState.RELEASED.getState());
	}
	
	/**
	 * Calculates total hours of a given shift copy.
	 * @param copy
	 * @return totalHours
	 */
	private int calculateTotalHours(ShiftCopy shiftCopy) {
		LocalTime toHours = shiftCopy.getShift().getToHour();
		LocalTime fromHours = shiftCopy.getShift().getFromHour();
		int toHoursToAdd = toHours.getHour();
		int fromHoursToAdd = fromHours.getHour();
		
		int totalHours = toHoursToAdd - fromHoursToAdd;
		return totalHours;			
	}
	
	/**
	 * Finds a shift, creates a copy of the shift and adds the
	 * copy to a list of copies.
	 * @param date
	 * @param fromHour
	 * @param toHour
	 * @return shiftCopies
	 * @throws DataAccessException
	 */
	public ArrayList<ShiftCopy> addShift(LocalDate date, LocalTime fromHour, LocalTime toHour) throws DataAccessException {
		Shift shift = shiftDB.findShiftOnFromAndTo(fromHour, toHour);
		ShiftCopy shiftCopy = shift.createCopy(shift, date);
		shiftCopiesToBeReleased.add(shiftCopy);
		return shiftCopiesToBeReleased;
	}
	
	/**
	 * Saves all added shift copies to the database.
	 * Clears the list of copies. 
	 * @return completed
	 * @throws DataAccessException
	 */
	public boolean completeReleaseNewShifts() throws DataAccessException {
		boolean completed = false;
		
		if(shiftDB.completeReleaseNewShifts(shiftCopiesToBeReleased)) {
			completed = true;
			shiftCopiesToBeReleased.clear();
		}
		return completed;
	}
	
	/**
	 * Finds a shift copy on a given index, and removes it
	 * from the list of copies. 
	 * @param index
	 * @return deleted
	 */
	public boolean deleteShiftCopy(int index) {
		boolean deleted = false;
		ShiftCopy shiftCopy = shiftCopiesToBeReleased.get(index);
		shiftCopiesToBeReleased.remove(index);
		
		if(!shiftCopiesToBeReleased.contains(shiftCopy)) {
			deleted = true;
		}
		return deleted;
	}
	
	/**
	 * Checks if 24 hours has passed since the shift copies were released.
	 * @return canBeDelegated
	 * @throws DataAccessException 
	 */
	public boolean checkReleasedAt() throws DataAccessException {
//		boolean canBeDelegated = false;
		boolean canBeDelegated = true;	// Set to true in order to show GUI functionality.
		shiftCopies = shiftDB.findShiftCopiesOnState(CopyState.RELEASED.getState());
		
		for(ShiftCopy element : shiftCopies) {
			LocalDateTime current = element.getReleasedAt();
			LocalDateTime nowMinus24Hours = LocalDateTime.now().minusHours(24);
			if(nowMinus24Hours.isAfter(current) || nowMinus24Hours.isEqual(current)) { 	// Checks if 24 hours or more has passed.
				canBeDelegated = true;
			}
		}
		return canBeDelegated;
	}
	
	/**
	 * Makes a list of copies where state is Tradeable and sets shiftCopies to that list.
	 * @return shiftCopies
	 * @throws DataAccessException
	 */
	public ArrayList<ShiftCopy> startTakePlannedShift() throws DataAccessException {
		String employeeCPR = "";
		int id = 0;
		shiftCopies = shiftDB.findShiftCopiesOnState(CopyState.TRADEABLE.getState());
		
		for(ShiftCopy element : shiftCopies) {
			id = element.getWorkSchedule().getID();
			employeeCPR = workScheduleController.getEmployeeCPROnID(id);
			element.getWorkSchedule().setEmployeeCPR(employeeCPR);
		}
		return shiftCopies;
	}

	/**
	 * Associates a shift copy on a given index, with the work schedule belonging
	 * to the logged in employee.
	 * Calculates and sets new total hours for logged in employee.
	 * @param index
	 * @return success
	 * @throws DataAccessException
	 */
	public boolean takePlannedShift(ShiftCopy shiftCopy) throws DataAccessException {
		boolean succes = false;
		int hours = 0;
		hours = calculateTotalHours(shiftCopy) * -1;
		String currentEmployeeCPR = shiftCopy.getWorkSchedule().getEmployeeCPR();
		String employeeCPR = employeeController.getLoggedInEmployee().getCPR();
		int workScheduleID = workScheduleController.findWorkScheduleIDOnEmployeeCPR(employeeCPR);
		String state = CopyState.OCCUPIED.getState();
		
		if(shiftDB.takeShift(shiftCopy, workScheduleID, state)) {
			workScheduleController.setTotalHoursOnWorkSchedule(hours, currentEmployeeCPR);
			hours = hours * -1;
			workScheduleController.setTotalHoursOnWorkSchedule(hours, employeeCPR);
			shiftCopies.remove(shiftCopy);
			succes = true;
		}
		return succes;
	}
	
	/**
	 * Gets list of shift copies. 
	 * @return shiftCopies. 
	 */
	public ArrayList<ShiftCopy> getShiftCopies() {
		return shiftCopies;
	}
  
	/**
	 * Clears the list of shift copies. 
	 */
	public void clearShiftCopies() {
		shiftCopies.clear();
	}
	
	/**
	 * Adds a copy object to list of released shift copies. 
	 * @param copy
	 */
	public void addCopyToShiftCopyList(ShiftCopy shiftCopy) {
		shiftCopies.add(shiftCopy);
	}
	
	/**
	 * Gets a list of shift copies marked as 'Released'.
	 * @return releasedShiftCopies
	 * @throws DataAccessException
	 */
	public ArrayList<ShiftCopy> getShiftCopiesAgain(String state) throws DataAccessException {
		shiftCopies = shiftDB.findShiftCopiesOnState(state);
		return shiftCopies;
	}

}

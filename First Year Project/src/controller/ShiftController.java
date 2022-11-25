package controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import database.ShiftDB;
import database.ShiftDBIF;
import model.Copy;
import model.Employee;
import model.Shift;
import model.Shop;
import model.WorkSchedule;
import utility.CopyState;
import utility.DataAccessException;

public class ShiftController {
	
	private EmployeeController employeeController;
	private ShopController shopController;
	private WorkScheduleController workScheduleController;
	private ShiftDBIF shiftDB;
	private ArrayList<Copy> shiftCopies;
	private ArrayList<Copy> releasedShiftCopies;
	
	/**
	 * Constructor to initialize instance variables. 
	 * @throws DataAccessException
	 */
	public ShiftController() throws DataAccessException {
		employeeController = new EmployeeController();
		shopController = new ShopController();
		workScheduleController = new WorkScheduleController();
		shiftDB = new ShiftDB();
		shiftCopies = new ArrayList<>();
		releasedShiftCopies = new ArrayList<>();
	}
	
	/**
	 * Finds all shift copies marked as 'Released'.
	 * @return releasedShiftCopies
	 * @throws DataAccessException
	 */
	public ArrayList<Copy> startTakeNewShift() throws DataAccessException {
		releasedShiftCopies = shiftDB.findReleasedShiftCopies();
		return releasedShiftCopies;
	}
	
	/**
	 * Associates a shift copy on a given index, with the work schedule belonging
	 * to the logged in employee.
	 * Calculates and sets new total hours for logged in employee.
	 * @param index
	 * @return success
	 * @throws DataAccessException
	 */
	public boolean takeNewShift(Copy copy) throws DataAccessException {
		boolean success = false;
		String employeeCPR = employeeController.getLoggedInEmployee().getCPR();
		int workScheduleID = workScheduleController.findWorkScheduleIDOnEmployeeCPR(employeeCPR);
		String state = CopyState.OCCUPIED.getState();
		
		if(shiftDB.takeNewShift(copy, workScheduleID, state)) {
			calculateAndSetTotalHours(copy, employeeCPR);
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
		workSchedules = workScheduleController.getAllWorkSchedules();
		int delegated;
		
		delegate(workSchedules);
		releasedShiftCopies = shiftDB.findReleasedShiftCopies();
		
		if(releasedShiftCopies.isEmpty()) {
			delegated = 0;
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
		int workScheduleID;
		int workScheduleIndex = 0;
		String employeeCPR;
		int index = 0;
		Copy copy;
		String state = CopyState.DELEGATED.getState();
		workSchedules.sort((w1,  w2) -> w1.getTotalHours().compareTo(w2.getTotalHours()));
		
		while (!releasedShiftCopies.isEmpty()) {
			copy = releasedShiftCopies.get(index);
			workScheduleID = workSchedules.get(workScheduleIndex).getID();
			employeeCPR = workSchedules.get(workScheduleIndex).getEmployeeCPR();
			
			if(shiftDB.takeNewShift(copy, workScheduleID, state)) {
				calculateAndSetTotalHours(copy, employeeCPR);
				if(releasedShiftCopies.size() == 0) {
					releasedShiftCopies = shiftDB.findReleasedShiftCopies();
				}
				delegate(workSchedules);
			}
			else if(workScheduleIndex == workSchedules.size() - 1 && index == releasedShiftCopies.size() - 1) {
				releasedShiftCopies.clear();
			}
			else if(index == releasedShiftCopies.size() - 1 && workScheduleIndex < workSchedules.size() - 1) {
				workScheduleIndex++;
			}
			else if(index < releasedShiftCopies.size() - 1) {
				releasedShiftCopies.remove(index);
			}
		}
	}
	

	/**
	 * Calculates and sets new total hours on work schedule for a given employee. 
	 * @param copy
	 * @param employeeCPR
	 * @param index
	 * @throws DataAccessException
	 */
	private void calculateAndSetTotalHours(Copy copy, String employeeCPR) throws DataAccessException {
		int hours = calculateTotalHours(copy);
		workScheduleController.setTotalHoursOnWorkSchedule(hours, employeeCPR);
		releasedShiftCopies.remove(copy);
	}
	
	/**
	 * Calculates total hours of a given shift copy.
	 * @param copy
	 * @return totalHours
	 */
	private int calculateTotalHours(Copy copy) {
		LocalTime toHours = copy.getShift().getToHour();
		LocalTime fromHours = copy.getShift().getFromHour();
		int toHoursToAdd = toHours.getHour();
		int fromHoursToAdd = fromHours.getHour();
		int totalHours = toHoursToAdd - fromHoursToAdd;
		return totalHours;			
	}

	/**
	 * Finds the logged in employee object, and finds and sets a shop object to it.
	 * @throws DataAccessException
	 */
	public void startReleaseNewShifts() throws DataAccessException {
		Employee employee = employeeController.getLoggedInEmployee();
		
		if(employee != null) {
			int id = employee.getShop().getID();
			Shop shop = shopController.findShopOnID(id);
			employee.setShop(shop);
		}
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
	public ArrayList<Copy> addShift(LocalDate date, LocalTime fromHour, LocalTime toHour) throws DataAccessException {
		Shift shift = shiftDB.findShiftOnFromAndTo(fromHour, toHour);
		Copy copy = shift.createCopy(shift, date);
		shiftCopies.add(copy);
		return shiftCopies;
	}
	
	/**
	 * Saves all added shift copies to the database.
	 * Clears the list of copies. 
	 * @return completed
	 * @throws DataAccessException
	 */
	public boolean completeReleaseNewShifts() throws DataAccessException {
		boolean completed = false;
		
		if(shiftDB.completeReleaseNewShifts(shiftCopies)) {
			completed = true;
			shiftCopies.clear();
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
		Copy copy = shiftCopies.get(index);
		shiftCopies.remove(index);
		
		if(!shiftCopies.contains(copy)) {
			deleted = true;
		}
		return deleted;
	}
	
	/**
	 * Checks if 24 hours has passed since the shift copies were released.
	 * @return canBeDelegated
	 */
	public boolean checkReleasedAt() {
//		boolean canBeDelegated = false;
		boolean canBeDelegated = true;
		
		for(Copy element : releasedShiftCopies) {
			LocalDateTime current = element.getReleasedAt();
			LocalDateTime nowMinus24Hours = LocalDateTime.now().minusHours(24);
			if(nowMinus24Hours.isAfter(current) || nowMinus24Hours.isEqual(current)) {
				canBeDelegated = true;
			}
		}
		return canBeDelegated;
	}
	
	/**
	 * Gets list of shift copies. 
	 * @return shiftCopies. 
	 */
	public ArrayList<Copy> getShiftCopies() {
		return shiftCopies;
	}
	
	public ArrayList<Copy> getReleasedShiftCopiesList() {
		return releasedShiftCopies;
	}
  
	/**
	 * Clears the list of shift copies. 
	 */
	public void clearShiftCopies() {
		shiftCopies.clear();
	}
	
	public void addCopyToReleasedShiftCopy(Copy copy) {
		releasedShiftCopies.add(copy);
	}
	/**
	 * Gets a list of shift copies marked as 'Released'.
	 * @return releasedShiftCopies
	 * @throws DataAccessException
	 */
	public ArrayList<Copy> getReleasedCopies() throws DataAccessException {
		releasedShiftCopies = shiftDB.findReleasedShiftCopies();
		return releasedShiftCopies;
	}

}

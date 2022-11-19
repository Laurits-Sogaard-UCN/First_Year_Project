package controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import database.ShiftDB;
import database.ShiftDBIF;
import model.Copy;
import model.Employee;
import model.Shift;
import model.Shop;
import model.WorkSchedule;
import utility.CopyState;
import utility.DataAccessException;

public class ShiftController {//TODO Catch exceptions her i controller
	
	private EmployeeController employeeController;
	private ShopController shopController;
	private WorkScheduleController workScheduleController;
	private ShiftDBIF shiftDB;
	private ArrayList<Copy> shiftCopies;
	private ArrayList<Copy> releasedShiftCopies;
	
	public ShiftController() throws DataAccessException {
		employeeController = new EmployeeController();
		shopController = new ShopController();
		workScheduleController = new WorkScheduleController();
		shiftDB = new ShiftDB();
		shiftCopies = new ArrayList<>();
		releasedShiftCopies = new ArrayList<>();
	}
	

	public void startReleaseNewShifts() throws DataAccessException {
		Employee employee = employeeController.getLoggedInEmployee();
		
		if(employee != null) {
			int id = employee.getShop().getID();
			Shop shop = shopController.findShopOnID(id);
			employee.setShop(shop);
		}
	}
	
	public ArrayList<Copy> addShift(LocalDate date, LocalTime fromHour, LocalTime toHour) throws DataAccessException {
		Shift shift = shiftDB.findShiftOnFromAndTo(fromHour, toHour);
		Copy copy = shift.createCopy(shift, date);
		shiftCopies.add(copy);
		return shiftCopies;
	}
	
	public boolean completeReleaseNewShifts() throws DataAccessException {
		boolean completed = false;
		
		if(shiftDB.completeReleaseNewShifts(shiftCopies)) {
			completed = true;
			shiftCopies.clear();
		}
		return completed;
	}
	
	public boolean deleteShiftCopy(int index) {
		boolean deleted = false;
		Copy copy = shiftCopies.get(index);
		shiftCopies.remove(index);
		
		if(!shiftCopies.contains(copy)) {
			deleted = true;
		}
		return deleted;
	}
	
	public ArrayList<Copy> startTakeNewShift() throws DataAccessException {
		releasedShiftCopies = shiftDB.findReleasedShiftCopies();
		return releasedShiftCopies;
	}
	
	public boolean takeNewShift(int index) throws DataAccessException {
		boolean success = false;
		Copy copy = releasedShiftCopies.get(index);
		byte[] currentVersionNumber = shiftDB.findCopyVersionNumberOnID(copy.getId());
		String employeeCPR = employeeController.getLoggedInEmployee().getCPR();
		int workScheduleID = workScheduleController.findWorkScheduleIDOnEmployeeCPR(employeeCPR);
		String state = CopyState.OCCUPIED.getState();
		
		if(Arrays.equals(copy.getVersionNumber(), currentVersionNumber)) { //TODO måske vi skal holde os til pessimistisk samtidighedskontrol?
			if(shiftDB.takeNewShift(copy, workScheduleID, state)) {
				calculateAndSetTotalHours(copy, employeeCPR, index);
				success = true;
			}
		}
		return success;
	}
	
	public boolean checkReleasedAt() {
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
	
	public int delegateShifts(int index, int workScheduleIndex) throws DataAccessException {
		int delegated = 0;
		Copy copy;
		ArrayList<WorkSchedule> workSchedules;
		int workScheduleID;
		String employeeCPR;
		String state = CopyState.DELEGATED.getState();
		releasedShiftCopies = shiftDB.findReleasedShiftCopies();
		
		if(!releasedShiftCopies.isEmpty()) {
			copy = releasedShiftCopies.get(index);
			workSchedules = workScheduleController.getAllWorkSchedules();
			workSchedules.sort(null);
			workScheduleID = workSchedules.get(workScheduleIndex).getID();
			employeeCPR = workSchedules.get(workScheduleIndex).getEmployeeCPR();
			
			if(shiftDB.takeNewShift(copy, workScheduleID, state)) {
				calculateAndSetTotalHours(copy, employeeCPR, index);
				delegateShifts(0, 0);
				delegated = 1;
			}
			else if(!releasedShiftCopies.isEmpty() && workScheduleIndex == workSchedules.size() - 1 && index == releasedShiftCopies.size() - 1) {
				releasedShiftCopies.clear();
			}
			else if(workScheduleIndex < workSchedules.size() - 1) {
				delegateShifts(index, workScheduleIndex + 1);
			}
			else if(workScheduleIndex == workSchedules.size() - 1) {
				delegateShifts(index + 1, 0);
			}
			
		}
		if(releasedShiftCopies.isEmpty()) {
			delegated = -1;
		}
		return delegated;
	}
	
	private void calculateAndSetTotalHours(Copy copy, String employeeCPR, int index) throws DataAccessException {
		int hours = calculateTotalHours(copy);
		workScheduleController.setTotalHoursOnWorkSchedule(hours, employeeCPR);
		releasedShiftCopies.remove(index);
	}
	
	private int calculateTotalHours(Copy copy) {
		LocalTime toHours = copy.getShift().getToHour();
		LocalTime fromHours = copy.getShift().getFromHour();
		int toHoursToAdd = toHours.getHour();
		int fromHoursToAdd = fromHours.getHour();
		int totalHours = toHoursToAdd - fromHoursToAdd;
		return totalHours;			
	}
	
	public ArrayList<Copy> getShiftCopies() {
		return shiftCopies;
	}
	
	public ArrayList<Copy> getReleasedCopies() throws DataAccessException {
		releasedShiftCopies = shiftDB.findReleasedShiftCopies();
		return releasedShiftCopies;
	}
	
	public void clearShiftCopies() {
		shiftCopies.clear();
	}

}

package controller;

import java.time.LocalDate;


import java.util.ArrayList;
import java.util.Arrays;

import database.ShiftDB;
import database.ShiftDBIF;
import model.Copy;
import model.Employee;
import model.Shift;
import model.Shop;
import utility.DataAccessException;

public class ShiftController {
	
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
	
	public ArrayList<Copy> addShift(LocalDate date, int fromHour, int toHour) throws DataAccessException {
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
		if(Arrays.equals(copy.getVersionNumber(), currentVersionNumber)) {
			if(shiftDB.takeNewShift(copy.getId(), workScheduleID)) { //TODO Fiks
				releasedShiftCopies.remove(index);
				success = true;
			}
		}
		return success;
	}
	
	public ArrayList<Copy> getShiftCopies() {
		return shiftCopies;
	}
	
	public ArrayList<Copy> getReleasedCopies() {
		return releasedShiftCopies;
	}

}

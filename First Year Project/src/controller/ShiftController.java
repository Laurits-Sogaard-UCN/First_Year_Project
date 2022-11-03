package controller;

import java.time.LocalDate;


import java.util.ArrayList;

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
	private ShiftDBIF shiftDB;
	private ArrayList<Copy> workShiftCopies;
	
	public ShiftController() throws DataAccessException {
		employeeController = new EmployeeController();
		shopController = new ShopController();
		shiftDB = new ShiftDB();
		workShiftCopies = new ArrayList<>();
	}
	
	public void startReleaseWorkShifts() throws DataAccessException {
		Employee employee = employeeController.getLoggedInEmployee();
		if(employee != null) {
			int id = employee.getShop().getID();
			Shop shop = shopController.findShopOnID(id);
			employee.setShop(shop);
		}
	}
	
	public ArrayList<Copy> addWorkShift(LocalDate date, int fromHour, int toHour) throws DataAccessException {
		Shift shift = shiftDB.findShiftOnFromAndTo(fromHour, toHour);
		Copy copy = shift.createCopy(shift, date);
		workShiftCopies.add(copy);
		return workShiftCopies;
	}
	
	public boolean completeReleaseWorkShifts() throws DataAccessException {
		boolean completed = false;
		if(shiftDB.completeReleaseWorkShifts(workShiftCopies)) {
			completed = true;
			workShiftCopies.clear();
		}
		return completed;
	}

}

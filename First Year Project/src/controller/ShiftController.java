package controller;

import java.time.LocalDate;
import java.util.ArrayList;

import database.ShiftDB;
import database.ShiftDBIF;
import model.Copy;
import model.Employee;
import model.Manager;
import model.Shift;
import model.Shop;
import model.WorkShift;
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
	}
	
	public Employee login(String username, String password) throws DataAccessException {
		Employee employee = employeeController.login(username, password);
		if(employee != null) {
			int id = employee.getShop().getID();
			Shop shop = shopController.findShopOnID(id);
			employee.setShop(shop);
		}
		return employee;
	}
	
	public void startReleaseWorkShifts() {
		workShiftCopies = new ArrayList<>();
	}
	
	public ArrayList<Copy> addWorkShift(LocalDate date, int fromHour, int toHour) throws DataAccessException {
		Shift shift = new Shift();
		WorkShift workShift = (WorkShift) shiftDB.findShiftOnFromAndTo(fromHour, toHour);
		Copy copy = shift.createCopy(workShift, date);
		workShiftCopies.add(copy);
		return workShiftCopies;
	}

}

package database;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.Employee;
import model.FullTimeEmployee;
import model.PartTimeEmployee;
import model.Shop;
import utility.DBMessages;
import utility.DataAccessException;

public class EmployeeDB implements EmployeeDBIF {
	
	/**
	 * Constructor to initialize instance variables.
	 * @throws DataAccessException
	 */
	public EmployeeDB() throws DataAccessException {
		init();
	}
	
	/**
	 * Initialization of Connection and PreparedStatments.
	 * @throws DataAccessException
	 */
	private void init() throws DataAccessException {
		Connection con = DBConnection.getInstance().getConnection();
		
	}
	
	// TODO find ud af om det her skal kasseres - det blev brugt til login.
	
	private Employee buildEmployeeObject(ResultSet rs) throws DataAccessException {
		Employee employee;
		String CPR;
		String fname;
		String lname;
		String email;
		String address;
		String phone;
		String username;
		String password;
		String employeeType;
		Shop shop;
		
		try {
			CPR = rs.getString("CPR");
			fname = rs.getString("Fname");
			lname = rs.getString("Lname");
			email = rs.getString("Email");
			address = rs.getString(rs.getString("Street") + " " + rs.getString("StreetNumber"));
			phone = rs.getString("Phone");
			username = rs.getString("Username");
			password = rs.getString("Password");
			employeeType = rs.getString("EmployeeType");
			shop = new Shop(rs.getInt("ShopID"));
			employee = new Employee(CPR, lname, fname, email, address, phone, username, password, employeeType, shop);
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_READ_RESULTSET, e);
		}
		return employee;
	}
	
	private Employee buildPartTimeEmployeeObject(ResultSet rs) throws DataAccessException {
		PartTimeEmployee employee = (PartTimeEmployee) buildEmployeeObject(rs);
		double hourlyWage;
		
		try {
			hourlyWage = rs.getFloat("HourlyWage");
			employee.setHourlyWage(hourlyWage);
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_READ_RESULTSET, e);
		}
		return employee;
	}
	
	private Employee buildFullTimeEmployeeObject(ResultSet rs) throws DataAccessException {
		FullTimeEmployee employee = (FullTimeEmployee) buildEmployeeObject(rs);
		double monthlyWage;
		
		try {
			monthlyWage = rs.getFloat("MonthlyWage");
			employee.setMonthlyWage(monthlyWage);
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_READ_RESULTSET, e);
		}
		return employee;
	}
	
	
	

}

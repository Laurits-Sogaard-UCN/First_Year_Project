package database;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Employee;
import model.EmployeeType;
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
	
	private Employee buildEmployeeObject(ResultSet rs) throws DataAccessException {
		Employee employee;
		try {
			String CPR = rs.getString("CPR");
			String fname = rs.getString("Fname");
			String lname = rs.getString("Lname");
			String email = rs.getString("Email");
			String address = rs.getString(rs.getString("Street") + " " + rs.getString("StreetNumber"));
			String phone = rs.getString("Phone");
			String username = rs.getString("Username");
			String password = rs.getString("Password");
			String employeeType = rs.getString("EmployeeType");
			Shop shop = new Shop(rs.getInt("ShopID"));
			employee = new Employee(CPR, lname, fname, email, address, phone, username, password, employeeType, shop);
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_READ_RESULTSET, e);
		}
		return employee;
	}
	
	private Employee buildPartTimeEmployeeObject(ResultSet rs) throws DataAccessException {
		PartTimeEmployee employee = (PartTimeEmployee) buildEmployeeObject(rs);
		try {
			double hourlyWage = rs.getFloat("HourlyWage");
			employee.setHourlyWage(hourlyWage);
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_READ_RESULTSET, e);
		}
		return employee;
	}
	
	private Employee buildFullTimeEmployeeObject(ResultSet rs) throws DataAccessException {
		FullTimeEmployee employee = (FullTimeEmployee) buildEmployeeObject(rs);
		try {
			double monthlyWage = rs.getFloat("MonthlyWage");
			employee.setMonthlyWage(monthlyWage);
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_READ_RESULTSET, e);
		}
		return employee;
	}
	
	
	

}

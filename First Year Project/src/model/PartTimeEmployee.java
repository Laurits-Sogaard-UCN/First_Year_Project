package model;

public class PartTimeEmployee extends Employee {
	
	private double hourlyWage;

	/**
	 * Constructor to initialize instance variables.
	 * @param cPR
	 * @param lname
	 * @param fname
	 * @param email
	 * @param address
	 * @param phone
	 * @param username
	 * @param password
	 * @param type
	 * @param shop
	 * @param hourlyWage
	 */
	public PartTimeEmployee(String CPR, String lname, String fname, String email, String address, String phone,
			String username, String password, String type, Shop shop, double hourlyWage) {
		super(CPR, lname, fname, email, address, phone, username, password, type, shop);
		this.hourlyWage = hourlyWage;
	}

	/**
	 * Gets hourly wage.
	 * @return hourlyWage
	 */
	public double getHourlyWage() {
		return hourlyWage;
	}

	/**
	 * Sets a new hourly wage.
	 * @param hourlyWage
	 */
	public void setHourlyWage(double hourlyWage) {
		this.hourlyWage = hourlyWage;
	}
	
	
}

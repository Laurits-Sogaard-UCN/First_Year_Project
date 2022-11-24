package model;

public class FullTimeEmployee extends Employee {
	
	private double monthlyWage;
	
	/**
	 * Constructor to initialize instance variables.
	 * @param CPR
	 * @param lname
	 * @param fname
	 * @param email
	 * @param address
	 * @param phone
	 * @param username
	 * @param password
	 * @param type
	 * @param shop
	 * @param monthlyWage
	 */
	public FullTimeEmployee(String CPR, String lname, String fname, String email, String address, String phone,
			String username, String password, String type, Shop shop, double monthlyWage) {
		super(CPR, lname, fname, email, address, phone, username, password, type, shop);
		this.monthlyWage = monthlyWage;
	}

	/**
	 * Gets monthly wage.
	 * @return monthlyWage
	 */
	public double getMonthlyWage() {
		return monthlyWage;
	}

	/**
	 * Sets a new monthly wage.
	 * @param monthlyWage
	 */
	public void setMonthlyWage(double monthlyWage) {
		this.monthlyWage = monthlyWage;
	}


}

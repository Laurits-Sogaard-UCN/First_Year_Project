package model;

public class PartTimeEmployee extends Employee {
	
	private double hourlyWage;

	/**
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
	public PartTimeEmployee(String cPR, String lname, String fname, String email, String address, String phone,
			String username, String password, String type, Shop shop, double hourlyWage) {
		super(cPR, lname, fname, email, address, phone, username, password, type, shop);
		this.hourlyWage = hourlyWage;
	}

	/**
	 * @return the hourlyWage
	 */
	public double getHourlyWage() {
		return hourlyWage;
	}

	/**
	 * @param hourlyWage the hourlyWage to set
	 */
	public void setHourlyWage(double hourlyWage) {
		this.hourlyWage = hourlyWage;
	}
	
	
}

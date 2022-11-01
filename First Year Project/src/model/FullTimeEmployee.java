package model;

public class FullTimeEmployee extends Employee {
	
	private double monthlyWage;
	
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
	 * @param monthlyWage
	 */
	public FullTimeEmployee(String cPR, String lname, String fname, String email, String address, String phone,
			String username, String password, String type, Shop shop, double monthlyWage) {
		super(cPR, lname, fname, email, address, phone, username, password, type, shop);
		this.monthlyWage = monthlyWage;
	}

	/**
	 * @return the monthlyWage
	 */
	public double getMonthlyWage() {
		return monthlyWage;
	}

	/**
	 * @param monthlyWage the monthlyWage to set
	 */
	public void setMonthlyWage(double monthlyWage) {
		this.monthlyWage = monthlyWage;
	}


}

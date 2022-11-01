package model;

import java.util.ArrayList;

public class Manager extends FullTimeEmployee {
	
	private int managerNumber;

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
	 * @param managerNumber
	 */
	public Manager(String cPR, String lname, String fname, String email, String address, String phone, String username,
			String password, String type, Shop shop, double monthlyWage, int managerNumber) {
		super(cPR, lname, fname, email, address, phone, username, password, type, shop, monthlyWage);
		this.managerNumber = managerNumber;
	}

	/**
	 * @return the managerNumber
	 */
	public int getManagerNumber() {
		return managerNumber;
	}

	/**
	 * @param managerNumber the managerNumber to set
	 */
	public void setManagerNumber(int managerNumber) {
		this.managerNumber = managerNumber;
	}
	
	
	
	
}

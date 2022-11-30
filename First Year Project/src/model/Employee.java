package model;

public class Employee {
	
	private String CPR;
	private String lname;
	private String fname;
	private String email;
	private String address;
	private String phone;
	private String username;
	private String password;
	private String type;
	private Shop shop;
	
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
	 */
	public Employee(String CPR, String lname, String fname, String email, String address, String phone, String username,
			String password, String type, Shop shop) {
		this.CPR = CPR;
		this.lname = lname;
		this.fname = fname;
		this.email = email;
		this.address = address;
		this.phone = phone;
		this.username = username;
		this.password = password;
		this.type = type;
		this.shop = shop;
	}
	
	/**
	 * Gets CPR.
	 * @return CPR
	 */
	public String getCPR() {
		return CPR;
	}

}

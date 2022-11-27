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
	
	/**
	 * Sets a new CPR.
	 * @param CPR
	 */
	public void setCPR(String CPR) {
		this.CPR = CPR;
	}
	
	/**
	 * gets lname.
	 * @return lname
	 */
	public String getLname() {
		return lname;
	}
	
	/**
	 * Sets a new lname.
	 * @param lname
	 */
	public void setLname(String lname) {
		this.lname = lname;
	}
	
	/**
	 * Gets fname.
	 * @return fname
	 */
	public String getFname() {
		return fname;
	}
	
	/**
	 * Sets a new fname.
	 * @param fname
	 */
	public void setFname(String fname) {
		this.fname = fname;
	}
	
	/**
	 * Gets email.
	 * @return email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets a new email.
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Gets address.
	 * @return address
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Sets a new address.
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * Gets phone.
	 * @return phone
	 */
	public String getPhone() {
		return phone;
	}
	
	/**
	 * Sets a new phone.
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	/**
	 * Gets username.
	 * @return username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sets a new username.
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Gets password.
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets a new password.
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Gets type.
	 * @return type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Sets a new type.
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Gets shop.
	 * @return shop
	 */
	public Shop getShop() {
		return shop;
	}
	
	/**
	 * Sets a new shop.
	 * @param shop
	 */
	public void setShop(Shop shop) {
		this.shop = shop;
	}

}

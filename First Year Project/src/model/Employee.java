package model;

import java.util.ArrayList;

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
	 */
	public Employee(String cPR, String lname, String fname, String email, String address, String phone, String username,
			String password, String type, Shop shop) {
		super();
		CPR = cPR;
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
	 * @return the cPR
	 */
	public String getCPR() {
		return CPR;
	}
	/**
	 * @param cPR the cPR to set
	 */
	public void setCPR(String cPR) {
		CPR = cPR;
	}
	/**
	 * @return the lname
	 */
	public String getLname() {
		return lname;
	}
	/**
	 * @param lname the lname to set
	 */
	public void setLname(String lname) {
		this.lname = lname;
	}
	/**
	 * @return the fname
	 */
	public String getFname() {
		return fname;
	}
	/**
	 * @param fname the fname to set
	 */
	public void setFname(String fname) {
		this.fname = fname;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the shop
	 */
	public Shop getShop() {
		return shop;
	}
	/**
	 * @param shop the shop to set
	 */
	public void setShop(Shop shop) {
		this.shop = shop;
	}

}

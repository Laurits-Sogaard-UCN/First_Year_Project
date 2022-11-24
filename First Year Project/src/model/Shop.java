package model;

public class Shop {
	
	private String address;
	private int zipcode;
	private String city;
	private String country;
	private String name;
	private int ID;

	/**
	 * Constructor to initialize instance variables.
	 * @param address
	 * @param zipcode
	 * @param city
	 * @param country
	 * @param name
	 * @param iD
	 */
	public Shop(String address, int zipcode, String city, String country, String name, int ID) {
		this.address = address;
		this.zipcode = zipcode;
		this.city = city;
		this.country = country;
		this.name = name;
		this.ID = ID;
	}

	/**
	 * Constructor to initialize instance variables.
	 * @param ID
	 */
	public Shop(int ID) {
		this.ID = ID;
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
	 * Gets zipcode.
	 * @return zipcode
	 */
	public int getZipcode() {
		return zipcode;
	}

	/**
	 * Sets a new zipcode.
	 * @param zipcode
	 */
	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * Gets city.
	 * @return city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets a new city.
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Gets country.
	 * @return country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Sets a new country.
	 * @param country
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
	/**
	 * Gets name.
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets a new name.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets ID.
	 * @return ID
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Sets a new ID:
	 * @param ID
	 */
	public void setID(int ID) {
		this.ID = ID;
	}
	

}

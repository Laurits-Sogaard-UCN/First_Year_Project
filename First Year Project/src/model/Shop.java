package model;

public class Shop {
	
	private String address;
	private int zipcode;
	private String city;
	private String country;
	private String name;
	private int ID;


	/**
	 * @param address
	 * @param zipcode
	 * @param city
	 * @param country
	 * @param name
	 * @param iD
	 */
	public Shop(String address, int zipcode, String city, String country, String name, int iD) {
		super();
		this.address = address;
		this.zipcode = zipcode;
		this.city = city;
		this.country = country;
		this.name = name;
		ID = iD;
	}

	public Shop(int ID) {
		this.ID = ID;
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
	 * @return the zipcode
	 */
	public int getZipcode() {
		return zipcode;
	}

	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
	}
	

}

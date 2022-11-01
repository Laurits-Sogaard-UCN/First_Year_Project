package model;

public class Shop {
	
	private String address;
	private String name;
	private int ID;
	
	/**
	 * @param address
	 * @param name
	 * @param iD
	 */
	public Shop(String address, String name, int ID) {
		this.address = address;
		this.name = name;
		this.ID = ID;
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

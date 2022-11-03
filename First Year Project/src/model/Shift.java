package model;

import java.time.LocalDate;

public class Shift {
	
	private int fromHour;
	private int toHour;
	private int ID;
	
	
	/**
	 * @param fromHour
	 * @param toHour
	 * @param shiftType
	 * @param iD
	 */
	public Shift(int fromHour, int toHour, String shiftType, int iD) {
		super();
		this.fromHour = fromHour;
		this.toHour = toHour;
		ID = iD;
	}

	public Shift() {
		
	}
	
	public Copy createCopy(Shift shift, LocalDate date) {
		Copy copy = new Copy(shift, null, null, date, "Released");
		return copy;
	}

	/**
	 * @return the fromHour
	 */
	public int getFromHour() {
		return fromHour;
	}

	/**
	 * @param fromHour the fromHour to set
	 */
	public void setFromHour(int fromHour) {
		this.fromHour = fromHour;
	}

	/**
	 * @return the toHour
	 */
	public int getToHour() {
		return toHour;
	}

	/**
	 * @param toHour the toHour to set
	 */
	public void setToHour(int toHour) {
		this.toHour = toHour;
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

package model;

import java.time.LocalDate;

public class Shift {
	
	private int fromHour;
	private int toHour;
	private String shiftType;
	
	/**
	 * @param fromHour
	 * @param toHour
	 * @param shiftType
	 */
	public Shift(int fromHour, int toHour, String shiftType) {
		this.fromHour = fromHour;
		this.toHour = toHour;
		this.shiftType = shiftType;
	}
	
	public Shift() {
		
	}
	
	public Copy createCopy(Shift shift, LocalDate date) {
		Copy copy = new Copy(shift, null, null, date);
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
	 * @return the shiftType
	 */
	public String getShiftType() {
		return shiftType;
	}

	/**
	 * @param shiftType the shiftType to set
	 */
	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}
	
	

}

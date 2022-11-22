package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import utility.CopyState;


public class Shift {
	
	private LocalTime fromHour;
	private LocalTime toHour;
	private int ID;
	
	/**
	 * @param fromHour
	 * @param toHour
	 * @param iD
	 */
	public Shift(LocalTime fromHour, LocalTime toHour, int ID) {
		this.fromHour = fromHour;
		this.toHour = toHour;
		this.ID = ID;
	}

	public Shift(int ID) {
		this.ID = ID;
	}

	public Shift() {
		
	}
	
	public Copy createFullCopy(int id, Shift shift, LocalDate date, String state, LocalDateTime releasedAt) {
		Copy copy = new Copy(id, shift, date, state, releasedAt);
		return copy;
	}
	
	public Copy createCopy(Shift shift, LocalDate date) {
		Copy copy = new Copy(shift, date, CopyState.RELEASED.getState(), LocalDateTime.now());
		return copy;
	}

	/**
	 * @return the fromHour
	 */
	public LocalTime getFromHour() {
		return fromHour;
	}

	/**
	 * @param fromHour the fromHour to set
	 */
	public void setFromHour(LocalTime fromHour) {
		this.fromHour = fromHour;
	}

	/**
	 * @return the toHour
	 */
	public LocalTime getToHour() {
		return toHour;
	}

	/**
	 * @param toHour the toHour to set
	 */
	public void setToHour(LocalTime toHour) {
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
	public void setID(int ID) {
		this.ID = ID;
	}
	
	

}

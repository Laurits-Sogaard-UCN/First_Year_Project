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
	 * Constructor to initialize instance variables.
	 * @param fromHour
	 * @param toHour
	 * @param iD
	 */
	public Shift(LocalTime fromHour, LocalTime toHour, int ID) {
		this.fromHour = fromHour;
		this.toHour = toHour;
		this.ID = ID;
	}

	/**
	 * Constructor to initialize instance variables.
	 * @param ID
	 */
	public Shift(int ID) {
		this.ID = ID;
	}

	/**
	 * Constructor to initialize instance variables.
	 */
	public Shift() {
		
	}
	
	/**
	 * Creates a full copy object. 
	 * @param id
	 * @param shift
	 * @param date
	 * @param state
	 * @param releasedAt
	 * @return copy
	 */
	public Copy createFullCopy(int id, Shift shift, LocalDate date, String state, LocalDateTime releasedAt) {
		Copy copy = new Copy(id, shift, date, state, releasedAt);
		return copy;
	}
	
	/**
	 * Creates a new copy object.
	 * @param shift
	 * @param date
	 * @return copy
	 */
	public Copy createCopy(Shift shift, LocalDate date) {
		Copy copy = new Copy(shift, date, CopyState.RELEASED.getState(), LocalDateTime.now());
		return copy;
	}

	/**
	 * Gets from hour.
	 * @return fromHour
	 */
	public LocalTime getFromHour() {
		return fromHour;
	}

	/**
	 * Sets a new from hour.
	 * @param fromHour
	 */
	public void setFromHour(LocalTime fromHour) {
		this.fromHour = fromHour;
	}

	/**
	 * Gets to hour.
	 * @return toHour
	 */
	public LocalTime getToHour() {
		return toHour;
	}

	/**
	 * Sets a new to hour.
	 * @param toHour
	 */
	public void setToHour(LocalTime toHour) {
		this.toHour = toHour;
	}

	/**
	 * Gets ID.
	 * @return ID
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Sets a new ID.
	 * @param ID
	 */
	public void setID(int ID) {
		this.ID = ID;
	}

}

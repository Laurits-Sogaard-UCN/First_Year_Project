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
	 * Gets to hour.
	 * @return toHour
	 */
	public LocalTime getToHour() {
		return toHour;
	}

	/**
	 * Gets ID.
	 * @return ID
	 */
	public int getID() {
		return ID;
	}


}

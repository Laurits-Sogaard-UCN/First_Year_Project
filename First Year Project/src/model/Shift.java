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
	public ShiftCopy createFullCopy(int id, Shift shift, WorkSchedule workSchedule, LocalDate date, String state, LocalDateTime releasedAt) {
		ShiftCopy shiftCopy = new ShiftCopy(id, shift, workSchedule, date, state, releasedAt);
		return shiftCopy;
	}
	
	/**
	 * Creates a new copy object.
	 * @param shift
	 * @param date
	 * @return copy
	 */
	public ShiftCopy createCopy(Shift shift, LocalDate date) {
		ShiftCopy shiftCopy = new ShiftCopy(shift, date, CopyState.RELEASED.getState(), LocalDateTime.now());
		return shiftCopy;
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

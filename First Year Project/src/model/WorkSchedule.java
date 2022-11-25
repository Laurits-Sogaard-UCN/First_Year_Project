package model;

import java.time.LocalDate;

public class WorkSchedule  {
	
	private int ID;
	private LocalDate fromDate;
	private LocalDate toDate;
	private int totalHours;
	private String employeeCPR;
	
	/**
	 * Constructor to initialize instance variables.
	 * @param ID
	 * @param fromDate
	 * @param toDate
	 * @param totalHours
	 * @param employeeCPR
	 */
	public WorkSchedule(int ID, LocalDate fromDate, LocalDate toDate, int totalHours, String employeeCPR) {
		this.ID = ID;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.totalHours = totalHours;
		this.employeeCPR = employeeCPR;
	}
	
	/**
	 * Constructor to initialize instance variables.
	 * @param ID
	 * @param totalHours
	 * @param employeeCPR
	 */
	public WorkSchedule(int ID, int totalHours, String employeeCPR) {
		this.ID = ID;
		this.totalHours = totalHours;
		this.employeeCPR = employeeCPR;
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

	/**
	 * Gets from date.
	 * @return fromDate
	 */
	public LocalDate getFromDate() {
		return fromDate;
	}

	/**
	 * Sets a new from date.
	 * @param fromDate
	 */
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * Gets to date.
	 * @return toDate
	 */
	public LocalDate getToDate() {
		return toDate;
	}

	/**
	 * Sets a new to date.
	 * @param toDate
	 */
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	/**
	 * Gets total hours.
	 * @return totalHours
	 */
	public Integer getTotalHours() {
		return totalHours;
	}

	/**
	 * Sets a new total hours.
	 * @param totalHours
	 */
	public void setTotalHours(int totalHours) {
		this.totalHours = totalHours;
	}

	/**
	 * Gets employee CPR.
	 * @return employeeCPR
	 */
	public String getEmployeeCPR() {
		return employeeCPR;
	}

	/**
	 * Sets a new employee CPR.
	 * @param employeeCPR
	 */
	public void setEmployeeCPR(String employeeCPR) {
		this.employeeCPR = employeeCPR;
	}

}

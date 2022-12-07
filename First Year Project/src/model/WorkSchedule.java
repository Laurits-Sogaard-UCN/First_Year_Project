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
	 * @param totalHours
	 * @param employeeCPR
	 */
	public WorkSchedule(int ID, int totalHours, String employeeCPR) {
		this.ID = ID;
		this.totalHours = totalHours;
		this.employeeCPR = employeeCPR;
	}

	/**
	 * Constructor to initialize instance variables.
	 * @param id
	 */
	public WorkSchedule(int id) {
		this.ID = id;
	}

	/**
	 * Gets ID.
	 * @return ID
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * Sets new ID.
	 * @param ID
	 */
	public void setID(int ID) {
		this.ID = ID;
	}

	/**
	 * Gets total hours.
	 * @return totalHours
	 */
	public Integer getTotalHours() {
		return totalHours;
	}

	/**
	 * Gets employee CPR.
	 * @return employeeCPR
	 */
	public String getEmployeeCPR() {
		return employeeCPR;
	}

	/**
	 * Sets new employee CPR.
	 * @param employeeCPR
	 */
	public void setEmployeeCPR(String employeeCPR) {
		this.employeeCPR = employeeCPR;
	}

}

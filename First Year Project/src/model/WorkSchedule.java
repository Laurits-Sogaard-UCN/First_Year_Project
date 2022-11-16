package model;

import java.time.LocalDate;

public class WorkSchedule implements Comparable<WorkSchedule> {
	
	private int ID;
	private LocalDate fromDate;
	private LocalDate toDate;
	private int totalHours;
	private String employeeCPR;
	
	/**
	 * @param iD
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
	
	public WorkSchedule(int ID, int totalHours, String employeeCPR) {
		this.ID = ID;
		this.totalHours = totalHours;
		this.employeeCPR = employeeCPR;
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

	/**
	 * @return the fromDate
	 */
	public LocalDate getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the toDate
	 */
	public LocalDate getToDate() {
		return toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the totalHours
	 */
	public int getTotalHours() {
		return totalHours;
	}

	/**
	 * @param totalHours the totalHours to set
	 */
	public void setTotalHours(int totalHours) {
		this.totalHours = totalHours;
	}

	/**
	 * @return the employeeCPR
	 */
	public String getEmployeeCPR() {
		return employeeCPR;
	}

	/**
	 * @param employeeCPR the employeeCPR to set
	 */
	public void setEmployeeCPR(String employeeCPR) {
		this.employeeCPR = employeeCPR;
	}

	@Override
	public int compareTo(WorkSchedule workSchedule) {
		if(totalHours == workSchedule.getTotalHours()) {
			return 0;
		}
		else if(totalHours > workSchedule.getTotalHours()) {
			return 1;
		}
		else {
			return -1;
		}
	}

}

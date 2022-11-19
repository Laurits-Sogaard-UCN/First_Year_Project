package database;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import utility.EmployeeType;
import model.WorkSchedule;
import utility.DBMessages;
import utility.DataAccessException;

public class WorkScheduleDB implements WorkScheduleDBIF {

	private static final String FIND_WORK_SCHEDULE_ID_ON_CPR = ("SELECT ws.ID\r\n"
			+ "FROM WorkSchedule ws\r\n"
			+ "WHERE ws.EmployeeCPR = ?");
	private PreparedStatement findWorkScheduleIDOnCPR;
	
	private static final String INSERT_NEW_WORK_SCHEDULE = ("INSERT INTO WorkSchedule(EmployeeCPR) VALUES(?)");
	private PreparedStatement insertNewWorkSchedule;
	
	private static final String GET_TOTAL_HOURS = ("SELECT ws.TotalHours\r\n"
			+ "FROM WorkSchedule ws\r\n"
			+ "WHERE ws.EmployeeCPR = ?");
	private PreparedStatement getTotalHours;
	
	private static final String SET_TOTAL_HOURS = ("UPDATE WorkSchedule\r\n"
			+ "SET TotalHours = ?\r\n"
			+ "WHERE EmployeeCPR = ?");
	private PreparedStatement setTotalHours;
	
	private static final String GET_ALL_WORK_SCHEDULES = ("SELECT ws.*\r\n"
			+ "FROM WorkSchedule ws, Employee e\r\n"
			+ "WHERE ws.EmployeeCPR = e.CPR\r\n"
			+ "and e.EmployeeType = ?");
	private PreparedStatement getAllWorkSchedules;
	
	/**
	 * Constructor to initialize instance variables.
	 * @throws DataAccessException
	 */
	public WorkScheduleDB() throws DataAccessException {
		init();
	}
	
	/**
	 * Initialization of Connection and PreparedStatments.
	 * @throws DataAccessException
	 */
	private void init() throws DataAccessException {
		Connection con = DBConnection.getInstance().getConnection();
		
		try {
			findWorkScheduleIDOnCPR = con.prepareStatement(FIND_WORK_SCHEDULE_ID_ON_CPR, PreparedStatement.RETURN_GENERATED_KEYS);
			insertNewWorkSchedule = con.prepareStatement(INSERT_NEW_WORK_SCHEDULE);
			getTotalHours = con.prepareStatement(GET_TOTAL_HOURS);
			setTotalHours = con.prepareStatement(SET_TOTAL_HOURS);
			getAllWorkSchedules = con.prepareStatement(GET_ALL_WORK_SCHEDULES);
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_PREPARE_STATEMENT, e);
		}
	}
	
	public int findWorkScheduleIDOnEmployeeCPR(String CPR) throws DataAccessException {
		int workScheduleID = 0;
		ResultSet rs;
		
		try {
			findWorkScheduleIDOnCPR.setString(1, CPR);
			rs = findWorkScheduleIDOnCPR.executeQuery();
			if(rs.next()) {
				workScheduleID = rs.getInt("ID");
			}
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return workScheduleID;
	}
	
	public boolean setTotalHoursOnWorkSchedule(int hours, String employeeCPR) throws DataAccessException {
		boolean set = false;
		int totalHours;
		
		try {
			DBConnection.getInstance().startTransaction(); // TODO skal der ikke defineres isolationsniveau her?
			totalHours = getTotalHours(employeeCPR);
			setTotalHours.setInt(1, totalHours + hours);
			setTotalHours.setString(2, employeeCPR);
			setTotalHours.executeUpdate();
			DBConnection.getInstance().commitTransaction();
			set = true;
			
		} catch(SQLException e) {
			DBConnection.getInstance().rollbackTransaction();
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return set;
	}
	
	private int getTotalHours(String employeeCPR) throws DataAccessException {
		int totalHours = 0;
		ResultSet rs;
		
		try {
			getTotalHours.setString(1, employeeCPR);
			rs = getTotalHours.executeQuery();
			if(rs.next()) {
				totalHours = rs.getInt("TotalHours");
			}
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return totalHours;
	}
	
	public ArrayList<WorkSchedule> getAllWorkSchedules() throws DataAccessException {
		ArrayList<WorkSchedule> workSchedules = new ArrayList<>();
		ResultSet rs;
		
		try {
			getAllWorkSchedules.setString(1, EmployeeType.PARTTIME.getType());
			rs = getAllWorkSchedules.executeQuery();
			workSchedules = buildWorkScheduleObjects(rs);
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return workSchedules;
	}
	
	private ArrayList<WorkSchedule> buildWorkScheduleObjects(ResultSet rs) throws DataAccessException {
		ArrayList<WorkSchedule> workSchedules = new ArrayList<>();
		WorkSchedule workSchedule;
		
		try {
			while(rs.next()) {
				workSchedule = buildWorkScheduleObject(rs);
				workSchedules.add(workSchedule);
			} 
			
		} catch (SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_READ_RESULTSET, e);
		}
		return workSchedules;

	}

	private WorkSchedule buildWorkScheduleObject(ResultSet rs) throws DataAccessException {
		WorkSchedule workSchedule;
		int id;
		int totalHours;
		String employeeCPR;
		
		try {
			id = rs.getInt("ID");
			totalHours = rs.getInt("TotalHours");
			employeeCPR = rs.getString("EmployeeCPR");
			workSchedule = new WorkSchedule(id, totalHours, employeeCPR);
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return workSchedule;
	}
	
	
	
	
}

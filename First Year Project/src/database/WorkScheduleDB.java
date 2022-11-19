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
	
	private static final String GET_CURRENT_HOURS = ("SELECT ws.TotalHours\r\n"
			+ "FROM WorkSchedule ws\r\n"
			+ "WHERE ws.EmployeeCPR = ?");
	private PreparedStatement getCurrentHours;
	
	private static final String SET_TOTAL_HOURS = ("UPDATE WorkSchedule\r\n"
			+ "SET TotalHours = ?\r\n"
			+ "WHERE EmployeeCPR = ?");
	private PreparedStatement setTotalHours;
	
	private static final String GET_ALL_WORK_SCHEDULES = ("SELECT ws.*\r\n"
			+ "FROM WorkSchedule ws, Employee e\r\n"
			+ "WHERE ws.EmployeeCPR = e.CPR\r\n"
			+ "and e.EmployeeType = ?");
	private PreparedStatement getAllWorkSchedules;
	
	private Connection con;
	
	/**
	 * Constructor to initialize instance variables.
	 * @throws DataAccessException
	 * @throws SQLException 
	 */
	public WorkScheduleDB() throws DataAccessException {
		init();
	}
	
	/**
	 * Initialization of Connection and PreparedStatments.
	 * @throws DataAccessException
	 * @throws SQLException 
	 */
	private void init() throws DataAccessException {
		con = DBConnection.getInstance().getConnection();
		
		try {
			findWorkScheduleIDOnCPR = con.prepareStatement(FIND_WORK_SCHEDULE_ID_ON_CPR, PreparedStatement.RETURN_GENERATED_KEYS);
			getCurrentHours = con.prepareStatement(GET_CURRENT_HOURS);
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
		int currentHours;
		
		try {
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			DBConnection.getInstance().startTransaction(); 
			currentHours = getCurrentHours(employeeCPR);
			setTotalHours(currentHours, hours, employeeCPR);
			DBConnection.getInstance().commitTransaction();
			set = true;
			
		} catch(Exception e) {
			DBConnection.getInstance().rollbackTransaction();
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return set;
	}
	
	private void setTotalHours(int currentHours, int hours, String employeeCPR) throws DataAccessException {
		try {
			setTotalHours.setInt(1, currentHours + hours);
			setTotalHours.setString(2, employeeCPR);
			setTotalHours.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_INSERT, e);
		}
	}
	
	private int getCurrentHours(String employeeCPR) throws DataAccessException {
		int currentHours = 0;
		ResultSet rs;
		
		try {
			getCurrentHours.setString(1, employeeCPR);
			rs = getCurrentHours.executeQuery();
			if(rs.next()) {
				currentHours = rs.getInt("TotalHours");
			}
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return currentHours;
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

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
import utility.DatabaseType;

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
	
	private static final String GET_ALL_PART_TIME_WORK_SCHEDULES = ("SELECT ws.*\r\n"
			+ "FROM WorkSchedule ws, Employee e\r\n"
			+ "WHERE ws.EmployeeCPR = e.CPR\r\n"
			+ "and e.EmployeeType = ?");
	private PreparedStatement getAllPartTimeWorkSchedules;
	
	private DBConnection dbConnection;
	private Connection con;
	
	/**
	 * Constructor to initialize instance variables.
	 * @throws DataAccessException
	 */
	public WorkScheduleDB(DatabaseType database) throws DataAccessException {
		init(database);
	}
	
	/**
	 * Initialization of Connection and PreparedStatements.
	 * @throws DataAccessException
	 */
	private void init(DatabaseType database) throws DataAccessException {
		dbConnection = ConnectionFactory.createDatabase(database);
		con = dbConnection.getConnection();
		
		try {
			findWorkScheduleIDOnCPR = con.prepareStatement(FIND_WORK_SCHEDULE_ID_ON_CPR, PreparedStatement.RETURN_GENERATED_KEYS);
			getCurrentHours = con.prepareStatement(GET_CURRENT_HOURS);
			setTotalHours = con.prepareStatement(SET_TOTAL_HOURS);
			getAllPartTimeWorkSchedules = con.prepareStatement(GET_ALL_PART_TIME_WORK_SCHEDULES);
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_PREPARE_STATEMENT, e);
		}
	}
	
	/**
	 * Finds a work schedule ID on a given employee CPR by executing a query.
	 * @param CPR
	 * @return workScheduleID
	 * @throws DataAccessException
	 */
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
	
	/**
	 * Set new total hours on work schedule. 
	 * @param hours
	 * @param employeeCPR
	 * @return set
	 * @throws DataAccessException
	 */
	public boolean setTotalHoursOnWorkSchedule(int hours, String employeeCPR) throws DataAccessException {
		boolean set = false;
		int currentHours;
		
		try {
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ); 	// Sets isolation level on transaction.
			dbConnection.startTransaction(); 
			currentHours = getCurrentHours(employeeCPR);
			setTotalHours(currentHours, hours, employeeCPR);
			dbConnection.commitTransaction();
			set = true;
			
		} catch(Exception e) {
			dbConnection.rollbackTransaction();
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return set;
	}
	
	/**
	 * Sets total hours on a work schedule by executing update. 
	 * @param currentHours
	 * @param hours
	 * @param employeeCPR
	 * @throws DataAccessException
	 */
	private void setTotalHours(int currentHours, int hours, String employeeCPR) throws DataAccessException {
		try {
			setTotalHours.setInt(1, currentHours + hours);
			setTotalHours.setString(2, employeeCPR);
			setTotalHours.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_INSERT, e);
		}
	}
	
	/**
	 * Gets the current total hours on a work schedule with a given employeeCPR by executing query.
	 * @param employeeCPR
	 * @return currentHours
	 * @throws DataAccessException
	 */
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
	
	/**
	 * Gets all work schedules marked as 'PartTime'.
	 * @return workSchedules
	 * @throws DataAccessException
	 */
	public ArrayList<WorkSchedule> getAllPartTimeWorkSchedules() throws DataAccessException {
		ArrayList<WorkSchedule> workSchedules = new ArrayList<>();
		ResultSet rs;
		
		try {
			getAllPartTimeWorkSchedules.setString(1, EmployeeType.PARTTIME.getType());
			rs = getAllPartTimeWorkSchedules.executeQuery();
			workSchedules = buildWorkScheduleObjects(rs);
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return workSchedules;
	}
	
	/**
	 * Builds a collection containing one or more work schedules.
	 * @param rs
	 * @return workSchedules
	 * @throws DataAccessException
	 */
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

	/**
	 * Builds a work schedule object from a ResultSet.
	 * @param rs
	 * @return workSchedule
	 * @throws DataAccessException
	 */
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

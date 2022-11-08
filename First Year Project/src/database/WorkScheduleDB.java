package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import utility.DBMessages;
import utility.DataAccessException;

public class WorkScheduleDB implements WorkScheduleDBIF {

	private static final String FIND_WORK_SCHEDULE_ID_ON_CPR = ("SELECT ws.ID\r\n"
			+ "FROM WorkSchedule ws\r\n"
			+ "WHERE ws.EmployeeCPR = ?");
	private PreparedStatement findWorkScheduleIDOnCPR;
	
	private static final String INSERT_NEW_WORK_SCHEDULE = ("INSERT INTO WorkSchedule(EmployeeCPR) VALUES(?)");
	private PreparedStatement insertNewWorkSchedule;
	
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
			if(workScheduleID == 0) {
				insertNewWorkSchedule(CPR);
				ResultSet rs1 = insertNewWorkSchedule.getGeneratedKeys();
				if(rs1.next()) {
					workScheduleID = rs1.getInt(1);
				}
			}
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return workScheduleID;
	}
	
	private boolean insertNewWorkSchedule(String CPR) throws DataAccessException {
		boolean inserted = false;
		int rowsAffected = -1;
		try {
			insertNewWorkSchedule.setString(1, CPR);
			rowsAffected = insertNewWorkSchedule.executeUpdate();
			if(rowsAffected == 0) {
				inserted = true;
			}
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_INSERT, e);
		}
		return inserted;
	}
}

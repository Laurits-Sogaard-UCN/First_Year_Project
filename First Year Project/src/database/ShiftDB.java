package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Employee;
import model.PartTimeEmployee;
import model.Shift;
import model.Shop;
import model.WorkShift;
import utility.DataAccessException;

public class ShiftDB implements ShiftDBIF {
	
	private static final String FIND_SHIFT_ON_FROM_AND_TO = ("");
	private PreparedStatement findShiftOnFromAndTo;
	
	/**
	 * Constructor to initialize instance variables.
	 * @throws DataAccessException
	 */
	public ShiftDB() throws DataAccessException {
		init();
	}
	
	/**
	 * Initialization of Connection and PreparedStatments.
	 * @throws DataAccessException
	 */
	private void init() throws DataAccessException {
		Connection con = DBConnection.getInstance().getConnection();
		try {
			findShiftOnFromAndTo = con.prepareStatement(FIND_SHIFT_ON_FROM_AND_TO);
		} catch(SQLException e) {
			throw new DataAccessException("Could not prepare statement", e);
		}
	}
	
	public Shift findShiftOnFromAndTo(int fromHour, int toHour) throws DataAccessException {
		ResultSet rs;
		Shift shift = null;
		try {
			findShiftOnFromAndTo.setInt(1, fromHour);
			findShiftOnFromAndTo.setInt(2, toHour);
			rs = findShiftOnFromAndTo.executeQuery();
			if(rs.next()) {
				shift = buildWorkShiftObject(rs);
			}
		} catch(SQLException e) {
			throw new DataAccessException("No resultset", e);
		}
		return shift;
	}
	
	private Shift buildShiftObject(ResultSet rs) throws DataAccessException {
		Shift shift;
		try {
			int fromHour = rs.getInt("FromHour");
			int toHour = rs.getInt("ToHour");
			String shiftType = rs.getString("ShiftType");
			shift = new Shift(fromHour, toHour, shiftType);
		} catch(SQLException e) {
			throw new DataAccessException("Could not build object", e);
		}
		return shift;
	}
	
	private Shift buildWorkShiftObject(ResultSet rs) throws DataAccessException {
		WorkShift workShift = (WorkShift) buildShiftObject(rs);
		try {
			String state = rs.getString("State");
			workShift.setState(state);
		} catch(SQLException e) {
			throw new DataAccessException("Could not build object", e);
		}
		return workShift;
	}

}

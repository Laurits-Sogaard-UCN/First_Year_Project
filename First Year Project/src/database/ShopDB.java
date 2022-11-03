package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Employee;
import model.Shop;
import utility.DBMessages;
import utility.DataAccessException;

public class ShopDB implements ShopDBIF {
	
	private static final String FIND_SHOP_ON_ID = ("SELECT *\r\n"
			+ "FROM Shop s\r\n"
			+ "WHERE s.ID = ?");
	private PreparedStatement findShopOnID;
	
	/**
	 * Constructor to initialize instance variables.
	 * @throws DataAccessException
	 */
	public ShopDB() throws DataAccessException {
		init();
	}
	
	/**
	 * Initialization of Connection and PreparedStatments.
	 * @throws DataAccessException
	 */
	private void init() throws DataAccessException {
		Connection con = DBConnection.getInstance().getConnection();
		try {
			findShopOnID = con.prepareStatement(FIND_SHOP_ON_ID);
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_PREPARE_STATEMENT, e);
		}
	}
	
	public Shop findShopOnID(int id) throws DataAccessException {
		ResultSet rs;
		Shop shop = null;
		try {
			findShopOnID.setInt(1, id);
			rs = findShopOnID.executeQuery();
			if(rs.next()) {
				shop = buildShopObject(rs);
			}
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_BIND_OR_EXECUTE_QUERY, e);
		}
		return shop;
	}
	
	private Shop buildShopObject(ResultSet rs) throws DataAccessException {
		Shop shop;
		try {
			String name = rs.getString("Fname");
			String address = rs.getString(rs.getString("Street") + " " + rs.getString("StreetNumber"));
			int id = rs.getInt("ID");
			shop = new Shop(address, name, id);
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_READ_RESULTSET, e);
		}
		return shop;
	}
	

}

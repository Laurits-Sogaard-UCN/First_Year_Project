package database;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Shop;
import utility.DBMessages;
import utility.DataAccessException;

public class ShopDB implements ShopDBIF {
	
	private static final String FIND_SHOP_ON_ID = ("SELECT s.*, a.*, ac.City, ac.Country\r\n"
			+ "FROM Shop s, Address a, AddressCity ac\r\n"
			+ "WHERE s.ID = ?\r\n"
			+ "AND s.AddressID = a.ID\r\n"
			+ "AND a.Zipcode = ac.Zipcode");
	private PreparedStatement findShopOnID;
	
	/**
	 * Constructor to initialize instance variables.
	 * @throws DataAccessException
	 */
	public ShopDB() throws DataAccessException {
		init();
	}
	
	/**
	 * Initialization of Connection and PreparedStatements.
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
	
	/**
	 * Finds shop on ID by executing query.
	 * @param id
	 * @return shop
	 */
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
	
	/**
	 * Builds shop object from ResultSet.
	 * @param rs
	 * @return shop
	 * @throws DataAccessException
	 */
	private Shop buildShopObject(ResultSet rs) throws DataAccessException {
		Shop shop;
		String address;
		int zipcode;
		String city;
		String country;
		String name;
		int id;
		
		try {
			address = rs.getString("Street") + " " + rs.getString("StreetNumber");
			zipcode = rs.getInt("Zipcode");
			city = rs.getString("City");
			country = rs.getString("Country");
			name = rs.getString("Name");
			id = rs.getInt("ID");
			shop = new Shop(address, zipcode, city, country, name, id);
			
		} catch(SQLException e) {
			throw new DataAccessException(DBMessages.COULD_NOT_READ_RESULTSET, e);
		}
		return shop;
	}
	

}

package controller;

import database.ShopDB;
import database.ShopDBIF;
import model.Shop;
import utility.DataAccessException;

public class ShopController {
	
	private ShopDBIF shopDB;
	
	/**
	 * Constructor to initialize instance variables. 
	 * @throws DataAccessException
	 */
	public ShopController() throws DataAccessException {
		shopDB = new ShopDB();
	}
	
	/**
	 * Finds a shop object on ID.
	 * @param id
	 * @return shop
	 * @throws DataAccessException
	 */
	public Shop findShopOnID(int id) throws DataAccessException {
		Shop shop = shopDB.findShopOnID(id);
		return shop;
	}

}

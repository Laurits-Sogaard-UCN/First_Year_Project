package controller;

import database.ShiftDBIF;
import database.ShopDB;
import database.ShopDBIF;
import model.Shop;
import utility.DataAccessException;

public class ShopController {
	
	private ShopDBIF shopDB;
	
	public ShopController() throws DataAccessException {
		shopDB = new ShopDB();
	}
	
	public Shop findShopOnID(int id) throws DataAccessException {
		Shop shop = shopDB.findShopOnID(id);
		return shop;
	}

}

package database;

import model.Shop;
import utility.DataAccessException;

public interface ShopDBIF {
	
	public Shop findShopOnID(int id) throws DataAccessException;

}

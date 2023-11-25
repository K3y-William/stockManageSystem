package dao;

import entity.Stock;
import java.util.List;

public interface userStockDao {

    //return all the user's stock data, include stock_id, stock_num, and adding date
    List<Stock> selectAll(int user_id);

    //return single stock data
    Stock selectOne(int user_id, int stock_id);

    //insert single stock data
    int insert(int user_id, Stock stock);

    //update single stock data
    int update(int user_id, Stock stock);

    //delete one of stock
    int delete(int userid, int stock_id);

    int updateTransaction(int user_id, Stock stock);

    boolean stockExist(int user_id, int stock_id);


}

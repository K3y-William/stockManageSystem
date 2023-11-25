package dao;

import entity.Stock;
import java.util.List;

public interface priceControlDao {

    //return all the stock related info in this stock market
    List<Stock> selectAll();

    //return single stock data
    Stock selectOne(int stock_id);

    double getMarketPrice(int stock_id);

    //insert single stock data
    int insert(Stock stock);

    //update single stock data
    int update(Stock stock);

    //delete one of stock
    int delete(int stock_id);
}

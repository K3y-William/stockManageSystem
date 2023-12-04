package dao.impl;

import dao.userStockDao;
import entity.Stock;
import util.DButil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static util.DButil.exeUpdate;

public class userStockDaoImp implements userStockDao {

    //input id is user_id
    @Override
    public List<Stock> selectAll(int id) {
        Connection conn = DButil.getConnection();
        List<Stock> stock_list = new ArrayList<>();
        PreparedStatement pstm = null;
        ResultSet rs = null;

        //query all the stock info based on user_id from property
        //then use stock_id query current_price from stock_price
        String sql =
                "select rs.stock_id,rs.stock_num,stock_price,rs.buying_date from " +
                    "(select property.stock_id,stock_num,buying_date " +
                    "from property where user_id = ?) as rs " +
                 "left outer join stock_price on rs.stock_id = stock_price.stock_id";

        try {
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1,id);
            rs = pstm.executeQuery();
            while (rs.next()){
                //extract data from rs
                int s_id = rs.getInt(1);
                int num = rs.getInt(2);
                double price = rs.getDouble(3);
                Timestamp date = rs.getTimestamp(4);

                //put the data into stock object
                Stock stock = new Stock(s_id,price,num,date);

                //add all the stock into stock_list
                stock_list.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DButil.closeAll(pstm,conn,rs);
        }
        return stock_list;
    }

    @Override
    public Stock selectOne(int user_id, int stock_id) {
        Stock stock = new Stock();
        Connection conn = DButil.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;

        //query all the stock info based on user_id from property,
        //then based on stock_id query the target stock
        //finally use stock_id query current_price from stock_price,
        String sql =
                "select stock.stock_id,stock_num,current_price,buying_date from " +
                        "(select stock_id,stock_num,buying_date " +
                            "from property where user_id = " + user_id + " AND stock_id = " + stock_id + ") as stock " +
                "left outer join stock_price on stock.stock_id = stock_price.stock_id";

        try {
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()){
                //extract data from rs
                int s_id = rs.getInt(1);
                int num = rs.getInt(2);
                double price = rs.getDouble(3);
                Timestamp date = rs.getTimestamp(4);

                //put the data into stock object
                stock.stockSetValue(s_id,price,num,date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DButil.closeAll(pstm,conn,rs);
        }
        return stock;
    }

    //insert one of stock with its owner user_id into property
    //when user buy one of the stock that he already owned, we need to instead call insert, but update
    @Override
    public int insert(int user_id, Stock stock) {
        Object obj[] = {user_id, stock.getStock_id(), stock.getStock_num(), stock.getDate()};
        String sql = "insert into property values (?,?,?,?)";
        return exeUpdate(sql,obj);
    }

    //update original stock data in property by the input stock
    //create a stock object and call this update function
    @Override
    public int update(int user_id, Stock stock) {
        Object obj[] = {stock.getStock_num(),stock.getDate(), user_id,stock.getStock_id()};
        String sql = "update property set stock_num = ?, buying_date = ? where user_id = ? and stock_id = ?";
        return exeUpdate(sql,obj);
    }

    @Override
    public int delete(int user_id, int stock_id) {
        Object obj[] = {user_id,stock_id};
        String sql = "delete from property where user_id = ? and stock_id = ?";
        return exeUpdate(sql,obj);
    }


    public boolean stockExist(int user_id, int stock_id){
        Connection conn = DButil.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;

        String sql = "select stock_id from property where user_id = ? and stock_id = ?";

        try {
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1,user_id);
            pstm.setInt(2,stock_id);
            rs = pstm.executeQuery();
           if (rs.next()){
                return true;
           }
           else {
               return false;
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DButil.closeAll(pstm,conn,rs);
        }
        return false;
    }

    //if the new stock amount is negative represent selling, otherwise buying
    //these amounts are changing amounts
    //if buying stock update buying date, otherwise do not modify changing date
    @Override
    public int updateTransaction(int user_id, Stock stock) {
        String sql;
        Object [] obj;

        //buying
        if (stock.getStock_num() >= 0) {
            obj = new Object[] {user_id,stock.getStock_id(),stock.getStock_num(),stock.getDate()};
            sql = "INSERT INTO property (user_id, stock_id, stock_num, buying_date)" +
                    "VALUES (?, ?, ?, ?)" +
                    "ON DUPLICATE KEY UPDATE" +
                    "    stock_num = stock_num + VALUES(stock_num)," +
                    "    buying_date = VALUES(buying_date)";
        }

        //selling
        else {
            obj = new Object[]{user_id,stock.getStock_id(),stock.getStock_num()};
            sql = "INSERT INTO property (user_id, stock_id, stock_num)" +
                    "VALUES (?, ?, ?)" +
                    "ON DUPLICATE KEY UPDATE" +
                    "    stock_num = stock_num + VALUES(stock_num)";
        }
        return exeUpdate(sql,obj);

    }
}

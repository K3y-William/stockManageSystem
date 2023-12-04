package dao.impl;

import dao.priceControlDao;
import entity.Stock;
import util.DButil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static util.DButil.exeUpdate;

public class priceControlDaoImp implements priceControlDao {
    @Override
    public List<Stock> selectAll() {
        Connection conn = DButil.getConnection();
        List<Stock> stock_list = new ArrayList<>();
        PreparedStatement pstm = null;
        ResultSet rs = null;

        //query all the stock info based on user_id from property
        //then use stock_id query current_price from stock_price
        String sql = "select * from stock_price";

        try {
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()){
                //extract data from rs
                int s_id = rs.getInt(1);
                double price = rs.getDouble(2);
                int num = rs.getInt(3);
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


    //get the target stock's price and changing date
    @Override
    public Stock selectOne(int stock_id) {
        Connection conn = DButil.getConnection();
        Stock stock = new Stock();
        PreparedStatement pstm = null;
        ResultSet rs = null;

        //query all the stock info based on user_id from property
        //then use stock_id query current_price from stock_price
        String sql = "select * from stock_price where stock_id = " + stock_id;

        try {
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()){
                //extract data from rs
                int s_id = rs.getInt(1);
                double price = rs.getDouble(2);
                int num = rs.getInt(3);
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



    //get target stock's market price, return -1 if invalid
    @Override
    public double getMarketPrice(int stock_id) {
        double price = -1;
        Connection conn = DButil.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;

        String sql =
                "select stock_price from stock_price whre stock_id = ?";

        try {
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1,stock_id);
            rs = pstm.executeQuery();
            while (rs.next()){
                //extract data from rs
                price = rs.getDouble("stock_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DButil.closeAll(pstm,conn,rs);
        }
        return price;
    }

    @Override
    public int insert(Stock stock) {
        Object obj[] = {stock.getStock_id(), stock.getStock_price(), stock.getStock_num(), stock.getDate()};
        String sql = "insert into stock_price values (?,?,?,?)";
        return exeUpdate(sql,obj);
    }

    @Override
    public int update(Stock stock) {
        Object obj[] = {stock.getStock_price(),stock.getStock_num(),stock.getDate(),stock.getStock_id()};
        String sql = "update stock_price set stock_price = ?, stock_num = ?, changing_date = ? where stock_id = ?";
        return exeUpdate(sql,obj);
    }

    @Override
    public int delete(int stock_id) {
        Object obj[] = {stock_id};
        String sql = "delete from stock_price where stock_id = ?";
        return exeUpdate(sql,obj);
    }

    @Override
    public int updatePrice(int stock_id, double price) {
        Object obj[] = {price,stock_id};
        String sql = "update stock_price set stock_price = ? where stock_id = ?";
        return exeUpdate(sql,obj);
    }
}

package dao.impl;

import dao.transactionDao;
import entity.Stock;
import entity.Transaction;
import entity.User;
import util.DButil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static util.DButil.exeUpdate;
import static util.DButil.getConnection;

public class buyingDaoImp implements transactionDao {
    @Override
    public List<Transaction> selectAll() {
        Connection conn = DButil.getConnection();
        List<Transaction> transactions_list = new ArrayList<>();
        PreparedStatement pstm = null;
        ResultSet rs = null;

        String sql = "select * from buying_platform";

        try {
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()){
                //extract data from rs
                int user_id = rs.getInt(1);
                int stock_id = rs.getInt(2);
                int request_amount = rs.getInt(3);
                double request_price = rs.getDouble(4);
                Timestamp date = rs.getTimestamp(5);
                String status = rs.getString(6);

                Transaction transaction = new Transaction(user_id,stock_id,request_amount,request_price,date,status);

                transactions_list.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DButil.closeAll(pstm,conn,rs);
        }
        return transactions_list;
    }


    //find all unprocessed and processing transactions related to input stock_id, sorted from high price to low
    //and order date form oldest to newest
    public static List<Transaction> getBuyingOrder(int stock_id){
        Connection conn = DButil.getConnection();
        List<Transaction> transactions_list = new ArrayList<>();
        PreparedStatement pstm = null;
        ResultSet rs = null;

        String sql = "select * from buying_platform " +
                "where stock_id = ?  and status != 'completed' " +
                "order by request_price DESC, request_date ASC ";

        try {
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1,stock_id);
            rs = pstm.executeQuery();
            while (rs.next()){
                //extract data from rs
                int user_id = rs.getInt(1);
                int s_id = rs.getInt(2);
                int request_amount = rs.getInt(3);
                double request_price = rs.getDouble(4);
                Timestamp date = rs.getTimestamp(5);
                String status = rs.getString(6);

                Transaction transaction = new Transaction(user_id,s_id,request_amount,request_price,date,status);

                transactions_list.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DButil.closeAll(pstm,conn,rs);
        }
        return transactions_list;
    }


    //find all the transaction related to input stock_id
    @Override
    public List<Transaction> checkStock(int stock_id) {
        Connection conn = DButil.getConnection();
        List<Transaction> transactions_list = new ArrayList<>();
        PreparedStatement pstm = null;
        ResultSet rs = null;

        String sql = "select * from buying_platform where stock_id = ?";

        try {
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1,stock_id);
            rs = pstm.executeQuery();
            while (rs.next()){
                //extract data from rs
                int user_id = rs.getInt(1);
                int s_id = rs.getInt(2);
                int request_amount = rs.getInt(3);
                double request_price = rs.getDouble(4);
                Timestamp date = rs.getTimestamp(5);
                String status = rs.getString(6);

                Transaction transaction = new Transaction(user_id,s_id,request_amount,request_price,date,status);

                transactions_list.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DButil.closeAll(pstm,conn,rs);
        }
        return transactions_list;
    }


    //find all the transaction related to input user_id
    @Override
    public List<Transaction> checkUser(int user_id) {
        Connection conn = DButil.getConnection();
        List<Transaction> transactions_list = new ArrayList<>();
        PreparedStatement pstm = null;
        ResultSet rs = null;

        String sql = "select * from buying_platform where user_id = ?";

        try {
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1,user_id);
            rs = pstm.executeQuery();
            while (rs.next()){
                //extract data from rs
                int u_id = rs.getInt(1);
                int stock_id = rs.getInt(2);
                int request_amount = rs.getInt(3);
                double request_price = rs.getDouble(4);
                Timestamp date = rs.getTimestamp(5);
                String status = rs.getString(6);

                Transaction transaction = new Transaction(u_id,stock_id,request_amount,request_price,date,status);

                transactions_list.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DButil.closeAll(pstm,conn,rs);
        }
        return transactions_list;
    }


    //delete the target transaction from database
    @Override
    public int cancelTransaction(int user_id, int stock_id) {
        Object obj[] = {user_id, stock_id};
        String sql = "delete from buying_platform where user_id = ?, stock_id = ?";
        return exeUpdate(sql,obj);
    }

    public boolean existTransaction(int user_id, int stock_id) throws SQLException{
        Connection conn = getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "select user_id from buying_platform where user_id = ? AND stock_id = ?";
        try {
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1,user_id);
            pstm.setInt(2,stock_id);
            rs = pstm.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (rs.next()){
            rs.close();
            pstm.close();
            conn.close();
            return true;
        }
        else{
            rs.close();
            pstm.close();
            conn.close();
            return false;
        }
    }

    //if there is no exist transaction, add new one into buying_platform
    //if already exist, update accordingly to the input transaction
    @Override
    public int updateTransaction(Transaction transaction) throws SQLException {
        //if exist such transaction
        if (existTransaction(transaction.getUser_id(),transaction.getStock_id())){
            Object obj[] = {transaction.getAmounts(),transaction.getPrice(), transaction.getStatus(), transaction.getUser_id(),transaction.getStock_id()};
            String sql = "update buying_platform set request_amount = ?, request_price = ?, status = ? where user_id = ? and stock_id = ?";
            return exeUpdate(sql,obj);
        }

        //if not insert one with status unprocessed
        else{
            Object obj[] = {transaction.getUser_id(),transaction.getStock_id(),transaction.getAmounts(),transaction.getPrice(),transaction.getDate(),transaction.getStatus()};
            String sql = "insert into buying_platform values (?,?,?,?,?,?)";
            return exeUpdate(sql,obj);
        }

    }

//    public static int updateTransaction(int user_id, int stock_id, int newAmount, String status){
//        Object obj[] = {newAmount,status,user_id,stock_id};
//        String sql = "update buying_platform set request_amount = ?, status = ? " +
//                "where user_id = ? and stock_id = ?";
//        return exeUpdate(sql,obj);
//    }
}

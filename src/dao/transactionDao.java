package dao;

import entity.Stock;
import entity.Transaction;
import entity.User;

import java.sql.SQLException;
import java.util.List;

public interface transactionDao {

    //return all the transactions that is posted on the platform
    List<Transaction> selectAll();


    //return the specific stock on the platform
    List<Transaction> checkStock(int stock_id);


    //return all the posts that created by certain user
    List<Transaction> checkUser(int user_id);




    //cancel specific transaction based on the user_id and stock_id
    int cancelTransaction(int user_id, int stock_id);


    //update transactions
    int updateTransaction(Transaction transaction) throws SQLException;


}

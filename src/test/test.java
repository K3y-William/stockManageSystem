package test;

import dao.impl.priceControlDaoImp;
import dao.impl.sellingDaoImp;
import dao.priceControlDao;
import dao.transactionDao;
import entity.Stock;
import entity.Transaction;
import entity.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


public class test {
    //private  static   userStockDao  userStockDao=new userUserStockDaoImpl();
    private  static   priceControlDao priceControlDao = new priceControlDaoImp();
    private static transactionDao transactionDao = new sellingDaoImp();

    public static void main(String[] args) {
        List<Transaction> transactions_list = transactionDao.checkStock(1);
        System.out.println(transactions_list.get(0).getAmounts());

//        Timestamp ts=new Timestamp(new Date().getTime());
//        Stock stock = new Stock(1,900,300,ts);
//        User user = new User();
//        user.setUser_id(1);
//
//        try {
//            transactionDao.updateTransaction(user,stock);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        priceControlDao.insert(stock);
//        priceControlDao.selectOne(1);
        //System.out.println(userStockDao.delete(1,1));


    }
}

package test;

import dao.impl.*;
import dao.priceControlDao;
import dao.transactionDao;
import dao.userCapitalDao;
import dao.userStockDao;
import entity.Stock;
import entity.Transaction;
import entity.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


public class test {
    static final priceControlDao priceControlDao = new priceControlDaoImp();
    static final transactionDao buyingDao = new buyingDaoImp();
    static final transactionDao sellingDao = new sellingDaoImp();
    static final dao.userCapitalDao userCapitalDao = new userCapitalDaoImp();
    static final dao.userStockDao userStockDao = new userStockDaoImp();

    public static void main(String[] args) {
        //List<Transaction> transactions_list = transactionDao.checkStock(1);
        //System.out.println(transactions_list.get(0).getAmounts());
        System.out.println(userStockDao.selectAll(3).get(0).toString());
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

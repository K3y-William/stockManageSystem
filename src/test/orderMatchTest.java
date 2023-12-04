package test;

import dao.userStockDao;
import entity.Stock;
import entity.Transaction;
import dao.userCapitalDao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import dao.transactionDao;
import dao.impl.buyingDaoImp;
import dao.impl.sellingDaoImp;
import dao.impl.userStockDaoImp;
import dao.impl.userCapitalDaoImp;

public class orderMatchTest {
    private static transactionDao buyingDao = new buyingDaoImp();
    private static transactionDao sellingDao = new sellingDaoImp();
    private static userStockDao userStockDao = new userStockDaoImp();
    private static userCapitalDao userCapitalDao = new userCapitalDaoImp();
    public static void main(String[] args) throws SQLException {
        List<Transaction> unprocessedTransactions = new ArrayList<>();

        // Specify a common stock ID for all transactions
        int commonStockId = 201;
        //buying adding
        // Add five transactions with the same stock ID and the status "Unprocessed"
//        unprocessedTransactions.add(new Transaction(1, commonStockId, 8, 40.0, Timestamp.valueOf("2023-01-01 12:00:00"), "Unprocessed"));
//        unprocessedTransactions.add(new Transaction(2, commonStockId, 12, 35.0, Timestamp.valueOf("2023-01-02 15:30:00"), "Unprocessed"));
//        unprocessedTransactions.add(new Transaction(3, commonStockId, 5, 20.0, Timestamp.valueOf("2023-01-03 09:45:00"), "Unprocessed"));
//        unprocessedTransactions.add(new Transaction(4, commonStockId, 15, 60.0, Timestamp.valueOf("2023-01-04 18:20:00"), "Unprocessed"));
//        unprocessedTransactions.add(new Transaction(5, commonStockId, 10, 55.0, Timestamp.valueOf("2023-01-05 22:10:30"), "Unprocessed"));
//
//        for (Transaction transaction : unprocessedTransactions) {
//            buyingDao.updateTransaction(transaction);
//        }

        //selling adding
//        List<Transaction> unprocessedTransactions2 = new ArrayList<>();
//        unprocessedTransactions2.add(new Transaction(6, commonStockId, 1, 60.0, Timestamp.valueOf("2023-01-02 12:00:00"), "Unprocessed"));
//        unprocessedTransactions2.add(new Transaction(7, commonStockId, 20, 50.0, Timestamp.valueOf("2023-01-03 15:30:00"), "Unprocessed"));
//        unprocessedTransactions2.add(new Transaction(8, commonStockId, 5, 35.0, Timestamp.valueOf("2023-01-05 09:45:00"), "Unprocessed"));
//        unprocessedTransactions2.add(new Transaction(9, commonStockId, 5, 25.0, Timestamp.valueOf("2023-01-06 18:20:00"), "Unprocessed"));
//        unprocessedTransactions2.add(new Transaction(10, commonStockId, 15, 20.0, Timestamp.valueOf("2023-01-08 22:10:30"), "Unprocessed"));
//
//        for (Transaction transaction : unprocessedTransactions2) {
//            sellingDao.updateTransaction(transaction);
//        }

        //adding property
//        List<Stock> userproperty = new ArrayList<>();
//        userproperty.add(new Stock(30,Timestamp.valueOf("2023-01-02 12:00:00"),commonStockId));
//        userproperty.add(new Stock(30,Timestamp.valueOf("2023-01-03 12:00:00"),commonStockId));
//        userproperty.add(new Stock(30,Timestamp.valueOf("2023-01-04 12:00:00"),commonStockId));
//        userproperty.add(new Stock(30,Timestamp.valueOf("2023-01-05 12:00:00"),commonStockId));
//        userproperty.add(new Stock(30,Timestamp.valueOf("2023-01-06 12:00:00"),commonStockId));
//        int i = 6;
//        for (Stock stock : userproperty){
//
//            userStockDao.insert(i,stock);
//            i++;
//        }

        //add initial capital
//        for (int i=1; i<=10;i++){
//            userCapitalDao.insert(i,2000);
//        }
    }
}

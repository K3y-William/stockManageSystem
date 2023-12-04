package company;

import dao.impl.*;
import dao.priceControlDao;
import dao.transactionDao;
import dao.userCapitalDao;
import dao.userStockDao;
import entity.Stock;
import entity.Transaction;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class OrderMatchingSystem implements Runnable{

    private final priceControlDao priceControlDao;
    private final transactionDao buyingDao;
    private final transactionDao sellingDao;
    private final userCapitalDao userCapitalDao;
    private final userStockDao userStockDao;

    public OrderMatchingSystem(dao.priceControlDao priceControlDao, transactionDao buyingDao, transactionDao sellingDao, dao.userCapitalDao userCapitalDao, dao.userStockDao userStockDao) {
        this.priceControlDao = priceControlDao;
        this.buyingDao = buyingDao;
        this.sellingDao = sellingDao;
        this.userCapitalDao = userCapitalDao;
        this.userStockDao = userStockDao;
    }

    //Continuous Trading
        //making change to buying/selling platform, property, and user_capital
    @Override
    public void run() {
        System.out.println("Order Matching System Start....");
        while (true) {

            List<Stock> stock_list = priceControlDao.selectAll();
            for (Stock stock : stock_list) {
                int stock_id = stock.getStock_id();
                List<Transaction> buyingOrderList = buyingDaoImp.getBuyingOrder(stock_id);
                List<Transaction> sellingOrderList = sellingDaoImp.getSellingOrder(stock_id);

                if (buyingOrderList.isEmpty() || sellingOrderList.isEmpty()) {
                    continue;
                }

                while (!(buyingOrderList.isEmpty()) && !(sellingOrderList.isEmpty())
                        && (buyingOrderList.get(0).getPrice() >= sellingOrderList.get(0).getPrice())) {

                    Transaction currentBuyingOrder = buyingOrderList.get(0);
                    Transaction currentSellingOrder = sellingOrderList.get(0);

                    int buyer_id = currentBuyingOrder.getUser_id();
                    int seller_id = currentSellingOrder.getUser_id();

                    //1.get dealing price
                    //dealPrice equals to buying and selling price's average
                    double dealPrice = (currentBuyingOrder.getPrice() + currentSellingOrder.getPrice()) / 2;


                    //2.update transaction amount, status in buying and selling platform database
                    int newBuyingAmount, newSellingAmount;
                    int currentBuyingAmount = currentBuyingOrder.getAmounts();
                    int currentSellingAmount = currentSellingOrder.getAmounts();

                    //update transaction amount and status
                    if (currentBuyingAmount >= currentSellingAmount) {
                        newBuyingAmount = currentBuyingAmount - currentSellingAmount;
                        newSellingAmount = 0;
                        currentSellingOrder.setStatus("completed");
                        if (newBuyingAmount == 0) {
                            currentBuyingOrder.setStatus("completed");
                        } else {
                            currentBuyingOrder.setStatus("processing");
                        }
                    } else {
                        newSellingAmount = currentSellingAmount - currentBuyingAmount;
                        newBuyingAmount = 0;
                        currentBuyingOrder.setStatus("completed");
                        if (newSellingAmount == 0) {
                            currentSellingOrder.setStatus("completed");
                        } else {
                            currentSellingOrder.setStatus("processing");
                        }
                    }
                    currentBuyingOrder.setAmounts(newBuyingAmount);
                    currentSellingOrder.setAmounts(newSellingAmount);


                    //update in database
                    try {
                        buyingDao.updateTransaction(currentBuyingOrder);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        sellingDao.updateTransaction(currentSellingOrder);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }


                    //3.update user's capital
                    //calculate profit and expense
                    int sellingAmount = currentSellingAmount - newSellingAmount;
                    int buyingAmount = currentBuyingAmount - newBuyingAmount;

                    double sellerProfit = sellingAmount * dealPrice;
                    double buyerExpense = -1 * buyingAmount * dealPrice;

                    //update in user_capital database
                    userCapitalDao.updateCapital(seller_id, sellerProfit);
                    userCapitalDao.updateCapital(buyer_id, buyerExpense);


                    //4.update user's property
                    //preparing input variables
                    Timestamp date = new Timestamp(new Date().getTime());
                    Stock buyerStock = new Stock(buyingAmount, date, stock_id);
                    Stock sellerStock = new Stock(-1 * sellingAmount, stock_id);

                    //update property in database
                    userStockDao.updateTransaction(buyer_id, buyerStock);
                    userStockDao.updateTransaction(seller_id, sellerStock);


                    //5.update orderList
                    //if the current order is finish, amounts is 0, remove it from orderList
                    //otherwise remain in the orderList and wait for the next round matching
                    if (currentBuyingOrder.getAmounts() == 0) {
                        buyingOrderList.remove(0);
                    }
                    if (currentSellingOrder.getAmounts() == 0) {
                        sellingOrderList.remove(0);
                    }
                }
            }

            try {
                // sleep 10s
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}

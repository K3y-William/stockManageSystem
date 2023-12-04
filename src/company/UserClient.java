package company;

import dao.priceControlDao;
import dao.userStockDao;
import dao.userCapitalDao;
import dao.transactionDao;
import entity.Stock;
import entity.Transaction;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class UserClient implements Runnable {

    private int userId;  // Assuming you have the userId to identify the user
    private final userStockDao stockDao;
    private final userCapitalDao capitalDao;
    private final transactionDao buyingDao;
    private final transactionDao sellingDao;
    private final priceControlDao priceControlDao;

    public UserClient(int userId, userStockDao stockDao, userCapitalDao capitalDao, transactionDao buyingDao, transactionDao sellingDao, dao.priceControlDao priceControlDao) {
        this.userId = userId;
        this.stockDao = stockDao;
        this.capitalDao = capitalDao;
        this.buyingDao = buyingDao;
        this.sellingDao = sellingDao;
        this.priceControlDao = priceControlDao;
    }

    @Override
    public void run() {
        System.out.println("User Client Start....");
        Scanner scanner = new Scanner(System.in);

        // Check if the user is logged in or registered
        if (userId <= 0) {
            System.out.println("You need to either login or register first.");
            System.out.print("1. Register\n2. Login\nChoose an option: ");
            int authChoice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            try {
                switch (authChoice) {
                    case 1:
                        register();
                        break;
                    case 2:
                        userId = login();
                        break;
                    default:
                        System.out.println("Invalid option. Exiting the program.");
                        return;
                }
            } catch (SQLException e) {
                System.err.println("An error occurred: " + e.getMessage());
                return;
            }
        }

        while (true) {
            System.out.println("==== Menu ====");
            System.out.println("1. Inject Funds");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. Check Capital");
            System.out.println("5. Check Transactions");
            System.out.println("6. Check Assets");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            try {
                switch (choice) {
                    case 1:
                        injectFunds();
                    case 2:
                        buyStock();
                        break;
                    case 3:
                        sellStock();
                        break;
                    case 4:
                        checkCapital();
                        break;
                    case 5:
                        checkTransactions();
                        break;
                    case 6:
                        checkAssets();
                    case 7:
                        System.out.println("Exiting the program. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option. Please choose a valid option.");
                }
            } catch (SQLException e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private void checkAssets() {
        System.out.println("Checking Assets....");
        List<Stock> stockList = stockDao.selectAll(userId);
        for (int i = 0; i < stockList.size(); i++){
            System.out.println(stockList.get(i).toString());
        }
        System.out.println("Checking completed");
        System.out.println("------------------------------------");
    }

    private void injectFunds() {
        System.out.println("Injecting Funds....");
        Scanner scanner = new Scanner(System.in);
        double amount = -1;
        while (amount < 0){
            System.out.println("Enter the amount of Funds you want to transfer in: ");
            amount = scanner.nextDouble();
            if (amount < 0){
                System.out.println("PLEASE ENTER A VALID NUMBER!");
            }
        }
        capitalDao.insert(userId,amount);
        System.out.println("Inject completed");
        System.out.println("------------------------------------");
    }

    private void register() throws SQLException {
        // Implement registration logic
        System.out.println("Starting register....");
        dao.login.register();
        System.out.println("Register completed");
        System.out.println("------------------------------------");
    }

    private int login() throws SQLException {
        // Implement login logic and return the user id
        System.out.println("Starting login....");
        int id = dao.login.logging();
        System.out.println("Login completed");
        System.out.println("------------------------------------");
        return id;
    }

    private void buyStock() throws SQLException {
        // Implement buying stock logic
        System.out.println("Buying stock started....");
        System.out.println("Current Stock Market: ");
        List<Stock> stockList = priceControlDao.selectAll();
        for (int i = 0; i < stockList.size(); i++){
            System.out.println("    "+stockList.get(i).toString());
        }

        int stock_id = -1;
        int buyingNum = -1;
        double buyingPrice = -1;
        Scanner scanner = new Scanner(System.in);
        while (stock_id < 0 || buyingNum < 0 || buyingPrice < 0){
            System.out.println("Please enter the Stock ID you want to buy: ");
            stock_id = scanner.nextInt();
            System.out.println("Please enter the amount you want to buy: ");
            buyingNum = scanner.nextInt();
            System.out.println("Please enter the Buying price: ");
            buyingPrice = scanner.nextDouble();

            boolean notContain = true;
            for (Stock stock : stockList) {
                if (stock_id == stock.getStock_id()) {
                    notContain = false;
                    break;
                }
            }

            //check whether user has enough funds to buy stocks
            boolean notEnoughFunds = true;
            if (capitalDao.getCapital(userId) >= buyingNum*buyingPrice) notEnoughFunds = false;

            if (stock_id < 0 || buyingNum < 0 || buyingPrice < 0 || notContain || notEnoughFunds){
                System.out.println("PLEASE ENTER THE VALID INPUT! TRY AGAIN: ");
            }
        }
        Timestamp currTime= new Timestamp(new Date().getTime());
        Transaction transaction = new Transaction(userId,stock_id,buyingNum,buyingPrice,currTime,"unprocessed");
        buyingDao.updateTransaction(transaction);
        System.out.println("Transaction Completed");
        System.out.println("------------------------------------");


    }

    private void sellStock() throws SQLException {
        // Implement selling stock logic
        System.out.println("Selling stock started....");
        System.out.println("Current Stock Assets: ");
        List<Stock> stockList = stockDao.selectAll(userId);
        for (Stock value : stockList) {
            System.out.println("    " + value.toString());
        }

        int stock_id = -1;
        int sellingNum = -1;
        double sellingPrice = -1;
        Scanner scanner = new Scanner(System.in);
        while (stock_id < 0 || sellingNum < 0 || sellingPrice < 0){
            System.out.println("Please enter the Stock ID you want to sell: ");
            stock_id = scanner.nextInt();
            System.out.println("Please enter the amount you want to sell: ");
            sellingNum = scanner.nextInt();
            System.out.println("Please enter the Selling price: ");
            sellingPrice = scanner.nextDouble();

            //check whether user has enough stocks Assets to sell stocks
            boolean notContain = true;
            boolean notEnoughStocks = true;
            for (Stock stock : stockList) {
                if (stock_id == stock.getStock_id()) {
                    notContain = false;
                    if (sellingNum <= stock.getStock_num())  notEnoughStocks = false;
                    break;
                }
            }

            if (stock_id < 0 || sellingNum < 0 || sellingPrice < 0 || notContain || notEnoughStocks){
                System.out.println("PLEASE ENTER THE VALID INPUT! TRY AGAIN: ");
            }
        }
        Timestamp currTime= new Timestamp(new Date().getTime());
        Transaction transaction = new Transaction(userId,stock_id,sellingNum,sellingPrice,currTime,"unprocessed");
        sellingDao.updateTransaction(transaction);
        System.out.println("Transaction Completed");
        System.out.println("------------------------------------");
    }

    private void checkCapital() throws SQLException {
        // Implement checking capital logic
        System.out.println("Checking capital.....");
        double capital = capitalDao.getCapital(userId);
        System.out.println("Your remaining capital is: " + capital);
        System.out.println("------------------------------------");

    }

    private void checkTransactions() throws SQLException {
        // Implement checking transactions logic
        System.out.println("Checking transactions started....");
        List<Transaction> buyingTransactions = buyingDao.checkUser(userId);
        List<Transaction> sellingTransactions = sellingDao.checkUser(userId);
        System.out.println("Buying Orders: ");
        for (Transaction transaction : buyingTransactions){
            System.out.println("    " + transaction.toString());
        }
        System.out.println("Selling Orders: ");
        for (Transaction transaction : sellingTransactions){
            System.out.println("    " + transaction.toString());
        }

        System.out.println("Checking completed");
        System.out.println("------------------------------------");
    }
}

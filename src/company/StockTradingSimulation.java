package company;

import dao.impl.*;
import dao.*;

import java.util.Scanner;

public class StockTradingSimulation {
    private static final Scanner userClientListener = new Scanner(System.in);
    public static void main(String[] args) {
        // Create instances of the DAOs
        final priceControlDao priceControlDao = new priceControlDaoImp();
        final transactionDao buyingDao = new buyingDaoImp();
        final transactionDao sellingDao = new sellingDaoImp();
        final userCapitalDao userCapitalDao = new userCapitalDaoImp();
        final userStockDao userStockDao = new userStockDaoImp();

        // Create instances of the threads
        OrderMatchingSystem orderMatchingSystem = new OrderMatchingSystem(priceControlDao, buyingDao, sellingDao, userCapitalDao, userStockDao);
        PriceControlSystem priceControlSystem = new PriceControlSystem(priceControlDao);

        // Start the threads
        Thread orderMatchingThread = new Thread(orderMatchingSystem);
        Thread priceControlThread = new Thread(priceControlSystem);

        orderMatchingThread.start();
        priceControlThread.start();


        // The main thread can handle user clients
        //handleUserClients(userStockDao, userCapitalDao, buyingDao, sellingDao, priceControlDao);

    }
//    private static void handleUserClients(userStockDao userStockDao, userCapitalDao userCapitalDao, transactionDao buyingDao, transactionDao sellingDao, priceControlDao priceControlDao) {
//        while (true) {
//            System.out.println("Waiting for the new User Client request....");
//            System.out.println("Press 1 to request");
//            int userInput = userClientListener.nextInt();
//
//            if (userInput == 1) {
//                // initialize user_id = -1
//                int userId = -1;
//
//                // Create a new UserClient thread for each request
//                UserClient userClient = new UserClient(userId, userStockDao, userCapitalDao, buyingDao, sellingDao, priceControlDao);
//                Thread userClientThread = new Thread(userClient);
//
//                // Start the UserClient thread
//                userClientThread.start();
//            }
//            else {
//                System.out.println("Invalid input. Please enter 1 to sending new User Client request");
//            }
//
//            // Sleep for a short duration to avoid consuming too many resources
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}

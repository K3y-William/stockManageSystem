package entity;

import java.sql.Timestamp;

public class Transaction {

    private int user_id;
    private int stock_id;
    private int amounts;
    private double price;
    private Timestamp date;
    private String status;

    public Transaction(int user_id, int stock_id, int amounts, double price, Timestamp date, String status) {
        this.user_id = user_id;
        this.stock_id = stock_id;
        this.amounts = amounts;
        this.price = price;
        this.date = date;
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getStock_id() {
        return stock_id;
    }

    public int getAmounts() {
        return amounts;
    }

    public double getPrice() {
        return price;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setAmounts(int amounts) {
        this.amounts = amounts;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "user_id=" + user_id +
                ", stock_id=" + stock_id +
                ", amounts=" + amounts +
                ", price=" + price +
                ", date=" + date +
                ", status='" + status + '\'' +
                '}';
    }
}

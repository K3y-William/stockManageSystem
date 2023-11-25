package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable{
    private int user_id;
    private List<Stock> stock_list = new ArrayList<>();
    private double money;

    public User(int user_id, List<Stock> stock_list, double money) {
        this.user_id = user_id;
        this.stock_list = stock_list;
        this.money = money;
    }

    public User() {
    }

    public int getUser_id() {
        return user_id;
    }

    public List<Stock> getStock_list() {
        return stock_list;
    }

    public double getMoney() {
        return money;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setStock_list(List<Stock> stock_list) {
        this.stock_list = stock_list;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}

package dao;

import java.sql.*;
import java.util.Scanner;
import static util.DButil.*;


public class login {

    //check whether the username is in the database
    public static boolean check_username(String username) throws SQLException{
        Connection conn = getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "select username from login_info where username = ?";
        try {
            pstm = conn.prepareStatement(sql);
            pstm.setString(1,username);
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

    //check whether the password is in the database
    public static boolean check_passwrod(String password) throws SQLException{
        Connection conn = getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "select password from login_info where password = ?";
        try {
            pstm = conn.prepareStatement(sql);
            pstm.setString(1,password);
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


    //enter username and password, then confirm your password again until enter correctly twice
    //if the username has been used, try change to another one
    //after register successfully, exit register function and waiting for other orders
    public static void register() throws SQLException{

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your Username, no space is allowed: ");
        String username = scanner.next();
        while (check_username(username)){
            System.out.println("This username has been used, please try another one: ");
            username = scanner.next();
        }

        System.out.println("please enter your password, no space is allowed:");
        String password = scanner.next();
        System.out.println("please enter your password again: ");
        String psw = scanner.next();

        while (!psw.equals(password)){
            System.out.println("Please confirm you entered same password. Try again: ");
            password = scanner.next();
            System.out.println("please enter your password again: ");
            psw = scanner.next();
        }

        String sql = "insert into login_info(username,password) values(?,?)";
        Object[] obj= {username,password};
        exeUpdate(sql, obj);
        System.out.println("Register successfully!");

    }

    //login function, return the user's user_id
    public static int logging() throws SQLException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = scanner.next();
        System.out.println("Enter your password: ");
        String password = scanner.next();

        while(!check_username(username) || !check_passwrod(password)){
            System.out.println("Wrong username or password!");
            System.out.println("Renter your username: ");
            username = scanner.next();
            System.out.println("Renter your password: ");
            password = scanner.next();
        }

        Connection conn = getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "select user_id from login_info where username = ?";
        try {
            pstm = conn.prepareStatement(sql);
            pstm.setString(1,username);
            rs = pstm.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Login success!");
        rs.next();
        int id = rs.getInt(1);
        rs.close();
        pstm.close();
        conn.close();
        return id;
    }
}

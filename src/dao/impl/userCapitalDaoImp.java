package dao.impl;

import dao.userCapitalDao;
import util.DButil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static util.DButil.exeUpdate;

public class userCapitalDaoImp implements userCapitalDao {


    //if capital equals to -1, it means no such user_id
    @Override
    public double getCapital(int user_id) {
        double capital = -1;
        Connection conn = DButil.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;

        String sql =
                "select user_capital from user_capital where user_id = ?";

        try {
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1,user_id);
            rs = pstm.executeQuery();
            while (rs.next()){
                //extract data from rs
                capital = rs.getDouble("user_capital");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DButil.closeAll(pstm,conn,rs);
        }
        return capital;
    }

    //
    @Override
    public int updateCapital(int user_id, double changingAmount) {
        Object obj[] = {changingAmount,user_id};
        String sql = "update user_capital set user_capital = user_capital + ? where user_id = ?";
        return exeUpdate(sql,obj);
    }

    @Override
    public int insert(int user_id, double capital) {
        Object obj[] = {user_id,capital};
        String sql = "insert into user_capital values (?,?)";
        return exeUpdate(sql,obj);
    }
}

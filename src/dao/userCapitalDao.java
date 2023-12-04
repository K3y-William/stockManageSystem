package dao;

public interface userCapitalDao {

    //get target user's capital
    double getCapital(int user_id);

    //update user's capital by changingAmount
    int updateCapital(int user_id, double changingAmount);

    int insert(int user_id, double capital);
}

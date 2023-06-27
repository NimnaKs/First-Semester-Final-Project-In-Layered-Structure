package lk.ijse.posm.dao.custom.impl;

import lk.ijse.posm.controller.LoginFormController;
import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.UserDAO;
import lk.ijse.posm.entity.User_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAOImpl implements UserDAO {

    @Override
    public ArrayList<User_Entity> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(User_Entity user_entity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO user VALUES (?,?,?,?,?)",
                user_entity.getUser_id(),
                user_entity.getUser_name(),
                user_entity.getPassword(),
                user_entity.getType(),
                user_entity.getEmployee_id()
        );
    }

    @Override
    public boolean update(User_Entity user_entity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE user SET user_name=? WHERE user_id=?",
                user_entity.getUser_name(),user_entity.getUser_id());
    }

    @Override
    public boolean exits(String current_password) throws SQLException, ClassNotFoundException {

        ResultSet resultSet=SQLUtil.execute("SELECT * FROM user WHERE password=? AND user_id=?",current_password, LoginFormController.userId);
        return resultSet.next();
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT user_id FROM user ORDER BY user_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString("user_id");
            int newItemId = Integer.parseInt(id.replace("User", "")) + 1;
            return String.format("User%04d", newItemId);
        } else {
            return "User0001";
        }
    }

    @Override
    public User_Entity search(String Value) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean checkCredentials(User_Entity user_entity) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT * FROM USER WHERE user_name=? AND password=?",user_entity.getUser_name(),user_entity.getPassword());
        return resultSet.next();
    }

    @Override
    public boolean updatePassword(String newPassword) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE user SET password=? WHERE user_id=?",newPassword,LoginFormController.userId);
    }

    @Override
    public String getUserId(String username, String password) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT user_id FROM user WHERE password=? AND user_name=?",password,username);
        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    public boolean checkUserName(String username) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT * FROM user WHERE user_name=?",username);
        return resultSet.next();
    }

    @Override
    public boolean isNewPasswordExists(String newPassword) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT * FROM user WHERE password=?",newPassword);
        return resultSet.next();
    }

}

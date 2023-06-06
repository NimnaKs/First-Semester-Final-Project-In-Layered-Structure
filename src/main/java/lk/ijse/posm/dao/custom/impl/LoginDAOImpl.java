package lk.ijse.posm.dao.custom.impl;

import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.LoginDAO;
import lk.ijse.posm.entity.User_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginDAOImpl implements LoginDAO {

    @Override
    public ArrayList<User_Entity> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(User_Entity data) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(User_Entity data) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exits(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public void delete(String s) throws SQLException, ClassNotFoundException {

    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        return null;
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
}

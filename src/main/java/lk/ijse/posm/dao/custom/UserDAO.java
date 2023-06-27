package lk.ijse.posm.dao.custom;

import lk.ijse.posm.dao.CrudDAO;
import lk.ijse.posm.entity.User_Entity;

import java.sql.SQLException;

public interface UserDAO extends CrudDAO<User_Entity,String> {

    boolean checkCredentials(User_Entity user_entity) throws SQLException, ClassNotFoundException;

    boolean updatePassword(String newPassword) throws SQLException, ClassNotFoundException;

    String getUserId(String username, String password) throws SQLException, ClassNotFoundException;

    boolean checkUserName(String username) throws SQLException, ClassNotFoundException;

    boolean isNewPasswordExists(String newPassword) throws SQLException, ClassNotFoundException;
}

package lk.ijse.posm.dao.custom;

import lk.ijse.posm.dao.CrudDAO;
import lk.ijse.posm.entity.User_Entity;

import java.sql.SQLException;

public interface LoginDAO extends CrudDAO<User_Entity,String> {

    boolean checkCredentials(User_Entity user_entity) throws SQLException, ClassNotFoundException;
}

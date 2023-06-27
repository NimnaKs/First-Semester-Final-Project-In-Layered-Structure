package lk.ijse.posm.dao.custom;

import lk.ijse.posm.dao.CrudDAO;
import lk.ijse.posm.entity.Postman_Entity;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PostManDAO extends CrudDAO<Postman_Entity,String> {
    ArrayList<String> getAllIds() throws SQLException, ClassNotFoundException;

    ArrayList<String> getPostAreas() throws SQLException, ClassNotFoundException;
}

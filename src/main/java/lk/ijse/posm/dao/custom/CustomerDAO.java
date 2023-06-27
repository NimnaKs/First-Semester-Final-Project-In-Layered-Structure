package lk.ijse.posm.dao.custom;

import lk.ijse.posm.dao.CrudDAO;
import lk.ijse.posm.dao.SuperDAO;
import lk.ijse.posm.entity.Customer_Entity;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO extends CrudDAO<Customer_Entity,String> {
    List<String> getAllIds() throws SQLException, ClassNotFoundException;
}

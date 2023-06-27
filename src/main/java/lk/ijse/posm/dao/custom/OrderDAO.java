package lk.ijse.posm.dao.custom;

import lk.ijse.posm.dao.CrudDAO;
import lk.ijse.posm.dao.SuperDAO;
import lk.ijse.posm.entity.Orders_Entity;

import java.sql.SQLException;

public interface OrderDAO extends CrudDAO<Orders_Entity,String> {
    int getTotalOrderCount() throws SQLException, ClassNotFoundException;

}

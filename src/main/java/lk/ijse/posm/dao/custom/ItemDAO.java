package lk.ijse.posm.dao.custom;

import lk.ijse.posm.dao.CrudDAO;
import lk.ijse.posm.entity.Item_Entity;
import lk.ijse.posm.entity.Order_Details_Entity;

import java.sql.SQLException;
import java.util.List;

public interface ItemDAO extends CrudDAO<Item_Entity,String> {

    boolean updateQty(String itemCode, int qty) throws SQLException, ClassNotFoundException;

    List<String> getAllIds() throws SQLException, ClassNotFoundException;

    boolean updateRemoveQty(Order_Details_Entity orderDetailsEntity) throws SQLException, ClassNotFoundException;
}

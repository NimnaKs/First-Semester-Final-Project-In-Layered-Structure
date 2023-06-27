package lk.ijse.posm.dao.custom.impl;

import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.OrderDetailsDAO;
import lk.ijse.posm.entity.Order_Details_Entity;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailsDAOImpl implements OrderDetailsDAO {
    @Override
    public ArrayList<Order_Details_Entity> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Order_Details_Entity orderDetailsEntity) throws SQLException, ClassNotFoundException {

        return SQLUtil.execute("INSERT INTO order_details VALUES (?, ?, ?, ?)",
                orderDetailsEntity.getQty(),orderDetailsEntity.getItem_price(),orderDetailsEntity.getItem_code(),
                orderDetailsEntity.getOrder_id());

    }

    @Override
    public boolean update(Order_Details_Entity data) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exits(String paymentId) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String orderId) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("DELETE FROM order_details WHERE order_id=?",orderId);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public Order_Details_Entity search(String Value) throws SQLException, ClassNotFoundException {
        return null;
    }
}

package lk.ijse.posm.dao.custom.impl;

import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.OrderDAO;
import lk.ijse.posm.entity.Orders_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public int getTotalOrderCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet= SQLUtil.execute("SELECT COUNT(order_id) FROM orders WHERE order_date=CURRENT_DATE()");
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    @Override
    public ArrayList<Orders_Entity> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Orders_Entity ordersEntity) throws SQLException, ClassNotFoundException {
         return SQLUtil.execute("INSERT INTO Orders VALUES (?, ?, ?)",
                ordersEntity.getOrder_id(),
                ordersEntity.getOrder_date(),
                ordersEntity.getCustomer_id()
        );
    }

    @Override
    public boolean update(Orders_Entity data) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exits(String orderId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM orders WHERE order_id=?",orderId);
        return resultSet.next();
    }

    @Override
    public boolean delete(String orderId) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("DELETE FROM orders WHERE order_id=?",orderId);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString("order_id");
            int newItemId = Integer.parseInt(id.replace("Order", "")) + 1;
            return String.format("Order%03d", newItemId);
        } else {
            return "Order001";
        }
    }

    @Override
    public Orders_Entity search(String oId) throws SQLException, ClassNotFoundException {

        ResultSet resultSet=SQLUtil.execute("SELECT * FROM orders WHERE order_id=?",oId);

        if (resultSet.next()) {
            return new Orders_Entity(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
        }

        return new Orders_Entity();
    }
}

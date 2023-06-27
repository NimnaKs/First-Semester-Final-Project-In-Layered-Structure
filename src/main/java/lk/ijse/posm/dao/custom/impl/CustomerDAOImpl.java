package lk.ijse.posm.dao.custom.impl;

import lk.ijse.posm.controller.LoginFormController;
import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.CustomerDAO;
import lk.ijse.posm.entity.Customer_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public ArrayList<Customer_Entity> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Customer_Entity customerEntity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO customer VALUES (?,?,?,?)",
                customerEntity.getCustomer_id(),
                customerEntity.getCustomer_name(),
                customerEntity.getCustomer_contact(),
                LoginFormController.userId
        );
    }

    @Override
    public boolean update(Customer_Entity customerEntity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE customer SET customer_name=?,customer_contact=? WHERE customer_id=?",
                customerEntity.getCustomer_name(),
                customerEntity.getCustomer_contact(),
                customerEntity.getCustomer_id());
    }

    @Override
    public boolean exits(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT customer_id FROM customer ORDER BY customer_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString("customer_id");
            int newItemId = Integer.parseInt(id.replace("C", "")) + 1;
            return String.format("C%03d", newItemId);
        } else {
            return "C001";
        }
    }

    @Override
    public Customer_Entity search(String customerId) throws SQLException, ClassNotFoundException {

        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Customer WHERE customer_id = ?",customerId);

        if (resultSet.next()) {
            return new Customer_Entity(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );
        }
        return null;
    }

    @Override
    public List<String> getAllIds() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT customer_id FROM customer");

        List<String> ids = new ArrayList<>();
        while (resultSet.next()) {
            ids.add(resultSet.getString(1));
        }
        return ids;
    }
}

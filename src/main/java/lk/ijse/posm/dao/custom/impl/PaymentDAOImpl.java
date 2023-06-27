package lk.ijse.posm.dao.custom.impl;

import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.PaymentDAO;
import lk.ijse.posm.entity.Bill_payment_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public double getBillPaymentIncomeInEachMonth(int month) throws SQLException, ClassNotFoundException {
        ResultSet resultSet= SQLUtil.execute("SELECT SUM(payment) FROM bill_payment WHERE MONTH(payment_date)=?",month);
        if (resultSet.next()){
            return resultSet.getDouble(1)/100*5;
        }
        return 0.0;
    }

    @Override
    public boolean isTodayPaymentOk() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT COUNT(payment_id) FROM bill_payment WHERE payment_date=current_date");
        if (resultSet.next()){
            return resultSet.getInt(1) > 0;
        }
        return false;
    }

    @Override
    public ArrayList<Bill_payment_Entity> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Bill_payment_Entity paymentEntity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO bill_payment VALUES (?,?,?,?,?,?,?,?,?)",
                paymentEntity.getPayment_id(),
                paymentEntity.getBill_owner_name(),
                paymentEntity.getBill_type(),
                paymentEntity.getCompany_name(),
                paymentEntity.getReference_no(),
                paymentEntity.getPayment_date(),
                paymentEntity.getCustomer_id(),
                paymentEntity.getUser_id(),
                paymentEntity.getPayment());
    }

    @Override
    public boolean update(Bill_payment_Entity paymentEntity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE bill_payment SET bill_owner_name=?,bill_type=?,company_name=?,reference_no=?,payment_date=?,customer_id=?,payment=? WHERE payment_id=?",
                paymentEntity.getBill_owner_name(),
                paymentEntity.getBill_type(),
                paymentEntity.getCompany_name(),
                paymentEntity.getReference_no(),
                paymentEntity.getPayment_date(),
                paymentEntity.getCustomer_id(),
                paymentEntity.getPayment(),
                paymentEntity.getPayment_id());
    }

    @Override
    public boolean exits(String payment_id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT * FROM bill_payment WHERE payment_id=?",payment_id);
        return resultSet.next();
    }

    @Override
    public boolean delete(String payment_id) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("DELETE FROM bill_payment WHERE payment_id=?",payment_id);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT payment_id FROM bill_payment ORDER BY payment_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString("payment_id");
            int newItemId = Integer.parseInt(id.replace("Pay", "")) + 1;
            return String.format("Pay%03d", newItemId);
        } else {
            return "Pay001";
        }
    }

    @Override
    public Bill_payment_Entity search(String paymentId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT * FROM bill_payment WHERE payment_id=?",paymentId);

        if (resultSet.next()){
            return new Bill_payment_Entity(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getDouble(9));
        }

        return new Bill_payment_Entity();
    }
}

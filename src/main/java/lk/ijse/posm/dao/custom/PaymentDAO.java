package lk.ijse.posm.dao.custom;

import lk.ijse.posm.dao.CrudDAO;
import lk.ijse.posm.dao.SuperDAO;
import lk.ijse.posm.entity.Bill_payment_Entity;

import java.sql.SQLException;

public interface PaymentDAO extends CrudDAO<Bill_payment_Entity,String> {

    double getBillPaymentIncomeInEachMonth(int month) throws SQLException, ClassNotFoundException;

    boolean isTodayPaymentOk() throws SQLException, ClassNotFoundException;
}

package lk.ijse.posm.dao.custom.impl;

import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.ChangeDAO;
import lk.ijse.posm.entity.Changes_Entity;
import lk.ijse.posm.entity.Money_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ChangeDAOImpl implements ChangeDAO {
    @Override
    public double getChangeIncomeInEachMonth(int month) throws SQLException, ClassNotFoundException {
        ResultSet resultSet= SQLUtil.execute("SELECT SUM(qty) FROM changes WHERE MONTH(date)=? AND status='Getting Money'",month);
        if (resultSet.next()){
            return resultSet.getDouble(1)/100*5;
        }
        return 0.0;
    }

    @Override
    public boolean isTodayChangeOk() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT COUNT(payment_id) FROM changes WHERE date=current_date");
        if (resultSet.next()){
            return resultSet.getInt(1) > 0;
        }
        return false;
    }

    @Override
    public boolean makeChangesInMoney(double moneyAmount, String moneyId) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE money SET qty_on_hand = (qty_on_hand + ?) WHERE money_id = ?",moneyAmount,moneyId);
    }

    @Override
    public double getSellingMoneyAmount(String moneyType) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT qty_on_hand FROM money WHERE money_type=?",moneyType);
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0.0;
    }

    @Override
    public double getReceivingMoneyAmount(String moneyType, double moneyAmount) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT unit_selling_price FROM money WHERE money_type=?",moneyType);

        if (resultSet.next()){
            return moneyAmount*resultSet.getDouble(1);
        }

        return 0.0;
    }

    @Override
    public double getReceivingMoneyAmountFromAnotherCurrencyType(String moneyType, double moneyAmount) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT unit_getting_price FROM money WHERE money_type=?",moneyType);

        if (resultSet.next()){
            return moneyAmount/resultSet.getDouble(1);
        }

        return 0.0;
    }

    @Override
    public boolean updateChanges(Changes_Entity changesEntity, Money_Entity moneyEntity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE changes SET qty=? WHERE payment_id=? AND money_id=?",changesEntity.getQty(),changesEntity.getPayment_id(),moneyEntity.getMoney_id());
    }

    @Override
    public ArrayList<Changes_Entity> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Changes_Entity changesEntity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO changes VALUES (?,?,?,?,?,?,?)",
                changesEntity.getPayment_id(),changesEntity.getCustomer_id(),changesEntity.getMoney_id(), changesEntity.getQty(),
                changesEntity.getUnit_price(),changesEntity.getDate(),changesEntity.getStatus());
    }

    @Override
    public boolean update(Changes_Entity data) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exits(String paymentId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet= SQLUtil.execute("SELECT payment_id FROM changes WHERE payment_id=?",paymentId);
        return resultSet.next();
    }

    @Override
    public boolean delete(String paymentId) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("DELETE FROM changes WHERE payment_id=?",paymentId);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT payment_id FROM changes ORDER BY payment_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString("payment_id");
            int newItemId = Integer.parseInt(id.replace("Pay", "")) + 1;
            return String.format("Pay%03d", newItemId);
        } else {
            return "Pay001";
        }
    }

    @Override
    public Changes_Entity search(String Value) throws SQLException, ClassNotFoundException {
        return null;
    }
}

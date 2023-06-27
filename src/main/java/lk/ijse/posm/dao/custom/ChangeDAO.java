package lk.ijse.posm.dao.custom;

import lk.ijse.posm.dao.CrudDAO;
import lk.ijse.posm.entity.Changes_Entity;
import lk.ijse.posm.entity.Money_Entity;

import java.sql.SQLException;

public interface ChangeDAO extends CrudDAO<Changes_Entity,String> {
    double getChangeIncomeInEachMonth(int month) throws SQLException, ClassNotFoundException;

    boolean isTodayChangeOk() throws SQLException, ClassNotFoundException;

    boolean makeChangesInMoney(double moneyAmount, String moneyId) throws SQLException, ClassNotFoundException;

    double getSellingMoneyAmount(String moneyType) throws SQLException, ClassNotFoundException;

    double getReceivingMoneyAmount(String moneyType, double moneyAmount) throws SQLException, ClassNotFoundException;

    double getReceivingMoneyAmountFromAnotherCurrencyType(String moneyType, double moneyAmount) throws SQLException, ClassNotFoundException;

    boolean updateChanges(Changes_Entity sellingMoney, Money_Entity sellingMoney1) throws SQLException, ClassNotFoundException;
}

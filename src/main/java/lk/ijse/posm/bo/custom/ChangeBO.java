package lk.ijse.posm.bo.custom;

import lk.ijse.posm.bo.SuperBO;
import lk.ijse.posm.dto.ChangeDTO;
import lk.ijse.posm.dto.CustomerDTO;
import lk.ijse.posm.dto.MonthIncomeDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ChangeBO extends SuperBO {
    ArrayList<MonthIncomeDTO> getChangeIncomeInEachMonth() throws SQLException, ClassNotFoundException;

    String generateNextPaymentId() throws SQLException, ClassNotFoundException;

    boolean removeChangeDetails(ChangeDTO changeDTO) throws SQLException, ClassNotFoundException;

    boolean updateChangeDetails(CustomerDTO customerDTO, ChangeDTO changeDTO) throws SQLException, ClassNotFoundException;

    boolean saveChangeDetails(CustomerDTO customerDTO, ChangeDTO changeDTO) throws SQLException, ClassNotFoundException;

    double getSellingMoneyAmount(String value) throws SQLException, ClassNotFoundException;

    double getReceivingMoneyAmount(String value, double v) throws SQLException, ClassNotFoundException;

    double getReceivingMoneyAmountFromAnotherCurrencyType(String value, double v) throws SQLException, ClassNotFoundException;

    boolean isTodayChangesOk() throws SQLException, ClassNotFoundException;

    boolean isExits(String paymentId) throws SQLException, ClassNotFoundException;

    ChangeDTO searchPayment(String paymentIds) throws SQLException, ClassNotFoundException;
}

package lk.ijse.posm.bo.custom;

import lk.ijse.posm.bo.SuperBO;
import lk.ijse.posm.dto.CustomerDTO;
import lk.ijse.posm.dto.MonthIncomeDTO;
import lk.ijse.posm.dto.PaymentDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PaymentBO extends SuperBO {
    ArrayList<MonthIncomeDTO> getBillPaymentIncomeInEachMonth() throws SQLException, ClassNotFoundException;

    String generateNextPaymentId() throws SQLException, ClassNotFoundException;

    boolean removePayment(String payment_id) throws SQLException, ClassNotFoundException;

    boolean savePayment(CustomerDTO customerDTO, PaymentDTO paymentDTO) throws SQLException;

    boolean updatePayment(CustomerDTO customerDTO, PaymentDTO paymentDTO) throws SQLException;

    boolean isExists(String text) throws SQLException, ClassNotFoundException;

    PaymentDTO searchPayment(String text) throws SQLException, ClassNotFoundException;

    boolean isTodayPaymentsOk() throws SQLException, ClassNotFoundException;
}

package lk.ijse.posm.bo.custom;

import lk.ijse.posm.bo.SuperBO;
import lk.ijse.posm.dto.CartDTO;
import lk.ijse.posm.dto.CustomerDTO;
import lk.ijse.posm.dto.MonthIncomeDTO;
import lk.ijse.posm.dto.OrderDTO;
import lk.ijse.posm.dto.TM.OrderTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface OrderBO extends SuperBO {
    double getTotalSalesIncome() throws SQLException, ClassNotFoundException;

    int getTotalOrderCount() throws SQLException, ClassNotFoundException;

    ArrayList<MonthIncomeDTO> getTotalOrderIncomeInEachMonth() throws SQLException, ClassNotFoundException;

    boolean removeOrder(String orderId) throws SQLException, ClassNotFoundException;

    boolean placeOrder(String oId, CustomerDTO customerDTO, List<CartDTO> cartDTOList) throws SQLException;

    boolean updateOrder(String oId, CustomerDTO customerDTO, List<CartDTO> cartDTOList) throws SQLException, ClassNotFoundException;

    String generateNexOrderId() throws SQLException, ClassNotFoundException;

    boolean isExists(String text) throws SQLException, ClassNotFoundException;

    OrderDTO searchOrder(String text) throws SQLException, ClassNotFoundException;

    ArrayList<OrderTM> getDetails(String oId) throws SQLException, ClassNotFoundException;
}

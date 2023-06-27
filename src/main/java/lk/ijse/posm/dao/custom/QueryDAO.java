package lk.ijse.posm.dao.custom;

import lk.ijse.posm.dao.SuperDAO;
import lk.ijse.posm.dto.ChangeDTO;
import lk.ijse.posm.dto.NewUserDTO;
import lk.ijse.posm.dto.ParcelMailDTO;
import lk.ijse.posm.dto.PostMan_EmployeeDTO;
import lk.ijse.posm.dto.TM.OrderTM;

import java.sql.SQLException;
import java.util.ArrayList;

public interface QueryDAO extends SuperDAO {

    String getUserName(String username) throws SQLException, ClassNotFoundException;

    double getTotalSalesIncome() throws SQLException, ClassNotFoundException;

    int getTotalItemSalesCount() throws SQLException, ClassNotFoundException;

    String getUserEmail(String username) throws SQLException, ClassNotFoundException;

    double getTotalOrderIncomeInEachMonth(int month) throws SQLException, ClassNotFoundException;

    int getTotalCustomers() throws SQLException, ClassNotFoundException;

    String getPostMan(String address) throws SQLException, ClassNotFoundException;

    PostMan_EmployeeDTO getPostmanDetails(String postman_id) throws SQLException, ClassNotFoundException;

    NewUserDTO searchUser(String userId) throws SQLException, ClassNotFoundException;

    ChangeDTO searchPayment(String paymentIds) throws SQLException, ClassNotFoundException;

    ParcelMailDTO searchParcel(String mailId) throws SQLException, ClassNotFoundException;

    ArrayList<OrderTM> getAllOrderDetails(String oId) throws SQLException, ClassNotFoundException;
}

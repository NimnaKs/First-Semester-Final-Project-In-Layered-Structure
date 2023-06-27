package lk.ijse.posm.bo.custom;

import lk.ijse.posm.bo.SuperBO;
import lk.ijse.posm.dto.ChangeDTO;
import lk.ijse.posm.dto.CustomerDTO;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBO extends SuperBO {
    int getTotalCustomers() throws SQLException, ClassNotFoundException;

    CustomerDTO searchCustomer(String customerId) throws SQLException, ClassNotFoundException;

    String generateNextCustId() throws SQLException, ClassNotFoundException;

    List<String> getAllIds() throws SQLException, ClassNotFoundException;

}

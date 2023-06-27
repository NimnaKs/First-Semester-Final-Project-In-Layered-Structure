package lk.ijse.posm.bo.custom.impl;

import lk.ijse.posm.bo.custom.CustomerBO;
import lk.ijse.posm.dao.DAOFactory;
import lk.ijse.posm.dao.custom.CustomerDAO;
import lk.ijse.posm.dao.custom.QueryDAO;
import lk.ijse.posm.dto.CustomerDTO;
import lk.ijse.posm.entity.Customer_Entity;

import java.sql.SQLException;
import java.util.List;

public class CustomerBOImpl implements CustomerBO {

    CustomerDAO customerDAO= (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    QueryDAO queryDAO= (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY_DAO);

    @Override
    public int getTotalCustomers() throws SQLException, ClassNotFoundException {
        return queryDAO.getTotalCustomers();
    }

    @Override
    public CustomerDTO searchCustomer(String customerId) throws SQLException, ClassNotFoundException {
        Customer_Entity customerEntity=customerDAO.search(customerId);
        return new CustomerDTO(
                customerEntity.getCustomer_id(),
                customerEntity.getCustomer_name(),
                customerEntity.getCustomer_contact()
        );
    }

    @Override
    public String generateNextCustId() throws SQLException, ClassNotFoundException {
        return customerDAO.generateNewId();
    }

    @Override
    public List<String> getAllIds() throws SQLException, ClassNotFoundException {
        return customerDAO.getAllIds();
    }
}

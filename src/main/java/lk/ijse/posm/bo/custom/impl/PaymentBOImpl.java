package lk.ijse.posm.bo.custom.impl;

import lk.ijse.posm.bo.custom.PaymentBO;
import lk.ijse.posm.dao.DAOFactory;
import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.CustomerDAO;
import lk.ijse.posm.dao.custom.PaymentDAO;
import lk.ijse.posm.db.DBConnection;
import lk.ijse.posm.dto.CustomerDTO;
import lk.ijse.posm.dto.MonthIncomeDTO;
import lk.ijse.posm.dto.PaymentDTO;
import lk.ijse.posm.entity.Bill_payment_Entity;
import lk.ijse.posm.entity.Customer_Entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
public class PaymentBOImpl implements PaymentBO {

    PaymentDAO paymentDAO= (PaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PAYMENT);

    CustomerDAO customerDAO= (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    Connection con;

    {
        try {
            con = DBConnection.getDbConnection().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<MonthIncomeDTO> getBillPaymentIncomeInEachMonth() throws SQLException, ClassNotFoundException {
        ArrayList<MonthIncomeDTO> billPaymentIncome=new ArrayList<>();
        ArrayList<String> monthList=new ArrayList<>(Arrays.asList("January","February","March","April","May","June","July","August","October","November","December"));
        for (int i = 0; i < monthList.size() ; i++) {
            billPaymentIncome.add(new MonthIncomeDTO(monthList.get(i), paymentDAO.getBillPaymentIncomeInEachMonth(i+1)));
        }
        return billPaymentIncome;
    }

    @Override
    public String generateNextPaymentId() throws SQLException, ClassNotFoundException {
        return paymentDAO.generateNewId();
    }

    @Override
    public boolean removePayment(String payment_id) throws SQLException, ClassNotFoundException {
        return paymentDAO.delete(payment_id);
    }

    @Override
    public boolean savePayment(CustomerDTO customerDTO, PaymentDTO paymentDTO) throws SQLException {

        try {

            con.setAutoCommit(false);

            boolean isSavedCustomer = false;

            if (customerDTO.getCustomer_id().equals(customerDAO.generateNewId())){
                isSavedCustomer = customerDAO.save(new Customer_Entity(
                        customerDTO.getCustomer_id(),
                        customerDTO.getCustomer_name(),
                        customerDTO.getCustomer_contact(),
                        null
                ));
            } else {
                isSavedCustomer = customerDAO.update(new Customer_Entity(
                        customerDTO.getCustomer_id(),
                        customerDTO.getCustomer_name(),
                        customerDTO.getCustomer_contact(),
                        null)
                );
            }

            if (isSavedCustomer) {
                boolean isSavedPayment = paymentDAO.save(new Bill_payment_Entity(
                        paymentDTO.getPayment_id(),
                        paymentDTO.getBill_owner_name(),
                        paymentDTO.getBill_type(),
                        paymentDTO.getCompany_name(),
                        paymentDTO.getReferenceNo(),
                        paymentDTO.getPayment_Date(),
                        paymentDTO.getCustomer_id(),
                        paymentDTO.getUser_id(),
                        paymentDTO.getPaymentAmount()
                ));

                if (isSavedPayment) {
                    con.commit();
                    return true;
                }
            }
            return false;
        }catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            con.setAutoCommit(true);
        }
    }

    @Override
    public boolean updatePayment(CustomerDTO customerDTO, PaymentDTO paymentDTO) throws SQLException {
        try {
            con.setAutoCommit(false);

            boolean isUpdatedCustomer = false;

            isUpdatedCustomer = customerDAO.update(new Customer_Entity(
                    customerDTO.getCustomer_id(),
                    customerDTO.getCustomer_name(),
                    customerDTO.getCustomer_contact(),
                    null)
            );

            if (isUpdatedCustomer) {
                boolean isUpdatedPayment = paymentDAO.update(new Bill_payment_Entity(
                        paymentDTO.getPayment_id(),
                        paymentDTO.getBill_owner_name(),
                        paymentDTO.getBill_type(),
                        paymentDTO.getCompany_name(),
                        paymentDTO.getReferenceNo(),
                        paymentDTO.getPayment_Date(),
                        paymentDTO.getCustomer_id(),
                        paymentDTO.getUser_id(),
                        paymentDTO.getPaymentAmount()
                ));

                if (isUpdatedPayment) {
                    con.commit();
                    return true;
                }
            }
            return false;
        }catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            con.setAutoCommit(true);
        }
    }

    @Override
    public boolean isExists(String payment_id) throws SQLException, ClassNotFoundException {
        return paymentDAO.exits(payment_id);
    }

    @Override
    public PaymentDTO searchPayment(String paymentId) throws SQLException, ClassNotFoundException {
        Bill_payment_Entity billPaymentEntity=paymentDAO.search(paymentId);
        return new PaymentDTO(
                billPaymentEntity.getPayment_id(),
                billPaymentEntity.getBill_owner_name(),
                billPaymentEntity.getBill_type(),
                billPaymentEntity.getCompany_name(),
                billPaymentEntity.getReference_no(),
                billPaymentEntity.getPayment_date(),
                billPaymentEntity.getCustomer_id(),
                billPaymentEntity.getUser_id(),
                billPaymentEntity.getPayment()

        );
    }

    @Override
    public boolean isTodayPaymentsOk() throws SQLException, ClassNotFoundException {
        return paymentDAO.isTodayPaymentOk();
    }
}

package lk.ijse.posm.bo.custom.impl;

import lk.ijse.posm.bo.custom.ChangeBO;
import lk.ijse.posm.controller.LoginFormController;
import lk.ijse.posm.dao.DAOFactory;
import lk.ijse.posm.dao.custom.ChangeDAO;
import lk.ijse.posm.dao.custom.CurrencyDAO;
import lk.ijse.posm.dao.custom.CustomerDAO;
import lk.ijse.posm.dao.custom.QueryDAO;
import lk.ijse.posm.db.DBConnection;
import lk.ijse.posm.dto.ChangeDTO;
import lk.ijse.posm.dto.CustomerDTO;
import lk.ijse.posm.dto.MonthIncomeDTO;
import lk.ijse.posm.entity.Changes_Entity;
import lk.ijse.posm.entity.Customer_Entity;
import lk.ijse.posm.entity.Money_Entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ChangeBOImpl implements ChangeBO {

    ChangeDAO changeDAO= (ChangeDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CHANGE);

    CurrencyDAO currencyDAO= (CurrencyDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CURRENCY);

    QueryDAO queryDAO= (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY_DAO);

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
    public ArrayList<MonthIncomeDTO> getChangeIncomeInEachMonth() throws SQLException, ClassNotFoundException {
        ArrayList<MonthIncomeDTO> changeDetailsIncome=new ArrayList<>();
        ArrayList<String> monthList=new ArrayList<>(Arrays.asList("January","February","March","April","May","June","July","August","October","November","December"));
        for (int i = 0; i < monthList.size() ; i++) {
            changeDetailsIncome.add(new MonthIncomeDTO(monthList.get(i), changeDAO.getChangeIncomeInEachMonth(i+1)));
        }
        return changeDetailsIncome;
    }

    @Override
    public String generateNextPaymentId() throws SQLException, ClassNotFoundException {
        return changeDAO.generateNewId();
    }

    @Override
    public boolean removeChangeDetails(ChangeDTO changeDTO) throws SQLException, ClassNotFoundException {

        Money_Entity sellingMoney=currencyDAO.getMoneyDetails(changeDTO.getSellingMoneyType());

        Money_Entity receivingMoney=currencyDAO.getMoneyDetails(changeDTO.getReceivingMoneyType());

        try {

            con.setAutoCommit(false);

            boolean isRemoveMoney=changeDAO.delete(changeDTO.getPaymentId());

            if (isRemoveMoney) {

                boolean isRemoveSellingMoney = changeDAO.makeChangesInMoney(changeDTO.getSellingMoneyAmount(),sellingMoney.getMoney_id());

                if (isRemoveSellingMoney) {

                    boolean isRemoveReceivingMoney = changeDAO.makeChangesInMoney(-changeDTO.getReceivingMoneyAmount(),receivingMoney.getMoney_id());

                    if (isRemoveReceivingMoney) {
                        con.commit();
                        return true;
                    }
                }
            }
            return false;
        }catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }
    }

    @Override
    public boolean updateChangeDetails(CustomerDTO customerDTO, ChangeDTO changeDTO) throws SQLException, ClassNotFoundException {

        Money_Entity sellingMoney = currencyDAO.getMoneyDetails(changeDTO.getSellingMoneyType());

        Money_Entity receivingMoney = currencyDAO.getMoneyDetails(changeDTO.getReceivingMoneyType());

        try {
            con.setAutoCommit(false);

            boolean isSavedCustomer = false;

            if (customerDTO.getCustomer_id().equals(customerDAO.generateNewId())) {
                isSavedCustomer = customerDAO.save(new Customer_Entity(
                        customerDTO.getCustomer_id(),
                        customerDTO.getCustomer_name(),
                        customerDTO.getCustomer_contact(),
                        LoginFormController.userId
                ));
            } else {
                isSavedCustomer = customerDAO.update(new Customer_Entity(
                        customerDTO.getCustomer_id(),
                        customerDTO.getCustomer_name(),
                        customerDTO.getCustomer_contact(),
                        LoginFormController.userId
                ));
            }
            if (isSavedCustomer) {

                boolean isUpdateChange_1 = changeDAO.updateChanges(new Changes_Entity(
                        changeDTO.getPaymentId(), customerDTO.getCustomer_id(), receivingMoney.getMoney_id(), changeDTO.getReceivingMoneyAmount(),
                        receivingMoney.getUnit_getting_price(), String.valueOf(LocalDate.now()),
                        "Getting Money"), receivingMoney);

                if (isUpdateChange_1) {

                    boolean isUpdateChange_2 = changeDAO.updateChanges(new Changes_Entity(
                            changeDTO.getPaymentId(), customerDTO.getCustomer_id(), sellingMoney.getMoney_id(), changeDTO.getSellingMoneyAmount(),
                            sellingMoney.getUnit_getting_price(), String.valueOf(LocalDate.now()),
                            "Selling Money"),sellingMoney);

                    if (isUpdateChange_2) {

                        boolean isUpdateMoney = currencyDAO.updateMoneyQty(new Changes_Entity(), sellingMoney, receivingMoney);
                        if (isUpdateMoney) {
                            con.commit();
                            return true;
                        }
                    }
                }

            }
            return false;
        }catch (SQLException er) {
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }

    }

    @Override
    public boolean saveChangeDetails(CustomerDTO customerDTO, ChangeDTO changeDTO) throws SQLException, ClassNotFoundException {
        Money_Entity sellingMoney=currencyDAO.getMoneyDetails(changeDTO.getSellingMoneyType());

        Money_Entity receivingMoney=currencyDAO.getMoneyDetails(changeDTO.getReceivingMoneyType());

        try {

            con.setAutoCommit(false);

            boolean isSavedCustomer = false;

            if (customerDTO.getCustomer_id().equals(customerDAO.generateNewId())) {
                isSavedCustomer = customerDAO.save(new Customer_Entity(
                        customerDTO.getCustomer_id(),
                        customerDTO.getCustomer_name(),
                        customerDTO.getCustomer_contact(),
                        LoginFormController.userId
                ));
            } else {
                isSavedCustomer = customerDAO.update(new Customer_Entity(
                        customerDTO.getCustomer_id(),
                        customerDTO.getCustomer_name(),
                        customerDTO.getCustomer_contact(),
                        LoginFormController.userId
                ));
            }

            if (isSavedCustomer) {
                boolean isSavedPayment_1 =changeDAO.save(
                        new Changes_Entity(
                                changeDTO.getPaymentId(),customerDTO.getCustomer_id(),receivingMoney.getMoney_id(),changeDTO.getReceivingMoneyAmount(),
                                receivingMoney.getUnit_getting_price(),String.valueOf(LocalDate.now()),
                                "Getting Money"
                        ));

                if (isSavedPayment_1) {
                    boolean isSavedPayment_2 = changeDAO.save(
                            new Changes_Entity(
                                    changeDTO.getPaymentId(), customerDTO.getCustomer_id(), sellingMoney.getMoney_id(), changeDTO.getSellingMoneyAmount(),
                                    sellingMoney.getUnit_getting_price(), String.valueOf(LocalDate.now()),
                                    "Selling Money"
                            ));
                    if (isSavedPayment_2) {

                        boolean isUpdate = currencyDAO.updateQty(changeDTO, sellingMoney, receivingMoney);

                        if (isUpdate) {
                            con.commit();
                            return true;
                        }
                    }
                }
            }
            return false;
        }catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }
    }

    @Override
    public double getSellingMoneyAmount(String moneyType) throws SQLException, ClassNotFoundException {
        return changeDAO.getSellingMoneyAmount(moneyType);
    }

    @Override
    public double getReceivingMoneyAmount(String moneyType, double moneyAmount) throws SQLException, ClassNotFoundException {
        return changeDAO.getReceivingMoneyAmount(moneyType,moneyAmount);
    }

    @Override
    public double getReceivingMoneyAmountFromAnotherCurrencyType(String moneyType, double moneyAmount) throws SQLException, ClassNotFoundException {
        return changeDAO.getReceivingMoneyAmountFromAnotherCurrencyType(moneyType,moneyAmount);
    }

    @Override
    public boolean isTodayChangesOk() throws SQLException, ClassNotFoundException {
        return changeDAO.isTodayChangeOk();
    }

    @Override
    public boolean isExits(String paymentId) throws SQLException, ClassNotFoundException {
        return changeDAO.exits(paymentId);
    }

    @Override
    public ChangeDTO searchPayment(String paymentIds) throws SQLException, ClassNotFoundException {
        return queryDAO.searchPayment(paymentIds);
    }
}

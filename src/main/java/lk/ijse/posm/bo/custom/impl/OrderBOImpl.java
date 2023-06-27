package lk.ijse.posm.bo.custom.impl;

import lk.ijse.posm.bo.custom.OrderBO;
import lk.ijse.posm.controller.LoginFormController;
import lk.ijse.posm.dao.DAOFactory;
import lk.ijse.posm.dao.custom.*;
import lk.ijse.posm.db.DBConnection;
import lk.ijse.posm.dto.CartDTO;
import lk.ijse.posm.dto.CustomerDTO;
import lk.ijse.posm.dto.MonthIncomeDTO;
import lk.ijse.posm.dto.OrderDTO;
import lk.ijse.posm.dto.TM.OrderTM;
import lk.ijse.posm.entity.Customer_Entity;
import lk.ijse.posm.entity.Order_Details_Entity;
import lk.ijse.posm.entity.Orders_Entity;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderBOImpl implements OrderBO {

    QueryDAO queryDAO= (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY_DAO);

    OrderDAO orderDAO= (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);

    CustomerDAO customerDAO= (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    OrderDetailsDAO orderDetailsDAO= (OrderDetailsDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAILS);

    ItemDAO itemDAO= (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    Connection con;

    {
        try {
            con = DBConnection.getDbConnection().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double getTotalSalesIncome() throws SQLException, ClassNotFoundException {
        return queryDAO.getTotalSalesIncome();
    }

    @Override
    public int getTotalOrderCount() throws SQLException, ClassNotFoundException {
        return orderDAO.getTotalOrderCount();
    }

    @Override
    public ArrayList<MonthIncomeDTO> getTotalOrderIncomeInEachMonth() throws SQLException, ClassNotFoundException {
        ArrayList<MonthIncomeDTO> orderIncomeList=new ArrayList<>();
        ArrayList<String> monthList=new ArrayList<>(Arrays.asList("January","February","March","April","May","June","July","August","October","November","December"));
        for (int i = 0; i < monthList.size(); i++) {
            orderIncomeList.add(new MonthIncomeDTO(monthList.get(i), queryDAO.getTotalOrderIncomeInEachMonth(i+1)));
        }
        return orderIncomeList;
    }

    @Override
    public boolean removeOrder(String orderId) throws SQLException, ClassNotFoundException {
        try {
            con.setAutoCommit(false);

            ArrayList<OrderTM> orderList=new ArrayList<>();

            orderList=queryDAO.getAllOrderDetails(orderId);

            boolean isUpdateRemoveQty=false;

            for (OrderTM orderTM:orderList) {
                isUpdateRemoveQty=itemDAO.updateRemoveQty(new Order_Details_Entity(
                        orderTM.getQty(),
                        orderTM.getCode()
                ));
                isUpdateRemoveQty=true;
            }

            if (isUpdateRemoveQty){

                boolean isRemoveOrderId=orderDetailsDAO.delete(orderId);

                if (isRemoveOrderId){

                    boolean isRemoveOrder=orderDAO.delete(orderId);

                    if (isRemoveOrder){
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
    public boolean placeOrder(String oId, CustomerDTO customerDTO, List<CartDTO> cartDTOList) throws SQLException {
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
                boolean isSaved = orderDAO.save(new Orders_Entity(
                        oId,
                        String.valueOf(LocalDate.now()),
                        customerDTO.getCustomer_id()
                ));

                if (isSaved) {
                    boolean isUpdated = false;
                    for (CartDTO cartDTO :cartDTOList) {
                        isUpdated=itemDAO.updateQty(cartDTO.getItem_code(), cartDTO.getQty());
                    }
                    if (isUpdated) {
                        boolean isOrderDetailSaved = false;
                        for (CartDTO cartDTO :cartDTOList) {
                            isOrderDetailSaved=orderDetailsDAO.save(new Order_Details_Entity(
                                    cartDTO.getQty(),
                                    cartDTO.getUnitPrice(),
                                    cartDTO.getItem_code(),
                                    oId
                            ));
                            isOrderDetailSaved=true;
                        }
                        if (isOrderDetailSaved) {
                            con.commit();
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (SQLException er) {
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
    public boolean updateOrder(String oId, CustomerDTO customerDTO, List<CartDTO> cartDTOList) throws SQLException, ClassNotFoundException {

        boolean isRemove=removeOrder(oId);

        boolean isUpdate=false;
        if (isRemove){
            isUpdate=placeOrder(oId,customerDTO,cartDTOList);
        }
        System.out.println("isUpdate"+isUpdate);
        return isUpdate;
    }

    @Override
    public String generateNexOrderId() throws SQLException, ClassNotFoundException {
        return orderDAO.generateNewId();
    }

    @Override
    public boolean isExists(String orderId) throws SQLException, ClassNotFoundException {
        return orderDAO.exits(orderId);
    }

    @Override
    public OrderDTO searchOrder(String order_id) throws SQLException, ClassNotFoundException {
        Orders_Entity ordersEntity= orderDAO.search(order_id);
        return new OrderDTO(
                ordersEntity.getOrder_id(),
                ordersEntity.getOrder_date(),
                ordersEntity.getCustomer_id()
        );
    }

    @Override
    public ArrayList<OrderTM> getDetails(String oId) throws SQLException, ClassNotFoundException {
        return  queryDAO.getAllOrderDetails(oId);
    }
}
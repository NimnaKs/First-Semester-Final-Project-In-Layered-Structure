package lk.ijse.posm.bo.custom.impl;

import lk.ijse.posm.bo.custom.ParcelBO;
import lk.ijse.posm.controller.LoginFormController;
import lk.ijse.posm.dao.DAOFactory;
import lk.ijse.posm.dao.custom.*;
import lk.ijse.posm.db.DBConnection;
import lk.ijse.posm.dto.CustomerDTO;
import lk.ijse.posm.dto.MonthIncomeDTO;
import lk.ijse.posm.dto.ParcelMailDTO;
import lk.ijse.posm.dto.ParcelOrderDTO;
import lk.ijse.posm.dto.TM.CuriorTM;
import lk.ijse.posm.entity.Customer_Entity;
import lk.ijse.posm.entity.Ems_parcel_Entity;
import lk.ijse.posm.entity.Ems_parcel_service_Entity;
import lk.ijse.posm.entity.Parcel_Order_Details_Entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ParcelBOImpl implements ParcelBO {

    ParcelDAO parcelDAO= (ParcelDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PARCEL);

    ParcelServiceDAO parcelServiceDAO= (ParcelServiceDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ParcelService);

    ParcelOrderDetailsDAO parcelOrderDetailsDAO= (ParcelOrderDetailsDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ParcelOrderDetails);

    QueryDAO queryDAO= (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY_DAO);

    MailDAO mailDAO= (MailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.MAIL);

    CustomerDAO customerDAO= (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    Connection con;

    {
        try {
            con = DBConnection.getDbConnection().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            con = DBConnection.getDbConnection().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<ParcelOrderDTO> parcel_order_list;

    @Override
    public int getTodayParcelCount() throws SQLException, ClassNotFoundException {
        return parcelDAO.getTodayParcelCount();
    }

    @Override
    public ArrayList<MonthIncomeDTO> getTotalParcelIncomeInEachMonth() throws SQLException, ClassNotFoundException {
        ArrayList<MonthIncomeDTO> parcelDetailsIncome=new ArrayList<>();
        ArrayList<String> monthList=new ArrayList<>(Arrays.asList("January","February","March","April","May","June","July","August","October","November","December"));
        for (int i = 0; i <monthList.size() ; i++) {
            parcelDetailsIncome.add(new MonthIncomeDTO(monthList.get(i), parcelDAO.getTotalParcelIncomeInEachMonth(i+1)));
        }
        return parcelDetailsIncome;
    }

    @Override
    public ArrayList<CuriorTM> getAllParcelServiceDetails() throws SQLException, ClassNotFoundException {
        ArrayList<Ems_parcel_service_Entity> emsParcelServiceEntity =parcelServiceDAO.getAll();
        ArrayList<CuriorTM> parcelTMS=new ArrayList<>();
        for (Ems_parcel_service_Entity emsParcelEntity:emsParcelServiceEntity) {
            parcelTMS.add(new CuriorTM(
                    emsParcelEntity.getWeight_d(),
                    emsParcelEntity.getType(),
                    emsParcelEntity.getWeight_range(),
                    emsParcelEntity.getUnit_price()
            ));
        }
        return parcelTMS;
    }

    @Override
    public String getWeightId() throws SQLException, ClassNotFoundException {
        return parcelServiceDAO.generateNewId();
    }

    @Override
    public boolean saveEmsParcelDetails(CuriorTM curiorTM) throws SQLException, ClassNotFoundException {
        return parcelServiceDAO.save(new Ems_parcel_service_Entity(
                curiorTM.getWeightId(),
                curiorTM.getDescription(),
                curiorTM.getType(),
                curiorTM.getPrice()
        ));
    }

    @Override
    public boolean updateEmsParcelDetails(CuriorTM curiorTM) throws SQLException, ClassNotFoundException {
        return parcelServiceDAO.update(new Ems_parcel_service_Entity(
                curiorTM.getWeightId(),
                curiorTM.getDescription(),
                curiorTM.getType(),
                curiorTM.getPrice()
        ));
    }

    @Override
    public boolean isTodayMailsOk() throws SQLException, ClassNotFoundException {
        return parcelDAO.isTodayMailsOk();
    }

    @Override
    public boolean remove(String mail_id) throws SQLException {
        try {
            con.setAutoCommit(false);

            boolean isRemoveParcelOrder=parcelOrderDetailsDAO.delete(mail_id);

            if (isRemoveParcelOrder) {

                boolean isRemoveParcel=parcelDAO.delete(mail_id);

                if (isRemoveParcel){
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
    public boolean isExists(String mail_id) throws SQLException, ClassNotFoundException {
        return parcelDAO.exits(mail_id);
    }

    @Override
    public ParcelMailDTO searchParcel(String mail_id) throws SQLException, ClassNotFoundException {
        return queryDAO.searchParcel(mail_id);
    }

    @Override
    public double calculatePrice(double weightAmount, String type) throws SQLException, ClassNotFoundException {

        ArrayList<Ems_parcel_service_Entity> emsParcelServiceEntities= parcelOrderDetailsDAO.totalPrice(type);
        double total=0.0;

        parcel_order_list=new ArrayList<>();

        L1:for (Ems_parcel_service_Entity emsParcelEntity : emsParcelServiceEntities){

            String weightRange=emsParcelEntity.getWeight_range();

            if (!weightRange.equals("0")) {

                String[] range = emsParcelEntity.getWeight_range().split("-");

                double weight2=Double.parseDouble(range[1]);
                double weight1=Double.parseDouble(range[0]);

                if (weightAmount-(weight2-weight1)>=0){

                    total+=emsParcelEntity.getUnit_price()*(weight2-weight1);
                    parcel_order_list.add(new ParcelOrderDTO(emsParcelEntity.getWeight_d(),emsParcelEntity.getUnit_price(),weight2-weight1));
                    weightAmount-=(weight2-weight1);

                }else if (weightAmount-(weight2-weight1)<0){

                    total+=emsParcelEntity.getUnit_price()*weightAmount;
                    parcel_order_list.add(new ParcelOrderDTO(emsParcelEntity.getWeight_d(),emsParcelEntity.getUnit_price(),weightAmount));
                    weightAmount-=weightAmount;
                    break L1;

                }
            }else{
                total+=emsParcelEntity.getUnit_price();
                parcel_order_list.add(new ParcelOrderDTO(emsParcelEntity.getWeight_d(),emsParcelEntity.getUnit_price(),0));
            }
        }

        return total;
    }

    @Override
    public String getPostMan(String type) throws SQLException, ClassNotFoundException {
        return queryDAO.getPostMan(type);
    }

    @Override
    public String generateNextMailId() throws SQLException, ClassNotFoundException {
        return parcelDAO.generateNewId();
    }

    @Override
    public boolean updateDetails(CustomerDTO customerDTO, ParcelMailDTO parcelMailDTO) throws SQLException {

        try {

            con.setAutoCommit(false);

            boolean isRemoveParcelOrder=parcelOrderDetailsDAO.delete(parcelMailDTO.getMail_id());

            if (isRemoveParcelOrder) {

                boolean isRemoveParcel = parcelDAO.delete(parcelMailDTO.getMail_id());

                if (isRemoveParcel) {
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

                        boolean isSavedParcel = parcelDAO.save(new Ems_parcel_Entity(
                                parcelMailDTO.getDelivery_postman_id(),
                                customerDTO.getCustomer_id(),
                                LoginFormController.userId,
                                parcelMailDTO.getMail_id(),
                                parcelMailDTO.getSend_Name(),
                                parcelMailDTO.getSend_Address(),
                                parcelMailDTO.getSend_date(),
                                parcelMailDTO.getReceiver_Name(),
                                parcelMailDTO.getReceiver_Address(),
                                null,
                                parcelMailDTO.getReceiver_Telephone_No(),
                                parcelMailDTO.getMail_type(),
                                parcelMailDTO.getWeight(),
                                "Still delivering",
                                parcelMailDTO.getPrice()
                        ));

                        boolean isSavedParcelDetails=false;

                        if (isSavedParcel){
                            boolean isParcelSaved=false;
                            for (ParcelOrderDTO parcelOrderDTO : parcel_order_list) {
                                isParcelSaved=parcelDAO.saveParcelWeightDetails(new Parcel_Order_Details_Entity(
                                        parcelMailDTO.getMail_id(),
                                        parcelOrderDTO.getWeight_Id(),
                                        parcelOrderDTO.getPrice_per_Weight_range(),
                                        parcelOrderDTO.getWeight()
                                ));
                            }
                            if (isParcelSaved){
                                con.commit();
                                return true;
                            }
                        }
                    }
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
    public boolean saveDetails(CustomerDTO customerDTO, ParcelMailDTO parcelMailDTO) throws SQLException {

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

                boolean isSavedParcel = parcelDAO.save(new Ems_parcel_Entity(
                        parcelMailDTO.getDelivery_postman_id(),
                        customerDTO.getCustomer_id(),
                        LoginFormController.userId,
                        parcelMailDTO.getMail_id(),
                        parcelMailDTO.getSend_Name(),
                        parcelMailDTO.getSend_Address(),
                        String.valueOf(LocalDate.now()),
                        parcelMailDTO.getReceiver_Name(),
                        parcelMailDTO.getReceiver_Address(),
                        null,
                        parcelMailDTO.getReceiver_Telephone_No(),
                        parcelMailDTO.getMail_type(),
                        parcelMailDTO.getWeight(),
                        "Still delivering",
                        parcelMailDTO.getPrice()
                ));

                boolean isSavedParcelDetails=false;

                if (isSavedParcel){
                    boolean isParcelSaved=false;
                    for (ParcelOrderDTO parcelOrderDTO : parcel_order_list) {
                        isParcelSaved=parcelDAO.saveParcelWeightDetails(new Parcel_Order_Details_Entity(
                                parcelMailDTO.getMail_id(),
                                parcelOrderDTO.getWeight_Id(),
                                parcelOrderDTO.getPrice_per_Weight_range(),
                                parcelOrderDTO.getWeight()
                        ));
                    }
                    if (isParcelSaved){
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            con.setAutoCommit(true);
        }
    }

    @Override
    public String getPostManId(String address) throws SQLException, ClassNotFoundException {
        return mailDAO.getPostId(address);
    }


}

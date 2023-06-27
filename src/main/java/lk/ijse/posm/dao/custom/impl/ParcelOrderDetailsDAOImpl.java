package lk.ijse.posm.dao.custom.impl;

import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.ParcelOrderDetailsDAO;
import lk.ijse.posm.entity.Ems_parcel_service_Entity;
import lk.ijse.posm.entity.Parcel_Order_Details_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ParcelOrderDetailsDAOImpl implements ParcelOrderDetailsDAO {
    @Override
    public ArrayList<Parcel_Order_Details_Entity> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Parcel_Order_Details_Entity data) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Parcel_Order_Details_Entity data) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exits(String paymentId) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String mailId) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("DELETE FROM parcel_order_details WHERE mail_id=?",mailId);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public Parcel_Order_Details_Entity search(String Value) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public ArrayList<Ems_parcel_service_Entity> totalPrice(String type) throws SQLException, ClassNotFoundException {

        ResultSet resultSet = SQLUtil.execute("SELECT * FROM ems_parcel_service WHERE type=?",type);

        ArrayList<Ems_parcel_service_Entity> ems=new ArrayList<>();

        while (resultSet.next()){
            Ems_parcel_service_Entity emsParcelServiceEntity=new Ems_parcel_service_Entity(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4)
            );
            if(emsParcelServiceEntity.getWeight_d().equals("weight000")){
                emsParcelServiceEntity.setWeight_range("0");
            }
            ems.add(emsParcelServiceEntity);
        }

        return ems;
    }
}

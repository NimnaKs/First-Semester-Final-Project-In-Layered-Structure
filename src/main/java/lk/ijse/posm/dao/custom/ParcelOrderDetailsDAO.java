package lk.ijse.posm.dao.custom;

import lk.ijse.posm.dao.CrudDAO;
import lk.ijse.posm.entity.Ems_parcel_service_Entity;
import lk.ijse.posm.entity.Parcel_Order_Details_Entity;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ParcelOrderDetailsDAO extends CrudDAO<Parcel_Order_Details_Entity,String> {
    ArrayList<Ems_parcel_service_Entity> totalPrice(String type) throws SQLException, ClassNotFoundException;
}

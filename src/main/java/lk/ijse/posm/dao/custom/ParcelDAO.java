package lk.ijse.posm.dao.custom;

import lk.ijse.posm.dao.CrudDAO;
import lk.ijse.posm.dao.SuperDAO;
import lk.ijse.posm.dto.ParcelOrderDTO;
import lk.ijse.posm.entity.Ems_parcel_Entity;
import lk.ijse.posm.entity.Parcel_Order_Details_Entity;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ParcelDAO extends CrudDAO<Ems_parcel_Entity,String> {
    int getTodayParcelCount() throws SQLException, ClassNotFoundException;

    double getTotalParcelIncomeInEachMonth(int month) throws SQLException, ClassNotFoundException;

    int getMailCountForPostMan(String mailId) throws SQLException, ClassNotFoundException;

    ArrayList<Ems_parcel_Entity> getParcelDetails(String postmanId) throws SQLException, ClassNotFoundException;

    boolean isTodayMailsOk() throws SQLException, ClassNotFoundException;

    boolean saveParcelWeightDetails(Parcel_Order_Details_Entity parcelOrderDetailsEntity) throws SQLException, ClassNotFoundException;
}

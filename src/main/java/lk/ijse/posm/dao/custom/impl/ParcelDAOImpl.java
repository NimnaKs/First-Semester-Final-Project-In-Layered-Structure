package lk.ijse.posm.dao.custom.impl;

import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.ParcelDAO;
import lk.ijse.posm.dto.ParcelOrderDTO;
import lk.ijse.posm.entity.Ems_parcel_Entity;
import lk.ijse.posm.entity.Parcel_Order_Details_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ParcelDAOImpl implements ParcelDAO {
    @Override
    public int getTodayParcelCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet= SQLUtil.execute("SELECT COUNT(mail_id) FROM ems_parcel WHERE send_date=CURRENT_DATE()");
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    @Override
    public double getTotalParcelIncomeInEachMonth(int month) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT SUM(price) FROM ems_parcel WHERE MONTH(send_date)=?",month);
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0.0;
    }

    @Override
    public int getMailCountForPostMan(String postman_Id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet1=SQLUtil.execute("SELECT count(mail_id) FROM ems_parcel WHERE postman_id=? AND send_date=CURRENT_DATE()",postman_Id);

        if (resultSet1.next()){
            return resultSet1.getInt(1);
        }

        return 0;
    }

    @Override
    public ArrayList<Ems_parcel_Entity> getParcelDetails(String postmanId) throws SQLException, ClassNotFoundException {

        ArrayList<Ems_parcel_Entity> emsParcelEntities=new ArrayList<>();

        ResultSet resultSets=SQLUtil.execute("SELECT mail_id,receiver_name,receivers_address FROM ems_parcel WHERE postman_id=? AND send_date=CURRENT_DATE()",postmanId);

        while (resultSets.next()){
            emsParcelEntities.add(new Ems_parcel_Entity(
                    resultSets.getString(1),resultSets.getString(2),resultSets.getString(3)
            ));
        }

        return emsParcelEntities;
    }

    @Override
    public boolean isTodayMailsOk() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT COUNT(mail_id) FROM ems_parcel WHERE send_date=current_date");
        if (resultSet.next()){
            return resultSet.getInt(1)>0;
        }
        return false;
    }

    @Override
    public boolean saveParcelWeightDetails(Parcel_Order_Details_Entity parcelOrderDetailsEntity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO parcel_order_details VALUES (?,?,?,?)",
                parcelOrderDetailsEntity.getMail_id(),parcelOrderDetailsEntity.getWeight_id(),parcelOrderDetailsEntity.getPrice_per_weight_range(),parcelOrderDetailsEntity.getWeight());
    }

    @Override
    public ArrayList<Ems_parcel_Entity> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Ems_parcel_Entity emsParcelEntity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO ems_parcel VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                emsParcelEntity.getPostman_id(),
                emsParcelEntity.getCustomerId(),
                emsParcelEntity.getUser_id(),
                emsParcelEntity.getMail_id(),
                emsParcelEntity.getSenders_Name(),
                emsParcelEntity.getSender_Address(),
                emsParcelEntity.getSend_date(),
                emsParcelEntity.getReceiver_Name(),
                emsParcelEntity.getReceivers_address(),
                emsParcelEntity.getReceive_date(),
                emsParcelEntity.getReceivers_contact_No(),
                emsParcelEntity.getMail_type(),
                emsParcelEntity.getWeight(),
                emsParcelEntity.getStatus(),
                emsParcelEntity.getPrice()
        );

    }

    @Override
    public boolean update(Ems_parcel_Entity data) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exits(String mail_id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet= SQLUtil.execute("SELECT mail_id FROM ems_parcel WHERE mail_id=?",mail_id);
        return resultSet.next();
    }

    @Override
    public boolean delete(String mailId) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("DELETE FROM ems_parcel WHERE mail_id=?",mailId);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT mail_id FROM ems_parcel ORDER BY mail_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString("mail_id");
            int newItemId = Integer.parseInt(id.replace("Cmail", "")) + 1;
            return String.format("Cmail%03d", newItemId);
        } else {
            return "Cmail001";
        }
    }

    @Override
    public Ems_parcel_Entity search(String Value) throws SQLException, ClassNotFoundException {
        return null;
    }

}

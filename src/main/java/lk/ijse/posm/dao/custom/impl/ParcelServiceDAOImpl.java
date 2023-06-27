package lk.ijse.posm.dao.custom.impl;

import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.ParcelServiceDAO;
import lk.ijse.posm.entity.Ems_parcel_service_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ParcelServiceDAOImpl implements ParcelServiceDAO {
    @Override
    public ArrayList<Ems_parcel_service_Entity> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Ems_parcel_service_Entity> emsParcelServiceEntities=new ArrayList<>();

        ResultSet resultSet=SQLUtil.execute("SELECT * FROM ems_parcel_service");

        while (resultSet.next()){
            emsParcelServiceEntities.add(new Ems_parcel_service_Entity(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getDouble(4)));
        }

        return emsParcelServiceEntities;
    }

    @Override
    public boolean save(Ems_parcel_service_Entity emsParcelServiceEntity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO ems_parcel_service VALUES (?,?,?,?)",
                emsParcelServiceEntity.getWeight_d(),emsParcelServiceEntity.getWeight_range(),emsParcelServiceEntity.getType(),
                emsParcelServiceEntity.getUnit_price());
    }

    @Override
    public boolean update(Ems_parcel_service_Entity emsParcelServiceEntity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE ems_parcel_service SET unit_price=? WHERE weight_id=?",emsParcelServiceEntity.getUnit_price(),emsParcelServiceEntity.getWeight_d());
    }

    @Override
    public boolean exits(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT weight_id FROM ems_parcel_service ORDER BY weight_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString("weight_id");
            int newItemId = Integer.parseInt(id.replace("weight", "")) + 1;
            return String.format("weight%03d", newItemId);
        } else {
            return "weight001";
        }
    }

    @Override
    public Ems_parcel_service_Entity search(String Value) throws SQLException, ClassNotFoundException {
        return null;
    }
}

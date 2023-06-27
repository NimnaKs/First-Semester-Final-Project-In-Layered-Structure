package lk.ijse.posm.dao.custom.impl;

import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.PostManDAO;
import lk.ijse.posm.dto.PostManDTO;
import lk.ijse.posm.entity.Postman_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PostManDAOImpl implements PostManDAO {
    @Override
    public ArrayList<Postman_Entity> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Postman_Entity postman_entity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO postMan VALUES (?,?,?,?,?,?)",
                postman_entity.getPostman_id(),
                postman_entity.getPost_area(),
                postman_entity.getVehicle_no(),
                postman_entity.getVehicle_type(),
                postman_entity.getEmployee_id(),
                postman_entity.getImg()
        );
    }

    @Override
    public boolean update(Postman_Entity postman_entity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE postman SET post_area=?,vehicle_No=?,vehicle_type=?,image=? WHERE postman_id=?",
                postman_entity.getPost_area(),
                postman_entity.getVehicle_no(),
                postman_entity.getVehicle_type(),
                postman_entity.getImg(),
                postman_entity.getPostman_id()
        );
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
        ResultSet rst = SQLUtil.execute("SELECT postman_id FROM postMan ORDER BY postman_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString("postman_id");
            int newItemId = Integer.parseInt(id.replace("P", "")) + 1;
            return String.format("P%03d", newItemId);
        } else {
            return "P001";
        }
    }

    @Override
    public Postman_Entity search(String postmanId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT * FROM postMan WHERE postman_id=?",postmanId);

        if(resultSet.next()) {
            return new Postman_Entity(resultSet.getString(1),
                    resultSet.getString(2), resultSet.getString(3),
                    resultSet.getString(4), resultSet.getString(5),
                    resultSet.getString(6));
        }

        return new Postman_Entity();
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException, ClassNotFoundException {
        ArrayList<String> postmanIds=new ArrayList<>();

        ResultSet resultSet= SQLUtil.execute("SELECT postman_id FROM postMan");

        while (resultSet.next()){
            postmanIds.add(resultSet.getString(1));
        }

        return postmanIds;
    }

    @Override
    public ArrayList<String> getPostAreas() throws SQLException, ClassNotFoundException {
        ArrayList<String> postAreaList=new ArrayList<>();
        ResultSet resultSet=SQLUtil.execute("SELECT post_area FROM postMan");
        while (resultSet.next()){
            postAreaList.add(resultSet.getString(1));
        }
        return postAreaList;
    }
}

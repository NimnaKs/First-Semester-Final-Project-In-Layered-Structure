package lk.ijse.posm.dao.custom.impl;

import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.ItemDAO;
import lk.ijse.posm.entity.Item_Entity;
import lk.ijse.posm.entity.Order_Details_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {

    @Override
    public ArrayList<Item_Entity> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Item_Entity> itemList=new ArrayList<>();

        ResultSet resultSet=SQLUtil.execute("SELECT * FROM item");

        while (resultSet.next()){
            itemList.add(new Item_Entity(resultSet.getString(1),resultSet.getString(2),
                    resultSet.getInt(3),resultSet.getDouble(4),
                    resultSet.getString(5)));
        }

        return itemList;
    }

    @Override
    public boolean save(Item_Entity itemEntity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO item VALUES (?,?,?,?,?)",
                itemEntity.getItem_code(),itemEntity.getItem_type(),
                itemEntity.getQty_on_hand(),itemEntity.getUnit_price(),
                itemEntity.getImage()
        );
    }

    @Override
    public boolean update(Item_Entity itemEntity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE item SET item_type=?,qty_on_hand=?,unit_price=?,image=? WHERE item_code=?",
                itemEntity.getItem_type(),itemEntity.getQty_on_hand(),
                itemEntity.getUnit_price(),itemEntity.getImage(),
                itemEntity.getItem_code()
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
        ResultSet rst = SQLUtil.execute("SELECT item_code FROM item ORDER BY item_code DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString("item_code");
            int newItemId = Integer.parseInt(id.replace("I", "")) + 1;
            return String.format("I%03d", newItemId);
        } else {
            return "I001";
        }
    }

    @Override
    public Item_Entity search(String code) throws SQLException, ClassNotFoundException {

        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Item WHERE item_code = ?", code);

        if(resultSet.next()) {
            return new Item_Entity(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5)
            );
        }
        return null;
    }

    @Override
    public boolean updateQty(String itemCode, int qty) throws SQLException, ClassNotFoundException {
        return  SQLUtil.execute("UPDATE Item SET qty_on_hand = (qty_on_hand - ?) WHERE item_code = ?",
                qty,itemCode);
    }

    @Override
    public List<String> getAllIds() throws SQLException, ClassNotFoundException {
        List<String> codes = new ArrayList<>();
        ResultSet resultSet = SQLUtil.execute("SELECT item_code FROM Item");
        while(resultSet.next()) {
            codes.add(resultSet.getString(1));
        }
        return codes;
    }

    @Override
    public boolean updateRemoveQty(Order_Details_Entity orderDetailsEntity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE Item SET qty_on_hand=(qty_on_hand + ?) WHERE item_code=?",orderDetailsEntity.getQty(),orderDetailsEntity.getItem_code());
    }
}

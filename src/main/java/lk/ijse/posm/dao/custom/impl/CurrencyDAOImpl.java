package lk.ijse.posm.dao.custom.impl;

import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.CurrencyDAO;
import lk.ijse.posm.dto.ChangeDTO;
import lk.ijse.posm.entity.Changes_Entity;
import lk.ijse.posm.entity.Money_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CurrencyDAOImpl implements CurrencyDAO {
    @Override
    public ArrayList<Money_Entity> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Money_Entity> moneyEntityArrayList = new ArrayList<>();
        ResultSet resultSet= SQLUtil.execute("SELECT * FROM money");

        while(resultSet.next()){
            moneyEntityArrayList.add(new Money_Entity(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getDouble(5)
            ));
        }

        return moneyEntityArrayList;
    }

    @Override
    public boolean save(Money_Entity moneyEntity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO money VALUES (?,?,?,?,?)",
                moneyEntity.getMoney_id(),
                moneyEntity.getMoney_type(),
                moneyEntity.getUnit_selling_Price(),
                moneyEntity.getUnit_getting_price(),
                moneyEntity.getQty_on_hand()
        );
    }

    @Override
    public boolean update(Money_Entity moneyEntity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE money SET unit_selling_price=?,unit_getting_price=? WHERE money_type=?",
                moneyEntity.getUnit_selling_Price(),moneyEntity.getUnit_getting_price(),moneyEntity.getMoney_type());
    }

    @Override
    public boolean exits(String paymentId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet= SQLUtil.execute("SELECT payment_id FROM changes WHERE payment_id=?",paymentId);
        return resultSet.next();
    }

    @Override
    public boolean delete(String paymentId) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT money_id FROM money ORDER BY money_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString("money_id");
            int newItemId = Integer.parseInt(id.replace("M", "")) + 1;
            return String.format("M%03d", newItemId);
        } else {
            return "M001";
        }
    }

    @Override
    public Money_Entity search(String Value) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public Money_Entity getMoneyDetails(String moneyType) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT * FROM money WHERE money_type=?",moneyType);

        if (resultSet.next()){
            return new Money_Entity(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4),
                    resultSet.getDouble(5)

            );
        }

        return new Money_Entity();
    }

    @Override
    public boolean updateQty(ChangeDTO changeDTO, Money_Entity sellingMoney, Money_Entity receivingMoney) throws SQLException, ClassNotFoundException {
        boolean isUpdateSellingQty=SQLUtil.execute("UPDATE money SET qty_on_hand = (qty_on_hand - ?) WHERE money_id = ?",changeDTO.getSellingMoneyAmount(),sellingMoney.getMoney_id());

        boolean isUpdateReceivingQty=false;

        if (isUpdateSellingQty){
            isUpdateReceivingQty=SQLUtil.execute("UPDATE money SET qty_on_hand = (qty_on_hand + ?) WHERE money_id = ?",changeDTO.getReceivingMoneyAmount(),receivingMoney.getMoney_id());
        }

        return isUpdateReceivingQty;
    }

    @Override
    public boolean updateMoneyQty(Changes_Entity changesEntity, Money_Entity sellingMoney, Money_Entity receivingMoney) throws SQLException, ClassNotFoundException {
        boolean isUpdateSellingMoney=SQLUtil.execute("UPDATE money SET qty_on_hand=(qty_on_hand+?-?) WHERE money_id=?",sellingMoney.getUnit_getting_price(),changesEntity.getQty(),sellingMoney.getMoney_id());
        boolean isUpdateReceivingMoney=false;
        if (isUpdateSellingMoney){
            isUpdateReceivingMoney=SQLUtil.execute("UPDATE money SET qty_on_hand=(qty_on_hand-?+?) WHERE money_id=?",receivingMoney.getUnit_selling_Price(),changesEntity.getQty(),receivingMoney.getMoney_id());
        }
        return isUpdateReceivingMoney;
    }
}

package lk.ijse.posm.dao.custom;

import lk.ijse.posm.dao.CrudDAO;
import lk.ijse.posm.dto.ChangeDTO;
import lk.ijse.posm.entity.Changes_Entity;
import lk.ijse.posm.entity.Money_Entity;

import java.sql.SQLException;

public interface CurrencyDAO extends CrudDAO<Money_Entity,String> {
    Money_Entity getMoneyDetails(String moneyType) throws SQLException, ClassNotFoundException;

    boolean updateQty(ChangeDTO changeDTO, Money_Entity sellingMoney, Money_Entity receivingMoney) throws SQLException, ClassNotFoundException;

    boolean updateMoneyQty(Changes_Entity changesEntity, Money_Entity sellingMoney, Money_Entity receivingMoney) throws SQLException, ClassNotFoundException;
}

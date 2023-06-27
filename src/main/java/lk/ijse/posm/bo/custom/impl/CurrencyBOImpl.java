package lk.ijse.posm.bo.custom.impl;

import lk.ijse.posm.bo.custom.CurrencyBO;
import lk.ijse.posm.dao.DAOFactory;
import lk.ijse.posm.dao.custom.CurrencyDAO;
import lk.ijse.posm.dto.TM.CurrencyTM;
import lk.ijse.posm.entity.Money_Entity;

import java.sql.SQLException;
import java.util.ArrayList;

public class CurrencyBOImpl implements CurrencyBO {

    CurrencyDAO currencyDAO= (CurrencyDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CURRENCY);
    @Override
    public ArrayList<CurrencyTM> getAllCurrencyDetails() throws SQLException, ClassNotFoundException {
        ArrayList<Money_Entity> moneyEntityArrayList=currencyDAO.getAll();
        ArrayList<CurrencyTM> currencyTMArrayList=new ArrayList<>();
        for (Money_Entity moneyEntity: moneyEntityArrayList) {
            currencyTMArrayList.add(new CurrencyTM(moneyEntity.getMoney_type(),
                    moneyEntity.getUnit_selling_Price(),
                    moneyEntity.getUnit_getting_price(),
                    moneyEntity.getQty_on_hand())
            );
        }
        return currencyTMArrayList;
    }

    @Override
    public boolean saveCurrencyDetails(CurrencyTM currencyTM) throws SQLException, ClassNotFoundException {
        return currencyDAO.save(new Money_Entity(currencyDAO.generateNewId(), currencyTM.getMoneyType(),
                currencyTM.getUnitSellingPrice(),currencyTM.getUnitGettingPrice(),
                currencyTM.getQtyOnHand()));
    }

    @Override
    public boolean updateCurrencyDetails(CurrencyTM currencyTM) throws SQLException, ClassNotFoundException {
        return currencyDAO.update(new Money_Entity(null, currencyTM.getMoneyType(),
                currencyTM.getUnitSellingPrice(),currencyTM.getUnitGettingPrice(),
                currencyTM.getQtyOnHand()));
    }
}

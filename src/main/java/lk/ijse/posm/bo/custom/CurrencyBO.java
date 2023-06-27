package lk.ijse.posm.bo.custom;

import lk.ijse.posm.bo.SuperBO;
import lk.ijse.posm.dto.TM.CurrencyTM;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CurrencyBO extends SuperBO {
    ArrayList<CurrencyTM> getAllCurrencyDetails() throws SQLException, ClassNotFoundException;

    boolean saveCurrencyDetails(CurrencyTM currencyTM) throws SQLException, ClassNotFoundException;

    boolean updateCurrencyDetails(CurrencyTM currencyTM) throws SQLException, ClassNotFoundException;
}

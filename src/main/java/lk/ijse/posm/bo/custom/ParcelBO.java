package lk.ijse.posm.bo.custom;

import lk.ijse.posm.bo.SuperBO;
import lk.ijse.posm.dto.CustomerDTO;
import lk.ijse.posm.dto.MonthIncomeDTO;
import lk.ijse.posm.dto.ParcelMailDTO;
import lk.ijse.posm.dto.TM.CuriorTM;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ParcelBO extends SuperBO {
    int getTodayParcelCount() throws SQLException, ClassNotFoundException;

    ArrayList<MonthIncomeDTO> getTotalParcelIncomeInEachMonth() throws SQLException, ClassNotFoundException;

    ArrayList<CuriorTM> getAllParcelServiceDetails() throws SQLException, ClassNotFoundException;

    String getWeightId() throws SQLException, ClassNotFoundException;

    boolean saveEmsParcelDetails(CuriorTM curiorTM) throws SQLException, ClassNotFoundException;

    boolean updateEmsParcelDetails(CuriorTM curiorTM) throws SQLException, ClassNotFoundException;

    boolean isTodayMailsOk() throws SQLException, ClassNotFoundException;

    boolean remove(String mail_id) throws SQLException;

    boolean isExists(String mail_id) throws SQLException, ClassNotFoundException;

    ParcelMailDTO searchParcel(String text) throws SQLException, ClassNotFoundException;

    double calculatePrice(double weightAmount, String value) throws SQLException, ClassNotFoundException;

    String getPostMan(String international) throws SQLException, ClassNotFoundException;

    String generateNextMailId() throws SQLException, ClassNotFoundException;

    boolean updateDetails(CustomerDTO customerDTO, ParcelMailDTO international) throws SQLException;
    boolean saveDetails(CustomerDTO customerDTO, ParcelMailDTO international) throws SQLException;

    String getPostManId(String address) throws SQLException, ClassNotFoundException;
}

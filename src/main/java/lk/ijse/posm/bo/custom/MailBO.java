package lk.ijse.posm.bo.custom;

import lk.ijse.posm.bo.SuperBO;
import lk.ijse.posm.dto.MailDTO;

import java.sql.SQLException;

public interface MailBO extends SuperBO {
    int getInternationalMailsCount() throws SQLException, ClassNotFoundException;
    int getTodayTotalMailsCount() throws SQLException, ClassNotFoundException;

    String generateNextMailId() throws SQLException, ClassNotFoundException;

    String getPostMan(String address) throws SQLException, ClassNotFoundException;

    boolean saveMailDetails(MailDTO mailDTO) throws SQLException, ClassNotFoundException;

    String getPostManId(String address) throws SQLException, ClassNotFoundException;

    boolean updateDetails(MailDTO mailDTO) throws SQLException, ClassNotFoundException;

    boolean removeMailDetails(String mailId) throws SQLException, ClassNotFoundException;

    MailDTO searchData(String mailId) throws SQLException, ClassNotFoundException;

    boolean isIdExists(String mailId) throws SQLException, ClassNotFoundException;

    boolean isTodayMailsOk() throws SQLException, ClassNotFoundException;
}

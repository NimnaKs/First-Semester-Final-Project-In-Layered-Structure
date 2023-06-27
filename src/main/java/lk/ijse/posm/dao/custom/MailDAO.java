package lk.ijse.posm.dao.custom;

import lk.ijse.posm.dao.CrudDAO;
import lk.ijse.posm.dto.MailDTO;
import lk.ijse.posm.entity.Mails_Entity;

import java.sql.SQLException;
import java.util.ArrayList;

public interface MailDAO extends CrudDAO<Mails_Entity,String> {

     int getTodayMailsCount() throws SQLException, ClassNotFoundException;

     int getInternationalMailsCount() throws SQLException, ClassNotFoundException;

    String getPostId(String address) throws SQLException, ClassNotFoundException;

    boolean isTodayMailsOk() throws SQLException, ClassNotFoundException;

    int getMailCountForPostMan(String mailId) throws SQLException, ClassNotFoundException;

    ArrayList<Mails_Entity> getMailDetails(String postmanId) throws SQLException, ClassNotFoundException;
}

package lk.ijse.posm.dao.custom.impl;

import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.MailDAO;
import lk.ijse.posm.dto.MailDTO;
import lk.ijse.posm.dto.TM.PostManMailDetailsTM;
import lk.ijse.posm.entity.Mails_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MailDAOImpl implements MailDAO {
    @Override
    public int getTodayMailsCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet= SQLUtil.execute("SELECT COUNT(mail_id) FROM mails WHERE send_date=CURRENT_DATE() AND mail_type='Local'");
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    @Override
    public int getInternationalMailsCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT COUNT(mail_id) FROM mails WHERE send_date=CURRENT_DATE() AND mail_type='International'");
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    @Override
    public String getPostId(String address) throws SQLException, ClassNotFoundException {

        ResultSet resultSet = SQLUtil.execute("SELECT postman_id FROM postMan WHERE post_area=?",address);

        if (resultSet.next()){
            return resultSet.getString(1);
        }

        return null;
    }

    @Override
    public boolean isTodayMailsOk() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT COUNT(mail_id) FROM mails WHERE send_date=current_date");
        if (resultSet.next()){
            return resultSet.getInt(1)>0 ;
        }
        return false;
    }

    @Override
    public int getMailCountForPostMan(String postman_Id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT count(mail_id) FROM mails WHERE postman_id=? AND send_date=CURRENT_DATE()", postman_Id);

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    @Override
    public ArrayList<Mails_Entity> getMailDetails(String postman_Id) throws SQLException, ClassNotFoundException {

        ArrayList<Mails_Entity> mailsEntities=new ArrayList<>();

        ResultSet resultSet=SQLUtil.execute("SELECT mail_id,receiver_name,receivers_address FROM mails WHERE postman_id=? AND send_date=CURRENT_DATE()",postman_Id);

        while (resultSet.next()){
            mailsEntities.add(new Mails_Entity(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3)));
        }

        return mailsEntities;
    }

    @Override
    public ArrayList<Mails_Entity> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Mails_Entity mailsEntity) throws SQLException, ClassNotFoundException {

        return SQLUtil.execute("INSERT INTO mails VALUES (?,?,?,?,?,?,?,?,?)",mailsEntity.getPostman_id(),
                mailsEntity.getMails_id(),
                mailsEntity.getSenders_name(),
                mailsEntity.getSenders_address(),
                mailsEntity.getSend_date(),
                mailsEntity.getReceivers_name(),
                mailsEntity.getReceivers_address(),
                mailsEntity.getReceiver_date(),
                mailsEntity.getType());
    }

    @Override
    public boolean update(Mails_Entity mailsEntity) throws SQLException, ClassNotFoundException {

        return SQLUtil.execute("UPDATE mails SET postman_id=?,senders_name=?,sender_address=?,send_date=?,receiver_name=?,receivers_address=?,receive_date=?,mail_type=? WHERE mail_id=?",
                mailsEntity.getPostman_id(),
                mailsEntity.getSenders_name(),
                mailsEntity.getSenders_address(),
                mailsEntity.getSend_date(),
                mailsEntity.getReceivers_name(),
                mailsEntity.getReceivers_address(),
                mailsEntity.getReceiver_date(),
                mailsEntity.getType(),
                mailsEntity.getMails_id()
        );
    }

    @Override
    public boolean exits(String mailId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet= SQLUtil.execute("SELECT mail_id FROM mails WHERE mail_id=?",mailId);
        return resultSet.next();
    }

    @Override
    public boolean delete(String mailId) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("DELETE FROM mails WHERE mail_id=?",mailId);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT mail_id FROM mails ORDER BY mail_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString("mail_id");
            int newItemId = Integer.parseInt(id.replace("mail", "")) + 1;
            return String.format("mail%03d", newItemId);
        } else {
            return "mail001";
        }
    }

    @Override
    public Mails_Entity search(String mailId) throws SQLException, ClassNotFoundException {

        ResultSet resultSet = SQLUtil.execute("SELECT * FROM mails WHERE mail_id=?", mailId);

        if (resultSet.next()){
            return new Mails_Entity(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getString(9));
        }

        return new Mails_Entity();
    }
}

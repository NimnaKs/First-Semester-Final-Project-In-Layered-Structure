package lk.ijse.posm.bo.custom.impl;

import lk.ijse.posm.bo.custom.MailBO;
import lk.ijse.posm.dao.DAOFactory;
import lk.ijse.posm.dao.custom.MailDAO;
import lk.ijse.posm.dao.custom.QueryDAO;
import lk.ijse.posm.dto.MailDTO;
import lk.ijse.posm.entity.Mails_Entity;

import java.sql.SQLException;

public class MailBOImpl implements MailBO {

    MailDAO mailDAO= (MailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.MAIL);

    QueryDAO queryDAO= (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY_DAO);

    @Override
    public int getInternationalMailsCount() throws SQLException, ClassNotFoundException {
        return mailDAO.getInternationalMailsCount();
    }

    @Override
    public int getTodayTotalMailsCount() throws SQLException, ClassNotFoundException {
        return mailDAO.getTodayMailsCount();
    }

    @Override
    public String generateNextMailId() throws SQLException, ClassNotFoundException {
       return mailDAO.generateNewId();
    }

    @Override
    public String getPostMan(String address) throws SQLException, ClassNotFoundException {
        return queryDAO.getPostMan(address);
    }

    @Override
    public boolean saveMailDetails(MailDTO mailDTO) throws SQLException, ClassNotFoundException {
        return mailDAO.save(new Mails_Entity(
                mailDTO.getPostman_id(),
                mailDTO.getMails_id(),
                mailDTO.getSenders_name(),
                mailDTO.getSenders_address(),
                mailDTO.getSend_date(),
                mailDTO.getReceivers_name(),
                mailDTO.getReceivers_address(),
                mailDTO.getReceiver_date(),
                mailDTO.getType()
        ));
    }

    @Override
    public String getPostManId(String address) throws SQLException, ClassNotFoundException {
        return mailDAO.getPostId(address);
    }

    @Override
    public boolean updateDetails(MailDTO mailDTO) throws SQLException, ClassNotFoundException {
        return mailDAO.update(new Mails_Entity(
                mailDTO.getPostman_id(),
                mailDTO.getMails_id(),
                mailDTO.getSenders_name(),
                mailDTO.getSenders_address(),
                mailDTO.getSend_date(),
                mailDTO.getReceivers_name(),
                mailDTO.getReceivers_address(),
                mailDTO.getReceiver_date(),
                mailDTO.getType()
        ));
    }

    @Override
    public boolean removeMailDetails(String mailId) throws SQLException, ClassNotFoundException {
        return mailDAO.delete(mailId);
    }

    @Override
    public MailDTO searchData(String mailId) throws SQLException, ClassNotFoundException {
        Mails_Entity mailsEntity=mailDAO.search(mailId);
        return new MailDTO(
                mailsEntity.getPostman_id(),
                mailsEntity.getMails_id(),
                mailsEntity.getSenders_name(),
                mailsEntity.getSenders_address(),
                mailsEntity.getSend_date(),
                mailsEntity.getReceivers_name(),
                mailsEntity.getReceivers_address(),
                mailsEntity.getReceiver_date(),
                mailsEntity.getType()
        );
    }

    @Override
    public boolean isIdExists(String mailId) throws SQLException, ClassNotFoundException {
        return mailDAO.exits(mailId);
    }

    @Override
    public boolean isTodayMailsOk() throws SQLException, ClassNotFoundException {
        return mailDAO.isTodayMailsOk();
    }
}

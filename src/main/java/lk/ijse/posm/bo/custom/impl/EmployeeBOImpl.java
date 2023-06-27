package lk.ijse.posm.bo.custom.impl;

import lk.ijse.posm.bo.custom.EmployeeBO;
import lk.ijse.posm.dao.DAOFactory;
import lk.ijse.posm.dao.custom.*;
import lk.ijse.posm.dto.PostMan_EmployeeDTO;
import lk.ijse.posm.dto.TM.PostManMailDetailsTM;
import lk.ijse.posm.entity.Ems_parcel_Entity;
import lk.ijse.posm.entity.Mails_Entity;

import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeBOImpl implements EmployeeBO {

    PostManDAO postManDAO= (PostManDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.POSTMAN);

    EmployeeDAO employeeDAO= (EmployeeDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.EMPLOYEE);

    QueryDAO queryDAO= (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY_DAO);

    MailDAO mailDAO= (MailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.MAIL);

    ParcelDAO parcelDAO= (ParcelDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PARCEL);

    @Override
    public ArrayList<String> getAllIds() throws SQLException, ClassNotFoundException {
        return postManDAO.getAllIds();
    }

    @Override
    public PostMan_EmployeeDTO getPostmanDetails(String postman_id) throws SQLException, ClassNotFoundException {
        return queryDAO.getPostmanDetails(postman_id);
    }

    @Override
    public int getMailCount(String postman_id) throws SQLException, ClassNotFoundException {
        int count=0;

        count+=mailDAO.getMailCountForPostMan(postman_id);

        count+=parcelDAO.getMailCountForPostMan(postman_id);

        return count;
    }

    @Override
    public ArrayList<PostManMailDetailsTM> getMailDetails(String postman_Id) throws SQLException, ClassNotFoundException {

        ArrayList<PostManMailDetailsTM> mailDetails=new ArrayList<>();

        ArrayList<Mails_Entity> mailDTOArrayList=mailDAO.getMailDetails(postman_Id);

        for (Mails_Entity mailsEntity:mailDTOArrayList) {
            mailDetails.add(new PostManMailDetailsTM(mailsEntity.getMails_id(),
                    mailsEntity.getReceivers_name(),mailsEntity.getReceivers_address()));
        }

        ArrayList<Ems_parcel_Entity> parcelEntities=parcelDAO.getParcelDetails(postman_Id);

        for (Ems_parcel_Entity emsParcelEntity:parcelEntities) {
            mailDetails.add(new PostManMailDetailsTM(emsParcelEntity.getMail_id(),
                    emsParcelEntity.getReceiver_Name(),emsParcelEntity.getReceivers_address()));
        }

        return mailDetails;
    }
}

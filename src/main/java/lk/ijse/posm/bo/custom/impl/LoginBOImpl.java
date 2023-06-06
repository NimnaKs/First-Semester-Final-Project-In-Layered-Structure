package lk.ijse.posm.bo.custom.impl;

import lk.ijse.posm.bo.custom.LoginBO;
import lk.ijse.posm.dao.CrudDAO;
import lk.ijse.posm.dao.DAOFactory;
import lk.ijse.posm.dao.custom.LoginDAO;
import lk.ijse.posm.dto.UserDTO;
import lk.ijse.posm.entity.User_Entity;

import java.sql.SQLException;

public class LoginBOImpl implements LoginBO {

    LoginDAO loginDAO= (LoginDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.LOGIN);

    @Override
    public boolean checkCredentials(UserDTO userDTO) throws SQLException, ClassNotFoundException {
        return loginDAO.checkCredentials(new User_Entity(userDTO.getUserName(),userDTO.getPassword()));
    }
}

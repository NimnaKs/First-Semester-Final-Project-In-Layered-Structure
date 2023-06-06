package lk.ijse.posm.bo.custom;

import lk.ijse.posm.bo.SuperBO;
import lk.ijse.posm.dto.UserDTO;

import java.sql.SQLException;

public interface LoginBO extends SuperBO {

    boolean checkCredentials(UserDTO userDTO) throws SQLException, ClassNotFoundException;
}

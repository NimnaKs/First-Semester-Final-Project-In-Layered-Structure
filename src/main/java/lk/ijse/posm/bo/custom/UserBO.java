package lk.ijse.posm.bo.custom;

import lk.ijse.posm.bo.SuperBO;
import lk.ijse.posm.dto.NewUserDTO;
import lk.ijse.posm.dto.UserDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UserBO extends SuperBO {

    boolean checkCredentials(UserDTO userDTO) throws SQLException, ClassNotFoundException;

    String getUserName(String username) throws SQLException, ClassNotFoundException;

    String getUserEmail(String username) throws SQLException, ClassNotFoundException;

    boolean isExists(String text) throws SQLException, ClassNotFoundException;

    boolean updatePassword(String newPassword) throws SQLException, ClassNotFoundException;

    String getUserId(String username, String password) throws SQLException, ClassNotFoundException;

    boolean updateUser(NewUserDTO newUserDTO) throws SQLException, ClassNotFoundException;

    NewUserDTO getAllUserDetails() throws SQLException, ClassNotFoundException;

    boolean checkUserName(String userName) throws SQLException, ClassNotFoundException;

    boolean isNewPasswordExists(String newPassword) throws SQLException, ClassNotFoundException;

    String generateNextUserId() throws SQLException, ClassNotFoundException;

    boolean saveNewUser(NewUserDTO newUserDTO) throws SQLException, ClassNotFoundException;
}

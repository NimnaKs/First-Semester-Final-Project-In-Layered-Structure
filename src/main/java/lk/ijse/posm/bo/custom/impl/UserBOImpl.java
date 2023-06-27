package lk.ijse.posm.bo.custom.impl;

import lk.ijse.posm.bo.custom.UserBO;
import lk.ijse.posm.controller.LoginFormController;
import lk.ijse.posm.dao.DAOFactory;
import lk.ijse.posm.dao.custom.EmployeeDAO;
import lk.ijse.posm.dao.custom.QueryDAO;
import lk.ijse.posm.dao.custom.UserDAO;
import lk.ijse.posm.db.DBConnection;
import lk.ijse.posm.dto.NewUserDTO;
import lk.ijse.posm.dto.UserDTO;
import lk.ijse.posm.entity.Employee_Entity;
import lk.ijse.posm.entity.User_Entity;

import java.sql.Connection;
import java.sql.SQLException;

public class UserBOImpl implements UserBO {

    UserDAO userDAO = (UserDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.LOGIN);

    QueryDAO queryDAO= (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY_DAO);

    EmployeeDAO employeeDAO= (EmployeeDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.EMPLOYEE);

    Connection con;

    {
        try {
            con = DBConnection.getDbConnection().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean checkCredentials(UserDTO userDTO) throws SQLException, ClassNotFoundException {
        return userDAO.checkCredentials(new User_Entity(userDTO.getUserName(),userDTO.getPassword()));
    }

    @Override
    public String getUserName(String username) throws SQLException, ClassNotFoundException {
        return queryDAO.getUserName(username);
    }

    @Override
    public String getUserEmail(String username) throws SQLException, ClassNotFoundException {
        return queryDAO.getUserEmail(username);
    }

    @Override
    public boolean isExists(String current_password) throws SQLException, ClassNotFoundException {
        return userDAO.exits(current_password);
    }

    @Override
    public boolean updatePassword(String newPassword) throws SQLException, ClassNotFoundException {
        return userDAO.updatePassword(newPassword);
    }

    @Override
    public String getUserId(String username, String password) throws SQLException, ClassNotFoundException {
        return userDAO.getUserId(username,password);
    }

    @Override
    public boolean updateUser(NewUserDTO newUserDTO) throws SQLException, ClassNotFoundException {

        String employee_id = employeeDAO.getEmployeeId(LoginFormController.userId);

        try {

            con.setAutoCommit(false);

            boolean isUpdateUserDetails = userDAO.update(new User_Entity(
                    LoginFormController.userId,
                    newUserDTO.getUserName(),
                    null,
                    null,
                    null
            ));

            System.out.println("isUpdateUserDetails"+isUpdateUserDetails);

            if (isUpdateUserDetails) {
                boolean isSavedEmployeeDetails = employeeDAO.update(new Employee_Entity(
                        employee_id,
                        newUserDTO.getEmployeeName(),
                        newUserDTO.getEmployeeContactNo(),
                        newUserDTO.getEmployeeEmailAddress(),
                        null
                ));

                System.out.println("isSavedEmployeeDetails"+isSavedEmployeeDetails);

                if (isSavedEmployeeDetails) {
                    con.commit();
                    return true;
                }
            }
            return false;
        } catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }
    }

    @Override
    public NewUserDTO getAllUserDetails() throws SQLException, ClassNotFoundException {
        return queryDAO.searchUser(LoginFormController.userId);
    }

    @Override
    public boolean checkUserName(String userName) throws SQLException, ClassNotFoundException {
        return userDAO.checkUserName(userName);
    }

    @Override
    public boolean isNewPasswordExists(String newPassword) throws SQLException, ClassNotFoundException {
        return userDAO.isNewPasswordExists(newPassword);
    }

    @Override
    public String generateNextUserId() throws SQLException, ClassNotFoundException {
        return userDAO.generateNewId();
    }

    @Override
    public boolean saveNewUser(NewUserDTO newUserDTO) throws SQLException, ClassNotFoundException {

        String employeeId=employeeDAO.generateNewId();

        try {

            con.setAutoCommit(false);

            boolean isSavedEmployee=employeeDAO.save(new Employee_Entity(
                    employeeId,
                    newUserDTO.getEmployeeName(),
                    newUserDTO.getEmployeeContactNo(),
                    newUserDTO.getEmployeeEmailAddress(),
                    newUserDTO.getType())
            );

            if (isSavedEmployee) {
                boolean isSavedUserDetails =userDAO.save(new User_Entity(
                        newUserDTO.getUserId(),
                        newUserDTO.getUserName(),
                        newUserDTO.getPassword(),
                        newUserDTO.getType(),
                        employeeId
                ));

                if (isSavedUserDetails) {
                    con.commit();
                    return true;
                }
            }
            return false;
        } catch (SQLException er) {
            er.printStackTrace();
            con.rollback();
            return false;
        } finally {
            con.setAutoCommit(true);
        }

    }

}

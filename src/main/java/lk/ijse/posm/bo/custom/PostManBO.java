package lk.ijse.posm.bo.custom;

import lk.ijse.posm.bo.SuperBO;
import lk.ijse.posm.dto.EmployeeDTO;
import lk.ijse.posm.dto.PostManDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PostManBO extends SuperBO {
    String generateNextPostId() throws SQLException, ClassNotFoundException;

    ArrayList<String> getPostAreas() throws SQLException, ClassNotFoundException;

    PostManDTO getPostmanDetails(String postmanId) throws SQLException, ClassNotFoundException;

    EmployeeDTO getEmployeeDetails(String employeeId) throws SQLException, ClassNotFoundException;

    boolean savePostManAndEmployeeDetails(EmployeeDTO postmen, PostManDTO postManDTO) throws ClassNotFoundException, SQLException;

    boolean updatePostManAndEmployeeDetails(EmployeeDTO postman, PostManDTO postManDTO) throws SQLException;
}

package lk.ijse.posm.bo.custom;

import lk.ijse.posm.bo.SuperBO;
import lk.ijse.posm.dto.PostMan_EmployeeDTO;
import lk.ijse.posm.dto.TM.PostManMailDetailsTM;

import java.sql.SQLException;
import java.util.ArrayList;

public interface EmployeeBO extends SuperBO {
    ArrayList<String> getAllIds() throws SQLException, ClassNotFoundException;

    PostMan_EmployeeDTO getPostmanDetails(String postman_id) throws SQLException, ClassNotFoundException;

    int getMailCount(String postman_id) throws SQLException, ClassNotFoundException;

    ArrayList<PostManMailDetailsTM> getMailDetails(String postman_id) throws SQLException, ClassNotFoundException;
}

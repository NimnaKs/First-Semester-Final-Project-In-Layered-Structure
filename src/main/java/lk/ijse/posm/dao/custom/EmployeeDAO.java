package lk.ijse.posm.dao.custom;

import lk.ijse.posm.dao.CrudDAO;
import lk.ijse.posm.entity.Employee_Entity;

import java.sql.SQLException;

public interface EmployeeDAO extends CrudDAO<Employee_Entity,String> {
    String getEmployeeId(String userId) throws SQLException, ClassNotFoundException;
}

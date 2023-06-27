package lk.ijse.posm.dao.custom.impl;

import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.EmployeeDAO;
import lk.ijse.posm.entity.Employee_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeDAOImpl implements EmployeeDAO {
    @Override
    public ArrayList<Employee_Entity> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Employee_Entity employeeEntity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO employee VALUES (?,?,?,?,?)",
                employeeEntity.getEmployee_id(),
                employeeEntity.getEmployee_name(),
                employeeEntity.getEmployee_contactNo(),
                employeeEntity.getEmployee_email(),
                "User");
    }

    @Override
    public boolean update(Employee_Entity employee_entity) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE employee SET employee_name=?,employee_contact_no=?,employee_email=? WHERE employee_id=?",
                employee_entity.getEmployee_name(),
                employee_entity.getEmployee_contactNo(),
                employee_entity.getEmployee_email(),
                employee_entity.getEmployee_id());
    }

    @Override
    public boolean exits(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT employee_id FROM employee ORDER BY employee_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString("employee_id");
            int newItemId = Integer.parseInt(id.replace("E", "")) + 1;
            return String.format("E%03d", newItemId);
        } else {
            return "E001";
        }
    }

    @Override
    public Employee_Entity search(String employee_id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet= SQLUtil.execute("SELECT * FROM employee WHERE employee_id=?",employee_id);

        if (resultSet.next()) {
            return new Employee_Entity(resultSet.getString(1),resultSet.getString(2),
                    resultSet.getString(3),resultSet.getString(4),resultSet.getString(5));
        }

        return new Employee_Entity();
    }

    @Override
    public String getEmployeeId(String userId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT employee_id FROM user WHERE user_id=?",userId);

        if (resultSet.next()){
            return resultSet.getString(1);
        }

        return null;
    }
}

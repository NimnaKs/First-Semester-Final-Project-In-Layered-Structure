package lk.ijse.posm.bo.custom.impl;

import lk.ijse.posm.bo.custom.PostManBO;
import lk.ijse.posm.dao.DAOFactory;
import lk.ijse.posm.dao.custom.EmployeeDAO;
import lk.ijse.posm.dao.custom.PostManDAO;
import lk.ijse.posm.db.DBConnection;
import lk.ijse.posm.dto.EmployeeDTO;
import lk.ijse.posm.dto.PostManDTO;
import lk.ijse.posm.entity.Employee_Entity;
import lk.ijse.posm.entity.Postman_Entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class PostManBOImpl implements PostManBO {

    private final Connection con;

    {
        try {
            con = DBConnection.getDbConnection().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    PostManDAO postManDAO= (PostManDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.POSTMAN);

    EmployeeDAO employeeDAO= (EmployeeDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.EMPLOYEE);
    @Override
    public String generateNextPostId() throws SQLException, ClassNotFoundException {
        return postManDAO.generateNewId();
    }

    @Override
    public ArrayList<String> getPostAreas() throws SQLException, ClassNotFoundException {
        return postManDAO.getPostAreas();
    }

    @Override
    public PostManDTO getPostmanDetails(String postmanId) throws SQLException, ClassNotFoundException {
        Postman_Entity postman_entity=postManDAO.search(postmanId);
        return new PostManDTO(
                postman_entity.getPostman_id(),
                postman_entity.getPost_area(),
                postman_entity.getVehicle_no(),
                postman_entity.getVehicle_type(),
                postman_entity.getEmployee_id(),
                postman_entity.getImg()
        );
    }

    @Override
    public EmployeeDTO getEmployeeDetails(String employeeId) throws SQLException, ClassNotFoundException {
        Employee_Entity employeeEntity=employeeDAO.search(employeeId);
        return new EmployeeDTO(
                employeeEntity.getEmployee_id(),
                employeeEntity.getEmployee_name(),
                employeeEntity.getEmployee_contactNo(),
                employeeEntity.getEmployee_email(),
                employeeEntity.getEmployee_type()
        );
    }

    @Override
    public boolean savePostManAndEmployeeDetails(EmployeeDTO employeeDTO, PostManDTO postManDTO) throws ClassNotFoundException, SQLException {

        try {

            con.setAutoCommit(false);

            String employeeId = employeeDAO.generateNewId();

            if (employeeId != null) {

                boolean isEmployeeSave = employeeDAO.save(new Employee_Entity(
                        employeeId, employeeDTO.getEmployee_name(),
                        employeeDTO.getEmployee_contactNo(),
                        employeeDTO.getEmployee_email(),
                        employeeDTO.getEmployee_type()
                ));

                if (isEmployeeSave) {

                    boolean isPostManSaved = postManDAO.save(new Postman_Entity(
                            postManDTO.getPostman_id(),
                            postManDTO.getPost_area(),
                            postManDTO.getVehicle_no(),
                            postManDTO.getVehicle_type(),
                            employeeId,
                            postManDTO.getImg()
                    ));

                    if (isPostManSaved) {
                        con.commit();
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException er) {
            er.printStackTrace();
            Objects.requireNonNull(con).rollback();
            return false;
        } finally {
            Objects.requireNonNull(con).setAutoCommit(true);
        }

    }

    @Override
    public boolean updatePostManAndEmployeeDetails(EmployeeDTO employeeDTO, PostManDTO postManDTO) throws SQLException {

        try {

            con.setAutoCommit(false);

            boolean isEmployeeSave = employeeDAO.update(new Employee_Entity(
                    employeeDTO.getEmployee_id(), employeeDTO.getEmployee_name(),
                    employeeDTO.getEmployee_contactNo(),
                    employeeDTO.getEmployee_email(),
                    employeeDTO.getEmployee_type()
            ));

            if (isEmployeeSave) {

                boolean isPostManSaved = postManDAO.update(new Postman_Entity(
                        postManDTO.getPostman_id(),
                        postManDTO.getPost_area(),
                        postManDTO.getVehicle_no(),
                        postManDTO.getVehicle_type(),
                        employeeDTO.getEmployee_id(),
                        postManDTO.getImg()
                ));

                if (isPostManSaved) {
                    con.commit();
                    return true;
                }
            }

            return false;
        } catch (SQLException er) {
            er.printStackTrace();
            Objects.requireNonNull(con).rollback();
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            Objects.requireNonNull(con).setAutoCommit(true);
        }
    }

}

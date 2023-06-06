package lk.ijse.posm.dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T,ID> extends SuperDAO{
    public ArrayList<T> getAll() throws SQLException, ClassNotFoundException;

    public boolean save(T data) throws SQLException, ClassNotFoundException;

    public boolean update(T data) throws SQLException, ClassNotFoundException;

    public boolean exits(ID id) throws SQLException, ClassNotFoundException;

    public void delete(ID id) throws SQLException, ClassNotFoundException;

    public ID generateNewId() throws SQLException, ClassNotFoundException;

    public T search(ID Value) throws SQLException, ClassNotFoundException;
}

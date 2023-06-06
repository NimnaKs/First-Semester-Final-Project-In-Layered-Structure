package lk.ijse.posm.dao;

import lk.ijse.posm.db.DBConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLUtil {

    public static <T> T execute(String sql,Object... args) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getDbConnection().getConnection().prepareStatement(sql);
        for(int i=0;i<args.length;i++)
            preparedStatement.setObject(i+1,args[i]);
        return (sql.startsWith("SELECT"))?
                (T) preparedStatement.executeQuery():
                (T)(Boolean)(preparedStatement.executeUpdate()>0);
    }

}

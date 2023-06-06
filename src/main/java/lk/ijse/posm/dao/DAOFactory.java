package lk.ijse.posm.dao;


import lk.ijse.posm.dao.custom.impl.LoginDAOImpl;

public class DAOFactory {
    private static DAOFactory daoFactory;
    private DAOFactory() {
    }
    public static DAOFactory getDaoFactory() {
        return (daoFactory == null) ? daoFactory = new DAOFactory() : daoFactory;
    }
    public enum DAOTypes {
        LOGIN,CUSTOMER,ITEM,ORDER,ORDER_DETAILS,QUERY_DAO
    }
    public SuperDAO getDAO(DAOTypes types){
        switch (types) {
            case LOGIN:
                return new LoginDAOImpl();
            case ITEM:
                //return new ItemDAOImpl();
            case ORDER:
                //return new OrderDAOImpl();
            case ORDER_DETAILS:
               // return new OrderDetailsDAOImpl();
            case QUERY_DAO:
               // return new QueryDAOImpl();
            default:
                return null;
        }
    }
}
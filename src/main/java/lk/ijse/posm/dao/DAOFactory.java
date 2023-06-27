package lk.ijse.posm.dao;


import lk.ijse.posm.dao.custom.impl.*;

public class DAOFactory {
    private static DAOFactory daoFactory;
    private DAOFactory() {
    }
    public static DAOFactory getDaoFactory() {
        return (daoFactory == null) ? daoFactory = new DAOFactory() : daoFactory;
    }
    public enum DAOTypes {
        LOGIN,ORDER,ITEM,CUSTOMER,QUERY_DAO,MAIL,PARCEL,PAYMENT,CHANGE,CURRENCY,EMPLOYEE,POSTMAN,ParcelService,ParcelOrderDetails,ORDER_DETAILS
    }
    public SuperDAO getDAO(DAOTypes types){
        switch (types) {
            case LOGIN:
                return new UserDAOImpl();
            case ORDER:
                return new OrderDAOImpl();
            case ITEM:
                return new ItemDAOImpl();
            case QUERY_DAO:
               return new QueryDAOImpl();
            case CUSTOMER:
                return new CustomerDAOImpl();
            case MAIL:
                return new MailDAOImpl();
            case PARCEL:
                return new ParcelDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            case CHANGE:
                return new ChangeDAOImpl();
            case CURRENCY:
                return new CurrencyDAOImpl();
            case EMPLOYEE:
                return new EmployeeDAOImpl();
            case POSTMAN:
                return new PostManDAOImpl();
            case ParcelService:
                return new ParcelServiceDAOImpl();
            case ParcelOrderDetails:
                return new ParcelOrderDetailsDAOImpl();
            case ORDER_DETAILS:
                return new OrderDetailsDAOImpl();
            default:
                return null;
        }
    }
}
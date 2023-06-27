package lk.ijse.posm.bo;

import lk.ijse.posm.bo.custom.impl.*;
import lk.ijse.posm.dao.custom.impl.MailDAOImpl;
import lk.ijse.posm.dao.custom.impl.ParcelDAOImpl;

public class BOFactory {
    private static BOFactory boFactory;
    private BOFactory() {
    }
    public static BOFactory getBoFactory() {
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }
    public enum BOTypes {
        LOGIN,ORDER,ITEM,CUSTOMER,MAIL,PARCEL,PAYMENT,CHANGE,CURRENCY,EMPLOYEE,POSTMAN
    }
    public SuperBO getBO(BOTypes types){
        switch (types) {
            case LOGIN:
                return new UserBOImpl();
            case ORDER:
                return new OrderBOImpl();
            case ITEM:
                return new ItemBOImpl();
            case CUSTOMER:
                return new CustomerBOImpl();
            case MAIL:
                return new MailBOImpl();
            case PARCEL:
                return new ParcelBOImpl();
            case PAYMENT:
                return new PaymentBOImpl();
            case CHANGE:
                return new ChangeBOImpl();
            case CURRENCY:
                return new CurrencyBOImpl();
            case EMPLOYEE:
                return new EmployeeBOImpl();
            case POSTMAN:
                return new PostManBOImpl();
            default:
                return null;
        }
    }

}

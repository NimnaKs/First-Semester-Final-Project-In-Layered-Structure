package lk.ijse.posm.bo;

import lk.ijse.posm.bo.custom.impl.LoginBOImpl;

public class BOFactory {
    private static BOFactory boFactory;
    private BOFactory() {
    }
    public static BOFactory getBoFactory() {
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }
    public enum BOTypes {
        LOGIN,CUSTOMER,ITEM,PURCHASE_ORDER,
    }
    public SuperBO getBO(BOTypes types){
        switch (types) {
            case LOGIN:
                return new LoginBOImpl();
            default:
                return null;
        }
    }

}

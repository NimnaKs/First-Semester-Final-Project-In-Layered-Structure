package lk.ijse.posm.dao.custom.impl;

import javafx.scene.control.Button;
import lk.ijse.posm.controller.LoginFormController;
import lk.ijse.posm.dao.SQLUtil;
import lk.ijse.posm.dao.custom.QueryDAO;
import lk.ijse.posm.dto.ChangeDTO;
import lk.ijse.posm.dto.NewUserDTO;
import lk.ijse.posm.dto.ParcelMailDTO;
import lk.ijse.posm.dto.PostMan_EmployeeDTO;
import lk.ijse.posm.dto.TM.OrderTM;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryDAOImpl implements QueryDAO {

    @Override
    public String getUserName(String username) throws SQLException, ClassNotFoundException {
        ResultSet resultSet= SQLUtil.execute("SELECT employee_name FROM employee INNER JOIN user ON user.employee_id=employee.employee_id WHERE user_name=?",username);
        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return "N.K Sekara";
    }

    @Override
    public double getTotalSalesIncome() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT SUM(qty*item_price) FROM order_details INNER JOIN orders ON order_details.order_id=orders.order_id WHERE order_date=CURRENT_DATE()");
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return 0.0;
    }

    @Override
    public int getTotalItemSalesCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT SUM(qty) FROM order_details INNER JOIN orders ON orders.order_id=order_details.order_id WHERE order_date=CURRENT_DATE()");
        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    @Override
    public String getUserEmail(String username) throws SQLException, ClassNotFoundException {
        ResultSet resultSet= (SQLUtil.execute("SELECT employee_email FROM employee INNER JOIN user ON user.employee_id=employee.employee_id WHERE user_name=?",username));
        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    public double getTotalOrderIncomeInEachMonth(int month) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT SUM(qty*item_price) FROM order_details INNER JOIN orders ON order_details.order_id=orders.order_id WHERE MONTH(order_date)=?",month);
        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return  0.0;
    }

    @Override
    public int getTotalCustomers() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT  Count(DISTINCT orders.customer_id)FROM orders INNER JOIN customer ON customer.customer_id=orders.customer_id  WHERE orders.order_date=CURRENT_DATE()");
        if(resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    @Override
    public String getPostMan(String address) throws SQLException, ClassNotFoundException {

        ResultSet resultSet = SQLUtil.execute("SELECT employee_name FROM employee INNER JOIN postMan ON postMan.employee_id=employee.employee_id WHERE post_area=?",address);

        if (resultSet.next()){
            return resultSet.getString(1);
        }

        return null;
    }

    @Override
    public PostMan_EmployeeDTO getPostmanDetails(String postman_id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT post_area,employee_name,image FROM postMan INNER JOIN employee ON employee.employee_id = postMan.employee_id WHERE postman_id=?",postman_id);

        if (resultSet.next()){
            return new PostMan_EmployeeDTO(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3));
        }

        return new PostMan_EmployeeDTO();
    }

    @Override
    public NewUserDTO searchUser(String userId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT Type,user_id,user_name,employee_name,employee_email,employee_contact_no FROM user INNER JOIN employee ON employee.employee_id=user.employee_id WHERE user_id=?",
                LoginFormController.userId);

        if (resultSet.next()){
            return new NewUserDTO(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    null
            );
        }

        return new NewUserDTO();
    }

    @Override
    public ChangeDTO searchPayment(String paymentId) throws SQLException, ClassNotFoundException {
        ChangeDTO change=new ChangeDTO();

        change.setPaymentId(paymentId);

        ResultSet resultSet=SQLUtil.execute("SELECT customer_id,money_type,qty,date FROM changes INNER JOIN money ON changes.money_id=money.money_id WHERE payment_id=? AND status=?",
                paymentId,"Selling Money");

        if (resultSet.next()){
            change.setCustomerId(resultSet.getString(1));
            change.setSellingMoneyType(resultSet.getString(2));
            change.setSellingMoneyAmount(resultSet.getDouble(3));
            change.setPaymentDate(resultSet.getString(4));
        }

        ResultSet resultSet1=SQLUtil.execute("SELECT money_type,qty FROM changes INNER JOIN money ON changes.money_id=money.money_id WHERE payment_id=? AND status=?",
                paymentId,"Getting Money");
        if (resultSet1.next()){
            change.setReceivingMoneyType(resultSet1.getString(1));
            change.setReceivingMoneyAmount(resultSet1.getDouble(2));
        }

        return change;
    }

    @Override
    public ParcelMailDTO searchParcel(String mailId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=SQLUtil.execute("SELECT ems_parcel.mail_id,type,send_date,senders_name,sender_address,receiver_name,receivers_contact_no,receivers_address,SUM(parcel_order_details.weight),ems_parcel.price,employee_name,customer_id FROM ems_parcel INNER JOIN parcel_order_details ON ems_parcel.mail_id=parcel_order_details.mail_id INNER JOIN postMan ON postMan.postman_id=ems_parcel.postman_id INNER JOIN employee ON employee.employee_id=postMan.employee_id WHERE ems_parcel.mail_id=?",mailId);

        if (resultSet.next()){
            return new ParcelMailDTO(resultSet.getString(1),
                    resultSet.getString(2),resultSet.getString(3),
                    resultSet.getString(4),resultSet.getString(5),
                    resultSet.getString(6),resultSet.getString(7),
                    resultSet.getString(8),resultSet.getDouble(9),
                    resultSet.getDouble(10),resultSet.getString(11),resultSet.getString(12));
        }

        return new ParcelMailDTO();
    }

    @Override
    public ArrayList<OrderTM> getAllOrderDetails(String oId) throws SQLException, ClassNotFoundException {

        ResultSet resultSet=SQLUtil.execute("SELECT item.item_code,item_type,qty,item_price FROM order_details INNER JOIN item ON order_details.item_code=item.item_code WHERE order_id=?",oId);

        ArrayList<OrderTM> list=new ArrayList<>();

        while (resultSet.next()){
            String code=resultSet.getString(1);
            String description=resultSet.getString(2);
            int qty=resultSet.getInt(3);
            double unitPriced=resultSet.getDouble(4);

            OrderTM orderTm=new OrderTM(code,description,qty,unitPriced,qty*unitPriced,new Button("Remove"));

            list.add(orderTm);
        }

        return list;
    }

}

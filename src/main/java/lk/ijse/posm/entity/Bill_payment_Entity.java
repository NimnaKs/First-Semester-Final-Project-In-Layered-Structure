package lk.ijse.posm.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Bill_payment_Entity {

    private String payment_id;
    private String bill_owner_name;
    private String bill_type;
    private String company_name;
    private String reference_no;
    private String payment_date;
    private String customer_id;
    private String user_id;
    private double payment;

}

package lk.ijse.posm.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Changes_Entity {
    private String payment_id;
    private String customer_id;
    private String money_id;
    private double qty;
    private double unit_price;
    private String date;
    private String status;
}

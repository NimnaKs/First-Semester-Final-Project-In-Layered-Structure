package lk.ijse.posm.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Money_Entity {
    private String money_id;
    private String money_type;
    private double unit_selling_Price;
    private double unit_getting_price;
    private  double qty_on_hand;
}

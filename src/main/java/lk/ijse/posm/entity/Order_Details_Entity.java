package lk.ijse.posm.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Order_Details_Entity {

    private int qty;
    private double item_price;
    private String item_code;
    private String order_id;

    public Order_Details_Entity(int qty, String item_code) {
        this.qty = qty;
        this.item_code = item_code;
    }
}

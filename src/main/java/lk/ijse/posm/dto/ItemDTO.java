package lk.ijse.posm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class ItemDTO {
    private String item_code;
    private String item_type;
    private int qty_on_hand;
    private double unit_price;
    private String image;
}

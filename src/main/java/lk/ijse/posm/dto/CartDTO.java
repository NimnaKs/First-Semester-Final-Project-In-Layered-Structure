package lk.ijse.posm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class CartDTO {

    private String item_code;

    private int qty;

    private double unitPrice;

}

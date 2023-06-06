package lk.ijse.posm.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Parcel_Order_Details_Entity {
    private String mail_id;
    private String weight_id;
    private double price_per_weight_range;
    private double weight;
}

package lk.ijse.posm.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Ems_parcel_service_Entity {
    private String weight_d;
    private String weight_range;
    private String type;
    private double unit_price;
}

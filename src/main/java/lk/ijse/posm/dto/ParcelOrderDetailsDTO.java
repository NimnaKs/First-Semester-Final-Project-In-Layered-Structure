package lk.ijse.posm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class ParcelOrderDetailsDTO {

    private String weight_id;

    private double unit_price;

    private String range;

}

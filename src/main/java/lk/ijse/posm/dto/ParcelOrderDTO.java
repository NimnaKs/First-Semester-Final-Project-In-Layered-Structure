package lk.ijse.posm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class ParcelOrderDTO {

    private String weight_Id;
    private double price_per_Weight_range;
    private double weight;
}

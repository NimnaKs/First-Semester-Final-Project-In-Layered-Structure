package lk.ijse.posm.dto.TM;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class CurrencyTM {

    private String moneyType;

    private double unitSellingPrice;

    private double unitGettingPrice;

    private double qtyOnHand;

}

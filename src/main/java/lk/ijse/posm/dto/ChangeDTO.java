package lk.ijse.posm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class ChangeDTO {

    private String paymentId;

    private String paymentDate;

    private String sellingMoneyType;

    private double sellingMoneyAmount;

    private String receivingMoneyType;

    private double receivingMoneyAmount;

    private String customerId;

    public ChangeDTO(String paymentId, String paymentDate, String sellingMoneyType, double sellingMoneyAmount, String receivingMoneyType, double receivingMoneyAmount) {
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.sellingMoneyType = sellingMoneyType;
        this.sellingMoneyAmount = sellingMoneyAmount;
        this.receivingMoneyType = receivingMoneyType;
        this.receivingMoneyAmount = receivingMoneyAmount;
    }
}

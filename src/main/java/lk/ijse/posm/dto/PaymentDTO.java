package lk.ijse.posm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class PaymentDTO {
    private String payment_id;

    private String bill_owner_name;

    private String bill_type;

    private String company_name;

    private String referenceNo;

    private String payment_Date;

    private String customer_id;

    private String user_id;

    private double paymentAmount;
}

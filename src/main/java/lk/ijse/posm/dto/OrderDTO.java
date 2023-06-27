package lk.ijse.posm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class OrderDTO {

    private String orderId;

    private String date;

    private String customerId;

}

package lk.ijse.posm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class CustomerDTO {
    private String customer_id;

    private String customer_name;

    private String customer_contact;
}

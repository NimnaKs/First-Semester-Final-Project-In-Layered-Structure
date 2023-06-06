package lk.ijse.posm.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Orders_Entity {
    private String order_id;
    private String order_date;
    private String customer_id;
}

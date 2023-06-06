package lk.ijse.posm.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class User_Entity {
    private String user_id;
    private String user_name;
    private String password;
    private String type;
    private String employee_id;
}

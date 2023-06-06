package lk.ijse.posm.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Employee_Entity {
    private String employee_id;
    private String employee_name;
    private String employee_contactNo;
    private String employee_email;
    private String employee_type;
}

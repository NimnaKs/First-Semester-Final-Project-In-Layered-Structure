package lk.ijse.posm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class EmployeeDTO {
    private String employee_id;
    private String employee_name;
    private String employee_contactNo;
    private String employee_email;
    private String employee_type;
}

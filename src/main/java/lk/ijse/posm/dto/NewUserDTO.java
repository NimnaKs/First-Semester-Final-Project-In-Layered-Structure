package lk.ijse.posm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class NewUserDTO {
    private String type;

    private String userId;

    private String userName;

    private String employeeName;

    private String employeeEmailAddress;

    private String employeeContactNo;

    private String password;
    public NewUserDTO(String userName, String employeeName, String employeeEmailAddress, String employeeContactNo) {
        this.userName = userName;
        this.employeeName = employeeName;
        this.employeeEmailAddress = employeeEmailAddress;
        this.employeeContactNo = employeeContactNo;
    }
}

package lk.ijse.posm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class PostManDTO {
    private String postman_id;
    private String post_area;
    private String vehicle_no;
    private String vehicle_type;
    private String employee_id;
    private String img;
}

package lk.ijse.posm.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Mails_Entity {
    private String postman_id;
    private String mails_id;
    private String senders_name;
    private String senders_address;
    private String send_date;
    private String receivers_name;
    private String receivers_address;
    private String receiver_date;
    private String type;
}

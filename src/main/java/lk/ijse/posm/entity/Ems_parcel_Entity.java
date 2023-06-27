package lk.ijse.posm.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Ems_parcel_Entity {

    private String postman_id;

    private String customerId;

    private String user_id;

    private String mail_id;

    private String senders_Name;

    private String sender_Address;

    private String send_date;

    private String receiver_Name;

    private String receivers_address;

    private String receive_date;

    private String receivers_contact_No;

    private String mail_type;

    private double weight;

    private String status;

    private double price;

    public Ems_parcel_Entity(String mail_id, String receiver_Name, String receivers_address) {
        this.mail_id = mail_id;
        this.receiver_Name = receiver_Name;
        this.receivers_address = receivers_address;
    }


}

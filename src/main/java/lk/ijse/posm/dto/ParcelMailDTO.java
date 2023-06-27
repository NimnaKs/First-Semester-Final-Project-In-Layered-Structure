package lk.ijse.posm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class ParcelMailDTO {

    private String mail_id;

    private String mail_type;

    private String send_date;

    private String send_Name;

    private String send_Address;

    private String receiver_Name;

    private String receiver_Telephone_No;

    private String receiver_Address;

    private double weight;

    private double price;

    private String delivery_postman_id;

    private String customerId;

    public ParcelMailDTO(String mail_id, String mail_type, String send_date, String send_Name, String send_Address, String receiver_Name, String receiver_Telephone_No, String receiver_Address, double weight, double price, String delivery_postman_id) {
        this.mail_id = mail_id;
        this.mail_type = mail_type;
        this.send_date = send_date;
        this.send_Name = send_Name;
        this.send_Address = send_Address;
        this.receiver_Name = receiver_Name;
        this.receiver_Telephone_No = receiver_Telephone_No;
        this.receiver_Address = receiver_Address;
        this.weight = weight;
        this.price = price;
        this.delivery_postman_id = delivery_postman_id;
    }

}

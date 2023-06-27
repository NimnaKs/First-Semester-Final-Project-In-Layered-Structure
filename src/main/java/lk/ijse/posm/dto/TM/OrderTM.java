package lk.ijse.posm.dto.TM;


import javafx.scene.control.Button;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class OrderTM {

    private String code;

    private String description;

    private int qty;

    private double unitPriced;

    private double total;

    private Button btn;

}

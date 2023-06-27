package lk.ijse.posm.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lk.ijse.posm.dto.ItemDTO;

public class StationaryItemCard {

    @FXML
    private Label itemName;

    @FXML
    private ImageView img;

    private ItemDTO itemDTO;

    public void setData(ItemDTO itemDTO) {
        this.itemDTO = itemDTO;
        itemName.setText(itemDTO.getItem_type());
        Image image = new Image(itemDTO.getImage());
        img.setImage(image);
    }
}

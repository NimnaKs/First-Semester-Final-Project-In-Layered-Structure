package lk.ijse.posm.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import lk.ijse.posm.bo.BOFactory;
import lk.ijse.posm.bo.custom.ParcelBO;
import lk.ijse.posm.dto.TM.CuriorTM;
import lk.ijse.posm.util.ValidationPattern.RegExPatterns;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CuriorDetailsForm implements Initializable{

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<CuriorTM> parcelTable;

    @FXML
    private TableColumn<CuriorTM,String> colWeightId;

    @FXML
    private TableColumn<CuriorTM,String> colType;

    @FXML
    private TableColumn<CuriorTM,String> colDescription;

    @FXML
    private TableColumn<CuriorTM,Double> colPrice;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private JFXTextField description;

    @FXML
    private JFXTextField price;

    @FXML
    private JFXComboBox<String> parcelType;

    @FXML
    private JFXButton back;

    @FXML
    private Label dateAndTime;

    private ObservableList<CuriorTM> obList;

    private CuriorTM selectedItems;

    private boolean isDescription0k=false;

    private boolean isPriceOk=false;

    private boolean isTypeOk=false;

    ParcelBO parcelBo= (ParcelBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PARCEL);
    @FXML
    void onActionBack(ActionEvent event) throws IOException {
        URL resource = getClass().getResource("/view/curiorForm.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        root.getChildren().clear();
        root.getChildren().add(load);
    }

    @FXML
    void onActionSave(ActionEvent event) {
        CuriorTM curiorTM=null;

        if (saveBtn.getText().equals("Save")){
            boolean isSave=false;
            try {
                curiorTM= new CuriorTM(parcelBo.getWeightId(), parcelType.getValue(),description.getText(), Double.parseDouble(price.getText()));
                isSave = parcelBo.saveEmsParcelDetails(curiorTM);
                if (isSave){
                    new Alert(Alert.AlertType.CONFIRMATION,"Details Saved Successfully").showAndWait();
                }else{
                    new Alert(Alert.AlertType.WARNING,"Error In Saving Details").showAndWait();
                }
            }catch (SQLException er){
                new Alert(Alert.AlertType.ERROR, "SQL Error.Please Try Again Later!").showAndWait();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Class Not Found Error.Please Try Again Later!").showAndWait();
            }

            if (isSave){
                parcelTable.getItems().add(curiorTM);
            }
        }else{
            boolean isUpdate=false;
            curiorTM= new CuriorTM(selectedItems.getWeightId(), parcelType.getValue(),description.getText(), Double.parseDouble(price.getText()));
            try {
                isUpdate=parcelBo.updateEmsParcelDetails(curiorTM);
                if (isUpdate){
                    new Alert(Alert.AlertType.CONFIRMATION,"Details Updated Successfully").showAndWait();
                }else{
                    new Alert(Alert.AlertType.WARNING,"Error In Updating Details").showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "SQL Error.Please Try Again Later!").showAndWait();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Class Not Found Error.Please Try Again Later!").showAndWait();
            }

            if (isUpdate){
                parcelTable.getItems().add(parcelTable.getSelectionModel().getSelectedIndex(),curiorTM);
                parcelTable.getItems().remove(parcelTable.getSelectionModel().getSelectedIndex());
            }

            saveBtn.setText("Save");
            parcelType.setEditable(true);
            description.setEditable(true);
        }

        makeAllNull();
        setBooleanFalse(false);
        isButtonsEnable();
    }

    private void makeAllNull() {
        parcelType.getSelectionModel().select(null);
        price.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        price.setText(null);
        description.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        description.setText(null);
    }

    @FXML
    void onMouseClicked(MouseEvent event) {
        description.setEditable(false);
        parcelType.setEditable(false);
        selectedItems =parcelTable.getSelectionModel().getSelectedItem();
        setItemData(selectedItems);
        saveBtn.setText("Update");
    }

    private void setItemData(CuriorTM selectedItems) {
        parcelType.getSelectionModel().select(selectedItems.getType());
        description.setText(selectedItems.getDescription());
        price.setText(String.valueOf(selectedItems.getPrice()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        setTableData();
        setComboBox();
        isButtonsEnable();
    }

    private void setTableData() {
        ArrayList<CuriorTM> curiorList=null;
        try {
            curiorList=parcelBo.getAllParcelServiceDetails();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error.Please Try Again Later!").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error.Please Try Again Later!").showAndWait();
        }

        assert curiorList != null;
        obList.addAll(curiorList);
        parcelTable.setItems(obList);
    }

    private void setCellValueFactory() {
        colWeightId.setCellValueFactory(new PropertyValueFactory<>("weightId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        obList = FXCollections.observableArrayList();
        parcelTable.setItems(obList);
    }

    private void setComboBox() {

        parcelType.setValue(null);

        ObservableList<String> mailType = FXCollections.observableArrayList();

        mailType.addAll("International","Local");

        parcelType.setItems(mailType);

    }

    @FXML
    void onActionCheckDescription(KeyEvent event) {

        Pattern compile= RegExPatterns.getParcelDescriptionPattern();
        Matcher matcher=compile.matcher(description.getText());
        boolean matches=matcher.matches();
        String[] strings = description.getText().split("-");

        if (matches && (Integer.parseInt(strings[1]) > Integer.parseInt(strings[0]))){
            description.setUnFocusColor(Paint.valueOf("blue"));
            this.isDescription0k=true;
        }else{
            description.setUnFocusColor(Paint.valueOf("red"));
            this.isDescription0k=false;
        }

        isButtonsEnable();

    }

    @FXML
    void onActionCheckPrice(KeyEvent event) {

        Pattern compile= RegExPatterns.getDoublePattern();
        Matcher matcher=compile.matcher(price.getText());
        boolean matches=matcher.matches();
        if (matches){
            price.setUnFocusColor(Paint.valueOf("blue"));
            this.isPriceOk=true;
        }else{
            price.setUnFocusColor(Paint.valueOf("red"));
            this.isPriceOk=false;
        }

        isButtonsEnable();
    }

    private void isButtonsEnable() {
        setButtonDisable(!isDescription0k || !isPriceOk || !isTypeOk);
    }

    private void setButtonDisable(boolean isDisable) {
        saveBtn.setDisable(isDisable);
    }

    private void setBooleanFalse(boolean isFalse){
       isDescription0k=isFalse;
       isPriceOk=isFalse;
       isTypeOk=isFalse;
    }

    @FXML
    void onActionCheckType(ActionEvent event) {
        isTypeOk= parcelType.getValue() != null;
        isButtonsEnable();
    }
}
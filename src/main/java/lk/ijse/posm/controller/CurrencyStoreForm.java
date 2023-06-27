package lk.ijse.posm.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import lk.ijse.posm.bo.custom.CurrencyBO;
import lk.ijse.posm.dto.TM.CurrencyTM;
import lk.ijse.posm.util.ValidationPattern.RegExPatterns;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyStoreForm implements Initializable{

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<CurrencyTM> currencyStoreTable;

    @FXML
    private TableColumn<CurrencyTM,String> colMoneyType;

    @FXML
    private TableColumn<CurrencyTM,Double> colUnitSellingPrice;

    @FXML
    private TableColumn<CurrencyTM,Double> colUnitGettingPrice;

    @FXML
    private TableColumn<CurrencyTM,Integer> colQtyOnHand;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private JFXTextField moneyType;

    @FXML
    private JFXTextField unitSellingPrice;

    @FXML
    private JFXTextField unitGettingPrice;

    @FXML
    private Label dateAndTime;
    private ObservableList<CurrencyTM> obList=null;

    private CurrencyTM selectedItems;

    private boolean isUnitGettingPriceOK=false;

    private boolean isUnitSellingPriceOk=false;

    private boolean isMoneyTypeOk=false;

    CurrencyBO currencyBO= (CurrencyBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CURRENCY);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        setTableData();
        isButtonsEnable();
    }

    private void setTableData() {
        ArrayList<CurrencyTM> currencyList=null;

        try {
            currencyList= currencyBO.getAllCurrencyDetails();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error.Please Try Again Later!").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"Class Not Found  ERROR .Please Try Again Later !").showAndWait();
        }

        assert currencyList != null;
        obList.addAll(currencyList);
        currencyStoreTable.setItems(obList);
    }

    private void setCellValueFactory() {
        colMoneyType.setCellValueFactory(new PropertyValueFactory<>("moneyType"));
        colUnitSellingPrice.setCellValueFactory(new PropertyValueFactory<>("unitSellingPrice"));
        colUnitGettingPrice.setCellValueFactory(new PropertyValueFactory<>("unitGettingPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        obList = FXCollections.observableArrayList();
        currencyStoreTable.setItems(obList);
    }

    @FXML
    void onActionSave(ActionEvent event) {

        CurrencyTM currencyTM = new CurrencyTM(moneyType.getText(),Double.parseDouble(unitSellingPrice.getText()),Double.parseDouble(unitGettingPrice.getText()), 0.0);

        if (saveBtn.getText().equals("Save")) {
            boolean isSaved = false;

            try {
                isSaved = currencyBO.saveCurrencyDetails(currencyTM);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "SQL Error.Please Try Again Later!").showAndWait();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,"Class Not Found  ERROR .Please Try Again Later !").showAndWait();
            }

            if (isSaved) {
                currencyStoreTable.getItems().add(currencyTM);
            }
        }else {
            boolean isUpdate=false;

            try {
                isUpdate=currencyBO.updateCurrencyDetails(currencyTM);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "SQL Error.Please Try Again Later!").showAndWait();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,"Class Not Found  ERROR .Please Try Again Later !").showAndWait();
            }

            if (isUpdate){
                currencyTM.setQtyOnHand(selectedItems.getQtyOnHand());
                currencyStoreTable.getItems().add(currencyStoreTable.getSelectionModel().getSelectedIndex(),currencyTM);
                currencyStoreTable.getItems().remove(currencyStoreTable.getSelectionModel().getSelectedIndex());
            }

            saveBtn.setText("Save");
            moneyType.setEditable(true);
        }

        makeAllNull();
        setBooleanFalse(false);
        isButtonsEnable();
    }

    private void makeAllNull() {

        moneyType.setText(null);
        moneyType.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        unitSellingPrice.setText(null);
        unitSellingPrice.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        unitGettingPrice.setText(null);
        unitGettingPrice.setUnFocusColor(Paint.valueOf("#4d4d4d"));

    }

    @FXML
    void onActionUpdateRow(MouseEvent event) {
        moneyType.setEditable(false);
        selectedItems = currencyStoreTable.getSelectionModel().getSelectedItem();
        setItemData(selectedItems);
        saveBtn.setText("Update");
    }

    private void setItemData(CurrencyTM selectedItems) {

        moneyType.setText(selectedItems.getMoneyType());
        unitSellingPrice.setText(selectedItems.getUnitSellingPrice()+"");
        unitGettingPrice.setText(selectedItems.getUnitGettingPrice()+"");

    }

    private void isButtonsEnable() {
        if (isMoneyTypeOk&&isUnitGettingPriceOK&&isUnitSellingPriceOk){
            setButtonDisable(false);
        }else{
            setButtonDisable(true);
        }
    }

    private void setButtonDisable(boolean isDisable) {
        saveBtn.setDisable(isDisable);
    }

    private void setBooleanFalse(boolean isFalse){
        isMoneyTypeOk=isFalse;
        isUnitGettingPriceOK=isFalse;
        isUnitSellingPriceOk=isFalse;
    }

    @FXML
    void onActionUnitGettingPrice(KeyEvent event) {
        Pattern compile= RegExPatterns.getDoublePattern();
        Matcher matcher=compile.matcher(unitGettingPrice.getText());
        boolean matches=matcher.matches();
        if (matches){
            unitGettingPrice.setUnFocusColor(Paint.valueOf("blue"));
            this.isUnitGettingPriceOK=true;
        }else{
            unitGettingPrice.setUnFocusColor(Paint.valueOf("red"));
            this.isUnitGettingPriceOK=false;
        }

        isButtonsEnable();
    }

    @FXML
    void onActionUnitSellingPrice(KeyEvent event) {
        Pattern compile= RegExPatterns.getDoublePattern();
        Matcher matcher=compile.matcher(unitSellingPrice.getText());
        boolean matches=matcher.matches();
        if (matches){
            unitSellingPrice.setUnFocusColor(Paint.valueOf("blue"));
            this.isUnitSellingPriceOk=true;
        }else{
            unitSellingPrice.setUnFocusColor(Paint.valueOf("red"));
            this.isUnitSellingPriceOk=false;
        }

        isButtonsEnable();
    }

    @FXML
    void onActionMoneyType(KeyEvent actionEvent) {
        if (moneyType.getText()!=null) {
            Pattern compile = RegExPatterns.getTwoStringCheckPattern();
            Matcher matcher = compile.matcher(moneyType.getText());
            boolean matches = matcher.matches();

            if (matches) {
                moneyType.setUnFocusColor(Paint.valueOf("blue"));
                isMoneyTypeOk = true;
            } else {
                moneyType.setUnFocusColor(Paint.valueOf("red"));
                isMoneyTypeOk = false;
            }
            isButtonsEnable();
        }
    }
}

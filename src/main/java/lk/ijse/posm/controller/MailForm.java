package lk.ijse.posm.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import lk.ijse.posm.bo.BOFactory;
import lk.ijse.posm.bo.custom.MailBO;
import lk.ijse.posm.db.DBConnection;
import lk.ijse.posm.dto.MailDTO;
import lk.ijse.posm.util.ValidationPattern.RegExPatterns;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailForm implements Initializable {

    @FXML
    private JFXButton saveBtn;

    @FXML
    private Label mailId;

    @FXML
    private JFXComboBox<String> mailTypeComboBox;

    @FXML
    private Label sendDate;

    @FXML
    private Label postmanName;

    @FXML
    private Label deliveredDate;

    @FXML
    private JFXTextField senderAddress;

    @FXML
    private JFXTextField receiversName;

    @FXML
    private JFXTextField receiversAddress;

    @FXML
    private JFXTextField senderName;

    @FXML
    private JFXButton removeBtn;

    @FXML
    private JFXButton searchBtn;

    @FXML
    private TextField search;

    @FXML
    private JFXButton todayMailsDetails;

    @FXML
    private Label dateAndTime;
    private boolean isSendersNameOk=false;

    private boolean isSendersAddressOk=false;

    private boolean isReceiversNameOk=false;

    private boolean isReceiversAddressOk=false;

    private String newMailId=null;

    MailBO mailBO= (MailBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.MAIL);

    @FXML
    void onActionSearch(ActionEvent event) {
        boolean isCorrectIds= false;
        try {
            isCorrectIds = mailBO.isIdExists(search.getText());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
        }
        if (!isCorrectIds){
            search.setText("Wrong MailId Id Or Null Id ");
            makeAllNull();
        } else {
            try {
                MailDTO mail = mailBO.searchData(search.getText());
                mailId.setText(mail.getMails_id());
                mailTypeComboBox.setValue(mail.getType());
                sendDate.setText(mail.getSend_date());
                senderName.setText(mail.getSenders_name());
                senderAddress.setText(mail.getSenders_address());
                receiversName.setText(mail.getReceivers_name());
                receiversAddress.setText(mail.getReceivers_address());
                String[] strings = receiversAddress.getText().split(",");
                String receiversAddresses=strings[strings.length-1];
                postmanName.setText(mailBO.getPostMan((mail.getType().equals("International"))?"Air port":receiversAddresses));
                deliveredDate.setText(mail.getReceiver_date());
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").show();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
            }
            saveBtn.setText("Update");
            setBooleanFalse(true);
            isButtonsEnable();
        }
    }

    @FXML
    void onActionGetPostManName(ActionEvent event) throws NullPointerException {
        try {
        if (mailTypeComboBox.getValue().equals("International")){
            postmanName.setText(mailBO.getPostMan("Air port"));
        }else{
            String[] strings = receiversAddress.getText().split(",");
            String receiversAddresses=strings[strings.length-1];
            postmanName.setText(mailBO.getPostMan(receiversAddresses));
        }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
        }

        Pattern compile= RegExPatterns.getAddressPattern();
        Matcher matcher=compile.matcher(receiversAddress.getText());
        boolean matches=matcher.matches();

        boolean isPostManNameEmpty=false;

        try {
            isPostManNameEmpty=(postmanName.getText().isEmpty())?true:false;
        }catch (NullPointerException exception){
            exception.printStackTrace();
        }

        if (!isPostManNameEmpty && matches){
            isReceiversAddressOk=true;
            receiversAddress.setUnFocusColor(Paint.valueOf("blue"));
        }else {
            isReceiversAddressOk=false;
            receiversAddress.setUnFocusColor(Paint.valueOf("red"));
        }

        isButtonsEnable();
    }

    @FXML
    void onActionRemove(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do u really want to remove the Mail Details ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if ((alert.getResult() == ButtonType.YES )&& (!newMailId.equals(mailId.getText()))) {
            boolean isRemoved=false;
            try {
              isRemoved=mailBO.removeMailDetails(mailId.getText());
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").show();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
            }
            if (isRemoved){
                new Alert(Alert.AlertType.CONFIRMATION,"Mail Details Removed SuccessFully.").showAndWait();
            }else{
                new Alert(Alert.AlertType.WARNING, "Error happened in Removing Process.").showAndWait();
            }
        }
        saveBtn.setText("Save");
        makeAllNull();
        setBooleanFalse(false);
        isButtonsEnable();
    }

    @FXML
    void onActionSave(ActionEvent event) {

        if(saveBtn.getText().equals("Save") ){
            saveDetails();
        }else {
            updateDetails();
        }

        setBooleanFalse(false);
        isButtonsEnable();

    }

    private void updateDetails() {
        String[] strings = receiversAddress.getText().split(",");
        String receiversAddresses=strings[strings.length-1];
        boolean isUpdated=false;
        try {
           isUpdated=mailBO.updateDetails(new MailDTO(mailBO.getPostManId((mailTypeComboBox.getValue().equals("International"))?"Air port":receiversAddresses),
                    mailId.getText(),
                    senderName.getText(),
                    senderAddress.getText(),
                    LocalDate.now().toString(),
                    receiversName.getText(),
                    receiversAddress.getText(),
                    null,
                    mailTypeComboBox.getValue()));
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"Mail Details Updated SuccessFully.").showAndWait();
            }else{
                new Alert(Alert.AlertType.WARNING, "Error happened in Updating Process.").showAndWait();
            }
            makeAllNull();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
        }
        saveBtn.setText("Save");
    }

    private void saveDetails() {
        String[] strings = receiversAddress.getText().split(",");
        String receiversAddresses=strings[strings.length-1];
        boolean isSaved=false;
        try {
            isSaved=mailBO.saveMailDetails(new MailDTO(mailBO.getPostManId((mailTypeComboBox.getValue().equals("International"))?"Air port":receiversAddresses),
                    mailId.getText(),
                    senderName.getText(),
                    senderAddress.getText(),
                    String.valueOf(LocalDate.now()),
                    receiversName.getText(),
                    receiversAddress.getText(),
                    null,
                    mailTypeComboBox.getValue())
            );
            if (isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"Mail Details Saved SuccessFully.").showAndWait();
            }else{
                new Alert(Alert.AlertType.WARNING, "Error happened in Saving Process.").showAndWait();
            }

            makeAllNull();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
        }
    }

    @FXML
    void onActionTodayMailDetails(ActionEvent event) throws FileNotFoundException, JRException, SQLException, ClassNotFoundException {
        if(mailBO.isTodayMailsOk()) {
            InputStream input = new FileInputStream(new File("src/main/resources/report/todayMailDetails.jrxml"));
            JasperDesign jasperDesign = JRXmlLoader.load(input);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getDbConnection().getConnection());

            JasperViewer.viewReport(jasperPrint, false);
        }else{
            new Alert(Alert.AlertType.WARNING,"Still No Mails Information to Show.").showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Generate Mail_Id
        generateMailId();

        //Set Send Date
        setDate();

        //set Mail Type Combo Box
        setComboBox();

        isButtonsEnable();

    }

    private void generateMailId() {
        try {
            newMailId=mailBO.generateNextMailId();
            mailId.setText(newMailId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
        }
    }

    private void setDate() {
        sendDate.setText(String.valueOf(LocalDate.now()));
    }

    private void setComboBox() {

        mailTypeComboBox.setValue(null);

        ObservableList<String> mailType = FXCollections.observableArrayList();

        mailType.addAll("International","Local");

        mailTypeComboBox.setItems(mailType);

    }

    private void makeAllNull() {
        senderName.setText(null);
        senderName.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        senderAddress.setText(null);
        senderAddress.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        receiversName.setText(null);
        receiversName.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        receiversAddress.setText(null);
        receiversAddress.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        setComboBox();
        generateMailId();
        postmanName.setText(null);
        deliveredDate.setText(null);
        search.setText(null);
    }

    @FXML
    void onActionSenderName(KeyEvent event) {
        Pattern compile1= RegExPatterns.getNamePattern();
        Matcher matcher=compile1.matcher(senderName.getText());
        boolean matches1=matcher.matches();

        Pattern compile2=RegExPatterns.getTwoStringCheckPattern();
        matcher=compile2.matcher(senderName.getText());
        boolean matches2=matcher.matches();

        if (matches1 && matches2){
            senderName.setUnFocusColor(Paint.valueOf("blue"));
            isSendersNameOk=true;
        }else{
            senderName.setUnFocusColor(Paint.valueOf("red"));
            isSendersNameOk=false;
        }

        isButtonsEnable();
    }

    private void isButtonsEnable() {
        if (isSendersNameOk&&isReceiversAddressOk&&isReceiversNameOk&&isSendersAddressOk){
            setButtonDisable(false);
        }else{
            setButtonDisable(true);
        }
    }

    private void setButtonDisable(boolean isDisable) {
        removeBtn.setDisable(isDisable);
        saveBtn.setDisable(isDisable);
    }

    private void setBooleanFalse(boolean isFalse){
        isSendersAddressOk=isFalse;
        isReceiversNameOk=isFalse;
        isReceiversAddressOk=isFalse;
        isSendersAddressOk=isFalse;
    }

    @FXML
    void onActionSendersAddress(KeyEvent event) {
        Pattern compile= RegExPatterns.getAddressPattern();
        Matcher matcher=compile.matcher(senderAddress.getText());
        boolean matches=matcher.matches();

        if (matches){
            senderAddress.setUnFocusColor(Paint.valueOf("blue"));
            isSendersAddressOk=true;
        }else{
            senderAddress.setUnFocusColor(Paint.valueOf("red"));
            isSendersAddressOk=false;
        }

        isButtonsEnable();
    }

    @FXML
    void onActionReceiversName(KeyEvent event) {
        Pattern compile1= RegExPatterns.getNamePattern();
        Matcher matcher=compile1.matcher(receiversName.getText());
        boolean matches1=matcher.matches();

        Pattern compile2=RegExPatterns.getTwoStringCheckPattern();
        matcher=compile2.matcher(receiversName.getText());
        boolean matches2=matcher.matches();

        if (matches1 && matches2){
            receiversName.setUnFocusColor(Paint.valueOf("blue"));
            isReceiversNameOk=true;
        }else{
            receiversName.setUnFocusColor(Paint.valueOf("red"));
            isReceiversNameOk=false;
        }

        isButtonsEnable();
    }

}

package lk.ijse.posm.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import lk.ijse.posm.bo.BOFactory;
import lk.ijse.posm.bo.custom.UserBO;
import lk.ijse.posm.dto.NewUserDTO;
import lk.ijse.posm.util.ValidationPattern.RegExPatterns;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddUser implements Initializable{

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField employeeName;

    @FXML
    private JFXTextField emailAddress;

    @FXML
    private JFXTextField contactNo;

    @FXML
    private JFXTextField userName;

    @FXML
    private JFXTextField password;

    @FXML
    private JFXButton save;

    @FXML
    private JFXComboBox<String> userRollComboBox;

    @FXML
    private Label userId;

    @FXML
    private Label dateAndTime;

    @FXML
    private JFXPasswordField passwordPWF;

    @FXML
    private Label lblEyeClose;

    @FXML
    private Label lblEyeOpen;

    @FXML
    private Label userPasswordError;

    @FXML
    private Label userNameError;

    private volatile boolean stop=false;

    private String passWord=null;

    private boolean isUserNameOk=false;

    private boolean isEmployeeNameOk=false;

    private boolean isEmailOk=false;

    private boolean isTpOk=false;

    private boolean isPasswordOk=false;

    private boolean isUserRollOk=false;

    UserBO userBO= (UserBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.LOGIN);

    @FXML
    void close_eye_clicked(MouseEvent event) {
        lblEyeClose.setVisible(false);
        lblEyeOpen.setVisible(true);
        password.setVisible(true);
        passwordPWF.setVisible(false);
    }

    @FXML
    void hidePasswordNewOnAction(KeyEvent event) {
        passWord=passwordPWF.getText();
        password.setText(passWord);
        Pattern compile= RegExPatterns.getUserPWPattern();
        Matcher matcher=compile.matcher(passwordPWF.getText());
        boolean matches=matcher.matches();
        compile=RegExPatterns.getUserPWPattern();
        matcher=compile.matcher(password.getText());
        boolean matches1=matcher.matches();
        if (matches&&matches1&&checkCPW(passWord)){
            passwordPWF.setUnFocusColor(Paint.valueOf("blue"));
            password.setUnFocusColor(Paint.valueOf("blue"));
            this.isPasswordOk=true;
            userPasswordError.setVisible(false);
        }else{
            passwordPWF.setUnFocusColor(Paint.valueOf("red"));
            password.setUnFocusColor(Paint.valueOf("red"));
            this.isPasswordOk=false;
            userPasswordError.setVisible(true);
        }
        isButtonsEnable();
    }

    @FXML
    void onActionUserRoll(ActionEvent event) {
        isUserRollOk= userRollComboBox.getValue() != null;
        isButtonsEnable();
    }

    @FXML
    void onContactNoCheck(KeyEvent event) {
        Pattern compile= RegExPatterns.getMobilePattern();
        Matcher matcher=compile.matcher(contactNo.getText());
        boolean matches=matcher.matches();
        if (matches){
            contactNo.setUnFocusColor(Paint.valueOf("blue"));
            this.isTpOk=true;
        }else{
            contactNo.setUnFocusColor(Paint.valueOf("red"));
            this.isTpOk=false;
        }
        isButtonsEnable();
    }

    @FXML
    void onEmailCheck(KeyEvent event) {
        Pattern compile= RegExPatterns.getEmailPattern();
        Matcher matcher=compile.matcher(emailAddress.getText());
        boolean matches=matcher.matches();
        if (matches){
            emailAddress.setUnFocusColor(Paint.valueOf("blue"));
            this.isEmailOk=true;
        }else{
            emailAddress.setUnFocusColor(Paint.valueOf("red"));
            this.isEmailOk=false;
        }
        isButtonsEnable();
    }

    @FXML
    void onEmployeeNameCheck(KeyEvent event) {
        Pattern compile1= RegExPatterns.getNamePattern();
        Matcher matcher=compile1.matcher(employeeName.getText());
        boolean matches1=matcher.matches();

        Pattern compile2=RegExPatterns.getTwoStringCheckPattern();
        matcher=compile2.matcher(employeeName.getText());
        boolean matches2=matcher.matches();

        if (matches1 && matches2){
            employeeName.setUnFocusColor(Paint.valueOf("blue"));
            isEmployeeNameOk=true;
        }else{
            employeeName.setUnFocusColor(Paint.valueOf("red"));
            isEmployeeNameOk=false;
        }

        isButtonsEnable();
    }

    @FXML
    void onUserNameCheck(KeyEvent event) {
        Pattern compile= RegExPatterns.getUserNamePattern();
        Matcher matcher=compile.matcher(userName.getText());
        boolean matches=matcher.matches();
        if (matches && checkUserNameMatch(userName.getText())){
            userName.setUnFocusColor(Paint.valueOf("blue"));
            this.isUserNameOk=true;
            userNameError.setVisible(false);
        }else{
            userName.setUnFocusColor(Paint.valueOf("red"));
            this.isUserNameOk=false;
            userNameError.setVisible(true);
        }
        isButtonsEnable();
    }

    private boolean checkUserNameMatch(String userName) {
        try {
            return !userBO.checkUserName(userName);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error or SQL Error ! Please Try Again Later.").showAndWait();
        }

        return false;
    }

    private boolean checkCPW(String currentPassword){

        try {
            return !userBO.isNewPasswordExists(currentPassword);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error or SQL Error ! Please Try Again Later.").showAndWait();
        }

        return false;
    }

    @FXML
    void open_eye_clicked(MouseEvent event) {
        lblEyeClose.setVisible(true);
        lblEyeOpen.setVisible(false);
        password.setVisible(false);
        passwordPWF.setVisible(true);
    }

    @FXML
    void showPasswordOnAction(KeyEvent event) {
        passWord=password.getText();
        passwordPWF.setText(passWord);
        Pattern compile= RegExPatterns.getUserPWPattern();
        Matcher matcher=compile.matcher(password.getText());
        boolean matches=matcher.matches();
        compile=RegExPatterns.getUserPWPattern();
        matcher=compile.matcher(passwordPWF.getText());
        boolean matches1=matcher.matches();
        if (matches&&matches1&&checkCPW(passWord)){
            password.setUnFocusColor(Paint.valueOf("blue"));
            passwordPWF.setUnFocusColor(Paint.valueOf("blue"));
            this.isPasswordOk=true;
            userPasswordError.setVisible(false);
        }else{
            password.setUnFocusColor(Paint.valueOf("red"));
            passwordPWF.setUnFocusColor(Paint.valueOf("red"));
            this.isPasswordOk=false;
            userPasswordError.setVisible(true);
        }
        isButtonsEnable();
    }

    @FXML
    public void onActionBackBtn(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/settingsForm.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        root.getChildren().clear();
        root.getChildren().add(load);
    }

    @FXML
    void onActionSave(ActionEvent event) {
        boolean isSaved=false;
        try {
            isSaved=userBO.saveNewUser(new NewUserDTO(userRollComboBox.getValue(),userId.getText(),userName.getText(),
                    employeeName.getText(),emailAddress.getText(),contactNo.getText(),password.getText()
            ));
            if (isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"User Saved Successfully.").showAndWait();
                makeAllNull();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"SQL ERROR .Please Try Again Later !").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"Class Not Found ERROR .Please Try Again Later !").showAndWait();
        }

        setBooleanFalse(false);
        isButtonsEnable();
    }

    private void makeAllNull() {
        generateNextUserId();
        userRollComboBox.getSelectionModel().select(null);
        userName.setText(null);
        userName.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        employeeName.setText(null);
        employeeName.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        emailAddress.setText(null);
        emailAddress.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        contactNo.setText(null);
        contactNo.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        password.setText(null);
        password.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        passwordPWF.setText(null);
        passwordPWF.setUnFocusColor(Paint.valueOf("#4d4d4d"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Set User Roll Combo Box
        setUserRollComboBox();

        //generate next UserId
        generateNextUserId();

        lblEyeOpen.setVisible(false);
        password.setVisible(false);

        setBooleanFalse(false);
        isButtonsEnable();

        userNameError.setVisible(false);
        userPasswordError.setVisible(false);
    }

    private void generateNextUserId() {
        try {
            userId.setText(userBO.generateNextUserId());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").show();
        }
    }

    private void setUserRollComboBox() {
        ObservableList<String> userRollType = FXCollections.observableArrayList();

        userRollType.addAll("Manager","Admin");

        userRollComboBox.setItems(userRollType);

    }

    private void isButtonsEnable() {
        setButtonDisable(!isEmailOk || !isEmployeeNameOk || !isPasswordOk || !isTpOk || !(isUserNameOk & isUserRollOk));
    }

    private void setButtonDisable(boolean isDisable) {
        save.setDisable(isDisable);
    }

    private void setBooleanFalse(boolean isFalse){
       isUserNameOk=isFalse;
       isTpOk=isFalse;
       isEmployeeNameOk=isFalse;
       isPasswordOk=isFalse;
       isEmailOk=isFalse;
       isUserRollOk=isFalse;
    }
}



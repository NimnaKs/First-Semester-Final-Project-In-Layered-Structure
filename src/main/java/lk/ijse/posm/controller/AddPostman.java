package lk.ijse.posm.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import lk.ijse.posm.bo.BOFactory;
import lk.ijse.posm.bo.custom.EmployeeBO;
import lk.ijse.posm.bo.custom.PostManBO;
import lk.ijse.posm.dto.EmployeeDTO;
import lk.ijse.posm.dto.PostManDTO;
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

public class AddPostman implements Initializable{

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField employeeName;

    @FXML
    private JFXTextField emailAddress;

    @FXML
    private JFXTextField contactNo;

    @FXML
    private JFXTextField employeeImg;

    @FXML
    private ImageView imgView;

    @FXML
    private JFXTextField postArea;

    @FXML
    private JFXTextField vehicleNo;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private Label postId;

    @FXML
    private JFXComboBox<String> vehicleType;

    @FXML
    private Label dateAndTime;
    private PostManDTO postManDTO;

    private EmployeeDTO employeeDTO;

    private boolean isEmployeeNameOk=false;

    private boolean isEmployeeImgOk=false;

    private boolean isEmailAddressOk=false;

    private boolean isTpOk=false;

    private boolean isPostAreaOk=false;

    private boolean isVehicleNoOk=false;

    private boolean isVehicleTypeOk=false;

    private String postManArea=null;

    PostManBO postManBO= (PostManBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.POSTMAN);
    @FXML
    void onActionSave(ActionEvent event) {

        if (saveBtn.getText().equals("Save")){
            boolean isSaved=false;
            try {
                isSaved=postManBO.savePostManAndEmployeeDetails(new EmployeeDTO(
                        null,
                        employeeName.getText(),
                        contactNo.getText(),
                        emailAddress.getText(),
                        "Postmen"),
                        new PostManDTO(postId.getText(),
                                postArea.getText(),
                                vehicleNo.getText(),
                                vehicleType.getValue(),
                                null,
                                employeeImg.getText()));
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
            }

            if (isSaved){
                new Alert(Alert.AlertType.CONFIRMATION, "Postman Saved Successfully!").showAndWait();
            }else{
                new Alert(Alert.AlertType.WARNING, "Postman Doesn't Saved Successfully!").showAndWait();
            }
        }else{
            boolean isUpdated=false;
            try {
                isUpdated=postManBO.updatePostManAndEmployeeDetails(new EmployeeDTO(
                                employeeDTO.getEmployee_id(),
                                employeeName.getText(),
                                contactNo.getText(),
                                emailAddress.getText(),
                                "Postman"),
                        new PostManDTO(postId.getText(),
                                postArea.getText(),
                                vehicleNo.getText(),
                                vehicleType.getValue(),
                                employeeDTO.getEmployee_id(),
                                employeeImg.getText()));
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
            }

            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION, "Postman Updated Successfully!").showAndWait();
            }else{
                new Alert(Alert.AlertType.WARNING, "Postman Doesn't Updated Successfully!").showAndWait();
            }
        }

        setBooleanFalse(false);
        isButtonsEnable();
        makeAllNull();

    }

    private void makeAllNull() {
        employeeName.setText(null);
        employeeName.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        emailAddress.setText(null);
        emailAddress.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        contactNo.setText(null);
        contactNo.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        employeeImg.setText(null);
        employeeImg.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        Image image = new Image("/assert/Employeee.png");
        imgView.setImage(image);
        postArea.setText(null);
        postArea.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        vehicleType.getSelectionModel().select(null);
        vehicleNo.setText(null);
        vehicleNo.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        setNewPostManId();

    }

    public void onActionBack(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/employeeForm.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        root.getChildren().clear();
        root.getChildren().add(load);
        EmployeeForm.isNew=false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (EmployeeForm.isNew){
            setItemsForNewPostman();
            saveBtn.setText("Save");
        }else{
            setCurrentPostMan();
            saveBtn.setText("Update");
        }

        setBooleanFalse(false);
        isButtonsEnable();
    }

    private void setCurrentPostMan() {
        //set VehicleComboBox
        setVehicleComboBox();

        try {
            postManDTO=postManBO.getPostmanDetails(EmployeeForm.postmanId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
        }

        try {
            employeeDTO=postManBO.getEmployeeDetails(postManDTO.getEmployee_id());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
        }

        postId.setText(postManDTO.getPostman_id());
        employeeName.setText(employeeDTO.getEmployee_name());
        if (postManDTO.getImg()!=null) {
            try {
                Image image = new Image(postManDTO.getImg());
                imgView.setImage(image);
                employeeImg.setText(postManDTO.getImg());
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
        emailAddress.setText(employeeDTO.getEmployee_email());
        contactNo.setText(employeeDTO.getEmployee_contactNo());
        postArea.setText(postManDTO.getPost_area());
        vehicleNo.setText(postManDTO.getVehicle_no());
        vehicleType.getSelectionModel().select(postManDTO.getVehicle_type());
        postManArea=postArea.getText();

    }

    private void setItemsForNewPostman() {

        //set VehicleComboBox
        setVehicleComboBox();

        //set PostmanId
        setNewPostManId();


    }

    private void setNewPostManId() {
        try {
            postId.setText(postManBO.generateNextPostId());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
        }
    }

    private void setVehicleComboBox() {

        ObservableList<String> vehicleTypeList = FXCollections.observableArrayList();

        vehicleTypeList.addAll("Van","Bike","ThreeWheel","PaddleBike","Lorry");

        vehicleType.setItems(vehicleTypeList);
    }

    @FXML
    void onActionSetImg(ActionEvent event) {

        Image image = new Image(employeeImg.getText());
        this.imgView.setImage(image);
    }

    @FXML
    void onActionCheckEmail(KeyEvent event) {
        Pattern compile= RegExPatterns.getEmailPattern();
        Matcher matcher=compile.matcher(emailAddress.getText());
        boolean matches=matcher.matches();
        if (matches){
            emailAddress.setUnFocusColor(Paint.valueOf("blue"));
            this.isEmailAddressOk=true;
        }else{
            emailAddress.setUnFocusColor(Paint.valueOf("red"));
            this.isEmailAddressOk=false;
        }
        isButtonsEnable();
    }

    @FXML
    void onActionCheckEmployeeImg(KeyEvent event) {
        String imgEmp=employeeImg.getText();
        if (imgEmp.endsWith(".png") || imgEmp.endsWith(".jpg") ){
            employeeImg.setUnFocusColor(Paint.valueOf("blue"));
            this.isEmployeeImgOk=true;
        }else{
            employeeImg.setUnFocusColor(Paint.valueOf("red"));
            this.isEmployeeImgOk=false;
        }
        isButtonsEnable();
    }

    @FXML
    void onActionCheckEmployeeName(KeyEvent event) {
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
    void onActionCheckPostArea(KeyEvent event) {
        Pattern compile= RegExPatterns.getReferenceTypePattern();
        Matcher matcher=compile.matcher(postArea.getText());
        boolean matches=matcher.matches();
        if (matches&&getMatchPostArea(postArea.getText())){
            postArea.setUnFocusColor(Paint.valueOf("blue"));
            this.isPostAreaOk=true;
        }else{
            postArea.setUnFocusColor(Paint.valueOf("red"));
            this.isPostAreaOk=false;
        }
        isButtonsEnable();
    }

    private boolean getMatchPostArea(String postAreaName) {
        ArrayList<String> postAreaList=null;
        try {
            postAreaList=postManBO.getPostAreas();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
        }
        assert postAreaList != null;
        for (String postAea :postAreaList){
            if (!EmployeeForm.isNew && postAea.equals(postManArea)){
                continue;
            }else if(postAea.equals(postAreaName)){
                return false;
            }
        }
        return true;
    }

    @FXML
    void onActionCheckTp(KeyEvent event) {
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
    void onActionCheckVehicleType(ActionEvent event) {
        isVehicleTypeOk= vehicleType.getValue() != null;
        isButtonsEnable();
    }

    @FXML
    void onActionVehicleNo(KeyEvent event) {
        Pattern compile= RegExPatterns.getReferenceTypePattern();
        Matcher matcher=compile.matcher(vehicleNo.getText());
        boolean matches=matcher.matches();
        if (matches){
            vehicleNo.setUnFocusColor(Paint.valueOf("blue"));
            this.isVehicleNoOk=true;
        }else{
            vehicleNo.setUnFocusColor(Paint.valueOf("red"));
            this.isVehicleNoOk=false;
        }

        isButtonsEnable();
    }

    private void isButtonsEnable() {
        setButtonDisable(isEmployeeNameOk&&isEmployeeImgOk&&isEmailAddressOk&&isTpOk&&isPostAreaOk&&isPostAreaOk&&isTpOk&&isVehicleNoOk&&isVehicleTypeOk);
    }

    private void setButtonDisable(boolean isDisable) {
        saveBtn.setDisable(!isDisable);
    }

    private void setBooleanFalse(boolean isFalse){
        isVehicleNoOk=isFalse;
        isVehicleTypeOk=isFalse;
        isTpOk=isFalse;
        isPostAreaOk=isFalse;
        isEmailAddressOk=isFalse;
        isEmployeeImgOk=isFalse;
        isEmployeeNameOk=isFalse;
    }

}

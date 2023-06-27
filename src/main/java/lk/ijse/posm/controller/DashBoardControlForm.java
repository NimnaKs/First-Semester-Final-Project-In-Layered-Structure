package lk.ijse.posm.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardControlForm implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private AnchorPane root1;

    @FXML
    private JFXButton product;

    @FXML
    private JFXButton order;
    
    @FXML
    private JFXButton mails;

    @FXML
    private JFXButton curior;

    @FXML
    private JFXButton utilityBills;

    @FXML
    private JFXButton currencyStore;

    @FXML
    private JFXButton MoneyTransfer;

    @FXML
    private JFXButton employee;

    @FXML
    private JFXButton settings;

    @FXML
    private JFXButton signOut;

    @FXML
    private JFXButton dashboard;

    @FXML
    void onActionCurior(ActionEvent event) throws IOException {
        setForms("/view/curiorForm.fxml");
    }

    @FXML
    void onActionCurrencyStore(ActionEvent event) throws IOException {
        setForms("/view/currencyStoreForm.fxml");
    }

    @FXML
    void onActionEmployee(ActionEvent event) throws IOException {
        setForms("/view/employeeForm.fxml");
    }

    @FXML
    void onActionMails(ActionEvent event) throws IOException {
        setForms("/view/mailForm.fxml");
    }

    @FXML
    void onActionMonetTransfer(ActionEvent event) throws IOException {
        setForms("/view/moneyTransferForm.fxml");
    }

    @FXML
    void onActionOrder(ActionEvent event) throws IOException {
        setForms("/view/orderForm.fxml");
    }

    @FXML
    void onActionSettings(ActionEvent event) throws IOException {
        setForms("/view/settingsForm.fxml");
    }

    @FXML
    void onActionSignOut(ActionEvent event) throws IOException {
//        Thread thread=null;
//        try {
//            Mail mail = new Mail("Hii Mr. "/*+ UserModel.getUserName(LoginFormController.userNameAvailable)*/+".\nYou Log out from Post office Kalutara System",/*UserModel.getUsreEmail(LoginFormController.userNameAvailable)*/,
//                    "Logout Information", "/Users/mac/Desktop/Post Office Management System 2/src/main/resources/assert/ReviewBackgroundForPdf.png");
//            mail.outMail();
//            thread=new Thread(mail);
//            thread.start();
//        } catch (SQLException | MessagingException e) {
//            e.printStackTrace();
//        }
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/welcomeForm.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) root1.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Post Office Management System");
        stage.centerOnScreen();
        //setClockStop("/view/welcomeForm.fxml");

    }

    @FXML
    void onActionUtilityBills(ActionEvent event) throws IOException {
        setForms("/view/utilityBillsForm.fxml");
    }

    @FXML
    void onActiondashboardLoad(ActionEvent event) throws IOException {
        setForms("/view/dashBoardForm.fxml");
    }

    @FXML
    void onActionproduct(ActionEvent event) throws IOException {
        setForms("/view/productForm.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setForms("/view/dashBoardForm.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setForms(String FormFxml) throws IOException {
        URL resource = getClass().getResource(FormFxml);
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        root.getChildren().clear();
        root.getChildren().add(load);
    }

}

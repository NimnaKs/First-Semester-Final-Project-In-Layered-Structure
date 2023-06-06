package lk.ijse.posm.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import lk.ijse.posm.bo.BOFactory;
import lk.ijse.posm.bo.custom.LoginBO;
import lk.ijse.posm.bo.custom.impl.LoginBOImpl;
import lk.ijse.posm.dto.UserDTO;
import lk.ijse.posm.util.MailPack.Mail;
import lk.ijse.posm.util.ValidationPattern.RegExPatterns;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFormController implements Initializable {

    @FXML
    public JFXTextField userName;

    @FXML
    public JFXPasswordField passwordHide;

    @FXML
    public JFXTextField passwordShow;

    @FXML
    public Label lblEyeClose;

    @FXML
    public Label lblEyeOpen;

    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton login;

    @FXML
    private Label forgetPassword;

    @FXML
    private Label userNameError;

    @FXML
    private Label userPasswordError;

    LoginBO loginBO= (LoginBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.LOGIN);

    private String password=null;

    @FXML
    void onActionLogin(ActionEvent event) throws IOException {

        String passwords = passwordHide.getText();
        String username = userName.getText();

        try {

            if (passwords != null || username != null) {
                if (loginBO.checkCredentials(new UserDTO(username, passwords))) {

//            Thread thread=null;
//            try {
//                checkUserNameMatch(userName.getText());
//                Mail mail = new Mail("Hii Mr. "+UserModel.getUserName(userNameAvailable)+".\nYou Log In to Post office Kalutara", UserModel.getUsreEmail(userNameAvailable),
//                        "Login Information", "/Users/mac/Desktop/Post Office Management System 2/src/main/resources/assert/ReviewBackgroundForPdf.png");
//                mail.outMail();
//                thread=new Thread(mail);
//                thread.start();
//            } catch (SQLException | MessagingException e) {
//                e.printStackTrace();
//            }

                    AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/dashBoardControlForm.fxml"));
                    Scene scene = new Scene(anchorPane);
                    Stage stage = (Stage) root.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle("Post Office Management System");
                    stage.centerOnScreen();
                    stage.setFullScreen(true);

//            thread.interrupt();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Your Entered Password Or UserName is Wrong !").show();
                }

            } else {
                new Alert(Alert.AlertType.ERROR, "Please fill All the fields !").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error !").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "ClassNotFound Error !").show();
        }

    }
//
    @FXML
    void onMouseClickForgetPassword(MouseEvent event) throws IOException {

    }

    @FXML
    void HidePasswordOnAction(KeyEvent event) {
        if (passwordHide.getText() != null) {
            Pattern compile = RegExPatterns.getUserPWPattern();
            Matcher matcher = compile.matcher(passwordHide.getText());
            boolean matches = matcher.matches();
            if (matches) {
                passwordHide.setUnFocusColor(Paint.valueOf("blue"));
                userPasswordError.setVisible(false);
            } else {
                passwordHide.setUnFocusColor(Paint.valueOf("red"));
                userPasswordError.setVisible(true);
            }
            password = passwordHide.getText();
            passwordShow.setText(password);
        }

    }

    @FXML
    void close_eye_clicked(MouseEvent event) {
        lblEyeClose.setVisible(false);
        lblEyeOpen.setVisible(true);
        passwordHide.setVisible(false);
        passwordShow.setVisible(true);
    }

    @FXML
    public void showPasswordOnAction(KeyEvent keyEvent) {
        if (passwordShow.getText() != null) {
            Pattern compile = RegExPatterns.getUserPWPattern();
            Matcher matcher = compile.matcher(passwordShow.getText());
            boolean matches = matcher.matches();
            if (matches) {
                passwordShow.setUnFocusColor(Paint.valueOf("blue"));
                userPasswordError.setVisible(false);
            } else {
                passwordShow.setUnFocusColor(Paint.valueOf("red"));
                userPasswordError.setVisible(true);
            }
            password = passwordShow.getText();
            passwordHide.setText(password);
        }
    }

    @FXML
    public void open_eye_clicked(MouseEvent mouseEvent) {
        lblEyeClose.setVisible(true);
        lblEyeOpen.setVisible(false);
        passwordHide.setVisible(true);
        passwordShow.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblEyeClose.setVisible(true);
        lblEyeOpen.setVisible(false);
        passwordHide.setVisible(true);
        passwordShow.setVisible(false);
        userNameError.setVisible(false);
        userPasswordError.setVisible(false);
    }

    @FXML
    void onActionUserName(KeyEvent event) {
        Pattern compile= RegExPatterns.getUserNamePattern();
        Matcher matcher=compile.matcher(userName.getText());
        boolean matches=matcher.matches();
        if (matches){
            userName.setUnFocusColor(Paint.valueOf("blue"));
            userNameError.setVisible(false);
        }else{
            userName.setUnFocusColor(Paint.valueOf("red"));
            userNameError.setVisible(true);
        }
    }

}


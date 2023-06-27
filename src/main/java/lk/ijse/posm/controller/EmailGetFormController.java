package lk.ijse.posm.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import lk.ijse.posm.util.GenerateReports.GeneratePdf;
import lk.ijse.posm.util.MailPack.Mail;
import lk.ijse.posm.util.ValidationPattern.RegExPatterns;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailGetFormController {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton sendBtn;

    @FXML
    private JFXButton skipBtn;

    @FXML
    private JFXTextField emailTxtField;

    private boolean isEmailOk=false;

    public static Mail mail=new Mail();

    public static String form=null;

    @FXML
    void onActionSend(ActionEvent event) {
        Thread thread=null;
        try {
            setForm();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        if (isEmailOk) {
            try {
                mail.setTo(emailTxtField.getText());
                mail.outMail();
                thread=new Thread(mail);
                thread.start();
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    void onActionSkip(ActionEvent event) {

        try {
            setForm();
        }catch (IOException exception){
            exception.printStackTrace();
        }

    }

    @FXML
    void onActionEmailTxt(KeyEvent event) {
        Pattern compile= RegExPatterns.getEmailPattern();
        Matcher matcher=compile.matcher(emailTxtField.getText());
        boolean matches=matcher.matches();
        if (matches){
            emailTxtField.setUnFocusColor(Paint.valueOf("blue"));
            this.isEmailOk=true;
        }else{
            emailTxtField.setUnFocusColor(Paint.valueOf("red"));
            this.isEmailOk=false;
        }
    }

    public void setForm() throws IOException {
        URL resource= getClass().getResource(form);
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        root.getChildren().clear();
        root.getChildren().add(load);
    }

}

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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import lk.ijse.posm.bo.BOFactory;
import lk.ijse.posm.bo.custom.ChangeBO;
import lk.ijse.posm.bo.custom.CurrencyBO;
import lk.ijse.posm.bo.custom.CustomerBO;
import lk.ijse.posm.dao.custom.ChangeDAO;
import lk.ijse.posm.dao.custom.impl.ChangeDAOImpl;
import lk.ijse.posm.db.DBConnection;
import lk.ijse.posm.dto.ChangeDTO;
import lk.ijse.posm.dto.CustomerDTO;
import lk.ijse.posm.dto.TM.CurrencyTM;
import lk.ijse.posm.util.GenerateReports.GeneratePdfMoneyTransfer;
import lk.ijse.posm.util.MailPack.Mail;
import lk.ijse.posm.util.ValidationPattern.RegExPatterns;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoneyTransferForm implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private TextField enteredPaymentId;

    @FXML
    private JFXButton search;

    @FXML
    private Label paymentId;

    @FXML
    private JFXTextField customerName;

    @FXML
    private JFXTextField customerTeleNo;

    @FXML
    private JFXTextField sellingMoneyAmount;

    @FXML
    private JFXButton RemoveBtn;

    @FXML
    private JFXComboBox<String> customerIdComboBox;

    @FXML
    private JFXButton newId;

    @FXML
    private Label paymentDate;

    @FXML
    private JFXComboBox<String> sellingMoneyTypeComboBox;

    @FXML
    private JFXComboBox<String> receivingMoneyComboBox;

    @FXML
    private Label receivingMoneyAmount;

    @FXML
    private Label dateAndTime;

    private volatile boolean stop=false;

    private String customer_id="C000";

    private ObservableList<String> customerIdList=null;

    private ObservableList<String> sellingMoneyType=FXCollections.observableArrayList();

    private ObservableList<String> receivingMoneyType=FXCollections.observableArrayList();

    private boolean isCustomerIdOk=false;

    private boolean isCustomerNameOk=false;

    private boolean isCustomerTpOk=false;

    private boolean isSellingMoneyTypeOk=false;

    private boolean isSellingMoneyAmountOk=false;

    private boolean isReceivingMoneyTypeOk=false;

    ChangeBO changeBO= (ChangeBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CHANGE);

    CustomerBO customerBO= (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);

    CurrencyBO currencyBO= (CurrencyBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CURRENCY);

    @FXML
    void newIdBtn(ActionEvent event) {
        if (customer_id.equals("C000")) {
            try {
                customer_id = generateCustomerId();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
            }
            customerIdList.add(customer_id);
            customerIdComboBox.setItems(customerIdList);
            customerIdComboBox.setPromptText(customer_id);

        }else{
            customerIdComboBox.getSelectionModel().select(customer_id);
        }

        labelEdit(true);
    }

    @FXML
    void onActionRemove(ActionEvent event) {
        boolean isRemove=false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do u really want to remove the Payment Details ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        String newPaymentId=null;
        try {
            newPaymentId=changeBO.generateNextPaymentId();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
        }
        try {
            if (alert.getResult() == ButtonType.YES && (!paymentId.getText().equals(newPaymentId))) {
                try {
                    isRemove = changeBO.removeChangeDetails(new ChangeDTO(paymentId.getText(), paymentDate.getText(), sellingMoneyTypeComboBox.getValue(),
                            Double.parseDouble(sellingMoneyAmount.getText()), receivingMoneyComboBox.getValue(), Double.parseDouble(receivingMoneyAmount.getText())));
                    if (isRemove) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Details Removed Successfully !!").showAndWait();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Details Removed UnSuccessful !!").showAndWait();
                    }
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
                } catch (ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
                }
            }
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
        saveBtn.setText("Save");
        makeAllNull();
        setBooleanFalse(false);
        isButtonsEnable();
    }

    private String generateCustomerId() throws SQLException, ClassNotFoundException {
        return customerBO.generateNextCustId();
    }

    private void labelEdit(boolean isEligible) {
        customerName.setEditable(isEligible);
        customerTeleNo.setEditable(isEligible);
    }

    @FXML
    void onActionSave(ActionEvent event) throws JRException, SQLException, FileNotFoundException {

        boolean isUpdated = false;
        boolean isSaved = false;

        GeneratePdfMoneyTransfer generatePdf = new GeneratePdfMoneyTransfer();

        generatePdf.setCustId(customerIdComboBox.getValue());
        generatePdf.setCustName(customerName.getText());
        generatePdf.setCustTp(customerTeleNo.getText());

        generatePdf.setPayemntId(paymentId.getText());
        generatePdf.setReceivingMoneyAmount(Double.parseDouble(receivingMoneyAmount.getText()));
        generatePdf.setSellingMoneyAmount(Double.parseDouble(sellingMoneyAmount.getText()));
        generatePdf.setReceivingMoneyType(receivingMoneyComboBox.getValue());
        generatePdf.setSellingMoneyType(sellingMoneyTypeComboBox.getValue());


        if (saveBtn.getText().equals("Save")) {
            try {
                isSaved = changeBO.saveChangeDetails(new CustomerDTO(customerIdComboBox.getValue(),
                                customerName.getText(),
                                customerTeleNo.getText()),
                        new ChangeDTO(paymentId.getText(), paymentDate.getText(), sellingMoneyTypeComboBox.getValue(),
                                Double.parseDouble(sellingMoneyAmount.getText()), receivingMoneyComboBox.getValue(), Double.parseDouble(receivingMoneyAmount.getText())
                        )
                );

                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Details Saved Successfully !!").showAndWait();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Details Saved UnSuccessful !!").showAndWait();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
            }

        }else {
            try {
                isUpdated = changeBO.updateChangeDetails(new CustomerDTO(customerIdComboBox.getValue(),
                                customerName.getText(),
                                customerTeleNo.getText()),
                        new ChangeDTO(paymentId.getText(), paymentDate.getText(), sellingMoneyTypeComboBox.getValue(),
                                Double.parseDouble(sellingMoneyAmount.getText()), receivingMoneyComboBox.getValue(), Double.parseDouble(receivingMoneyAmount.getText())));

                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Details Updated Successfully !!").showAndWait();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Details Updated UnSuccessful !!").showAndWait();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
            }
            saveBtn.setText("Save");
        }

        if (isSaved || isUpdated) {

            generatePdf.generateMoneyTrasferReport();

            File pdfFile = new File("invoiceMoneyTrasfer.pdf");

            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(pdfFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Desktop is not supported");
                }
            } else {
                System.out.println("File not found");
            }

            try {
                EmailGetFormController.mail=new Mail("Your Money Transfer Bill Of Kalutara Post Office","Money Transfer Information","invoiceMoneyTrasfer.pdf");
                EmailGetFormController.form="/view/moneyTransferForm.fxml";
                URL resource = getClass().getResource("/view/emailGetForm.fxml");
                assert resource != null;
                Parent load = FXMLLoader.load(resource);
                root.getChildren().clear();
                root.getChildren().add(load);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        makeAllNull();
        setBooleanFalse(false);
        isButtonsEnable();
    }

    private void makeAllNull() {
        customerIdComboBox.getSelectionModel().select(null);
        customerName.setText(null);
        customerName.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        customerTeleNo.setText(null);
        customerTeleNo.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        setPaymentId();
        setDate();
        sellingMoneyTypeComboBox.getSelectionModel().select(null);
        sellingMoneyAmount.setText(null);
        sellingMoneyAmount.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        receivingMoneyComboBox.getSelectionModel().select(null);
        receivingMoneyAmount.setText(null);
    }

    @FXML
    void onActionSearch(ActionEvent event) {
        boolean isWrongIds= false;
        try {
            isWrongIds = changeBO.isExits(enteredPaymentId.getText());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
        }
        if (!isWrongIds) {
            enteredPaymentId.setText("Wrong Order Id Or Null ");
            makeAllNull();
        }else {
            String paymentIds = enteredPaymentId.getText();
            ChangeDTO change = null;
            CustomerDTO customer = null;

            try {
                change = changeBO.searchPayment(paymentIds);
                customer = customerBO.searchCustomer(change.getCustomerId());
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
            }
            customerIdComboBox.getSelectionModel().select(change.getCustomerId());
            customerName.setText(customer.getCustomer_name());
            customerTeleNo.setText(customer.getCustomer_contact());
            paymentId.setText(paymentIds);
            paymentDate.setText(change.getPaymentDate());
            sellingMoneyTypeComboBox.getSelectionModel().select(change.getSellingMoneyType());
            sellingMoneyAmount.setText(change.getSellingMoneyAmount() + "");
            receivingMoneyComboBox.getSelectionModel().select(change.getReceivingMoneyType());
            receivingMoneyAmount.setText(change.getReceivingMoneyAmount() + "");

            saveBtn.setText("Update");
        }

    }

    @FXML
    void onActionSelectCustomerId(ActionEvent event) {
        isCustomerIdOk= customerIdComboBox.getValue() != null;
        isButtonsEnable();
        if (isCustomerIdOk) {
            String cus_id = null;
            try {
                cus_id = customerIdComboBox.getSelectionModel().getSelectedItem();

                String newCustomerId = null;

                try {
                    newCustomerId = generateCustomerId();
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
                } catch (ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
                }

                if (cus_id.equals(newCustomerId)) {
                    labelEdit(true);
                    customerName.setText(null);
                    customerTeleNo.setText(null);
                } else {
                    CustomerDTO customer = null;
                    try {
                        customer = customerBO.searchCustomer(cus_id);
                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
                    } catch (ClassNotFoundException e) {
                        new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
                    }
                    customerName.setText(customer.getCustomer_name());
                    customerTeleNo.setText(customer.getCustomer_contact());
                    labelEdit(false);
                    if (customer.getCustomer_contact().equals("0000000000")) {
                        customerTeleNo.setEditable(true);
                    }
                }
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
    }

    @FXML
    void onActionSellingMoneyType(ActionEvent event) {
        if (sellingMoneyTypeComboBox.getValue()==null){
            isSellingMoneyTypeOk=false;
            isReceivingMoneyTypeOk=false;
        }else{
            isSellingMoneyTypeOk=true;
            isReceivingMoneyTypeOk=true;
        }
        isButtonsEnable();
        if (isSellingMoneyTypeOk&&isReceivingMoneyTypeOk) {
            try {
                if (sellingMoneyTypeComboBox.getValue().equals("Sri Lankan LKR")) {
                    receivingMoneyType.remove("Sri Lankan LKR");
                    receivingMoneyComboBox.setItems(receivingMoneyType);
                } else {
                    receivingMoneyComboBox.getSelectionModel().select("Sri Lankan LKR");
                    receivingMoneyComboBox.setEditable(false);
                }
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
    }

    @FXML
    void onActionReceivingMoneyType(ActionEvent event) {
        if (receivingMoneyComboBox.getValue()==null){
            isSellingMoneyTypeOk=false;
            isReceivingMoneyTypeOk=false;
        }else{
            isSellingMoneyTypeOk=true;
            isReceivingMoneyTypeOk=true;
        }
        isButtonsEnable();
        if (isReceivingMoneyTypeOk&&isReceivingMoneyTypeOk) {
            try {
                if (receivingMoneyComboBox.getValue().equals("Sri Lankan LKR")) {
                    sellingMoneyType.remove("Sri Lankan LKR");
                    sellingMoneyTypeComboBox.setItems(sellingMoneyType);
                } else {
                    sellingMoneyTypeComboBox.getSelectionModel().select("Sri Lankan LKR");
                    sellingMoneyTypeComboBox.setEditable(false);
                }
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Set Send Date
        setDate();

        //Load Customer Ids
        loadCustomerIds();

        //set PaymentId
        setPaymentId();

        //Load selling Money Type
        loadMoneyType();

        saveBtn.setText("Save");

        isButtonsEnable();
    }

    private void loadMoneyType() {
        ArrayList<CurrencyTM> currencyList=null;
        try {
            currencyList=currencyBO.getAllCurrencyDetails();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
        }

        for (CurrencyTM moneyType : Objects.requireNonNull(currencyList)){
            sellingMoneyType.add(moneyType.getMoneyType());
            receivingMoneyType.add(moneyType.getMoneyType());
        }

        sellingMoneyTypeComboBox.setItems(sellingMoneyType);

        receivingMoneyComboBox.setItems(receivingMoneyType);

    }

    private void setDate() {
        paymentDate.setText(String.valueOf(LocalDate.now()));
    }

    private void loadCustomerIds() {
        try {
            List<String> ids = customerBO.getAllIds();
            customerIdList = FXCollections.observableArrayList();

            customerIdList.addAll(ids);
            customerIdComboBox.setItems(customerIdList);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
        }
    }

    private void setPaymentId() {
        try {
            paymentId.setText(changeBO.generateNextPaymentId());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
        }
    }

    @FXML
    void onActionSetReceivingMoneyAmount(ActionEvent event) {
        Pattern compile= RegExPatterns.getDoublePattern();
        Matcher matcher=compile.matcher(sellingMoneyAmount.getText());
        boolean matches=matcher.matches();
        if (matches){
            sellingMoneyAmount.setUnFocusColor(Paint.valueOf("blue"));
            this.isSellingMoneyAmountOk=true;
        }else{
            sellingMoneyAmount.setUnFocusColor(Paint.valueOf("red"));
            this.isSellingMoneyAmountOk=false;
        }

        isButtonsEnable();

        if (matches) {
            try {
                if (Double.parseDouble(sellingMoneyAmount.getText()) > (changeBO.getSellingMoneyAmount(sellingMoneyTypeComboBox.getValue()))) {
                    new Alert(Alert.AlertType.WARNING, "The Requested Amount is insufficient.Please type sufficient amount.").showAndWait();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
            }

            if (!sellingMoneyTypeComboBox.getValue().equals("Sri Lankan LKR")) {
                try {
                    receivingMoneyAmount.setText(String.valueOf(changeBO.getReceivingMoneyAmount(sellingMoneyTypeComboBox.getValue(), Double.parseDouble(sellingMoneyAmount.getText()))));
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
                } catch (ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
                }
            } else {
                try {
                    receivingMoneyAmount.setText(String.valueOf(changeBO.getReceivingMoneyAmountFromAnotherCurrencyType(receivingMoneyComboBox.getValue(), Double.parseDouble(sellingMoneyAmount.getText()))));
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
                } catch (ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
                }
            }
        }
    }
    @FXML
    void onActionCustomerNameCheck(KeyEvent event) {
        Pattern compile1= RegExPatterns.getNamePattern();
        Matcher matcher=compile1.matcher(customerName.getText());
        boolean matches1=matcher.matches();

        Pattern compile2=RegExPatterns.getTwoStringCheckPattern();
        matcher=compile2.matcher(customerName.getText());
        boolean matches2=matcher.matches();

        if (matches1 && matches2){
            customerName.setUnFocusColor(Paint.valueOf("blue"));
            isCustomerNameOk=true;
        }else{
            customerName.setUnFocusColor(Paint.valueOf("red"));
            isCustomerNameOk=false;
        }

        isButtonsEnable();
    }

    @FXML
    void onActionCustomerTpCheck(KeyEvent event) {
        Pattern compile= RegExPatterns.getMobilePattern();
        Matcher matcher=compile.matcher(customerTeleNo.getText());
        boolean matches=matcher.matches();
        if (matches){
            customerTeleNo.setUnFocusColor(Paint.valueOf("blue"));
            this.isCustomerTpOk=true;
        }else{
            customerTeleNo.setUnFocusColor(Paint.valueOf("red"));
            this.isCustomerTpOk=false;
        }

        isButtonsEnable();
    }

    private void isButtonsEnable() {
        setButtonDisable(!isCustomerIdOk || !isSellingMoneyTypeOk || !isSellingMoneyAmountOk || !isReceivingMoneyTypeOk || !isCustomerNameOk || !isCustomerTpOk);
    }

    private void setButtonDisable(boolean isDisable) {
        saveBtn.setDisable(isDisable);
    }

    private void setBooleanFalse(boolean isFalse){
        isCustomerIdOk=isFalse;
        isReceivingMoneyTypeOk=isFalse;
        isSellingMoneyAmountOk=isFalse;
        isSellingMoneyTypeOk=isFalse;
        isCustomerNameOk=isFalse;
        isCustomerTpOk=isFalse;
    }

    @FXML
    void onActionTodayMoneyTrasferDetails(ActionEvent event) throws FileNotFoundException, JRException, SQLException, ClassNotFoundException {
        if(changeBO.isTodayChangesOk()) {
            InputStream input = new FileInputStream(new File("src/main/resources/report/moneyTransfer.jrxml"));
            JasperDesign jasperDesign = JRXmlLoader.load(input);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getDbConnection().getConnection());

            JasperViewer.viewReport(jasperPrint, false);
        }else{
            new Alert(Alert.AlertType.WARNING,"Still No Money Trasfers to Show.").showAndWait();
        }
    }
}

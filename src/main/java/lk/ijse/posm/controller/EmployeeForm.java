package lk.ijse.posm.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lk.ijse.posm.bo.BOFactory;
import lk.ijse.posm.bo.custom.EmployeeBO;
import lk.ijse.posm.dto.PostMan_EmployeeDTO;
import lk.ijse.posm.dto.TM.PostManMailDetailsTM;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EmployeeForm implements Initializable{

    @FXML
    private AnchorPane root;

    @FXML
    private Label stationaryName;

    @FXML
    private JFXButton updateEmployee;

    @FXML
    private ComboBox<String> idsComboBox;

    @FXML
    private Label postManName;

    @FXML
    private Label todayDate;

    @FXML
    private Label postManArea;

    @FXML
    private Label postQty;

    @FXML
    private JFXButton addEmployeeBtn;

    @FXML
    private TableView<PostManMailDetailsTM> postManTable;

    @FXML
    private TableColumn<PostManMailDetailsTM,String> colMailId;

    @FXML
    private TableColumn<PostManMailDetailsTM,String> colReceiversName;

    @FXML
    private TableColumn<PostManMailDetailsTM,String> colReceiversAddress;

    @FXML
    private ImageView imgView;

    @FXML
    private Label dateAndTime;

    private ObservableList<PostManMailDetailsTM> obList=null;

    public static boolean isNew=false;

    public static String postmanId=null;

    EmployeeBO employeeBO= (EmployeeBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.EMPLOYEE);

    @FXML
    void onActionAddEmployee(ActionEvent event) throws IOException {
        isNew=true;
        URL resource = getClass().getResource("/view/addPostMan.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        root.getChildren().clear();
        root.getChildren().add(load);
    }

    @FXML
    void onActionUpdateEmployee(ActionEvent event) throws IOException {
        URL resource = getClass().getResource("/view/addPostMan.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        root.getChildren().clear();
        root.getChildren().add(load);
    }

    @FXML
    void onActionPostmanDetails(ActionEvent event) throws MalformedURLException {
        PostMan_EmployeeDTO postManDTO=null;
        ArrayList<PostManMailDetailsTM> mailDetails=null;
        try {
            postManDTO=employeeBO.getPostmanDetails(idsComboBox.getValue());
            postQty.setText(String.valueOf(employeeBO.getMailCount(idsComboBox.getValue())));
            mailDetails=employeeBO.getMailDetails(idsComboBox.getValue());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
        }

        assert postManDTO != null;
        postManArea.setText(postManDTO.getPost_area());
        postManName.setText(postManDTO.getEmployee_name());

        if(postManDTO.getImg()!=null) {
            try {
                Image image = new Image(postManDTO.getImg());
                imgView.setImage(image);
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
        
        setTable(mailDetails);

        postmanId=idsComboBox.getValue();
    }

    private void setTable(ArrayList<PostManMailDetailsTM> mailDetails) {

        postManTable.getItems().clear();

        setCellValueFactory();

        obList.addAll(mailDetails);

        postManTable.setItems(obList);

    }

    private void setCellValueFactory() {
        colMailId.setCellValueFactory(new PropertyValueFactory<>("mailId"));
        colReceiversName.setCellValueFactory(new PropertyValueFactory<>("receiverName"));
        colReceiversAddress.setCellValueFactory(new PropertyValueFactory<>("receiversAddress"));

        obList = FXCollections.observableArrayList();
        postManTable.setItems(obList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Load postman Id
        loadPostmanIds();

        //Set Send Date
        setDate();

    }

    private void loadPostmanIds() {

        ArrayList<String> postManIds=null;
        try {
            postManIds=employeeBO.getAllIds();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").showAndWait();
        }

        ObservableList<String> postmanIdList = FXCollections.observableArrayList();

        assert postManIds != null;
        postmanIdList.addAll(postManIds);

        idsComboBox.setItems(postmanIdList);
    }

    private void setDate() {
        todayDate.setText(String.valueOf(LocalDate.now()));
    }

}


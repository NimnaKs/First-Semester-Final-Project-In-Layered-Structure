package lk.ijse.posm.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import lk.ijse.posm.bo.BOFactory;
import lk.ijse.posm.bo.custom.*;
import lk.ijse.posm.dto.MonthIncomeDTO;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable/*, MainController*/ {

    @FXML
    private StackedBarChart<String,Number> chart;

    @FXML
    private Label totalSales;

    @FXML
    private Label totalOrders;

    @FXML
    private Label totalCustomers;

    @FXML
    private Label normalMails;

    @FXML
    private Label internationalMails;

    @FXML
    private Label parcels;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label totalSalesIncome;

    @FXML
    private Label emsCount;

    @FXML
    private Label dateAndTime;

    UserBO userBO = (UserBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.LOGIN);

    OrderBO orderBO= (OrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDER);

    ItemBO itemBO= (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);

    CustomerBO customerBO= (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);

    MailBO mailBO= (MailBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.MAIL);

    ParcelBO parcelBO= (ParcelBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PARCEL);

    PaymentBO paymentBO= (PaymentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PAYMENT);

    ChangeBO changeBO= (ChangeBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CHANGE);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setLabels();

        setStackedChart();

    }

    private void setStackedChart() {

        chart.setStyle("-fx-background-color: #21222D");

        final XYChart.Series<String,Number> series1=new XYChart.Series<>();
        series1.setName("Item Store Income");
        final XYChart.Series<String,Number> series2=new XYChart.Series<>();
        series2.setName("Parcel Income");
        final XYChart.Series<String,Number> series3=new XYChart.Series<>();
        series3.setName("Utility Bill Income");
        final XYChart.Series<String,Number> series4=new XYChart.Series<>();
        series4.setName("Money Transfer Income");

        ArrayList<MonthIncomeDTO> orderIncomeList=null;
        ArrayList<MonthIncomeDTO> parcelDetailsIncome=null;
        ArrayList<MonthIncomeDTO> billPaymentIncome=null;
        ArrayList<MonthIncomeDTO> changeDetailsIncome=null;

        try {
            orderIncomeList=orderBO.getTotalOrderIncomeInEachMonth();
            parcelDetailsIncome=parcelBO.getTotalParcelIncomeInEachMonth();
            billPaymentIncome=paymentBO.getBillPaymentIncomeInEachMonth();
            changeDetailsIncome=changeBO.getChangeIncomeInEachMonth();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"SQL ERROR .Please Try Again Later !").showAndWait();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Class Not Found  ERROR .Please Try Again Later !").showAndWait();
        }

        for (int i = 0; i < orderIncomeList.size(); i++) {
            series1.getData().add(new XYChart.Data<>(orderIncomeList.get(i).getMonth(), (Number) orderIncomeList.get(i).getIncome()));
            series2.getData().add(new XYChart.Data<>(parcelDetailsIncome.get(i).getMonth(), (Number) parcelDetailsIncome.get(i).getIncome()));
            series3.getData().add(new XYChart.Data<>(billPaymentIncome.get(i).getMonth(), (Number) billPaymentIncome.get(i).getIncome()));
            series4.getData().add(new XYChart.Data<>(changeDetailsIncome.get(i).getMonth(), (Number) changeDetailsIncome.get(i).getIncome()));
        }

        chart.getData().addAll(series1,series2,series3,series4);

    }

    private void setLabels() {

        try {
            welcomeLabel.setText("nice To meet u Mr." + userBO.getUserName(LoginFormController.username) + "!");
            totalSalesIncome.setText("Rs." + String.valueOf(orderBO.getTotalSalesIncome()));
            totalOrders.setText(String.valueOf(orderBO.getTotalOrderCount()));
            totalSales.setText(String.valueOf(itemBO.getTotalItemSalesCount()));
            totalCustomers.setText(String.valueOf(customerBO.getTotalCustomers()));
            normalMails.setText(String.valueOf(mailBO.getTodayTotalMailsCount()));
            internationalMails.setText(String.valueOf(mailBO.getInternationalMailsCount()));
            parcels.setText(String.valueOf(parcelBO.getTodayParcelCount()));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"SQL ERROR .Please Try Again Later !").showAndWait();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"ClassNotFoundException ERROR .Please Try Again Later !").showAndWait();
            throw new RuntimeException(e);
        }

        emsCount.setText("5");
    }
}


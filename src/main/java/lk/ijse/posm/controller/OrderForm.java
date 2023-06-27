package lk.ijse.posm.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import lk.ijse.posm.bo.BOFactory;
import lk.ijse.posm.bo.custom.CustomerBO;
import lk.ijse.posm.bo.custom.ItemBO;
import lk.ijse.posm.bo.custom.OrderBO;
import lk.ijse.posm.dto.*;
import lk.ijse.posm.dto.TM.OrderTM;
import lk.ijse.posm.util.GenerateReports.GeneratePdf;
import lk.ijse.posm.util.MailPack.Mail;
import lk.ijse.posm.util.ValidationPattern.RegExPatterns;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderForm implements Initializable{

    @FXML
    private AnchorPane root;

    @FXML
    private Label orderId;

    @FXML
    private JFXComboBox<String> itemComboBox;

    @FXML
    private Label description;

    @FXML
    private Label unitPrice;

    @FXML
    private Label QtyOnHand;

    @FXML
    private TextField Qty;

    @FXML
    private TableView<OrderTM> itemTable;

    @FXML
    private TableColumn<OrderTM,String> colCode;

    @FXML
    private TableColumn<OrderTM,String> colDescription;

    @FXML
    private TableColumn<OrderTM,String> colQty;

    @FXML
    private TableColumn<OrderTM,String> colUnitPrice;

    @FXML
    private TableColumn<OrderTM,String> colTotal;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private JFXButton addToCart;

    @FXML
    private JFXButton placeOrderBtn;

    @FXML
    private Label netTotal;

    @FXML
    private JFXButton removeBtn;

    @FXML
    private JFXComboBox<String> customerIdComboBox;

    @FXML
    private JFXButton newId;

    @FXML
    private JFXTextField customerName;

    @FXML
    private Label orderDate;

    @FXML
    private JFXButton search;

    @FXML
    private TextField oId;

    @FXML
    private Label dateAndTime;

    @FXML
    private Label lblQtyError;

    private ObservableList<String> customerIdList;

    private ObservableList<OrderTM> obList;

    private String customer_id="C000";

    private boolean isUpdate=false;

    private OrderTM selectedItems=null;

    private double nTotal = 0.0;

    private boolean isNewCustomer=false;

    private boolean isCustomerDetails=false;

    private boolean isItemDetails=false;

    private boolean isCustomerIdOk=false;

    private String newOrderId=null;

    private int QtyBoolean=0;

    private String contactNo=null;

    CustomerBO customerBO= (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);

    ItemBO itemBO= (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);

    OrderBO orderBO= (OrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDER);

    @FXML
    void onActionSelectCustomerId(ActionEvent event) {

        isCustomerIdOk= customerIdComboBox.getValue() != null;

        if (isCustomerIdOk) {
            try {
                String cus_id = customerIdComboBox.getSelectionModel().getSelectedItem();

                String newCustomerId = null;

                try {
                    newCustomerId = generateCustomerId();
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").show();
                } catch (ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").show();
                }

                if (cus_id.equals(newCustomerId)) {
                    setCustomerNameEdit(true);
                    customerName.setText(null);
                } else {
                    CustomerDTO customer = null;
                    try {
                        customer = customerBO.searchCustomer(cus_id);
                        contactNo=customer.getCustomer_contact();
                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").show();
                    } catch (ClassNotFoundException e) {
                        new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").show();
                    }
                    assert customer != null;
                    customerName.setText(customer.getCustomer_name());
                    setCustomerNameEdit(false);
                }
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }


    }

    @FXML
    void OnActionItemComboBox(ActionEvent event) {
        boolean isItemIdOk= itemComboBox.getValue() != null;

        if (isItemIdOk) {
            String code = itemComboBox.getValue();
            try {
                ItemDTO item =itemBO.searchItem(code);
                fillItemFields(item);
                Qty.requestFocus();
                QtyBoolean=item.getQty_on_hand();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "SQL Error!,Plz try again later").showAndWait();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Class Not Found  Error!,Plz try again later").showAndWait();
            }
        }

    }


    private void fillItemFields(ItemDTO item) {
        try {
            description.setText(item.getItem_type());
            unitPrice.setText(String.valueOf(item.getUnit_price()));
            QtyOnHand.setText(String.valueOf(item.getQty_on_hand()));
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    void OnActionQty(ActionEvent event){
        Pattern compile=RegExPatterns.getIntWithRange100Pattern();
        Matcher matcher=compile.matcher(Qty.getText());
        boolean matches=matcher.matches();
        int QtyOnHand= Integer.parseInt(Qty.getText());
        lblQtyError.setVisible(!matches || (QtyBoolean < QtyOnHand));
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        if (itemComboBox.getValue()==null || Qty.getText()==null){
            new Alert(Alert.AlertType.ERROR,"Null Value in item code or Qty").showAndWait();
        }else {
            if (isUpdate) {
                calculateNetTotal(-selectedItems.getTotal());
                int qty = Integer.parseInt(Qty.getText());
                double unitPriced = Double.parseDouble(unitPrice.getText());
                double total = unitPriced * qty;
                selectedItems.setQty(qty);
                selectedItems.setTotal(total);
                itemTable.refresh();
                isUpdate = false;
                calculateNetTotal(total);
                makeAllNull();
            } else {
                String code = itemComboBox.getValue();
                String descriptions = description.getText();
                int qty = Integer.parseInt(Qty.getText());
                double unitPriced = Double.parseDouble(unitPrice.getText());
                double total = unitPriced * qty;
                Button btn = new Button("Remove");

                btn.setOnAction(new EventHandler() {
                    @Override
                    public void handle(Event event) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do u really want to remove the item ?", ButtonType.YES, ButtonType.NO);
                        alert.showAndWait();
                        if (alert.getResult() == ButtonType.YES) {
                            OrderTM selectedItem = itemTable.getSelectionModel().getSelectedItem();
                            itemTable.getItems().remove(selectedItem);
                            calculateNetTotal(-selectedItem.getTotal());
                            makeAllNull();
                            isButtonsEnable();
                            isItemDetails = nTotal != 0.0;
                            isButtonsEnable();
                        }
                    }
                });

                OrderTM tm = new OrderTM(code, descriptions, qty, unitPriced, total, btn);

                itemTable.getItems().add(tm);

                calculateNetTotal(total);
                makeAllNull();

                isButtonsEnable();

                isItemDetails=true;
                isButtonsEnable();
            }
        }



    }

    private void makeAllNull() {
        itemComboBox.getSelectionModel().select(null);
        description.setText(null);
        Qty.setText(null);
        unitPrice.setText(null);
        QtyOnHand.setText(null);
    }

    private void calculateNetTotal(double total){
        nTotal+=total;
        netTotal.setText(String.valueOf(nTotal));
    }

    @FXML
    void newIdBtn(ActionEvent event) {
        if (customer_id.equals("C000")) {
            try {
                customer_id = generateCustomerId();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            customerIdList.add(customer_id);
            customerIdComboBox.setItems(customerIdList);
            customerIdComboBox.setPromptText(customer_id);

        }else{
            customerIdComboBox.setPromptText(customer_id);
        }

        setCustomerNameEdit(true);

        isNewCustomer=true;

        isCustomerIdOk=true;
        isButtonsEnable();
    }

    private String generateCustomerId() throws SQLException, ClassNotFoundException {
        return customerBO.generateNextCustId();
    }

    @FXML
    void onActionRemove(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do u really want to remove the Order ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        System.out.println(!newOrderId.equals(orderId.getText()));
        if ((alert.getResult() == ButtonType.YES )&& (!newOrderId.equals(orderId.getText()))){
            try {
                orderBO.removeOrder(oId.getText());
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").show();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").show();
            }

            makeAllNullInOrder();
        }else if (newOrderId.equals(orderId.getText())){
            makeAllNullInOrder();
        }

    }

    private void makeAllNullInOrder() {
        oId.setText(null);
        generateOrderId();
        setDate();
        setFieldsEmpty();
        makeAllNull();
        placeOrderBtn.setText("Place Order");
    }

    @FXML
    void onActionPlaceOrder(ActionEvent event) {

        boolean addItem=true;
        boolean isUpdate = false;
        boolean isPlaced = false;

        GeneratePdf generatePdf = new GeneratePdf();
        generatePdf.setCustId(customerIdComboBox.getValue());
        generatePdf.setCustName(customerName.getText());
        generatePdf.setOrderId(orderId.getText());

        List<ProductReportDTO> productDTOList = new ArrayList<>();

        for (int i = 0; i < itemTable.getItems().size(); i++) {
            OrderTM tm = obList.get(i);

            ProductReportDTO productReportDTO = new ProductReportDTO(tm.getDescription(), tm.getQty(), tm.getUnitPriced());
            productDTOList.add(productReportDTO);
        }

        generatePdf.setProductList(productDTOList);

        if (nTotal==0.0){
            addItem=false;
            new Alert(Alert.AlertType.WARNING, "Please Add Item to Order").showAndWait();
        }

        if (placeOrderBtn.getText().equals("Place Order") && addItem) {
            String oId = orderId.getText();
            String cusId = customerIdComboBox.getValue();
            String customerNameText = customerName.getText();

            List<CartDTO> cartDTOList = new ArrayList<>();

            for (int i = 0; i < itemTable.getItems().size(); i++) {
                OrderTM tm = obList.get(i);

                CartDTO cartDTO = new CartDTO(tm.getCode(), tm.getQty(), tm.getUnitPriced());
                cartDTOList.add(cartDTO);
            }

            try {
                if (isNewCustomer) {
                    cusId = generateCustomerId();
                    isNewCustomer = false;
                }
                isPlaced = orderBO.placeOrder(oId, new CustomerDTO(cusId,customerNameText,contactNo), cartDTOList);

                if (isPlaced) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Order Placed!").showAndWait();
                    setFieldsEmpty();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Order Not Placed!").showAndWait();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").show();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").show();
            }
        }else if(addItem){
            String oId = orderId.getText();
            String cusId = customerIdComboBox.getValue();
            String customerNameText = customerName.getText();

            List<CartDTO> cartDTOList = new ArrayList<>();

            for (int i = 0; i < itemTable.getItems().size(); i++) {
                OrderTM tm = obList.get(i);

                CartDTO cartDTO = new CartDTO(tm.getCode(), tm.getQty(), tm.getUnitPriced());
                cartDTOList.add(cartDTO);
            }

            try {
                if (isNewCustomer) {
                    cusId = generateCustomerId();
                    isNewCustomer = false;
                }

                isUpdate = orderBO.updateOrder(oId, new CustomerDTO(cusId,customerNameText,contactNo), cartDTOList);
                if (isUpdate) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Order Updated!").showAndWait();
                    makeAllNullInOrder();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Order Not Updated!").showAndWait();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "SQL Error!.Plz try again later.").showAndWait();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Class Not Found  Error!.Plz try again later.").showAndWait();
            }
            placeOrderBtn.setText("Place Order");
        }

        if (isPlaced || isUpdate){

            generatePdf.generateOrderReport();

            File pdfFile = new File("invoice.pdf");

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
                EmailGetFormController.mail=new Mail("Your Order Bill Of Kalutara Post Office","Order Report Information","invoice.pdf");
                EmailGetFormController.form="/view/orderForm.fxml";
                URL resource = getClass().getResource("/view/emailGetForm.fxml");
                assert resource != null;
                Parent load = FXMLLoader.load(resource);
                root.getChildren().clear();
                root.getChildren().add(load);
            }catch (IOException exception){
                exception.printStackTrace();
            }

        }
        makeAllNullInOrder();
        isCustomerDetails=false;
        isItemDetails=false;
        isCustomerIdOk=false;
        isButtonsEnable();

    }

    private void setFieldsEmpty() {
        customerIdComboBox.getSelectionModel().select(null);
        customerName.setText(null);
        customerName.setUnFocusColor(Paint.valueOf("#4d4d4d"));
        itemTable.getItems().clear();
        netTotal.setText(null);
        generateOrderId();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Set Send Date
        setDate();

        //Generate OrderId
        generateOrderId();

        //Load Customer Ids
        loadCustomerIds();

        //Set TextField Edit
        setCustomerNameEdit(false);

        //Load Item Codes
        loadItemCodes();

        //Set Cell Value Factory
        setCellValueFactory();

        customerName.setEditable(false);

        lblQtyError.setVisible(false);

        isButtonsEnable();

    }

    private void isButtonsEnable() {
        placeOrderBtn.setDisable(!isCustomerIdOk || !isCustomerDetails || !isItemDetails);
    }

    private void setCellValueFactory() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPriced"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));

        obList = FXCollections.observableArrayList();
        itemTable.setItems(obList);
    }

    private void loadItemCodes() {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> codes = itemBO.getAllIds();

            obList.addAll(codes);
            itemComboBox.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").show();
        }
    }

    private void setCustomerNameEdit(boolean isEditable) {
        customerName.setEditable(isEditable);
    }

    private void loadCustomerIds() {
        try {
            List<String> ids = customerBO.getAllIds();
            customerIdList= FXCollections.observableArrayList();

            customerIdList.addAll(ids);
            customerIdComboBox.setItems(customerIdList);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").show();
        }
    }

    private void generateOrderId() {
        try {
            newOrderId=(orderBO.generateNexOrderId());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").show();
        }

        orderId.setText(newOrderId);
    }

    private void setDate() {
        orderDate.setText(String.valueOf(LocalDate.now()));
    }

    @FXML
    void onMousedClicked(MouseEvent event) {
        selectedItems = itemTable.getSelectionModel().getSelectedItem();
        setItemData(selectedItems);
        isUpdate=true;
        placeOrderBtn.setDisable(false);
    }

    private void setItemData(OrderTM selectedItems) {
        itemComboBox.getSelectionModel().select(selectedItems.getCode());
        description.setText(selectedItems.getDescription());
        Qty.setText(String.valueOf(selectedItems.getQty()));
        unitPrice.setText(String.valueOf(selectedItems.getUnitPriced()));
        try {
            QtyOnHand.setText(String.valueOf(itemBO.getSelectedItemQtyOnHand(selectedItems.getCode())-selectedItems.getQty()));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").show();
        }
    }

    @FXML
    void onActionSearch(ActionEvent event) {
        OrderDTO order=null;
        boolean isWrongIds= false;
        try {
            isWrongIds = orderBO.isExists(oId.getText());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").show();
        }
        if (!isWrongIds){
            oId.setText("Wrong Order Id Or Null ");
            makeAllNullInOrder();
        }else {
            try {
               order = orderBO.searchOrder(oId.getText());
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").show();
            }
            placeOrderBtn.setText("Update Order");

            assert order != null;
            orderId.setText(order.getOrderId());

            orderDate.setText(order.getDate());

            customerIdComboBox.getSelectionModel().select(order.getCustomerId());

            try {
                customerName.setText(customerBO.searchCustomer(order.getCustomerId()).getCustomer_name());
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").show();
            }

            setItemTable(oId.getText());
            placeOrderBtn.setDisable(false);
        }

    }

    private void setItemTable(String oId) {
        ArrayList<OrderTM> Orderlist=new ArrayList<>();
        try {
            Orderlist=orderBO.getDetails(oId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Query Error ! Please Try Again Later.").showAndWait();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error ! Please Try Again Later.").show();
        }


        for (OrderTM ordertm:Orderlist){
            ordertm.getBtn().setOnAction(new EventHandler() {
                @Override
                public void handle(Event event) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do u really want to remove the item ?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        OrderTM selectedItem = itemTable.getSelectionModel().getSelectedItem();
                        itemTable.getItems().remove(selectedItem);
                        calculateNetTotal(-selectedItem.getTotal());
                        makeAllNull();
                        isUpdate=false;
                        isItemDetails= nTotal != 0.0;
                        isButtonsEnable();
                    }
                }
            });
            itemTable.getItems().add(ordertm);
        }

        setNetTotal(Orderlist);

    }

    private void setNetTotal(ArrayList<OrderTM> orderList) {

        for (OrderTM orderTM : orderList){
            nTotal+=orderTM.getTotal();
        }

        netTotal.setText(String.valueOf(nTotal));
    }

    @FXML
    void onActionCustomerName(ActionEvent event) {

        Pattern compile1=RegExPatterns.getNamePattern();
        Matcher matcher=compile1.matcher(customerName.getText());
        boolean matches=matcher.matches();

        Pattern compile2=RegExPatterns.getTwoStringCheckPattern();
        matcher=compile2.matcher(customerName.getText());
        boolean matches1=matcher.matches();

        if (matches && matches1){
            customerName.setUnFocusColor(Paint.valueOf("blue"));
            isCustomerDetails=true;
        }else{
            customerName.setUnFocusColor(Paint.valueOf("red"));
            isCustomerDetails=false;
        }

        isButtonsEnable();
    }

}

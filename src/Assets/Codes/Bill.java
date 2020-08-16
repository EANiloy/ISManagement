package Assets.Codes;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Bill implements Initializable
{

    @FXML
    private AnchorPane Drag_handle;

    @FXML
    private Circle minimize_btn;

    @FXML
    private Circle maximize_btn;

    @FXML
    private Circle exit_btn;

    @FXML
    private JFXButton Log_Out;

    @FXML
    private Label Clock_time;

    @FXML
    public TableView<Model_Bill> inv_table;

    @FXML
    public TableColumn<Model_Bill, String> inv_col_pn;

    @FXML
    public TableColumn<Model_Bill, String> inv_col_ori;

    @FXML
    public TableColumn<Model_Bill, String> inv_col_cat;

    @FXML
    public TableColumn<Model_Bill, JFXTextField> inv_col_quantity;

    @FXML
    private JFXTextField bill_quantity;

    @FXML
    private JFXTextField discount;

    @FXML
    private JFXTextField inv_search;

    @FXML
    public JFXComboBox<String> comp_combo;

    @FXML
    private JFXComboBox<String> cat_combo;

    @FXML
    private JFXButton inv_btn;

    @FXML
    private JFXHamburger nav_burg;

    @FXML
    private JFXDrawer draw;

    @FXML
    private Pane clock;

    @FXML
    private Pane date;
    @FXML
    private Label Date;

    @FXML
    private JFXSlider quantity;

    @FXML
    private JFXTextField cus_name;

    @FXML
    private JFXTextField cus_contact;

    @FXML
    private JFXTextArea cus_address;

    @FXML
    public TableView<Model_Cart> cart_table;

    @FXML
    public TableColumn<Model_Cart, String> cart_col_comp;

    @FXML
    public TableColumn<Model_Cart, String> cart_col_product_name;

    @FXML
    public TableColumn<Model_Cart, String> cart_col_cat;

    @FXML
    public TableColumn<Model_Cart, String> cart_col_orientation;

    @FXML
    public TableColumn<Model_Cart, Integer> cart_col_quantity;
    @FXML
    public TableColumn<Model_Cart, JFXTextField> cart_col_price;

    @FXML
    private JFXButton add_btn;

    @FXML
    private JFXButton Remove_btn;

    @FXML
    private JFXTextField paid_amount;

    @FXML
    private JFXButton total_btn;

    private double Xoffset=0;
    private double Yoffset=0;
    static String category=null;

    static String company =null;
    static String subcategory = null;
    int stock=0;
    String defaultquery = "Select * FROM "+category+" WHERE quantity>0";
    @FXML
    public void Log_Out(ActionEvent event)throws IOException {
        try{
            String logout = "UPDATE tb_"+Login.user+" SET logout_time = ? WHERE date= ? && logout_time IS NULL";
            PreparedStatement update = Main.conn.prepareStatement(logout);
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String time= LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
            update.setString(1, time);
            update.setString(2, date);
            update.execute();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
            Main.Loader("/Assets/FXMLs/Login.fxml");
    }
    @FXML
    void LoadPage(ActionEvent event) throws IOException {
        Main.Loader("/Assets/FXMLs/OwnerPage.fxml");
    }
    @FXML
    void Close(MouseEvent event) throws IOException {
        Main.exit();
    }
    @FXML
    void Minimize(MouseEvent event) throws IOException{
        Main.mini();
    }
    @FXML
    void Maximize(MouseEvent event) throws IOException {
        Main.maxi();
    }
    @FXML
    void Pressed(MouseEvent e){
        Xoffset = e.getSceneX();
        Yoffset = e.getSceneY();
    }
    @FXML
    void Drag(MouseEvent e){
        if(Main.stage.maximizedProperty().getValue())
        {
            Main.stage.setMaximized(false);
        }
        else {
            Main.stage.setX(e.getScreenX() - Xoffset);
            Main.stage.setY(e.getScreenY() - Yoffset);
        }
    }

    @FXML
    void LoadDate(MouseEvent event) throws IOException
    {
        date.setDisable(true);
        date.setVisible(false);
        clock.setDisable(false);
        clock.setVisible(true);
    }
    @FXML
    void LoadClock (MouseEvent event) throws IOException{
        clock.setVisible(false);
        clock.setDisable(true);
        date.setDisable(false);
        date.setVisible(true);
    }
    @FXML
    void Released(MouseEvent events)
    {
        Main.stage.setOpacity(1.0);
    }
    private void clocktime()
    {
        Timeline time = new Timeline(new KeyFrame(Duration.ZERO,actionEvent -> {
            Clock_time.setText("   "+LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE :  hh :  mm :  ss  a")));
        }),new KeyFrame(Duration.seconds(1)));
        Date.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("   dd   :   LLL   :   yyyy   ")));
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }
    @FXML
    void Go_back(MouseEvent event) throws IOException {
        if (Login.user_type.equals("employee")) {
            Main.Loader("/Assets/FXMLs/EmployeePage.fxml");
        }
        else
        {
            Main.Loader("/Assets/FXMLs/OwnerPage.fxml");
        }
    }
    @FXML
    void Confirm(ActionEvent event) throws IOException,SQLException{
        if(cus_name.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"Please provide Customer Name");
        }
        else if (cus_contact.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"Please provide Customer Contact Number");
        }
        else if (cus_address.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"Please provide Customer Address");
        }
        else if (discount.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"Please provide Discount Amount");
        }
        else if (paid_amount.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"Please provide Paid Amount");
        }
        else if(Integer.parseInt(discount.getText())>100 && Integer.parseInt(discount.getText())<0)
        {
            JOptionPane.showMessageDialog(null,"Incorrect discount amount");
        }
        else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("Are you sure you want to make this purchase?");
            confirm.setTitle("Purchase Confirmation");
            confirm.setHeaderText("Confirm Purchase?");
            Optional<ButtonType> result = confirm.showAndWait();
            if(result.get()==ButtonType.OK) {
                List<Model_Receipt> list = FXCollections.observableArrayList();
                int total = 0;
                for (Model_Cart cart : cart_table.getItems()) {
                    total = cart.getQuantity() * Integer.parseInt(cart.getPrice().getText());
                    list.addAll(Collections.singleton(new Model_Receipt(cart.getProduct_Name(), cart.getQuantity(), Integer.parseInt(cart.getPrice().getText()), total, 0)));
                }
                int subtotal = 0;
                int subquantity = 0;
                for (Model_Cart cart : cart_table.getItems()) {
                    subtotal = subtotal + cart.getQuantity() * Integer.parseInt(cart.getPrice().getText());
                    subquantity = subquantity + cart.getQuantity();
                }
                subtotal = subtotal - ((subtotal * Integer.parseInt(discount.getText())) / 100);
                String getcustomer = "SELECT * FROM tb_customer WHERE c_name = '" + cus_name.getText() + "'&& c_contact='" + cus_contact.getText() + "'";
                PreparedStatement prepcus = Main.conn.prepareStatement(getcustomer);
                ResultSet rescus = prepcus.executeQuery();
                int due = 0;
                if (!rescus.next()) {
                    String customer = "INSERT INTO tb_customer(c_id,c_name,c_contact,c_address,c_p_amount,c_d_amount,c_advance,c_b_quantity) VALUES (?,?,?,?,?,?,?,?)";
                    PreparedStatement insertcustomer = Main.conn.prepareStatement(customer);
                    insertcustomer.setString(1, null);
                    insertcustomer.setString(2, cus_name.getText());
                    insertcustomer.setString(3, cus_contact.getText());
                    insertcustomer.setString(4, cus_address.getText());
                    insertcustomer.setInt(5, Integer.parseInt(paid_amount.getText()));
                    due = subtotal - Integer.parseInt(paid_amount.getText());
                    if (due <= 0) {
                        insertcustomer.setInt(6, 0);
                        insertcustomer.setInt(7, (Integer.parseInt(paid_amount.getText()) - subtotal));
                    } else {
                        insertcustomer.setInt(6, due);
                        insertcustomer.setInt(7, 0);
                    }
                    insertcustomer.setInt(8, subquantity);
                    insertcustomer.execute();

                } else {
                    rescus.beforeFirst();
                    rescus.next();
                    String customer = "UPDATE tb_customer SET c_p_amount=c_p_amount+?, c_d_amount = c_d_amount+?, c_advance=c_advance+?, c_b_quantity=c_b_quantity+?" +
                            " WHERE c_name = '" + cus_name.getText() + "' && c_contact = '" + cus_contact.getText() + "'";
                    PreparedStatement updatecustomer = Main.conn.prepareStatement(customer);
                    updatecustomer.setInt(1, Integer.parseInt(paid_amount.getText()));
                    int extra = Integer.parseInt(paid_amount.getText()) - subtotal;
                    if (extra > 0) {
                        if (rescus.getInt("c_d_amount") >= 0) {
                            if (rescus.getInt("c_advance") > 0) {
                                if (rescus.getInt("c_d_amount") - rescus.getInt("c_advance") <= 0) {
                                    updatecustomer.setInt(2, -rescus.getInt("c_d_amount"));
                                    updatecustomer.setInt(3, -rescus.getInt("c_d_amount") + extra);
                                } else {
                                    updatecustomer.setInt(2, -(rescus.getInt("c_advance") + extra));
                                    updatecustomer.setInt(3, -rescus.getInt("c_advance"));
                                }
                            } else {
                                if (rescus.getInt("c_d_amount") - extra <= 0) {
                                    updatecustomer.setInt(2, -rescus.getInt("c_d_amount"));
                                    updatecustomer.setInt(3, extra - rescus.getInt("c_d_amount"));
                                } else {
                                    updatecustomer.setInt(2, -extra);
                                    updatecustomer.setInt(3, 0);
                                }
                            }
                        } else {
                            updatecustomer.setInt(2, 0);
                            updatecustomer.setInt(3, extra);
                        }
                    } else if (Integer.parseInt(paid_amount.getText()) - subtotal == 0) {
                        updatecustomer.setInt(2, 0);
                        updatecustomer.setInt(3, 0);
                    } else {
                        if (rescus.getInt("c_d_amount") >= 0) {
                            if (rescus.getInt("c_advance") > 0) {
                                if (rescus.getInt("c_d_amount") - rescus.getInt("c_advance") > 0) {
                                    updatecustomer.setInt(2, -(rescus.getInt("c_advance") + extra));
                                    updatecustomer.setInt(3, -rescus.getInt("c_advance"));
                                } else if (rescus.getInt("c_d_amount") - rescus.getInt("c_advance") == 0) {
                                    updatecustomer.setInt(2, -rescus.getInt("c_d_amount") - extra);
                                    updatecustomer.setInt(3, -rescus.getInt("c_advance"));
                                } else {
                                    if ((rescus.getInt("c_advance") + extra) < 0) {
                                        updatecustomer.setInt(2, -(rescus.getInt("c_advance") + extra));
                                        updatecustomer.setInt(3, -(rescus.getInt("c_advance")));
                                    } else {
                                        updatecustomer.setInt(2, -(rescus.getInt("c_d_amount")));
                                        updatecustomer.setInt(3, extra);
                                    }
                                }
                            } else {
                                updatecustomer.setInt(2, -extra);
                                updatecustomer.setInt(3, 0);
                            }
                        } else {
                            updatecustomer.setInt(2, -extra);
                            updatecustomer.setInt(3, 0);
                        }
                    }
                    updatecustomer.setInt(4, subquantity);
                    updatecustomer.execute();
                }
                for (Model_Cart cart : cart_table.getItems()) {
                    String company = null;
                    if (cart.getCompany().equals("Union Polymer")) {
                        company = "tb_upoly";
                    } else {
                        company = "tb_" + cart.getCompany();
                    }
                    String change = "UPDATE " + company + " SET quantity=quantity-" + cart.getQuantity() + " WHERE product_name = '" + cart.getProduct_Name() + "'" +
                            " && orientation = '" + cart.getOrientation() + "'";
                    PreparedStatement ps = Main.conn.prepareStatement(change);
                    ps.execute();
                }
                String getbillno = "SELECT * FROM tb_sales";
                PreparedStatement bill = Main.conn.prepareStatement(getbillno);
                ResultSet billno = bill.executeQuery();
                billno.last();
                int billnumber = billno.getInt("bill_no")+1;

                for(Model_Cart sale : cart_table.getItems())
                {
                    String sales = "INSERT INTO tb_sales(bill_no,product_name,Orientation,Quantity,Price,customer_name,total_price,paid_amount,due_amount,sold_by,sold_date) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement preparesales = Main.conn.prepareStatement(sales);
                    preparesales.setInt(1,billnumber);
                    preparesales.setString(2,sale.getProduct_Name());
                    preparesales.setString(3,sale.getOrientation());
                    preparesales.setInt(4,sale.getQuantity());
                    preparesales.setInt(5,Integer.parseInt(sale.getPrice().getText()));
                    preparesales.setString(6,cus_name.getText());
                    preparesales.setInt(7,subtotal);
                    preparesales.setInt(8,Integer.parseInt(paid_amount.getText()));
                    if(due>0) {
                        preparesales.setInt(9, due);
                    }
                    else
                    {
                        preparesales.setInt(9,0);
                    }
                    preparesales.setString(10,Login.username);
                    preparesales.setString(11,Date.getText());
                    preparesales.execute();
                }

                Stage receipt = new Stage();
                AnchorPane root = new AnchorPane();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Assets/FXMLs/Receipt.fxml"));
                root = loader.load();
                Receipt obj = loader.getController();
                obj.getItems(list, cus_name.getText(), cus_address.getText(), discount.getText(), paid_amount.getText(),billnumber);
                Scene scene = new Scene(root);
                scene.getStylesheets().addAll(getClass().getResource("/Assets/StyleSheet/Receipt.css").toExternalForm());
                receipt.setScene(scene);
                receipt.setTitle("Receipt");
                receipt.setX(400);
                receipt.setY(20);
                receipt.show();
                Main.Loader("/Assets/FXMLs/Bill.fxml");
                Printer printer = Printer.getDefaultPrinter();
                PrinterJob job = PrinterJob.createPrinterJob();
                PageLayout layout = printer.createPageLayout(Paper.A3, PageOrientation.PORTRAIT,60.0,0.0,0.0,0.0);
                if(job!=null)
                {
                    job.printPage(layout,obj.printablearea);
                    job.endJob();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Purchase Canceled");
            }
        }
    }
    @FXML
    void Clear(ActionEvent event) throws IOException{
        Alert clear = new Alert(Alert.AlertType.CONFIRMATION);
        clear.setTitle("Clear the fields");
        clear.setContentText("Are you sure you want to clear the customer information");
        Optional<ButtonType> result = clear.showAndWait();
        if(result.get()==ButtonType.OK)
        {
            cus_name.clear();
            cus_contact.clear();
            cus_address.clear();
            discount.clear();
        }
    }
    @FXML
    void Remove(ActionEvent event) throws IOException{
        if(cart_table.getSelectionModel().getSelectedIndices().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Please choose a product");
        }
        else
        {
            Alert remove = new Alert(Alert.AlertType.CONFIRMATION);
            remove.setHeaderText("Are you Sure?");
            remove.setContentText("This will remove the item from the cart.");
            remove.setTitle("Delete Entry");
            Optional<ButtonType> result = remove.showAndWait();
            if(result.get()==ButtonType.OK) {
                cart_table.getItems().removeAll(cart_table.getSelectionModel().getSelectedItems());
            }
        }
    }

    @FXML
    void Search(KeyEvent event) throws IOException
    {
        if(event.getCode().isLetterKey() || event.getCode().equals(KeyCode.BACK_SPACE)) {
                try {
                    if (inv_search.getText().isEmpty())
                    {
                        loadTable("SELECT * FROM "+category+" WHERE category = '"+cat_combo.getValue()+"'");
                    }
                    else {

                        String search = "SELECT * FROM " +category+ " WHERE product_name LIKE '%" + inv_search.getText().toString() + "%' " +
                                "&& category = '"+cat_combo.getValue()+"'";
                        PreparedStatement prepare = Main.conn.prepareStatement(search);
                        ResultSet result = prepare.executeQuery();

                        while (result.next()) {
                            loadTable(search);
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
        }
    }
    @FXML
    void GetTotal(ActionEvent event)throws IOException
    {
        if(discount.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"Please provide a discount");
        }
        else {
            int total = 0;
            for (Model_Cart cart : cart_table.getItems()) {
                total = total + cart.getQuantity() * Integer.parseInt(cart.getPrice().getText());
            }
            total = total - ((total * Integer.parseInt(discount.getText())) / 100);
            Alert show = new Alert(Alert.AlertType.INFORMATION);
            show.setHeaderText("Total Cost : "+total);
            show.setTitle("Total Price");
            show.setContentText("You have to pay total " + total + " Taka");
            show.showAndWait();
        }
    }

    @FXML
    void Submit(ActionEvent event) throws IOException
    {
        if (comp_combo.getSelectionModel().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Please Select a company first");
        }
        else if(cat_combo.getSelectionModel().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Please Select a category first");
        }
        else {
            inv_search.setDisable(false);
            subcategory=cat_combo.getValue();
            company=comp_combo.getValue();
            String load = "SELECT * FROM "+category+" WHERE category = '"+cat_combo.getValue()+"' && quantity>0";
            loadTable(load);
            bill_quantity.requestFocus();
        }

    }
    private void loadTable(String query){
        try {
            ObservableList<Model_Bill> oblist = FXCollections.observableArrayList();
            PreparedStatement table_data=Main.conn.prepareStatement(query);
            ResultSet result=table_data.executeQuery();
            while(result.next())
            {
                oblist.addAll(new Model_Bill(result.getString("product_name"),result.getString("orientation"),result.getString("category")));
            }inv_col_pn.setCellValueFactory(new PropertyValueFactory<>("Product_Name"));
            inv_col_ori.setCellValueFactory(new PropertyValueFactory<>("Orientation"));
            inv_col_cat.setCellValueFactory(new PropertyValueFactory<>("Category"));
            inv_table.setItems(oblist);
        }

        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
        }
    @FXML
    void Add(ActionEvent event) throws IOException {
        if (inv_table.getSelectionModel().getSelectedIndices().isEmpty())
           {
             JOptionPane.showMessageDialog(null, "Please choose a product");
             bill_quantity.clear();
           }
        else if(bill_quantity.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null, "Please select Quantity");
        }
        else if(Integer.parseInt(bill_quantity.getText())<0)
        {
            JOptionPane.showMessageDialog(null,"Sorry. Quantity cannot be negative.");
        }
        else {
            if (comp_combo.getValue().equals("Union Polymer"))
            {
                String company = "upoly";
            }
            else
            {
                company=comp_combo.getValue();
            }
            String quantity = "SELECT quantity FROM tb_"+company+" WHERE product_name = '"+inv_table.getSelectionModel().getSelectedItem().getProduct_Name()+"' && orientation='"+inv_table.getSelectionModel().getSelectedItem().getOrientation()+"'";
            try {

                PreparedStatement verify = Main.conn.prepareStatement(quantity);
                ResultSet number = verify.executeQuery();
                number.next();
                if(cart_table.getItems().isEmpty()) {
                    stock = number.getInt("quantity");
                }
                else
                {
                    for(int i =0;i<cart_table.getItems().size();i++) {
                        if(cart_table.getItems().get(i).getProduct_Name().equals(inv_table.getSelectionModel().getSelectedItem().getProduct_Name()) && cart_table.getItems().get(i).getOrientation().equals(
                                inv_table.getSelectionModel().getSelectedItem().getOrientation()))
                        {
                            stock = number.getInt("quantity")-cart_table.getItems().get(i).getQuantity();
                            break;
                        }
                        else
                        {
                            stock=number.getInt("quantity");
                        }
                    }
                }

                if((stock-Integer.parseInt(bill_quantity.getText())>=0))
                {
                    loadCartTable(Integer.parseInt(bill_quantity.getText()));

                }
                else
                {
                    JOptionPane.showMessageDialog(null,"Insufficient Stock. Current stock amount is "+stock);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        }

    public void loadCartTable(int quantity){
        try {
            Model_Cart cart =new Model_Cart(company,inv_table.getSelectionModel().getSelectedItem().getProduct_Name(),inv_table.getSelectionModel().getSelectedItem().getCategory(),
                    inv_table.getSelectionModel().getSelectedItem().getOrientation(),quantity,null);

            bill_quantity.setText("");
            ObservableList<Model_Cart> list = cart_table.getItems();
            if(!list.isEmpty()) {
                for (int i = 0; i < cart_table.getItems().size(); i++) {
                    if (cart_table.getItems().get(i).getProduct_Name().equals(cart.getProduct_Name())&&cart_table.getItems().get(i).getOrientation().equals(cart.getOrientation())) {
                        cart_table.getItems().get(i).setQuantity(cart_table.getItems().get(i).getQuantity() + quantity);
                        cart_table.refresh();
                        JOptionPane.showMessageDialog(null,"Item added to cart");
                        return;
                    }
                }
            }

            JOptionPane.showMessageDialog(null,"Item added to cart");
            cart_table.getItems().addAll(cart);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    @FXML
    void SetCombo() throws IOException{
        cat_combo.getItems().clear();
        try{
            if(comp_combo.getValue().equals("Union Polymer"))
            {
                category="tb_upoly";
            }
            else{
                category="tb_"+comp_combo.getValue().toString().toLowerCase()+"";
            }
            String combo = "SELECT * FROM "+category;
            PreparedStatement data = Main.conn.prepareStatement(combo);
            ResultSet result = data.executeQuery();
            result.next();
            int i=0;
            while(result.next()) {

                if(cat_combo.getItems().isEmpty())
                {
                    cat_combo.getItems().addAll(result.getString("category"));
                }
                else if(cat_combo.getItems().get(i).equals(result.getString("category")))
                {
                    continue;
                }
                else
                {
                    cat_combo.getItems().addAll(result.getString("category"));
                    i++;
                }

            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"Please Choose company first");
        }
    }

    void SetCompany(){
        comp_combo.getItems().addAll("Alibaba","Bravo","NPoly","RFL","Partex","Star","Union Polymer");
        }
    void set_drawer()
    {
        draw.setDisable(true);
        draw.setVisible(false);
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/Assets/FXMLs/Drawer.fxml"));
            draw.setSidePane(box);
            for(Node node :box.getChildren()){
                if(node.getAccessibleText() !=null)
                {
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
                        try {
                            switch (node.getAccessibleText()) {
                                case "inv_btn":
                                    Main.Loader("/Assets/FXMLs/Brands.fxml");
                                    break;
                                case "emp_btn":
                                    Main.Loader("/Assets/FXMLs/CheckEmployee.fxml");
                                    break;
                                case "crt_btn":
                                    Main.Loader("/Assets/FXMLs/CreateInventory.fxml");
                                    break;
                                case "edit_btn":
                                    Main.Loader("/Assets/FXMLs/Edit_Inventory.fxml");
                                    break;
                                case "cus_btn":
                                    Main.Loader("/Assets/FXMLs/CheckCustomer.fxml");
                                    break;
                                case "sales_btn":
                                    Main.Loader("/Assets/FXMLs/CheckBills.fxml");
                                    break;
                                case "bill_btn":
                                    Main.Loader("/Assets/FXMLs/Bill.fxml");
                                    break;
                                case"logout_btn":
                                    ActionEvent e = new ActionEvent();
                                    Log_Out(e);
                                    break;

                            }
                        }
                        catch(IOException e)
                        {
                            System.out.println(e);
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        HamburgerBackArrowBasicTransition burgertask2 = new HamburgerBackArrowBasicTransition(nav_burg);
        burgertask2.setRate(-1);
        nav_burg.addEventHandler(MouseEvent.MOUSE_PRESSED,(event -> {
            burgertask2.setRate(burgertask2.getRate() * -1);
            burgertask2.play();
            if(draw.isOpened())
            {
                draw.close();
                draw.setDisable(true);
                draw.setVisible(false);
            }
            else
            {
                draw.setDisable(false);
                draw.setVisible(true);
                draw.open();
            }
        }));
    }
    void Initialize_cart()
    {
        cart_col_comp.setCellValueFactory(new PropertyValueFactory<>("Company"));
        cart_col_product_name.setCellValueFactory(new PropertyValueFactory<>("Product_Name"));
        cart_col_cat.setCellValueFactory(new PropertyValueFactory<>("Category"));
        cart_col_orientation.setCellValueFactory(new PropertyValueFactory<>("Orientation"));
        cart_col_quantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        cart_col_price.setCellValueFactory(new PropertyValueFactory<>("Price"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clocktime();
        ///Setting tooltip for close button
        Tooltip close = new Tooltip("Close");
        close.setShowDelay(Duration.seconds(.1));
        Tooltip.install(exit_btn,close);
        ///Setting tooltip for maximize button
        Tooltip maximize = new Tooltip("Maximize");
        maximize.setShowDelay(Duration.seconds(.1));
        Tooltip.install(maximize_btn,maximize);
        ///Setting tooltip for minimize button
        Tooltip minimize = new Tooltip("Minimize");
        minimize.setShowDelay(Duration.seconds(.1));
        Tooltip.install(minimize_btn,minimize);
        inv_search.setDisable(true);
        set_drawer();
        SetCompany();
        Initialize_cart();
    }
}

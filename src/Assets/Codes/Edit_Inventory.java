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
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class Edit_Inventory implements Initializable
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
    private Label Date;

    @FXML
    private TableView<Model_Products> inv_table;

    @FXML
    private TableColumn<Model_Products, String> inv_col_pn;

    @FXML
    private TableColumn<Model_Products, String> inv_col_ori;

    @FXML
    private TableColumn<Model_Products, String> inv_col_cat;

    @FXML
    private TableColumn<Model_Products, Integer> inv_col_stck;

    @FXML
    private JFXTextField inv_search;

    @FXML
    private JFXComboBox<String> comp_combo;

    @FXML
    private JFXComboBox<String> cat_combo;

    @FXML
    private JFXButton inv_btn;

    @FXML
    private JFXButton update_btn;

    @FXML
    private JFXButton Delete_btn;

    @FXML
    private JFXTextField pn_field;

    @FXML
    private JFXTextField ori_field;

    @FXML
    private JFXTextField cat_field;

    @FXML
    private JFXTextField stock_field;

    @FXML
    private JFXHamburger nav_burg;

    @FXML
    private JFXDrawer draw;

    @FXML
    private Pane clock;

    @FXML
    private Pane date;

    @FXML
    private JFXButton chng_btn;

    private double Xoffset=0;
    private double Yoffset=0;
    String category=null;
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
            String load = "SELECT * FROM "+category+" WHERE category = '"+cat_combo.getValue()+"'";
            loadTable(load);
        }

    }

    @FXML
    void Apply(KeyEvent event)throws IOException
    {
        if(event.getCode().equals(KeyCode.ENTER))
        {
            ActionEvent e = new ActionEvent();
            Submit(e);
        }
    }

    private void loadTable(String query){
        try{
            ObservableList<Model_Products> oblist = FXCollections.observableArrayList();
            PreparedStatement table_data=Main.conn.prepareStatement(query);
            ResultSet result=table_data.executeQuery();
            while(result.next())
            {
                oblist.addAll(new Model_Products(result.getInt("product_id"),
                        result.getString("product_name"),result.getString("category"),
                        result.getString("orientation"),result.getInt("quantity")));
            }
            inv_col_pn.setCellValueFactory(new PropertyValueFactory<>("Product_Name"));
            inv_col_ori.setCellValueFactory(new PropertyValueFactory<>("Orientation"));
            inv_col_cat.setCellValueFactory(new PropertyValueFactory<>("Category"));
            inv_col_stck.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
            inv_table.setItems(oblist);
            inv_table.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, NewSelection)-> {
            if(NewSelection!=null)
            {
                ShowDetails();
            }
            });
        }

        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
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

    void SetCompany() throws IOException{
        comp_combo.getItems().addAll("Alibaba","Bravo","NPoly","RFL","Partex","Star","Union Polymer");
        }
    @FXML
    void Delete(ActionEvent event) throws IOException, SQLException{
            Alert del = new Alert(Alert.AlertType.CONFIRMATION);
            del.setTitle("Confirm Delete");
            del.setHeaderText("This will delete entry from the database");
            del.setContentText("Are you sure you want to continue?");
        Optional<ButtonType> result = del.showAndWait();
        if(result.get()==ButtonType.OK) {
            String query = "DELETE FROM " + category + " WHERE product_name='" + pn_field.getText() + "' && orientation= '" + ori_field.getText() + "'";
            PreparedStatement delete = Main.conn.prepareStatement(query);
            delete.execute();
            loadTable("SELECT * FROM " + category + " WHERE category = '" + cat_combo.getValue() + "'");
        }

    }
    @FXML
    void Update(ActionEvent event) throws IOException, SQLException {
        String quantity ="SELECT * FROM "+category+" WHERE product_name='"+pn_field.getText()+"' && orientation = '"+ori_field.getText()+"'";
        PreparedStatement ps = Main.conn.prepareStatement(quantity);
        ResultSet rs = ps.executeQuery();
        rs.next();
        if(Login.user_type.equals("employee"))
        {
           if (Integer.parseInt(stock_field.getText()) < 0) {
                JOptionPane.showMessageDialog(null, "Invalid Amount");
            }
            else {
            Alert updatemsg = new Alert(Alert.AlertType.CONFIRMATION);
            updatemsg.setTitle("Confirm changing the stock???");
            updatemsg.setHeaderText("This will change the stock");
            updatemsg.setContentText("Are you sure you want to continue?");
            Optional<ButtonType> result = updatemsg.showAndWait();
            if(result.get()==ButtonType.OK) {

                String query = "UPDATE " + category + " SET quantity= quantity +" + Integer.parseInt(stock_field.getText()) + " WHERE product_name ='" + pn_field.getText() + "' && orientation = '" + ori_field.getText() + "'";
                PreparedStatement update = Main.conn.prepareStatement(query);
                update.execute();
                loadTable("SELECT * FROM " + category + " WHERE category = '" + cat_combo.getValue() + "'");
            }}
        }
        else {
            if (Integer.parseInt(stock_field.getText()) < 0) {
                JOptionPane.showMessageDialog(null, "Invalid Amount");
            }
            else {
                Alert updatemsg = new Alert(Alert.AlertType.CONFIRMATION);
                updatemsg.setTitle("Confirm changing the stock???");
                updatemsg.setHeaderText("This will change the stock");
                updatemsg.setContentText("Are you sure you want to continue?");
                Optional<ButtonType> result = updatemsg.showAndWait();
                if(result.get()==ButtonType.OK) {
                String query = "UPDATE " + category + " SET quantity= quantity +" + Integer.parseInt(stock_field.getText()) + " WHERE product_name ='" + pn_field.getText() + "' && orientation = '" + ori_field.getText() + "'";
                PreparedStatement update = Main.conn.prepareStatement(query);
                update.execute();
                loadTable("SELECT * FROM " + category + " WHERE category = '" + cat_combo.getValue() + "'");
            }}
        }
    }
    @FXML
    void Change(ActionEvent event) throws IOException, SQLException
    {
        if (Integer.parseInt(stock_field.getText()) < 0) {
            JOptionPane.showMessageDialog(null, "Invalid Amount");
        }
            else{
                Alert change = new Alert(Alert.AlertType.CONFIRMATION);
                change.setTitle("Confirm changing the stock???");
                change.setHeaderText("This will change the stock");
                change.setContentText("Are you sure you want to continue?");
                Optional<ButtonType> result = change.showAndWait();
                if (result.get() == ButtonType.OK) {
                    String query = "UPDATE " + category + " SET quantity= " + Integer.parseInt(stock_field.getText()) + " WHERE product_name ='" + pn_field.getText() + "' && orientation = '" + ori_field.getText() + "'";
                    PreparedStatement update = Main.conn.prepareStatement(query);
                    update.execute();
                    loadTable("SELECT * FROM " + category + " WHERE category = '" + cat_combo.getValue() + "'");
                }
            }
    }
    public void ShowDetails()
    {
        pn_field.setText(inv_table.getSelectionModel().getSelectedItem().Product_Name);
        ori_field.setText(inv_table.getSelectionModel().getSelectedItem().Orientation);
        cat_field.setText(inv_table.getSelectionModel().getSelectedItem().Category);
        String quantity = Integer.toString(inv_table.getSelectionModel().getSelectedItem().Quantity);
        stock_field.setText(quantity);
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
        try {
            SetCompany();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        if(Login.user_type=="employee")
        {
            chng_btn.setDisable(true);
            Delete_btn.setDisable(true);
            stock_field.setPromptText("Add Stock");
        }
        set_drawer();
    }
}

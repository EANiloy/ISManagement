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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Inventory implements Initializable
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
    private Pane clock;

    @FXML
    private Pane date;

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
    private JFXComboBox<String> inv_combo;

    @FXML
    private JFXRadioButton inv_toggle_btn;

    @FXML
    private JFXButton inv_btn;

    @FXML
    private JFXHamburger nav_burg;

    @FXML
    private JFXDrawer draw;

    private double Xoffset=0;
    private double Yoffset=0;
    String defaultquery = "Select * FROM "+Brands.tb_name+" WHERE quantity>0";

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
    void Go_back(MouseEvent event) throws IOException{
        Main.Loader("/Assets/FXMLs/Brands.fxml");
    }

    @FXML
    void Search(KeyEvent event) throws IOException
    {
        try {
            if(inv_search.getText().isEmpty())
            {
                loadTable(defaultquery);
            }
            else{

                String search = "SELECT * FROM " + Brands.tb_name + " WHERE product_name LIKE '%" +inv_search.getText().toString()+"%'";
                PreparedStatement prepare = Main.conn.prepareStatement(search);
                ResultSet result = prepare.executeQuery();

                while(result.next())
                {
                    loadTable(search);
                }
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
    }

    @FXML
    void Submit(ActionEvent event) throws IOException
    {
        if (inv_combo.getSelectionModel().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Please Select a category first");
        }
        else {
            String category = "SELECT * FROM " + Brands.tb_name + " WHERE category = '" + inv_combo.getValue() + "'";
            loadTable(category);
        }

    }

    @FXML
    void Unavailable(ActionEvent event) throws IOException
    {
        if (inv_toggle_btn.isSelected())
        {
       String unavail = "SELECT * FROM "+Brands.tb_name+" WHERE quantity = 0";
       loadTable(unavail);}
        else
        {
            loadTable(defaultquery);
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
        }

        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
    }
    void setCombo(){
        try{
            String combo = "SELECT * FROM " + Brands.tb_name;
            PreparedStatement data = Main.conn.prepareStatement(combo);
            ResultSet result = data.executeQuery();
            result.next();
            int i=0;
            while(result.next()) {

                if(inv_combo.getItems().isEmpty())
                {
                    inv_combo.getItems().addAll(result.getString("category"));
                }
                else if(inv_combo.getItems().get(i).equals(result.getString("category")))
                {
                    continue;
                }
                else
                {
                    inv_combo.getItems().addAll(result.getString("category"));
                    i++;
                }

            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
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
        loadTable(defaultquery);
        setCombo();
        set_drawer();
    }
}

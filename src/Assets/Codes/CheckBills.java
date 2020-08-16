package Assets.Codes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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

import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

public class CheckBills implements Initializable {

    @FXML
    private AnchorPane Drag_handle;

    @FXML
    private Circle minimize_btn;

    @FXML
    private Circle maximize_btn;

    @FXML
    private Circle exit_btn;

    @FXML
    private JFXHamburger nav_burg;

    @FXML
    private Pane clock;

    @FXML
    private Label Date;

    @FXML
    private Label clock_label1;

    @FXML
    private Pane date;

    @FXML
    private Label Clock_time;

    @FXML
    private Label clock_label;

    @FXML
    private Pane back_btn;

    @FXML
    private JFXComboBox<String> chk_cus;

    @FXML
    private JFXComboBox<Integer> chk_bill;

    @FXML
    private JFXButton submit_btn;

    @FXML
    private Label msg_label;

    @FXML
    private JFXDrawer draw;


    private double Xoffset=0;
    private double Yoffset=0;

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
        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
            Clock_time.setText("   "+LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE :  hh :  mm :  ss  a")));
        }),new KeyFrame(Duration.seconds(1)));
        Date.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("   dd   :   LLL   :   yyyy   ")));
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }
    @FXML
    void Go_back(MouseEvent event) throws IOException{
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
    void setBill (ActionEvent event)throws IOException,SQLException{
        String combo = "SELECT * FROM tb_sales";
        PreparedStatement data = Main.conn.prepareStatement(combo);
        ResultSet result = data.executeQuery();
        if(!result.next())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("No Bill in Database");
            alert.setTitle("Bills not found");
            alert.setContentText("Sorry! It seems you have not sold anything yet.");
            Optional<ButtonType> btn = alert.showAndWait();
            if(btn.get()==ButtonType.OK || btn.get()==ButtonType.CANCEL) {
                if (Login.user_type.equals("owner")) {
                    Main.Loader("/Assets/FXMLs/OwnerPage.fxml");
                } else {
                    Main.Loader("/Assets/FXMLs/EmployeePage.fxml");
                }
            }
        }
        else {
            int i = 0;
            while (result.next()) {
                if (chk_bill.getItems().isEmpty()) {
                    chk_bill.getItems().addAll(result.getInt("bill_no"));
                } else if (chk_bill.getItems().get(i).equals(result.getInt("bill_no"))) {
                    continue;
                } else {
                    chk_bill.getItems().addAll(result.getInt("bill_no"));
                    i++;
                }

            }
        }

    }
    @FXML
    void Submit(ActionEvent event)throws IOException{
        if(chk_cus.getItems().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Please Choose Customer");
        }
        else if (chk_bill.getItems().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Please Choose Bill No.");
        }
        else {
            AnchorPane root = new AnchorPane();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Assets/FXMLs/CheckSales.fxml"));
            root = loader.load();
            CheckSales obj = loader.getController();
            obj.getinfo(chk_cus.getValue(), chk_bill.getValue());
            Main.scene.setRoot(root);
            Main.scene.getStylesheets().addAll(getClass().getResource("/Assets/StyleSheet/Style.css").toExternalForm());
            Main.stage.setScene(Main.scene);
        }
    }
    void setcusbox() throws IOException,SQLException
    {
        String combo = "SELECT * FROM tb_sales";
        PreparedStatement data = Main.conn.prepareStatement(combo);
        ResultSet result = data.executeQuery();
        if(!result.next())
        {
                chk_cus.getItems().addAll("No bills in the database.");
        }
        else {
            int i = 0;
            while (result.next()) {

                if (chk_cus.getItems().isEmpty()) {
                    chk_cus.getItems().addAll(result.getString("customer_name"));
                } else if (chk_cus.getItems().get(i).equals(result.getString("customer_name"))) {
                    continue;
                } else {
                    chk_cus.getItems().addAll(result.getString("customer_name"));
                    i++;
                }

            }
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
                    node.addEventHandler(MOUSE_CLICKED, event -> {
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
        set_drawer();
        try {
            setcusbox();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

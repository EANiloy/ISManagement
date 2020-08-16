package Assets.Codes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CreateInventory implements Initializable
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
    private JFXTextField comp_name_field;

    @FXML
    private JFXTextField productname_field;

    @FXML
    private JFXTextField cat_field;

    @FXML
    private JFXTextField orientation_field;

    @FXML
    private JFXTextField quantity_field;

    @FXML
    private JFXButton submit_btn;

    @FXML
    private Label msg_label;

    @FXML
    private JFXHamburger nav_burg;

    @FXML
    private JFXDrawer draw;

    @FXML
    private Label Date;

    @FXML
    private Pane clock;

    @FXML
    private Pane date;

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
        Timeline time = new Timeline(new KeyFrame(Duration.ZERO,actionEvent -> {
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
    void Submit(ActionEvent event) throws IOException
    {
        if(comp_name_field.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Company Name cannot be empty");
        }
        else if(productname_field.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Product name cannot be empty");
        }
        else if(cat_field.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Category field cannot be empty");
        }
        else if(orientation_field.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Orientation field cannot be empty");
        }
        else if(quantity_field.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Quantity field cannot be empty");
        }
        else{
        try {
            ResultSet data = Main.conn.getMetaData().getTables("db_ussc", null, null, new String[]{"TABLE"});
            int id = 0;
            int flag = 0;
            String tbname = null;
            if (comp_name_field.getText().equals("Union Polymer")) {
                tbname = "tb_upoly";
            } else {
                tbname = "tb_" + comp_name_field.getText().replaceAll("\\s+", "").toLowerCase();
            }
            while (data.next()) {
                if (data.getString("TABLE_NAME").equals(tbname)) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 1) {
                ResultSet rs = Main.conn.prepareStatement("SELECT * FROM " + tbname).executeQuery();
                while (rs.next()) {
                    if (rs.last()) {
                        id = (rs.getInt("product_id") + 1);
                    }
                }
                String query = "INSERT INTO " + tbname + " (product_id,product_name,category,orientation,quantity) VALUES(?,?,?,?,?)";
                PreparedStatement prep = Main.conn.prepareStatement(query);
                prep.setInt(1, id);
                prep.setString(2, productname_field.getText());
                prep.setString(3, cat_field.getText());
                prep.setString(4, orientation_field.getText());
                prep.setInt(5, Integer.parseInt(quantity_field.getText()));
                prep.execute();
                msg_label.setStyle("-fx-background-color:rgb(0,198,0)");
                msg_label.setVisible(true);
                msg_label.setText("Account Created Successfully");
            } else {
                PreparedStatement create = Main.conn.prepareStatement("CREATE TABLE " + tbname + " (product_id INT(10) NOT NULL AUTO_INCREMENT,product_name VARCHAR(50) NULL," +
                        "category VARCHAR(50) NULL,orientation VARCHAR(15) NULL,quantity INT(10) NULL, PRIMARY KEY(product_id))");
                System.out.println(create);
                create.execute();
                PreparedStatement insert = Main.conn.prepareStatement("INSERT INTO "+tbname+" (product_id,product_name,category,orientation,quantity) VALUES(?,?,?,?,?)");
                insert.setInt(1, id);
                insert.setString(2, productname_field.getText());
                insert.setString(3, cat_field.getText());
                insert.setString(4, orientation_field.getText());
                insert.setInt(5, Integer.parseInt(quantity_field.getText()));
                insert.execute();
                msg_label.setStyle("-fx-background-color:rgb(0,198,0)");
                msg_label.setVisible(true);
                msg_label.setText("Data Inserted Successfully");
                PauseTransition ps = new PauseTransition(Duration.seconds(2));
                ps.play();
                ps.setOnFinished(actionEvent -> {
                    try {
                        Main.Loader("/Assets/FXMLs/CreateInventory.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
        } catch (Exception e) {
            msg_label.setStyle("-fx-background-color:rgb(215,22,6)");
            msg_label.setVisible(true);
            msg_label.setText("Data Insertion Failed.");
            PauseTransition ps = new PauseTransition(Duration.seconds(2));
            ps.play();
            ps.setOnFinished(actionEvent -> {
                try {
                    Main.Loader("/Assets/FXMLs/CreateInventory.fxml");
                } catch (IOException io) {
                    e.printStackTrace();
                }
            });
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
        msg_label.setVisible(false);
        set_drawer();
    }
}

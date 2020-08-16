package Assets.Codes;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class EmployeePage implements Initializable {

    @FXML
    private AnchorPane Drag_handle;

    @FXML
    private Circle minimize_btn;

    @FXML
    private Circle maximize_btn;

    @FXML
    private Circle exit_btn;

    @FXML
    private GridPane Owner_grid;

    @FXML
    private JFXButton inventory_btn;

    @FXML
    private Label Clock_time;

    @FXML
    private JFXButton Log_Out;

    @FXML
    private JFXButton crt_inv_btn;

    @FXML
    private JFXHamburger employee_burg;

    @FXML
    private JFXDrawer employee_drawer;

    @FXML
    private Pane clock;

    @FXML
    private Pane date;
    @FXML
    private Label Date;


    private double Xoffset=0;
    private double Yoffset=0;

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
    @FXML
    void Keypress(KeyEvent event) throws IOException{
        if(event.getCode()== KeyCode.ENTER)
        {
            ActionEvent key = new ActionEvent();
            System.out.println(event.getSource().equals("Check Inventory"));
        }
    }
    @FXML
    public void Log_Out(ActionEvent event)throws IOException{
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
    public void Inventory(ActionEvent event)throws IOException{
        Main.Loader("/Assets/FXMLs/Brands.fxml");
    }
    @FXML
    public void Edit_Inventory(ActionEvent event)throws IOException
    {
        Main.Loader("/Assets/FXMLs/Edit_Inventory.fxml");
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
    public void CheckEmployee(ActionEvent event) throws IOException{
        Main.Loader("/Assets/FXMLs/CheckEmployee.fxml");
    }
    @FXML
    public void CreateInventory(ActionEvent event) throws IOException{
        Main.Loader("/Assets/FXMLs/CreateInventory.fxml");
    }
    @FXML
    public void CheckSales(ActionEvent event)throws IOException{
        Main.Loader("/Assets/FXMLs/CheckBills.fxml");
    }
    @FXML
    public void CheckCustomer(ActionEvent event) throws IOException
    {
        Main.Loader("/Assets/FXMLs/CheckCustomer.fxml");
    }
    @FXML
    public void CreateBill(ActionEvent event) throws IOException{
        Main.Loader("/Assets/FXMLs/Bill.fxml");
    }
    void check_attendance() throws IOException{

        CheckAttendance.name="tb_"+Login.user;
        Stage stg = new Stage();
        stg.setHeight(700);
        stg.setWidth(1000);
        FXMLLoader load = new FXMLLoader(Main.class.getResource("/Assets/FXMLs/CheckAttendance.fxml"));
        AnchorPane anc = new AnchorPane();
        anc = load.load();
        Scene scn = new Scene(anc);
        scn.getStylesheets().addAll(Main.class.getResource("/Assets/StyleSheet/Style.css").toExternalForm());
        stg.setScene(scn);
        stg.setTitle("Attendance");
        stg.getIcons().add(new Image("/Assets/Images/Icon.png"));
        stg.show();
    }
    void reset_pass()throws IOException {
        ForgotPass.user = Login.user;
        Stage st = new Stage();
        st.getIcons().add(new Image("/Assets/Images/Icon.png"));
        st.setMaxWidth(400);
        st.setMaxHeight(300);
        AnchorPane anc = new AnchorPane();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Assets/FXMLs/ForgotPass.fxml"));
        anc = loader.load();
        Scene scn = new Scene(anc);
        scn.getStylesheets().addAll(Main.class.getResource("/Assets/StyleSheet/Style.css").toExternalForm());
        st.setTitle("Reset Password");
        st.setScene(scn);
        st.show();
    }
    void set_drawer()
    {
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/Assets/FXMLs/EmployeeDrawer.fxml"));
            employee_drawer.setSidePane(box);
            for(Node node :box.getChildren()){
                if(node.getAccessibleText() !=null)
                {
                    if(node.getAccessibleText().equals("welcome_text"))
                    {
                        Label lb = (Label) box.getChildren().get(3);
                        lb.setText(Login.username);
                    }
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
                        try {
                            switch (node.getAccessibleText()) {
                                case "chk_attendance":
                                    check_attendance();
                                    break;
                                case "change_password":
                                    reset_pass();
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
        HamburgerBackArrowBasicTransition burgertask2 = new HamburgerBackArrowBasicTransition(employee_burg);
        burgertask2.setRate(-1);
        employee_burg.addEventHandler(MouseEvent.MOUSE_PRESSED,(event -> {
            burgertask2.setRate(burgertask2.getRate() * -1);
            burgertask2.play();
            if(employee_drawer.isOpened())
            {
                employee_drawer.close();
            }
            else
            {
                employee_drawer.open();
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


}

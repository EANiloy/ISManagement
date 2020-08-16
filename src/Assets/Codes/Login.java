package Assets.Codes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
import java.util.ResourceBundle;


public class Login implements Initializable {
    @FXML
    private Label Login_msg;

    @FXML
    private Label Login_Notice;

    @FXML
    private JFXTextField Login_user;

    @FXML
    private JFXPasswordField Login_pass;

    @FXML
    private JFXButton Login_btn;

    @FXML
    private ImageView Login_logo;

    @FXML
    private AnchorPane Drag_handle;

    @FXML
    private Circle minimize_btn;

    @FXML
    private Circle maximize_btn;

    @FXML
    private Circle exit_btn;

    private double Xoffset=0;
    private double Yoffset=0;
    public static String user_type=null;
    public static String user=null;
    public static String username=null;

    public String Log(String user,String Pass) {
        try {
            String connectquery = "SELECT * FROM tb_user";
            PreparedStatement ps = Main.conn.prepareStatement(connectquery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String user_name = rs.getString("user_name");
                String password = rs.getString("password");
                if ((user.equals(user_name)) && (Pass.equals(password))) {
                    return rs.getString("user_type");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Invalid user";
    }
    @FXML
    public void Login(ActionEvent event) throws IOException, SQLException {

        if(Login_user.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"Please Enter Username");
        }
        else if(Login_pass.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Please Enter Password");
        }
        else {
            ResultSet rs = null;
                String select = "SELECT * FROM tb_user";
                PreparedStatement ps = Main.conn.prepareStatement(select);
                rs = ps.executeQuery();
                    if (Login_pass.getText().equals("USSC101PASS%NULL")) {
                        Stage st = new Stage();
                        st.getIcons().add(new Image("/Assets/Images/Icon.png"));
                        ForgotPass.user = Login_user.getText();
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
            else if (Log(Login_user.getText(), Login_pass.getText()).equals("owner")) {
                user_type="owner";
                user=Login_user.getText().replaceAll("\\s+","");
                username=Login_user.getText();
                try{
                    while(rs.next()) {
                        if(rs.getString("user_name").equals(Login_user.getText())){
                        String login = "INSERT INTO tb_" + user + " (date,login_time,logout_time) VALUES(?,?,DEFAULT)";
                        PreparedStatement insert = Main.conn.prepareStatement(login);
                        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                        String time= LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
                        insert.setString(1, date);
                        insert.setString(2,time);
                        insert.execute();
                    }
                    }
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(null,e);
                }
                Main.Loader("/Assets/FXMLs/OwnerPage.fxml");
            }
            else if (Log(Login_user.getText(), Login_pass.getText()).equals("employee")) {
                user_type="employee";
                        user=Login_user.getText().replaceAll("\\s+","");
                        username=Login_user.getText();
                        try{
                            while(rs.next()) {
                                if(rs.getString("user_name").equals(Login_user.getText())){
                                    String login = "INSERT INTO tb_" + user + " (date,login_time,logout_time) VALUES(?,?,DEFAULT)";
                                    PreparedStatement insert = Main.conn.prepareStatement(login);
                                    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                                    insert.setString(1, date);
                                    String time= LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
                                    insert.setString(2,time);
                                    insert.execute();
                                }
                            }
                        }
                        catch(Exception e)
                        {
                            JOptionPane.showMessageDialog(null,e);
                        }
                Main.Loader("/Assets/FXMLs/EmployeePage.fxml");

            }
            else if(Login_pass.getText().equals("Newuser"))
            {
                NewUser.username=Login_user.getText();
                Stage st1 = new Stage();
                st1.getIcons().add(new Image("/Assets/Images/Icon.png"));
                st1.setTitle("New User");
                st1.setMaxWidth(700);
                st1.setMaxHeight(500);
                AnchorPane anc = new AnchorPane();
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Assets/FXMLs/NewUser.fxml"));
                anc = loader.load();
                Scene scn = new Scene(anc);
                scn.getStylesheets().addAll(Main.class.getResource("/Assets/StyleSheet/Style.css").toExternalForm());
                st1.setTitle("New User");
                st1.setScene(scn);
                st1.show();
            }
            else {
                JOptionPane.showMessageDialog(null, "Invalid username or password.Please check your credentials");
            }
        }
    }
    @FXML
    void Keypress(KeyEvent event) throws IOException,SQLException{
        if(event.getCode()==KeyCode.ENTER)
        {
            ActionEvent key = new ActionEvent();
            if(event.getSource().getClass().getName().contains("JFXTextField")){
                Login_pass.requestFocus();
            }
           if(event.getSource().getClass().getName().contains("JFXPasswordField")){
               Login(key);
           }
        }
    }
    @FXML
    void Close(MouseEvent event) throws IOException, SQLException{
        Main.conn.close();
        System.exit(0);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
    }
    }
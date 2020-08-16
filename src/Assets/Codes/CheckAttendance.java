package Assets.Codes;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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

public class CheckAttendance implements Initializable
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
    private JFXTextField attendance_srch;

    @FXML
    private TableView<Model_Attendance> inv_table;

    @FXML
    private TableColumn<Model_Attendance, String> date_col;

    @FXML
    private TableColumn<Model_Attendance, String>  login_col;

    @FXML
    private TableColumn<Model_Attendance, String>  logout_col;

    private double Xoffset=0;
    private double Yoffset=0;
    public static String name=null;
    String defaultquery = "Select * FROM "+name;

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
    @FXML
    void Search(KeyEvent event) throws IOException
    {
        try {
            if(attendance_srch.getText().isEmpty())
            {
                loadTable(defaultquery);
            }
            else{

                String search = "SELECT * FROM " + name + " WHERE date LIKE '%" +attendance_srch.getText().toString()+"%'";
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


    public void loadTable(String query){
        try{
            ObservableList<Model_Attendance> oblist = FXCollections.observableArrayList();
            PreparedStatement table_data=Main.conn.prepareStatement(query);
            ResultSet result=table_data.executeQuery();
            while(result.next())
            {
                oblist.addAll(new Model_Attendance(result.getString("date"),
                        result.getString("login_time"),result.getString("logout_time")));
            }
            date_col.setCellValueFactory(new PropertyValueFactory<>("Date"));
            login_col.setCellValueFactory(new PropertyValueFactory<>("Login_time"));
            logout_col.setCellValueFactory(new PropertyValueFactory<>("Logout_time"));
            inv_table.setItems(oblist);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
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
        loadTable(defaultquery);
    }
}

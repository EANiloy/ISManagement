package Assets.Codes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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
import java.util.ResourceBundle;

public class CheckEmployee implements Initializable
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
    private JFXButton chk_attendance;

    @FXML
    private Label Clock_time;

    @FXML
    private TableView<Model_Employee> inv_table;

    @FXML
    private TableColumn<Model_Employee, Integer> emp_id_col;

    @FXML
    private TableColumn<Model_Employee, String> emp_name_col;

    @FXML
    private TableColumn<Model_Employee, String> emp_cont_col;

    @FXML
    private TableColumn<Model_Employee, String> emp_add_col;

    @FXML
    private JFXTextField inv_search;

    @FXML
    private JFXButton add_btn;

    @FXML
    private JFXButton update_btn;

    @FXML
    private JFXButton clear_btn;

    @FXML
    private JFXTextField en_field;

    @FXML
    private JFXTextField cont_field;

    @FXML
    private JFXTextField add_field;

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

    private double Xoffset=0;
    private double Yoffset=0;
    String category=null;
    String defaultquery = "Select * FROM tb_employees";
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
                        loadTable("SELECT * FROM tb_employees");
                    }
                    else {

                        String search = "SELECT * FROM tb_employees WHERE e_name LIKE '%"+inv_search.getText()+"%' ";
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

    private void loadTable(String query){
        try{
            ObservableList<Model_Employee> oblist = FXCollections.observableArrayList();
            PreparedStatement table_data=Main.conn.prepareStatement(query);
            ResultSet result=table_data.executeQuery();
            while(result.next())
            {
                oblist.addAll(new Model_Employee(result.getInt("e_id"),
                        result.getString("e_name"),result.getString("e_contact"),
                        result.getString("e_address")));
            }
            emp_id_col.setCellValueFactory(new PropertyValueFactory<>("emp_id"));
            emp_name_col.setCellValueFactory(new PropertyValueFactory<>("emp_name"));
            emp_cont_col.setCellValueFactory(new PropertyValueFactory<>("cont_no"));
            emp_add_col.setCellValueFactory(new PropertyValueFactory<>("address"));
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
    void Update(ActionEvent event) throws IOException, SQLException {
        String id ="SELECT * FROM tb_employees";
        PreparedStatement ps = Main.conn.prepareStatement(id);
        ResultSet rs = ps.executeQuery();
            if (en_field.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid Name");
            }
            else if(cont_field.getText().isEmpty())
            {
                JOptionPane.showMessageDialog(null,"Invalid Contact Number");
            }
            else if(add_field.getText().isEmpty())
            {
                JOptionPane.showMessageDialog(null,"Invalid Address");
            }
            else {
                while(rs.next()) {
                        if(inv_table.getSelectionModel().getSelectedItem().getEmp_id()==rs.getInt("e_id")) {
                            String query = "UPDATE tb_employees SET e_name='" + en_field.getText() + "', e_contact ='" + cont_field.getText() + "'," +
                                    " e_address ='" + add_field.getText() + "' WHERE e_id=" + rs.getInt("e_id");
                            PreparedStatement update = Main.conn.prepareStatement(query);
                            update.execute();
                            loadTable(defaultquery);
                            break;
                        }
                    }
                }
                }
    public void ShowDetails()
    {
        en_field.setText(inv_table.getSelectionModel().getSelectedItem().getEmp_name());
        cont_field.setText(inv_table.getSelectionModel().getSelectedItem().getCont_no());
        add_field.setText(inv_table.getSelectionModel().getSelectedItem().getAddress());
    }

    @FXML
    void Add (ActionEvent event) throws IOException{
            if (en_field.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid Name");
            } else if (cont_field.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid Contact Number");
            } else if (add_field.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid Address");
            } else {
                try {
                    String get = "SELECT * FROM tb_employees";
                    ResultSet rs=Main.conn.prepareStatement(get).executeQuery();
                    int sl =0;
                    while(rs.next())
                    {
                        if(rs.isLast())
                        {
                            sl=(rs.getInt("e_id")+1);

                        }
                    }
                    String query = "INSERT INTO tb_employees (e_id,e_name,e_contact,e_address) VALUES (?,?,?,?)";
                    PreparedStatement add = Main.conn.prepareStatement(query);
                    add.setInt(1,sl);
                    add.setString(2, en_field.getText());
                    add.setString(3, cont_field.getText());
                    add.setString(4, add_field.getText());
                    add.execute();
                    loadTable(defaultquery);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        }
    @FXML
    void Clear (ActionEvent event) throws IOException{
        en_field.setText("");
        cont_field.setText("");
        add_field.setText("");
    }

    @FXML
    void CheckAttendance(ActionEvent event) throws IOException{
            try {
                ResultSet data = Main.conn.getMetaData().getTables("db_ussc", null, null, new String[]{"TABLE"});
                String tb = null;
                int flag = 0;
                while (data.next()) {
                    if (data.getString("TABLE_NAME").equals("tb_" + inv_table.getSelectionModel().getSelectedItem().getEmp_name().replaceAll("\\s+", ""))) {
                        tb = "tb_" + inv_table.getSelectionModel().getSelectedItem().getEmp_name().replaceAll("\\s+", "");
                        flag = 1;
                        break;
                    }
                    if (flag == 1) {
                        break;
                    }
                }
                if (flag == 1) {
                    CheckAttendance.name = tb;
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
                } else {
                    JOptionPane.showMessageDialog(null, "Employee doesn't have a user account");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please Choose an Employee");
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
        loadTable(defaultquery);
        if(Login.user_type=="employee")
        {
            chk_attendance.setVisible(false);
            chk_attendance.setDisable(true);
            add_field.setEditable(false);
            en_field.setEditable(false);
            cont_field.setEditable(false);
            add_btn.setDisable(true);
            add_btn.setVisible(false);
            update_btn.setDisable(true);
            update_btn.setVisible(false);
            clear_btn.setDisable(true);
            clear_btn.setVisible(false);
        }
        set_drawer();
    }
}

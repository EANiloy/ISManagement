package Assets.Codes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class NewUser implements Initializable {
    public static String username=null;

    @FXML
    private JFXButton crt_btn;

    @FXML
    private Label accnt_lbl;

    @FXML
    private JFXTextField user_name;

    @FXML
    private JFXPasswordField pass;

    @FXML
    private JFXPasswordField cnfrm_pass;

    @FXML
    private JFXTextField contact_no;

    public static boolean flag=true;

    @FXML
    void CreateUser(ActionEvent event)throws IOException {
        if(user_name.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Please enter username");
        }
        else if(pass.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Please enter password");
        }
        else if(cnfrm_pass.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Please enter password again");
        }
        else if(contact_no.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Please enter Contact Number");
        }
        else if(pass.getText().equals(cnfrm_pass.getText())) {
            int id = 0;
            username=user_name.getText();
            try {
                String get = "SELECT * FROM tb_user";
                PreparedStatement getps = Main.conn.prepareStatement(get);
                ResultSet getrs = getps.executeQuery();
                while (getrs.next()) {
                    if (getrs.last()) {
                    id = getrs.getInt("user_id");
                }}
                String query = "INSERT INTO tb_user (user_id,user_name,user_type,password,contact_no) VALUES (?,?,DEFAULT,?,?)";
                PreparedStatement inps = Main.conn.prepareStatement(query);
                inps.setInt(1, (id + 1));
                inps.setString(2, username);
                inps.setString(3, pass.getText());
                inps.setString(4, contact_no.getText());
                inps.execute();
                String newtable= "CREATE TABLE tb_"+username.replaceAll("\\s+","")+" (`sl` INT(30) NOT NULL AUTO_INCREMENT,`date` VARCHAR(15) NULL , `login_time` VARCHAR(15) NULL , `logout_time` VARCHAR(15) NULL , PRIMARY KEY (`sl`))";
                PreparedStatement create =Main.conn.prepareStatement(newtable);
                create.execute();
                String insertuser ="INSERT INTO tb_employees (e_id,e_name,e_contact,e_address) VALUES (DEFAULT,?,?,DEFAULT)";
                PreparedStatement prep = Main.conn.prepareStatement(insertuser);
                prep.setString(1,username.replaceAll("\\s+",""));
                prep.setString(2,contact_no.getText());
                prep.execute();
                FadeTransition ft = new FadeTransition(Duration.seconds(.5), accnt_lbl);
                ft.setFromValue(0);
                ft.setToValue(1);
                accnt_lbl.setStyle("-fx-background-color:rgb(0,198,0)");
                accnt_lbl.setVisible(true);
                accnt_lbl.setText("Account Created Successfully");
                ft.play();
                ft.setOnFinished(actionEvent -> {
                    FadeTransition ft2 = new FadeTransition(Duration.seconds(2), accnt_lbl);
                    ft2.setFromValue(1);
                    ft2.setToValue(0);
                    ft2.play();
                });
            }
        catch (Exception e) {
            FadeTransition ft = new FadeTransition(Duration.seconds(.5), accnt_lbl);
            ft.setFromValue(0);
            ft.setToValue(1);
            accnt_lbl.setStyle("-fx-background-color:rgb(215,22,6)");
            accnt_lbl.setVisible(true);
            accnt_lbl.setText("Account Creation Failed.");
            ft.play();
            ft.setOnFinished(actionEvent -> {
                FadeTransition ft2 = new FadeTransition(Duration.seconds(2), accnt_lbl);
                ft2.setFromValue(1);
                ft2.setToValue(0);
                ft2.play();
            });
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"Please check password again");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            user_name.setText(username);
            if(flag) {
                user_name.setDisable(true);
            }
            else{
                user_name.setEditable(true);
                user_name.setDisable(false);
            }
    }
}

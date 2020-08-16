package Assets.Codes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

import javax.swing.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ForgotPass implements Initializable {

    @FXML
    private Label reset_label;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXPasswordField confrm_password;

    @FXML
    private JFXButton reset_btn;

    @FXML
    private Label confrm_lbl;

    @FXML
    private JFXTextField user_name;

    public static boolean flag=true;
    public static String user="";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(flag) {
            user_name.setText(user);
            user_name.setDisable(true);
        }
    }

    public void Reset(javafx.event.ActionEvent actionEvent) throws SQLException {
        if(user.equals("")) {
            user = user_name.getText();
        }
        String select = "SELECT user_name FROM tb_user";
        PreparedStatement ps = Main.conn.prepareStatement(select);
        ResultSet rs = ps.executeQuery();
        int flag=0;
        while(rs.next()){
        if(user.equals(rs.getString("user_name"))){
            flag=1;
        if(password.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Please enter password");
        }
        else if(confrm_password.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Please enter password");
        }
        else if(password.getText().equals(confrm_password.getText())){
        try{
            String query="UPDATE tb_user SET password = '"+password.getText()+"' WHERE user_name = '"+user+"'";
            PreparedStatement psup =Main.conn.prepareStatement(query);
            psup.execute();
            FadeTransition ft1 = new FadeTransition(Duration.seconds(.5),confrm_lbl);
            confrm_lbl.setText("Password changed successfully. Please use your new password to login");
            confrm_lbl.setStyle(" -fx-background-color:rgb(0,198,0);");
            confrm_lbl.setVisible(true);
            ft1.setFromValue(0);
            ft1.setToValue(1);
            ft1.play();
            ft1.setOnFinished(actionEvent1 -> {
                FadeTransition ft2 = new FadeTransition(Duration.seconds(2),confrm_lbl);
                ft2.setFromValue(1);
                ft2.setToValue(0);
                ft2.play();
            });
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
        }
        else{
            JOptionPane.showMessageDialog(null,"Password didn't match.Please verify again.");
        }
        }
        }
        if(flag==0)
        {
            confrm_lbl.setText("Password reset failed. Please check username.");
            confrm_lbl.setStyle("-fx-background-color:rgb(215,22,6)");
            confrm_lbl.setVisible(true);
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.play();

        }
}}

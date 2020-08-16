package Assets.Codes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main extends Application {

    static public DBConnect connect = new DBConnect();
    static public Connection conn;
    static Stage stage;
    static AnchorPane rootpane = new AnchorPane();
    static AnchorPane splash = new AnchorPane();
    static Scene scene =new Scene (rootpane);
    static Scene splashscreen = new Scene(splash);
    public static Stage splashStage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.stage=primaryStage;
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.setY(10);
        stage.setX(10);
        stage.getIcons().add(new Image("/Assets/Images/Icon.png"));
        splash();
    }
    public void splash()throws Exception{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Assets/FXMLs/Splash.fxml"));
        splash=loader.load();
        splashscreen.getStylesheets().addAll(Main.class.getResource("/Assets/StyleSheet/Style.css").toExternalForm());
        splashscreen.setRoot(splash);
        splashStage.setTitle("Splash Screen");
        splashStage.setScene(splashscreen);
        splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.getIcons().add(new Image("/Assets/Images/Icon.png"));
        splashStage.show();
    }
    public static void mainWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Assets/FXMLs/Login.fxml"));
        rootpane=loader.load();
        scene.setRoot(rootpane);
        scene.getStylesheets().addAll(Main.class.getResource("/Assets/StyleSheet/Style.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);
        stage.setTitle("United Sells and Service Center");
        stage.setScene(scene);
        stage.show();

    }

    public static void Loader(String link)throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(link));
        rootpane.getChildren().removeAll();
        rootpane=loader.load();
        scene.getStylesheets().addAll(Main.class.getResource("/Assets/StyleSheet/Style.css").toExternalForm());
        scene.setRoot(rootpane);
        stage.setScene(scene);
    }
    static public void exit()
    {
        try {
            String logout = "UPDATE tb_"+Login.user+" SET logout_time = ? WHERE date= ? && logout_time IS NULL";
            PreparedStatement update = Main.conn.prepareStatement(logout);
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String time= LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
            update.setString(1, time);
            update.setString(2, date);
            update.execute();
            conn.close();
            System.exit(0);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
    }
    static public void maxi() {

        if (stage.maximizedProperty().getValue()) {
            stage.setMaximized(false);
        } else {
            stage.setMaximized(true);
        }
    }
    static public void mini()
    {
        stage.setIconified(true);
    }
    public static void main(String[] args) {
        conn=connect.connectDb();
        launch(args);
    }

}

package Assets.Codes;

import com.jfoenix.controls.JFXSpinner;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Splash implements Initializable {

    @FXML
    private Label Splash_name;

    @FXML
    private Label Splash_logo;

    @FXML
    private JFXSpinner Splash_spinner;

    @FXML
    private Label Splash_info;

    @FXML
    private Pane Loading_pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Splash_logo.setVisible(false);
        Splash_name.setVisible(false);
        Loading_pane.setVisible(false);
        TranslateTransition logostart = new TranslateTransition(Duration.seconds(.25), Splash_logo);
        logostart.setByY(400);
        logostart.play();
        logostart.setOnFinished(actionEvent -> {
            Splash_logo.setVisible(true);
            TranslateTransition logoend = new TranslateTransition(Duration.seconds(.75), Splash_logo);
            logoend.setByY(-400.0);
            logoend.play();
            logoend.setOnFinished(actionEvent1 -> {
                TranslateTransition namestart = new TranslateTransition(Duration.seconds(.25), Splash_name);
                namestart.setByY(400.0);
                namestart.play();
                namestart.setOnFinished(actionEvent2 -> {
                    Splash_name.setVisible(true);
                    TranslateTransition nameend = new TranslateTransition(Duration.seconds(.5), Splash_name);
                    nameend.setByY(-400.0);
                    nameend.play();
                    nameend.setOnFinished(actionEvent3 -> {
                        Loading_pane.setVisible(true);
                        FadeTransition loadingstart = new FadeTransition(Duration.seconds(.5), Loading_pane);
                        loadingstart.setFromValue(0);
                        loadingstart.setToValue(1);
                        loadingstart.play();
                        loadingstart.setOnFinished(actionEvent4 -> {
                            PauseTransition p1 = new PauseTransition(Duration.seconds(.5));
                            Splash_info.setText("Please wait for the software to load");
                            p1.play();
                            p1.setOnFinished(actionEvent5 -> {
                                PauseTransition p2 = new PauseTransition(Duration.seconds(.5));
                                Splash_info.setText("Loading up the modules");
                                p2.play();
                                p2.setOnFinished(actionEvent6 -> {
                                    PauseTransition p3 = new PauseTransition(Duration.seconds(.5));
                                    Splash_info.setText("Checking resources");
                                    p3.play();
                                    p3.setOnFinished(actionEvent7 -> {
                                        PauseTransition p4 = new PauseTransition(Duration.seconds(.5));
                                        Splash_info.setText("Loading images");
                                        p4.play();
                                        p4.setOnFinished(actionEvent8 -> {
                                            PauseTransition p5 = new PauseTransition(Duration.seconds(1));
                                            Splash_info.setText("Application is starting");
                                            p5.play();
                                            p5.setOnFinished(actionEvent9 -> {
                                                try {
                                                    Main.mainWindow();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                Main.splashStage.close();
                                            });
                                        });
                                    });


                                });
                            });

                        });
                    });
                });

            });
        });
    }
}
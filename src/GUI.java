import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.AWTException;

public class GUI extends Application implements EventHandler<ActionEvent> {
    Button button;
    Clicker clicker;

    public static void main(String[] args) throws AWTException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        clicker = new Clicker();

        primaryStage.setTitle("Clicker");
        button = new Button();
        button.setText("START");
        button.setOnAction(this);
        button.setStyle("-fx-background-color: #FAA");
        button.setMinSize(150,75);

        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        Scene scene = new Scene(layout, 250, 100);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    @Override
    public void stop() {
        Platform.exit();
        Runtime.getRuntime().halt(0);
    }

    @Override
    public void handle(ActionEvent event) {
        if(event.getSource()==button) {
            if(clicker.enabled) {
                button.setStyle("-fx-background-color: #FAA");
                button.setText("START");
            } else {
                button.setStyle("-fx-background-color: #AFA");
                button.setText("STOP");
            }
            clicker.enabled = !clicker.enabled;
        }
    }


}

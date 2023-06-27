import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Objects;


public class Launcher extends Application {
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/welcomeForm.fxml")));
        stage.setTitle("Post Office Management System");
        stage.centerOnScreen();
        stage.setScene(new Scene(root));
        stage.show();
        stage.setFullScreen(true);

    }
}

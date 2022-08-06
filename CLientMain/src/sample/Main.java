package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("classes/interfaces/authorization.fxml"))));
        primaryStage.setTitle("Авторизация");
        primaryStage.setScene(new Scene(root, 700, 650));
        primaryStage.show();
    }
}
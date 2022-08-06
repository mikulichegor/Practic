package sample.classes.interfaces;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DirectorController {

    @FXML
    public Button btnWorkers;
    @FXML
    public Button btnLeave;
    @FXML
    public Button btnDocs;
    private Socket clientSocket = null;

    private ObjectOutputStream writerObj = null;

    private ObjectInputStream readerObj = null;

    private String windowName = "../interfaces/director.fxml";

    private Stage stage = null;

    private final int role = 1;

    @FXML
    void initialize() {

        btnDocs.setOnAction(actionEvent -> {
            Stage stage1 = (Stage) btnDocs.getScene().getWindow();
            stage1.hide();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("documents.fxml"));
                Parent root = null;
                root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                DocumentsController controller = loader.getController();
                controller.getConnect(clientSocket, writerObj, readerObj, stage1, this.role);
                stage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        btnWorkers.setOnAction(actionEvent -> {

            Stage stage1 = (Stage) btnWorkers.getScene().getWindow();
            stage1.hide();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("dirWorkers.fxml"));
                Parent root = null;
                root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                DirWorkersController controller = loader.getController();
                controller.getConnect(clientSocket, writerObj, readerObj, stage1);
                stage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        btnLeave.setOnAction(actionEvent -> {
           setWindow("authorization");

        });



    }

    void setWindow(String windowName){
        Stage stage1 = (Stage) btnLeave.getScene().getWindow();
        stage1.hide();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../interfaces/" + windowName + ".fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            loader.getController();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getConnect(Socket clientSocket, ObjectOutputStream writerObj, ObjectInputStream readerObj, Stage stage){
        this.clientSocket = clientSocket;
        this.writerObj = writerObj;
        this.readerObj = readerObj;
        this.stage = stage;
    }
}

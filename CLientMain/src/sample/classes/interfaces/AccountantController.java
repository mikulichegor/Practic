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


public class AccountantController {

    @FXML
    private Button btnWorkers;

    @FXML
    private Button btnLeave;

    @FXML
    private Button btnDocs;

    @FXML
    private Button btnAddWorker;
    private Socket clientSocket = null;

    private ObjectOutputStream writerObj = null;

    private ObjectInputStream readerObj = null;

    private Stage stage = null;

    private final int role = 2;

    @FXML
    void initialize(){

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

        btnLeave.setOnAction(actionEvent -> {
            setWindow("authorization");

        });

        btnAddWorker.setOnAction(actionEvent -> {
            Stage stage1 = (Stage) btnAddWorker.getScene().getWindow();
            stage1.hide();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("addWorker.fxml"));
                Parent root = null;
                root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                AddWorkerController controller = loader.getController();
                controller.getConnect(clientSocket, writerObj, readerObj, stage1);
                stage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        btnWorkers.setOnAction(actionEvent -> {
            Stage stage1 = (Stage) btnWorkers.getScene().getWindow();
            stage1.hide();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("accWorkers.fxml"));
                Parent root = null;
                root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                AccWorkersController controller = loader.getController();
                controller.getConnect(clientSocket, writerObj, readerObj, stage1);
                stage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

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

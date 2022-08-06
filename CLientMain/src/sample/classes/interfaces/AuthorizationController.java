package sample.classes.interfaces;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.classes.db.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class AuthorizationController {

    private Socket clientSocket = null;
    private ObjectOutputStream writerObj = null;
    private ObjectInputStream readerObj = null;
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
    @FXML
    private Button btnLogin;

    @FXML
    private TextField inputLogin = new TextField("");


    @FXML
    private PasswordField inputPassword = new PasswordField();;

    @FXML
    void initialize() {
        try {
            this.clientSocket = new Socket("127.0.0.1", 8000);

            writerObj = new ObjectOutputStream(clientSocket.getOutputStream());
            readerObj = new ObjectInputStream(clientSocket.getInputStream());

            btnLogin.setOnAction(actionEvent -> {
                Stage stage1 = (Stage) btnLogin.getScene().getWindow();
                try {
                    writerObj.writeObject("CHECK_VALID");
                    writerObj.writeObject(inputLogin.getText() + " " + inputPassword.getText());
                    Object obj = readerObj.readObject();
                    if (Objects.equals(String.valueOf(obj), "INVALID")){
                        alert.setAlertType(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Действие не выполнено.");
                        alert.setContentText("Неверный логин или пароль!");

                        alert.showAndWait();
                        inputLogin.clear();
                        inputPassword.clear();
                    }
                    else{
                        stage1.hide();

                        User user = (User)obj;


                        if (user.getRole() == 1){

                            setWindow("director");
                        }

                        if (user.getRole() == 2){

                            setWindow("accountantMenu");
                        }
                        if (user.getRole() == 3){

                            Stage stage2 = (Stage) btnLogin.getScene().getWindow();
                            stage2.hide();
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("../interfaces/workerMenu.fxml"));
                                Parent root = loader.load();
                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));

                                    WorkerController controller = loader.getController();
                                    controller.getConnect(clientSocket, writerObj, readerObj, stage2, user.getId());


                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }


            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setWindow (String windowName){
        Stage stage1 = (Stage) btnLogin.getScene().getWindow();
        stage1.hide();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../interfaces/" + windowName + ".fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            if(Objects.equals(windowName, "director")) {
                DirectorController controller = loader.getController();
                controller.getConnect(clientSocket, writerObj, readerObj, stage1);
            }
            else if(Objects.equals(windowName, "accountantMenu")) {
                AccountantController controller = loader.getController();
                controller.getConnect(clientSocket, writerObj, readerObj, stage1);
            }


            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getConnect(Socket clientSocket, ObjectOutputStream writerObj, ObjectInputStream readerObj){
        this.clientSocket = clientSocket;
        this.writerObj = writerObj;
        this.readerObj = readerObj;

    }

}

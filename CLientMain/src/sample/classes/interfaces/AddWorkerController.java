package sample.classes.interfaces;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.classes.db.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class AddWorkerController {

    @FXML
    private Button btnAdd;
    @FXML
    private TextField inputLogin;
    @FXML
    private TextField inputPass;
    @FXML
    private TextField inputRole;
    @FXML
    private TextField inputName;
    @FXML
    private TextField inputSurname;
    @FXML
    private TextField inputPost;
    @FXML
    private TextField inputSalary;
    @FXML
    private TextField inputBonus;
    @FXML
    private TextField inputPension;
    @FXML
    private Button btnBack;


    private Socket clientSocket = null;

    private ObjectOutputStream writerObj = null;

    private ObjectInputStream readerObj = null;

    private Stage stage = null;


    @FXML
    void initialize(){

        btnBack.setOnAction(actionEvent -> {
            backMenu();
        });

        btnAdd.setOnAction(actionEvent -> {

            String name = inputName.getText();
            String surname = inputSurname.getText();
            String post = inputPost.getText();
            String login = inputLogin.getText();
            String password = inputPass.getText();

            String role = inputRole.getText();
            String salary = inputSalary.getText();
            String bonus = inputBonus.getText();
            String  pension = inputPension.getText();

            if (!Objects.equals(name, "") && !Objects.equals(surname, "") && !Objects.equals(post, "")
                    && !Objects.equals(login, "") && !Objects.equals(password, "") && !Objects.equals(role, "")
                    && !Objects.equals(salary, "") && !Objects.equals(bonus, "") && !Objects.equals(pension, "")){
                User user = new User(login, password, Integer.parseInt(role), name, surname, post);
                Salary sal = new Salary(Integer.parseInt(salary), Integer.parseInt(bonus), Integer.parseInt(pension));

                try {
                    writerObj.writeObject("ADD_USER");
                    writerObj.writeObject(user);
                    writerObj.writeObject(sal);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                backMenu();
            }


        });


    }

    void backMenu(){
        Stage stage1 = (Stage) btnBack.getScene().getWindow();
        stage1.hide();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("accountantMenu.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            AccountantController controller = loader.getController();
            controller.getConnect(clientSocket, writerObj, readerObj,stage1);
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

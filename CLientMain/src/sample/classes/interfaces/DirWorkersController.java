package sample.classes.interfaces;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.classes.db.Unique;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class DirWorkersController {
    private Socket clientSocket = null;
    private ObjectOutputStream writerObj = null;
    private ObjectInputStream readerObj = null;
    private Stage stage = null;
    private String type = "";
    private String type_ch = "user";
    private String id;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnFind;

    @FXML
    private Button btnBasic;

    @FXML
    private Button btnSalary;

    @FXML
    private Button btnWeekend;

    @FXML
    private TableView<Unique> tableWorkers;

    @FXML
    private TableColumn<Unique, String> IDColumn;

    @FXML
    private TableColumn<Unique, String> loginColumn;

    @FXML
    private TableColumn<Unique, String> nameColumn;

    @FXML
    private TableColumn<Unique, String> surnameColumn;

    @FXML
    private TableColumn<Unique, String> postColumn;

    @FXML
    private TableColumn<Unique, String> salaryColumn;

    @FXML
    private ChoiceBox choiceBox;



    @FXML
    void initialize(){

        btnBack.setOnAction(actionEvent -> {
            setWindow("../interfaces/director.fxml", "");
        });

        btnFind.setOnAction(actionEvent -> {
            String post = (String) choiceBox.getSelectionModel().getSelectedItem();

            if(post != null) {
                Stage stage1 = (Stage) btnFind.getScene().getWindow();
                stage1.hide();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("posts.fxml"));
                    Parent root = null;
                    root = loader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    PostsController controller = loader.getController();
                    controller.getConnect(clientSocket, writerObj, readerObj, stage1, post);
                    stage.show();
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }});

        btnBasic.setOnAction(actionEvent -> {
            Stage stage1 = (Stage) btnBasic.getScene().getWindow();
            stage1.hide();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("averageSalaries.fxml"));
                Parent root = null;
                root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                AverageSalariesController controller = loader.getController();
                controller.getConnect(clientSocket, writerObj, readerObj, stage1);
                stage.show();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        btnSalary.setOnAction(actionEvent -> {
            Unique selectedItem = (Unique) tableWorkers.getSelectionModel().getSelectedItem();

            if(selectedItem != null) id = selectedItem.getText1();
            ArrayList salary = new ArrayList<>();
            try{
                writerObj.writeObject("SHOW_SEL_SALARY");
                writerObj.writeObject(id);

                salary = (ArrayList<Unique>) readerObj.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            ObservableList<Unique> list = FXCollections.observableArrayList(salary);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Зарплата");

            alert.setHeaderText(null);
            alert.setContentText("Оклад: " + list.get(0).getText1() + "\nПремия: " + list.get(0).getText2() + "\nПенсия: " + list.get(0).getText3());
            alert.showAndWait();
        });

        btnWeekend.setOnAction(actionEvent -> {
            Unique selectedItem = (Unique) tableWorkers.getSelectionModel().getSelectedItem();

            if(selectedItem != null) id = selectedItem.getText1();
            ArrayList vacation = new ArrayList<>();
            try{
                writerObj.writeObject("SHOW_SEL_VACATION");
                writerObj.writeObject(id);

                vacation = (ArrayList<Unique>) readerObj.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            ObservableList<Unique> list = FXCollections.observableArrayList(vacation);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Отпуск");

            alert.setHeaderText(null);
            alert.setContentText("Изначально: " + list.get(0).getText1() + "\nОсталось: " + list.get(0).getText2() + "\nПолучил: " + list.get(0).getText3());
            alert.showAndWait();
        });
    }

    void setWindow (String windowName, String post){
        Stage stage1 = (Stage) btnBack.getScene().getWindow();
        stage1.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(windowName));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            switch (windowName) {
                case "../interfaces/director.fxml" -> {
                    DirectorController controller = loader.getController();
                    controller.getConnect(clientSocket, writerObj, readerObj, stage1);
                }
                case "../interfaces/posts.fxml" -> {
                    PostsController controller = loader.getController();
                    controller.getConnect(clientSocket, writerObj, readerObj, stage1, post);
                }
                case "../interfaces/averageSalaries.fxml" -> {
                    AverageSalariesController controller = loader.getController();
                    controller.getConnect(clientSocket, writerObj, readerObj, stage1);
                }
            }

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList getNotes(){
        ArrayList users = new ArrayList<>();
        try {
            writerObj.writeObject("SHOW_USERS_DIR");

            users = (ArrayList<Unique>) readerObj.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        ObservableList list = FXCollections.observableArrayList(users);
        return list;
    }

    public void getConnect(Socket clientSocket, ObjectOutputStream writerObj, ObjectInputStream readerObj, Stage stage){
        this.clientSocket = clientSocket;
        this.writerObj = writerObj;
        this.readerObj = readerObj;
        this.stage = stage;

        ArrayList choices = new ArrayList<>();
        try {

            writerObj.writeObject("SET_CHOICES");


            choices = (ArrayList) readerObj.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        ObservableList choicesRd = FXCollections.observableArrayList(choices);

        choiceBox.setItems(FXCollections.observableArrayList(choicesRd));

        IDColumn.setCellValueFactory(new PropertyValueFactory<>("text1"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("text2"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("text3"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("text4"));
        postColumn.setCellValueFactory(new PropertyValueFactory<>("text5"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("text6"));

        tableWorkers.setItems(getNotes());
    }
}

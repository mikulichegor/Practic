package sample.classes.interfaces;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.classes.db.Unique;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class AccWorkersController {


    private Socket clientSocket = null;

    private ObjectOutputStream writerObj = null;

    private ObjectInputStream readerObj = null;

    private String windowName = "../interfaces/accWorkers.fxml";
    private static final String SET_CHOICES = "set choices";
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private Stage stage = null;
    @FXML
    private TextField inputBonus;
    @FXML
    private TextField inputPension;
    @FXML
    private Button btnChange;
    @FXML
    private Button btnDelete;
    @FXML
    private TextField inputSalary;
    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private TableView<Unique> tableWorkers;
    @FXML
    private TableColumn<Unique, String> IDColumn;
    @FXML
    private TableColumn<Unique, String> nameColumn;
    @FXML
    private TableColumn<Unique, String> surnameColumn;
    @FXML
    private TableColumn<Unique, String> postColumn;
    @FXML
    private TableColumn<Unique, String> salaryColumn;
    @FXML
    private TableColumn<Unique, String> bonusColumn;
    @FXML
    private TableColumn<Unique, String> pensionColumn;
    @FXML
    private Text labelWorkers;
    @FXML
    private Button btnBack;

    private ObservableList<Unique> data;



    @FXML
    void initialize() {

        btnBack.setOnAction(actionEvent -> {
            Stage stage1 = (Stage) btnBack.getScene().getWindow();
            stage1.hide();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("accountantMenu.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                AccountantController controller = loader.getController();
                controller.getConnect(clientSocket, writerObj, readerObj, stage1);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnDelete.setOnAction(actionEvent -> {
            Unique unique = tableWorkers.getSelectionModel().getSelectedItem();
            if (unique == null){
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Действие не выполнено.");
                alert.setContentText("Не выбран сотрудник для удаления!");

                alert.showAndWait();
            }
            else {
                try {
                    writerObj.writeObject("DELETE_USER");
                    writerObj.writeObject(unique);
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Уведомление");
                    alert.setHeaderText("Действие выполнено.");
                    alert.setContentText("Сотрудник удалён!");
                    alert.showAndWait();

                    refresh();
                    chooseBoxRefresh();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnChange.setOnAction(actionEvent -> {
            Unique unique = tableWorkers.getSelectionModel().getSelectedItem();
            if (unique == null){
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Действие не выполнено.");
                alert.setContentText("Не выбран сотрудник для изменения!");

                alert.showAndWait();
            }
            else {

                    String is = inputSalary.getText();
                    String ib = inputBonus.getText();
                    String ip = inputPension.getText();
                    String ch = (String) choiceBox.getSelectionModel().getSelectedItem();
                    if (ch == null && Objects.equals(is, "") && Objects.equals(ib, "") && Objects.equals(ip, "")){

                        alert.setAlertType(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Действие не выполнено.");
                        alert.setContentText("Не выбрано ни одного поля для изменения!");

                        alert.showAndWait();
                    }
                    else {
                        try {
                            if (ch != null) {
                                writerObj.writeObject("UPDATE_POST");
                                writerObj.writeObject(ch);
                                writerObj.writeObject(unique.getText1());
                            }
                            if (!Objects.equals(is, "")) {
                                writerObj.writeObject("UPDATE_SALARY");
                                writerObj.writeObject(is);
                                writerObj.writeObject(unique.getText1());
                            }
                            if (!Objects.equals(ib, "")) {
                                writerObj.writeObject("UPDATE_BONUS");
                                writerObj.writeObject(ib);
                                writerObj.writeObject(unique.getText1());
                            }
                            if (!Objects.equals(ip, "")) {
                                writerObj.writeObject("UPDATE_PENSION");
                                writerObj.writeObject(ip);
                                writerObj.writeObject(unique.getText1());
                            }

                            alert.setAlertType(Alert.AlertType.INFORMATION);
                            alert.setTitle("Уведомление");
                            alert.setHeaderText("Действие выполнено.");
                            alert.setContentText("Данные о сотруднике изменены!");
                            alert.showAndWait();

                            inputSalary.clear();
                            inputBonus.clear();
                            inputPension.clear();
                            refresh();
                            chooseBoxRefresh();
                        }
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }



            }

        });
    }

    public ObservableList getNotes(){
        ArrayList<Unique> transacts = new ArrayList<>();
        try {

            writerObj.writeObject("SHOW_USERS");

            transacts = (ArrayList<Unique>) readerObj.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(transacts);
    }

    public void refresh(){
        tableWorkers.setItems(getNotes());
    }
    public void chooseBoxRefresh(){
        ArrayList choices = new ArrayList<>();
        try {
            writerObj.writeObject("SHOW_QUERY");
            choices = (ArrayList) readerObj.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        ObservableList choicesRd = FXCollections.observableArrayList(choices);

        choiceBox.setItems(FXCollections.observableArrayList(choicesRd));
    }

    public void getConnect(Socket clientSocket, ObjectOutputStream writerObj, ObjectInputStream readerObj, Stage stage){

        this.clientSocket = clientSocket;
        this.writerObj = writerObj;
        this.readerObj = readerObj;
        this.stage = stage;

        IDColumn.setCellValueFactory(new PropertyValueFactory<>("text1"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("text2"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("text3"));
        postColumn.setCellValueFactory(new PropertyValueFactory<>("text4"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("text5"));
        bonusColumn.setCellValueFactory(new PropertyValueFactory<>("text6"));
        pensionColumn.setCellValueFactory(new PropertyValueFactory<>("text7"));

        refresh();
        chooseBoxRefresh();
    }
}

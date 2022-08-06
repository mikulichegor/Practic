package sample.classes.interfaces;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.classes.db.Document;
import sample.classes.db.Unique;
import java.util.*;
import java.text.*;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class WorkerController {

    private Socket clientSocket = null;
    private ObjectOutputStream writerObj = null;
    private ObjectInputStream readerObj = null;
    private String windowName = "../interfaces/authorization.fxml";
    private Stage stage = null;
    private int id = 0;

    @FXML
    public Button btnBack;

    @FXML
    public Text nameTxt;

    @FXML
    public Text surnameTxt;

    @FXML
    public Text postTxt;

    @FXML
    public Text loginTxt;

    @FXML
    public Text passTxt;


    @FXML
    public Text salaryTxt;

    @FXML
    public Text bonusTxt;

    @FXML
    public Text pensionTxt;

    @FXML
    public Text daysLeftTxt;

    @FXML
    public Text daysGotTxt;

    @FXML
    private TableView<Unique> tableDocs;

    @FXML
    private TableColumn<Unique, String> topicColumn;

    @FXML
    private TableColumn<Unique, String> dateColumn;

    @FXML
    private TableColumn<Unique, String> cnfDirColumn;

    @FXML
    private TableColumn<Unique, String> cnfAccColumn;

    @FXML
    private Button btnAsk;

    @FXML
    private Button btnSend;

    @FXML
    private TextField inputTitle;



    @FXML
    void initialize() {
        btnBack.setOnAction(actionEvent -> {
            setWindow("../interfaces/authorization.fxml", "");
        });

        btnSend.setOnAction(actionEvent -> {
            String topic = inputTitle.getText();
            Date dateNow = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd");

            if(!Objects.equals(topic, "")){
                Document doc = new Document(id,topic, formatForDateNow.format(dateNow));
                System.out.println(doc.toString());
                try{
                    writerObj.writeObject("ADD_DOC");
                    writerObj.writeObject(doc);
                    refresh();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnAsk.setOnAction(actionEvent -> {
            String topic = "Отпуск 15 дней";
            Date dateNow = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd");

            if(!Objects.equals(topic, "")){
                Document doc = new Document(id, topic, formatForDateNow.format(dateNow));
                try{
                    writerObj.writeObject("ADD_DOC");
                    writerObj.writeObject(doc);
                    refresh();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    void refresh(){
        tableDocs.setItems(getNotesDocs());

    }
    void setWindow (String windowName, String table){
        Stage stage1 = (Stage) btnBack.getScene().getWindow();
        stage1.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(windowName));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            switch (windowName) {
                case "../interfaces/authorization.fxml" -> {
                    AuthorizationController controller = loader.getController();
                    controller.getConnect(clientSocket, writerObj, readerObj);
                }
            }

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public ObservableList getNotesWorker(){
        ArrayList worker = new ArrayList<>();
        try {
            writerObj.writeObject("SET_WORKER");
            writerObj.writeObject(String.valueOf(id));

            worker = (ArrayList<Unique>) readerObj.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        ObservableList list = FXCollections.observableArrayList(worker);
        return list;
    }

    public ObservableList getNotesDocs(){
        ArrayList docs = new ArrayList<>();
        try {
            writerObj.writeObject("SET_WORKER_DOCS");
            writerObj.writeObject(String.valueOf(id));

            docs = (ArrayList<Unique>) readerObj.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        ObservableList list = FXCollections.observableArrayList(docs);
        return list;
    }

    public void getConnect(Socket clientSocket, ObjectOutputStream writerObj, ObjectInputStream readerObj, Stage stage, int id){
        this.clientSocket = clientSocket;
        this.writerObj = writerObj;
        this.readerObj = readerObj;
        this.stage = stage;
        this.id = id;


        topicColumn.setCellValueFactory(new PropertyValueFactory<>("text1"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("text2"));
        cnfDirColumn.setCellValueFactory(new PropertyValueFactory<>("text3"));
        cnfAccColumn.setCellValueFactory(new PropertyValueFactory<>("text4"));

        tableDocs.setItems(getNotesDocs());


        ObservableList <Unique> worker = getNotesWorker();

        nameTxt.setText(worker.get(0).getText1());
        surnameTxt.setText(worker.get(0).getText2());
        postTxt.setText(worker.get(0).getText3());
        loginTxt.setText("Логин: " + worker.get(0).getText4());
        passTxt.setText("Пароль: " + worker.get(0).getText5());

        salaryTxt.setText("Оклад: " + worker.get(0).getText7());
        bonusTxt.setText("Премия: " + worker.get(0).getText8());
        pensionTxt.setText("Пенсия: " + worker.get(0).getText9());
        daysLeftTxt.setText("Осталось: " + worker.get(0).getText10());
        daysGotTxt.setText("Потратил: " + worker.get(0).getText11());
    }
}

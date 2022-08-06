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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.classes.db.*;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class DocumentsController {

    @FXML
    private Button btnBack;
    @FXML
    private Button btnDecline;

    @FXML
    private Button btnConfirm;

    private Socket clientSocket = null;

    private ObjectOutputStream writerObj = null;

    private ObjectInputStream readerObj = null;

    private String windowName = "../interfaces/documents.fxml";

    private Stage stage = null;
    private int role;
    @FXML
    private TableView<Unique> tableDocs;

    @FXML
    private TableColumn<Unique, String> IDColumn;

    @FXML
    private TableColumn<Unique, String> userIDColumn;

    @FXML
    private TableColumn<Unique, String> titleColumn;

    @FXML
    private TableColumn<Unique, String>  dateColumn;

    @FXML
    private TableColumn<Unique, String>  cnfDirColumn;

    @FXML
    private TableColumn<Unique, String>  cnfAccColumn;

    private ObservableList<Unique> data;

    @FXML
    void initialize() {

        btnConfirm.setOnAction(actionEvent -> {
            Unique unique = tableDocs.getSelectionModel().getSelectedItem();
            int docId = Integer.parseInt(unique.getText2());
            int userId = Integer.parseInt(unique.getText1());
            boolean check = true;
            if (unique == null){
                System.out.println("Не выбрано");
            }
            else{
                try {
                    if(role == 1 && Objects.equals(unique.getText5(), "В обработке")){
                        writerObj.writeObject("CONFIRM");
                        writerObj.writeObject(unique);
                        writerObj.writeObject("DIRECTOR");
                    }
                    else if(role == 2 && Objects.equals(unique.getText6(), "В обработке")){
                        writerObj.writeObject("CONFIRM");
                        writerObj.writeObject(unique);
                        writerObj.writeObject("ACCOUNTANT");
                    }
                    else {
                        check = false;
                    }
                    if (check){
                        refresh();
                        for(Unique obj:tableDocs.getItems()){
                            if(Objects.equals(obj.getText2(), String.valueOf(docId))){
                                if(Objects.equals(obj.getText5(), obj.getText6())){
                                    writerObj.writeObject("UPDATE_VAC");
                                    writerObj.writeObject(userId);
                                }
                            }
                        }
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnDecline.setOnAction(actionEvent -> {
            Unique unique = tableDocs.getSelectionModel().getSelectedItem();
            if (unique == null){
                System.out.println("Не выбрано");
            }
            else{
                try {
                    if(role == 1 && Objects.equals(unique.getText5(), "В обработке")){
                        writerObj.writeObject("DECLINE");
                        writerObj.writeObject(unique);
                        writerObj.writeObject("DIRECTOR");
                    }
                    else if(role == 2 && Objects.equals(unique.getText6(), "В обработке")){
                        writerObj.writeObject("DECLINE");
                        writerObj.writeObject(unique);
                        writerObj.writeObject("ACCOUNTANT");
                    }
                    refresh();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnBack.setOnAction(actionEvent -> {
            if (this.role == 1){
                setWindow("director");
            }
            else{
                setWindow("accountantMenu");
            }
        });
    }

    public ObservableList getNotes(){
        ArrayList<Unique> transacts = new ArrayList<>();
        try {

            writerObj.writeObject("SHOW_UDOC");

            transacts = (ArrayList<Unique>) readerObj.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(transacts);
    }

    void setWindow(String windowName){
        Stage stage1 = (Stage) btnBack.getScene().getWindow();
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
    public void refresh(){
        tableDocs.setItems(getNotes());
    }


    public void getConnect(Socket clientSocket, ObjectOutputStream writerObj, ObjectInputStream readerObj, Stage stage, int role){

        this.clientSocket = clientSocket;
        this.writerObj = writerObj;
        this.readerObj = readerObj;
        this.stage = stage;
        this.role = role;

        IDColumn.setCellValueFactory(new PropertyValueFactory<>("text2"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("text1"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("text3"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("text4"));
        cnfDirColumn.setCellValueFactory(new PropertyValueFactory<>("text5"));
        cnfAccColumn.setCellValueFactory(new PropertyValueFactory<>("text6"));

        refresh();

    }
}

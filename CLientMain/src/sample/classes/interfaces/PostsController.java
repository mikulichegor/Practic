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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import sample.classes.db.Unique;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class PostsController {
    private Socket clientSocket = null;
    private ObjectOutputStream writerObj = null;
    private ObjectInputStream readerObj = null;
    private String windowName = "../interfaces/director.fxml";
    private Stage stage = null;
    private String post;

    @FXML
    public Button btnBack;

    @FXML
    private TableView<Unique> tablePosts;

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
    private Text salaryTxt;

    @FXML
    private Text workersTxt;

    @FXML
    void initialize() {

        btnBack.setOnAction(actionEvent -> {
            setWindow("../interfaces/dirWorkers.fxml", "");
        });
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
                case "../interfaces/dirWorkers.fxml" -> {
                    DirWorkersController controller = loader.getController();
                    controller.getConnect(clientSocket, writerObj, readerObj, stage1);
                }
            }

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList getNotes(){
        ArrayList posts = new ArrayList<>();
        try {
            writerObj.writeObject("SHOW_POSTS");
            writerObj.writeObject(post);

            posts = (ArrayList<Unique>) readerObj.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        ObservableList list = FXCollections.observableArrayList(posts);
        return list;
    }

    public void getConnect(Socket clientSocket, ObjectOutputStream writerObj, ObjectInputStream readerObj, Stage stage, String post){
        this.clientSocket = clientSocket;
        this.writerObj = writerObj;
        this.readerObj = readerObj;
        this.stage = stage;
        this.post = post;

        ObservableList<Unique> data = getNotes();

        workersTxt.setText("Количество сотрудников: " + data.size());
        int avgS = 0;
        for (Unique datum : data) {
            avgS += Integer.parseInt(datum.getText6());
        }
        salaryTxt.setText("Средняя зарплата по должности: " + avgS/data.size());


        IDColumn.setCellValueFactory(new PropertyValueFactory<>("text1"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("text2"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("text3"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("text4"));
        postColumn.setCellValueFactory(new PropertyValueFactory<>("text5"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("text6"));

        tablePosts.setItems(data);
    }
}

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
import sample.classes.db.Unique;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class AverageSalariesController {
    private Socket clientSocket = null;
    private ObjectOutputStream writerObj = null;
    private ObjectInputStream readerObj = null;
    private String windowName = "../interfaces/director.fxml";
    private Stage stage = null;

    @FXML
    public Button btnBack;

    @FXML
    private TableView<Unique> tableAverageSalary;

    @FXML
    private TableColumn<Unique, String> postColumn;

    @FXML
    private TableColumn<Unique, String> workersColumn;

    @FXML
    private TableColumn<Unique, String> salaryColumn;

    @FXML
    private BarChart<String, Number> postWorkersChart;

    @FXML
    private CategoryAxis xWC;

    @FXML
    private NumberAxis yWC;

    @FXML
    private BarChart<String, Number> postSalaryChart;

    @FXML
    private CategoryAxis xSC;

    @FXML
    private NumberAxis ySC;


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
        ArrayList data = new ArrayList<>();
        try {
            writerObj.writeObject("SHOW_AVG_SALARIES");

            data = (ArrayList<Unique>) readerObj.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        ObservableList list = FXCollections.observableArrayList(data);
        return list;
    }


    public void getConnect(Socket clientSocket, ObjectOutputStream writerObj, ObjectInputStream readerObj, Stage stage){
        this.clientSocket = clientSocket;
        this.writerObj = writerObj;
        this.readerObj = readerObj;
        this.stage = stage;

        ObservableList<Unique> data = getNotes();

        postColumn.setCellValueFactory(new PropertyValueFactory<>("text1"));
        workersColumn.setCellValueFactory(new PropertyValueFactory<>("text2"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("text3"));

        tableAverageSalary.setItems(data);


        XYChart.Series set1 = new XYChart.Series<>();
        for (int i = 0; i < data.size(); i++){
            set1.getData().add(new XYChart.Data(data.get(i).getText1(), Integer.parseInt(data.get(i).getText2())));
        }
        postWorkersChart.getData().addAll(set1);

        XYChart.Series set2 = new XYChart.Series<>();
        for (int i = 0; i < data.size(); i++){
            set2.getData().add(new XYChart.Data(data.get(i).getText1(), Integer.parseInt(data.get(i).getText3())));
        }
        postSalaryChart.getData().addAll(set2);
    }
}

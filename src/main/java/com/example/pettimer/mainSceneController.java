package com.example.pettimer;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class mainSceneController implements Initializable {

    private MyDataBase DateBase;
    private Connection cn;

    private double xOffset = 0;
    private double yOffset = 0;
    private int row = 0;

    private boolean addActIsClicked = false;

    @FXML
    private GridPane ActivitiesGrid;

    @FXML
    private AnchorPane slidingField;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button createNewActButton;

    @FXML
    private TextField newActName;

    @FXML
    private Text idText;

    @FXML
    private ImageView logOutImg;

    @FXML
    private ImageView createNewActImg;

    @FXML
    private Text loginText;

    @FXML
    private Node collapseImg;

    @FXML
    private Node sideField;

    public mainSceneController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        moveTheObject(slidingField, -210, 0, 200, 1);
        newActName.setFocusTraversable(false);
    }

    private void rotateTheObject(Node node, int angle, double millis, int cycleCount) {
        RotateTransition rt = new RotateTransition(Duration.millis(millis), node);
        rt.setByAngle(angle);
        rt.setCycleCount(cycleCount);
        rt.setAutoReverse(true);

        rt.play();
    }

    private void moveTheObject(Node node, double x, double y, double millis, int cycleCount){
        TranslateTransition tt = new TranslateTransition(Duration.millis(millis), node);
        tt.setToX(x);
        tt.setToY(y);
        tt.setCycleCount(cycleCount);
        tt.play();
    }

    @FXML
    void createNewActButtonClicked(MouseEvent event) throws SQLException, IOException {
        String name = newActName.getText();
        if(!DateBase.createActivity(name)){
            errorNewActShake(name);
        }
        else{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("activity.fxml"));

            Pane pane = fxmlLoader.load();

            ActivityController activityController = fxmlLoader.getController();
            activityController.setId(DateBase.getIdByActName(name));
            activityController.setActivityName(name);
            activityController.sendDB(DateBase);

            ActivitiesGrid.add(pane, 0, row);
            GridPane.setMargin(pane, new Insets(0, 0, 10, 0));
            row++;
        }
    }

    @FXML
    void addActImgClicked(MouseEvent event) {
        if(!addActIsClicked){
            addActIsClicked = true;

            rotateTheObject(createNewActImg, 45, 200, 1);
            moveTheObject(slidingField, 0, 0, 200, 1);
        }
        else {
            addActIsClicked = false;
            rotateTheObject(createNewActImg, -45, 200, 1);
            moveTheObject(slidingField, -210, 0, 200, 1);
        }
    }

    @FXML
    void logOutImgClicked(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("initialize.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene currentScene = ((Node) event.getSource()).getScene();
        Scene newScene = new Scene(root);
        newScene.setFill(Color.TRANSPARENT);

        Stage primaryStage = (Stage) currentScene.getWindow();
        primaryStage.setScene(newScene);
    }

    @FXML
    void closeImgClicked(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void collapseImgClicked(MouseEvent event) {
        Stage stage = (Stage) collapseImg.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void sideFieldDragged(MouseEvent event) {
        Stage stage = (Stage)sideField.getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    @FXML
    void sideFieldPressed(MouseEvent event) {
        Stage stage = (Stage)sideField.getScene().getWindow();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    public void sendDB(MyDataBase DateBase){
        this.DateBase = DateBase;
        this.cn = DateBase.getConnection();

        try {
            HashMap<String, String> allActivities = DateBase.getAllUserActivities();

            for(Map.Entry<String, String> entry : allActivities.entrySet())
            {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("activity.fxml"));

                Pane pane = fxmlLoader.load();

                ActivityController activityController = fxmlLoader.getController();
                activityController.setId(entry.getKey());
                activityController.setActivityName(entry.getValue());
                activityController.sendDB(DateBase);

                ActivitiesGrid.add(pane, 0, row);
                GridPane.setMargin(pane, new Insets(0, 0, 10, 0));
                row++;
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void errorNewActShake(String name){
        newActName.clear();

        /*todo Как-то реализовать скролинк к существубщей с таким именем активности и подсвечиванием её на время красным
        Node activity = null;
        ObservableList<Node> childrens = ActivitiesGrid.getChildren();

        for (Node node : childrens) {
            if(ActivitiesGrid.getRowIndex(node) == row) {
                activity = node;
                break;
            }
        }
        ActivitiesGrid.setOnScroll();*/

        TranslateTransition tt = new TranslateTransition(Duration.millis(50), slidingField);

        tt.setToX(-10);
        tt.setAutoReverse(true);
        tt.setCycleCount(4);
        tt.play();
    }

}

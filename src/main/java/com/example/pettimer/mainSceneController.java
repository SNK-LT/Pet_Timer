package com.example.pettimer;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
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
        moveTheObject(slidingField, -200, 0, 200, 1);
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
    void addActImgClicked(MouseEvent event) {
        if(!addActIsClicked){
            addActIsClicked = true;

            rotateTheObject(createNewActImg, 45, 200, 1);
            moveTheObject(slidingField, 0, 0, 200, 1);
        }
        else {
            addActIsClicked = false;
            rotateTheObject(createNewActImg, -45, 200, 1);
            moveTheObject(slidingField, -200, 0, 200, 1);
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
}

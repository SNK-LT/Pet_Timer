package com.example.pettimer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

public class mainSceneController implements Initializable {

    private MyDataBase DateBase = new MyDataBase();
    private Connection cn = DateBase.connectToDB("PetTimer", "postgres", "2311");

    @FXML
    private GridPane ActivitiesGrid;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Text idText;

    @FXML
    private Text loginText;

    @FXML
    private Node collapseImg;

    @FXML
    private Node sideField;

    private double xOffset = 0;
    private double yOffset = 0;
    private int row = 0;

    public mainSceneController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        try {
            HashMap<String, String> allActivities = DateBase.getAllActivities(cn);

            for(Map.Entry<String, String> entry : allActivities.entrySet())
            {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("activity.fxml"));

                Pane pane = fxmlLoader.load();

                ActivitiesGrid.add(pane, 0, row);
                GridPane.setMargin(pane, new Insets(0, 0, 10, 0));
                row++;
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
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
}

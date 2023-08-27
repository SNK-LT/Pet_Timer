package com.example.pettimer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class mainSceneController implements Initializable {

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

    public mainSceneController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

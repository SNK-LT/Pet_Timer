package com.example.pettimer;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class ActivityController implements Initializable {

    private MyDataBase DateBase;
    private Connection cn;
    private Timestamp timestamp;

    private String id;

    @FXML
    private Text activityName;

    @FXML
    private ImageView playImage;

    @FXML
    private ImageView statisticImage;

    @FXML
    private Text todaySumTime;

    @FXML
    private ImageView stopImage;

    @FXML
    private Text timeText;

    /*public DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    public LocalTime time = LocalTime.parse("00:00:00", dtf);*/

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private int hours = 0;
    private int min = 0;
    private int sec = 0;
    private int step = -1;

    private Thread t;

    public ActivityController(){
    }

    @FXML
    void statisticImageClicked(MouseEvent event) {

    }

    @FXML
    void playImageClicked(MouseEvent event) throws SQLException {
        timestamp = new Timestamp(System.currentTimeMillis());

        playImage.setVisible(false);
        stopImage.setVisible(true);
        isRunning.set(true);

        t = new Thread(() -> {
            while(isRunning.get())
            {
                try
                {
                    step++;
                    hours = step / 3600;
                    min = (step - hours * 3600) / 60;
                    sec = step - ((hours * 3600) + (min * 60));
                    timeText.setText(String.format("%02d:%02d:%02d", hours, min, sec));
                    Thread.sleep(1000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        t.start();
    }

    @FXML
    void stopImageClicked(MouseEvent event) throws SQLException {
        DateBase.createSession(id, timestamp);
        todaySumTime.setText(DateBase.getTodaySumTimeByActivityId(id));

        playImage.setVisible(true);
        stopImage.setVisible(false);

        isRunning.set(false);

        DateBase.updateSumTimeOfActivity(id, step);

        step = -1;
        hours = 0;
        min = 0;
        sec = 0;
        timeText.setText(String.format("%02d:%02d:%02d", hours, min, sec));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setActivityName(String actName) {
        this.activityName.setText(actName);
    }

    public void sendDB(MyDataBase DateBase) throws SQLException {
        this.DateBase = DateBase;
        this.cn = DateBase.getConnection();
        this.todaySumTime.setText(DateBase.getTodaySumTimeByActivityId(id));
    }
}

package com.example.pettimer;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class ActivityController implements Initializable {

    private MyDataBase DateBase = new MyDataBase();
    private Connection cn = DateBase.connectToDB("PetTimer", "postgres", "2311");

    @FXML
    private Text acticityName;

    @FXML
    private ImageView playImage;

    @FXML
    private ImageView statisticImage;

    @FXML
    private ImageView stopImage;

    @FXML
    private Text timeText;

    /*public DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    public LocalTime time = LocalTime.parse("00:00:00", dtf);*/

    private boolean isRunning;

    private int hours = 0;
    private int min = 0;
    private int sec = 0;
    private int step = 0;
    private String id;

    public ActivityController(String id, String name){
        this.id = id;
        this.acticityName.setText(name);
    }

    @FXML
    void statisticImageClicked(MouseEvent event) {

    }

    @FXML
    void playImageClicked(MouseEvent event) {

        playImage.setVisible(false);
        stopImage.setVisible(true);
        isRunning = true;

        Thread t = new Thread(() -> {
            for(;;)
            {
                if(isRunning)
                {
                    try
                    {
                        Thread.sleep(1000);

                        step++;
                        hours = step / 3600;
                        min = (step - hours * 3600) / 60;
                        sec = step - ((hours * 3600) + (min * 60));
                        timeText.setText(String.format("%02d:%02d:%02d", hours, min, sec));

                        /*if(sec == 59)
                        {
                            min++;
                            minTime.setText(":" + min);
                            sec = 0;
                        }
                        if(min == 59)
                        {
                            hours++;
                            hTime.setText(String.valueOf(hours));
                            min = 0;
                        }
                        sec++;
                        secTime.setText(":" + sec);*/

                    }
                    catch (Exception e)
                    {

                    }
                }
                else{
                    step = 0;
                    hours = 0;
                    min = 0;
                    sec = 0;

                    timeText.setText(String.format("%02d:%02d:%02d", hours, min, sec));

                    /*hours = 0;
                    min = 0;
                    sec = 0;

                    hTime.setText("0:");
                    minTime.setText("0:");
                    secTime.setText("0");*/
                };
            }
        });
        t.start();
    }

    @FXML
    void stopImageClicked(MouseEvent event) {
        playImage.setVisible(true);
        stopImage.setVisible(false);

        isRunning = false;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

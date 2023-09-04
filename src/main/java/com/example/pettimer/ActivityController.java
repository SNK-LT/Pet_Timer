package com.example.pettimer;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class ActivityController implements Initializable {

    @FXML
    private Text acticityName;

    @FXML
    private Text hTime;

    @FXML
    private Text minTime;

    @FXML
    private ImageView playImage;

    @FXML
    private Text secTime;

    @FXML
    private ImageView statisticImage;

    @FXML
    private ImageView stopImage;

    /*public DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    public LocalTime time = LocalTime.parse("00:00:00", dtf);*/

    private boolean isRunning;

    private int hours = 0;
    private int min = 0;
    private int sec = 0;

    public ActivityController(){
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
                        if(sec == 60)
                        {
                            min++;
                            minTime.setText(":" + min);
                            sec = 0;
                        }
                        if(min == 60)
                        {
                            hours++;
                            hTime.setText(String.valueOf(hours));
                            min = 0;
                        }
                        sec++;
                        secTime.setText(":" + sec);
                    }
                    catch (Exception e)
                    {

                    }
                }
                else{
                    hours = 0;
                    min = 0;
                    sec = 0;

                    hTime.setText("0:");
                    minTime.setText("0:");
                    secTime.setText("0");
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

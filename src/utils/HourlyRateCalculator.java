/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;


import java.util.Timer;
import java.util.TimerTask;
/**
 *
 * @author MINH DANG
 */
public class HourlyRateCalculator {
    private double hourlyRate = 5000.0; // Giá tiền giờ (ví dụ: 5000 đồng)
    private float totalAmount = 0;
    private Timer timer;

    public HourlyRateCalculator() {
    }
    
    public void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                addHourlyRate();
            }
        }, 0, 5 * 60 * 1000); // Hãy cộng vào tiền mỗi 5 phút (5 * 60 * 1000 milliseconds)
    }
    
     private void addHourlyRate() {
        totalAmount += hourlyRate;
    }
     
     public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
     
    public float getTotalAmount() {
        return totalAmount;
    }
}

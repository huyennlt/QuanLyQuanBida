/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.Random;
import javax.swing.JProgressBar;

/**
 *
 * @author MINH DANG
 */
public class TimeProgress implements Runnable {
    private JProgressBar progressbar;
    Random rd;

    public TimeProgress(JProgressBar progressbar) {
        this.progressbar = progressbar;
        rd = new Random();
    }

    @Override
    public void run() {
        int minimum = progressbar.getMinimum();
        int maximum = progressbar.getMaximum();
        for (int i = minimum; i < maximum; i++) {
            try {
                int value = progressbar.getValue();
                progressbar.setValue(value + rd.nextInt(20) + 1);
                Thread.sleep(500);
            } catch (Exception e) {
            }
        }
    }
}

package com.example.serverlicensecheck.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleAlert {


    public static void showAlert(String message) {
        JWindow alertWindow = new JWindow();
        alertWindow.setLayout(new BorderLayout());
        alertWindow.setSize(100, 40);

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        alertWindow.add(messageLabel, BorderLayout.CENTER);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - alertWindow.getWidth();
        int y = screenSize.height - alertWindow.getHeight();
        alertWindow.setLocation(x, y-40);

        alertWindow.setVisible(true);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alertWindow.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}

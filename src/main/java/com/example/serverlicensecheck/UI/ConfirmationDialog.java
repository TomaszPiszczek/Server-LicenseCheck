package com.example.serverlicensecheck.UI;

import javax.swing.*;

public class ConfirmationDialog {
    private JFrame invisibleFrame;
    public ConfirmationDialog() {
        invisibleFrame = new JFrame();
        invisibleFrame.setUndecorated(true);
        invisibleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        invisibleFrame.setAlwaysOnTop(true);
        invisibleFrame.setVisible(false);
    }

    public int createConfirmationDialog(String message, String title, String yesOption, String noOption, String defaultButton) {

        return JOptionPane.showOptionDialog(
                invisibleFrame,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{yesOption, noOption},
                defaultButton
        );
    }
}
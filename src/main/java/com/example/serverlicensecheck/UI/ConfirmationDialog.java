package com.example.serverlicensecheck.UI;

import javax.swing.*;

public class ConfirmationDialog {
    public int createConfirmationDialog(String message,String title,String yesOption,String noOption,String defaultButton){
           return JOptionPane.showOptionDialog(null,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{yesOption, noOption},
                defaultButton);



    }
}

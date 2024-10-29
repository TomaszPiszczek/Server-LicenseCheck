package com.example.serverlicensecheck;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WindowsProcessChecker {

    private static final Logger logger = Logger.getLogger(WindowsProcessChecker.class.getName());

    public boolean isProcessRunning(String processName) {
        try {
            Process taskListCommand = Runtime.getRuntime().exec("tasklist");
            BufferedReader listOfWindowsTasks = new BufferedReader(new InputStreamReader(taskListCommand.getInputStream()));
            String line;
            while ((line = listOfWindowsTasks.readLine()) != null) {
                if (line.toLowerCase(Locale.ROOT).contains(processName.toLowerCase(Locale.ROOT))) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error checking if process is running: " + processName, e);
        }
        return false;
    }

    public void killProcess(String processName) {
        try {
            Process process = Runtime.getRuntime().exec("taskkill /F /IM " + "\"" +  processName + "\"");
            process.waitFor();

            logger.info("Process " + processName + " has been determinate.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while trying to kill process: " + processName, e);
        }
    }
}

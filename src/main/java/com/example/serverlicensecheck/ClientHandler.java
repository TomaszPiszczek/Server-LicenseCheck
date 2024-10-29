package com.example.serverlicensecheck;

import com.example.serverlicensecheck.UI.ConfirmationDialog;
import com.example.serverlicensecheck.UI.SimpleAlert;
import com.example.serverlicensecheck.exception.ServerException;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler {

    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());
    private final WindowsProcessChecker windowsProcessChecker;
    private final ConfirmationDialog confirmationDialog;
    private final Socket connectedClient;

    public ClientHandler(Socket connectedClient) {
        this.connectedClient = connectedClient;

        this.windowsProcessChecker = new WindowsProcessChecker();
        this.confirmationDialog = new ConfirmationDialog();
    }

    public void handle(String command) throws ServerException {
        switch (command.toUpperCase()) {
            case "CHECK":
                handleCheck();
                break;

            case "SHUTDOWN":
                handleShutdown();
                break;

            case "CLOSE":
                handleClose();
                break;

            default:
                logger.warning("Unknown command received: " + command);
                sendBoolToClient(false);
        }
    }

    private void handleCheck() throws ServerException {
        boolean isChromeRunning = windowsProcessChecker.isProcessRunning("Chrome.exe");

        if (isChromeRunning) {
            SimpleAlert.showAlert("Panel w użyciu");
        }
        sendBoolToClient(isChromeRunning);
    }

    private void handleShutdown() throws ServerException {
        int dialogResponse = confirmationDialog.createConfirmationDialog(
                "Prośba o wyłączenie Comarch Optima.",
                "Zamknij program",
                "Akceptuj",
                "Odrzuć",
                "Akceptuj"
        );

        if (dialogResponse == JOptionPane.YES_OPTION) {
            windowsProcessChecker.killProcess("Chrome.exe");
        }
        sendBoolToClient(dialogResponse == JOptionPane.YES_OPTION);
    }

    private void handleClose() {
        System.out.println("GOT CLOSE FROM SERVER" + connectedClient.getInetAddress());
        // Close any open dialogs or perform necessary clean-up here.
       // confirmationDialog.dispose();

        // Close the client connection
        try {
            if (connectedClient != null && !connectedClient.isClosed()) {
                connectedClient.close();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error closing client connection", e);
        }
    }

    private void sendBoolToClient(boolean bool) throws ServerException {
        PrintWriter sendToClient;
        try {
            sendToClient = new PrintWriter(connectedClient.getOutputStream(), true);
            sendToClient.println(bool);
            logger.info("Sent to client: " + bool + " " + connectedClient.getInetAddress());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to send to client", e);
            throw new ServerException("Failed to send boolean to client", e);
        }
    }
}

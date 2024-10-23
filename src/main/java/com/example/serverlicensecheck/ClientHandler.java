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

    public void handle() throws ServerException {
        boolean isChromeRunning = windowsProcessChecker.isProcessRunning("Comarch Opt!ma.exe");

        if (isChromeRunning) {
            SimpleAlert.showAlert("Panel w użyciu");
            sendBoolToClient(true);

        } else {
            int dialogResponse = confirmationDialog.createConfirmationDialog(
                    "Prośba o wyłączenie Comarch Optima.",
                    "Zamknij program",
                    "Akceptuj",
                    "Odrzuć",
                    "Akceptuj"
            );
            if (dialogResponse == JOptionPane.YES_OPTION) {
                windowsProcessChecker.killProcess("Comarch Opt!ma.exe");
            }
            sendBoolToClient(dialogResponse == JOptionPane.YES_OPTION);
        }
    }

    private void sendBoolToClient(boolean bool) throws ServerException {
        PrintWriter sendToClient;
        try {
            sendToClient = new PrintWriter(connectedClient.getOutputStream(), true);
            sendToClient.println(bool);
            logger.info("Sended to client: " + bool + " " + connectedClient.getInetAddress());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Not sended to client", e);
            throw new ServerException("Failed to send boolean to client", e);


        }
    }
}
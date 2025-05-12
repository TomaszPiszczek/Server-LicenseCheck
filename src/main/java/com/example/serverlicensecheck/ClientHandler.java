package com.example.serverlicensecheck;

import com.example.serverlicensecheck.UI.ConfirmationDialog;
import com.example.serverlicensecheck.UI.SimpleAlert;
import com.example.serverlicensecheck.exception.ServerException;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());
    private final WindowsProcessChecker windowsProcessChecker;
    private final ConfirmationDialog confirmationDialog;
    private final Socket connectedClient;
    private final Server server; // Referencja do serwera
    private volatile boolean closeRequested = false;
    private long connectionStartTime; // Zmienna do śledzenia czasu połączenia

    public ClientHandler(Socket connectedClient, Server server) {
        this.connectedClient = connectedClient;
        this.windowsProcessChecker = new WindowsProcessChecker();
        this.confirmationDialog = new ConfirmationDialog();
        this.server = server; // Inicjalizacja referencji do serwera
        this.connectionStartTime = System.currentTimeMillis(); // Ustaw czas rozpoczęcia połączenia
    }
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()))) {
            String clientCommand;
            while (!closeRequested && (clientCommand = in.readLine()) != null) {
                // Sprawdzenie czasu trwania połączenia
                if (System.currentTimeMillis() - connectionStartTime > 15000) { // 15 sekund
                    System.out.println("Connection timed out.");
                    closeRequested = true; // Ustaw flagę zamknięcia
                    break; // Przerywamy pętlę
                }
                System.out.println("GOT COMMAND " + clientCommand);
                handle(clientCommand);
            }
        } catch (IOException | ServerException e) {
            // Obsługa błędów logowania
        } finally {
            handleClose();
        }
    }

    public void handle(String command) throws ServerException {
        switch (command.toUpperCase()) {
            case "CHECK":
                handleCheck();
                handleClose();
                break;
            case "SHUTDOWN":
                handleShutdown();
                handleClose();
                break;
            case "CLOSE":
                closeRequested = true;
                server.closeConnectionsByIP(connectedClient.getInetAddress().getHostAddress());
                handleClose();
                break;
            case "DIALOG":
                System.out.println("GOT DIALOG");
                handleCloseDialogs();
                handleClose();
                break;
            default:
                sendBoolToClient(false);
        }
    }

    private void handleClose() {
        try {
            // Zamknięcie wszystkich okien dialogowych typu JDialog

            if (connectedClient != null && !connectedClient.isClosed()) {
                connectedClient.close();
                server.removeConnection(this); // Usunięcie połączenia z serwera
                System.out.println("CONNECTION CLOSED");
            }
        } catch (IOException e) {
            // Obsługa błędów logowania
        }
    }

    private void closeAllConfirmationDialogs() {
        System.out.println("HANDLING DIALOG 2");;
        // Przeszukaj wszystkie otwarte okna
        for (Window window : Window.getWindows()) {
            if (window instanceof JDialog) {
                JDialog dialog = (JDialog) window;
                // Tutaj możemy również dodać dodatkowe warunki, jeśli chcemy zamknąć tylko określone dialogi
                if (dialog.getTitle().equals("Zamknij program") && dialog.isShowing()) {
                    System.out.println(dialog.getTitle());
                    dialog.dispose(); // Zamknięcie okna dialogowego
                    System.out.println("CONFIRMATION DIALOG CLOSED: " + dialog.getTitle());
                }
            }
        }
    }
    public void requestClose() {
        closeRequested = true;
        handleClose();
    }

    public String getClientIP() {
        return connectedClient.getInetAddress().getHostAddress();
    }

    private void handleCheck() throws ServerException {
        boolean isChromeRunning = windowsProcessChecker.isProcessRunning("Comarch Opt!ma.exe");
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
            windowsProcessChecker.killProcess("Comarch Opt!ma.exe");

        }
        sendBoolToClient(dialogResponse == JOptionPane.YES_OPTION);
        System.out.println(connectedClient.getInetAddress().getHostAddress() + " SUTDOWN TO CLIENT");
        if (closeRequested) {
            handleClose();
        }
    }
    private void handleCloseDialogs(){
        System.out.println("HANDLING DIALOG 1");
        closeAllConfirmationDialogs();
    }

    private void sendBoolToClient(boolean bool) throws ServerException {
        try (PrintWriter sendToClient = new PrintWriter(connectedClient.getOutputStream(), true)) {
            sendToClient.println(bool);
            System.out.println("SENT TO CLIENT " + bool);
        } catch (IOException e) {
            throw new ServerException("Failed to send boolean to client", e);
        }
    }
}

package com.example.serverlicensecheck;

import com.example.serverlicensecheck.exception.ServerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private final int PORT;
    private Socket connectedClient;

    public Server(int PORT) {
        this.PORT = PORT;
    }

    public void startServer() throws ServerException {
        try (ServerSocket serverSocket = new ServerSocket(this.PORT)) {
            logger.info("SERWER URUCHOMIONY");

            while (true) {
                this.connectedClient = serverSocket.accept();
                logger.info("Klient połączony: " + connectedClient.getInetAddress());

                try {
                    ClientHandler clientHandler = new ClientHandler(connectedClient);

                    // Odczytujemy dane wejściowe od klienta
                    BufferedReader in = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()));
                    String clientCommand;

                    while ((clientCommand = in.readLine()) != null) {
                        clientHandler.handle(clientCommand);
                        System.out.println(clientCommand + "COMMAND");
                    }
                    stopConnectionWithClient();

                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error processing client input", e);
                }

            }
        } catch (IOException e) {
            throw new ServerException("Failed to start server", e);
        }
    }

    private void stopConnectionWithClient() throws ServerException {
        try {
            if (connectedClient != null && !connectedClient.isClosed()) {
                connectedClient.close();
                connectedClient = null;
                logger.info("Connection closed");
            }
        } catch (IOException e) {
            throw new ServerException("Failed to close connection with client", e);
        }
    }
}

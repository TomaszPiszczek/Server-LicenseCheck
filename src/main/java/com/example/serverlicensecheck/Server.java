package com.example.serverlicensecheck;

import com.example.serverlicensecheck.exception.ServerException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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

                ClientHandler clientHandler = new ClientHandler(connectedClient);
                clientHandler.handle();

                stopConnectionWithClient();
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

package com.example.serverlicensecheck;

import com.example.serverlicensecheck.exception.ServerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private final int PORT;
    private final List<ClientHandler> activeConnections; // Lista aktywnych połączeń

    public Server(int PORT) {
        this.PORT = PORT;
        this.activeConnections = new CopyOnWriteArrayList<>(); // Bezpieczna do jednoczesnych modyfikacji
    }

    public void startServer() throws ServerException {
        try (ServerSocket serverSocket = new ServerSocket(this.PORT)) {
            while (true) {
                Socket connectedClient = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(connectedClient, this); // Przekazujemy referencję do serwera

                // Dodajemy połączenie do listy
                activeConnections.add(clientHandler);

                // Uruchomienie nowego wątku obsługującego klienta
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            throw new ServerException("Failed to start server", e);
        }
    }

    public void closeConnectionsByIP(String ip) {
        for (ClientHandler handler : activeConnections) {
            if (handler.getClientIP().equals(ip)) {
                handler.requestClose();
            }
        }
    }

    public void removeConnection(ClientHandler handler) {
        activeConnections.remove(handler);
    }
}
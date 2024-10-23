package com.example.serverlicensecheck;

import com.example.serverlicensecheck.exception.ServerException;

public class ProcessServer {
    public static void main(String[] args) {


        Server server = new Server(1099);

        try {
            // Start the server
            server.startServer();
        } catch (ServerException e) {
            e.printStackTrace();
        }
    }
}

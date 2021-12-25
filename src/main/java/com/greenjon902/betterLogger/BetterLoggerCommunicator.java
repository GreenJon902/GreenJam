package com.greenjon902.betterLogger;

import java.io.IOException;
import java.net.ServerSocket;

public class BetterLoggerCommunicator {
    private boolean started = false;
    private ServerSocket serverSocket;

    private void openServer() throws IOException {
        serverSocket = new ServerSocket(0);
    }

    private void closeServer() throws IOException {
        serverSocket.close();
    }

    public void start() {
        if (!started) {
            try {
                openServer();
                System.out.println("Logging on port " + serverSocket.getLocalPort());
            } catch (IOException e) {
                System.out.println(Colors.format("[ERROR] Failed to start logger"));
            }
            started = true;
        }
    }

    public void end() {
        try {
            closeServer();
        } catch (IOException e) {
            System.out.println(Colors.format("[ERROR] Failed to end logger"));
        }
    }
}

package com.greenjon902.betterLogger;

import java.io.IOException;
import java.net.ServerSocket;

public class BetterLoggerCommunicator {
    private boolean started = false;
    ServerSocket serverSocket;

    private int openServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(0);
        return serverSocket.getLocalPort();
    }

    private void closeServer() throws IOException {
        serverSocket.close();
    }

    public void start() {
        if (!started) {
            try {
                openServer();
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

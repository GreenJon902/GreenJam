package com.greenjon902.betterLogger;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

public class BetterLoggerCommunicator {
    private static final File pythonFile = new File(System.getProperty("user.dir"), "betterLoggerPortal.py");

    private boolean started = false;
    private ServerSocket serverSocket;

    private void openServer() throws IOException {
        serverSocket = new ServerSocket(0);
    }

    private void closeServer() throws IOException {
        serverSocket.close();
    }

    private void connectPython(int port) throws IOException {
        if (!pythonFile.exists()) {
            //noinspection ConstantConditions
            FileUtils.copyURLToFile(getClass().getResource("betterLoggerPortal.py"), pythonFile);
        }

        ProcessBuilder processBuilder = new ProcessBuilder("python", pythonFile.getAbsolutePath(), String.valueOf(port));
        System.out.println(1);
        processBuilder.start();
        System.out.println(2);
    }

    public void start() {
        if (!started) {
            try {
                openServer();
                System.out.println("Logging on port " + serverSocket.getLocalPort());
                connectPython(serverSocket.getLocalPort());
            } catch (Exception e) {
                System.out.println(Colors.format("[ERROR] Failed to start logger, error:"));
                e.printStackTrace();
            }
            started = true;
        }
    }

    public void end() {
        try {
            closeServer();
        } catch (Exception e) {
            System.out.println(Colors.format("[ERROR] Failed to end logger"));
        }
    }
}

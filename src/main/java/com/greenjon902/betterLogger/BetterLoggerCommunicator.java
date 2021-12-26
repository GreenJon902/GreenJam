package com.greenjon902.betterLogger;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BetterLoggerCommunicator {
    private static final File pythonFile = new File(System.getProperty("user.dir"), "betterLoggerPortal.py");

    private boolean started = false;
    private ServerSocket serverSocket;

    private void openServer() throws IOException {
        String port = System.getenv("BETTERLOGGERPORT");
        if (port == null) {
            port = "0";
        }
        serverSocket = new ServerSocket(Integer.parseInt(port));
    }

    private void closeServer() throws IOException {
        serverSocket.close();
    }

    private void connectPython(int port) throws IOException {
        if (!pythonFile.exists() || (System.getenv("FORCEBETTERLOGGERCOPYFILE") != null && Boolean.parseBoolean(System.getenv("FORCEBETTERLOGGERCOPYFILE")))) {
            //noinspection ConstantConditions
            FileUtils.copyURLToFile(getClass().getResource("betterLoggerPortal.py"), pythonFile);
            System.out.println("Copied betterLoggerPortal.py");
        }

        ProcessBuilder processBuilder = new ProcessBuilder("python", pythonFile.getAbsolutePath(), String.valueOf(port));
        Process process = processBuilder.start();

        System.out.println("Waiting on connection");
        Socket pythonConnection = serverSocket.accept();
        System.out.println("Incoming connection from " + pythonConnection.getInetAddress().toString() + pythonConnection.getPort());
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

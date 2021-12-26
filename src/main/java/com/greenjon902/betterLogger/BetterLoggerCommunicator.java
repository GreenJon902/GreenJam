package com.greenjon902.betterLogger;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BetterLoggerCommunicator {
    private static final int pythonConn_typeLength = 4;
    private static final int pythonConn_messageLength = 8;

    private static final File pythonFile = new File(System.getProperty("user.dir"), "betterLoggerPortal.py");

    private boolean started = false;
    private ServerSocket serverSocket;
    InputStream pythonConnectionInputStream;
    Thread loggerHandlerThread;
    Process pythonProcess;

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

        ProcessBuilder processBuilder = new ProcessBuilder("python3", pythonFile.getAbsolutePath(), String.valueOf(port));
        pythonProcess = processBuilder.start();

        System.out.println("Waiting on connection");
        Socket pythonConnection = serverSocket.accept();
        pythonConnectionInputStream = pythonConnection.getInputStream();
        System.out.println("Incoming connection from " + pythonConnection.getInetAddress().toString() + ":" + pythonConnection.getPort());
    }

    private void handleLogger() {
        System.out.println("Handling logger on thread " + Thread.currentThread().getName());
        while (true) {
            try {
                byte[] typeLengthBytes = new byte[pythonConn_typeLength];
                int bytes_read = pythonConnectionInputStream.read(typeLengthBytes);

                if (bytes_read != -1) {
                    byte[] typeBytes = new byte[Integer.parseInt(new String(typeLengthBytes, StandardCharsets.UTF_8))];
                    //noinspection ResultOfMethodCallIgnored
                    pythonConnectionInputStream.read(typeBytes);

                    String type = new String(typeBytes, StandardCharsets.UTF_8);


                    byte[] messageLengthBytes = new byte[pythonConn_messageLength];
                    //noinspection ResultOfMethodCallIgnored
                    pythonConnectionInputStream.read(messageLengthBytes);

                    byte[] messageBytes = new byte[Integer.parseInt(new String(messageLengthBytes, StandardCharsets.UTF_8))];
                    //noinspection ResultOfMethodCallIgnored
                    pythonConnectionInputStream.read(messageBytes);

                    String message = new String(messageBytes, StandardCharsets.UTF_8);
                    System.out.print("[" + type + "]  " + message);
                }

            } catch (Exception e) {
                System.out.println("Got exception while handling logger");
                System.out.print(Colors.RED);
                e.printStackTrace(System.out);
                System.out.print(Colors.RESET);
            }
        }
    }

    public void start() {
        if (!started) {
            try {
                openServer();
                System.out.println("Logging on port " + serverSocket.getLocalPort());
                connectPython(serverSocket.getLocalPort());

                loggerHandlerThread = new Thread(this::handleLogger);
                loggerHandlerThread.start();

                started = true;
            } catch (Exception e) {
                System.out.println(Colors.format("[ERROR] Failed to start logger, error:"));
                e.printStackTrace();
            }
        }

        try
        {
            Thread.sleep(10000);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    public void end() {
        try {
            loggerHandlerThread.join();
            closeServer();
        } catch (Exception e) {
            System.out.println(Colors.format("[ERROR] Failed to end logger"));
        }
    }
}

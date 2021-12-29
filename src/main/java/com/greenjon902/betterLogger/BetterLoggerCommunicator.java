package com.greenjon902.betterLogger;

import com.greenjon902.betterLogger.commands.Command;
import com.greenjon902.betterLogger.commands.CommandCtrl;
import com.greenjon902.betterLogger.commands.CommandCtrlEnd;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BetterLoggerCommunicator {
    public static final int pythonConn_typeLength = 4;
    public static final int pythonConn_messageLength = 8;

    private static final File pythonFile = new File(System.getProperty("user.dir"), "betterLoggerPortal.py");
    private static final File pythonExceptionsFile = new File(System.getProperty("user.dir"), "betterLoggerPortalExceptions.py");

    private boolean started = false;
    private ServerSocket serverSocket;
    private InputStream pythonConnectionInputStream;
    private OutputStream pythonConnectionOutputStream;
    private Thread loggerHandlerInThread;
    private Process pythonProcess;

    private final Map<String,String> environment = new HashMap<>();

    public BetterLoggerCommunicator(String name, String author, String version, String shortname) {
        environment.put("APPNAME", name);
        environment.put("APPAUTHOR", author);
        environment.put("APPVERSION", version);
        environment.put("SHORT_APPNAME", shortname);
        environment.put("LOG_FILE_NAME_FORMAT", "{short_appname}_{year}-{day}-{hour}-{minute}_{number}.log");
    }

    public BetterLoggerCommunicator() {

    }

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
        if (!pythonFile.exists() || !pythonExceptionsFile.exists() || (System.getenv("FORCEBETTERLOGGERCOPYFILE") != null && Boolean.parseBoolean(System.getenv("FORCEBETTERLOGGERCOPYFILE")))) {
            //noinspection ConstantConditions
            FileUtils.copyURLToFile(getClass().getResource("betterLoggerPortal.py"), pythonFile);
            System.out.println("Copied betterLoggerPortal.py");
            //noinspection ConstantConditions
            FileUtils.copyURLToFile(getClass().getResource("betterLoggerPortal.py"), pythonExceptionsFile);
            System.out.println("Copied betterLoggerPortal.py");
        }
        if (!Boolean.parseBoolean(System.getenv("STARTBETTERLOGGEREXTERNALLY"))) {
            ProcessBuilder processBuilder = new ProcessBuilder("python3", pythonFile.getAbsolutePath(), String.valueOf(port));
            processBuilder.environment().putAll(environment);
            pythonProcess = processBuilder.start();
        } else {
            System.out.println("Please run this command:  python3 " + pythonFile.getAbsolutePath() + " " + port);
        }

        System.out.println("Waiting on connection");
        Socket pythonConnection = serverSocket.accept();
        pythonConnectionInputStream = pythonConnection.getInputStream();
        pythonConnectionOutputStream = pythonConnection.getOutputStream();
        System.out.println("Incoming connection from " + pythonConnection.getInetAddress().toString() + ":" + pythonConnection.getPort());
    }

    private void handleLoggerIn() {
        System.out.println("Handling logger in on thread " + Thread.currentThread().getName());
        boolean running = true;
        while (running) {
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

                    switch (type) {
                        case "LOG":
                            System.out.print(message);
                            break;
                        case "INFO":
                            System.out.println(Colors.format("{BLACK}<INFO>  " + message + "{RESET}"));
                            break;
                        case "PIP_INSTALL":
                            System.out.println(Colors.format("{BLACK}<PIP_INSTALL>  " + message + "{RESET}"));
                            break;
                        case "ERROR":
                            System.out.print(Colors.format("{RED}<ERROR>  " + message + "{RESET}"));
                            running = false;
                            break;
                        case "CTRL":
                            if (message.equals("END")) {
                                pythonConnectionOutputStream.write(StandardCharsets.UTF_8.encode("F").array()); // Send message to say connection is done
                                running = false;
                            } else {
                                System.out.println(Colors.format("{RED}<ERROR>  Received unknown control message from betterLogger - \"" + message + "\"{RESET}"));
                            }
                            break;

                        default:
                            System.out.println(Colors.format("{RED}<ERROR>  Received unknown type from betterLogger - \"" + type + "\"{RESET}"));
                    }
                }

            } catch (Exception e) {
                System.out.println("Got exception while handling logger");
                System.out.print(Colors.RED);
                e.printStackTrace(System.out);
                System.out.print(Colors.RESET);
                break;
            }
        }
        System.out.flush();
    }

    public void sendCommand(Command command) {
        try {
            pythonConnectionOutputStream.write(command.encode());

        } catch (Exception e) {
            System.out.println("Got exception while sending data logger");
            System.out.print(Colors.RED);
            e.printStackTrace(System.out);
            System.out.print(Colors.RESET);
        }
    }

    public void start() {
        if (!started) {
            try {
                openServer();
                System.out.println("Logging on port " + serverSocket.getLocalPort());
                connectPython(serverSocket.getLocalPort());

                loggerHandlerInThread = new Thread(this::handleLoggerIn);
                loggerHandlerInThread.start();

                started = true;
            } catch (Exception e) {
                System.out.println(Colors.format("[ERROR] Failed to start logger, error:"));
                e.printStackTrace();
            }
        }
    }

    public void end() {
        try {
            sendCommand(new CommandCtrlEnd());
            loggerHandlerInThread.join();
            closeServer();
        } catch (Exception e) {
            System.out.println(Colors.format("[ERROR] Failed to end logger"));
        }
    }
}

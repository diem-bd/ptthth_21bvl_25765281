package baitap10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceDiscoveryServer {
    public static final int UDP_DISCOVERY_PORT = 5020;
    public static final int TCP_SERVICE_PORT = 5021;
    public static final String SERVICE_NAME = "EchoComputeService";
    public static final String SERVICE_VERSION = "v1.0.0";

    public static void main(String[] args) {
        System.out.println("=== Service Discovery Server Starting ===");

        // 1. Thread UDP Discovery: Lắng nghe và phản hồi quảng bá dịch vụ
        Thread discoveryThread = new Thread(() -> {
            byte[] buffer = new byte[1024];
            try (DatagramSocket udpSocket = new DatagramSocket(UDP_DISCOVERY_PORT)) {
                System.out.println("UDP Discovery Listener active on port " + UDP_DISCOVERY_PORT);

                while (true) {
                    DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
                    udpSocket.receive(requestPacket);

                    String msg = new String(requestPacket.getData(), requestPacket.getOffset(), requestPacket.getLength(), StandardCharsets.UTF_8).trim();
                    System.out.println("[UDP Discovery] Received '" + msg + "' from " + requestPacket.getAddress() + ":" + requestPacket.getPort());

                    if (msg.equalsIgnoreCase("DISCOVER_SERVICE")) {
                        String responseMsg = "SERVICE " + SERVICE_NAME + " " + TCP_SERVICE_PORT + " " + SERVICE_VERSION;
                        byte[] respBytes = responseMsg.getBytes(StandardCharsets.UTF_8);

                        DatagramPacket respPacket = new DatagramPacket(
                                respBytes, respBytes.length,
                                requestPacket.getAddress(), requestPacket.getPort());
                        udpSocket.send(respPacket);
                        System.out.println("[UDP Discovery] Responded: " + responseMsg);
                    }
                }
            } catch (IOException e) {
                System.err.println("UDP Discovery error: " + e.getMessage());
            }
        });
        discoveryThread.setDaemon(true);
        discoveryThread.start();

        // 2. TCP Service: Phục vụ dịch vụ chính cho client kết nối tới
        ExecutorService tcpPool = Executors.newFixedThreadPool(10);
        try (ServerSocket serverSocket = new ServerSocket(TCP_SERVICE_PORT)) {
            System.out.println("TCP Service (" + SERVICE_NAME + ") active on port " + TCP_SERVICE_PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                tcpPool.submit(() -> handleTcpClient(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("TCP Service error: " + e.getMessage());
        } finally {
            tcpPool.shutdown();
        }
    }

    private static void handleTcpClient(Socket socket) {
        String remote = socket.getRemoteSocketAddress().toString();
        System.out.println("[TCP Service] Client connected from: " + remote);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                System.out.println("[TCP Service " + remote + "] Request: " + trimmed);

                if (trimmed.equalsIgnoreCase("QUIT")) {
                    writer.println("OK BYE");
                    break;
                } else if (trimmed.equalsIgnoreCase("INFO")) {
                    writer.println("OK SERVICE=" + SERVICE_NAME + " VER=" + SERVICE_VERSION + " STATUS=ONLINE");
                } else if (trimmed.startsWith("ECHO ")) {
                    writer.println("OK " + trimmed.substring(5).toUpperCase());
                } else {
                    writer.println("ERR UNKNOWN_COMMAND (Try: INFO, ECHO <text>, QUIT)");
                }
            }
        } catch (IOException e) {
            System.err.println("[TCP Service] Error handling client: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
            System.out.println("[TCP Service] Client disconnected: " + remote);
        }
    }
}

package baitap6;

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

public class BenchmarkServer {
    public static final int TCP_PORT = 5016;
    public static final int UDP_PORT = 5016;
    public static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        System.out.println("=== Benchmark Server Starting ===");

        // Thread 1: Chạy UDP Echo Server
        Thread udpThread = new Thread(() -> {
            try (DatagramSocket udpSocket = new DatagramSocket(UDP_PORT)) {
                System.out.println("UDP Benchmark Server listening on port " + UDP_PORT);
                byte[] buffer = new byte[BUFFER_SIZE];

                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    udpSocket.receive(packet);

                    // Phản hồi lại đúng gói tin (Echo)
                    DatagramPacket response = new DatagramPacket(
                            packet.getData(), packet.getOffset(), packet.getLength(),
                            packet.getAddress(), packet.getPort());
                    udpSocket.send(response);
                }
            } catch (IOException e) {
                System.err.println("UDP Server error: " + e.getMessage());
            }
        });
        udpThread.setDaemon(true);
        udpThread.start();

        // Thread 2: Chạy TCP Echo Server với Thread Pool
        ExecutorService tcpPool = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(TCP_PORT)) {
            System.out.println("TCP Benchmark Server listening on port " + TCP_PORT);

            while (true) {
                Socket client = serverSocket.accept();
                tcpPool.submit(() -> handleTcpClient(client));
            }
        } catch (IOException e) {
            System.err.println("TCP Server error: " + e.getMessage());
        } finally {
            tcpPool.shutdown();
        }
    }

    private static void handleTcpClient(Socket socket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equalsIgnoreCase("QUIT")) {
                    writer.println("BYE");
                    break;
                }
                writer.println(line); // Echo lại
            }
        } catch (IOException ignored) {
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }
}

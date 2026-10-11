package baitap4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalculatorServer {
    public static final int PORT = 5014;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : PORT;
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("=== Remote Calculator Server is running on port " + port + " ===");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server socket error: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    private static void handleClient(Socket socket) {
        String clientAddress = socket.getRemoteSocketAddress().toString();
        System.out.println("Client connected: " + clientAddress);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[" + clientAddress + "] Request: " + line);
                String response = processCommand(line);
                writer.println(response);
                System.out.println("[" + clientAddress + "] Response: " + response);

                if (line.trim().equalsIgnoreCase("QUIT")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Communication error with " + clientAddress + ": " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
            System.out.println("Client disconnected: " + clientAddress);
        }
    }

    public static String processCommand(String line) {
        if (line == null) {
            return "ERR INVALID_FORMAT";
        }
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return "ERR INVALID_FORMAT";
        }
        if (trimmed.equalsIgnoreCase("QUIT")) {
            return "OK BYE";
        }

        String[] parts = trimmed.split("\\s+");
        if (parts.length == 0 || !parts[0].equalsIgnoreCase("CALC")) {
            return "ERR INVALID_FORMAT";
        }

        if (parts.length != 4) {
            return "ERR INVALID_FORMAT";
        }

        String op = parts[1];
        String num1Str = parts[2];
        String num2Str = parts[3];

        if (!op.equals("+") && !op.equals("-") && !op.equals("*") && !op.equals("/")) {
            return "ERR UNSUPPORTED_OPERATOR";
        }

        double a, b;
        try {
            a = Double.parseDouble(num1Str);
            b = Double.parseDouble(num2Str);
        } catch (NumberFormatException e) {
            return "ERR INVALID_NUMBER";
        }

        double result;
        switch (op) {
            case "+":
                result = a + b;
                break;
            case "-":
                result = a - b;
                break;
            case "*":
                result = a * b;
                break;
            case "/":
                if (b == 0.0) {
                    return "ERR DIVIDE_BY_ZERO";
                }
                result = a / b;
                break;
            default:
                return "ERR UNSUPPORTED_OPERATOR";
        }

        if (result == Math.floor(result) && !Double.isInfinite(result) && Math.abs(result) < Long.MAX_VALUE) {
            return "OK " + (long) result;
        } else {
            return "OK " + result;
        }
    }
}

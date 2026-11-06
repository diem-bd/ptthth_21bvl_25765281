package baitap7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class LogServer {
    public static final int PORT = 5017;
    private static final String LOG_DIR = "data/logs";
    private static final Pattern CLIENT_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        int port;
        try {
            port = args.length > 0 ? Integer.parseInt(args[0]) : PORT;
        } catch (NumberFormatException e) {
            System.err.println("Cổng phải là số nguyên: " + args[0]);
            return;
        }

        File dir = new File(LOG_DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Không tạo được thư mục log: " + dir.getAbsolutePath());
            return;
        }

        ExecutorService pool = Executors.newFixedThreadPool(20);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("=== Message Logger Server listening on port " + port + " ===");
            System.out.println("Logs stored in directory: " + dir.getAbsolutePath());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("LogServer error: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    private static void handleClient(Socket socket) {
        String remote = socket.getRemoteSocketAddress().toString();
        System.out.println("Client connected: " + remote);
        String clientId = null;

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String line;
            while ((line = in.readLine()) != null) {
                String trimmed = line.trim();
                // Mỗi yêu cầu đều phải có phản hồi, nếu không client sẽ chờ vô hạn.
                if (trimmed.isEmpty()) {
                    out.println("ERR EMPTY_MESSAGE");
                    continue;
                }

                if (trimmed.equalsIgnoreCase("QUIT")) {
                    out.println("OK BYE");
                    break;
                }

                if (trimmed.startsWith("HELLO ")) {
                    String candidateId = trimmed.substring(6).trim();
                    if (!CLIENT_ID_PATTERN.matcher(candidateId).matches()) {
                        out.println("ERR INVALID_CLIENT_ID");
                    } else {
                        clientId = candidateId;
                        out.println("OK WELCOME " + clientId);
                        System.out.println("Client identified: " + clientId + " (" + remote + ")");
                    }
                } else {
                    if (clientId == null) {
                        out.println("ERR MUST_HELLO_FIRST");
                    } else {
                        String logMessage = trimmed.startsWith("MSG ") ? trimmed.substring(4) : trimmed;
                        // Chỉ xác nhận đã ghi khi việc ghi thực sự thành công.
                        if (writeLog(clientId, remote, logMessage)) {
                            out.println("OK LOGGED");
                        } else {
                            out.println("ERR LOG_WRITE_FAILED");
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Session error with " + remote + ": " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
            System.out.println("Client disconnected: " + remote + (clientId != null ? " [" + clientId + "]" : ""));
        }
    }

    /**
     * Ghi thêm một dòng vào data/logs/<clientId>.txt.
     * synchronized trên cả lớp là đủ cho bài lab: mọi client ghi nối tiếp nhau,
     * không cần map ổ khoá riêng cho từng client.
     *
     * @return true nếu ghi thành công, false nếu có lỗi vào/ra.
     */
    private static synchronized boolean writeLog(String clientId, String remoteAddress, String content) {
        File logFile = new File(LOG_DIR, clientId + ".txt");
        try (PrintWriter fw = new PrintWriter(new FileWriter(logFile, StandardCharsets.UTF_8, true))) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            fw.println("[" + timestamp + "] [" + remoteAddress + "] " + content);
            return !fw.checkError();
        } catch (IOException e) {
            System.err.println("Error writing log for " + clientId + ": " + e.getMessage());
            return false;
        }
    }
}

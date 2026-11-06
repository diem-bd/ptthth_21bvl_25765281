package vidu43;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ví dụ 4.3: phục vụ nhiều client đồng thời bằng ExecutorService.
 * Phần giao thức và xử lý lệnh được TÁI SỬ DỤNG từ vidu42.TcpCommandServer,
 * không sao chép lại: cùng một serve() chạy trong từng luồng của thread pool.
 */
public class MultiClientTcpServer {
    private static final int PORT = 5000;
    private static final int MAX_CLIENTS = 20;

    public static void main(String[] args) {
        int port;
        try {
            port = args.length > 0 ? Integer.parseInt(args[0]) : PORT;
        } catch (NumberFormatException e) {
            System.err.println("Cổng phải là số nguyên: " + args[0]);
            return;
        }
        ExecutorService pool = Executors.newFixedThreadPool(MAX_CLIENTS);

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Multi-client server listening on port " + port
                    + " (Thread Pool: " + MAX_CLIENTS + ")");

            while (true) {
                Socket socket = server.accept();
                pool.submit(() -> {
                    String client = String.valueOf(socket.getRemoteSocketAddress());
                    System.out.println("Connected: " + client);
                    try (socket) {
                        vidu42.TcpCommandServer.serve(socket);
                    } catch (IOException e) {
                        System.err.println("Client " + client + " failed: " + e.getMessage());
                    } finally {
                        System.out.println("Disconnected: " + client);
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }
}

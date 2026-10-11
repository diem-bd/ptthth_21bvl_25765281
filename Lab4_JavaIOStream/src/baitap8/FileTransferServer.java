package baitap8;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileTransferServer {
    public static final int PORT = 5018;
    public static final String UPLOAD_DIR = "upload";

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : PORT;

        File uploadFolder = new File(UPLOAD_DIR);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        ExecutorService pool = Executors.newFixedThreadPool(10);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("=== Secure File Transfer Server running on port " + port + " ===");
            System.out.println("Upload folder: " + uploadFolder.getAbsolutePath());

            while (true) {
                Socket socket = serverSocket.accept();
                pool.submit(() -> handleTransfer(socket));
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    private static void handleTransfer(Socket socket) {
        String remote = socket.getRemoteSocketAddress().toString();
        System.out.println("Incoming connection from: " + remote);

        try (DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            // 1. Nhận Metadata
            String clientFilename = in.readUTF();
            long fileSize = in.readLong();
            String clientSha256 = in.readUTF();

            System.out.println("[" + remote + "] Requested file: '" + clientFilename + "', size: " + fileSize + " bytes, SHA256: " + clientSha256);

            // 2. Kiểm tra ngăn chặn Path Traversal
            if (clientFilename.contains("..") || clientFilename.contains("/") || clientFilename.contains("\\")) {
                System.err.println("[" + remote + "] Path traversal attack detected in: " + clientFilename);
                out.writeUTF("ERR PATH_TRAVERSAL_DETECTED");
                return;
            }

            // Lấy tên file an toàn
            Path safePath = Paths.get(UPLOAD_DIR, Paths.get(clientFilename).getFileName().toString());
            File destinationFile = safePath.toFile();

            // 3. Đọc chính xác số byte đã khai báo và tính SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            long bytesRemaining = fileSize;

            try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
                while (bytesRemaining > 0) {
                    int bytesToRead = (int) Math.min(buffer.length, bytesRemaining);
                    int bytesRead = in.read(buffer, 0, bytesToRead);
                    if (bytesRead == -1) {
                        break;
                    }
                    fos.write(buffer, 0, bytesRead);
                    digest.update(buffer, 0, bytesRead);
                    bytesRemaining -= bytesRead;
                }
            }

            if (bytesRemaining > 0) {
                destinationFile.delete();
                out.writeUTF("ERR INCOMPLETE_DATA");
                return;
            }

            // 4. Kiểm tra mã băm SHA-256
            byte[] hashBytes = digest.digest();
            StringBuilder hashBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hashBuilder.append(String.format("%02x", b));
            }
            String serverSha256 = hashBuilder.toString();

            if (serverSha256.equalsIgnoreCase(clientSha256)) {
                System.out.println("[" + remote + "] File verified and saved: " + destinationFile.getName());
                out.writeUTF("OK FILE_SAVED " + serverSha256);
            } else {
                destinationFile.delete();
                System.err.println("[" + remote + "] SHA-256 mismatch! Server: " + serverSha256 + ", Client: " + clientSha256);
                out.writeUTF("ERR HASH_MISMATCH");
            }

        } catch (Exception e) {
            System.err.println("[" + remote + "] Transfer error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }
}

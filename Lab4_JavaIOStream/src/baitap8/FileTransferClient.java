package baitap8;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.MessageDigest;

public class FileTransferClient {
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : FileTransferServer.PORT;
        String filePath = args.length > 2 ? args[2] : null;

        if (filePath == null) {
            System.out.println("Usage: java baitap8.FileTransferClient <host> <port> <filepath>");
            return;
        }

        File file = new File(filePath);
        if (!file.exists() && !filePath.contains("..")) {
            System.err.println("File does not exist: " + filePath);
            return;
        }

        try {
            // Tính SHA-256
            String sha256 = file.exists() ? calculateSha256(file) : "dummyhash";
            long fileSize = file.exists() ? file.length() : 0;
            String filename = file.getName();

            // Nếu người dùng cố tình test path traversal
            if (filePath.contains("..")) {
                filename = filePath;
            }

            System.out.println("Connecting to " + host + ":" + port + "...");
            try (Socket socket = new Socket(host, port);
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                 DataInputStream in = new DataInputStream(socket.getInputStream())) {

                System.out.println("Sending metadata: Filename=" + filename + ", Size=" + fileSize + ", SHA256=" + sha256);
                out.writeUTF(filename);
                out.writeLong(fileSize);
                out.writeUTF(sha256);

                if (file.exists() && fileSize > 0) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                    }
                }
                out.flush();

                String serverResponse = in.readUTF();
                System.out.println("Server response: " + serverResponse);
            }
        } catch (Exception e) {
            System.err.println("FileTransferClient error: " + e.getMessage());
        }
    }

    public static String calculateSha256(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        }
        byte[] hash = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

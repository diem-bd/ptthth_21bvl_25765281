package baitap8;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileTransferClient {
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port;
        try {
            port = args.length > 1 ? Integer.parseInt(args[1]) : FileTransferServer.PORT;
        } catch (NumberFormatException e) {
            System.err.println("Port phải là số nguyên");
            return;
        }

        if (args.length < 3) {
            System.out.println("Cách dùng: java baitap8.FileTransferClient <host> <port> <filepath>");
            System.out.println("Ví dụ kiểm thử path traversal: <host> " + port + " ../etc/passwd");
            return;
        }
        // Tên gửi đi mặc định là tên tệp thật. Khi tham số thứ tư được cung cấp,
        // client gửi tên đó thay thế để chủ động kiểm thử trường hợp tên nguy hiểm.
        String filePath = args[2];
        String tenGui = args.length > 3 ? args[3] : new File(filePath).getName();

        File file = new File(filePath);
        boolean canRead = file.isFile() && file.canRead();
        if (!canRead) {
            System.err.println("Không đọc được tệp nguồn: " + filePath);
            // Vẫn cho phép chạy khi đang kiểm thử tên nguy hiểm để thấy server từ chối.
            if (!tenGui.contains("..")) return;
        }

        try {
            long fileSize = canRead ? file.length() : 0;
            String sha256 = canRead ? calculateSha256(file) : "-";

            System.out.println("Connecting to " + host + ":" + port + "...");
            try (Socket socket = new Socket(host, port);
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                 DataInputStream in = new DataInputStream(socket.getInputStream())) {

                // Giao thức: tên (UTF) -> kích thước (long) -> SHA-256 (UTF) -> dữ liệu thô.
                System.out.println("Sending metadata: filename=" + tenGui
                        + ", size=" + fileSize + ", sha256=" + sha256);
                out.writeUTF(tenGui);
                out.writeLong(fileSize);
                out.writeUTF(sha256);

                if (canRead && fileSize > 0) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                    }
                }
                out.flush();

                System.out.println("Server response: " + in.readUTF());
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println("FileTransferClient error: " + e.getMessage());
        }
    }

    public static String calculateSha256(File file) throws IOException, NoSuchAlgorithmException {
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

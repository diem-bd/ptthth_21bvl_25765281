package Lab3_JavaStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BinaryFileCopy {
    private static final int BUFFER_SIZE = 8192;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Cách dùng: java -cp ../bin Lab3_JavaStream.BinaryFileCopy <nguồn> <đích>");
            return;
        }

        long totalBytes = 0;

        // Nếu nguồn và đích là cùng một tệp, mở đầu ra sẽ làm rỗng tệp nguồn trước khi đọc.
        Path nguon = Path.of(args[0]);
        Path dich = Path.of(args[1]);
        try {
            boolean cungTep = nguon.toAbsolutePath().normalize()
                    .equals(dich.toAbsolutePath().normalize());
            if (!cungTep && Files.exists(dich)) {
                cungTep = Files.isSameFile(nguon, dich);
            }
            if (cungTep) {
                System.err.println("Nguồn và đích là cùng một tệp, không thể sao chép.");
                return;
            }
        } catch (IOException e) {
            System.err.println("Không kiểm tra được đường dẫn: " + e.getMessage());
            return;
        }

        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(args[0]));
             BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(args[1]))) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                // Lần đọc cuối thường ngắn hơn buffer: chỉ ghi đúng bytesRead byte.
                output.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }

            System.out.println("Nguồn: " + args[0]);
            System.out.println("Đích: " + args[1]);
            System.out.println("Đã sao chép " + totalBytes + " byte.");
        } catch (IOException e) {
            System.err.println("Sao chép thất bại: " + e.getMessage());
        }
    }
}

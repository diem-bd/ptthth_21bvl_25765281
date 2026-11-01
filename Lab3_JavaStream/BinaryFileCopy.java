package Lab3_JavaStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class BinaryFileCopy {
    private static final int BUFFER_SIZE = 8192;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Cách dùng: java -cp ../bin Lab3_JavaStream.BinaryFileCopy <nguồn> <đích>");
            return;
        }

        try {
            Path source = Path.of(args[0]);
            Path target = Path.of(args[1]);
            if (source.toAbsolutePath().normalize().equals(target.toAbsolutePath().normalize())
                    || (Files.exists(target) && Files.isSameFile(source, target))) {
                System.err.println("Nguồn và đích phải là hai tệp khác nhau.");
                return;
            }

            long totalBytes = 0;
            // CREATE_NEW tránh ghi đè ảnh có sẵn khi nhập nhầm tên đích.
            try (InputStream input = new BufferedInputStream(Files.newInputStream(source));
                 OutputStream output = new BufferedOutputStream(Files.newOutputStream(
                         target, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE))) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    // Lần đọc cuối thường ngắn hơn buffer: chỉ ghi bytesRead byte.
                    output.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                }
            }
            System.out.println("Nguồn: " + source);
            System.out.println("Đích: " + target);
            System.out.println("Đã sao chép " + totalBytes + " byte.");
            System.out.println("Kích thước nguồn/đích: " + Files.size(source) + "/" + Files.size(target));
            System.out.println("Nội dung giống nhau: " + (Files.mismatch(source, target) == -1));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Sao chép thất bại [" + args[0] + " -> " + args[1] + "]: " + e.getMessage());
        }
    }
}

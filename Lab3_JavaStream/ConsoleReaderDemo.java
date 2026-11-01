package Lab3_JavaStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ConsoleReaderDemo {
    public static void main(String[] args) {
        // Không đóng reader này vì nó bọc luồng dùng chung System.in.
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        int count = 0;
        System.out.println("Nhập văn bản; nhập q để kết thúc:");

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equalsIgnoreCase("q")) {
                    break;
                }
                System.out.printf("Dòng %d: %s%n", ++count, line);
            }
        } catch (IOException e) {
            System.err.println("Không thể đọc bàn phím: " + e.getMessage());
        }
        System.out.println("Tổng số dòng đã nhập: " + count);
    }
}

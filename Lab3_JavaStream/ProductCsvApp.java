package Lab3_JavaStream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Bài thực hành 4: đọc products.csv, tạo danh sách Product, hiển thị,
 * tính tổng giá trị tồn kho và ghi báo cáo ra report.txt.
 * Bài này tự đọc CSV để có thể học/chạy độc lập, không phụ thuộc InventoryManager.
 */
public class ProductCsvApp {
    public static void main(String[] args) {
        Path input = Path.of("data", "products.csv");
        Path report = Path.of("data", "report.txt");
        try {
            List<Product> products = docCsv(input);
            if (products.isEmpty()) {
                System.out.println("Không có dòng sản phẩm hợp lệ nào trong " + input);
                return;
            }

            System.out.println("--- Danh sách sản phẩm ---");
            double total = 0;
            for (Product product : products) {
                System.out.println(product);
                total += product.inventoryValue();
            }

            try (BufferedWriter writer = Files.newBufferedWriter(report, StandardCharsets.UTF_8)) {
                writer.write("Số sản phẩm: " + products.size());
                writer.newLine();
                writer.write(String.format(Locale.US, "Tổng giá trị tồn kho: %,.0f VND", total));
                writer.newLine();
            }
            System.out.printf(Locale.US, "Tổng giá trị tồn kho: %,.0f VND%n", total);
            System.out.println("Đã ghi báo cáo: " + report.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Lỗi xử lý " + input + " / " + report + ": " + e.getMessage());
        }
    }

    /** Đọc tệp CSV: dòng 1 là tiêu đề, mỗi dòng sau là một sản phẩm. */
    private static List<Product> docCsv(Path file) throws IOException {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null || !header.equals(Product.CSV_HEADER)) {
                throw new IOException("Dòng 1 phải là tiêu đề: " + Product.CSV_HEADER);
            }
            String line;
            int soDong = 1;
            while ((line = reader.readLine()) != null) {
                soDong++;
                if (line.isBlank()) continue;
                try {
                    products.add(Product.fromCsv(line));
                } catch (IllegalArgumentException e) {
                    System.err.println(file + ", dòng " + soDong + ": " + e.getMessage() + ". Bỏ qua.");
                }
            }
        }
        return products;
    }
}

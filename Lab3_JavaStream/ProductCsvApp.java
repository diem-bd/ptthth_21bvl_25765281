package Lab3_JavaStream;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class ProductCsvApp {
    public static void main(String[] args) {
        Path input = Path.of("data", "products.csv");
        Path report = Path.of("data", "report.txt");
        try {
            List<Product> products = InventoryManager.loadProducts(input);
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
}

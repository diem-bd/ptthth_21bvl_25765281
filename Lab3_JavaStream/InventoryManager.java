package Lab3_JavaStream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InventoryManager {
    private static final Path INVENTORY = Path.of("data", "inventory.csv");
    private static final Path REPORT = Path.of("data", "inventory-report.txt");

    public static List<Product> loadProducts(Path file) throws IOException {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null || !header.equals(Product.CSV_HEADER)) {
                throw new IOException("Tệp " + file + ", dòng 1: thiếu/sai tiêu đề " + Product.CSV_HEADER);
            }
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }
                try {
                    products.add(Product.fromCsv(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("Tệp " + file + ", dòng " + lineNumber
                            + ": " + e.getMessage() + ". Bỏ qua dòng.");
                }
            }
        }
        return products;
    }

    public static void saveProducts(Path file, List<Product> products) throws IOException {
        Files.createDirectories(file.toAbsolutePath().getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            writer.write(Product.CSV_HEADER);
            writer.newLine();
            for (Product product : products) {
                writer.write(product.toCsv());
                writer.newLine();
            }
        }
    }

    public static double totalValue(List<Product> products) {
        double total = 0;
        for (Product product : products) {
            total += product.inventoryValue();
        }
        return total;
    }

    public static Product highestValueProduct(List<Product> products) {
        Product highest = null;
        for (Product product : products) {
            if (highest == null || product.inventoryValue() > highest.inventoryValue()) {
                highest = product;
            }
        }
        return highest;
    }

    public static void writeReport(Path file, List<Product> products) throws IOException {
        Files.createDirectories(file.toAbsolutePath().getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            writer.write("BÁO CÁO TỒN KHO");
            writer.newLine();
            for (Product product : products) {
                writer.write(product.toString());
                writer.newLine();
            }
            writer.write("Số sản phẩm: " + products.size());
            writer.newLine();
            writer.write(String.format(Locale.US, "Tổng giá trị tồn kho: %,.0f VND", totalValue(products)));
            writer.newLine();
            Product highest = highestValueProduct(products);
            writer.write("Sản phẩm có giá trị tồn kho cao nhất: "
                    + (highest == null ? "Danh sách rỗng" : highest));
            writer.newLine();
        }
    }

    private static String readLine(BufferedReader reader, String prompt) throws IOException {
        System.out.print(prompt);
        String line = reader.readLine();
        if (line == null) {
            throw new EOFException("Đã kết thúc dữ liệu nhập (EOF)");
        }
        return line;
    }

    private static int readCount(BufferedReader reader) throws IOException {
        while (true) {
            try {
                int count = Integer.parseInt(readLine(reader, "Số sản phẩm cần nhập: ").trim());
                if (count < 0) {
                    throw new IllegalArgumentException("Số sản phẩm không được âm");
                }
                return count;
            } catch (IllegalArgumentException e) {
                System.out.println("Số lượng không hợp lệ. Nhập số nguyên không âm.");
            }
        }
    }

    private static List<Product> inputProducts(BufferedReader reader) throws IOException {
        List<Product> products = new ArrayList<>();
        int count = readCount(reader);
        for (int i = 0; i < count;) {
            System.out.println("Sản phẩm thứ " + (i + 1) + ":");
            String code = readLine(reader, "Mã: ");
            String name = readLine(reader, "Tên: ");
            String price = readLine(reader, "Đơn giá: ");
            String quantity = readLine(reader, "Số lượng: ");
            try {
                Product product = new Product(code, name,
                        Double.parseDouble(price.trim()), Integer.parseInt(quantity.trim()));
                products.add(product);
                i++;
                System.out.println("Đã nhận: " + product);
            } catch (NumberFormatException e) {
                System.out.println("Từ chối: đơn giá/số lượng không phải số. Nhập lại sản phẩm này.");
            } catch (IllegalArgumentException e) {
                System.out.println("Từ chối: " + e.getMessage() + ". Nhập lại sản phẩm này.");
            }
        }
        return products;
    }

    private static void displayProducts(List<Product> products) {
        for (Product product : products) {
            System.out.println(product);
        }
        System.out.println("Số sản phẩm: " + products.size());
        System.out.printf(Locale.US, "Tổng giá trị tồn kho: %,.0f VND%n", totalValue(products));
        Product highest = highestValueProduct(products);
        System.out.println("Tồn kho cao nhất: " + (highest == null ? "Danh sách rỗng" : highest));
    }

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        List<Product> products = new ArrayList<>();
        System.out.println("=== QUẢN LÝ TỒN KHO – OOP & JAVA I/O ===");
        System.out.println("Thư mục làm việc: " + Path.of("").toAbsolutePath());

        while (true) {
            System.out.println("\n1. Nhập và lưu CSV  2. Đọc CSV  3. Hiển thị  4. Ghi báo cáo  0. Thoát");
            try {
                String choice = readLine(reader, "Chọn: ").trim();
                switch (choice) {
                    case "1":
                        List<Product> entered = inputProducts(reader);
                        saveProducts(INVENTORY, entered);
                        products = entered;
                        System.out.println("Đã lưu " + products.size() + " sản phẩm vào " + INVENTORY);
                        break;
                    case "2":
                        String path = readLine(reader, "Tệp CSV (Enter = data/inventory.csv): ").trim();
                        Path input = path.isEmpty() ? INVENTORY : Path.of(path);
                        // Chỉ thay danh sách khi đọc thành công, tránh mất dữ liệu đang dùng.
                        products = loadProducts(input);
                        System.out.println("Đã đọc " + products.size() + " sản phẩm từ " + input);
                        break;
                    case "3":
                        displayProducts(products);
                        break;
                    case "4":
                        writeReport(REPORT, products);
                        System.out.println("Đã ghi báo cáo: " + REPORT);
                        break;
                    case "0":
                        System.out.println("Kết thúc chương trình.");
                        return;
                    default:
                        System.out.println("Chọn số từ 0 đến 4.");
                }
            } catch (EOFException e) {
                System.out.println(e.getMessage() + ". Kết thúc chương trình.");
                return;
            } catch (NoSuchFileException e) {
                System.err.println("Không tìm thấy tệp: " + e.getFile());
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("Lỗi xử lý dữ liệu: " + e.getMessage());
            }
        }
    }
}

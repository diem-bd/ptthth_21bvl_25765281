package Lab3_JavaStream;

import java.util.Locale;

public class Product {
    public static final String CSV_HEADER = "ma,ten,donGia,soLuong";

    private final String code;
    private final String name;
    private final double unitPrice;
    private final int quantity;

    public Product(String code, String name, double unitPrice, int quantity) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Mã không được rỗng");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên không được rỗng");
        }
        if (unitPrice <= 0) {
            throw new IllegalArgumentException("Đơn giá phải lớn hơn 0");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Số lượng không được âm");
        }
        this.code = code.trim();
        this.name = name.trim();
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }

    public double inventoryValue() {
        return unitPrice * quantity;
    }

    public String toCsv() {
        // Double.toString dùng dấu chấm thập phân, không phụ thuộc locale của máy.
        return code + "," + name + "," + Double.toString(unitPrice) + "," + quantity;
    }

    public static Product fromCsv(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length != 4) {
            throw new IllegalArgumentException("Cần 4 cột, nhận được " + parts.length);
        }
        try {
            return new Product(parts[0].trim(), parts[1].trim(),
                    Double.parseDouble(parts[2].trim()), Integer.parseInt(parts[3].trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Đơn giá hoặc số lượng không phải số hợp lệ", e);
        }
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s - %s | Giá: %,.0f | SL: %d | Tồn kho: %,.0f VND",
                code, name, unitPrice, quantity, inventoryValue());
    }
}

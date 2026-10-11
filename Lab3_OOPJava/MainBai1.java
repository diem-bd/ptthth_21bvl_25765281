package Lab3_OOPJava;

public class MainBai1 {
    public static void main(String[] args) {
        System.out.println("=== BÀI 1: QUẢN LÝ SẢN PHẨM – TÍNH ĐÓNG GÓI ===\n");

        // 1. Tạo ít nhất hai sản phẩm
        SanPham sp1 = new SanPham("SP01", "Bàn phím cơ", 1500000, 10);
        SanPham sp2 = new SanPham("SP02", "Chuột Logitech", 800000, 5);

        System.out.println("--- Trạng thái ban đầu ---");
        sp1.hienThiThongTin();
        sp2.hienThiThongTin();

        // 2. Nhập thêm hàng cho sản phẩm 1
        System.out.println("\n--- Thao tác: Nhập thêm 5 cái cho SP01 ---");
        sp1.nhapHang(5);
        sp1.hienThiThongTin();

        // 3. Thử bán hàng thành công cho sản phẩm 2
        System.out.println("\n--- Thao tác: Bán 3 cái SP02 ---");
        boolean banOk = sp2.banHang(3);
        System.out.println("Kết quả bán: " + banOk);
        sp2.hienThiThongTin();

        // 4. Thử bán số lượng lớn hơn tồn kho cho sản phẩm 2
        System.out.println("\n--- Thao tác: Bán 10 cái SP02 (lớn hơn tồn kho) ---");
        boolean banFail = sp2.banHang(10);
        System.out.println("Kết quả bán: " + banFail);
        sp2.hienThiThongTin();
    }
}

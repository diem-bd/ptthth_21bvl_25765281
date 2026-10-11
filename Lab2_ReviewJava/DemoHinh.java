package Lab2_ReviewJava;

public class DemoHinh {
    public static void main(String[] args) {
        System.out.println("=== DEMO BÀI 1: HÌNH HỌC ===");

        // Khởi tạo hình chữ nhật
        HCN hcn1 = new HCN(5.5, 4.0);
        hcn1.xuatThongTin();

        // Khởi tạo hình vuông
        HVuong hv1 = new HVuong(6.0);
        hv1.xuatThongTin();

        // Khởi tạo hình tam giác
        HTG htg1 = new HTG(3.0, 4.0, 5.0);
        htg1.xuatThongTin();
    }
}

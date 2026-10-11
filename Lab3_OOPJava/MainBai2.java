package Lab3_OOPJava;

public class MainBai2 {
    public static void main(String[] args) {
        System.out.println("=== BÀI 2: QUẢN LÝ NGƯỜI TRONG TRƯỜNG ĐẠI HỌC – TÍNH KẾ THỪA ===\n");

        // 1. Tạo hai sinh viên
        SinhVien sv1 = new SinhVien("Trần Văn An", 2003, "TP.HCM", "SV001", "Kỹ thuật phần mềm", 8.7);
        SinhVien sv2 = new SinhVien("Lê Thị Mai", 2004, "Bình Dương", "SV002", "Khoa học máy tính", 6.8);

        // 2. Tạo hai giảng viên
        GiangVien gv1 = new GiangVien("TS. Nguyễn Văn Hùng", 1980, "TP.HCM", "GV101", "Hệ thống thông tin", 10000000, 3.5);
        GiangVien gv2 = new GiangVien("ThS. Đỗ Thu Hà", 1988, "Đồng Nai", "GV102", "Công nghệ phần mềm", 8500000, 2.8);

        // 3. Hiển thị thông tin từng đối tượng, xếp loại sinh viên, lương giảng viên
        System.out.println("--- Danh sách Sinh viên ---");
        sv1.hienThiThongTin();
        sv2.hienThiThongTin();

        System.out.println("\n--- Danh sách Giảng viên ---");
        gv1.hienThiThongTin();
        gv2.hienThiThongTin();
    }
}

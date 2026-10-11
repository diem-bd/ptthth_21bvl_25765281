package Lab3_OOPJava;

import java.time.Year;

public class Nguoi {
    private String hoTen;
    private int namSinh;
    private String diaChi;

    public Nguoi(String hoTen, int namSinh, String diaChi) {
        this.hoTen = hoTen;
        this.namSinh = namSinh;
        this.diaChi = diaChi;
    }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public int getNamSinh() { return namSinh; }
    public void setNamSinh(int namSinh) { this.namSinh = namSinh; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public int tinhTuoi() {
        int currentYear = Year.now().getValue();
        return currentYear - namSinh;
    }

    public void hienThiThongTin() {
        System.out.printf("Họ tên: %-18s | Năm sinh: %d (Tuổi: %2d) | Địa chỉ: %-12s",
                hoTen, namSinh, tinhTuoi(), diaChi);
    }
}

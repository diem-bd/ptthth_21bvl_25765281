package Lab2_ReviewJava;

import java.util.Scanner;

public class Person {
    private String ten;
    private String gioiTinh;
    private String ngaySinh;
    private String diaChi;

    public Person() {
    }

    public Person(String ten, String gioiTinh, String ngaySinh, String diaChi) {
        this.ten = ten;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.diaChi = diaChi;
    }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public void inputInfo(Scanner sc) {
        System.out.print("Nhập họ tên: ");
        this.ten = sc.nextLine();
        System.out.print("Nhập giới tính: ");
        this.gioiTinh = sc.nextLine();
        System.out.print("Nhập ngày sinh (dd/MM/yyyy): ");
        this.ngaySinh = sc.nextLine();
        System.out.print("Nhập địa chỉ: ");
        this.diaChi = sc.nextLine();
    }

    public void printInfo() {
        System.out.printf("Tên: %-18s | Giới tính: %-5s | Ngày sinh: %-10s | Địa chỉ: %s",
                ten, gioiTinh, ngaySinh, diaChi);
    }
}

package Lab2_ReviewJava;

import java.util.Scanner;

public class Student extends Person {
    private double diemTrungBinh;
    private String email;

    public Student() {
        super();
    }

    public Student(String ten, String gioiTinh, String ngaySinh, String diaChi,
                   double diemTrungBinh, String email) {
        super(ten, gioiTinh, ngaySinh, diaChi);
        setDiemTrungBinh(diemTrungBinh);
        setEmail(email);
    }

    public double getDiemTrungBinh() {
        return diemTrungBinh;
    }

    public void setDiemTrungBinh(double diemTrungBinh) {
        if (diemTrungBinh >= 0.0 && diemTrungBinh <= 10.0) {
            this.diemTrungBinh = diemTrungBinh;
        } else {
            throw new IllegalArgumentException("Điểm trung bình phải từ 0.0 đến 10.0!");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && email.contains("@") && !email.contains(" ")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email phải chứa ký tự @ và không có khoảng trắng!");
        }
    }

    public boolean xetHocBong() {
        return this.diemTrungBinh > 8.0;
    }

    public void inputInfo(Scanner sc) {
        super.inputInfo(sc);
        while (true) {
            try {
                System.out.print("Nhập điểm trung bình (0.0 - 10.0): ");
                double dtb = Double.parseDouble(sc.nextLine());
                setDiemTrungBinh(dtb);
                break;
            } catch (Exception e) {
                System.out.println("Lỗi: " + e.getMessage() + ". Vui lòng nhập lại!");
            }
        }

        while (true) {
            try {
                System.out.print("Nhập email (chứa @, không có dấu cách): ");
                String mail = sc.nextLine();
                setEmail(mail);
                break;
            } catch (Exception e) {
                System.out.println("Lỗi: " + e.getMessage() + ". Vui lòng nhập lại!");
            }
        }
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.printf(" | ĐTB: %4.1f | Email: %-22s | Học bổng: %s%n",
                diemTrungBinh, email, xetHocBong() ? "CÓ" : "KHÔNG");
    }
}

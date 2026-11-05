package Lab2_ReviewJava;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentDemo {
    public static void main(String[] args) {
        System.out.println("=== DEMO BÀI 3, 4, 5: QUẢN LÝ SINH VIÊN ===");
        Scanner sc = new Scanner(System.in);
        List<Student> list = new ArrayList<>();

        // Đề Bài 5: "Nhập vào n sinh viên (n là số lượng sinh viên được nhập vào từ bàn phím)"
        System.out.print("Nhập số lượng sinh viên n = ");
        int n = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < n; i++) {
            System.out.println("--- Sinh viên thứ " + (i + 1) + " ---");
            Student s = new Student();
            s.inputInfo(sc);
            list.add(s);
        }

        System.out.println("\n1. Hiển thị danh sách tất cả sinh viên:");
        for (Student s : list) {
            s.printInfo();
        }

        System.out.println("\n2. Sinh viên có điểm trung bình cao nhất và thấp nhất:");
        Student maxSV = list.get(0);
        Student minSV = list.get(0);
        for (Student s : list) {
            if (s.getDiemTrungBinh() > maxSV.getDiemTrungBinh()) maxSV = s;
            if (s.getDiemTrungBinh() < minSV.getDiemTrungBinh()) minSV = s;
        }
        System.out.println("-> SV Điểm TB cao nhất:");
        maxSV.printInfo();
        System.out.println("-> SV Điểm TB thấp nhất:");
        minSV.printInfo();

        System.out.println("\n3. Danh sách các sinh viên đạt học bổng (ĐTB > 8.0):");
        for (Student s : list) {
            if (s.xetHocBong()) {
                s.printInfo();
            }
        }
    }
}

package Lab2_ReviewJava;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentDemo {
    public static void main(String[] args) {
        System.out.println("=== DEMO BÀI 3, 4, 5: QUẢN LÝ SINH VIÊN ===");
        List<Student> list = new ArrayList<>();

        // Tạo dữ liệu mẫu để demo nhanh nếu không muốn nhập tay toàn bộ
        list.add(new Student("Nguyen Van An", "Nam", "12/03/2003", "TP.HCM", 8.8, "an.nguyen@gmail.com"));
        list.add(new Student("Tran Thi Binh", "Nu", "25/08/2003", "Ha Noi", 7.2, "binh.tran@gmail.com"));
        list.add(new Student("Le Van Cuong", "Nam", "01/11/2002", "Da Nang", 9.1, "cuong.le@gmail.com"));
        list.add(new Student("Pham Thi Dung", "Nu", "18/05/2003", "Can Tho", 5.5, "dung.pham@gmail.com"));

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

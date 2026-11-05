package Lab2_ReviewJava;

import java.util.Scanner;

public class DemoArray {
    public static void main(String[] args) {
        System.out.println("=== DEMO BÀI 2: MẢNG SỐ NGUYÊN & RADIX SORT ===");
        Scanner sc = new Scanner(System.in);
        IntArray arr = new IntArray(20);

        // 1. Đề yêu cầu: "Khởi tạo và nhập giá trị cho mảng"
        System.out.println("1. Nhập mảng từ bàn phím:");
        arr.nhapMang(sc);
        arr.xuatMang();

        System.out.println("\n2. Thêm 999 vào đầu, 111 vào cuối, 555 vào vị trí thứ 3:");
        arr.themVaoDau(999);
        arr.themVaoCuoi(111);
        arr.themVaoViTri(3, 555);
        arr.xuatMang();

        System.out.println("\n3. Tìm kiếm phần tử 555 khi chưa sắp xếp:");
        int idx = arr.timKiemChuaSapXep(555);
        System.out.println("Vị trí tìm thấy: " + idx);

        System.out.println("\n4. Xóa phần tử giá trị 999 và xóa phần tử tại vị trí index 2:");
        arr.xoaPhanTuX(999);
        arr.xoaPhanTuThuJ(2);
        arr.xuatMang();

        System.out.println("\n5. Sắp xếp mảng tăng dần theo Radix Sort:");
        arr.radixSortTangDan();
        arr.xuatMang();

        System.out.println("\n6. Tìm kiếm nhị phân phần tử 802 trong mảng đã sắp xếp:");
        int binaryIdx = arr.timKiemDaSapXep(802);
        System.out.println("Vị trí tìm thấy: " + binaryIdx);

        System.out.println("\n7. Sắp xếp mảng giảm dần:");
        arr.radixSortGiamDan();
        arr.xuatMang();
    }
}

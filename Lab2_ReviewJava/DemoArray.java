package Lab2_ReviewJava;

public class DemoArray {
    public static void main(String[] args) {
        System.out.println("=== DEMO BÀI 2: MẢNG SỐ NGUYÊN & RADIX SORT ===");
        IntArray arr = new IntArray(20);

        // Khởi tạo các phần tử mẫu
        arr.themVaoCuoi(170);
        arr.themVaoCuoi(45);
        arr.themVaoCuoi(75);
        arr.themVaoCuoi(90);
        arr.themVaoCuoi(802);
        arr.themVaoCuoi(24);
        arr.themVaoCuoi(2);
        arr.themVaoCuoi(66);

        System.out.println("1. Mảng ban đầu:");
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

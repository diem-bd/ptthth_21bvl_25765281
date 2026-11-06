package Lab2_ReviewJava;

import java.util.Scanner;

public class IntArray {
    private int[] a;
    private int size;
    private int capacity;

    public IntArray(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Số phần tử tối đa phải lớn hơn 0");
        }
        this.capacity = capacity;
        this.a = new int[capacity];
        this.size = 0;
    }

    // Đề Bài 2 yêu cầu: "Khởi tạo và nhập giá trị cho mảng"
    public void nhapMang(Scanner sc) {
        System.out.print("Nhập số lượng phần tử ban đầu (<= " + capacity + "): ");
        int n = Integer.parseInt(sc.nextLine());
        if (n < 0) n = 0;
        if (n > capacity) n = capacity;
        this.size = n;
        for (int i = 0; i < n; i++) {
            System.out.printf("a[%d] = ", i);
            a[i] = Integer.parseInt(sc.nextLine());
        }
    }

    public void xuatMang() {
        System.out.print("Mảng (" + size + "/" + capacity + "): [ ");
        for (int i = 0; i < size; i++) {
            System.out.print(a[i] + " ");
        }
        System.out.println("]");
    }

    public boolean themVaoDau(int y) {
        return themVaoViTri(0, y);
    }

    public boolean themVaoCuoi(int y) {
        return themVaoViTri(size, y);
    }

    public boolean themVaoViTri(int i, int y) {
        if (size >= capacity) {
            System.out.println("Mảng đã đầy, không thể thêm!");
            return false;
        }
        if (i < 0 || i > size) {
            System.out.println("Vị trí không hợp lệ!");
            return false;
        }
        for (int k = size; k > i; k--) {
            a[k] = a[k - 1];
        }
        a[i] = y;
        size++;
        return true;
    }

    public boolean xoaPhanTuX(int x) {
        int index = timKiemChuaSapXep(x);
        if (index != -1) {
            return xoaPhanTuThuJ(index);
        }
        System.out.println("Không tìm thấy giá trị " + x + " để xóa.");
        return false;
    }

    public boolean xoaPhanTuThuJ(int j) {
        if (j < 0 || j >= size) {
            System.out.println("Chỉ số j không hợp lệ!");
            return false;
        }
        for (int k = j; k < size - 1; k++) {
            a[k] = a[k + 1];
        }
        size--;
        return true;
    }

    // Radix Sort (LSD) cho mảng số nguyên KHÔNG ÂM, độ dài n.
    // Dùng long cho trị tuyệt đối để xử lý được cả Integer.MIN_VALUE.
    private static void radixSortKhongAm(long[] arr, int n) {
        if (n <= 1) return;
        long max = arr[0];
        for (int i = 1; i < n; i++) {
            if (arr[i] > max) max = arr[i];
        }
        long[] output = new long[n];
        int[] count = new int[10];
        for (long exp = 1; max / exp > 0; exp *= 10) {
            java.util.Arrays.fill(count, 0);
            for (int i = 0; i < n; i++) {
                count[(int) (arr[i] / exp % 10)]++;
            }
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }
            for (int i = n - 1; i >= 0; i--) {
                int digit = (int) (arr[i] / exp % 10);
                output[--count[digit]] = arr[i];
            }
            System.arraycopy(output, 0, arr, 0, n);
        }
    }

    public void radixSortTangDan() {
        if (size <= 1) return;
        // Radix chỉ làm việc với chữ số 0..9, nên tách số âm ra và sắp theo trị tuyệt đối.
        long[] soAm = new long[size];
        long[] khongAm = new long[size];
        int soLuongAm = 0, soLuongKhongAm = 0;
        for (int i = 0; i < size; i++) {
            if (a[i] < 0) soAm[soLuongAm++] = -(long) a[i];
            else khongAm[soLuongKhongAm++] = a[i];
        }
        radixSortKhongAm(soAm, soLuongAm);
        radixSortKhongAm(khongAm, soLuongKhongAm);

        int k = 0;
        // Trị tuyệt đối giảm dần -> giá trị âm tăng dần (ví dụ -9, -2, -1).
        for (int i = soLuongAm - 1; i >= 0; i--) {
            a[k++] = (int) -soAm[i];
        }
        for (int i = 0; i < soLuongKhongAm; i++) {
            a[k++] = (int) khongAm[i];
        }
    }

    public void radixSortGiamDan() {
        radixSortTangDan();
        // Đảo ngược mảng
        for (int i = 0; i < size / 2; i++) {
            int tmp = a[i];
            a[i] = a[size - 1 - i];
            a[size - 1 - i] = tmp;
        }
    }

    public int timKiemChuaSapXep(int b) {
        for (int i = 0; i < size; i++) {
            if (a[i] == b) return i;
        }
        return -1;
    }

    public int timKiemDaSapXep(int b) {
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (a[mid] == b) return mid;
            if (a[mid] < b) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }
}

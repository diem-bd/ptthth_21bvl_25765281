package Lab2_ReviewJava;

import java.util.Arrays;
import java.util.Scanner;

public class IntArray {
    private int[] a;
    private int size;
    private int capacity;

    public IntArray(int capacity) {
        this.capacity = capacity;
        this.a = new int[capacity];
        this.size = 0;
    }

    public void nhapMang(Scanner sc) {
        System.out.print("Nhập số lượng phần tử ban đầu (<= " + capacity + "): ");
        int n = Integer.parseInt(sc.nextLine());
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

    // Radix Sort cho số nguyên (xử lý cả số không âm)
    private void countingSortByDigit(int exp) {
        int[] output = new int[size];
        int[] count = new int[10];

        for (int i = 0; i < size; i++) {
            int digit = (a[i] / exp) % 10;
            count[digit]++;
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = size - 1; i >= 0; i--) {
            int digit = (a[i] / exp) % 10;
            output[count[digit] - 1] = a[i];
            count[digit]--;
        }

        for (int i = 0; i < size; i++) {
            a[i] = output[i];
        }
    }

    public void radixSortTangDan() {
        if (size <= 1) return;
        int max = a[0];
        for (int i = 1; i < size; i++) {
            if (a[i] > max) max = a[i];
        }
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortByDigit(exp);
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

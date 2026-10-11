package Lab1_JavaBasic;

import java.util.Scanner;

public class BaiTapCoBan {
    public static void helloWorld() {
        System.out.println("Hello, World!");
    }

    public static void chaoTheoTen(Scanner scanner) {
        System.out.println("What's your name?");
        String str = scanner.nextLine();
        System.out.println("Hi, I am " + str);
    }

    public static void tinhTong(Scanner scanner) {
        System.out.print("Vui lòng nhập số hạng thứ nhất: ");
        int soA = Integer.parseInt(scanner.nextLine());
        System.out.print("Vui lòng nhập số hạng thứ hai: ");
        int soB = Integer.parseInt(scanner.nextLine());
        int kq = soA + soB;
        System.out.println("Tính tổng [" + soA + " + " + soB + " = " + kq);
    }

    public static void kiemTraChanLe(Scanner scanner) {
        System.out.println(">> Kiểm tra số chẵn lẻ <<");
        System.out.print("Vui lòng nhập số cần kiểm tra: ");
        int so = Integer.parseInt(scanner.nextLine());
        if (so % 2 == 0) {
            System.out.println("Số " + so + " là số chẵn.");
        } else {
            System.out.println("Số " + so + " là số lẻ.");
        }
    }

    public static void inThangTiengAnh(Scanner scanner) {
        boolean isrun = true;
        while (isrun) {
            System.out.print("Vui lòng nhập tháng (1-12, số khác để dừng): ");
            int so = Integer.parseInt(scanner.nextLine());
            switch (so) {
                case 1:  System.out.println("January"); break;
                case 2:  System.out.println("February"); break;
                case 3:  System.out.println("March"); break;
                case 4:  System.out.println("April"); break;
                case 5:  System.out.println("May"); break;
                case 6:  System.out.println("June"); break;
                case 7:  System.out.println("July"); break;
                case 8:  System.out.println("August"); break;
                case 9:  System.out.println("September"); break;
                case 10: System.out.println("October"); break;
                case 11: System.out.println("November"); break;
                case 12: System.out.println("December"); break;
                default:
                    isrun = false;
                    System.out.println("STOP");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== 1. Hello World ===");
        helloWorld();

        System.out.println("\n=== 2. Nhập tên ===");
        chaoTheoTen(scanner);

        System.out.println("\n=== 3. Tính tổng ===");
        tinhTong(scanner);

        System.out.println("\n=== 4. Kiểm tra chẵn lẻ ===");
        kiemTraChanLe(scanner);

        System.out.println("\n=== 5. In tháng tiếng Anh ===");
        inThangTiengAnh(scanner);

        scanner.close();
    }
}

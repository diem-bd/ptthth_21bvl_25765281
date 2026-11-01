package bai6;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Locale;

public class BankClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1106);
        BankAccount account = (BankAccount) registry.lookup("Bank");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        System.out.printf(Locale.US, "Số dư ban đầu: %,.0f VND%n", account.getBalance());
        while (true) {
            System.out.println("\n1-Kiểm tra số dư  2-Gửi tiền  3-Rút tiền  0-Thoát");
            System.out.print("Chọn: ");
            String choice = in.readLine();
            if (choice == null || choice.trim().equals("0")) break;
            try {
                switch (choice.trim()) {
                    case "1":
                        System.out.printf(Locale.US, "Số dư: %,.0f VND%n", account.getBalance());
                        break;
                    case "2":
                        System.out.print("Số tiền gửi: ");
                        account.deposit(Double.parseDouble(in.readLine().trim()));
                        System.out.printf(Locale.US, "Nạp thành công. Số dư: %,.0f VND%n", account.getBalance());
                        break;
                    case "3":
                        System.out.print("Số tiền rút: ");
                        boolean ok = account.withdraw(Double.parseDouble(in.readLine().trim()));
                        System.out.println(ok ? "Rút thành công." : "Rút thất bại: không đủ số dư.");
                        System.out.printf(Locale.US, "Số dư: %,.0f VND%n", account.getBalance());
                        break;
                    default:
                        System.out.println("Chọn 0 đến 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số.");
            } catch (RemoteException e) {
                System.out.println("Server từ chối: " + e.getMessage());
            }
        }
    }
}

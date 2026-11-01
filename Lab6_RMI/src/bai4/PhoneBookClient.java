package bai4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PhoneBookClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1106);
        PhoneBook book = (PhoneBook) registry.lookup("PhoneBook");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        while (true) {
            System.out.println("\n1-Thêm  2-Tìm  3-Xóa  4-Liệt kê  0-Thoát");
            System.out.print("Chọn: ");
            String choice = in.readLine();
            if (choice == null || choice.trim().equals("0")) break;
            try {
                switch (choice.trim()) {
                    case "1":
                        System.out.print("Tên: ");
                        String name = in.readLine();
                        System.out.print("Số điện thoại: ");
                        String phone = in.readLine();
                        book.addContact(name, phone);
                        System.out.println("Đã thêm liên hệ.");
                        break;
                    case "2":
                        System.out.print("Tên cần tìm: ");
                        System.out.println(book.findContact(in.readLine()));
                        break;
                    case "3":
                        System.out.print("Tên cần xóa: ");
                        System.out.println(book.deleteContact(in.readLine()) ? "Đã xóa." : "Không có để xóa.");
                        break;
                    case "4":
                        System.out.print(book.listContacts());
                        break;
                    default:
                        System.out.println("Chọn 0 đến 4.");
                }
            } catch (RemoteException e) {
                System.out.println("Server từ chối: " + e.getMessage());
            }
        }
    }
}

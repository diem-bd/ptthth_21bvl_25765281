package bai7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class GeometryClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1106);
        Geometry geometry = (Geometry) registry.lookup("Geometry");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        while (true) {
            System.out.println("\n1-Chữ nhật  2-Hình tròn  3-Tam giác  0-Thoát");
            System.out.print("Chọn: ");
            String choice = in.readLine();
            if (choice == null || choice.trim().equals("0")) break;
            try {
                switch (choice.trim()) {
                    case "1":
                        System.out.printf("Diện tích = %.2f%n",
                                geometry.rectangleArea(read(in, "Chiều dài: "), read(in, "Chiều rộng: ")));
                        break;
                    case "2":
                        System.out.printf("Diện tích = %.2f%n", geometry.circleArea(read(in, "Bán kính: ")));
                        break;
                    case "3":
                        System.out.printf("Diện tích = %.2f%n",
                                geometry.triangleArea(read(in, "Cạnh đáy: "), read(in, "Chiều cao: ")));
                        break;
                    default:
                        System.out.println("Chọn 0 đến 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số.");
            } catch (RemoteException e) {
                // RMI bọc lỗi gốc trong RemoteException nên thông báo có thể nhiều dòng;
                // lấy dòng cuối và bỏ tiền tố lớp ngoại lệ để hiện đúng nội dung lỗi.
                String message = e.getMessage() == null ? "lỗi không xác định" : e.getMessage().trim();
                message = message.substring(message.lastIndexOf('\n') + 1).trim();
                if (message.startsWith("java.rmi.RemoteException:")) {
                    message = message.substring("java.rmi.RemoteException:".length()).trim();
                }
                System.out.println("Server từ chối: " + message);
            }
        }
    }

    private static double read(BufferedReader in, String prompt) throws Exception {
        System.out.print(prompt);
        return Double.parseDouble(in.readLine().trim());
    }
}

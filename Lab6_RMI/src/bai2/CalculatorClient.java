package bai2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1106);
        Calculator calculator = (Calculator) registry.lookup("Calculator");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        System.out.println("[Client] Nhập hai số nguyên, nhập q để thoát.");
        while (true) {
            Integer a = readInt(in, "a = ");
            if (a == null) break;
            Integer b = readInt(in, "b = ");
            if (b == null) break;
            try {
                System.out.printf("Kết quả từ server: %d + %d = %d%n", a, b, calculator.add(a, b));
            } catch (Exception e) {
                System.out.println("Lỗi từ server: " + e.getMessage());
            }
        }
    }

    private static Integer readInt(BufferedReader in, String prompt) throws Exception {
        while (true) {
            System.out.print(prompt);
            String line = in.readLine();
            if (line == null || line.trim().equalsIgnoreCase("q")) return null;
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số nguyên.");
            }
        }
    }
}

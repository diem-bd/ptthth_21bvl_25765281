package bai3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PrimeClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1106);
        PrimeService service = (PrimeService) registry.lookup("Prime");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        System.out.println("[Client] Nhập số cần kiểm tra, nhập q để thoát.");
        while (true) {
            System.out.print("n = ");
            String line = in.readLine();
            if (line == null || line.trim().equalsIgnoreCase("q")) break;
            try {
                int n = Integer.parseInt(line.trim());
                System.out.printf("%d %s số nguyên tố%n", n, service.isPrime(n) ? "LÀ" : "KHÔNG phải");
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số nguyên.");
            }
        }
    }
}

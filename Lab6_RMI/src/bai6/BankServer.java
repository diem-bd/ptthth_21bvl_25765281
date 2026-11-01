package bai6;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BankServer {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.createRegistry(1106);
        registry.rebind("Bank", new BankAccountImpl("Bui Duong Diem", 1_000_000));
        System.out.println("[Server] Bài 6: tài khoản ban đầu 1,000,000 VND");
    }
}

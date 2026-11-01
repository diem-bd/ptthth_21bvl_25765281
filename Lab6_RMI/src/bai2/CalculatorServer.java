package bai2;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorServer {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.createRegistry(1106);
        registry.rebind("Calculator", new CalculatorImpl());
        System.out.println("[Server] Bài 2: Calculator sẵn sàng tại cổng 1106");
    }
}

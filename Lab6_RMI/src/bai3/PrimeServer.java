package bai3;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PrimeServer {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.createRegistry(1106);
        registry.rebind("Prime", new PrimeServiceImpl());
        System.out.println("[Server] Bài 3: PrimeService sẵn sàng tại cổng 1106");
    }
}

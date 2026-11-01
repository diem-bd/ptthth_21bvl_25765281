package bai8;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FlightServer {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.createRegistry(1106);
        registry.rebind("Flight", new FlightServiceImpl());
        System.out.println("[Server] Bài 8: Flight sẵn sàng tại cổng 1106");
    }
}

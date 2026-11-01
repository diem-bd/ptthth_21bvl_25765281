package bai7;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class GeometryServer {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.createRegistry(1106);
        registry.rebind("Geometry", new GeometryImpl());
        System.out.println("[Server] Bài 7: Geometry sẵn sàng tại cổng 1106");
    }
}

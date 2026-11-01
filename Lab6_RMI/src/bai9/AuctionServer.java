package bai9;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AuctionServer {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.createRegistry(1106);
        registry.rebind("Auction", new AuctionServiceImpl());
        System.out.println("[Server] Bài 9: Auction sẵn sàng tại cổng 1106");
    }
}

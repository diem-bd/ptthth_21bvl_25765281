package bai4;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PhoneBookServer {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.createRegistry(1106);
        registry.rebind("PhoneBook", new PhoneBookImpl());
        System.out.println("[Server] Bài 4: PhoneBook sẵn sàng tại cổng 1106");
    }
}

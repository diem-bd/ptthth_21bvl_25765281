package bai5;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatServer {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.createRegistry(1106);
        registry.rebind("Chat", new ChatServiceImpl());
        System.out.println("[Server] Bài 5: Chat RMI sẵn sàng tại cổng 1106");
    }
}

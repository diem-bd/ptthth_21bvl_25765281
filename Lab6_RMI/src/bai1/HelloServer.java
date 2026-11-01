package bai1;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HelloServer {
    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 1106;
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind("Hello", new HelloImpl());
        System.out.println("[Server] Registry cổng " + port + ", đã đăng ký tên Hello");
    }
}

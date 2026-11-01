package bai1;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HelloClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1106);
        Hello hello = (Hello) registry.lookup("Hello");
        System.out.println("[Client] Gọi sayHello() từ xa...");
        System.out.println("Kết quả: " + hello.sayHello());
    }
}

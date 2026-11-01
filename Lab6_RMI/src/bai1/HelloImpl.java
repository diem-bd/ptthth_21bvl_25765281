package bai1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/** Lớp cài đặt chạy trong JVM của server, kế thừa UnicastRemoteObject để được xuất ra cổng RMI. */
public class HelloImpl extends UnicastRemoteObject implements Hello {
    public HelloImpl() throws RemoteException {
        super();
    }

    @Override
    public String sayHello() {
        return "Hello, World!";
    }
}

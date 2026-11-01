package bai1;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** Giao diện từ xa: client gọi sayHello() trên đối tượng nằm ở server. */
public interface Hello extends Remote {
    String sayHello() throws RemoteException;
}

package bai5;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** Giao diện callback: server gọi ngược về client khi có tin nhắn mới. */
public interface ChatCallback extends Remote {
    void receiveMessage(String message) throws RemoteException;
}

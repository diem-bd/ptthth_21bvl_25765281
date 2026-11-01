package bai5;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatService extends Remote {
    /** Đăng ký client để nhận tin nhắn (RMI callback hai chiều). */
    void register(String nickname, ChatCallback callback) throws RemoteException;

    /** Gửi tin nhắn tới server; server phản hồi lại người gửi. */
    String sendMessage(String nickname, String message) throws RemoteException;

    String getHistory() throws RemoteException;
}

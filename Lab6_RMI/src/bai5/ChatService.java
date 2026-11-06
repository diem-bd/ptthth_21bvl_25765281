package bai5;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Bài 5: chat 1-1 giữa client và server qua RMI.
 * Đề yêu cầu đúng một phương thức void sendMessage(String message).
 */
public interface ChatService extends Remote {
    /** Client gửi tin nhắn tới server. */
    void sendMessage(String message) throws RemoteException;

    /** Client lấy phản hồi mới nhất của server để hiển thị. */
    String getLastReply() throws RemoteException;
}

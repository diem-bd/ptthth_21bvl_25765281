package bai5;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatServiceImpl extends UnicastRemoteObject implements ChatService {
    private String lastReply = "(chưa có tin nhắn)";

    public ChatServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized void sendMessage(String message) throws RemoteException {
        if (message == null || message.isBlank()) {
            throw new RemoteException("Tin nhắn rỗng");
        }
        System.out.println("[Server] Nhận: " + message);
        // Chat 1-1: server phản hồi lại chính client đang gọi.
        lastReply = "Server đã nhận: \"" + message + "\" (" + message.length() + " ký tự)";
        System.out.println("[Server] Phản hồi: " + lastReply);
    }

    @Override
    public synchronized String getLastReply() {
        return lastReply;
    }
}

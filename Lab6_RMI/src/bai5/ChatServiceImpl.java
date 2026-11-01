package bai5;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServiceImpl extends UnicastRemoteObject implements ChatService {
    private final Map<String, ChatCallback> online = new ConcurrentHashMap<>();
    private final List<String> history = new ArrayList<>();

    public ChatServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized void register(String nickname, ChatCallback callback) throws RemoteException {
        online.put(nickname, callback);
        System.out.println("[Server] " + nickname + " đã tham gia, online = " + online.keySet());
    }

    @Override
    public synchronized String sendMessage(String nickname, String message) throws RemoteException {
        if (message == null || message.isBlank()) {
            throw new RemoteException("Tin nhắn rỗng");
        }
        String line = nickname + ": " + message;
        history.add(line);
        System.out.println("[Server] Nhận -> " + line);
        // Phát cho mọi client đang online, trừ người gửi.
        int delivered = 0;
        for (Map.Entry<String, ChatCallback> entry : online.entrySet()) {
            if (entry.getKey().equals(nickname)) continue;
            try {
                entry.getValue().receiveMessage(line);
                delivered++;
            } catch (RemoteException e) {
                online.remove(entry.getKey());
                System.out.println("[Server] Client " + entry.getKey() + " mất kết nối");
            }
        }
        return "Server phản hồi: đã nhận \"" + message + "\" (" + message.length()
                + " ký tự), phát tiếp tới " + delivered + " client khác";
    }

    @Override
    public synchronized String getHistory() {
        return history.isEmpty() ? "(Chưa có tin nhắn)" : String.join("\n", history);
    }
}

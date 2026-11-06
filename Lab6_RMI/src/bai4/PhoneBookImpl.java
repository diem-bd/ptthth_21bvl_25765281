package bai4;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class PhoneBookImpl extends UnicastRemoteObject implements PhoneBook {
    // Đề yêu cầu lưu danh bạ bằng HashMap<String, String>.
    // synchronized vì RMI có thể phục vụ nhiều lời gọi đồng thời.
    private final Map<String, String> contacts = new HashMap<>();

    public PhoneBookImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized void addContact(String name, String phone) throws RemoteException {
        if (name == null || name.isBlank()) {
            throw new RemoteException("Tên không được rỗng");
        }
        if (phone == null || phone.isBlank()) {
            throw new RemoteException("Số điện thoại không được rỗng");
        }
        boolean existed = contacts.containsKey(name.trim());
        contacts.put(name.trim(), phone);
        System.out.println("[Server] " + (existed ? "Cập nhật" : "Thêm") + ": " + name + " -> " + phone);
    }

    @Override
    public synchronized String findContact(String name) {
        String phone = contacts.get(name == null ? "" : name.trim());
        return phone == null ? "Không tìm thấy liên hệ: " + name : name + " -> " + phone;
    }

    @Override
    public synchronized boolean deleteContact(String name) {
        boolean removed = contacts.remove(name == null ? "" : name.trim()) != null;
        System.out.println("[Server] Xóa " + name + ": " + removed);
        return removed;
    }

    @Override
    public synchronized String listContacts() {
        if (contacts.isEmpty()) return "(Danh bạ rỗng)";
        StringBuilder builder = new StringBuilder();
        contacts.forEach((name, phone) -> builder.append("  ").append(name).append(" -> ").append(phone).append('\n'));
        return builder.toString();
    }
}

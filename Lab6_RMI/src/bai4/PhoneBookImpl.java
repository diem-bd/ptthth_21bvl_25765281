package bai4;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.TreeMap;

public class PhoneBookImpl extends UnicastRemoteObject implements PhoneBook {
    // TreeMap để danh sách liệt kê theo thứ tự tên; synchronized vì RMI phục vụ nhiều luồng.
    private final Map<String, String> contacts = new TreeMap<>();

    public PhoneBookImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized void addContact(String name, String phone) throws RemoteException {
        if (name == null || name.isBlank()) {
            throw new RemoteException("Tên không được rỗng");
        }
        if (phone == null || !phone.matches("\\d{9,11}")) {
            throw new RemoteException("Số điện thoại phải gồm 9-11 chữ số");
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

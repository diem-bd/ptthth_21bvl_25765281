package bai4;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PhoneBook extends Remote {
    void addContact(String name, String phone) throws RemoteException;

    String findContact(String name) throws RemoteException;

    boolean deleteContact(String name) throws RemoteException;

    /** Phần mở rộng để xem toàn bộ danh bạ khi demo. */
    String listContacts() throws RemoteException;
}

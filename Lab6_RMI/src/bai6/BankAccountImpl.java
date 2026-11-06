package bai6;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BankAccountImpl extends UnicastRemoteObject implements BankAccount {
    private final String owner;
    private double balance;

    public BankAccountImpl(String owner, double initialBalance) throws RemoteException {
        super();
        this.owner = owner;
        this.balance = initialBalance;
    }

    @Override
    public synchronized double getBalance() {
        return balance;
    }

    @Override
    public synchronized void deposit(double amount) throws RemoteException {
        // "NaN <= 0" là false, nên chỉ kiểm tra <= 0 sẽ để NaN lọt vào số dư.
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new RemoteException("Số tiền nạp phải là số hữu hạn lớn hơn 0");
        }
        balance += amount;
        System.out.printf("[Server] Nạp %,.0f -> số dư %,.0f%n", amount, balance);
    }

    @Override
    public synchronized boolean withdraw(double amount) throws RemoteException {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new RemoteException("Số tiền rút phải là số hữu hạn lớn hơn 0");
        }
        if (amount > balance) {
            System.out.printf("[Server] Từ chối rút %,.0f vì số dư %,.0f%n", amount, balance);
            return false;
        }
        balance -= amount;
        System.out.printf("[Server] Rút %,.0f -> số dư %,.0f%n", amount, balance);
        return true;
    }

    @Override
    public String toString() {
        return owner;
    }
}

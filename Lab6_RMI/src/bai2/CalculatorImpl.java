package bai2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CalculatorImpl extends UnicastRemoteObject implements Calculator {
    public CalculatorImpl() throws RemoteException {
        super();
    }

    @Override
    public int add(int a, int b) throws RemoteException {
        // Kiểm tra tràn số nguyên trước khi cộng.
        long sum = (long) a + b;
        if (sum > Integer.MAX_VALUE || sum < Integer.MIN_VALUE) {
            throw new RemoteException("Tổng vượt giới hạn int: " + sum);
        }
        return (int) sum;
    }
}

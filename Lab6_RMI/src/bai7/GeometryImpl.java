package bai7;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GeometryImpl extends UnicastRemoteObject implements Geometry {
    public GeometryImpl() throws RemoteException {
        super();
    }

    private static void check(double value, String name) throws RemoteException {
        if (!Double.isFinite(value) || value <= 0) {
            throw new RemoteException(name + " phải là số hữu hạn lớn hơn 0");
        }
    }

    @Override
    public double rectangleArea(double width, double height) throws RemoteException {
        check(width, "Chiều dài");
        check(height, "Chiều rộng");
        return width * height;
    }

    @Override
    public double circleArea(double radius) throws RemoteException {
        check(radius, "Bán kính");
        return Math.PI * radius * radius;
    }

    @Override
    public double triangleArea(double base, double height) throws RemoteException {
        check(base, "Cạnh đáy");
        check(height, "Chiều cao");
        return base * height / 2.0;
    }
}

package bai8;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Map;

public class FlightServiceImpl extends UnicastRemoteObject implements FlightService {
    private final Map<String, Integer> seats = new LinkedHashMap<>();

    public FlightServiceImpl() throws RemoteException {
        super();
        seats.put("VN123", 3);
        seats.put("VN456", 5);
        seats.put("VJ789", 2);
    }

    private int require(String flight) throws RemoteException {
        Integer current = seats.get(flight);
        if (current == null) throw new RemoteException("Không có chuyến bay " + flight);
        return current;
    }

    @Override
    public synchronized boolean bookTicket(String flightNumber, int count) throws RemoteException {
        if (count <= 0) throw new RemoteException("Số ghế phải lớn hơn 0");
        int current = require(flightNumber);
        if (count > current) {
            System.out.println("[Server] " + flightNumber + ": không đủ ghế (" + current + " < " + count + ")");
            return false;
        }
        seats.put(flightNumber, current - count);
        System.out.println("[Server] " + flightNumber + ": đặt " + count + ", còn " + (current - count));
        return true;
    }

    @Override
    public synchronized int availableSeats(String flightNumber) throws RemoteException {
        return require(flightNumber);
    }

    @Override
    public synchronized boolean cancelBooking(String flightNumber, int count) throws RemoteException {
        if (count <= 0) throw new RemoteException("Số ghế phải lớn hơn 0");
        int current = require(flightNumber);
        seats.put(flightNumber, current + count);
        System.out.println("[Server] " + flightNumber + ": hủy " + count + ", còn " + (current + count));
        return true;
    }

    @Override
    public synchronized String listFlights() {
        StringBuilder builder = new StringBuilder();
        seats.forEach((flight, count) -> builder.append("  ").append(flight).append(": ").append(count).append(" ghế\n"));
        return builder.toString();
    }
}

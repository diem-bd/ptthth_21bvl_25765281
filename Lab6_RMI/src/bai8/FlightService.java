package bai8;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FlightService extends Remote {
    boolean bookTicket(String flightNumber, int seats) throws RemoteException;

    int availableSeats(String flightNumber) throws RemoteException;

    boolean cancelBooking(String flightNumber, int seats) throws RemoteException;

    /** Phần mở rộng để xem danh sách chuyến bay. */
    String listFlights() throws RemoteException;
}

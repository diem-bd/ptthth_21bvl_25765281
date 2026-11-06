package bai8;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Map;

public class FlightServiceImpl extends UnicastRemoteObject implements FlightService {
    /** Sức chứa ban đầu của từng chuyến (không đổi). */
    private final Map<String, Integer> sucChua = new LinkedHashMap<>();
    /** Số ghế còn trống hiện tại. */
    private final Map<String, Integer> conTrong = new LinkedHashMap<>();
    /** Số ghế khách đã đặt và chưa hủy. */
    private final Map<String, Integer> daDat = new LinkedHashMap<>();

    public FlightServiceImpl() throws RemoteException {
        super();
        themChuyen("VN123", 3);
        themChuyen("VN456", 5);
        themChuyen("VJ789", 2);
    }

    private void themChuyen(String maChuyen, int soGhe) {
        sucChua.put(maChuyen, soGhe);
        conTrong.put(maChuyen, soGhe);
        daDat.put(maChuyen, 0);
    }

    private void require(String flight) throws RemoteException {
        if (!conTrong.containsKey(flight)) {
            throw new RemoteException("Không có chuyến bay " + flight);
        }
    }

    @Override
    public synchronized boolean bookTicket(String flightNumber, int count) throws RemoteException {
        if (count <= 0) throw new RemoteException("Số ghế phải lớn hơn 0");
        require(flightNumber);
        int current = conTrong.get(flightNumber);
        if (count > current) {
            System.out.println("[Server] " + flightNumber + ": không đủ ghế (" + current + " < " + count + ")");
            return false;
        }
        conTrong.put(flightNumber, current - count);
        daDat.put(flightNumber, daDat.get(flightNumber) + count);
        System.out.println("[Server] " + flightNumber + ": đặt " + count + ", còn " + (current - count));
        return true;
    }

    @Override
    public synchronized int availableSeats(String flightNumber) throws RemoteException {
        require(flightNumber);
        return conTrong.get(flightNumber);
    }

    @Override
    public synchronized boolean cancelBooking(String flightNumber, int count) throws RemoteException {
        if (count <= 0) throw new RemoteException("Số ghế phải lớn hơn 0");
        require(flightNumber);
        // Chỉ được hủy trong phạm vi số ghế đã đặt thật:
        // nếu không, hủy vé sẽ làm số ghế trống vượt sức chứa ban đầu.
        if (count > daDat.get(flightNumber)) {
            System.out.println("[Server] " + flightNumber + ": hủy " + count
                    + " vượt số đã đặt (" + daDat.get(flightNumber) + ")");
            return false;
        }
        conTrong.put(flightNumber, conTrong.get(flightNumber) + count);
        daDat.put(flightNumber, daDat.get(flightNumber) - count);
        System.out.println("[Server] " + flightNumber + ": hủy " + count + ", còn " + conTrong.get(flightNumber));
        return true;
    }

    @Override
    public synchronized String listFlights() {
        StringBuilder builder = new StringBuilder();
        for (String maChuyen : sucChua.keySet()) {
            builder.append("  ").append(maChuyen)
                    .append(": còn ").append(conTrong.get(maChuyen))
                    .append("/").append(sucChua.get(maChuyen)).append(" ghế\n");
        }
        return builder.toString();
    }
}

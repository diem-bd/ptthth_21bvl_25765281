package bai8;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FlightClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1106);
        FlightService flight = (FlightService) registry.lookup("Flight");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        while (true) {
            System.out.println("\n1-Đặt vé  2-Hủy vé  3-Kiểm tra ghế  4-Danh sách chuyến  0-Thoát");
            System.out.print("Chọn: ");
            String choice = in.readLine();
            if (choice == null || choice.trim().equals("0")) break;
            try {
                switch (choice.trim()) {
                    case "1":
                        System.out.print("Mã chuyến: ");
                        String bookFlight = in.readLine().trim().toUpperCase();
                        System.out.print("Số ghế: ");
                        int count = Integer.parseInt(in.readLine().trim());
                        System.out.println(flight.bookTicket(bookFlight, count)
                                ? "Đặt vé thành công, còn " + flight.availableSeats(bookFlight) + " ghế"
                                : "Đặt vé thất bại: không đủ ghế");
                        break;
                    case "2":
                        System.out.print("Mã chuyến: ");
                        String cancelFlight = in.readLine().trim().toUpperCase();
                        System.out.print("Số ghế hủy: ");
                        int cancel = Integer.parseInt(in.readLine().trim());
                        flight.cancelBooking(cancelFlight, cancel);
                        System.out.println("Đã hủy, còn " + flight.availableSeats(cancelFlight) + " ghế");
                        break;
                    case "3":
                        System.out.print("Mã chuyến: ");
                        String check = in.readLine().trim().toUpperCase();
                        System.out.println("Còn " + flight.availableSeats(check) + " ghế");
                        break;
                    case "4":
                        System.out.print(flight.listFlights());
                        break;
                    default:
                        System.out.println("Chọn 0 đến 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số.");
            } catch (RemoteException e) {
                System.out.println("Server từ chối: " + e.getMessage());
            }
        }
    }
}

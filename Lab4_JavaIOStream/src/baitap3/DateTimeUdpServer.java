package baitap3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUdpServer {
    private static final int PORT = 5004;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : PORT;
        byte[] buffer = new byte[4096];

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP DateTime Server đang lắng nghe trên cổng " + port);
            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                String cmd = new String(request.getData(), request.getOffset(), request.getLength(),
                        StandardCharsets.UTF_8).trim().toUpperCase();

                System.out.printf("Nhận lệnh '%s' từ %s:%d%n", cmd,
                        request.getAddress().getHostAddress(), request.getPort());

                String responseText = processCommand(cmd);
                byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);

                DatagramPacket response = new DatagramPacket(
                        responseBytes, responseBytes.length,
                        request.getAddress(), request.getPort()
                );
                socket.send(response);
            }
        } catch (IOException e) {
            System.err.println("Lỗi UDP Server: " + e.getMessage());
        }
    }

    public static String processCommand(String cmd) {
        LocalDateTime now = LocalDateTime.now();
        switch (cmd) {
            case "DATE":
                return "OK " + now.format(DATE_FMT);
            case "TIME":
                return "OK " + now.format(TIME_FMT);
            case "DATETIME":
                return "OK " + now.format(DATETIME_FMT);
            default:
                return "ERR UNKNOWN_COMMAND";
        }
    }
}

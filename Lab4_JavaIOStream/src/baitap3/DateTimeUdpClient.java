package baitap3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class DateTimeUdpClient {
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5004;

        InetAddress serverAddr = InetAddress.getByName(host);
        BufferedReader console = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));

        System.out.println("Kết nối tới UDP DateTime Server (" + host + ":" + port + ")");
        System.out.println("Nhập các lệnh: DATE, TIME, DATETIME (hoặc exit để thoát):");

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(3000); // 3 giây timeout nếu server không phản hồi
            byte[] buffer = new byte[4096];

            String line;
            while ((line = console.readLine()) != null) {
                if (line.trim().equalsIgnoreCase("exit")) break;

                byte[] sendData = line.getBytes(StandardCharsets.UTF_8);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddr, port);
                socket.send(sendPacket);

                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(receivePacket);
                    String response = new String(receivePacket.getData(), receivePacket.getOffset(),
                            receivePacket.getLength(), StandardCharsets.UTF_8);
                    System.out.println("UDP Server phản hồi: " + response);
                } catch (SocketTimeoutException e) {
                    System.err.println("⚠️ Hết 3 giây nhưng chưa nhận được phản hồi (Có thể Server đã dừng hoặc mạng bị ngắt)!");
                }
            }
        }
    }
}

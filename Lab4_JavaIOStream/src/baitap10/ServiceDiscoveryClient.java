package baitap10;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class ServiceDiscoveryClient {
    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("  BÀI TẬP 10: UDP SERVICE DISCOVERY & TCP SERVICE CLIENT  ");
        System.out.println("==========================================================");

        String discoveredIp = null;
        int discoveredTcpPort = -1;
        String discoveredName = null;

        // BƯỚC 1: Khám phá dịch vụ qua UDP
        System.out.println("[BƯỚC 1] Gửi gói tin UDP DISCOVER_SERVICE tới cổng " + ServiceDiscoveryServer.UDP_DISCOVERY_PORT + "...");
        try (DatagramSocket udpSocket = new DatagramSocket()) {
            udpSocket.setSoTimeout(3000); // Timeout 3 giây

            byte[] requestData = "DISCOVER_SERVICE".getBytes(StandardCharsets.UTF_8);
            InetAddress broadcastAddress = InetAddress.getByName("localhost");
            DatagramPacket requestPacket = new DatagramPacket(
                    requestData, requestData.length,
                    broadcastAddress, ServiceDiscoveryServer.UDP_DISCOVERY_PORT);

            udpSocket.send(requestPacket);

            byte[] responseBuffer = new byte[1024];
            Set<String> discoveredServers = new HashSet<>();

            try {
                while (true) {
                    DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
                    udpSocket.receive(responsePacket);

                    String senderIp = responsePacket.getAddress().getHostAddress();
                    String responseStr = new String(responsePacket.getData(), responsePacket.getOffset(), responsePacket.getLength(), StandardCharsets.UTF_8).trim();

                    String key = senderIp + ":" + responsePacket.getPort();
                    if (!discoveredServers.contains(key)) {
                        discoveredServers.add(key);
                        System.out.println("  -> Tìm thấy dịch vụ từ [" + senderIp + "]: " + responseStr);

                        // Parse format: SERVICE <name> <tcp_port> <version>
                        String[] parts = responseStr.split("\\s+");
                        if (parts.length >= 4 && parts[0].equals("SERVICE")) {
                            discoveredName = parts[1];
                            discoveredTcpPort = Integer.parseInt(parts[2]);
                            discoveredIp = senderIp;
                            break; // Lấy dịch vụ hợp lệ đầu tiên
                        }
                    }
                }
            } catch (SocketTimeoutException e) {
                if (discoveredServers.isEmpty()) {
                    System.err.println("  [Timeout] Không tìm thấy dịch vụ nào sau 3 giây.");
                    return;
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khám phá UDP: " + e.getMessage());
            return;
        }

        if (discoveredIp == null || discoveredTcpPort == -1) {
            System.err.println("Không phân giải được địa chỉ TCP Service từ phản hồi UDP.");
            return;
        }

        // BƯỚC 2: Tự động kết nối tới TCP Service vừa khám phá
        System.out.println("\n[BƯỚC 2] Tự động kết nối tới TCP Service '" + discoveredName + "' tại " + discoveredIp + ":" + discoveredTcpPort);
        try (Socket tcpSocket = new Socket(discoveredIp, discoveredTcpPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(tcpSocket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            System.out.println("  -> Đã kết nối TCP thành công!");

            // 1. Kiểm tra INFO
            out.println("INFO");
            System.out.println("Client gửi: INFO");
            System.out.println("Server trả lời: " + in.readLine());

            // 2. Thử nghiệm ECHO tiếng Việt
            out.println("ECHO Chào mừng đến với hệ thống khám phá dịch vụ Socket Java!");
            System.out.println("Client gửi: ECHO Chào mừng đến với hệ thống khám phá dịch vụ Socket Java!");
            System.out.println("Server trả lời: " + in.readLine());

            // 3. Đóng phiên
            out.println("QUIT");
            System.out.println("Client gửi: QUIT");
            System.out.println("Server trả lời: " + in.readLine());

            System.out.println("==========================================================");
            System.out.println("Hoàn tất quy trình khám phá UDP và sử dụng TCP Service 100%!");
        } catch (Exception e) {
            System.err.println("Lỗi gọi TCP Service: " + e.getMessage());
        }
    }
}

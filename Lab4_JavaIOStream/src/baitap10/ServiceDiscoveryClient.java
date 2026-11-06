package baitap10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServiceDiscoveryClient {
    private static final int DISCOVERY_TIMEOUT_MS = 3000;

    /** Gửi yêu cầu discovery tới broadcast của mạng cục bộ và tới loopback. */
    private static void guiDenCacDich(DatagramSocket udpSocket, byte[] requestData) {
        int cong = ServiceDiscoveryServer.UDP_DISCOVERY_PORT;
        // 255.255.255.255: broadcast giới hạn, đến mọi máy trong mạng cục bộ.
        for (String diaChi : new String[] {"255.255.255.255", "127.0.0.1"}) {
            try {
                InetAddress dich = InetAddress.getByName(diaChi);
                udpSocket.send(new DatagramPacket(requestData, requestData.length, dich, cong));
            } catch (IOException e) {
                // Một số máy/VPN chặn broadcast; vẫn tiếp tục với đích còn lại.
                System.err.println("  (không gửi được tới " + diaChi + ": " + e.getMessage() + ")");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("  BÀI TẬP 10: UDP SERVICE DISCOVERY & TCP SERVICE CLIENT");
        System.out.println("==========================================================");

        // BƯỚC 1: broadcast/multicast DISCOVER_SERVICE để tìm dịch vụ.
        // Map dùng để loại phản hồi trùng (cùng IP:port) và giữ được NHIỀU server.
        Map<String, String[]> servers = new LinkedHashMap<>();

        System.out.println("[BƯỚC 1] Broadcast DISCOVER_SERVICE tới cổng UDP "
                + ServiceDiscoveryServer.UDP_DISCOVERY_PORT + " ...");
        try (DatagramSocket udpSocket = new DatagramSocket()) {
            udpSocket.setBroadcast(true);
            udpSocket.setSoTimeout(DISCOVERY_TIMEOUT_MS);

            byte[] requestData = "DISCOVER_SERVICE".getBytes(StandardCharsets.UTF_8);
            guiDenCacDich(udpSocket, requestData);

            byte[] responseBuffer = new byte[1024];
            long deadline = System.currentTimeMillis() + DISCOVERY_TIMEOUT_MS;
            while (System.currentTimeMillis() < deadline) {
                DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
                try {
                    udpSocket.receive(responsePacket);
                } catch (SocketTimeoutException e) {
                    break; // hết thời gian chờ: dừng thu thập
                }

                String senderIp = responsePacket.getAddress().getHostAddress();
                String responseStr = new String(responsePacket.getData(),
                        responsePacket.getOffset(), responsePacket.getLength(),
                        StandardCharsets.UTF_8).trim();

                String[] parts = responseStr.split("\\s+");
                if (parts.length < 4 || !parts[0].equals("SERVICE")) continue;

                String key = senderIp + ":" + parts[2];
                if (servers.containsKey(key)) continue;
                servers.put(key, new String[] {senderIp, parts[1], parts[2], parts[3]});
                System.out.println("  -> Tìm thấy dịch vụ [" + key + "]: " + responseStr);
            }
        } catch (IOException e) {
            System.err.println("Lỗi khám phá UDP: " + e.getMessage());
        }

        if (servers.isEmpty()) {
            System.err.println("Không tìm thấy dịch vụ nào trong " + DISCOVERY_TIMEOUT_MS + " ms.");
            return;
        }

        System.out.println("\nTổng số server tìm thấy: " + servers.size());
        for (Map.Entry<String, String[]> entry : servers.entrySet()) {
            String[] s = entry.getValue();
            System.out.println("  - " + s[1] + " tại " + entry.getKey() + " (phiên bản " + s[3] + ")");
        }

        // BƯỚC 2: kết nối TCP tới server đầu tiên, dùng địa chỉ NGUỒN của datagram phản hồi.
        Map.Entry<String, String[]> first = servers.entrySet().iterator().next();
        String discoveredIp = first.getValue()[0];
        int discoveredTcpPort = Integer.parseInt(first.getValue()[2]);
        String discoveredName = first.getValue()[1];

        System.out.println("\n[BƯỚC 2] Kết nối TCP tới '" + discoveredName + "' tại "
                + discoveredIp + ":" + discoveredTcpPort);
        try (Socket tcpSocket = new Socket(discoveredIp, discoveredTcpPort);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(tcpSocket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(
                     new OutputStreamWriter(tcpSocket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            System.out.println("  -> Đã kết nối TCP thành công!");

            out.println("INFO");
            System.out.println("Client gửi: INFO");
            System.out.println("Server trả lời: " + in.readLine());

            out.println("ECHO Chào mừng đến với hệ thống khám phá dịch vụ Socket Java!");
            System.out.println("Client gửi: ECHO <chuỗi tiếng Việt>");
            System.out.println("Server trả lời: " + in.readLine());

            out.println("QUIT");
            System.out.println("Client gửi: QUIT");
            System.out.println("Server trả lời: " + in.readLine());
        } catch (IOException e) {
            System.err.println("Lỗi gọi TCP Service: " + e.getMessage());
        }
    }
}

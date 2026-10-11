package baitap9;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

public class MulticastReceiver {
    public static final String GROUP_ADDRESS = "239.255.0.1";
    public static final int PORT = 5019;

    public static void main(String[] args) {
        String receiverName = args.length > 0 ? args[0] : "Receiver-1";
        System.out.println("=== Starting Multicast Receiver [" + receiverName + "] ===");

        try (MulticastSocket socket = new MulticastSocket(PORT)) {
            // Cho phép nhiều tiến trình cùng bind vào cổng Multicast PORT
            socket.setReuseAddress(true);

            InetAddress group = InetAddress.getByName(GROUP_ADDRESS);
            SocketAddress groupAddress = new InetSocketAddress(group, PORT);

            NetworkInterface netIf = getPreferredInterface();
            System.out.println("[" + receiverName + "] Tham gia nhóm multicast " + GROUP_ADDRESS + ":" + PORT
                    + " qua card mạng: " + (netIf != null ? netIf.getName() + " (" + netIf.getDisplayName() + ")" : "DEFAULT"));

            if (netIf != null) {
                socket.joinGroup(groupAddress, netIf);
            }

            System.out.println("[" + receiverName + "] Sẵn sàng nhận thông báo (Chờ gửi từ MulticastSender)...");
            byte[] buffer = new byte[4096];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
                System.out.println("[" + receiverName + "] Nhận được thông báo từ " + packet.getAddress() + ":" + packet.getPort() + " -> " + message);

                if (message.equalsIgnoreCase("QUIT")) {
                    System.out.println("[" + receiverName + "] Nhận lệnh QUIT. Đang thoát...");
                    break;
                }
            }

            if (netIf != null) {
                socket.leaveGroup(groupAddress, netIf);
                System.out.println("[" + receiverName + "] Đã rời nhóm multicast an toàn.");
            }

        } catch (IOException e) {
            System.err.println("[" + receiverName + "] Lỗi Socket: " + e.getMessage());
        }
    }

    public static NetworkInterface getPreferredInterface() {
        try {
            // Thử lấy lo0 hoặc loopback trước để đảm bảo hoạt động 100% trên macOS và môi trường kiểm thử
            NetworkInterface lo = NetworkInterface.getByName("lo0");
            if (lo != null && lo.isUp() && lo.supportsMulticast()) {
                return lo;
            }

            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface n = interfaces.nextElement();
                if (n.isUp() && n.supportsMulticast()) {
                    return n;
                }
            }
        } catch (Exception ignored) {}
        return null;
    }
}

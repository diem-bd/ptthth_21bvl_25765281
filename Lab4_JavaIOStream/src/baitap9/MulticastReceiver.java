package baitap9;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

public class MulticastReceiver {
    public static final String GROUP_ADDRESS = "239.255.0.1";
    public static final int PORT = 5019;

    public static void main(String[] args) {
        String receiverName = args.length > 0 ? args[0] : "Receiver-1";
        System.out.println("=== Starting Multicast Receiver [" + receiverName + "] ===");

        try (MulticastSocket socket = new MulticastSocket(PORT)) {
            // Cho phép nhiều tiến trình cùng bind vào cổng multicast.
            socket.setReuseAddress(true);

            InetAddress group = InetAddress.getByName(GROUP_ADDRESS);
            SocketAddress groupAddress = new InetSocketAddress(group, PORT);

            NetworkInterface netIf = getPreferredInterface();
            if (netIf == null) {
                // Chưa join được group thì receive() sẽ không bao giờ nhận được gói multicast,
                // nên phải báo lỗi rõ ràng thay vì "chạy tiếp như đã join".
                System.err.println("[" + receiverName + "] Không tìm được card mạng hỗ trợ multicast.");
                System.err.println("  Gợi ý: bật Wi-Fi/Ethernet hoặc chạy trên mạng phòng lab.");
                return;
            }
            System.out.println("[" + receiverName + "] Tham gia nhóm multicast " + GROUP_ADDRESS + ":" + PORT
                    + " qua card mạng: " + netIf.getName() + " (" + netIf.getDisplayName() + ")");
            socket.joinGroup(groupAddress, netIf);

            try {
                System.out.println("[" + receiverName + "] Sẵn sàng nhận thông báo (chờ MulticastSender)...");
                byte[] buffer = new byte[4096];

                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String message = new String(packet.getData(), packet.getOffset(),
                            packet.getLength(), StandardCharsets.UTF_8);
                    System.out.println("[" + receiverName + "] Nhận được thông báo từ "
                            + packet.getAddress() + ":" + packet.getPort() + " -> " + message);

                    if (message.equalsIgnoreCase("QUIT")) {
                        System.out.println("[" + receiverName + "] Nhận lệnh QUIT. Đang thoát...");
                        break;
                    }
                }
            } finally {
                // Đề yêu cầu rời group trước khi đóng socket, kể cả khi có lỗi giữa chừng.
                socket.leaveGroup(groupAddress, netIf);
                System.out.println("[" + receiverName + "] Đã rời nhóm multicast.");
            }
        } catch (IOException e) {
            System.err.println("[" + receiverName + "] Lỗi Socket: " + e.getMessage());
        }
    }

    /**
     * Chọn card mạng để join group.
     * Có thể chỉ định tên card qua system property -Dmcast.if=en0.
     * Nếu không chỉ định: ưu tiên card thật đang hoạt động (en0, Wi-Fi, Ethernet) vì
     * trên macOS multicast qua loopback (lo0) thường báo "No route to host";
     * loopback chỉ dùng khi không còn lựa chọn nào khác.
     */
    public static NetworkInterface getPreferredInterface() {
        return getPreferredInterface(null);
    }

    public static NetworkInterface getPreferredInterface(String tenCard) {
        try {
            if (tenCard != null && !tenCard.isBlank()) {
                NetworkInterface chiDinh = NetworkInterface.getByName(tenCard.trim());
                if (chiDinh == null) {
                    System.err.println("Không có card mạng tên: " + tenCard);
                }
                return chiDinh;
            }
            NetworkInterface loopback = null;
            var interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces != null && interfaces.hasMoreElements()) {
                NetworkInterface n = interfaces.nextElement();
                if (!n.isUp() || !n.supportsMulticast()) continue;
                if (n.isLoopback()) {
                    loopback = n;
                    continue;
                }
                String ten = n.getName();
                // Bỏ qua card ảo của VPN/airdrop (utun*, awdl*, llw*, nan*, anpi*, gif*, stf*):
                // joinGroup trên các card này thường lỗi "Can't assign requested address".
                if (ten.startsWith("utun") || ten.startsWith("awdl") || ten.startsWith("llw")
                        || ten.startsWith("nan") || ten.startsWith("anpi") || ten.startsWith("gif")
                        || ten.startsWith("stf") || ten.startsWith("bridge")) {
                    continue;
                }
                // Card vật lý đã có địa chỉ IPv4 thì dùng được cho multicast cục bộ.
                if (n.getInetAddresses().hasMoreElements()) {
                    return n;
                }
            }
            return loopback;
        } catch (IOException e) {
            System.err.println("Lỗi đọc danh sách card mạng: " + e.getMessage());
        }
        return null;
    }
}

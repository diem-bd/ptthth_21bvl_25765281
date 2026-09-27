
package baitap1;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class HostAndUriInspector {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Cú pháp sử dụng: java baitap1.HostAndUriInspector <hostname> <uri>");
            System.out.println("Ví dụ: java baitap1.HostAndUriInspector localhost \"http://localhost:8080/api/users?sort=asc#top\"");
            return;
        }

        String hostname = args[0];
        String uriString = args[1];

        System.out.println("==================================================");
        System.out.println("               HOST & URI INSPECTOR               ");
        System.out.println("==================================================");

        // 1. Phân giải Hostname
        System.out.println("[1] KHẢO SÁT HOSTNAME: " + hostname);
        try {
            InetAddress[] addresses = InetAddress.getAllByName(hostname);
            for (int i = 0; i < addresses.length; i++) {
                InetAddress addr = addresses[i];
                System.out.printf("  Địa chỉ %d:%n", i + 1);
                System.out.println("    - IP:         " + addr.getHostAddress());
                String ipType = (addr instanceof Inet4Address) ? "IPv4"
                        : (addr instanceof Inet6Address) ? "IPv6" : "Unknown";
                System.out.println("    - Loại IP:    " + ipType);
                System.out.println("    - Canonical:  " + addr.getCanonicalHostName());
                System.out.println("    - Loopback:   " + addr.isLoopbackAddress());
                System.out.println("    - Site local: " + addr.isSiteLocalAddress());
            }
        } catch (UnknownHostException e) {
            System.err.println("  ❌ Lỗi: Không thể phân giải hostname '" + hostname + "' (Host không tồn tại hoặc lỗi DNS)");
        }

        // 2. Phân tích URI
        System.out.println("\n[2] PHÂN TÍCH THÀNH PHẦN URI: " + uriString);
        try {
            URI uri = new URI(uriString);
            // Kiểm tra tính hợp lệ cơ bản nếu URI không có scheme
            if (uri.getScheme() == null) {
                System.err.println("  ⚠️ Cảnh báo: URI không chỉ định giao thức (Scheme).");
            }
            System.out.println("    - Scheme:   " + (uri.getScheme() != null ? uri.getScheme() : "[Không có]"));
            System.out.println("    - Host:     " + (uri.getHost() != null ? uri.getHost() : "[Không có]"));
            System.out.println("    - Port:     " + (uri.getPort() != -1 ? uri.getPort() : "[Mặc định / Không có]"));
            System.out.println("    - Path:     " + (uri.getPath() != null && !uri.getPath().isEmpty() ? uri.getPath() : "[Trống]"));
            System.out.println("    - Query:    " + (uri.getQuery() != null ? uri.getQuery() : "[Không có]"));
            System.out.println("    - Fragment: " + (uri.getFragment() != null ? uri.getFragment() : "[Không có]"));
        } catch (URISyntaxException e) {
            System.err.println("  ❌ Lỗi: Cú pháp URI không hợp lệ -> " + e.getReason() + " tại vị trí " + e.getIndex());
        }
        System.out.println("==================================================\n");
    }
}

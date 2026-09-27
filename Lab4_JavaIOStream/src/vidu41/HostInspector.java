package vidu41;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostInspector {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java vidu41.HostInspector <hostname>");
            return;
        }

        try {
            InetAddress[] addresses = InetAddress.getAllByName(args[0]);
            System.out.println("Host: " + args[0]);
            for (InetAddress address : addresses) {
                System.out.println("- IP: " + address.getHostAddress());
                String ipType = (address instanceof Inet4Address) ? "IPv4"
                        : (address instanceof Inet6Address) ? "IPv6" : "Unknown";
                System.out.println("  Kiểu IP:    " + ipType);
                System.out.println("  Canonical:  " + address.getCanonicalHostName());
                System.out.println("  Loopback:   " + address.isLoopbackAddress());
                System.out.println("  Site local: " + address.isSiteLocalAddress());
            }
        } catch (UnknownHostException e) {
            System.err.println("Không phân giải được host: " + args[0]);
        }
    }
}

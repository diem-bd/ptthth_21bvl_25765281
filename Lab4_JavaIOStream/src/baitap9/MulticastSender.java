package baitap9;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;

public class MulticastSender {
    public static void main(String[] args) {
        System.out.println("=== Multicast Announcement Sender ===");
        System.out.println("Target Multicast Group: " + MulticastReceiver.GROUP_ADDRESS + ":" + MulticastReceiver.PORT);

        try (MulticastSocket socket = new MulticastSocket();
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

            NetworkInterface netIf = MulticastReceiver.getPreferredInterface();
            if (netIf != null) {
                socket.setNetworkInterface(netIf);
                System.out.println("Outgoing Network Interface: " + netIf.getName() + " (" + netIf.getDisplayName() + ")");
            }
            socket.setTimeToLive(1); // Local subnet/loopback

            InetAddress group = InetAddress.getByName(MulticastReceiver.GROUP_ADDRESS);

            if (args.length > 0) {
                // Gửi thông điệp từ command line args
                String msg = String.join(" ", args);
                byte[] data = msg.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(data, data.length, group, MulticastReceiver.PORT);
                socket.send(packet);
                System.out.println("Sent announcement: " + msg);
                return;
            }

            System.out.println("Enter announcement messages (Type 'QUIT' to exit):");
            System.out.print("> ");
            String line;
            while ((line = console.readLine()) != null) {
                byte[] data = line.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(data, data.length, group, MulticastReceiver.PORT);
                socket.send(packet);
                System.out.println("Sent announcement: " + line);

                if (line.trim().equalsIgnoreCase("QUIT")) {
                    break;
                }
                System.out.print("> ");
            }
        } catch (Exception e) {
            System.err.println("Sender error: " + e.getMessage());
        }
    }
}

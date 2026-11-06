package baitap6;

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

public class BenchmarkClient {
    private static final String HOST = "localhost";
    private static final int NUM_MESSAGES = 1000;
    private static final int NUM_ROUNDS = 5;
    private static final int PAYLOAD_BYTES = 256;
    private static final int TIMEOUT_MS = 3000;

    public static void main(String[] args) {
        // Payload là chuỗi ASCII đúng PAYLOAD_BYTES byte.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < PAYLOAD_BYTES; i++) {
            sb.append((char) ('A' + (i % 26)));
        }
        String payload = sb.toString();
        // TCP giao thức một dòng nên mỗi yêu cầu thêm 1 byte '\n'; UDP gửi đúng payload.
        int tcpBytes = payload.getBytes(StandardCharsets.UTF_8).length + 1;
        int udpBytes = payload.getBytes(StandardCharsets.UTF_8).length;

        System.out.println("==========================================================================");
        System.out.println("  BÀI TẬP 6: SO SÁNH HIỆU NĂNG TCP VÀ UDP (" + NUM_MESSAGES + " GÓI TIN x " + NUM_ROUNDS + " LƯỢT)");
        System.out.println("  Môi trường : " + System.getProperty("os.name") + " / "
                + System.getProperty("os.arch") + " / Java " + System.getProperty("java.version"));
        System.out.println("  Kích thước: TCP " + tcpBytes + " byte/dòng (payload " + PAYLOAD_BYTES
                + " + newline), UDP " + udpBytes + " byte/gói");
        System.out.println("  Timeout   : " + TIMEOUT_MS + " ms cho cả TCP và UDP");
        System.out.println("  Phương pháp: mỗi lượt gửi tuần tự NUM_MESSAGES yêu cầu và chờ phản hồi,");
        System.out.println("               đo tổng thời gian bằng System.nanoTime().");
        System.out.println("==========================================================================");

        long[] tcpTimes = new long[NUM_ROUNDS];
        int[] tcpReceived = new int[NUM_ROUNDS];
        long[] udpTimes = new long[NUM_ROUNDS];
        int[] udpReceived = new int[NUM_ROUNDS];

        System.out.printf("%-8s | %-16s | %-12s | %-16s | %-12s%n",
                "Lượt", "TCP Thời gian", "TCP Nhận/Gửi", "UDP Thời gian", "UDP Nhận/Gửi");
        System.out.println("--------------------------------------------------------------------------");

        for (int round = 0; round < NUM_ROUNDS; round++) {
            long startTcp = System.nanoTime();
            int countTcp = runTcpBenchmark(payload);
            long endTcp = System.nanoTime();
            tcpTimes[round] = (endTcp - startTcp) / 1_000_000;
            tcpReceived[round] = countTcp;

            long startUdp = System.nanoTime();
            int countUdp = runUdpBenchmark(payload);
            long endUdp = System.nanoTime();
            udpTimes[round] = (endUdp - startUdp) / 1_000_000;
            udpReceived[round] = countUdp;

            System.out.printf("Lần %-4d | %10d ms   | %7d/%-4d | %10d ms   | %7d/%-4d%n",
                    (round + 1), tcpTimes[round], countTcp, NUM_MESSAGES,
                    udpTimes[round], countUdp, NUM_MESSAGES);
        }

        long totalTcpTime = 0;
        long totalUdpTime = 0;
        int totalTcpReceived = 0;
        int totalUdpReceived = 0;
        for (int i = 0; i < NUM_ROUNDS; i++) {
            totalTcpTime += tcpTimes[i];
            totalUdpTime += udpTimes[i];
            totalTcpReceived += tcpReceived[i];
            totalUdpReceived += udpReceived[i];
        }

        double avgTcpTime = (double) totalTcpTime / NUM_ROUNDS;
        double avgUdpTime = (double) totalUdpTime / NUM_ROUNDS;

        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("TRUNG BÌNH| %10.1f ms   | %7d/%-4d | %10.1f ms   | %7d/%-4d%n",
                avgTcpTime, totalTcpReceived / NUM_ROUNDS, NUM_MESSAGES,
                avgUdpTime, totalUdpReceived / NUM_ROUNDS, NUM_MESSAGES);
        System.out.println("==========================================================================");
        System.out.println("NHẬN XÉT (dựa trên số đo thực tế ở trên, không kết luận trước):");
        if (avgUdpTime < avgTcpTime) {
            System.out.printf("1. Trong 5 lượt này UDP trung bình %.1f ms, TCP %.1f ms (UDP nhanh hơn %.2fx).%n",
                    avgUdpTime, avgTcpTime, avgTcpTime / Math.max(1e-9, avgUdpTime));
        } else {
            System.out.printf("1. Trong 5 lượt này TCP trung bình %.1f ms, UDP %.1f ms (UDP không nhanh hơn).%n",
                    avgTcpTime, avgUdpTime);
        }
        int udpLost = NUM_ROUNDS * NUM_MESSAGES - totalUdpReceived;
        int tcpLost = NUM_ROUNDS * NUM_MESSAGES - totalTcpReceived;
        System.out.printf("2. Độ tin cậy: TCP nhận %d/%d, UDP nhận %d/%d (UDP mất %d gói).%n",
                totalTcpReceived, NUM_ROUNDS * NUM_MESSAGES,
                totalUdpReceived, NUM_ROUNDS * NUM_MESSAGES, udpLost);
        System.out.println("   Kết quả trên chỉ đúng cho localhost ở lần chạy này: mất gói UDP phụ thuộc");
        System.out.println("   tải máy, buffer và mạng. TCP vẫn bảo đảm thứ tự và tự truyền lại khi mất gói,");
        System.out.println("   nên không thể kết luận \"UDP luôn nhanh hơn\" từ một phép đo.");
        if (tcpLost > 0) {
            System.out.println("   Lưu ý: TCP cũng thiếu phản hồi trong lần chạy này, cần xem lại timeout.");
        }
        System.out.println("==========================================================================");
    }

    private static int runTcpBenchmark(String payload) {
        int received = 0;
        try (Socket socket = new Socket(HOST, BenchmarkServer.TCP_PORT);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            socket.setSoTimeout(TIMEOUT_MS);
            for (int i = 0; i < NUM_MESSAGES; i++) {
                writer.println(payload);
                String resp = reader.readLine();
                if (resp != null) {
                    received++;
                }
            }
            writer.println("QUIT");
        } catch (IOException e) {
            System.err.println("TCP run error sau " + received + " gói: " + e.getMessage());
        }
        return received;
    }

    private static int runUdpBenchmark(String payload) {
        int received = 0;
        byte[] data = payload.getBytes(StandardCharsets.UTF_8);
        byte[] buffer = new byte[BenchmarkServer.BUFFER_SIZE];

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(TIMEOUT_MS);
            InetAddress address = InetAddress.getByName(HOST);

            for (int i = 0; i < NUM_MESSAGES; i++) {
                DatagramPacket packet = new DatagramPacket(data, data.length, address, BenchmarkServer.UDP_PORT);
                socket.send(packet);

                DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(responsePacket);
                    received++;
                } catch (SocketTimeoutException e) {
                    // Gói này coi như mất; tiếp tục gói kế tiếp để đo tỷ lệ nhận.
                }
            }
        } catch (IOException e) {
            System.err.println("UDP run error sau " + received + " gói: " + e.getMessage());
        }
        return received;
    }
}

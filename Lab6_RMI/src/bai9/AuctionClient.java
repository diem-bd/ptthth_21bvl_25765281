package bai9;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/** Một chương trình đóng cả vai người bán (thêm sản phẩm) và người mua (trả giá, nhận thông báo). */
public class AuctionClient {
    static class Listener extends UnicastRemoteObject implements BidderCallback {
        private final String nickname;

        Listener(String nickname) throws RemoteException {
            super(0);
            this.nickname = nickname;
        }

        @Override
        public void onOutbid(String product, String newBidder, double newPrice) {
            System.out.printf("  [%s nhận] Bị vượt giá %s: %s đặt %,.0f%n",
                    nickname, product, newBidder, newPrice);
        }

        @Override
        public void onNewBid(String product, String bidder, double price) {
            System.out.printf("  [%s nhận] Giá mới %s: %,.0f bởi %s%n", nickname, product, price, bidder);
        }
    }

    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1106);
        AuctionService auction = (AuctionService) registry.lookup("Auction");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        System.out.print("Biệt danh người đấu giá: ");
        String nickname = in.readLine().trim();
        auction.register(nickname, new Listener(nickname));
        while (true) {
            System.out.println("\n1-Thêm sản phẩm (người bán)  2-Danh sách  3-Đặt giá  4-Giá hiện tại  0-Thoát");
            System.out.print("Chọn: ");
            String choice = in.readLine();
            if (choice == null || choice.trim().equals("0")) break;
            try {
                switch (choice.trim()) {
                    case "1":
                        System.out.print("Tên sản phẩm: ");
                        String name = in.readLine().trim();
                        System.out.print("Giá khởi điểm: ");
                        auction.addProduct(name, Double.parseDouble(in.readLine().trim()));
                        System.out.println("Đã thêm sản phẩm.");
                        break;
                    case "2":
                        System.out.print(auction.listProducts());
                        break;
                    case "3":
                        System.out.print("Tên sản phẩm: ");
                        String product = in.readLine().trim();
                        System.out.print("Giá muốn trả: ");
                        double price = Double.parseDouble(in.readLine().trim());
                        System.out.println(auction.placeBid(product, nickname, price)
                                ? "Đặt giá thành công."
                                : "Đặt giá thất bại: phải cao hơn giá hiện tại.");
                        break;
                    case "4":
                        System.out.print("Tên sản phẩm: ");
                        System.out.printf("Giá hiện tại: %,.0f%n",
                                auction.currentPrice(in.readLine().trim()));
                        break;
                    default:
                        System.out.println("Chọn 0 đến 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số.");
            } catch (RemoteException e) {
                System.out.println("Server từ chối: " + e.getMessage());
            }
        }
    }
}

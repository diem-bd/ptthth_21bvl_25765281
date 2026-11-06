package bai9;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuctionServiceImpl extends UnicastRemoteObject implements AuctionService {
    private static class Item {
        final double startPrice;
        double highestPrice;
        String highestBidder = "(chưa có)";

        Item(double startPrice) {
            this.startPrice = startPrice;
            this.highestPrice = startPrice;
        }
    }

    private final Map<String, Item> items = new LinkedHashMap<>();
    private final Map<String, BidderCallback> bidders = new ConcurrentHashMap<>();

    public AuctionServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized void addProduct(String name, double startPrice) throws RemoteException {
        if (name == null || name.isBlank()) throw new RemoteException("Tên sản phẩm không được rỗng");
        if (!Double.isFinite(startPrice) || startPrice <= 0) {
            throw new RemoteException("Giá khởi điểm phải lớn hơn 0");
        }
        if (items.containsKey(name.trim())) throw new RemoteException("Sản phẩm đã tồn tại");
        items.put(name.trim(), new Item(startPrice));
        System.out.printf("[Server] Thêm sản phẩm %s, giá khởi điểm %,.0f%n", name, startPrice);
    }

    @Override
    public synchronized String listProducts() {
        if (items.isEmpty()) return "(Chưa có sản phẩm)";
        StringBuilder builder = new StringBuilder();
        items.forEach((name, item) -> builder.append(String.format("  %-16s khởi điểm %,.0f | cao nhất %,.0f (%s)%n",
                name, item.startPrice, item.highestPrice, item.highestBidder)));
        return builder.toString();
    }

    @Override
    public boolean placeBid(String product, String bidder, double amount) throws RemoteException {
        String previous;
        synchronized (this) {
            Item item = items.get(product);
            if (item == null) throw new RemoteException("Không có sản phẩm " + product);
            if (!Double.isFinite(amount) || amount <= 0) throw new RemoteException("Giá phải lớn hơn 0");
            if (amount <= item.highestPrice) {
                System.out.printf("[Server] Từ chối %,.0f của %s (phải > %,.0f)%n",
                        amount, bidder, item.highestPrice);
                return false;
            }
            previous = item.highestBidder;
            item.highestPrice = amount;
            item.highestBidder = bidder;
            System.out.printf("[Server] %s đặt %,.0f cho %s%n", bidder, amount, product);
        }
        // Thông báo callback ra NGOÀI khối synchronized: lời gọi RMI tới client có thể chậm
        // hoặc client gọi ngược lại server, giữ khóa khi gọi sẽ dễ gây treo.
        notifyBidders(product, bidder, amount, previous);
        return true;
    }

    private void notifyBidders(String product, String bidder, double amount, String previous) {
        for (Map.Entry<String, BidderCallback> entry : bidders.entrySet()) {
            String name = entry.getKey();
            BidderCallback callback = entry.getValue();
            try {
                if (name.equals(previous)) {
                    callback.onOutbid(product, bidder, amount);
                } else if (!name.equals(bidder)) {
                    callback.onNewBid(product, bidder, amount);
                }
            } catch (RemoteException e) {
                bidders.remove(name, callback);
                System.out.println("[Server] Client " + name + " mất kết nối");
            }
        }
    }

    @Override
    public synchronized double currentPrice(String product) throws RemoteException {
        Item item = items.get(product);
        if (item == null) throw new RemoteException("Không có sản phẩm " + product);
        return item.highestPrice;
    }

    @Override
    public synchronized void register(String bidder, BidderCallback callback) throws RemoteException {
        bidders.put(bidder, callback);
        System.out.println("[Server] " + bidder + " tham gia đấu giá");
    }

    @Override
    public synchronized void unregister(String bidder) {
        bidders.remove(bidder);
        System.out.println("[Server] " + bidder + " rời phiên đấu giá");
    }
}

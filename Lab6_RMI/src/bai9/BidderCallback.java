package bai9;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** Server gọi ngược về người đấu giá khi bị người khác trả giá cao hơn. */
public interface BidderCallback extends Remote {
    void onOutbid(String product, String newBidder, double newPrice) throws RemoteException;

    void onNewBid(String product, String bidder, double price) throws RemoteException;
}

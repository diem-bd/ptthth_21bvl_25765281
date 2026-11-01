package bai9;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuctionService extends Remote {
    void addProduct(String name, double startPrice) throws RemoteException;

    String listProducts() throws RemoteException;

    boolean placeBid(String product, String bidder, double amount) throws RemoteException;

    double currentPrice(String product) throws RemoteException;

    void register(String bidder, BidderCallback callback) throws RemoteException;
}

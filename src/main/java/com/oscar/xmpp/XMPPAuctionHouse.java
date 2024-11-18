package com.oscar.xmpp;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import com.oscar.Auction;
import com.oscar.AuctionHouse;

public class XMPPAuctionHouse implements AuctionHouse{
    private final XMPPConnection connection;

    private XMPPAuctionHouse(XMPPConnection connection){
        this.connection = connection;
    }

    public static XMPPAuctionHouse connect(String hostName, String userName, String password) throws XMPPException{
        XMPPConnection connection = connection(hostName, userName, password);

        return new XMPPAuctionHouse(connection);
    }

    @Override
    public Auction auctionFor(String itemId) {
        return new XMPPAuction(connection, itemId);
    }

    @Override
    public void disconnect() {
        connection.disconnect();
    }


    private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException{
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, XMPPAuction.AUCTION_RESOURCE);

        return connection;
    }
}

package com.oscar;

public interface AuctionHouse {
    Auction auctionFor(String itemId);
    void disconnect();
}

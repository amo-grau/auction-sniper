package com.oscar;

public interface AuctionHouse {
    Auction auctionFor(Item item);
    void disconnect();
}

package com.oscar;

import com.oscar.util.Announcer;

public class AuctionSniper implements AuctionEventListener {

    private SniperSnapshot snapshot;
    
    private final Announcer<SniperListener> sniperListeners = Announcer.to(SniperListener.class);

    private final Auction auction;

    public AuctionSniper(String itemId, Auction auction){
        this.auction = auction;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    public void auctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        switch (priceSource) {
            case FromSniper:
                snapshot = snapshot.winning(price);
                break;

            case FromOtherBidder:
                int bid = price + increment;
                auction.bid(bid);
                snapshot = snapshot.bidding(price, bid);
                break;
        }

        notifyChange();
    }

    private void notifyChange(){
        sniperListeners.announce().sniperStateChanged(snapshot);
    }

    public SniperSnapshot getSnapshot() {
        return snapshot;
    }

    public void addSniperListener(SniperListener swingThreadSniperListener) {
        sniperListeners.addListener(swingThreadSniperListener);
    }
}

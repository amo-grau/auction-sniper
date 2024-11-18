package com.oscar;

import com.oscar.util.Announcer;

public class AuctionSniper implements AuctionEventListener {

    private SniperSnapshot snapshot;
    private final Item item;
    
    private final Announcer<SniperListener> sniperListeners = Announcer.to(SniperListener.class);

    private final Auction auction;

    public AuctionSniper(Item item, Auction auction){
        this.auction = auction;
        this.snapshot = SniperSnapshot.joining(item.identifier);
        this.item = item;
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
                if (item.allowsBid(bid)){
                    auction.bid(bid);
                    snapshot = snapshot.bidding(price, bid);
                    break;
                } else{
                    snapshot = snapshot.losing(price);
                }
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

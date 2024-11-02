package com.oscar;

public class AuctionSniper implements AuctionEventListener {

    private SniperSnapshot snapshot;
    
    private final SniperListener sniperListener;
    private final Auction auction;

    public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener){
        this.sniperListener = sniperListener;
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
        sniperListener.sniperStateChanged(snapshot);
    }
}

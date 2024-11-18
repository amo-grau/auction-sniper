package com.oscar;

public record SniperSnapshot(String itemId, int lastPrice, int lastBid, SniperState state) 
{
    public SniperSnapshot bidding(int newLastPrice, int newLastbid){
        return new SniperSnapshot(itemId, newLastPrice, newLastbid, SniperState.BIDDING);
    }

    public SniperSnapshot winning(int newLastPrice){
        return new SniperSnapshot(itemId, newLastPrice, newLastPrice, SniperState.WINNING);
    }

    public static SniperSnapshot joining(String itemId){
        return new SniperSnapshot(itemId, 0, 0, SniperState.JOINING);
    }

    public SniperSnapshot closed(){
        return new SniperSnapshot(itemId, lastPrice, lastBid, state.whenAuctionClosed());
    }

    public boolean isForSameItemAs(SniperSnapshot otherSnapshot) {
        return this.itemId() == otherSnapshot.itemId();
    }

    public SniperSnapshot losing(int newLastPrice) {
        return new SniperSnapshot(itemId, newLastPrice, lastBid, SniperState.LOSING);
    }
}

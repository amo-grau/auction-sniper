package com.oscar;

import com.oscar.ui.MainWindow;
import com.oscar.ui.SnipersTableModel;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";
    public static final String XMPP_HOSTNAME = "localhost";
    
    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer... auctions){
        startSniper(auctions);

        for (FakeAuctionServer auction : auctions){
            final String itemId = auction.getItemId();
            driver.startBiddingFor(itemId, Integer.MAX_VALUE);
            driver.showsSniperStatus(itemId, 0, 0, SnipersTableModel.textFor(SniperState.JOINING));
        }
    }

    private void startSniper(final FakeAuctionServer... auctions){
        Thread thread = new Thread("Test Application"){
            @Override public void run(){
                try{
                    App.main(arguments(auctions));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        
        thread.setDaemon(true);
        thread.start();

        driver = new AuctionSniperDriver(1000);
        driver.hasTitle(MainWindow.APPLICATION_TITLE);
        driver.hasColumnTitles();
    }

    public static String [] arguments(FakeAuctionServer ... auctions){
        String[] arguments = new String[auctions.length + 3];
        arguments[0] = XMPP_HOSTNAME;
        arguments[1] = SNIPER_ID;
        arguments[2] = SNIPER_PASSWORD;

        for (int i = 0; i < auctions.length; i++){
            arguments[i + 3] = auctions[i].getItemId();
        }
        
        return arguments;
    }
    
    public void showsSniperHasLostAuction(FakeAuctionServer auction, int lastPrice, int lastBid){
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, SnipersTableModel.textFor(SniperState.LOST));
    }

    public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid){
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, SnipersTableModel.textFor(SniperState.BIDDING));
    }

    public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid){
        driver.showsSniperStatus(auction.getItemId(), winningBid, winningBid, 
            SnipersTableModel.textFor(SniperState.WINNING));
    }
    
    public void showsSniperHasWonAuction(FakeAuctionServer auction, int lastPrice){
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice,
                                SnipersTableModel.textFor(SniperState.WON));
    }
    
        public void hasShownSniperIsLosing(FakeAuctionServer auction, int lastPrice, int lastBid) {
            driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice,
                SnipersTableModel.textFor(SniperState.LOSING));
        }

    public void stop() {
        if (driver != null){
            driver.dispose();
        }
    }

    public void startBiddingWithStopPrice(FakeAuctionServer auction, int stopPrice) {
        startSniper(auction);

        driver.startBiddingFor(auction.getItemId(), stopPrice);
        driver.showsSniperStatus(auction.getItemId(), 0, 0, SnipersTableModel.textFor(SniperState.JOINING));
    }
}

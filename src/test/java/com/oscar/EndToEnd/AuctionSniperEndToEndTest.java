package com.oscar.EndToEnd;

import org.junit.After;
import org.junit.Test;

import com.oscar.ApplicationRunner;
import com.oscar.FakeAuctionServer;

public class AuctionSniperEndToEndTest{
    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final ApplicationRunner application = new ApplicationRunner();

    public AuctionSniperEndToEndTest() {
    }

    @Test public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
        auction.startSellingItem();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
        auction.announceClosed();
        application.showsSniperHasLostAuction();
    }

    @Test public void sniperMakesAHigherBidButLoses() throws Exception{
        auction.startSellingItem();

        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding();

        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);

        auction.announceClosed();
        application.showsSniperHasLostAuction();
    }

    // Additional cleanup
    @After public void stopAuction(){
        auction.stop();
    }
    @After public void stopApplication(){
        application.stop();
    }
}

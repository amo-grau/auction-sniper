package com.oscar.Integration.xmpp;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jivesoftware.smack.XMPPException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.oscar.ApplicationRunner;
import com.oscar.Auction;
import com.oscar.AuctionEventListener;
import com.oscar.FakeAuctionServer;
import com.oscar.EndToEnd.AuctionSniperEndToEndTest;
import com.oscar.xmpp.XMPPAuctionHouse;

@RunWith(MockitoJUnitRunner.class)
public class XMPPAuctionHouseTest {
    FakeAuctionServer server = new FakeAuctionServer(AuctionSniperEndToEndTest.ITEM_ID);
    XMPPAuctionHouse auctionHouse;

    @Before public void startAuction() throws XMPPException{
        auctionHouse = XMPPAuctionHouse.connect(ApplicationRunner.XMPP_HOSTNAME, ApplicationRunner.SNIPER_ID, ApplicationRunner.SNIPER_PASSWORD);
    }

    @Before public void startServer() throws XMPPException {
        server.startSellingItem();
    }

    @Test public void
    receivesEventsFromAuctionServerAfterJOining() throws Exception{
        CountDownLatch auctionWasClosed = new CountDownLatch(1);

        Auction auction = auctionHouse.auctionFor(server.getItemId());
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));

        auction.join();
        server.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        server.announceClosed();

        assertTrue("should have been closed", auctionWasClosed.await(2, TimeUnit.SECONDS));
    }

    private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed){
        return new AuctionEventListener() {

            @Override
            public void auctionClosed() {
                auctionWasClosed.countDown();
            }

            @Override
            public void currentPrice(int price, int increment, PriceSource priceSource) {
                // unimplemented
            }
            
        };
    }
}

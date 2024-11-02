package com.oscar.Unit;

import com.oscar.EndToEnd.AuctionSniperEndToEndTest;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oscar.Auction;
import com.oscar.AuctionSniper;
import com.oscar.SniperListener;
import com.oscar.SniperSnapshot;
import com.oscar.SniperState;
import com.oscar.AuctionEventListener.PriceSource;

@RunWith(MockitoJUnitRunner.class)
public class AuctionSniperTest{
    @Mock
    private SniperListener sniperListener;
    @Mock 
    private Auction auction;

    @InjectMocks
    private AuctionSniper sniper;

    @Before public void
    setup(){
        sniper = new AuctionSniper(AuctionSniperEndToEndTest.ITEM_ID, auction, sniperListener);
    }

    @Test public void
    reportsLostWhenAuctionClosesInmediately(){
        sniper.auctionClosed();

        verify(sniperListener, atLeast(1)).sniperStateChanged(
            new SniperSnapshot(AuctionSniperEndToEndTest.ITEM_ID, 0, 0, SniperState.LOST));
    }

    @Test public void
    reportsLostWhenAuctionClosesWhenBidding(){
        ignoreStubs(auction);
        
        sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);        
        verify(sniperListener, atLeastOnce()).sniperStateChanged(argThat(itsStateIs(SniperState.BIDDING)));

        sniper.auctionClosed();
        verify(sniperListener, atLeast(1)).sniperStateChanged(
            new SniperSnapshot(AuctionSniperEndToEndTest.ITEM_ID, 123, 168, SniperState.LOST));
    }

    @Test public void
    reportsWonIfAuctionClosesWhenWinning(){ // Todo: check if its correct
        sniper.currentPrice(123, 45, PriceSource.FromSniper);
        verify(sniperListener, atLeastOnce()).sniperStateChanged(argThat(itsStateIs(SniperState.WINNING)));


        sniper.auctionClosed();
        verify(sniperListener, atLeast(1)).sniperStateChanged(
            new SniperSnapshot(AuctionSniperEndToEndTest.ITEM_ID, 123, 123, SniperState.WON));    }

    @Test public void
    bidsHigherAndReportsBiddingWhenNewPriceArrives(){
        final int price = 1001;
        final int increment = 25;
        final int bid = price + increment;

        sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);

        verify(auction, times(1)).bid(price + increment);
        verify(sniperListener, atLeast(1)).sniperStateChanged(
            new SniperSnapshot(AuctionSniperEndToEndTest.ITEM_ID, price, bid, SniperState.BIDDING));
    }

    @Test public void
    reportsIsWinningWhenCurrentPriceComesFromSniper(){
        sniper.currentPrice(123, 12, PriceSource.FromOtherBidder);
        verify(sniperListener, atLeastOnce()).sniperStateChanged(argThat(itsStateIs(SniperState.BIDDING)));

        sniper.currentPrice(135, 45, PriceSource.FromSniper);
        verify(sniperListener, atLeast(1)).sniperStateChanged(
            new SniperSnapshot(AuctionSniperEndToEndTest.ITEM_ID, 135, 135, SniperState.WINNING)
        );
    }

    private ArgumentMatcher<SniperSnapshot> itsStateIs(SniperState expectedState) {
        return snapshot -> snapshot.state() == expectedState;
    }
}
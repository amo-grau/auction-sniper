package com.oscar.Unit;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oscar.Auction;
import com.oscar.AuctionSniper;
import com.oscar.SniperListener;
import com.oscar.AuctionEventListener.PriceSource;

@RunWith(MockitoJUnitRunner.class)
public class AuctionSniperTest{
    @Mock
    private SniperListener sniperListener;
    @Mock 
    private Auction auction;

    @InjectMocks
    private AuctionSniper sniper;

    @Test public void
    reportsLostWhenAuctionClosesInmediately(){
        sniper.auctionClosed();

        verify(sniperListener, atLeast(1)).sniperLost();
    }

    @Test public void
    reportsLostWhenAuctionClosesWhenBidding(){
        ignoreStubs(auction);
        
        sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);        
        verify(sniperListener, atLeastOnce()).sniperBidding();

        sniper.auctionClosed();
        verify(sniperListener, atLeast(1)).sniperLost();
    }

    @Test public void
    reportsWonIfAuctionClosesWhenWinning(){
        sniper.currentPrice(123, 45, PriceSource.FromSniper);
        verify(sniperListener, atLeastOnce()).sniperWinning();

        sniper.auctionClosed();
        verify(sniperListener, atLeastOnce()).sniperWon();
    }

    @Test public void
    bidsHigherAndReportsBiddingWhenNewPriceArrives(){
        final int price = 1001;
        final int increment = 25;

        sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);

        verify(auction, times(1)).bid(price + increment);
        verify(sniperListener, atLeast(1)).sniperBidding();
    }

    @Test public void
    reportsIsWinningWhenCurrentPriceComesFromSniper(){
        sniper.currentPrice(123, 45, PriceSource.FromSniper);

        verify(sniperListener, atLeast(1)).sniperWinning();
    }
}
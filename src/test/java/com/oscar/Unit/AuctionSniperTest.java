package com.oscar.Unit;

import static org.mockito.Mockito.atLeast;
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

@RunWith(MockitoJUnitRunner.class)
public class AuctionSniperTest{
    @Mock
    private SniperListener sniperListener;
    @Mock 
    private Auction auction;

    @InjectMocks
    private AuctionSniper sniper;

    @Test public void
    reportsLostWhenAuctionCloses(){
        sniper.auctionClosed();

        verify(sniperListener, atLeast(1)).sniperLost();
    }
    
    @Test public void
    bidsHigherAndReportsBiddingWhenNewPriceArrives(){
        final int price = 1001;
        final int increment = 25;

        sniper.currentPrice(price, increment);

        verify(auction, times(1)).bid(price + increment);
        verify(sniperListener, atLeast(1)).sniperBidding();
    }
}
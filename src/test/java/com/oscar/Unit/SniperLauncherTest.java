package com.oscar.Unit;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oscar.Auction;
import com.oscar.AuctionHouse;
import com.oscar.AuctionSniper;
import com.oscar.Item;
import com.oscar.SniperCollector;
import com.oscar.SniperLauncher;

@RunWith(MockitoJUnitRunner.class)
public class SniperLauncherTest {
    @Mock private AuctionHouse auctionHouse;
    @Mock private SniperCollector collector;
    @Mock private Auction auction;

    @InjectMocks
    private SniperLauncher launcher;

    @Test public void
    addsNewSniperToCollectorAndThenJoinsAuction(){
        final Item item = new Item("item 123", Integer.MAX_VALUE);

        when(auctionHouse.auctionFor(item)).thenReturn(auction);
        launcher.joinAuction(item);

        verify(auctionHouse).auctionFor(item);
        verify(auction, times(1)).addAuctionEventListener(argThat(sniperForItem(item.identifier)));
        verify(collector, times(1)).addSniper(argThat(sniperForItem(item.identifier)));
        verify(auction, times(1)).join();;
    }


    private ArgumentMatcher<AuctionSniper> sniperForItem(String itemId) {
        return sniper -> sniper.getSnapshot().itemId() == itemId;
    }
}

package com.oscar.Unit.ui;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.oscar.SniperSnapshot;
import com.oscar.SniperState;
import com.oscar.ui.Column;

@RunWith(MockitoJUnitRunner.class)
public class ColumnTest {
    private SniperSnapshot snapshot = new SniperSnapshot("item id", 123, 456, SniperState.BIDDING);

    @Test public void
    gettingItemIdFromColumnSnapshot(){
        assertThat(Column.at(0).valueIn(snapshot).equals("item id"));
    }

    @Test public void
    gettingLastPriceFromColumnSnapshot(){
        assertThat(Column.at(1).valueIn(snapshot).equals(123));
    }

    @Test public void
    gettingLastBidFromColumnSnapshot(){
        assertThat(Column.at(0).valueIn(snapshot).equals(456));
    }

    @Test public void
    gettingStatedFromColumnSnapshot(){
        assertThat(Column.at(0).valueIn(snapshot).equals(SniperState.BIDDING));
    }
}

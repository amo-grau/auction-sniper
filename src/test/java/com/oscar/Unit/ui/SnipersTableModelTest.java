package com.oscar.Unit.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.ignoreStubs;

import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;

import com.oscar.ui.Column;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.objogate.exception.Defect;
import com.oscar.Auction;
import com.oscar.AuctionSniper;
import com.oscar.Item;
import com.oscar.SniperSnapshot;
import com.oscar.SniperState;
import com.oscar.ui.SnipersTableModel;

@RunWith(MockitoJUnitRunner.class)
public class SnipersTableModelTest {
    @Mock
    private TableModelListener listener;
    @Mock 
    private Auction auction;
    
    @InjectMocks
    private SnipersTableModel model;

    @Before public void attachModelListener(){
        model.addTableModelListener(listener);
    }

    @Test public void
    hasEnoughColumns(){
        assertThat(model.getColumnCount()).isEqualTo(com.oscar.ui.Column.values().length);
    }

    @Test public void
    setsSniperValuesInColumns() {
        SniperSnapshot joining = SniperSnapshot.joining("item id");
        SniperSnapshot bidding = joining.bidding(555,  666);
        model.sniperAdded(new AuctionSniper(new Item(joining.itemId(), Integer.MAX_VALUE), auction));
        verify(listener, atLeastOnce()).tableChanged(argThat(anyInsertionEvent()));

        model.sniperStateChanged(bidding);

        assertRowMatchesSnapshot(0, bidding);
        verify(listener, times(1)).tableChanged(argThat(aChangeInRow(0)));
    }

    @Test public void 
    setsUpColumnHeadings(){
        for (Column column: Column.values()){
            assertEquals(column.name, model.getColumnName(column.ordinal()));
        }
    }

    @Test public void 
    notifiesListenersWhenAddingASniper() {
        SniperSnapshot joining = SniperSnapshot.joining("item123");
        assertEquals(0, model.getRowCount());

        model.sniperAdded(new AuctionSniper(new Item(joining.itemId(), Integer.MAX_VALUE), auction));

        assertEquals(1, model.getRowCount());
        assertRowMatchesSnapshot(0, joining);

        verify(listener, times(1)).tableChanged(argThat(anInsertionAtRow(0)));
    }

    @Test public void
    holdsSnipersInAdditionOrder(){
        ignoreStubs(listener);

        model.sniperAdded(new AuctionSniper(new Item("item 0", Integer.MAX_VALUE), auction));
        model.sniperAdded(new AuctionSniper(new Item("item 1", Integer.MAX_VALUE), auction));

        assertEquals("item 0", cellValue(0, Column.ITEM_IDENTIFIER));
        assertEquals("item 1", cellValue(1, Column.ITEM_IDENTIFIER));
    }

    @Test public void
    updatesCorrectRowForSniper(){
        SniperSnapshot joining1 = SniperSnapshot.joining("item 1");
        SniperSnapshot bidding1 = joining1.bidding(555, 666);

        model.sniperAdded(new AuctionSniper(new Item("item 1", Integer.MAX_VALUE), auction));
        model.sniperAdded(new AuctionSniper(new Item("item 2", Integer.MAX_VALUE), auction));
        
        model.sniperStateChanged(bidding1);

        assertRowMatchesSnapshot(0, bidding1);
    }

    @Test(expected=Defect.class) public void
    throwsDefectIfNoExistingSniperForAnUpdate(){
        model.sniperStateChanged(new SniperSnapshot("item 1", 123, 234, SniperState.WINNING));
    }

    private void assertRowMatchesSnapshot(int row, SniperSnapshot snapshot) {
        assertEquals(snapshot.itemId(), cellValue(row, Column.ITEM_IDENTIFIER));
        assertEquals(snapshot.lastPrice(), cellValue(row, Column.LAST_PRICE));
        assertEquals(snapshot.lastBid(), cellValue(row, Column.LAST_BID));
        assertEquals(SnipersTableModel.textFor(snapshot.state()), cellValue(row, Column.SNIPER_STATE));
    }

    private Object cellValue(int rowIndex, Column column) {
        return model.getValueAt(rowIndex, column.ordinal());
    }

    private ArgumentMatcher<TableModelEvent> anInsertionAtRow(final int row) {
        return actualEvent -> 
            actualEvent.getSource() == model && 
            actualEvent.getFirstRow() == row &&
            actualEvent.getLastRow() == row &&
            actualEvent.getType() == TableModelEvent.INSERT;
    }

    private ArgumentMatcher<TableModelEvent> aChangeInRow(final int row) {
        return actualEvent -> 
            actualEvent.getSource() == model && 
            actualEvent.getFirstRow() == row &&
            actualEvent.getLastRow() == row &&
            actualEvent.getType() == TableModelEvent.UPDATE;
    }

    private ArgumentMatcher<TableModelEvent> anyInsertionEvent() {
        return actualEvent -> 
            actualEvent.getSource() == model && 
            actualEvent.getType() == TableModelEvent.INSERT;
    }
}

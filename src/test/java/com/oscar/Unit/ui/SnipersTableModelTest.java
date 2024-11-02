package com.oscar.Unit.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.argThat;


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

import com.oscar.SniperSnapshot;
import com.oscar.SniperState;
import com.oscar.ui.SnipersTableModel;

@RunWith(MockitoJUnitRunner.class)
public class SnipersTableModelTest {
    @Mock
    private TableModelListener listener;

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
        model.sniperStateChanged(new SniperSnapshot("item id", 555, 666, SniperState.BIDDING));

        assertColumnEquals(Column.ITEM_IDENTIFIER, "item id");
        assertColumnEquals(Column.LAST_PRICE, 555);
        assertColumnEquals(Column.LAST_BID, 666);
        assertColumnEquals(Column.SNIPER_STATE, SnipersTableModel.textFor(SniperState.BIDDING));

        verify(listener, times(1)).tableChanged(argThat(aRowChangedEvent(model)));
    }

    @Test public void 
    setsUpColumnHeadings(){
        for (Column column: Column.values()){
            assertEquals(column.name, model.getColumnName(column.ordinal()));
        }
    }

    private void assertColumnEquals(Column column, Object expected){
        final int rowIndex = 0;
        final int columnIndex = column.ordinal();

        assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
    }

    private ArgumentMatcher<TableModelEvent> aRowChangedEvent(SnipersTableModel source) {
        return actualEvent ->
            actualEvent.getSource() == source &&
            actualEvent.getFirstRow() == 0 &&
            actualEvent.getLastRow() == 0 &&
            actualEvent.getType() == TableModelEvent.UPDATE;
    }
}

package com.oscar.ui;

import javax.swing.table.AbstractTableModel;

import com.oscar.SniperListener;
import com.oscar.SniperSnapshot;
import com.oscar.SniperState;

public class SnipersTableModel extends AbstractTableModel implements SniperListener{
    private final static SniperSnapshot STARTING_UP = SniperSnapshot.joining("");
    private final static String[] STATUS_TEXT = { "Joining", "Bidding", "Winning", "Lost", "Won" };
    
    private SniperSnapshot snapshot = STARTING_UP;

    @Override
    public int getRowCount() {
        return 1;
    }
    
    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshot);
    }

    @Override
    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        snapshot = newSnapshot;
        fireTableRowsUpdated(0, 0);
    }

    @Override
    public String getColumnName(int column){
        return Column.at(column).name;
    }

    public static String textFor(SniperState state){
        return STATUS_TEXT[state.ordinal()];
    }
}

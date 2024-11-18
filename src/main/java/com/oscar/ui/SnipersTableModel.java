package com.oscar.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.oscar.AuctionSniper;
import com.oscar.PortfolioListener;
import com.oscar.SniperListener;
import com.oscar.SniperSnapshot;
import com.oscar.SniperState;
import com.objogate.exception.Defect;

public class SnipersTableModel extends AbstractTableModel implements SniperListener, PortfolioListener{
    private final static String[] STATUS_TEXT = { "Joining", "Bidding", "Winning", "Loosing", "Lost", "Won" };
    
    private List<SniperSnapshot> snapshots = new ArrayList<SniperSnapshot>();

    @Override
    public int getRowCount() {
        return snapshots.size();
    }
    
    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshots.get(rowIndex));
    }

    @Override
    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        int row = rowMatching(newSnapshot);
        snapshots.set(row, newSnapshot);
        fireTableRowsUpdated(row, row);
    }

    private int rowMatching(SniperSnapshot snapshot) {
        for (int i = 0; i < snapshots.size(); i++){
            if (snapshot.isForSameItemAs(snapshots.get(i))){
                return i;
            }
        }

        throw new Defect("Cannot find match for " + snapshot);
    }

    @Override
    public String getColumnName(int column){
        return Column.at(column).name;
    }
    
    public static String textFor(SniperState state){
        return STATUS_TEXT[state.ordinal()];
    }
    
    private void addSniperSnapshot(SniperSnapshot newSnapshot){
        snapshots.add(newSnapshot);
        int row = snapshots.size() - 1;
        fireTableRowsInserted(row, row);
    }

    @Override
    public void sniperAdded(AuctionSniper sniper) {
        addSniperSnapshot(sniper.getSnapshot());
        sniper.addSniperListener(new SwingThreadSniperListener(this));    
    }
}

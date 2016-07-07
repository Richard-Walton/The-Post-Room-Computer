package uk.ac.hud.postroom.ui.table;

import uk.ac.hud.postroom.computer.*;
import uk.ac.hud.postroom.event.*;

import java.util.*;

/**
 * Super class for all Log table models - on computer reset the data is cleared
 * @author Richard Walton (c0410542@hud.ac.uk);
 */
public class ComputerLogTableModel extends SimpleTableModel {
    
    protected List<Object[]> dataModel;
    
    public ComputerLogTableModel(String[] columnNames, Computer computer) {
        super(columnNames);
        
        dataModel = new Vector<Object[]>();
        
        // Add listener to clear data on computer reset
        computer.addComputerListener(new ComputerAdapter() {
            public void computerReset(Computer computer) {
                dataModel.clear();
                
                fireTableDataChanged();
            }
        });
    }

    /** @inheritDoc **/
    public int getRowCount() {
        return dataModel.size();
    }

    /** @inheritDoc **/
    public Object getValueAt(int row, int column) {
        return dataModel.get(row)[column];
    }
    
    /** @inheritDoc **/
    @Override public boolean isCellEditable(int row, int column) {
        return false;
    }
    
    /** @inheritDoc **/
    @Override public Class getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }
}
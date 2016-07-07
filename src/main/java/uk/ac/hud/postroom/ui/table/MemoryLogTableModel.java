package uk.ac.hud.postroom.ui.table;

import uk.ac.hud.postroom.computer.*;
import uk.ac.hud.postroom.event.*;

/**
 * TableModel which stores the state of the Memory
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class MemoryLogTableModel extends ComputerLogTableModel  {
    
    /**
     * Constructs a new MemoryLogTableModel for the given memory
     * @param memory Memory to log
     */
    public MemoryLogTableModel(Memory memory) { 
        super(new String[] { "IOType", "Address", "Value"}, memory.getComputer());
        
        // listene for memory events and add to data model
        memory.addMemoryListener(new MemoryAdapter() {
            public void memoryWroteTo(Memory memory, int address, String value) {
                dataModel.add(new String[] {"Write", Integer.toString(address), value });
                
                // inform listeners of new data
                fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
            }
            
            public void memoryReadFrom(Memory memory, int address, String value) {
                dataModel.add(new String[] {"Read", Integer.toString(address), value });
                
                // inform listeners of new data
                fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
            }
        });
    }
}
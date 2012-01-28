package uk.ac.hud.postroom.ui.assembler;

import uk.ac.hud.postroom.*;

import uk.ac.hud.postroom.ui.table.*;

import java.awt.event.*;
import javax.swing.*;

public class InstructionTable extends JTable {
    
    public InstructionTable(final Instruction[] instructions) {
        super(new InstructionTableModel(instructions));
        
        // Allows only one row to be selected at any one time
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Disable re-ordering of columns
        getTableHeader().setReorderingAllowed(false);
        
        // Sets the preferred size of the table when placed in a JScollPane
        setPreferredScrollableViewportSize(getPreferredSize());
        
        getColumnModel().getColumn(1).setCellRenderer(new SourceCodeCellRenderer());
        
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() > 1) {
                    int selectedIndex = getSelectedRow();
                    if(selectedIndex > -1) {
                        StatementInfoDialog.showDialog(
                                InstructionTable.this, instructions[selectedIndex]);
                    }
                }
            }
        });
    }
}
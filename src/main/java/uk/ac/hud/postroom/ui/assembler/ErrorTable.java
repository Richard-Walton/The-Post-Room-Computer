package uk.ac.hud.postroom.ui.assembler;

import uk.ac.hud.postroom.exception.*;

import uk.ac.hud.postroom.ui.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ErrorTable extends JTable {
    
    public ErrorTable(final AssemblyError[] errors) {
        super(new ErrorTableModel(errors));
        
        // Allows only one row to be selected at any one time
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Disable re-ordering of columns
        getTableHeader().setReorderingAllowed(false);
        
        // Sets the preferred size of the table when placed in a JScollPane
        setPreferredScrollableViewportSize(new Dimension(0, 0));
        
        setFillsViewportHeight(true);
        
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() > 1) {
                    int selectedIndex = getSelectedRow();
                    if(selectedIndex > -1) {
                        ErrorInfoDialog.showDialog(
                                ErrorTable.this, errors[selectedIndex]);
                    }
                }
            }
        });
    }
}
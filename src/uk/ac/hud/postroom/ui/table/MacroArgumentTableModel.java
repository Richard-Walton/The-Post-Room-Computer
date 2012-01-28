package uk.ac.hud.postroom.ui.table;

import javax.swing.table.*;

/**
 * TableModel for displaying list of arguments in a macro
 * @author richard
 */
public class MacroArgumentTableModel extends DefaultTableModel {
    
    /**
     * Constructs a new MacroArgumentTableModel
     */
    public MacroArgumentTableModel() {
        super(new String[] { "Argument Number", "Argument Label"}, 0);
    }
    
    /** @inheritDoc **/
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /** @inheritDoc **/
    public Object getValueAt(int row, int column) {
        switch(column) {
            case 0 : return row;
            default : return super.getValueAt(row, column);
        }
    }
}
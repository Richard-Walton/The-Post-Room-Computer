package uk.ac.hud.postroom.ui.table;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.computer.*;
import uk.ac.hud.postroom.event.*;

/**
 * Tabel model which stores the state of the Flag register
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class FlagLogTableModel extends ComputerLogTableModel {
    
    /**
     * Create new FlagLogTableModel using the given register store
     * @param registers RegisterStore containing the flag register
     */
    public FlagLogTableModel(RegisterStore registers) {
        super(new String[] {
            "Mem", "Ovflw", "Not", "Cry", "Neg", "Zero" }, 
            registers.getComputer());
        
        // Listen for changes to the Flag register and add to dataModel
        registers.addRegisterListener(new RegisterAdapter() {
            public void registerWroteTo(RegisterStore registers, Register register, String value) {
                if(register == Register.FLG) {
                    Flag flag = new Flag(value);
                    dataModel.add(new Boolean[] {
                        flag.isMemory(), 
                        flag.isOverflow(),
                        flag.isNot(),
                        flag.isCarry(),
                        flag.isNegative(),
                        flag.isZero()});
                    
                    // inform listeners of new data
                    fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
                }
            }
        });
    }
}
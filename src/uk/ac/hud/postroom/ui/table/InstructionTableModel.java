package uk.ac.hud.postroom.ui.table;

import uk.ac.hud.postroom.*;
import javax.swing.table.*;

/**
 * TabelModel of a list of Instructions
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class InstructionTableModel extends SimpleTableModel {
    
    // Instructions being displayed
    private Instruction[] instructions;
    
    /**
     * Constructs a new InstructionTableModel using the given instructions
     * @param instructions Instructions to be displayed
     */
    public InstructionTableModel(Instruction[] instructions) {
        // Super-class (SimpleTableModel) constructor
        super(new String[] {"Address", "Source Code", "Instruction"});
        
        this.instructions = instructions;        
    }
    
    /** @inheritDoc **/
    public int getRowCount() {
        return instructions.length;
    }    
        
    /** @inheritDoc **/
    @Override public boolean isCellEditable(int row, int column){
        return false;
    }
    
    /** @inheritDoc **/
    public Object getValueAt(int row, int column) {
        switch(column) {
            case 0 : return instructions[row].getMemoryLocation();
            case 1 : return instructions[row].getSourceCode();
            case 2 : return instructions[row].getInstruction();
            default : return null;
        }
    }
}
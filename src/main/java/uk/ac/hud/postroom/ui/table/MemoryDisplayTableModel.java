package uk.ac.hud.postroom.ui.table;

import uk.ac.hud.postroom.computer.*;
import uk.ac.hud.postroom.event.*;

import javax.swing.table.*;

/**
 * TabelModel to display the memory
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class MemoryDisplayTableModel extends SimpleTableModel {
    
    /** Constant field for displaying the memory data in Hexadecimal form **/
    public static final int HEXADECIMAL = 16;
    
    /** Constant field for displaying the memory data in Decimal form **/
    public static final int DECIMAL = 10;
    
    /** Constant field for displaying the memory data in Octal form **/
    public static final int OCTAL = 8;
    
    /** Constant field for displaying the memory data in Binary form **/
    public static final int BINARY = 2;
    
    /** Constant field for displaying the memory data in Acsii form **/
    public static final int ACSII = 0;
    
    /** Constant field for displaying the raw memory data**/
    public static final int RAW = -1;
    
    // Memory being displayed
    private Memory memory;

    // Type of model being displayed
    private int modelType;
    
    /**
     * Constructs a new MemoryDisplayTableModel for Decimal ouput using the given memory
     * @param memory Memory to be displayed
     */
    public MemoryDisplayTableModel(Memory memory) {
        this(memory, RAW);
    }
    
    /**
     * Constructs a new MemoryDisplayTableModel using the given memory and model type
     * @param memory Memory to be displayed
     * @param modelType Type of model 
     */
    public MemoryDisplayTableModel(Memory memory, int modelType) {
        super(new String[] { "Address", "Value"});
        
        this.memory = memory;
        this.modelType = modelType;
        
        // listen for memory events and update information accordingly
        memory.addMemoryListener(new MemoryAdapter() {
            public void memoryWroteTo(Memory memory, int address, String value) {
                // inform listeners of data change
                fireTableRowsUpdated(address, address);
            }
        });
        
        // Reload memory on computer reset
        memory.getComputer().addComputerListener(new ComputerAdapter() {
            public void computerReset(Computer computer) {
                fireTableDataChanged();
            }
        });
    }
    
    /**
     * Sets the model to output a different type of data (see constant fields)
     * @param modelType New Model type
     */
    public void setModelType(int modelType) {
        switch(modelType) {
            case HEXADECIMAL : 
            case DECIMAL :
            case OCTAL :
            case BINARY :
            case ACSII :
            case RAW :
                this.modelType = modelType;
                
                // inform listeners of data change
                fireTableDataChanged();
                break;
            default :
                throw new IllegalArgumentException("Invalid model type");
        }
    }

    /** @inheritDoc **/
    public int getRowCount() {
        return memory.size();
    }
    
    /** @inheritDoc **/
    @Override public boolean isCellEditable(int column, int row) {
        return false;
    }

    /** @inheritDoc **/
    public Object getValueAt(int row, int column) {
        switch(column) {
            case 0 : return row;
            case 1 : 
                // Get memory value
                int memoryValue = Integer.parseInt(memory.get(row));
                switch(modelType) {
                    case ACSII :
                        // Return ascii character if value is within ascii range
                        if(memoryValue > -1 && memoryValue < 128) {
                            return (char) memoryValue;
                        }else {
                            // otherwise just return decimal value
                            return memoryValue;
                        }
                        
                    case RAW : 
                        // Returns the memory as is seen in memory
                        return memory.get(row);
                        
                    default :
                        // Return data using the model type as the base
                        return Integer.toString(memoryValue, modelType);
                }
            default :
                return null;
        }
    }
}
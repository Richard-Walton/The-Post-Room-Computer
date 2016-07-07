package uk.ac.hud.postroom.ui.table;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.computer.*;
import uk.ac.hud.postroom.event.*;

/**
 * Table Model which stores the state of the InstructionDecoder
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class InstructionDecoderLogTableModel extends ComputerLogTableModel {
    
    /**
     * Constructs a new InstructionDecoderLogTableModel
     * @param instructionDecoder InstructionDecoder to log
     */
    public InstructionDecoderLogTableModel(InstructionDecoder instructionDecoder) {        
        super(new String[] { 
            "Number", "Instruction", "Type"}, 
            instructionDecoder.getComputer());
        
        // listen for instruction decoder events and add to data model
        instructionDecoder.addDecoderListener(new InstructionDecoderListener() {
            public void instructionDecoded(InstructionDecoder decoder, String instruction) {
                String[] data = new String[3];
                data[0] = Integer.toString(getRowCount());
                data[1] = instruction;
                try {
                    // Get instruction type
                    data[2] = OpCode.getByValue(instruction.substring(0, 1)).name();
                }catch (Exception e) {
                    e.printStackTrace(); // shouldn't happen so print stack trace!
                    data[2] = "Invalid instruction";
                }
                
                dataModel.add(data);
                
                // inform listeners of new data
                fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
            }
            
            public void decodeError(InstructionDecoder decoder, String instruction, Throwable error) {
                String[] data = new String[3];
                data[0] = Integer.toString(getRowCount());
                data[1] = instruction;
                data[2] = "Invalid instruction";
                
                dataModel.add(data); 
                
                // inform listeners of new data
                fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
            }
        });
    }
}
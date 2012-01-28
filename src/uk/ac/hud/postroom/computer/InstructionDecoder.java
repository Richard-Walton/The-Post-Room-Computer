package uk.ac.hud.postroom.computer;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.event.*;

import java.util.*;

/**
 * Instruction Decoder component of the Post Room Computer
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class InstructionDecoder extends ComputerComponent {
    
    // Register store of the computer
    private RegisterStore registerStore;
    
    // Listener list
    private List<InstructionDecoderListener> listeners;
    
    /**
     * Constructs a new Instruction Decoder for the given computer
     * @param computer The computer which this instruction decoder is part of
     */
    public InstructionDecoder(Computer computer) {
        // Super-class (ComputerComponent) constructor
        super(computer);
        
        registerStore = computer.getRegisterStore();
        listeners = new ArrayList<InstructionDecoderListener>();
    }
    
    /**
     * Invokes the instruction decoder unit to read from the computer register store
     * and decoder the instruction stored in the IR register
     */
    protected void invoke() {
        // Read the instruction
        String instruction = registerStore.readFrom(Register.IR);
        
        try {        
            // Split instruction and store into relevent registers
            registerStore.writeTo(Register.OP, instruction.substring(0, 1));
            registerStore.writeTo(Register.AF1, instruction.substring(1, 4));
            registerStore.writeTo(Register.AF2, instruction.substring(4, 7));
            registerStore.writeTo(Register.AF3, instruction.substring(1, 4));
                
            // Inform listeners
            for(InstructionDecoderListener listener : getDecoderListeners()) {
                listener.instructionDecoded(this, instruction);
            }
        }catch (Exception e) {
            // Inform listeners of error during decoding
            for(InstructionDecoderListener listener : getDecoderListeners()) {
                listener.decodeError(this, instruction, new Exception("Malformed instruction"));
            }
        }
    }
    
    /**
     * Adds a listener for InstructionDecoder events
     * @param listener listener to add
     */
    public void addDecoderListener(InstructionDecoderListener listener) {
        if(listener != null) listeners.add(listener);
    }
    
    /**
     * Returns all InstructionDecoderListener which have been added to the Instruction Decoder
     * @return Array of Instruction Decoder Listeners
     */
    public InstructionDecoderListener[] getDecoderListeners() {
        return listeners.toArray(new InstructionDecoderListener[]{});
    }
    
    /**
     * Removes the given listener from the InstructionDecoder
     * @param listener listener to remove
     */
    public void removeDecoderListener(InstructionDecoderListener listener) {
        listeners.remove(listener);
    }
}
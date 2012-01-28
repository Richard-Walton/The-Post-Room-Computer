package uk.ac.hud.postroom.event;

import uk.ac.hud.postroom.computer.*;

/**
 * Listener for memory events
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public interface MemoryListener {
    
    /**
     * Called when the memory is read from
     * @param memory The memory which was read from
     * @param address Address which was read
     * @param value The value which was returned
     */
    public void memoryReadFrom(Memory memory, int address, String value);
    
    /**
     * Called when the memory was wrote to
     * @param memory The memory which was wrote to
     * @param address The address which was wrote to
     * @param value The value which was wrote
     */
    public void memoryWroteTo(Memory memory, int address, String value);
    
    /**
     * Called when an error occurs when memory is accessed (most likely due to
     * and out of bounds exception when attempting to access an invalid address) 
     * @param memory Memory which was accessed
     * @param error Error which occured
     */
    public void memoryError(Memory memory, Throwable error);
}
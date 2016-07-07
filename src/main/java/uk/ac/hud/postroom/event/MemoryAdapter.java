package uk.ac.hud.postroom.event;

import uk.ac.hud.postroom.computer.*;

/**
 * Listener adapter for memory events
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public abstract class MemoryAdapter implements MemoryListener {
    
    /** @inheritDoc **/
    public void memoryReadFrom(Memory memory, int address, String value) {}
    
    /** @inheritDoc **/
    public void memoryWroteTo(Memory memory, int address, String value) {}
    
    /** @inheritDoc **/
    public void memoryError(Memory memory, Throwable error) {}
}
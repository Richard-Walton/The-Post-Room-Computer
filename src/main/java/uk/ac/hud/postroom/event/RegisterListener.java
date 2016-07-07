package uk.ac.hud.postroom.event;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.computer.*;

/**
 * Listener for general register events
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public interface RegisterListener {
    
    /**
     * Called when a register is read from
     * @param registers The Register store which contains the register
     * @param register The Register which was read from
     * @param value The value which was stored in the Register
     */
    public void registerReadFrom(RegisterStore registers, Register register, String value);
    
    /**
     * Called when a register is wrote to
     * @param registers The Register store which contains the register
     * @param register The Register which was wrote to
     * @param value The value which was wrote to the Register
     */
    public void registerWroteTo(RegisterStore registers, Register register, String value);
}
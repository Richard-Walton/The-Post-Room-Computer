package uk.ac.hud.postroom.event;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.computer.*;

/**
 * Listener adapter for general register events
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public abstract class RegisterAdapter implements RegisterListener {
    
    /** @inheritDoc **/
    public void registerReadFrom(RegisterStore registers, Register register, String value){}
    
    /** @inheritDoc **/
    public void registerWroteTo(RegisterStore registers, Register register, String value){}
}
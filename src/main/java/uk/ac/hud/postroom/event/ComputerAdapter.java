package uk.ac.hud.postroom.event;

import uk.ac.hud.postroom.computer.*;

/**
 * Listener adapter for general computer events
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public abstract class ComputerAdapter implements ComputerListener {
    
    /** @inheritDoc **/
    public void computerStarted(Computer computer){}
    
    /** @inheritDoc **/
    public void computerStopped(Computer computer){}
    
    /** @inheritDoc **/
    public void computerReset(Computer computer) {}
    
    public void breakPointHit(Computer computer, int breakpoint){}
    
    /** @inheritDoc **/
    public void computerError(Computer computer, Throwable error){}
}
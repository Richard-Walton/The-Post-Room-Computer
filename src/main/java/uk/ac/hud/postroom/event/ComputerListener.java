package uk.ac.hud.postroom.event;

import uk.ac.hud.postroom.computer.*;

/**
 * Listener for general computer events
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public interface ComputerListener {
    /**
     * Called when the computer starts execution
     * @param computer Computer which started execution
     */
    public void computerStarted(Computer computer);
    
    /**
     * Called when the computer stops execution
     * @param computer Computer which stopped execution
     */
    public void computerStopped(Computer computer);
    
    /**
     * Called when the computer was reset
     * @param computer Computer which was reset
     */
    public void computerReset(Computer computer);
    
    public void breakPointHit(Computer computer, int breakpoint);
    
    /**
     * Called when a computer module encounteres an error
     * @param computer Computer which contains the module which encountered the error
     * @param error Error which occured
     */
    public void computerError(Computer computer, Throwable error);
}
package uk.ac.hud.postroom.computer;

/**
 * Enumeration of execution speeds which can be set to control the Post Room
 * Computer execution speed
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public enum ExecutionSpeed {
    
    /** Computer executes one instruction when prompted to **/
    STEP (0),
    
    /** Computer executes one instruction every 7 seconds **/
    SLOW (3000),
    
    /** Computer executes one instruction every 4 seconds **/
    MEDIUM (1000),
    
    /** Computer executes one instruction every second **/
    FAST (250),
    
    /** Computer executes one instruction as fast as the host CPU can **/
    FULL (-1);
    
    // Execution wait time
    private int pauseTime;
    
    /**
     * Constructs a new ExecutionSpeed enum with the given wait time
     * @param pauseTime Time (in milliseconds) to wait between execution instructions
     */
    private ExecutionSpeed(int pauseTime) {
        this.pauseTime = pauseTime;
    }
    
    /**
     * Returns the time (in milliseconds) the computer will wait between
     * executing instructions
     * @return the time the computer will wait between executing instructions
     */
    public int getPauseTime() {
        return pauseTime;
    }
}
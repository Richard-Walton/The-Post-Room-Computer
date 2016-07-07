package uk.ac.hud.postroom.computer;

/**
 * An external IO device which connects to the IOModule of the Post Room Computer
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public interface IODevice {
    
    /**
     * Called when the Post Room Computer needs input
     * @return External input
     */
    public String requestInput();
    
    /**
     * Called when the Post Room Compter needs to output information
     * @param output The information being output
     */
    public void showOutput(String output);
}
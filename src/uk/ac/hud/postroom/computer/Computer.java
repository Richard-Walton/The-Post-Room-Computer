package uk.ac.hud.postroom.computer;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.event.*;

/**
 * The Post Room Computer
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public interface Computer {
    
    /**
     * Returns the Arithmetic Logic Unit (ALU) of the Computer
     * @return Arithmetic Logic Unit (ALU) of the Computer
     */
    public ArithmeticLogicUnit getALU();
    
    /**
     * Returns the Instruction Decoder of the Computer
     * @return Instruction Decoder of the Computer
     */
    public InstructionDecoder getInstructionDecoder();
    
    /**
     * Returns the IO Module of the Computer
     * @return IO Module of the Computer
     */
    public IOModule getIOModule();
    
    /**
     * Returns the Memory of the Computer
     * @return Memory of the Computer
     */
    public Memory getMemory();
    
    /**
     * Returns the Register Store of the Computer
     * @return Register Store of the Computer
     */
    public RegisterStore getRegisterStore();
    
    /**
     * Sets the instructions to be executed by the computer
     * @param instructions instructions to be executed by the computer
     */
    public void setInstructions(Instruction[] instructions);

    public Instruction[] getInstructions();
    
    /**
     * Sets the execution speed of the Computer
     * @param executionSpeed Execution speed to set
     */
    public void setExecutionSpeed(ExecutionSpeed executionSpeed);
    
    public void setBreakPoints(int[] breakpoints);
    
    /**
     * Executes the current instrutions set by setInstructions(Instruction[])
     */
    public void execute();
    
    /** 
     * Resets the computer
     */
    public void reset();
    
    /**
     * Causes execution of the computer to resume
     */
    public void step();
    
    /**
     * Causes the execution of the Post Room Computer to stop
     */
    public void forceStop();
    
    /**
     * Adds a listener for computer events
     * @param listener listener to add 
     */
    public void addComputerListener(ComputerListener listener);
    
    /**
     * Returns all ComputerListeners which have been added to the computer
     * @return Array of computer Listeners
     */
    public ComputerListener[] getComputerListeners();
    
    /**
     * Removes the given listener from the computer
     * @param listener listener to remove
     */
    public void removeComputerListener(ComputerListener listener);   
}
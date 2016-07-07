package uk.ac.hud.postroom.computer;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.event.*;

import java.util.*;

/**
 * Abstract implementation of the Post Room Computer
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public abstract class AbstractComputer implements Computer {

    // Thread used to perform execution
    private Runnable executionThread;
    
    // Instructions to be executed
    private Instruction[] instructions;
    
    private int[] breakpoints;
    
    // Speed the computer will execution programs
    private ExecutionSpeed executionSpeed;
    
    /** Whether the computer should continue execution **/
    protected boolean running;
    
    /** The Arithmetic Logic Unit (ALU) of the computer **/
    protected ArithmeticLogicUnit alu;
    
    /** The InstructionDecoder of the computer **/
    protected InstructionDecoder instructionDecoder;
    
    /** The IOModule of the computer **/
    protected IOModule ioModule;
    
    /** The memory of the computer **/
    protected Memory memory;
    
    /** The register store of the computer **/
    protected RegisterStore registerStore;
    
    //Listener list
    private List<ComputerListener> listeners;
    
    /**
     * Constructs a new AbstractComputer
     */
    public AbstractComputer() {
        // Set execution thread to something to avoid null pointer exceptions
        executionThread = new Runnable() { public void run() {} };
        
        // Set default execution speed to FULL
        this.executionSpeed = ExecutionSpeed.MEDIUM;
        
        setBreakPoints(new int[]{});
        
        // Must construct listener list as all components use it
        listeners = new ArrayList<ComputerListener>(); 
        
        // Must construct register store first as other components use it
        registerStore = new RegisterStore(this);

        // Construct the rest of the computer modules
        alu = new ArithmeticLogicUnit(this);
        instructionDecoder = new InstructionDecoder(this);
        ioModule = new IOModule(this);
        memory = new Memory(this, 1000);
        
        running = false;
        
        // Add error listeners - forwards errors to general computer listeners
        instructionDecoder.addDecoderListener(new InstructionDecoderAdapter() {  
            public void decodeError(InstructionDecoder decoder, String instruction, Throwable error) {
                fireComputerError(error);
            }
        });
        
        memory.addMemoryListener(new MemoryAdapter() {
            public void memoryError(Memory memory, Throwable error) {
                fireComputerError(error);
            }
        });
    }

    /**
     * Returns the thread which is used to execute a program
     * @return thread which is used to execute a program
     */
    protected abstract Runnable getExecutionThread();

    /** @inheritDoc **/
    public ArithmeticLogicUnit getALU() {
        return alu;
    }

    /** @inheritDoc **/
    public InstructionDecoder getInstructionDecoder() {
        return instructionDecoder;
    }

    /** @inheritDoc **/
    public IOModule getIOModule() {
        return ioModule;
    }

    /** @inheritDoc **/
    public Memory getMemory() {
        return memory;
    }

    /** @inheritDoc **/
    public RegisterStore getRegisterStore() {
        return registerStore;
    }
    
    public int[] getBreakPoints() {
        return breakpoints;
    }
    
    /** @inheritDoc **/
    public void execute() {   
        reset();
        
        executionThread = getExecutionThread();
        
        fireComputerStarted();
        
        Thread thread = new Thread(executionThread);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
    
    /** @inheritDoc **/
    public void reset() { 
        fireComputerReset();
        
        if(instructions != null) {
            for(Instruction instruction : instructions) {
                memory.put(instruction.getMemoryLocation(), instruction.getInstruction());
            }
        }   
    }
    
    /**
     * Returns whether the computer should be / is running
     * @return true if the computer should be / is running
     */
    public boolean isRunning() {
        return running;
    }
    
    /** @inheritDoc **/
    public void setInstructions(Instruction[] instructions) {
        this.instructions = instructions;
    }

    public Instruction[] getInstructions() {
        return instructions;
    }
    
    public void setBreakPoints(int[] breakpoints) {
        this.breakpoints = breakpoints;
    }
    
    /** @inheritDoc **/
    public void setExecutionSpeed(ExecutionSpeed executionSpeed) {
        this.executionSpeed = executionSpeed;
        
        step(); // restarts execution at new speed
    }
    
    /**
     * Causes the Computer to wait a specific amount of time (set by setExecutionSpeed)
     * before continuing execution
     */
    protected void pauseExecution() {
        try {
            synchronized(executionThread) {
                if(executionSpeed != ExecutionSpeed.FULL) {
                    executionThread.wait(executionSpeed.getPauseTime());
                }
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /** @inheritDoc **/
    public void step() {
        synchronized(executionThread) {
            executionThread.notify();
        }
    }
    
    /** @inheritDoc **/
    public void forceStop() {
        running = false;
        
        fireComputerStopped();
    }

    /** @inheritDoc **/
    public void addComputerListener(ComputerListener listener) {
        if(listener != null) listeners.add(listener);
    }

    /** @inheritDoc **/
    public ComputerListener[] getComputerListeners() {
        return listeners.toArray(new ComputerListener[]{});
    }

    /** @inheritDoc **/
    public void removeComputerListener(ComputerListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Informs attached listeners that the computer has started execution
     */
    protected void fireComputerStarted() {
        running = true;
        
        for(ComputerListener listener : getComputerListeners()) {
            listener.computerStarted(this);
        }
    }
    
    /**
     * Informs attached listeners that the computer has stopped execution
     */
    protected void fireComputerStopped() {
        running = false;
        
        for(ComputerListener listener : getComputerListeners()) {
            listener.computerStopped(this);
        }
    }
    
    /**
     * Informs attached listeners that the computer has been reset
     */
    protected void fireComputerReset() {
        running = false;
        
        for(ComputerListener listener : getComputerListeners()) {
            listener.computerReset(this);
        }
    }
    
    protected void fireBreakPointHit(int breakpoint) {
        
        for(ComputerListener listener : getComputerListeners()) {
            listener.breakPointHit(this, breakpoint);
        }
    }
    
    /**
     * Informs attached listeners that the computer has encountered the given
     * error and then forces the computer to stop
     * @param error Error which has occured
     */
    protected void fireComputerError(Throwable error) {
        running = false;
        
        for(ComputerListener listener : getComputerListeners()) {
            listener.computerError(this, error);
        }
    }
    /**
     * Increments the Program Counter by one
     */
    protected void incrementPC() {
        int pc = Integer.parseInt(registerStore.readFrom(Register.PC));
        
        pc++;
        
        setPC(pc);
    }
    
    /**
     * Sets the Program Counter
     * @param pc New value for the Program counter
     */
    protected void setPC(int pc) {
        registerStore.writeTo(Register.PC, Integer.toString(pc));
    }
    
    /**
     * Invokes the InstructionDecoder to decoder the next instruction
     */
    protected void fetchAndDecodeInstruction() {
        int nextInstruction = Integer.parseInt(registerStore.readFrom(Register.PC));
        registerStore.writeTo(Register.IR, readFromMemory(nextInstruction));
        
        instructionDecoder.invoke();
        
        for(int breakpoint : getBreakPoints()) {
            if(nextInstruction == breakpoint) {
                fireBreakPointHit(breakpoint);
                ioModule.getIODevice().showOutput("Hit breakpoint");
                setExecutionSpeed(ExecutionSpeed.STEP);
                pauseExecution();
            }
        }
    }
    
    /**
     * Invokes Memory to perform a read operation
     * @param address Address to read from
     * @return The value at the given address in memory
     */
    protected String readFromMemory(int address) {
        registerStore.writeTo(Register.MAR, Integer.toString(address));
        registerStore.writeTo(Register.MRW, Integer.toString(0));
        
        memory.invoke();
        
        return registerStore.readFrom(Register.MDR);
    }
    
    /**
     * Invokes Memory to perform a write operation
     * @param address Address to write to
     * @param value Value to write
     */
    protected void writeToMemory(int address, String value) {
        registerStore.writeTo(Register.MAR, Integer.toString(address));
        registerStore.writeTo(Register.MDR, value);
        registerStore.writeTo(Register.MRW, Integer.toString(1));
            
        memory.invoke(); 
    }
       
    /**
     * Invokes the IOModule to get input
     * @return Information recieved from the IODevice attached to the IOModule
     */
    protected String readInput() {
        registerStore.writeTo(Register.IOT, "0");
        
        ioModule.invoke();
        
        return registerStore.readFrom(Register.IOB);
    }
    
    /**
     * Invokes the IOModule to output the given information
     * @param output Information to output
     */
    protected void writeOutput(String output) {
        registerStore.writeTo(Register.IOT, "1");
        registerStore.writeTo(Register.IOB, output);
        
        ioModule.invoke();
    }
    
    /**
     * Returns whether the given condition matches the flag register
     * @param condition Condition to match
     * @return true if the condition matches
     */
    protected boolean conditionMatches(Condition condition) {
        Flag flag = new Flag(registerStore.readFrom(Register.FLG));

        boolean doJMP = false;
        
        switch(condition) {
            case NVR : // never, always return false
                doJMP = false;
                break;
                
            case LWY : // always, always return true
                doJMP = true;
                break;
                
            case LTZ : // less than zero
                doJMP = flag.isNegative();
                break;
                
            case LEZ : // less than or equal to zero
                doJMP = flag.isNegative() || flag.isZero();
                break;
                
            case EQZ : // equal to zero
                doJMP = flag.isZero();
                break;
                
            case NEZ : // not equal to zero
                doJMP = !flag.isZero();
                break;
                       
            case GEZ : // greater or equal to zero
                doJMP = !flag.isNegative() || flag.isZero();
                break;
                
            case GTZ : // greater than zero
                doJMP = !flag.isNegative() && !flag.isZero();
                break;
                
            case CRY : // carry 
                doJMP = flag.isCarry();
                break;
                
            case NCRY : // not carry
                doJMP = !flag.isCarry();
                break;
                
            case VFL : // overflow
                doJMP = flag.isOverflow();
                break;
                
            case NVFL : // not overflow
                doJMP = !flag.isOverflow();
                break;
                
            case MEM : // memory 
                doJMP = flag.isMemory();
                break;
                
            case NMEM : // not memory
                doJMP = !flag.isMemory();
                break;                
        }
        // If the 'not' bit is set on the flag, invert the result
        if(flag.isNot()) {
            return !doJMP;
        }
        
        return doJMP;
    }
}
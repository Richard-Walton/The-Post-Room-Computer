package uk.ac.hud.postroom.computer;

import uk.ac.hud.postroom.*;

/**
 * Absolute Address Post Room Computer implementation
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class AbsoluteAddressComputer extends AbstractComputer {
    
    // The thread used to execute a postroom computer
    private Runnable executionThread;
    
    /** 
     * Constructs a new Absolute 2-Address Post Room Computer
     */
    public AbsoluteAddressComputer() {
        /* The execution thread of the computer
         * This thread controls the Post Room Computer and contains
         * the core functionality of the Computer */
        executionThread = new Runnable() {
            public void run() {
                
                while(isRunning()) {                    
                    
                    // Fetches the next instruction and decodes it into Registers
                    fetchAndDecodeInstruction();
                    
                    // Increments the Program Counter
                    incrementPC();
                    
                    // Perform action based on data in the Operator Port register
                    switch(OpCode.getByValue(registerStore.readFrom(Register.OP))) {
                        // Halt computer
                        case HLT :
                            // forces execution to stop
                            forceStop(); 
                            return;
                            
                        // Input instruction
                        case INP :   
                            // Reads input from the IOModule
                            String input = readInput();
                    
                            // Writes input to specified memory location
                            writeToMemory(Integer.parseInt(
                                    registerStore.readFrom(Register.AF1)), input);
                            break;
                    
                        // Output instruction
                        case OUT :
                            // Writes output through the IOModule
                            writeOutput(readFromMemory(Integer.parseInt(
                                    registerStore.readFrom(Register.AF1))));
                            break;
                    
                        /* Move, Mask, Addition, Substraction, Shift, and Move Effective
                         * Address operations are handled by the ALU */
                        case MOV :
                        case MSK :                   
                        case ADD : 
                        case SUB :  
                        case MEA :
                        case SHF :
                            /* Important registers used by the ALU must have
                             * instructional data stored in them before the ALU
                             * can be invoked */
                            
                            // Read the first operand of the instruction
                            String xAddress = registerStore.readFrom(Register.AF1);
                            
                            // Read the second operand of the instruction
                            String yAddress = registerStore.readFrom(Register.AF2);
        
                            // Write the xAddress
                            registerStore.writeTo(Register.XA, xAddress);
                            
                            /* Write the xAddressType (Always memory in the Absolute
                             * Address machine */
                            registerStore.writeTo(Register.XAT, "0");
                            
                            // Writes the value stored at the xAddress
                            registerStore.writeTo(Register.XV, 
                                    readFromMemory(Integer.parseInt(xAddress)));
                            
                            // Write the yAddress
                            registerStore.writeTo(Register.YA, yAddress);
                            
                            /* Write the yAddressType (Always memory in the Absolute
                             * Address machine */
                            registerStore.writeTo(Register.YAT, "0");
                            
                            // Writes the value stored at the yAddress
                            registerStore.writeTo(Register.YV, 
                                    readFromMemory(Integer.parseInt(yAddress)));

                            /* Writes the destination address (same as the xAddress
                             * as this is a 2-address implementation */
                            registerStore.writeTo(Register.ZA, xAddress);
                            
                            /* Write the zAddressType (Always memory in the Absolute
                             * Address machine */
                            registerStore.writeTo(Register.ZAT, "0");
                            
                            // Invokes the ALU to perform the operation
                            alu.invoke();    
                    
                            // Writes the result to memory
                            writeToMemory(
                                    Integer.parseInt(registerStore.readFrom(Register.ZA)), 
                                    registerStore.readFrom(Register.ZV));
                            break;
                    
                        // Jump instruction
                        case JMP : 
                            // Gets the machine code representation of the condition
                            String condition = registerStore.readFrom(Register.AF1);
                            
                            // Checks if the condition evalutes to true
                            if(conditionMatches(Condition.getByValue(condition))) {
                                // Jumps the Program counter to the specified address
                                setPC(Integer.parseInt(registerStore.readFrom(Register.AF2)));
                            }
                            break;
                    }
                    
                    // Pauses the computer for a specified amount of time
                    pauseExecution();
                }
            }
        };
    }
    
    /** @inheritDoc **/
    protected Runnable getExecutionThread() {
        return executionThread;
    }
}
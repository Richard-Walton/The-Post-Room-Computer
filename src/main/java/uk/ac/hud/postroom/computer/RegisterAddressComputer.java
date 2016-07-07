package uk.ac.hud.postroom.computer;

import uk.ac.hud.postroom.*;

/**
 * Absolute Address Post Room Computer implementation
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class RegisterAddressComputer extends AbstractComputer {
    
    // The thread used to execute a postroom computer program
    private Runnable executionThread;
    
    /** 
     * Constructs a new Register 2-Address Post Room Computer
     */
    public RegisterAddressComputer() {
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
                            doINP(registerStore.readFrom(Register.AF1));
                            break;
                    
                        // Output instruction
                        case OUT :
                            doOUT(registerStore.readFrom(Register.AF1));
                            break;
                    
                        /* Move, Mask, Addition, Substraction, and Move Effective
                         * Address operations are handled by the ALU */
                        case MOV :
                        case MSK :                   
                        case ADD : 
                        case SUB :  
                        case MEA :
                        case SHF :
                            invokeALU();
                            break;
                        // Jump instruction
                        case JMP : 
                            // Gets the machine code representation of the condition
                            String condition = registerStore.readFrom(Register.AF1);
                            
                            // Checks if the condition evalutes to true
                            if(conditionMatches(Condition.getByValue(condition))) {
                                // Jumps the Program counter to the specified address
                                setPC(Integer.parseInt(getValue(registerStore.readFrom(Register.AF2), true)));
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
    
    /**
     * Perfoms and input operation based on the given Register operand
     * @param operand Machine code register operand
     */
    private void doINP(String operand) {
        
        // Gets address mode
        AddressMode addressMode = getAddressMode(operand);
        
        // Variable maybe RegisterID or absolute value
        String variable = operand.substring(0, 2);
                            
        switch(addressMode) {
            // Special cases IMMEDIATE_DIRECT, REGISTER_DIRECT
            case IMMEDIATE_DIRECT :
                // Invalid - can't use this address mode for input 
                fireComputerError(new Exception("Invalid address mode for input address"));
                break;
            
            case REGISTER_DIRECT :
                // variable is Register - read from iomodule and write directly register
                registerStore.writeTo(Register.getByRegisterID(variable), readInput());
                break;
                
            case POSTINCREMENT_DIRECT :
            case POSTINCREMENT_INDIRECT :
                writeToMemory(getEffectiveMemoryAddress(operand, false), readInput());
                getEffectiveMemoryAddress(operand, true);
                break;
            
            default :
                // All other register modes write to memory
                writeToMemory(getEffectiveMemoryAddress(operand, true), readInput());
                break;
        }      
    }
    
    private void doOUT(String operand) {
        
        // Gets address mode
        AddressMode addressMode = getAddressMode(operand);
        
        switch(addressMode) {
            case POSTINCREMENT_DIRECT :
            case POSTINCREMENT_INDIRECT :
                writeOutput(getValue(operand, false));
                getValue(operand, true);
                break;
            default :
                writeOutput(getValue(operand, true));
        }
    }
    
    /**
     * Invokes the ALU
     */
    private void invokeALU() {
        // Read the first operand of the instruction
        String xAddress = registerStore.readFrom(Register.AF1);
        
        // AddressMode of the xOperand
        AddressMode xAddressMode = getAddressMode(xAddress);
        
        // Set the address type
        String xAddressType;
        switch(xAddressMode) {
            case IMMEDIATE_DIRECT :
                xAddressType = "2";
                break;
            case IMMEDIATE_INDIRECT :
                xAddressType = "0";
                break;
            default :
                xAddressType = "1";
        }
        
        // Get the address value
        String xValue;
        switch(xAddressMode) {
            case POSTINCREMENT_DIRECT :
            case POSTINCREMENT_INDIRECT :
                xValue = getValue(xAddress, false);
                break;
            default : 
                xValue = getValue(xAddress, true);
        }
        
        //-------------------------------
        
        // Read the second operand of the instruction
        String yAddress = registerStore.readFrom(Register.AF2);
        
        // AddressMode of the yOperand
        AddressMode yAddressMode = getAddressMode(yAddress);
        
        // Set the address type
        String yAddressType;
        switch(yAddressMode) {
            case IMMEDIATE_DIRECT :
                yAddressType = "2";
                break;
            case IMMEDIATE_INDIRECT :
                yAddressType = "0";
                break;
            default :
                yAddressType = "1";
        }
        
        // Get the address value
        String yValue = getValue(yAddress, true);
        
        //----------------------------
        
        // Two address implementation - so most zValues are copied from xValues
        
        String zAddress = xAddress;
        
        AddressMode zAddressMode = xAddressMode;
        String zAddressType;
        
        switch(zAddressMode) {
            case IMMEDIATE_DIRECT :
                fireComputerError(new Exception("Invalid address mode for return address"));
                return;
            default :
                zAddressType = xAddressType;
        }
        
        //---------Write values--------
        registerStore.writeTo(Register.XA, xAddress);
        registerStore.writeTo(Register.XAT, xAddressType);
        registerStore.writeTo(Register.XV, xValue);
        
        registerStore.writeTo(Register.YA, yAddress);
        registerStore.writeTo(Register.YAT, yAddressType);
        registerStore.writeTo(Register.YV, yValue);
        
        registerStore.writeTo(Register.ZA, zAddress);
        registerStore.writeTo(Register.ZAT, zAddressType); 
        
        //-----------------------------
        
        alu.invoke();
        
        String zValue = registerStore.readFrom(Register.ZV);
        
        switch(zAddressMode) {
            case REGISTER_DIRECT :
                registerStore.writeTo(getRegister(zAddress), zValue);
                break;
                
            case REGISTER_INDIRECT :
                writeToMemory(Integer.parseInt(
                        registerStore.readFrom(getRegister(zAddress))), zValue);
                break;

            case POSTINCREMENT_DIRECT :
            case POSTINCREMENT_INDIRECT :
                writeToMemory(getEffectiveMemoryAddress(zAddress, false), zValue);
                getEffectiveMemoryAddress(zAddress, true);
                break;
            
            default :
                
                // All other register modes write to memory
                writeToMemory(getEffectiveMemoryAddress(zAddress, false), zValue);
                break;
        }   
    }
    
    /**
     * Returns the Register part of the given machine code register operand - 
     * Machine Code register operands are in the form of [Register|AddressMode]
     * @param registerOperand Machine code register operand
     * @return The register specified by the given register operand
     */
    private Register getRegister(String registerOperand) {
        return Register.getByRegisterID(registerOperand.substring(0, 2));
    }
    
    /**
     * Returns the AddressMode part of the given machine code register operand -
     * Machine Code register operands are in the form of [Register|AddressMode]
     * @param registerOperand Machine code register operand
     * @return The AddressMode specified by the given register operand
     */
    private AddressMode getAddressMode(String registerOperand) {
        return AddressMode.getByValue(
                Integer.parseInt(registerOperand.substring(2, 3)));
    }
    
    /**
     * Returns the value specified by this register operand
     * @param operand Machine code register operand
     * @return The value (either an absolute value, value read from memory,
     * or a value read from a register)
     */
    private String getValue(String operand, boolean update) {
        AddressMode addressMode = getAddressMode(operand);

        
        switch(addressMode) {
            case IMMEDIATE_DIRECT : 
                // Absolute value
                return operand.substring(0, 2);
            case REGISTER_DIRECT : 
                return registerStore.readFrom(getRegister(operand));
            default : 
                // Operand Must point to memory
                return readFromMemory(getEffectiveMemoryAddress(operand, update));
        }
    }

    /**
     * Returns the effective memory address using the given Register and AddressMode
     * @param register Register part of the operand
     * @param addressMode AddressMode part of the operand
     * @return The effective memory address specified by the given register
     * and address mode or -1 if the address mode does not point to memory
     */
    private int getEffectiveMemoryAddress(String operand, boolean update) {
        AddressMode addressMode = getAddressMode(operand);
        
        // Tests address modes which do not use Registers to get the EA
        switch(addressMode) {
            case IMMEDIATE_DIRECT :
                return -1; // invalid operand
            case IMMEDIATE_INDIRECT :
                return Integer.parseInt(operand.substring(0, 2));
            case REGISTER_DIRECT :
                return -1; // not a memory address
        }
        
        // Address mode must use a register
        Register register = getRegister(operand);
        
        switch(addressMode) {                
            case REGISTER_INDIRECT :
                return Integer.parseInt(registerStore.readFrom(register));
                
            case BASE_DIRECT :
                return Integer.parseInt(registerStore.readFrom(Register.BSE)) +
                        Integer.parseInt(registerStore.readFrom(register));
            
            case BASE_INDIRECT :
                return Integer.parseInt(readFromMemory(
                        Integer.parseInt(registerStore.readFrom(Register.BSE)) +
                        Integer.parseInt(registerStore.readFrom(register))));
                
            case PREDECREMENT_DIRECT :
                int valuePD = Integer.parseInt(registerStore.readFrom(register));
                
                if(update) {
                    valuePD = valuePD - 1;
                    registerStore.writeTo(register, new Integer(valuePD).toString());
                }
                
                return valuePD;
                
            case PREDECREMENT_INDIRECT :
                int valuePDI = Integer.parseInt(registerStore.readFrom(register));
                
                if(update) {
                    valuePDI = valuePDI - 1;
                    registerStore.writeTo(register, new Integer(valuePDI).toString());
                }
                
                return Integer.parseInt(readFromMemory(valuePDI));
                
            case POSTINCREMENT_DIRECT :
                int valuePI = Integer.parseInt(registerStore.readFrom(register));
                if(update) {
                    int updatePI = valuePI + 1;
                    registerStore.writeTo(register, new Integer(updatePI).toString());
                }
                
                return valuePI;
                
            case POSTINCREMENT_INDIRECT :
                int valuePII = Integer.parseInt(registerStore.readFrom(register));
                if(update) {
                    int updatePII = valuePII + 1;
                    registerStore.writeTo(register, new Integer(updatePII).toString());
                }
                
                return Integer.parseInt(readFromMemory(valuePII));             
                
            default :
                return -1;
        }
    }
}
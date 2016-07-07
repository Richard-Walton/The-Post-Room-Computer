package uk.ac.hud.postroom;

/**
 * Enumeration of Operational Codes and their mnemonics
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public enum OpCode {
    /** Halt OpCode - 0 **/
    HLT ("0", 0, "Stops the computer from executing any further instructions. " +
            "This command requires no operands"),
    
    /** Mask OpCOde - 1 **/
    MSK ("1", 2, "Masks the number in the address specified by the first operand " +
            "against the number given by the second operand. " +
            "Stores the result in the first address"),
    
    /** Move OpCode - 2 **/
    MOV ("2", 2, "Copy the number in the address specified by the second operand" +
            " into the address specified by the first operand"),
    
    /** Addition OpCode - 3 **/
    ADD ("3", 2, "Get the numbers from the addresses specified by the first and second " +
            "operands and add them together.  The result is stored in the address " +
            "specified by the first operand"),
    
    /** Subtraction OpCode - 4 **/
    SUB ("4", 2, "Get the numbers from the addresses specified by the first and second " +
            "operands and substract them from each other. The result is stored in the address " +
            "specified by the first operand"),
    
    /** Input OpCode - 5 **/
    INP ("5", 1, "Input data into the address specified by the first and only operand"),
    
    /** Output OpCode - 6 **/
    OUT ("6", 1, "Output data from the address specified by the first and only operand"),
    
    /** Move Effective Address OpCode - 7 **/
    MEA ("7", 2, "Similar to MOV, except it is now not the number at the address specified " +
            "by the first operand, but the address itself which is copied into the address " +
            "specified by the second operand"),
    
    /** Jump OpCode - 8 **/
    JMP ("8", 2, "Jump to the address given in the second operand if the condition, " +
            "specified by the first operand, is true"),
    
    /** Shift OpCode - 9 **/
    SHF ("9", 2, "Shifts the number specified by the first operand left by the number " +
            "at the address specified by the second operand. If the number is negative, " +
            "the first number is shifted to the right");
    
    // Value of the OpCode
    private String value;
    
    // Number of required operands
    private int operandCount;
    
    // Description of the OpCode's function
    private String description;
    
    /**
     * Constructs a new OpCode with the given values
     * @param value Machine code Value
     * @param operandCount Required operand count
     * @param description Description of the OpCode's function
     */private OpCode(String value, int operandCount, String description) {
        this.value = value;
        this.operandCount = operandCount;
        this.description = description;
    }
    
    /**
     * Returns the value of the OpCode
     * @return value of the OpCode
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Returns the required operand count of this opcode
     * @return required operand count of this opcode
     */
    public int getRequiredOperandCount() {
        return operandCount;
    }
    
    /**
     * Returns the description of the OpCode
     * @return description of the OpCode
     */
    public String getDescription() {
        return description;
    }
    
    /** 
     * Returns the OpCode matching the given mnemonic
     * @param mnemonic Mnemonic of the OpCode
     * @return OpCode matching the given mnemonic
     */
    public static OpCode getByMnemonic(String mnemonic) {
        return valueOf(mnemonic.toUpperCase());
    }
    
    /**
     * Returns the OpCode matching the given value
     * @param value Value of the OpCode
     * @return OpCode matching the given value
     */
    public static OpCode getByValue(String value) {
        for(OpCode opCode : values()) {
            if(opCode.getValue().equals(value)) {
                return opCode;
            }
        }
        
        // Throw exception as no OpCode matches the given value
        throw new IllegalArgumentException("No OpCode with value " + value);
    }
}
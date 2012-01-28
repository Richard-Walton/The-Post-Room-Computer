package uk.ac.hud.postroom;

import java.util.*;

/**
 * Enumeration of the Post Room Computer Registers
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public enum Register {
    
    // BANK ZERO
    
    /** General purpose register - b0n0 **/
    R0 ("00"), 
    
    /** General purpose register - b0n1 **/
    R1 ("01"),
    
    /** General purpose register - b0n2 **/
    R2 ("02"), 
    
    /** General purpose register - b0n3 **/
    R3 ("03"), 
    
    /** General purpose register - b0n4 **/
    R4 ("04"),
    
    /** General purpose register - b0n5 **/
    R5 ("05"), 
    
    /** General purpose register - b0n6 **/
    R6 ("06"), 
    
    /** General purpose register - b0n7 **/
    R7 ("07"),
    
    /** General purpose register - b0n8 **/
    R8 ("08"),
    
    /** General purpose register - b0n9 **/
    R9 ("09"),
    
    // BANK ONE
    
    /** Program Counter - b1n0 **/
    PC ("10"), 
       
    /** Base Register - b1n2 **/
    BSE ("12"),
    
    /** Flag register - b1n4 **/
    FLG ("14"),
    
    /** Carry register - b1n8 **/
    CAR ("18"),
    
    // BANK TWO
    
    /** X address register - b2n0 **/
    XA ("20"),
    
    /** Y address register - b2n1 **/
    YA ("21"),
    
    /** Z (Destination) address register - b2n2 **/
    ZA ("22"),
    
    /** X address type register - b2n3 **/
    XAT("23"), 
    
    /** Y address type register - b2n4 **/
    YAT("24"),
    
    /** Z address type register - b2n5 **/
    ZAT("25"),
    
    /** X value register - b2n6 **/
    XV ("26"),
    
    /** Y value register - b2n7 **/
    YV ("27"),
    
    /** Z (Result) value register - b2n8 **/
    ZV ("28"),
    
    /** Operator register - b2n9 **/
    OP ("29"),
    
    // BANK THREE 
     
    /** Instruction register - b3n0 **/
    IR ("30"),
    
    /** Address field 1 register - b3n1 **/
    AF1 ("31"),
    
    /** Address field 2 register - b3n2 **/
    AF2 ("32"),
    
    /** Address field 3 register - b3n3 **/
    AF3("33"),
    
    // BANK FOUR
    
    /** The memory address register - b4n0 **/
    MAR ("40"),
    
    /** The memory data register - b4n1 **/
    MDR ("41"),
    
    /** The memory read/write register - b4n2 **/
    MRW ("42"), 
    
    /** The I/O address register - b4n3 **/
    IOA ("43"),
    
    /** The I/O buffer register - b4n4 **/
    IOB ("44"),
    
    /** The I/O read/write register - b4n5 **/
    IORW ("45"), 
     
    /** The I/O type buffer. Indicates the type of I/O to be performed  - b4n6 **/
    IOT ("46");
    
    // The bank which the register is stored in
    private int bank;
    
    // The register number
    private int registerNo;
    
    // The RegisterID (bank|registerNo) 
    private String registerID;
    
    /**
     * Constructs a new Register using the given registerID 
     * (1st digit for bank, 2nd for register number)
     * @param registerID ID (1st digit for bank, 2nd for register number) of the Register
     */
    private Register(String registerID) {
        this.registerID = new String(new char[] { registerID.charAt(1), registerID.charAt(0) });
        
        // Get bank and registerNo 
        bank = Integer.parseInt(registerID.substring(0, 1));
        registerNo = Integer.parseInt(registerID.substring(1, 2));
    }
    
    /**
     * Returns the bank which this register is stored in
     * @return bank which this register is stored in
     */
    public int getBankNo() { 
        return bank;
    }
    
    /**
     * Returns the number in the bank where this register is stored
     * @return number in the bank where this register is stored
     */
    public int getRegisterNo() {
        return registerNo;
    }
    
    /**
     * Returns the mnemonic representation of the Register
     * @return mnemonic representation of the Register
     */
    public String getMnemonic() {
        return name();
    }
    
    /**
     * Returns the registerID (bank|registerNo) of this register
     * @return registerID of this register
     */
    public String getRegisterID() {
        return registerID;
    }
    
    /**
     * Returns the Register matching the given mnemonic
     * @param mnemonic Mnemonic of the Register
     * @return Register matching the given mnemonic
     */
    public static Register getByMnemonic(String mnemonic) {
        return valueOf(mnemonic.toUpperCase());
    }
    
    /**
     * Returns the Register matching the given registerID
     * @param registerID ID of the register
     * @return Register matching the given registerID
     */
    public static Register getByRegisterID(String registerID) {
        for(Register register : values()) {
            if(register.getRegisterID().equals(registerID)) {
                return register;
            }
        }
        
        // Throw exception as no Register matches the given value
        throw new IllegalArgumentException("No Register with ID " + registerID);
    }
    
    /**
     * Returns all registers in the given bank
     * @param bankNo Bank of registers to get
     * @return Registers in the given bank
     */
    public static Register[] getByBank(int bankNo) {
        List<Register> registers = new ArrayList<Register>();
        
        for(Register register : values()) {
            if(register.getBankNo() == bankNo) {
                registers.add(register);
            }
        }
        
        return registers.toArray(registers.toArray(new Register[]{}));
    }
}
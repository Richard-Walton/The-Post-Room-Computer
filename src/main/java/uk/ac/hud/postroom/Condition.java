package uk.ac.hud.postroom;

/**
 * Enumeration of Condition Codes and their mnemonics
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public enum Condition {
    /** Never - 000 **/
    NVR ("000"),
    
    /** Always - 008 **/
    LWY ("008"),
    
    /** Less than zero - 002 **/
    LTZ ("002"),
    
    /** Less than or equal to zero - 003 **/ 
    LEZ ("003"),
    
    /** Equal to zero - 001 **/
    EQZ ("001"),
    
    /** Not equal to zero - 009 **/
    NEZ ("009"),
    
    /** Greater than or equal to zero - 010 **/
    GEZ ("010"),
    
    /** Greater than zero - 011 **/
    GTZ ("011"),
    
    /** Carry - 004 **/
    CRY ("004"),
    
    /** Not a carry - 012 **/
    NCRY("012"),
    
    /** Overflow - 016 **/
    VFL ("016"),
    
    /** Not an overflow - 024 **/
    NVFL("024"),
    
    /** Memory - 040 **/
    MEM ("040"),
    
    /** Not memory - 032 **/
    NMEM("032");
    
    // Value of the Condition
    private String value;
    
    /**
     * Constructs a new Condition with the given value
     * @param value Value of the Condition
     */
    private Condition(String value) {
        this.value = value;
    }
    
    /**
     * Returns the value of the Condition
     * @return value of the Condition
     */
    public String getValue() {
        return value;
    }
    
    /** 
     * Returns the Condition matching the given mnemonic
     * @param mnemonic Mnemonic of the Condition
     * @return Condition matching the given mnemonic
     */
    public static Condition getByMnemonic(String mnemonic) {
        return valueOf(mnemonic.toUpperCase());
    }
    
    /**
     * Returns the Condition matching the given value
     * @param value Value of the Condition
     * @return Condition matching the given value
     */
    public static Condition getByValue(String value) {
        for(Condition condition : values()) {
            if(condition.getValue().equals(value)) {
                return condition;
            }
        }
        
        // Throw exception as no Condition matches the given value
        throw new IllegalArgumentException("No Condition with value " + value);
    }
}
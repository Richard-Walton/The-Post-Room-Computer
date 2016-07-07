package uk.ac.hud.postroom;

/**
 * Enumeration of AddressModes and their mnemonics
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public enum AddressMode {
    
    /** Register direct - '.' | 0 **/
    REGISTER_DIRECT (0, "."),
    
    /** Base direct - '>' | 1 **/
    BASE_DIRECT (1, ">"),
    
    /** Immediate direct - '&' | 2 **/
    IMMEDIATE_DIRECT (2, "&"),
    
    /** Predecrement direct - '-' | 3 **/
    PREDECREMENT_DIRECT (3, "-"),
    
    /** Postdecrement direct - '+' | 4 **/
    POSTINCREMENT_DIRECT (4, "+"),
    
    /** Register indirect - '@' | 5 **/
    REGISTER_INDIRECT (5, "@."),
    
    /** Base indirect - '@>' | 6 **/
    BASE_INDIRECT (6, "@>"),
    
    /** Immediate indirect - '@&' | 7 **/
    IMMEDIATE_INDIRECT (7, "@&"),
    
    /** Predecrement indirect - '@-' | 8 **/
    PREDECREMENT_INDIRECT (8, "@-"),
    
    /** Postdecrement indirect - '@+' | 9 **/
    POSTINCREMENT_INDIRECT(9, "@+");
    
    private int value;
    private String mnemonic;
    
    /**
     * Constructs a new AddressMode
     * @param value Value of the AddressMode
     * @param mnemonic Mnemonic of the AddressMode
     */
    private AddressMode(int value, String mnemonic) {
        this.value = value;
        this.mnemonic = mnemonic;
    }
    
    /**
     * Returns the value of the AddressMode
     * @return value of the AddressMode
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Returns the mnemonic of the AddressMode
     * @return mnemonic of the AddressMode
     */
    public String getMnemonic() {
        return mnemonic;
    }
    
    /** 
     * Returns the AddressMode matching the given mnemonic
     * @param mnemonic Mnemonic of the AddressMode
     * @return AddressMode matching the given mnemonic
     */
    public static AddressMode getByMnemonic(String mnemonic) {
        for(AddressMode addressMode : values()) {
            if(addressMode.getMnemonic().equals(mnemonic)) {
                return addressMode;
            }
        }
        
        // Throw exception as no AddressMode matches the given mnemonic
        throw new IllegalArgumentException("No AddressMode with mnemonic " + mnemonic);
    }
    /**
     * Returns the AddressMode matching the given value
     * @param value Value of the AddressMode
     * @return AddressMode matching the given value
     */
    public static AddressMode getByValue(int value) {
        for(AddressMode addressMode : values()) {
            if(addressMode.getValue() == value) {
                return addressMode;
            }
        }
        
        // Throw exception as no AddressMode matches the given value
        throw new IllegalArgumentException("No AddressMode with value " + value);
    }
}
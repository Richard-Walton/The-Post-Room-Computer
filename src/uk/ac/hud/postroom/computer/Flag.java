package uk.ac.hud.postroom.computer;

/**
 * Flag Register
 * @author Richard Walton (c041052@hud.ac.uk)
 */
public class Flag {
    
    // Memory flag bit
    private boolean memory;
    
    // Overflow flag bit
    private boolean overflow;
    
    // Not flag bit
    private boolean not;
    
    // Carry flag bit
    private boolean carry;
    
    // Negative flag bit
    private boolean negative;
    
    // Zero flag bit
    private boolean zero;
    
    /**
     * Constructs a new Flag with all the bits set to 0 (false)
     */
    public Flag() {
        this("000000");
    }
    
    /**
     * Constructs a new Flag using the given string which must be 6 binary digits long
     * @param flag String representation of the flag
     */
    public Flag(String flag) {
        if(flag.length() != 6) {
            throw new IllegalArgumentException("Flag must be 6 binary digits long");
        }
        
        // Set flag bits
        memory = flag.charAt(0) == '1';
        overflow = flag.charAt(1) == '1';
        not = flag.charAt(2) == '1';
        carry = flag.charAt(3) == '1';
        negative = flag.charAt(4) == '1';
        zero = flag.charAt(5) == '1'; 
    }
    
    /**
     * Sets all the flag bits to false (0)
     */
    public void reset() {
        setMemory(false);
        setOverflow(false);
        setNot(false);
        setCarry(false);
        setNegative(false);
        setZero(false);
    }
    
    /**
     * Returns a 6 binary digit representation of the flag
     * @return 6 binary digit representation of the flag
     */
    public String getMask() {
        StringBuilder mask = new StringBuilder();
        
        // Build Flag mask
        mask.append(isMemory() ? "1" : "0");
        mask.append(isOverflow() ? "1" : "0");
        mask.append(isNot() ? "1" : "0");
        mask.append(isCarry() ? "1" : "0");
        mask.append(isNegative() ? "1" : "0");
        mask.append(isZero() ? "1" : "0");
        
        return mask.toString();    
    }
    
    /**
     * Returns whether the memory bit is set
     * @return true if memory bit is true
     */
    public boolean isMemory() {
        return memory;
    }
    
    /**
     * Returns whether the overflow bit is set
     * @return true if overflow bit is true
     */
    public boolean isOverflow() {
        return overflow;
    }
    
    /**
     * Returns whether the not bit is set
     * @return true if not bit is true
     */
    public boolean isNot() {
        return not;
    }
    
    /**
     * Returns whether the carry bit is set
     * @return true if carry bit is true
     */
    public boolean isCarry() {
        return carry;
    }
    
    /**
     * Returns whether the negative bit is set
     * @return true if negative bit is true
     */
    public boolean isNegative() {
        return negative;
    }
    
    /**
     * Returns whether the zero bit is set
     * @return true if zero bit is true
     */
    public boolean isZero() {
        return zero;
    }
    
    /**
     * Sets the memory bit
     * @param memory new bit value
     */
    public void setMemory(boolean memory) {
        this.memory = memory;
    }
    
    /**
     * Sets the overflow bit
     * @param overflow new bit value
     */
    public void setOverflow(boolean overflow) {
        this.overflow = overflow;
    }
    
    /**
     * Sets the not bit
     * @param not new bit value
     */
    public void setNot(boolean not) {
        this.not = not;
    }
    
    /**
     * Sets the carry bit
     * @param carry new bit value
     */
    public void setCarry(boolean carry) {
        this.carry = carry;
    }
    
    /**
     * Sets the negative bit
     * @param negative new bit value
     */
    public void setNegative(boolean negative) {
        this.negative = negative;
    }
    
    /**
     * Sets the zero bit
     * @param zero new bit value
     */
    public void setZero(boolean zero) {
        this.zero = zero;
    }
}
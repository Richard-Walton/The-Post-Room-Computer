package uk.ac.hud.postroom.assembler;

/**
 * A single token of data
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class Token {
    /** Type of token **/
    private TokenType type;
    
    /** Value of token **/
    private String value;
    
    /**
     * Constructs a new token
     * @param type Type of token
     * @param value Value of token
     */
    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }
    
    /**
     * Returns the type of the token
     * @return Type of the token
     */
    public TokenType getType() {
        return type;
    }
    
    /**
     * Returns the value of the token
     * @return Value of the token
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Returns whether the type of this token equals the given token
     * @param type Token of token
     * @return true if type equals the type of this token returned by getType()
     */
    public boolean is(TokenType type) {
        return this.type == type;
    }
}
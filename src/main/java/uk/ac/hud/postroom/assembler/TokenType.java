package uk.ac.hud.postroom.assembler;

/**
 * Enumeration of types of token
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public enum TokenType {
    
    INSTRUCTION ("Post Room Computer machine code instruction"), 
    
    /** Address token - e.g. 01 **/
    ADDRESS ("A 2 digit value used to specify a location in memory (from 00 to 99). " +
            "Use these only in Absolute Address Machines"),
    
    /** Character token - e.g. 'a' **/
    CHARACTER ("A alphanumberic single character"),
    
    /** Comment token - e.g. ;comment **/
    COMMENT ("Additional information which aids program description"),
    
    /** Condition token - e.g. LWY **/
    CONDITION ("A flag which is used to control the flow of execution"),
    
    /** Data token - e.g. (0) **/
    DATA ("A numberical value which is loaded into memory"),
    
    /** End of line token **/
    EOL ("The end of a line"),
    
    /** Error token **/
    ERROR  ("An unreconised token"),
    
    /** Filename token - e.g. macros.pca **/
    FILENAME ("A value specifying a .pca file in the same project to load and use"),
    
    /** Include directive token - !include **/
    INCLUDE_DIRECTIVE ("Informs the assembler that an additional file needs to be loaded"),
    
    /** Label token - e.g. :x: **/
    LABEL ("A way to direct the computer to a location in memory without specifying the absolute address"),
    
    LABEL2 ("A way to force instructions to be loaded into a specific point in the computer's memory"),
    
    /** Macro_End token - % **/
    MACRO_END ("The end of a macro"),
    
    /** Macro_Start token - e.g. %macro **/
    MACRO_START("The start of a macro"),
    
    /** OpCode token - e.g. INP **/
    OPCODE ("A Post Room Computer inbuilt command"),
    
    /** Register token - e.g. .R1 **/
    REGISTER ("A Post Room Computer register (including its AddressMode).  Use these only in the " +
            "Register Address Computer"),
    
    /** String token - e.g. "string" **/
    STRING ("A sequence of characters which will be assembled into a list of characters stored " +
            "sequentially in memory"), 
    
    /** Whitespace token **/
    WHITESPACE ("A space or a tab"),
    
    /** Word token **/
    WORD ("This data type is usually a macro call or a argument label in a macro");
    
    private String description;
    
    private TokenType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
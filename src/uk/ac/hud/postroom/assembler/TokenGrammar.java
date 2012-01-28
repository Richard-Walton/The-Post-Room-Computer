package uk.ac.hud.postroom.assembler;

import java.util.regex.*;

/**
 * Enumeration of regular expressions representing TokenTypes
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public enum TokenGrammar {
    
    INSTRUCTION ("[0-9]{7}", TokenType.INSTRUCTION), 
    
    /** Regular expression representing an Address **/
    ADDRESS ("[0-9]{1,2}", TokenType.ADDRESS),
    
    /** Regular expression representing a Character **/
    CHARACTER("['][a-z][']", TokenType.CHARACTER),
    
    /** Regular expression representing a Comment **/
    COMMENT (";.*", TokenType.COMMENT),
    
    /** Regular expression representing a Condition Code **/
    CONDITION ("NVR|LWY|LTZ|LEZ|EQZ|NEZ|GEZ|GTZ|CRY|NCRY|VFL|NVFL|MEM|NMEM", TokenType.CONDITION),
    
    /** Regular expression representing Data **/
    DATA ("[(][-]?[0-9]+[)]", TokenType.DATA),
    
    /** Regular expression representing a Filename **/
    FILENAME ("[\\w]+\\.pca", TokenType.FILENAME),
    
    /** Regular expression representing an Include directive **/
    INCLUDE_DIRECT ("!include", TokenType.INCLUDE_DIRECTIVE),
    
    /** Regular expression representing a Label **/
    LABEL ("[:][a-z]+[:]", TokenType.LABEL),
    
    LABEL2 ("[:]\\d{1,3}[:]", TokenType.LABEL2),
    
    /** Regular expression representing the end of a Macro **/
    MACRO_END ("%", TokenType.MACRO_END),
    
    /** Regular expression representing the start of a Macro **/
    MACRO_START ("%\\S+", TokenType.MACRO_START),
    
    /** Regular expression representing an OpCode **/
    OPCODE ("HLT|MSK|MOV|ADD|SUB|INP|OUT|MEA|JMP|SHF", TokenType.OPCODE),
    
    /** Regular expression representing a Register (including it's addressing mode) **/
    REGISTER ("(\\.|<|>|-|\\+|@.|@\\+|@-|@>|@<)" +
            "(R[0-9]|" + //bank 0
            "PC|BSE|FLG|CAR|" + // bank 1
            "XA|YA|VA|XAT|YAT|ZAT|XV|YV|ZV|" + // bank 2
            "IR|AF[1-3]|" + //bank 3
            "MAR|MDR|MRW|IAO|IOB|IORW|IOT)|(@&|&)([-]?[0-9]{1,2})|(@&|&)([:]\\S+[:])", TokenType.REGISTER), //bank 4
     
    /** Regular expression represting a string **/
    STRING ("[\"].+[\"]", TokenType.STRING),
    
    /** Regular expression representing whitespace **/
    WHITESPACE ("\\s+", TokenType.WHITESPACE),
    
    /** Regular expression representing a Word **/
    WORD ("\\S+", TokenType.WORD),
    
    EOL("\r\n|\n", TokenType.EOL);

    /** Regular expression representing the Token **/
    private Pattern pattern;
    
    /** The type of token represented by the Regular Expression **/
    private TokenType type;

    /**
     * Constucts a new TokenGrammar
     * @param regex Regular expression representing a Token
     * @param type Type of token represented
     */
    private TokenGrammar(String regex, TokenType type){
        this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        this.type = type;
    }

    /**
     * Returns a Matcher object using the given string
     * @param string Input to match against
     * @return Matcher object
     */
    public Matcher matcher(String string){
        return pattern.matcher(string);
    }

    /**
     * Returns the type of Token represented
     * @return TokenType of the represented token
     */
    public TokenType getType() {
        return type;
    }
}
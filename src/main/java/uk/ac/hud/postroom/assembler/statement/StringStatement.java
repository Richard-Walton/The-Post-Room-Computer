package uk.ac.hud.postroom.assembler.statement;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.exception.*;

import java.util.*;

/**
 * A String statement
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class StringStatement extends Statement {
    // Characters contained in this statement
    private char[] characters;
    
    // DataStatement representation of the characters contained in this statement
    private DataStatement[] characterData;
    
    /**
     * Constructs a new StringStatement
     * @param sourceFile SourceFile containing this Statement
     * @param lineNo Line number on which this Statement is located
     * @param tokens Tokens representing this statement
     * @throws SyntaxError If the tokens do not represent a StringStatement
     */
    public StringStatement(SourceFile sourceFile, int lineNo, Token[] tokens) throws SyntaxError {
        // Super-class (Statement) constructor
        super(sourceFile, lineNo, tokens);
        
        // First token must be a string token
        if(! tokens[0].is(TokenType.STRING)) {
            throwSyntaxError(0, "Expected string token");
        }
        
        // Convert the string into a character array
        characters = tokens[0].getValue().toCharArray();
        
        // Remove the leading and trailing quotation marks
        characters = Arrays.copyOfRange(characters, 1, characters.length - 1);
        
         // Creates the DataStatement representation of the characters
        characterData = new DataStatement[characters.length];
        for(int i = 0; i < characters.length; i++) {
            // Create data token
            Token data = new Token(TokenType.DATA, "(" + (int) characters[i] + ")");
            
            // Create data statement (including the required EOL token)
            characterData[i] = new DataStatement(
                    getSourceFile(), 
                    getLineNo(), 
                    new Token[] { data, new Token(TokenType.EOL, "") });
        }
        
        // Second token must be an end of line token
        if(!tokens[1].is(TokenType.EOL)) {
            throwSyntaxError(1, "Expected end of line");
        }
    }
    
    /**
     * Returns the characters used to make the string
     * @return character array of the string
     */
    public char[] getCharacters() {
        return characters;
    }
    
    /**
     * Returns the DataStatement represention of the string
     * @return DataStatement represention of the string
     */
    public DataStatement[] getDataStatements() {
        return characterData;
    }
}
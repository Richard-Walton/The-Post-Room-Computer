package uk.ac.hud.postroom.assembler.statement;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.exception.*;

/**
 * A Data statement
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class DataStatement extends Statement {
    
    /** Data this statement contains **/
    private String data;
    
    /**
     * Constructs a new DataStatement
     * @param sourceFile SourceFile containing this Statement
     * @param lineNo Line number on which this Statement is located
     * @param tokens Tokens representing this statement
     * @throws SyntaxError If the tokens do not represent a DataStatement
     */
    public DataStatement(SourceFile sourceFile, int lineNo, Token[] tokens) throws SyntaxError {
        // Super-class (Statement) constructor
        super(sourceFile, lineNo, tokens);
        
        // First token must be of type Data
        if(!tokens[0].is(TokenType.DATA)) {
            throwSyntaxError(0, "Not a data statement");
        }
        
        // Second token must be an EOL token
        if(!tokens[1].is(TokenType.EOL)) {
            throwSyntaxError(1, "Expected end of line");
        }
        
        // Get the data value by removing the '(' and ')' 
        String value = tokens[0].getValue();
        data = value.substring(1, value.length() - 1);
    }
    
    /**
     * Returns the Data value of this Statement
     * @return Data value
     */
    public String getData() {
        return data;
    }
}
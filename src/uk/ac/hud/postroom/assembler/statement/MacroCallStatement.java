package uk.ac.hud.postroom.assembler.statement;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.exception.*;

import java.util.*;

/**
 * A Macro-call statement.  This statement contains the Operands which are 
 * passed to a MacroStatement's expand() method.
 * 
 * @author Richard Walton (c0410542@uk.ac.hud)
 */
public class MacroCallStatement extends Statement {
    
    /** Operands to be passed **/
    private List<Token> operands;
    
    /**
     * Constructs a new MacroCallStatement
     * @param sourceFile SourceFile containing this Statement
     * @param lineNo Line number on which this Statement is located
     * @param tokens Tokens representing this statement
     * @throws SyntaxError If the tokens do not represent an MacroCallStatement
     */
    public MacroCallStatement(SourceFile sourceFile, int lineNo, Token[] tokens) throws SyntaxError {
        // Super-class (Statement) constructor
        super(sourceFile, lineNo, tokens);
        
        operands = new ArrayList<Token>();
        
        // First token must be a Macrocall (Macrocalls are tokenized as Words)
        if(!tokens[0].is(TokenType.WORD)) {
            throwSyntaxError(0, "Not a macro call");
        }
        
        /* Any number of tokens after (and until the EOL token) must be tokens 
         * suitable to passing to a macro - i.e. Address's, labels or Registers */
        for(int i = 1; i < tokens.length - 1; i++){
            switch(tokens[i].getType()) {
                case ADDRESS : case LABEL : case REGISTER : case CHARACTER :
                    operands.add(tokens[i]);
                    break;
                default :
                    throwSyntaxError(i, "Unexpected operand type");
            }
        }
        
        // Last token must be an EOL token
        if(!tokens[tokens.length-1].is(TokenType.EOL)) {
            throwSyntaxError(tokens.length-1, "Expected end of line");
        }
    }
    
    /**
     * Returns the name of the Macro this statement is suitable to call
     * @return name of the Macro this statement is suitable to call
     */
    public String getMacroName() {
        return getTokens()[0].getValue();
    }
    
    /**
     * Returns the operand tokens (in order) specified in this Statement
     * @return operand tokens (in order) specified in this Statement
     */
    public Token[] getOperandTokens() {
        return operands.toArray(new Token[]{});
    }
     
    /**
     * Returns the number of Operands this statement will pass to a MacroStatement
     * @return number of Operands this statement will pass to a MacroStatement
     */
    public int operandCount() {
        return operands.size();
    }
}
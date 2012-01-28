package uk.ac.hud.postroom.assembler.statement;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.exception.*;

import java.util.*;

/**
 * A Label statement
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class LabelStatement extends Statement {
    
    /** The statement which the label points to **/
    private Statement statement;
    
    private boolean addressJump;
    
    /**
     * Constructs a new LabelStatement
     * @param sourceFile SourceFile containing this Statement
     * @param lineNo Line number on which this Statement is located
     * @param tokens Tokens representing this statement
     * @throws SyntaxError If the tokens do not represent an LabelStatement
     */
    public LabelStatement(SourceFile sourceFile, int lineNo, Token[] tokens) throws SyntaxError {
        // Super-class (Statement) constructor
        super(sourceFile, lineNo, tokens);
        
        // First token must be a label
        if(tokens[0].is(TokenType.LABEL)){
            addressJump = false;
        }else if(tokens[0].is(TokenType.LABEL2)) {
            addressJump = true;
        }else {
            throwSyntaxError(0, "Not a Label definition");
        }
        
        // Get the tokens for the statement the label points to
        Token[] statementTokens = Arrays.copyOfRange(tokens, 1, tokens.length);
        
        /* Try to construct the sub-statement based upon the type of Token
         * at the start of the statementTokens e.g. OpCode token -> OpCodeStatment 
         * Any syntax errors thrown will be  re-thrown and so this
         * LabelStatement will fail to construct */
        switch(statementTokens[0].getType()) {
            case OPCODE :
                statement = new OpCodeStatement(sourceFile, lineNo, statementTokens);
                break;
            case WORD :
                statement = new MacroCallStatement(sourceFile, lineNo, statementTokens);
                break;
            case DATA :
                statement = new DataStatement(sourceFile, lineNo, statementTokens);
                break;
            case STRING :
                statement = new StringStatement(sourceFile, lineNo, statementTokens);
                break;
            case INSTRUCTION :
                statement = new Instruction(sourceFile, lineNo, statementTokens);
                break;
            case EOL :
                statement = null;
                break;
            default :
                // Labels can only point to OpCode, MacroCall, string or Data statements
                throwSyntaxError(1, "Expected opcode, data, string or EOL");
        }
    }
    
    /**
     * Returns the name of the label in this Statement
     * @return the name of the label in this Statement
     */
    public String getLabelName() {
        return getTokens()[0].getValue();
    }
    
    /**
     * Returns the Statement which this LabelStatement points to
     * @return the Statement which this LabelStatement points to
     */
    public Statement getStatement() {
        return statement;
    }
    
    public boolean isAddressJump() {
        return addressJump;
    }
    
    @Override public int hashCode() {
        return 12345;
    }
    
    /** @inheritDoc **/
    @Override public boolean equals(Object object) {
        if(object instanceof LabelStatement) {
            return getLabelName().equals(((LabelStatement) object).getLabelName());
        }else{
            return false;
        }
    }
}
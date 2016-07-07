package uk.ac.hud.postroom.assembler;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.exception.*;
import uk.ac.hud.postroom.assembler.statement.*;

/**
 * Abstract super-class of all Statements.  All sub classes perform syntax checking
 * on the given token array and so extra whitespace and comments should be removed
 * prior to attempting to construct the statement
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public abstract class Statement {
    
    /** Source File containing the Statement **/
    private SourceFile sourceFile;
    
    /** Line number in the Source File the Statement is from **/
    private int lineNo;
    
    /** Tokens representing this statement **/
    private Token[] tokens;
    
    private int memoryLocation;
    
    /**
     * Constructs a new Statement
     * @param sourceFile Source File the statement is from
     * @param lineNo Line number in the Source File the statement is from
     * @param tokens Tokens representing this statement
     */
    public Statement(SourceFile sourceFile, int lineNo, Token[] tokens) {
        this.sourceFile = sourceFile;
        this.tokens = tokens;
        this.lineNo = lineNo;
        this.memoryLocation = -1;
    }
    
    /**
     * Returns the SourceFile this statement is from
     * @return SourceFile this statement is from
     */
    public SourceFile getSourceFile() {
        return sourceFile;
    }
    
    /**
     * Returns the line number in the SourceFile this statement is from
     * @return line number in the SourceFile this statement is from
     */
    public int getLineNo() {
        return lineNo;
    }
    
    /**
     * Returns the original source code used to create this statement
     * @return Original source code used to create this statement
     */
    public String getSourceCode() {
        return getSourceFile().getSourceCode().split("\n|\n\r")[getLineNo()].trim();
    }
    
    /**
     * Returns the key tokens representing this statement
     * @return the tokens (minus Comment and Whitespace) representing this statement
     */
    public Token[] getTokens() {
        return tokens;
    }
    
    public int getMemoryLocation() {
        return memoryLocation;
    }
    
    public void setMemoryLocation(int memoryLocation) {
        this.memoryLocation = memoryLocation;
    }
    
    /**
     * Convenience method - Throws a syntax error using the given input
     * @param tokenIndex index of the token at which the error occured 
     * (used to set the near() parameter)
     * @param reason Reason the Error occured
     * @throws SyntaxError
     */
    protected void throwSyntaxError(int tokenIndex, String reason) throws SyntaxError {
        // Constructs a new Invalid statement using this statements data
        InvalidStatement statement = 
                new InvalidStatement(getSourceFile(), getLineNo(), getTokens());
        
        throw new SyntaxError(statement, reason, tokenIndex);
    }
    
    /** @inheritDoc **/
    @Override public String toString() {
        StringBuffer data = new StringBuffer();
        for(Token token : getTokens()) {
            data.append(token.getValue() + " ");
        }
        
        data.trimToSize();
        
        return data.toString();
    }
    
    /** @inheritDoc **/
    @Override public boolean equals(Object object) {
        if(object instanceof Statement){
            Token[] objectTokens = ((Statement) object).getTokens();
            if(tokens.length == objectTokens.length){
                for(int i = 0; i < tokens.length; i++){
                    if(!tokens[i].equals(objectTokens[i])){
                        return false;
                    }
                }
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
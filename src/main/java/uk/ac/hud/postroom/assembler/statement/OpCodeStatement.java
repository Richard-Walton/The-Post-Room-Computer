package uk.ac.hud.postroom.assembler.statement;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.exception.*;

/**
 * An OpCode statement 
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class OpCodeStatement extends Statement {

    // Type of OpCode used in this statement 
    private OpCode opCode;
    
    /**
     * Constructs a new OpCodeStatement using the given parameters
     * @param sourceFile SourceFile containing this Statement
     * @param lineNo Line number on which this Statement is located
     * @param tokens Tokens representing this statement
     * @throws uk.ac.hud.postroom.exception.SyntaxError thrown if SyntaxError is found
     */
    public OpCodeStatement(SourceFile sourceFile, int lineNo, Token[] tokens) throws SyntaxError {
        // Super class (Statement) constructor
        super(sourceFile, lineNo, tokens);
        
        // Check if first token represents an OpCode
        if(!tokens[0].is(TokenType.OPCODE)) {
            throwSyntaxError(0, "Expected opcode");
        }
        
        // Get the OpCode
        opCode = OpCode.getByMnemonic(tokens[0].getValue());
          
        // Check the length of the statement depending on the statement type
        switch(opCode) {
            case HLT :
                checkTokenLength(2);
                break;
            case INP :
            case OUT :
                checkTokenLength(3);
               break;
            default :
                checkTokenLength(4);
        }
        
        // Check the syntax of the statement 
        switch(opCode) {
            case JMP :
                // JUMP Statements must contain a Condition code
                if(tokens[1].getType() != TokenType.CONDITION) {
                    throwSyntaxError(1, "Expected condition code");
                }
                
                // Followed by either an address, label or register
                switch(tokens[2].getType()) {
                    case ADDRESS : case LABEL : case REGISTER : break;
                    default : throwSyntaxError(2, "Invalid operand type");
                }
                break;
                
            default :
                // All other statements must have either an Address, Label, Register, or character
                for(int i = 1; i < tokens.length - 1; i++) {
                    switch(tokens[i].getType()) {
                        case ADDRESS : case LABEL : case REGISTER : case CHARACTER : break;
                        default : throwSyntaxError(i, "Invalid operand type");
                    }
                }
                break;
        }  
        
        // Check if last token is an End of Line token
        if(! tokens[tokens.length - 1].is(TokenType.EOL)) {
            throwSyntaxError(tokens.length - 1, "Expected end of line");
        }
    }
    
    /**
     * Returns the OpCode used in this statement
     * @return OpCode used in this statement
     */
    public OpCode getOpCode() {
        return opCode;
    }
    
    /**
     * Checks for the correct length of the tokens in this statement
     * @param validLength Expected length of tokens
     * @throws uk.ac.hud.postroom.exception.SyntaxError thrown if tokens count is not as expected
     */
    private void checkTokenLength(int validLength) throws SyntaxError {
        int tokenCount = getTokens().length;
        
        // Check if there are less tokens
        if(tokenCount < validLength) {
            throwSyntaxError(tokenCount - 1, "Expected additional input");
        }
         
        // Check if there are more tokens
        if(tokenCount > validLength) {
            throwSyntaxError(tokenCount - 1, "Expected end of line");
        }
    }
}
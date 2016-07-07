package uk.ac.hud.postroom;

import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.exception.*;

/**
 * A Machine Code instruction
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class Instruction extends Statement {
    
    // Machine code instruction generated from this statement
    private String instruction;
    
    private boolean breakpoint;
    
    public Instruction(SourceFile sourceFile, int lineNo, Token[] tokens) throws SyntaxError {
        super(sourceFile, lineNo, tokens);
        
        if(! tokens[1].is(TokenType.EOL)) {
            throwSyntaxError(1, "Expected end of line");
        }
        
        instruction = tokens[0].getValue();
    }
    
    /**
     * Constructs a new Instruction
     * @param statement Statement used to generate the instruction
     * @param instruction Machine code instruction
     * @throws AssemblyError thrown if the instruction is not an integer
     */
    public Instruction(Statement statement, String instruction) throws AssemblyError {
        // Super-class (Statement) constructor
        super(statement.getSourceFile(), statement.getLineNo(), statement.getTokens());

        // Check to ensure valid (integer) instruction data
        try {
            Integer.parseInt(instruction);
        }catch (NumberFormatException e) {
            throw new AssemblyError(this, "Invalid instruction data");
        }
        
        this.instruction = instruction;
    }
    
    /**
     * Returns the instruction
     * @return instruction generated from this statement
     */
    public String getInstruction() {
        return instruction;
    }
    
    public void setBreakPoint(boolean breakpoint) {
        this.breakpoint = breakpoint;
    }
    
    public boolean isBreakPoint() {
        return breakpoint;
    }
    
    /**
     * Returns the instruction is string format
     * @return instruction generated from this statement
     */
    @Override public String toString() {
        return getInstruction();
    }
}

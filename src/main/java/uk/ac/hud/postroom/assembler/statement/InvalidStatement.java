package uk.ac.hud.postroom.assembler.statement;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.*;

/**
 * An Invalid statement - Used when a line of tokens does not pass a Syntax check
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class InvalidStatement extends Statement {
    
    /**
     * Constructs a new Invalid Statement
     * @param sourceFile SourceFile containing this Statement
     * @param lineNo Line number on which this Statement is located
     * @param tokens Tokens representing this statement
     */
    public InvalidStatement(SourceFile sourceFile, int lineNo, Token[] tokens) {
        // Super-class (Statement) constructor
        super(sourceFile, lineNo, tokens);
    }
}
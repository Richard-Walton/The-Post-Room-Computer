package uk.ac.hud.postroom.assembler.statement;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.exception.*;

/**
 * An Include statement
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class IncludeDirective extends Statement {
    
    /**
     * Constructs a new IncludeStatement
     * @param sourceFile SourceFile containing this Statement
     * @param lineNo Line number on which this Statement is located
     * @param tokens Tokens representing this statement
     * @throws SyntaxError If the tokens do not represent an IncludeStatement
     */
    public IncludeDirective(SourceFile sourceFile, int lineNo, Token[] tokens) throws SyntaxError {
        // Super-class (Statement) constructor
        super(sourceFile, lineNo, tokens);
        
        // First token must be an include directive
        if(!tokens[0].is(TokenType.INCLUDE_DIRECTIVE)) {
            throwSyntaxError(0, "Not an include statement");
        }
        
        // Second token must be a filename
        if(!tokens[1].is(TokenType.FILENAME)){
            throwSyntaxError(1, "Expected filename");
        }
        
        // Third token must be an EOL token
        if(!tokens[2].is(TokenType.EOL)){
            throwSyntaxError(2, "Expected End of line");
        }
    }
    
    /**
     * Returns the filename this statement wishes to Include
     * @return Filename this statement wishes to include
     */
    public String getFileName() {
        return getSourceFile().getParentFile().getPath() + "/" + getTokens()[1].getValue();
    }
}
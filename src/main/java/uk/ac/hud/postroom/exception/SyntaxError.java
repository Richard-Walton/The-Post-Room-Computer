package uk.ac.hud.postroom.exception;

import uk.ac.hud.postroom.assembler.*;

/**
 * A SyntaxError is thrown when a line of SourceCode does not pass Syntax defined 
 * in the uk.ac.hud.postroom.assembler.statement package
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class SyntaxError extends AssemblyError {
    // Text which caused the error
    private String errorText;
    
    /**
     * Constructs a new SyntaxError
     * @param statement Statement which caused the error
     * @param message Reason for the error
     * @param errorTextIndex Index of the token within the Statement which caused the error
     */
    public SyntaxError(Statement statement, String message, int errorTextIndex) {
        // Contructs a new SyntaxError using the text from the token at the given index
        this(statement, message, statement.getTokens()[errorTextIndex].getValue());
    }
    
    /**
     * Constructs a new SyntaxError
     * @param statement Statement which caused the error
     * @param message Reason for the error
     * @param errorText Index of the token within the Statement which caused the error
     */
    public SyntaxError(Statement statement, String message, String errorText) {
        // Super class (AssemblyError) constructor
        super(statement, message);
        
        this.errorText = errorText;
    }

    /**
     * Returns the text in the statement which caused the error
     * @return text in the statement which caused the error
     */
    public String getErrorText() {
        return errorText;
    }
    
    public String toString() {
        StringBuilder string = new StringBuilder(super.toString());
        
        string.append(" (At or around '");
        string.append(getErrorText());
        string.append("')");
        
        return string.toString();
    }
}
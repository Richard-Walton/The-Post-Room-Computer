package uk.ac.hud.postroom.exception;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.*;

/**
 * An Assembly Error can be thrown during assembly after Syntax Checking
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class AssemblyError extends Exception {
    // Statement which caused the error
    private Statement statement;
    
    /**
     * Constructs a new AssemblyError
     * @param statement Statement which caused the error
     * @param message The reason for the error
     */
    public AssemblyError(Statement statement, String message) {
        // Super class (Exception) constructor
        super(message);
        
        this.statement = statement;
    }
    
    /**
     * Returns the statement which caused the error
     * @return Statement which caused the error
     */
    public Statement getStatement() {
        return statement;
    }
    
    /**
     * Returns the SourceFile which the error occured in
     * @return SourceFile which the error occured in
     */
    public SourceFile getSourceFile() {
        return getStatement().getSourceFile();
    }
    
    /**
     * Returns the line number which the error occured
     * @return line number which the error occured
     */
    public int getLineNo() {
        return getStatement().getLineNo();
    }
    
    public String toString() {
        StringBuilder string = new StringBuilder();
        
        string.append(statement.getSourceFile());
        string.append(" (Line ");
        string.append(statement.getLineNo());
        string.append(") :");
        string.append(getMessage());
        
        return string.toString();
    }
}
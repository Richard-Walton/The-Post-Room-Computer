package uk.ac.hud.postroom.assembler;

import uk.ac.hud.postroom.exception.*;

/**
 * A Snap shot of the Assembler at a given stage
 * @author Richard Walton (c0410542@uk.ac.hud)
 */
public class AssemblySnapShot {
    
    /** Name of the SnapShot **/
    private String snapShotName;
    
    /** Statements at this stage **/
    private Statement[] statements;
    
    /** Errors at this stage **/
    private AssemblyError[] errors;
    
    /**
     * Constructs a new AssemblySnapShot with the given Statements and Errors
     * @param statements Statements at this stage of Assembly
     * @param errors Errors found during this stage of Assembly
     */
    public AssemblySnapShot(String snapShotName, Statement[] statements, AssemblyError[] errors) {
        this.snapShotName = snapShotName;
        this.statements = statements;
        this.errors = errors;
    }
    
    /**
     * Returns the name of this snap shot
     * @return name of this snap shot
     */
    public String getSnapShotName() {
        return snapShotName;
    }
    
    /**
     * Returns the Statements at this stage
     * @return Statements at this stage
     */
    public Statement[] getStatements() {
        return statements;
    }
    
    /**
     * Returns the Errors at this stage
     * @return Errors at this stage
     */
    public AssemblyError[] getErrors() {
        return errors;
    }
    
    /**
     * Returns whether any errors were found at this stage
     * @return true if one or more errors were found at this stage
     */
    public boolean hasErrors() {
        return getErrors().length > 0;
    }
}
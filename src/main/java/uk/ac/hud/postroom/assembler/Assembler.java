package uk.ac.hud.postroom.assembler;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.statement.*;
import uk.ac.hud.postroom.exception.*;

public interface Assembler {
    
    /**
     * Assembles a given source file
     * @param sourceFile SourceFile to assemble
     */
    public void assemble(SourceFile sourceFile);
    
    /**
     * Returns the Source File that was assembled
     * @return Source file that was assembled
     */
    public SourceFile getMainSourceFile();
    
    /**
     * Returns all SourceFiles that were used during assembly
     * @return  SourceFiles that were used during assembly
     */
    public SourceFile[] getSourceFiles();
    
    /**
     * Returns the snapshots taken during assembly
     * @return snapshots taken during assembly
     */
    public AssemblySnapShot[] getSnapShots();
    
    /**
     * Returns the instructions generated from the source file(s)
     * @return instructions generated from the source file(s)
     */
    public Instruction[] getInstructions();
    
    /**
     * Returns the macros found during assembly
     * @return macros found during assembly
     */
    public MacroStatement[] getMacros();
    
    /**
     * Returns the labels found during assembly
     * @return labels found during assembly
     */
    public LabelStatement[] getLabels();
    
    /**
     * Returns all errors found during assembly
     * @return errors found during assembly
     */
    public AssemblyError[] getErrors();
    
    /**
     * Returns whether the assembler encountered any errors during assembly
     * @return true if the assembler encountered one or more errors during assembly
     */
    public boolean hasErrors();
    
    /**
     * Returns whether the assembler generated any instructions
     * @return true if the assembler generated one or more instructions
     */
    public boolean hasInstructions();
}
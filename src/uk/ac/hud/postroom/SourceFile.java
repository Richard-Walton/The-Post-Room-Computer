package uk.ac.hud.postroom;

import java.io.*;

/**
 * A .pca SourceFile
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class SourceFile extends File {
    // Source Code contained in the SourceFile
    private String sourceCode;
    
    /**
     * Constructs a new SourceFile using the given filename
     * @param filePath Path to the new SourceFile
     * @throws java.io.IOException thrown if cannot write to the specified path
     */
    public SourceFile(String filePath) throws IOException {
        this(filePath, "");
    }
    
    /**
     * Constructs a new SourceFile using the given filename with the given SourceCode
     * @param filePath Path to the new SourceFile
     * @param sourceCode Default source code
     * @throws java.io.IOException thrown if cannot write to the specified path
     */
    public SourceFile(String filePath, String sourceCode) throws IOException {
        super(filePath);
        
        // Check that can actually write to the specified file
        createNewFile();        
        if(!canWrite()) {
            throw new IOException("Cannot write to file");
        }
        
        // Sets the default source code
        setSourceCode(sourceCode);
    }
    
    /**
     * Returns the source code contained in the SourceFile
     * @return String representation of the source Code contained in the Source File
     */
    public String getSourceCode() {
        return sourceCode;
    }
    
    /**
     * Sets the SourceCode in the SourceFile - Note this method does not save the data to disk
     * @param sourceCode String representation of the source Code
     */
    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
    
    public Project getProject() {
        try { 
            return new Project(getParent());
        }catch (Exception e) { return null; }
    }

    /**
     * Loads a SourceFile from disk using the given path
     * @param filePath Path to the SourceFile
     * @return SourceFile at the specified path
     * @throws java.io.IOException throws if file does not exist
     */
    public static SourceFile load(String filePath) throws IOException {
        FileInputStream input = new FileInputStream(filePath);
        
        byte[] fileData = new byte[input.available()];
        input.read(fileData);
        input.close();
        
        // Returns the new SourceFile
        return new SourceFile(filePath, new String(fileData));
    }
    
    /**
     * Saves a SourceFile to disk
     * @param sourceFile SourceFile to save
     * @throws java.io.IOException throws if an IOException occurs
     */
    public static void save(SourceFile sourceFile) throws IOException {
        FileWriter output = new FileWriter(sourceFile);
        output.write(sourceFile.getSourceCode());
        output.close();
    }
}
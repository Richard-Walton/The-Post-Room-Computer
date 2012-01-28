package uk.ac.hud.postroom;

import java.io.*;
import java.util.*;

/**
 * A Project is a list of SourceFiles which are in one directory
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class Project extends File {

    // List of SourceFiles in the project directory
    private List<SourceFile> sourceFiles;
    
    /**
     * Constructs a new Project using the given path
     * @param projectPath Path to the project directory
     * @throws java.io.IOException thrown if path is not a directory or there 
     * is a problem writing to the directory
     */
    public Project(String projectPath) throws IOException {
        super(projectPath);
        
        // Error checking
        if(! isDirectory()) {
            throw new IOException("Specified path is not a directory");
        }
        
        if(! canWrite()) {
            /* Windows XP and Vista do not always return correct values for 
             can write? Meaning this check has to be commented out */
          //  throw new IOException("Cannot write to project directory");
        }
        
        sourceFiles = new ArrayList<SourceFile>();
        
        // Loads all the .pca SourceFiles
        refreshProject();
    }
    
    /**
     * Creates and adds a new SourceFile to the project
     * @param filePath Path to the SourceFile (must be in the same directory)
     * @return The SourceFile that was created
     * @throws java.io.IOException If the source file already exists
     */
    public SourceFile addNewSourceFile(String filePath) throws IOException {
        SourceFile sourceFile = new SourceFile(filePath, "");
        
        if(sourceFiles.contains(sourceFile)) {
            throw new IOException("Source file already exists");
        }
        
        sourceFiles.add(sourceFile);
        
        return sourceFile;
    }
    
    /**
     * Deletes (from disk) and removes a SourceFile from the project
     * @param sourceFile SourceFile to be deleted
     * @throws java.io.IOException If the sourceFile is not contained in this project
     */
    public void deleteSourceFile(SourceFile sourceFile) throws IOException {
        if(! sourceFiles.contains(sourceFile)) {
            throw new IOException("Source file does not exist in this project");
        }
        
        sourceFile.delete();
        sourceFiles.remove(sourceFile);
    }
    
    /**
     * Deletes and removes all source files in the project
     * @throws java.io.IOException thrown if unable to delete a file
     */
    public void deleteSourceFiles() throws IOException {
        for(SourceFile sourceFile : getSourceFiles()) {
            deleteSourceFile(sourceFile);
        }
    }
    
    /**
     * Deletes and removes all source files - if the directory is empty then the 
     * directory is also deleted
     * @throws java.io.IOException thrown if unable to delete a file
     */
    public void deleteProject() throws IOException {
        deleteSourceFiles();
        
        if(listFiles().length == 0) {
            delete();
        }
    }
    
    /**
     * Saves all source files and reloads them - will then load any files which
     * have been placed in the directory externally
     * @throws java.io.IOException if unable to save or load a file
     */
    public void refreshProject() throws IOException {     
        sourceFiles.clear();
        
        // Finds all .pca files and loads them
        for(File file : listFiles()) {
            if(file.getName().endsWith(".pca")) {
                sourceFiles.add(SourceFile.load(file.getPath()));
            }
            
            if(file.getName().endsWith(".pco")) {
                sourceFiles.add(SourceFile.load(file.getPath()));
            }
        }
    }
        
    /**
     * Returns all the SourceFiles currently loaded in this project
     * @return SourceFiles currently loaded in this project
     */
    public SourceFile[] getSourceFiles() {
        return sourceFiles.toArray(new SourceFile[]{});
    } 
}
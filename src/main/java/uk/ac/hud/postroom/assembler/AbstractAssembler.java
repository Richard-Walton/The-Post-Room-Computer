package uk.ac.hud.postroom.assembler;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.exception.*;
import uk.ac.hud.postroom.assembler.statement.*;

import java.util.*;

/**
 * Assembles a Source File into Post Room Computer Machine Code
 * @author Richard Walton (c0410542@uk.ac.hud)
 */
public abstract class AbstractAssembler implements Assembler {

    /** The Source File being assembled **/
    protected SourceFile sourceFile;
    
    /** List of Source Files referenced **/
    protected List<SourceFile> sourceFiles;
    
    /** Snapshots of the assembly progress **/
    protected List<AssemblySnapShot> snapshots;
    
    /** List of statements being assembled **/
    protected List<Statement> statements;
    
    protected List<Instruction> instructions;
    
    /** List of errors encountered during assembly **/
    protected List<AssemblyError> errors;
    
    /** List of macros found during assembly **/
    protected List<MacroStatement> macros;
    
    /** Map of labels found during assembly and the address they represent **/
    protected Map<LabelStatement, Integer> labels;
    
    public AbstractAssembler() {
        sourceFiles = new ArrayList<SourceFile>();
        snapshots = new ArrayList<AssemblySnapShot>();
        statements = new ArrayList<Statement>();
        instructions = new ArrayList<Instruction>();
        errors = new ArrayList<AssemblyError>();
        macros = new ArrayList<MacroStatement>();
        labels = new Hashtable<LabelStatement, Integer>();
    }
    
    public abstract void assemble(SourceFile sourceFile);
    
    /** @inherhitDoc **/
    public SourceFile getMainSourceFile() {
        return sourceFile;
    }
    
    /** @inherhitDoc **/
    public SourceFile[] getSourceFiles() {
        return sourceFiles.toArray(new SourceFile[]{});
    }
    
    /** @inherhitDoc **/
    public AssemblySnapShot[] getSnapShots() {
        return snapshots.toArray(new AssemblySnapShot[]{});
    }
    
    /** @inherhitDoc **/
    public MacroStatement[] getMacros() {
        return macros.toArray(new MacroStatement[]{});
    }
    
    /** @inherhitDoc **/
    public LabelStatement[] getLabels() {
        return labels.keySet().toArray(new LabelStatement[]{});
    }
    
    /** @inheritDoc **/
    public Instruction[] getInstructions() {
        return instructions.toArray(new Instruction[]{});
    }
    
    /** @inherhitDoc **/
    public boolean hasInstructions() {
        return getInstructions().length > 0;
    }
    
    /** @inherhitDoc **/
    public AssemblyError[] getErrors() {
        errors.clear();
        
        for(AssemblySnapShot snapShot : getSnapShots()) {
            errors.addAll(Arrays.asList(snapShot.getErrors()));
        }
        
        return errors.toArray(new AssemblyError[]{});
    }
    
    /** @inherhitDoc **/
    public boolean hasErrors() {
        return getErrors().length > 0;
    }
    
    /**
     * Resets the assembler ready to assemble the specified file
     * @param sourceFile Next source file to assemble
     */
    protected void reset(SourceFile sourceFile) {
        this.sourceFile = sourceFile;
        
        sourceFiles.clear();
        snapshots.clear();
        statements.clear();
        instructions.clear();
        errors.clear();
        macros.clear();
        labels.clear();   
    }
    
    /**
     * Gets the statements from the given Source File.  Any include directives 
     * recursively call this method until the entire Statement list is built
     * @param sourceFile Source File to get the statement list from
     */
    protected void includeStatementsFrom(SourceFile sourceFile) {
        // Logs which unique source files are being used during assembly
        if(! sourceFiles.contains(sourceFile)) {
            sourceFiles.add(sourceFile);
        }
        
        SyntaxAnalyser analyser = new SyntaxAnalyser(sourceFile);
        
        // logs all errors encountered by the Syntax Analyser
        errors.addAll(Arrays.asList(analyser.getErrors()));
        
        /**
         * Loops through all the statements in the Source file.  
         * 
         * - Any Include Directives are used to load the specificed Source File
         * and recursively build the statements contained within that file.
         * 
         * - All other statements (apart from EOL and Invalid statements) are 
         * added to the statement list.
         * 
         * The final statement list is then translated by the Assembler into 
         * machine code
         */
        for(Statement statement : analyser.getStatements()) {
            if(statement instanceof IncludeDirective) {
                String fileName = ((IncludeDirective) statement).getFileName();
                try {
                    SourceFile includedFile = SourceFile.load(fileName);
                    
                    // Stop some problems with include loops
                    if(includedFile.equals(statement.getSourceFile())) {
                        logError(statement, "Source Files cannot include themselves");
                    }else {
                        includeStatementsFrom(includedFile);
                    }
                    
                }catch (Exception ioError) {
                    // Source File does not exist or an IO error occured.
                    logError(statement, ioError.getMessage());
                }
            }else if(!(statement instanceof InvalidStatement)) {
                statements.add(statement);
            }
        }
    }
    
    /**
     * Loops through the statement list and logs any unique macros which are 
     * defined within it.  
     */
    protected void buildMacroList() {
        ListIterator<Statement> statementList = statements.listIterator();
                
        // Loops through all statements in the statement list
        while(statementList.hasNext()) {
            Statement statement = statementList.next();
            
            if(statement instanceof MacroStatement) {
                // Found macro. Remove from statement list
                statementList.remove();
                
                MacroStatement macro = (MacroStatement) statement;
                
                if(! macros.contains(macro)) {
                    // Unique macro name found - store macro
                    macros.add(macro);
                }else {
                    // Macro with same name already exists.  Log error
                    MacroStatement existingMacro = macros.get(macros.indexOf(macro));
                    
                    logError(statement, 
                            "A Duplicate macro '" + existingMacro.getName() + "'" + 
                            "is already defined on line " + existingMacro.getLineNo() +
                            " of '" + existingMacro.getSourceFile().getName() + "'");
                }
            }
        }
    }
    
    /**
     * Expands any macros in the statement list
     */
    protected void expandMacros() {
        ListIterator<Statement> statementList = statements.listIterator();
        
        // Loops through all statements in the statement list
        boolean reExpand = false;
        while(statementList.hasNext()) {
            
            Statement statement = statementList.next();
            
            if(statement instanceof MacroCallStatement) {
                // Found macro call.  Remove from statement list
                statementList.remove();
                
                MacroCallStatement macroCall = (MacroCallStatement) statement;
                MacroStatement macro = null;
                
                // Find the macro which the MacroCall statement represents.
                for(MacroStatement aMacro : getMacros()) {
                    if(macroCall.getMacroName().equals(aMacro.getName())) {
                        macro = aMacro;
                        // Found macro - break from loop
                        break;
                    }
                }
                
                if(macro != null) {
                    // Macro found - try and expand it using the Macro Call
                    try {
                        for(Statement macroStatement : macro.expand(macroCall)) {
                            // add macro statement
                            statementList.add(macroStatement);
                        }
                        
                        /* As Macro's can call other macros we must recheck the 
                         * statement list for more macro call statements **/
                        reExpand = true;
                    }catch (AssemblyError e) {
                        // Some error occured during expansion
                        errors.add(e);
                    }
                }else {
                    // No macro was found with the name specified in the macro call
                    logError(statement, 
                            "Macro '" + macroCall.getMacroName() + "' " +
                            "has not been defined.");
                }
            }
        }
        
        // Recheck statement list for more macro calls
        if(reExpand) {
            expandMacros();
        }
    }
    
    /**
     * Loops through the statement list and logs any unique labels which are 
     * defined within it.  
     */
    protected void buildLabelList() {
        ListIterator<Statement> statementList = statements.listIterator();
        int memoryLocation = 0;
        
        // Loops through all statements in the statement list
        while(statementList.hasNext()) {
            Statement statement = statementList.next();
            
            if(statement instanceof LabelStatement) {
                // Found Label.  Remove from statement list
                statementList.remove();
                
                LabelStatement label = (LabelStatement) statement;
                
                if(label.isAddressJump()) {
                    String address = label.getLabelName();
                    memoryLocation = Integer.parseInt(address.substring(1, address.length() - 1));
                }
                if(!labels.containsKey(label)) {
                    // Unique label found - store Label->Address map
                    labels.put(label, memoryLocation);
                    
                    // Replace label with the statement(s) at that address
                    Statement labelStatement = label.getStatement();
                    if(labelStatement != null) {
                        // StringStatements are actualy an array of DataStatements
                        if(labelStatement instanceof StringStatement) {
                            StringStatement string = (StringStatement) labelStatement;
                            
                            // Add all the data statements
                            for(DataStatement data : string.getDataStatements()) {
                                statementList.add(data);
                                
                                data.setMemoryLocation(memoryLocation);
                                memoryLocation ++;
                            }
                        }else {
                            // All other statements are singular
                            statementList.add(labelStatement);
                            labelStatement.setMemoryLocation(memoryLocation);
                        }
                    }else {
                        memoryLocation --;
                    }
                }else {
                    // Label with same name already exists.  Log error
                    Statement existingLabel = statements.get(labels.get(label));
                    
                    logError(statement,                             
                            "Label '" + label.getLabelName() + "' is already defined " +
                            "on line " + existingLabel.getLineNo() + " of '" +
                            existingLabel.getSourceFile().getName() + "'");
                }
            }else {
                statement.setMemoryLocation(memoryLocation);
            }
            
            memoryLocation ++;
        }
        
    }
    
    /**
     * Rebuilds the statement list with statements constructed after Label tokens
     * have been replaced with Address tokens
     */
    protected void substituteLabels() {
        ListIterator<Statement> statementList = statements.listIterator();
        
        // Loops through all statements in the statement list
        while(statementList.hasNext()) {
            Statement statement = statementList.next();
            
            if(statement instanceof OpCodeStatement) {
                /* Found OpCode (Only type of statement which requires label 
                 * replacement) - Remove from statement list */
                statementList.remove();
                
                /**
                 * Replaces Label tokens contained within the statement 
                 * with corresponding address tokens
                 */
                ArrayList<Token> substitutedTokens = new ArrayList<Token>();
                for(Token token : statement.getTokens()) {
                    if(token.is(TokenType.LABEL)) {
                        // Label found.
                        String address = getAddressFor(token.getValue());
                        
                        if(address != null) {
                            
                            // Address found - add new Address token
                            substitutedTokens.add(new Token(TokenType.ADDRESS, address));
                        }else {
                            // No address found for the label - log error
                            logError(statement, 
                                    "Label '" + token.getValue() + "' " +
                                    "has not been defined");
                        }
                    }else if (token.is(TokenType.REGISTER)) {
                        String register = token.getValue();
                        int labelIndex = register.indexOf(':');
                        if(labelIndex > -1) {
                            String addressMode = register.substring(0, labelIndex);
                            String label = register.substring(labelIndex);
                            String address = getAddressFor(label);
                            if(address != null) {
                                substitutedTokens.add(new Token(
                                    TokenType.REGISTER, addressMode + address));
                            }else {
                                // No address found for the label - log error
                                logError(statement, 
                                        "Label '" + token.getValue() + "' " +
                                        "has not been defined");
                            }
                        }else {
                           substitutedTokens.add(token); 
                        }
                    }else {
                        // Not a label token - add to new token array
                        substitutedTokens.add(token);
                    }
                }
                
                // Re-creates the OpCode statement with the address only token array
                try {
                    OpCodeStatement newStatement = new OpCodeStatement(
                            statement.getSourceFile(), 
                            statement.getLineNo(),
                            substitutedTokens.toArray(new Token[]{}));
                    newStatement.setMemoryLocation(statement.getMemoryLocation());
                    
                    statementList.add(newStatement);
                }catch (SyntaxError e) {
                    // Don't log as substitution errors have already been logged earlier
                }
            }
        }
    }
    
    /** 
     * Returns the address which the specified label points to
     * @param label Label to evaluate
     * @return Address which the label points to or NULL
     */
    protected String getAddressFor(String label) {
        String address = null;
        
        for(LabelStatement labelStatement : getLabels()) {
            if(label.equals(labelStatement.getLabelName())) {
                address = labels.get(labelStatement).toString();
            }
        }
        
        if(address != null) {
            while(address.length() < 2) {
                address = "0" + address;
            }
        }
        
        return address;
    }
    
    /** 
     * Logs and Assembly Error
     * @param statement Statement which contains the error
     * @param reason The reason an error occured
     */
    protected void logError(Statement statement, String reason) {
        errors.add(new AssemblyError(statement, reason));
    }
    
    /** 
     * Takes a snapshot of the current statement and error lists
     * @param snapShotName Name of the snapshot
     */
    protected void takeSnapShot(String snapShotName) {
        snapshots.add(new AssemblySnapShot(
                snapShotName,
                statements.toArray(new Statement[]{}), 
                errors.toArray(new AssemblyError[]{})));
        
        errors.clear();  
    }
}
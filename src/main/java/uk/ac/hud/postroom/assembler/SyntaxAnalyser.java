package uk.ac.hud.postroom.assembler;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.exception.*;
import uk.ac.hud.postroom.assembler.statement.*;

import java.util.*;

/**
 * Converts source code into Statements based upon Syntax (
 * Any Syntax Errors are logged)
 * 
 * @author Richard Walton (c0410542@uk.ac.hud)
 */
public class SyntaxAnalyser {
    /** Source File used to generate Statements from **/
    private SourceFile sourceFile;
    
    /** Tokens from the SourceCode of the Source File
     * (minus Comments and Whitespace) used to check Syntax**/
    private Token[][] cleanTokens;
    
    /** All Statements generated from the SourceFile **/
    private List<Statement> statements;
    
    /** Include Statements **/
    private List<IncludeDirective> includes;
    
    /** Label Statements **/
    private List<LabelStatement> labels;
    
    /** Macro statements **/
    private List<MacroStatement> macros;
    
    /** Syntax Errors **/
    private List<SyntaxError> errors;
    
    /**
     * Constructs a new SyntaxAnalysers and generates statements, based upon 
     * Syntax, from the given SourceFile
     * @param sourceFile SourceFile to analyse
     */
    public SyntaxAnalyser(SourceFile sourceFile) {
        this.sourceFile = sourceFile;
        
        // Tokenizes the entire SourceFile
        Token[][] tokens = new Tokenizer(sourceFile.getSourceCode()).tokenize();
        
        /* Remove Comment and Whitespace tokens from original tokens. These 
         * tokens are used build statements as they are easier to analyse */
        cleanTokens = new Token[tokens.length][];
        for(int i = 0 ; i < tokens.length; i++){
            ArrayList<Token> line = new ArrayList<Token>();
            for(Token token : tokens[i]){
                if(! (token.is(TokenType.COMMENT) || token.is(TokenType.WHITESPACE))){
                    line.add(token);
                }
            }
            cleanTokens[i] = line.toArray(new Token[]{});
        }
        
        // Constuct arrays used to store generated information
        statements = new ArrayList<Statement>();
        includes = new ArrayList<IncludeDirective>();
        labels = new ArrayList<LabelStatement>();
        macros = new ArrayList<MacroStatement>();
        errors = new ArrayList<SyntaxError>();
        
        /**
         * Generates the statements within the SourceFile based upon Syntax.
         * Any Syntax Errors are logged
         */
        
        // Loop through all lines of Source Code
        for(int i = 0; i < cleanTokens.length; i++){
            Statement statement = null; // Statement which will be generated
            try{
                /* Construct a new Statement based upon the type of Token
                 * at the start of the line e.g. OpCode token -> OpCodeStatment */
                switch(cleanTokens[i][0].getType()) {
                    case INCLUDE_DIRECTIVE :
                        // New Include statement
                        statement = new IncludeDirective(sourceFile, i, cleanTokens[i]);
                        // Add to separate Include statement Array
                        includes.add((IncludeDirective) statement);
                        break;
                        
                    case MACRO_START :
                        try {
                            // Need to parse as Macros span multiple lines
                            statement = parseMacroStatement(i);
                            // Add to separate Macro statement array
                            macros.add((MacroStatement) statement);
                        
                            // Skip past lines which are part of the Macro
                            i = i + ((MacroStatement) statement).getBody().length;
                        }catch (SyntaxError e) {
                            /* The macro has a Syntax Error, so skip past the 
                             macro body to stop multpile macro body errors being reported **/
                            while(i++ < cleanTokens.length) {
                                TokenType type = cleanTokens[i][0].getType();
                                if(type == TokenType.MACRO_END || type == TokenType.MACRO_START){
                                    break;
                                }
                            }
                            // Re-throw the exception
                            throw e;
                        }
                        break;

                    case LABEL :
                    case LABEL2 :
                        // New Label statement
                        statement = new LabelStatement(sourceFile, i, cleanTokens[i]);
                        // Add to separate Label statement Array
                        labels.add((LabelStatement) statement);
                        break;
                        
                    case OPCODE :
                        // New OpCode statement
                        statement = new OpCodeStatement(sourceFile, i, cleanTokens[i]);
                        break;
                        
                    case EOL :
                        //i.e no input on this line
                        break;

                    case WORD :
                        // Possible macro call - Can't tell at this stage
                        statement = new MacroCallStatement(sourceFile, i, cleanTokens[i]);
                        break;
                        
                    case DATA : 
                        statement = new DataStatement(sourceFile, i, cleanTokens[i]);
                        break;
                        
                    case INSTRUCTION :
                        statement = new Instruction(sourceFile, i, cleanTokens[i]);
                        break;
                        
                    default :
                        // Invalid start of line
                        statement = new InvalidStatement(sourceFile, i, cleanTokens[i]);
                        throw new SyntaxError(statement, "Invalid start of line", 0);
                }   
            }catch (SyntaxError e) {
                // Log Syntax Errors thrown during construction of a statement
                errors.add(e);
                
                // Construct new Invalid Statement
                statement = new InvalidStatement(sourceFile, i, cleanTokens[i]);
            }
            
            // Add new statement to array
            if(statement != null) {
                statements.add(statement);
            }
        }
    }
    
    /**
     * Constructs a MacroStatement which starts at the given line number
     * @param lineNo Line Number the macro starts on
     * @return MacroStatement which starts at the given line number
     * @throws SyntaxError re-thrown from Macro_Statement construction
     */
    private MacroStatement parseMacroStatement(int lineNo) throws SyntaxError {
        List<Token[]> macroBody = new ArrayList<Token[]>();
        
        /* Adds each line of Tokens which are in the macro start into an array.
         * If a Macro_End / Macro_Start token is found the loop is broken */ 
        for(int i = lineNo + 1; i < cleanTokens.length; i++){
            macroBody.add(cleanTokens[i]);
            TokenType type = cleanTokens[i][0].getType();
            if(type == TokenType.MACRO_END || type == TokenType.MACRO_START){
                break;
            }
        }
        
        // Returns new MacroStatement
        return new MacroStatement(sourceFile, lineNo, 
                cleanTokens[lineNo], macroBody.toArray(new Token[][]{}));
    }
    
    /**
     * Source File used to generate Statements from
     * @return Source File used to generate Statement from 
     */
    public SourceFile getSourceFile() {
        return sourceFile;
    }
    
    /**
     * Returns the number of lines of Source code in the Source File
     * @return number of linesof Source code in the Source File
     */
    public int getLineCount() {
        return cleanTokens.length;
    }
    
    /**
     * Returns Statements generated from the Source Code in the Source File
     * @return Statement array
     */
    public Statement[] getStatements() {
        return statements.toArray(new Statement[]{});
    }
    
    /**
     * Returns Include Statements generated from the Source Code in the Source File
     * @return Include Statement array
     */
    public IncludeDirective[] getIncludeStatements() {
        return includes.toArray(new IncludeDirective[]{});
    }
    
    /**
     * Returns Label Statements generated from the Source Code in the Source File
     * @return Label Statement array
     */
    public LabelStatement[] getLabelStatements() {
        return labels.toArray(new LabelStatement[]{});
    }
    
    /**
     * Returns Macro Statements generated from the Source Code in the Source File
     * @return Macro Statement array
     */
    public MacroStatement[] getMacroStatements() {
        return macros.toArray(new MacroStatement[]{});
    }
    
    /**
     * Returns whether any SyntaxErrors were found
     * @return true is one or more SyntaxErrors were found during analysis
     */
    public boolean hasErrors() {
        return errors.size() > 0;
    }
    
    /**
     * Returns SyntaxErrors found during analysis of the Source Code in the Source File
     * @return SyntaxError array
     */
    public SyntaxError[] getErrors() {
        return errors.toArray(new SyntaxError[]{});
    }
}
package uk.ac.hud.postroom.assembler;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.statement.*;
import uk.ac.hud.postroom.exception.*;
import java.util.*;

public class AbsoluteAddressAssembler extends AbstractAssembler {
    
    public void assemble(SourceFile sourceFile) {
        reset(sourceFile);
        
        try {
            includeStatementsFrom(sourceFile);
        }catch (StackOverflowError e) {
            // Include Directive loops are hard to check for.  Wait for stack overflow instead
            logError(new InvalidStatement(sourceFile, 0, new Token[]{}), "Include directive loop");
        }
        
        buildMacroList();
        
        takeSnapShot("Initial Statement List");

        expandMacros();
        
        if(getMacros().length > 0 || errors.size() > 0) {
            takeSnapShot("Macros expanded");
        }
        
        buildLabelList();
        substituteLabels();
        
        if(getLabels().length > 0 || errors.size() > 0) {
            takeSnapShot("Labels substituted");
        }
        
        translate();
        
        takeSnapShot("Translation");
    }
    
   /**
     * Loops through the statement list and translates each statement into 
     * Post Room Computer machine code
     */
    private void translate() {
        ListIterator<Statement> statementList = statements.listIterator();
        
        // Loops through all statements in the statement list
        while(statementList.hasNext()) {
            Statement statement = statementList.next();
            
            statementList.remove();
            
            // Translated instruction
            StringBuilder translation = new StringBuilder();
            
            /* At this point in assembly only OpCode and/or Data statements are
             * in the statement list */
            if(statement instanceof OpCodeStatement) {
                // OpCode found
                OpCodeStatement opCode = (OpCodeStatement) statement;
                
                translation.append(opCode.getOpCode().getValue());
                
                /**
                 * Translate each token into machine code
                 */
                for(Token token : opCode.getTokens()) {
                    switch(token.getType()) {                            
                        // ADDRESS tokens are translated into 3 digit addresses
                        case ADDRESS :
                            StringBuilder address = new StringBuilder(token.getValue());
                            while(address.length() < 3) {
                                address.insert(0, "0");
                            }
                            
                            translation.append(address.toString());
                            break;
                            
                        // CHARACTER tokens are translated into 3 digit ascii values
                        case CHARACTER :
                            StringBuilder character = new StringBuilder(
                                    Integer.toString((int) token.getValue().charAt(1)));
                            
                            while(character.length() < 3) {
                                character.insert(0, "0");
                            }
                            
                            translation.append(character);
                            break;

                        // JMP Condition codes
                        case CONDITION :
                            StringBuilder code = new StringBuilder();
                            code.append(Condition.getByMnemonic(token.getValue()).getValue());
                            while(code.length() < 3) {
                                code.insert(0, "0");
                            }
                            
                            translation.append(code.toString());
                            break;
                            
                        // REGISTER tokens
                        case REGISTER :
                            logError(statement, "Cannot use register address functions in absolute address computer");
                            break;
                            
                        // EOL tokens can finally be discarded.    
                        case EOL :
                            break;
                    }
                }
                
                /* OpCode statements can contain optional parameters.  
                 * If these have not been specified the instruction needs padding */
                while(translation.length() < 7) {
                    translation.append("000");
                }
                
            }else if(statement instanceof DataStatement) {
                // Data found.  Simply add the data it contains in Token format.
                translation.append(((DataStatement) statement).getData());
            }else {
                logError(statement, "Invalid statmenent");
            }
            
            // Store the instruction
            try {
                Instruction instruction = new Instruction(statement, translation.toString());
                instruction.setMemoryLocation(statement.getMemoryLocation());
                
                statementList.add(instruction);
                instructions.add(instruction);
            }catch (AssemblyError e) {
                errors.add(e);
            }
        }
    }
}
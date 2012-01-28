package uk.ac.hud.postroom.assembler;

import uk.ac.hud.postroom.*;
import java.util.*;

public class MachineCodeAssembler extends AbstractAssembler {

    public void assemble(SourceFile sourceFile) {
        reset(sourceFile);
        
        SyntaxAnalyser analyser = new SyntaxAnalyser(sourceFile);
        
        errors.addAll(Arrays.asList(analyser.getErrors()));
        
        for(Statement statement : analyser.getStatements()) {
            if(statement instanceof Instruction) {
                statements.add(statement);
                instructions.add((Instruction) statement);
            }else {
                logError(statement, "Not a machine code statement");
            }
        }
        
        takeSnapShot("Instruction List");
    }
}
package uk.ac.hud.postroom.assembler.statement;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.exception.*;

import java.util.*;

/**
 * A MacroStatement.  This statement 
 * @author richard
 */
public class MacroStatement extends Statement {
    
    private String name;
    private Token[][] body;
    private List<String> arguments;
    private int expansionCount;
    
    public MacroStatement(SourceFile sourceFile, int lineNo, 
            Token[] definition, Token[][] body) throws SyntaxError {
        super(sourceFile, lineNo, definition);

        if(!definition[0].is(TokenType.MACRO_START)){
            throwSyntaxError(0, "Not a macro");
        }
        
        if(!body[body.length - 1][0].is(TokenType.MACRO_END)){
            throwSyntaxError(0, "No macro end found");
        }
        
        if(body.length == 1){
            throwSyntaxError(0, "No macro body defined");
        }
        
        name = definition[0].getValue().substring(1);
        this.body = body;
        arguments = new ArrayList<String>();
        expansionCount = 0;
        
        for(int i = 1; i < definition.length; i++) {
            if(definition[i].is(TokenType.WORD)){
                String argument = definition[i].getValue();
                if(arguments.contains(argument)){
                    throwSyntaxError(i, "Argument '" + argument + "' repeated");
                }
                
                arguments.add(argument);
            }else if(!definition[i].is(TokenType.EOL)){
                throwSyntaxError(i, "Unexpected  " + definition[i].getType() + " token");
            }
        }  
    }
    
    /**
     * Returns the name of the macro
     * @return Name of the macro
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the parameter names used in the macro
     * @return parameter names used in the macro
     */
    public String[] getArgumentLabels() {
        return arguments.toArray(new String[]{});
    }
    
    /**
     * Returns the number of arguments needed to call this macro
     * @return number of arguments needed to call this macro
     */
    public int argumentCount() {
        return arguments.size();
    }
    
    /**
     * Returns the body of the macro as it was defined in the source code
     * @return body of the macro as it was defined in the source code
     */
    public Token[][] getBody() {
        return body;
    }
    
    /**
     * Using the given MacroCall any applicable parameter substitution is made
     * before the macro body statmenets are returned.   
     * @param macroCall The statement used to call this macro
     * @return Macrobody statements (The expanded macro)
     * @throws uk.ac.hud.postroom.exception.SyntaxError If the given MacroCall
     * is not compatable (e.g. wrong operand count)
     */
    public Statement[] expand(MacroCallStatement macroCall) throws SyntaxError {
        
        if(!macroCall.getMacroName().equals(getName())){
            throw new SyntaxError(macroCall, "Invalid macro call for this macro", 0);
        }
            
        if(macroCall.operandCount() != argumentCount()) {
            throw new SyntaxError(macroCall, "Incorrect number of operand passed " +
                    "to macro '" + getName() + "' (arity " + argumentCount() + ")", 0);
        }
        
        Token[] operands = macroCall.getOperandTokens();
        
        List<Statement> expanded = new ArrayList<Statement>();
        for(int i = 0; i < body.length - 1; i++){
            ArrayList<Token> tokens = new ArrayList<Token>();
            for(Token token : body[i]) {
                String tokenValue = token.getValue();
                
                if(arguments.contains(tokenValue)) {
                    tokens.add(operands[arguments.indexOf(tokenValue)]);
                }else if (token.is(TokenType.LABEL)) {
                    String label = tokenValue.substring(1, tokenValue.length() -1);
                    if(label.startsWith(".")) {
                        label = ":" + label.substring(1) + ":";
                    }else {
                        label = ":" + label + expansionCount + ":";
                    }
                    
                    tokens.add(new Token(TokenType.LABEL, label));
                }else{
                    tokens.add(new Token(token.getType(), tokenValue));
                }
            }
            
            Token[] line = tokens.toArray(new Token[]{});
            int lineNo = getLineNo() + i  +1;
            
            Statement statement = null;
            
            switch(line[0].getType()){
                case OPCODE :
                    statement = new OpCodeStatement(getSourceFile(), lineNo, line);
                    break;
                        
                case LABEL :
                    statement = new LabelStatement(getSourceFile(), lineNo, line);
                    break;
                case WORD: 
                    statement = new MacroCallStatement(getSourceFile(), lineNo, line);
                    if(((MacroCallStatement) statement).getMacroName().equals(this.getName())) {
                        throw new SyntaxError(statement, "Macros cannot call themselves", 0);
                    }
                    statement = new MacroCallStatement(getSourceFile(), lineNo, line);
                    break;
                        
                case EOL :
                    break;
                        
                default :
                    statement = new InvalidStatement(getSourceFile(), lineNo, line);
                    throw new SyntaxError(statement, "Macro bodies can only contain " +
                            "OpCode, Label, or MacroCall statements", 0);
                }
            
            if(statement != null){
                expanded.add(statement);
            }
        }
        
        expansionCount++;
        return expanded.toArray(new Statement[]{});
    }
    
    @Override public int hashCode() {
        return 54321;
    }
    
    @Override public boolean equals(Object object) {
        if(object instanceof MacroStatement) {
            return getName().equals(((MacroStatement) object).getName());
        }else{
            return false;
        }
    }
}
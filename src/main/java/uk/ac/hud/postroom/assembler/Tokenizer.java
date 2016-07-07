package uk.ac.hud.postroom.assembler;

import java.util.*;
import java.util.regex.*;

/**
 * Converts SourceCode into tokens
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class Tokenizer {
    /** Input to tokenize **/
    private String input;
    
    /** Current offset to produce next token from **/
    private int offset;
    
    /** Regular expression to split input into Whitespace OR Text blocks **/
    private static Pattern split = Pattern.compile("[\\s&&[^\r\n|^\n]]+|\\S+", Pattern.UNIX_LINES);
    
    /**
     * Constructs a new Tokenizer using the given input
     * @param input Input (SourceCode) to tokenize
     */
    public Tokenizer(String input) {
        setInput(input);
    }
    
    /**
     * Resets the tokenizer to tokenize from the beginning of the given input
     * @param input Input (SourceCode) to tokenize
     */
    public void setInput(String input) {
        this.input = input;
        setOffset(0);
    }
    
    /**
     * Returns the input which is being tokenized
     * @return Input (SourceCode) being tokenized
     */
    public String getInput() {
        return input;
    }

    /**
     * Sets the offset within the Input to produce the next token from
     * @param offset Offset within the Input (SourceCode) to produce the next 
     * token from
     */
    public void setOffset(int offset) {
        if(offset >= 0 && offset <= getInput().length()){
            this.offset = offset;
        }else{
            /* Throw exception if the offset is out of bounds 
             (below 0 or above the length of the input) */
            throw new IndexOutOfBoundsException();
        }
    }
    
    /**
     * Returns the offset from which the next token will be produced from
     * @return the offset within the input (SourceCode) the next token will be 
     * produced from
     */
    public int getOffset() {
        return offset;
    }
    
    /**
     * Shifts the offset by the given number of places
     * @param shift places to shift the offset by (can be negative)
     */
    public void shiftOffset(int shift) {
        setOffset(getOffset() + shift);
    }
    
    /**
     * Returns whether the tokenizer can produce more tokens from the current offset
     * @return true if the tokenizer can produce more tokens from the current offset
     */
    public boolean hasMoreTokens() {
        return getOffset() < getInput().length();
    }
    
    /**
     * Gets the token at the given offset
     * @param offset offset to get the token
     * @return Token at the given offset
     */
    public Token getTokenAtOffset(int offset) {
        // Keep reference to old offset
        int oldOffset = getOffset();

        int offsetInLine = 0;
        // Find start of the token
        while(offset > 0 && input.charAt(offset - 1) != '\n') {
            offset--;
            offsetInLine++;
        }

        // Set the new offset and get the token at that location
        setOffset(offset);
        
        Token[] tokens = nextLine();
        Token tokenAtOffset = null;
        int location = 0;
        for(Token token : tokens) {
            location = location + token.getValue().length();
            if(location > offsetInLine) {
                tokenAtOffset = token;
                break;
            }
        }
        
        // Reset the offset to its original value
        setOffset(oldOffset);
        
        return tokenAtOffset;
    }
    
    /**
     * Tokenizes the entire input from the current offset into a 2D token array
     * @return 2D token array
     */
    public Token[][] tokenize() {
        List<Token[]> tokens = new ArrayList<Token[]>();
        
        // Adds lines of tokens to an array until no more tokens can be generated
        while(hasMoreTokens()){
            tokens.add(nextLine());
        }
        
        // Returns a static 2D token array
        return (Token[][]) tokens.toArray(new Token[][]{});
    }
    
    /**
     * Tokenizes the entire line of input from the current offset into a 2D token array
     * @return 2D token array
     */
    public Token[] nextLine() {
        List<Token> tokens = new ArrayList<Token>();
        
        // Adds tokens to an array until the EOL token is found
        Token token;
        while(! (token = nextToken()).is(TokenType.EOL)){
            tokens.add(token);
        }

        // Adds the EOL token missed in the above loop
        tokens.add(token);

        // Returns a static Token array
        return (Token[]) tokens.toArray(new Token[]{}); 
    }

    /**
     * Returns the next token from the curret offset
     * @return next token
     */
    public Token nextToken() {
        // Returns EOL token if the character at the current offset is a NewLine

        // Gets any untokenized input
        String unTokenizedInput = getInput().substring(getOffset());

        try{
            if(unTokenizedInput.charAt(0) == '\n') {
                // Shift past the EOL
                shiftOffset(1);
                return new Token(TokenType.EOL, " ");

            }else if(unTokenizedInput.charAt(0) == '\r' && unTokenizedInput.charAt(1) == '\n') {
                // \r\n for Windows!
                shiftOffset(2);
                return new Token(TokenType.EOL, "  ");  
            }
        }catch (Exception e){
            // Return EOL token when tokenizer has reached the end of the input
            return new Token(TokenType.EOL, "");
        }

        /* Gets the next word (either a block of whitespace or text) from the 
         * unTokenizedInput (any input past the current offset) */

        Matcher splitter =  split.matcher(unTokenizedInput);
        splitter.lookingAt();

        String word = unTokenizedInput.substring(0, splitter.end()); 

        /* Attempts to match the next word of untokenized input to one of the 
         * regular expressions delared as a TokenGrammar */
        for(TokenGrammar grammar : TokenGrammar.values()){
            Matcher matcher = grammar.matcher(word);
            // Check if the word matches the current grammar
            if(matcher.matches()){
                // MATCH FOUND
                TokenType type = grammar.getType();

                if(type == TokenType.COMMENT){
                    /* Special case: Comments span multiple words so match
                     * against entire unTokenized input and not just the first word */ 
                    matcher = grammar.matcher(unTokenizedInput); 

                    /* Matches the Comment from the start of the unTokenized input
                     * to the end of the Comment */
                    matcher.lookingAt();
                }

                // Shifts the offset past the matched input
                shiftOffset(matcher.end());

                // Returns a new Token using the Type and Value of the matched Grammar
                return new Token(type, unTokenizedInput.substring(0, matcher.end()));
            }
        }

        /* If execution reaches this point no match was found for the current 
         * word and so the offset is shifted past the input and an Error token 
         * is returned */

        shiftOffset(word.length());

        return new Token(TokenType.ERROR, word);  
    }
}
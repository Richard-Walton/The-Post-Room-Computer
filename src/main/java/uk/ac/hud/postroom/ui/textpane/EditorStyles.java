package uk.ac.hud.postroom.ui.textpane;

import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.event.*;

import java.awt.*;
import javax.swing.text.*;
import java.util.*;

/**
 * Provdes access to the EditorStyles used to colour Post Room Computer assembler code
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public final class EditorStyles {
    
    // Font used by all the styles
    private static Font font;
    
    // Default style for TokenTypes which do not have a custom style
    private static MutableAttributeSet defaultStyle;
    
    // Map of styles
    private static Map<TokenType, MutableAttributeSet> styles;
    
    private static ArrayList<EditorStylesListener> listeners;
    
    /**
     * Private constructor as class should not be instansiated 
     */
    private EditorStyles() {}
    
    /**
     * Init styles
     */
    static {  
        
        listeners = new ArrayList<EditorStylesListener>();
        
        /* Use LinkedHashMap as returns values in the order they were entered.
         * This provides a consistant return value for getEditableStyleTypes */
        styles = new LinkedHashMap<TokenType, MutableAttributeSet>();
        
        // Add custom default styles (DataType|Foreground|bold|italic|underline)
        setStyle(TokenType.OPCODE, Color.BLUE, true, false, false);
        setStyle(TokenType.REGISTER, Color.ORANGE, true, false, false);
        setStyle(TokenType.LABEL, Color.GRAY, false, false, false);
        setStyle(TokenType.LABEL2, Color.GRAY, true, false, false);
        setStyle(TokenType.DATA, Color.RED, false, false, false);
        setStyle(TokenType.COMMENT, Color.GREEN, false, false, false);
        setStyle(TokenType.CONDITION, Color.MAGENTA, true, false, false);
        setStyle(TokenType.MACRO_START, Color.PINK, false, false, false);
        setStyle(TokenType.MACRO_END, Color.PINK, false, false, false);
        setStyle(TokenType.INCLUDE_DIRECTIVE, Color.BLACK, false, true, false);
        setStyle(TokenType.FILENAME, Color.ORANGE, false, true, false);
        setStyle(TokenType.ERROR, Color.RED, true, false, false);
        setStyle(TokenType.CHARACTER, Color.MAGENTA, false, true, false);
        setStyle(TokenType.STRING, Color.BLACK, false, true, false);
        setStyle(TokenType.INSTRUCTION, Color.BLACK, true, false, false);
        
        // Init fall backdefaults
        defaultStyle = new SimpleAttributeSet();
        setFont(new Font("Monospaced", Font.PLAIN, 12));
    }
    
    /**
     * Returns an array of styles which can be edited.  Other data types are 
     * non vistual types such as Whitespace or the EOL
     * @return an array of styles which can be edited
     */
    public static TokenType[] getEditableStyleTypes() {
        return styles.keySet().toArray(new TokenType[]{});
    }
    
    /**
     * Sets the style for the given data type
     * @param tokenType Data type to apply the style to
     * @param foreground Colour of the text
     * @param bold Whether it is bold
     * @param italic Whether it is italic
     * @param underline Whether it is underline
     */
    private static void setStyle(TokenType tokenType, 
            Color foreground, boolean bold, boolean italic, boolean underline) {
        
        MutableAttributeSet attributes = new SimpleAttributeSet();
        
        StyleConstants.setForeground(attributes, foreground);
        StyleConstants.setBold(attributes, bold);
        StyleConstants.setItalic(attributes, italic);
        StyleConstants.setUnderline(attributes, underline);
        
        styles.put(tokenType, attributes);
    }
    
    /**
     * Returns the style for the given data type
     * @param tokenType Data type to get the style of
     * @return style for the given data type
     */
    public static MutableAttributeSet getStyle(TokenType tokenType) {
        MutableAttributeSet style = styles.get(tokenType);
        if(style == null) {
            style = defaultStyle;
        }
        
        return style;
    }
    
    /**
     * Returns the Font used by all the data type styles
     * @return Font used by all the data type styles
     */
    public static Font getFont() {
        return font;    
    }
    
    /**
     * Returns the foreground for the given data type
     * @param tokenType Data type to get the foreground of
     * @return foreground for the given data type
     */
    public static Color getForeground(TokenType tokenType){
        return StyleConstants.getForeground(getStyle(tokenType));
    } 
    
    /**
     * Returns whether the style of the data type is bold
     * @param tokenType Data type to get whether is bold
     * @return true if the style of the data type is bold
     */
    public static boolean isBold(TokenType tokenType){
        return StyleConstants.isBold(getStyle(tokenType));
    }
    
    /**
     * Returns whether the style of the data type is italic
     * @param tokenType Data type to get whether is italic
     * @return true if the style of the data type is italic
     */
    public static boolean isItalic(TokenType tokenType){
        return StyleConstants.isItalic(getStyle(tokenType));
    }
    
    /**
     * Returns whether the style of the data type is underline
     * @param tokenType Data type to get whether is underline
     * @return true if the style of the data type is underline
     */
    public static boolean isUnderline(TokenType tokenType){
        return StyleConstants.isUnderline(getStyle(tokenType));
    }
    
    /**
     * Sets the font for all the style types
     * @param font font for all the style types
     */
    public static void setFont(Font font) {
        EditorStyles.font = font;
        
        // Update default style
        StyleConstants.setFontFamily(defaultStyle, font.getFamily());
        StyleConstants.setFontSize(defaultStyle, font.getSize());
        
        // Update other styles
        for(MutableAttributeSet attributes : styles.values()) {
            StyleConstants.setFontFamily(attributes, font.getFamily());
            StyleConstants.setFontSize(attributes, font.getSize());
        }
        
        fireFontChanged();
    }
    
    /**
     * Sets the foreground of the style for the given data type
     * @param tokenType data type of the style 
     * @param color foreground colour
     */
    public static void setForeground(TokenType tokenType, Color color){
        StyleConstants.setForeground(getStyle(tokenType), color);
        fireFontChanged();
    }
    
    /**
     * Sets whether the given data type style should be bold
     * @param tokenType data type of the style to set whether it is bold
     * @param bold whether the data type style is bold
     */
    public static void setBold(TokenType tokenType, boolean bold){
        StyleConstants.setBold(getStyle(tokenType), bold);
        fireFontChanged();
    }
    
    
    /**
     * Sets whether the given data type style should be italic
     * @param tokenType data type of the style to set whether it is italic
     * @param italic whether the data type style is italic
     */
    public static void setItalic(TokenType tokenType, boolean italic){
        StyleConstants.setItalic(getStyle(tokenType), italic);
        fireFontChanged();
    }
    
    /**
     * Sets whether the given data type style should be underline
     * @param tokenType data type of the style to set whether it is underline
     * @param underline whether the data type style is underline
     */
    public static void setUnderline(TokenType tokenType, boolean underline){
        StyleConstants.setUnderline(getStyle(tokenType), underline);
        fireFontChanged();
    }
    
    public static void addEditorStylesListener(EditorStylesListener listener) {
        if(listener != null) listeners.add(listener);
    }
    
    public static EditorStylesListener[] getEditorStylesListeners() {
        return listeners.toArray(new EditorStylesListener[]{});
    }
    
    public static void removeEditorStylesListener(EditorStylesListener listener) {
        listeners.remove(listener);
    }
    
    private static void fireFontChanged() {
        for(EditorStylesListener listener : listeners) {
            listener.fontChanged();
        }
    }
}
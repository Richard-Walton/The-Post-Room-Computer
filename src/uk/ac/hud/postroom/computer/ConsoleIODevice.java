package uk.ac.hud.postroom.computer;

import java.io.*;

/**
 * ConsoleIODevice is a implementation of a Post Room Computer IODevice which
 * uses the default terminal to input / output information
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ConsoleIODevice implements IODevice {

    // Input
    private BufferedReader input;
    
    // Output
    private PrintWriter output;
    
    /**
     * Constructs a new ConsoleIODevice
     */
    public ConsoleIODevice() {
        input = new BufferedReader(new InputStreamReader(System.in));
        output = new PrintWriter(System.out);
    }
    
    /** @inheritDoc **/
    public String requestInput() {
        try {
            showOutput("Input? ", false);
            return input.readLine();
        }catch (IOException e) {
            return e.getMessage();
        }
    }

    /** @inheritDoc **/
    public void showOutput(String string) {
        showOutput(string, true);
    }
    
    private void showOutput(String string, boolean fromIOModule) {
        output.println((fromIOModule ? "Out: " : "") + string);
        output.flush();
    }
}
package uk.ac.hud.postroom.ui.computer;

import uk.ac.hud.postroom.computer.*;
import uk.ac.hud.postroom.event.*;

import uk.ac.hud.postroom.ui.*;
import javax.swing.*;

/**
 * GUI component which implements the Post Room Computer IODevice interface
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class GIODevice extends JTextArea implements IODevice {

          // The ioModule of the computer
          private IOModule ioModule;

          /**
           * Constructs a new G(raphical ) IODevice and redirects IO from the given ioModule
           * @param ioModule ioModule of the Computer
           */
          public GIODevice(IOModule ioModule) {
                    super(5, 40);

                    this.ioModule = ioModule;

                    ioModule.setIODevice(this);

                    // Clear output text area on computer reset
                    ioModule.getComputer().addComputerListener(new ComputerAdapter() {

                              public void computerStarted(Computer computer) {
                                        showOutput("Computer started", false);
                              }

                              public void computerStopped(Computer computer) {
                                        showOutput("Computer stopped", false);
                              }

                              public void computerReset(Computer computer) {
                                        setText("");
                              }

                              public void computerError(Computer computer, Throwable error) {
                                        showOutput("Error: " + error.getMessage(), false);
                              }
                    });

                    setEditable(false);
          }

          /** @inheritDoc **/
          public String requestInput() {
                    String input = JOptionPane.showInputDialog(this, "Input Reqested");
                    if (input == null) {
                              // User cancelled input - stop the computer
                              ioModule.getComputer().forceStop();
                              return "0000000";
                    }

                    input = input.trim();

                    try {
                              Integer.parseInt(input);
                              
                              showOutput("In: " + input, false);
                              
                              return input;
                    } catch (NumberFormatException e) {
                              // Invalid input - show error and try again
                              UIUtilities.showErrorDialog(this, "Expected integer input");
                              return requestInput();
                    }
          }

          /** @inheritDoc **/
          public void showOutput(String output) {
                    showOutput(output, true);
          }

          /**
           * Outputs the given text to the IODevice
           * @param output Output to write
           * @param fromIOModule Whether the IO came from the IOModule
           */
          private void showOutput(String output, boolean fromIOModule) {
                    append((fromIOModule ? "Out: " : "") + output + "\n");
                    setCaretPosition(getDocument().getLength());
          }
}

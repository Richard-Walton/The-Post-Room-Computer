package uk.ac.hud.postroom.computer;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.event.*;

import java.util.*;

/**
 * Registers component of the Post Room Computer
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class RegisterStore extends ComputerComponent {
    
    // Data store for each Register
    private Map<Register, String> registerStore;
    
    // Listener list
    private List<RegisterListener> listeners;
    
    /**
     * Constructs a new Register store for the given computer
     * @param computer Computer which this Register store is part of
     */
    public RegisterStore(Computer computer) {
        // Super-class (ComputerComponent) constructor
        super(computer);
        
        registerStore = new HashMap<Register, String>();
        listeners = new ArrayList<RegisterListener>();
        
        computer.addComputerListener(new ComputerAdapter() {
            public void computerReset(Computer computer) {
                reset();
            }
        });
        
        // Loads the registers with default 0 (zero) values
        reset();
    }
    
    /**
     * Returns the value stored in the given register
     * @param register Register to read from
     * @return value stored in the given register
     */
    protected String readFrom(Register register) {
        String value = registerStore.get(register);
        
        // Inform listeners
        for(RegisterListener listener : getRegisterListeners()) {
            listener.registerReadFrom(this, register, value);
        }
        
        return value;
    }
    
    /**
     * Writes a value to the given register
     * @param register Register to write to
     * @param value Value to write
     */
    protected void writeTo(Register register, String value) {  
        registerStore.put(register, value);
        
        //Inform listeners
        for(RegisterListener listener : getRegisterListeners()) {
            listener.registerWroteTo(this, register, value);
        }
    }
    
    /**
     * Writes zeros to all the registers - No listeners are fired
     */
    protected void reset() {
        for(Register register : Register.values()) {
            registerStore.put(register, "000000");
        }
    }
    
    /**
     * Adds a listener for Register events
     * @param listener listener to add
     */
    public void addRegisterListener(RegisterListener listener) {
        if(listener != null) listeners.add(listener);
    };
    
    /**
     * Returns all RegisterListeners which have been added to the register store
     * @return Array of Register Listeners
     */
    public RegisterListener[] getRegisterListeners() {
        return listeners.toArray(new RegisterListener[]{});
    }
    
    /**
     * Removes the given listener from the register store
     * @param listener listener to remove
     */
    public void removeRegisterListener(RegisterListener listener) {
        listeners.remove(listener);
    }

    /**
     * Method is a byproduct of implementation and should not be used
     */
    protected void invoke() {}
}
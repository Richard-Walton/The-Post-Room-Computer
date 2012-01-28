package uk.ac.hud.postroom.computer;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.event.*;

import java.util.*;

/**
 * Memory component of the Post Room Computer 
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class Memory extends ComputerComponent {
    
    // Memory store
    private Map<Integer, String> memory;
    
    // Register store of the computer
    private RegisterStore registerStore;

    // Listener list
    private List<MemoryListener> listeners;
    
    /**
     * Constructs a new 100 address Memory component for the given computer
     * @param computer Computer which the memory component is part of
     */
    public Memory(Computer computer) {
        this(computer, 100);
    }
    
    /**
     * Constructs a new Memory component of the given size for the given computer
     * @param computer Computer which the memory component is part of
     * @param size Size of the memory
     */
    public Memory(Computer computer, int size) {
        // Super-class (ComputerComponent) constructor
        super(computer);
        
        memory = new HashMap<Integer, String>(size);
        registerStore = computer.getRegisterStore();
        listeners = new ArrayList<MemoryListener>();
        
        computer.addComputerListener(new ComputerAdapter() {
            public void computerReset(Computer computer) {
                reset();
            }
        });
        
        for(int i = 0; i < size; i++) {
            put(i, "0");
        }
    }
    
    /**
     * Invokes the memory unit to read from the computer register store
     * and perform either a read or write operation 
     */
    protected void invoke() {
        // Reads the memory address register
        int address = Integer.parseInt(registerStore.readFrom(Register.MAR));
            
        try {        
            // Finds what memory operation should be performed
            switch(Integer.parseInt(registerStore.readFrom(Register.MRW))) {
                case 0 : //read
                    String value = get(address);

                    // Informs listeners
                    for(MemoryListener listener : getMemoryListeners()) {
                        listener.memoryReadFrom(this, address, value);
                    }
                
                    // Writes the value to the Memory data register
                    registerStore.writeTo(Register.MDR, value);
                
                    break;
            
                case 1 : //write
                    String data = registerStore.readFrom(Register.MDR);
                
                    // Writes the vaue from the Memory data register to memory
                    put(address, data);
                
                    // Informs listeners
                    for(MemoryListener listener : getMemoryListeners()) {
                        listener.memoryWroteTo(this, address, data);
                    }
                
                    break;
            }
        }catch (Exception e) {
            // Inform listeners of error
            for(MemoryListener listener : getMemoryListeners()) {
                listener.memoryError(this, new Exception("Invalid memory address " + address));
            }
        }
    }
    
    /**
     * Writes zeros to all the memory locations - No listeners are fired
     */
    protected void reset() {
        for(int i = 0; i < size(); i++) {
            put(i, "0");
        }
    }
    
    /**
     * Returns the value at the given address (No listeners are fired)
     * @param address Address to read from
     * @return Value at the given address
     */
    public String get(int address) {
        return memory.get(address);
    }
    
    /**
     * Stores a value at the given address (No listeners are fired)
     * @param address Address to write to
     * @param value Value to write
     */
    public void put(int address, String value) {
        memory.put(address, value);
    }
    
    /**
     * Returns the size of the memory
     * @return size of the memory
     */
    public int size() {
        return memory.size();
    }
    
    /**
     * Adds a listener for Memory events
     * @param listener listener to add
     */
    public void addMemoryListener(MemoryListener listener) {
        if(listener != null) listeners.add(listener);
    }
    
    /**
     * Returns all MemoryListener which have been added to memory
     * @return Array of Memory Listeners
     */
    public MemoryListener[] getMemoryListeners() {
        return listeners.toArray(new MemoryListener[]{});
    }
    
    /**
     * Removes the given listener from memory
     * @param listener listener to remove
     */
    public void removeMemoryListener(MemoryListener listener) {
        listeners.remove(listener);
    }
}
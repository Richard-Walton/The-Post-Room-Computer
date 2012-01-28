package uk.ac.hud.postroom.computer;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.event.*;

/**
 * IOModule component of the Post Room Computer
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class IOModule extends ComputerComponent {
    
    // Register store of the computer
    private RegisterStore registerStore;
    
    // IO Device used to get IO
    private IODevice ioDevice;
    
    /**
     * Constructs a new IOModule for the given computer using a ConsoleIODevice as
     * the IODevice
     * @param computer The computer which this IOModule is part of
     */
    public IOModule(Computer computer) {
        this(computer, new ConsoleIODevice());
    }
    
    /**
     * Constructs a new IOModule for the given computer and IODevice
     * @param computer The computer which this IOModule is part of
     * @param ioDevice The IODevice which this IOModule uses
     */
    public IOModule(Computer computer, IODevice ioDevice) {
        // Super-class (ComputerComponent) constructor
        super(computer);
        
        registerStore = computer.getRegisterStore();
        this.ioDevice = ioDevice;
    }
    
    /**
     * Invokes the IOModule to read from the computer register store and 
     * performs an IO operation using the current IODevice
     */
    protected void invoke() {
        // Get the type of IO to perform
        int ioType = Integer.parseInt(registerStore.readFrom(Register.IOT));
        
        switch(ioType) {
            case 0 : //read
                registerStore.writeTo(Register.IOB, ioDevice.requestInput());
                break;
                
            case 1 : //write
                ioDevice.showOutput(registerStore.readFrom(Register.IOB));
                break;
        }
    }
    
    /**
     * Sets a new IODevice to be used by this IOModule
     * @param ioDevice IODevice to used by this IOModule
     */
    public void setIODevice(IODevice ioDevice) {
        this.ioDevice = ioDevice;
    }
    
    /**
     * Gets the IODevice used by this IOModule
     * @return IODevice used by this IOModule
     */
    public IODevice getIODevice() {
        return ioDevice;
    }
}
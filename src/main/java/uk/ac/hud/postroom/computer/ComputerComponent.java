package uk.ac.hud.postroom.computer;

/**
 * A component of the Post Room Computer
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public abstract class ComputerComponent {
    
    // Computer this component is part of
    private Computer computer;
    
    /**
     * Constructs a new ComputerComponent which is part of the given computer
     * @param computer Computer which this component is part of
     */
    public ComputerComponent(Computer computer) {
        this.computer = computer;
    }
    
    /**
     * Returns the computer this component is part of
     * @return computer this component is part of
     */
    public Computer getComputer() {
        return computer;
    }
    
    /**
     * Invokes the computer component to perform an action
     */
    protected abstract void invoke();
}
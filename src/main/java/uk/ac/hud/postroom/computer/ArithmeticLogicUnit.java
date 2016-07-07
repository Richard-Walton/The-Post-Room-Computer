package uk.ac.hud.postroom.computer;

import uk.ac.hud.postroom.*;

/**
 * Arithmetic Logic Unit (ALU) of the Post Room Computer
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ArithmeticLogicUnit extends ComputerComponent {
    
    // Register store of the Computer
    private RegisterStore registerStore;
    
    // Flag of the computer
    private Flag flag;
    
    /**
     * Constructs a new ArithmeticLogicUnit for the given computer
     * @param computer computer this ArithmeticLogicUnit is part of
     */
    public ArithmeticLogicUnit(Computer computer) {
        super(computer);
        
        registerStore = computer.getRegisterStore();
        
        flag = new Flag();
    }
    
    /**
     * Invokes the ALU to read from the computer register store and perform an
     * action based on the values of the OP, XA, XV, XAT, YA, YV, YAT, ZX
     * and ZAT registers
     */
    protected void invoke() {
        // Read from registers
        String xAddress = registerStore.readFrom(Register.XA);
        String xValue = registerStore.readFrom(Register.XV);
        String xType = registerStore.readFrom(Register.XAT);
        
        String yAddress = registerStore.readFrom(Register.YA);
        String yValue = registerStore.readFrom(Register.YV);
        String yType = registerStore.readFrom(Register.YAT);
        
        String zAddress = registerStore.readFrom(Register.ZA);
        String zType = registerStore.readFrom(Register.ZAT);

        String result = "0";
        
        switch(OpCode.getByValue(registerStore.readFrom(Register.OP))) {
            case MSK :
                result = doMSK(xValue, yValue);
                break;
                
            case MOV :
                result = yValue;
                break;
                
            case ADD :
                result = Integer.toString(Integer.parseInt(xValue) + Integer.parseInt(yValue));
                break;
                
            case SUB :
                result = Integer.toString(Integer.parseInt(xValue) - Integer.parseInt(yValue));
                break;
                
            case MEA :
                result = yAddress;
                break;
                
            case SHF :
                result = doSHF(xValue, yValue);
                break;
        }
        
        int intResult = Integer.parseInt(result);
        flag.setNegative(intResult < 0);
        flag.setZero(intResult == 0);
        flag.setMemory(zType.equals("0"));
        
        registerStore.writeTo(Register.FLG, flag.getMask());
        registerStore.writeTo(Register.ZV, result);
    }
    
    private String doMSK(String xValue, String yValue) {
        while(yValue.length() < xValue.length()) {
            yValue = yValue + "0";
        }
        
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < xValue.length(); i++) {
            if(yValue.charAt(i) == '0') {
                result.append("0");
            }else{
                result.append(xValue.charAt(i));
            }
        }
        
        return result.toString();
    }
    
    private String doSHF(String xValue, String yValue) {
        StringBuilder result = new StringBuilder(xValue);
        StringBuilder carry = new StringBuilder("0000000000");
        
        int shiftAmount = Integer.parseInt(yValue);
        
        flag.setCarry(shiftAmount != 0);

        if(shiftAmount > 0) {
            while(shiftAmount > 0) {
                carry.deleteCharAt(0);
                carry.append(result.charAt(0));
                result.deleteCharAt(0);
                result.append("0");
                
                shiftAmount--;
            }
        }else {
            while(shiftAmount < 0) {
                carry.insert(0, result.charAt(result.length() - 1));
                carry.deleteCharAt(carry.length() - 1);
                result.deleteCharAt(result.length() - 1);
                result.insert(0, "0");
                
                shiftAmount++;
            }
        }
        
        registerStore.writeTo(Register.CAR, carry.toString());
        
        return result.toString();  
    }
}
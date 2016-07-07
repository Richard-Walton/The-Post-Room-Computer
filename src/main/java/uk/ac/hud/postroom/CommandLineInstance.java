package uk.ac.hud.postroom;

import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.computer.*;
import uk.ac.hud.postroom.exception.*;
import uk.ac.hud.postroom.event.*;

import java.io.*;
import java.util.*;

public class CommandLineInstance {
    
    private SourceFile sourceFile;
    private String baseFileName;
    
    private Computer computer;
    private Assembler assembler;
    
    private boolean machineCode;
    private boolean execute;
    private boolean trace;
    
    public CommandLineInstance(String[] arguments) {
        
        machineCode = false;
        execute = false;
        trace = false;
        
        processArguments(arguments);
        
        assemble();
        
        if(execute) {
            execute();
        }
    }
    
    private void processArguments(String[] arguments) {
        
        if(arguments == null || arguments.length == 0) {
            return;
        }
        
        if(arguments[0].matches("-h|-help")) { // help
            doHelp(arguments);
        }
           
        if(arguments[0].matches("-v|-version")) { // version
            doVersion(arguments);
        }
            
        if(arguments[0].matches("-a|-author")) { // author
            doAuthor(arguments);
        }
            
        if(arguments[0].matches("-f")) { // format
            doFormat(arguments);
            
            processArguments(Arrays.copyOfRange(arguments, 2, arguments.length));
            return;
        }
            
        if(arguments[0].matches("-i")) { // input
            doInput(arguments);
            
            processArguments(Arrays.copyOfRange(arguments, 2, arguments.length));
            return;
        }
            
        if(arguments[0].matches("-e")) { // execute
            doExecute();
            
            processArguments(Arrays.copyOfRange(arguments, 1, arguments.length));
            return;
        }
            
        if(arguments[0].matches("-o")) { // output
            doTrace();
            
            processArguments(Arrays.copyOfRange(arguments, 1, arguments.length));
            return;
        }
        
        printError("Invalid command syntax");
    }
    
    private void doHelp(String[] arguments) {
        if(arguments.length != 1) {
            printError("Invalid command syntax");
        }
        
        print("Post Room Computer Help File:");
        print("Usage: java -jar path/to/postroom.jar -[option]");
        print("[option]");
        print(" h | help                        : Show help file");
        print(" v | version                     : Show version number");
        print(" a | author                      : Show author");
        print(" f [format] -i <filename>        : Assemble file");
        print(" f [format] -i <filename>  -e    : Assemble file and execute");
        print(" f [format] -i <filename>  -e -o : Assemble file, execute, and save trace information");
        print("[format]");
        print(" a | absolute                    : Absolute Address Machine");
        print(" r | register                    : Register Address Machine");
        
        System.exit(0);
    }
    
    private void doVersion(String[] arguments) {
        if(arguments.length != 1) {
            printError("Invalid command syntax");
        }
        
        print("Post Room Computer : Version 1.1 (July 2008)");
        
        System.exit(0);
    }
    
    private void doAuthor(String[] arguments) {
        if(arguments.length != 1) {
            printError("Invalid command syntax");
        }
        
        print("Post Room Computer : By Richard Walton");
        print("Based on work by Dr. Hugh Osborne");
        
        System.exit(0);
    }
    
    private void doFormat(String[] arguments) {
        if(arguments.length < 2) {
            printError("-f : No format specified");
        }
        
        if(arguments[1].matches("a|absolute")) {              
            computer = new AbsoluteAddressComputer();
            assembler = new AbsoluteAddressAssembler();
        }else if(arguments[1].matches("r|register")) {                
            computer = new RegisterAddressComputer();
            assembler = new RegisterAddressAssembler();
        }else {
            printError("-f : Invalid format specified");
        }
    }
    
    private void doInput(String[] arguments) {
        if(arguments.length < 2) {
            printError("-i : No filename specified");
        }
        
        if(arguments[1].endsWith(".pco")) {
            machineCode = true;
            assembler = new MachineCodeAssembler();
        }else {
            if(! new File(arguments[1]).exists()) {
                print("...no file extension specified.  Guessing .pca");
                    
                arguments[1] = arguments[1] + ".pca";
            }
        }
           
        try {
            sourceFile = SourceFile.load(arguments[1]);
            baseFileName = arguments[1].substring(0, arguments[1].lastIndexOf("."));
        }catch (IOException e) {
            printError(e.getMessage());
        }
    }
    
    private void doExecute() {
        execute = true;
    }
    
    private void doTrace() {
        trace = true;
    }
    
    private void assemble() {
        if(sourceFile == null) {
            printError("-i : No source file specified");
        }
        
        if(assembler == null) {
            printError("-f : No format specified");
        }
               
        print("...Assembling " + sourceFile.getName());
        
        assembler.assemble(sourceFile);
        
        print("..." + assembler.getErrors().length + " error(s) found");
        
        try {
            PrintWriter output = null;
            
            if(! assembler.hasErrors()) {
                if(! machineCode) {
                    print("...saving .pco file");
                    
                    output = new PrintWriter(new FileWriter(baseFileName + ".pco"));
                    
                    for(Instruction instruction : assembler.getInstructions()) {
                        output.print(instruction.getInstruction());
                        output.println(" ;" + instruction.getSourceCode());
                    }
                }else {
                    print("Machine code file varified");
                }
            }else {
                print("...saving .err file");
                
                output = new PrintWriter(new FileWriter(baseFileName + ".err"));
                for(AssemblyError error : assembler.getErrors()) {
                    output.println(error.toString());
                }
            }
            
            if(output != null) {
                output.close();
            }
        }catch (IOException e) {
            printError(e.getMessage());
        }   
    }
    
    private void execute() {
        if(sourceFile == null) {
            printError("-i : No source file specified");
        }
        
        if(assembler == null || computer == null) {
            printError("-f : No format specified");
        }
        
        if(assembler.hasErrors()) {
            printError("Unable to execute file due to errors");
        }
        
        print("...preparing to execute program");
        
        if(trace) {
            print("...opening computer trace files");
            addTraceListeners();
        }
        
        computer.setExecutionSpeed(ExecutionSpeed.FULL);
        computer.setInstructions(assembler.getInstructions());
        
        computer.addComputerListener(new ComputerAdapter() {
            public void computerStarted(Computer computer) {
                print("...Execution started");
            }
            public void computerStopped(Computer computer) {
                print("...Execution complete");
            }
        });
        
        computer.execute();
    }
    
    private void addTraceListeners() {
        try {
            final PrintWriter memoryTrace = 
                new PrintWriter(new FileWriter(baseFileName + ".mem"));
            final PrintWriter registerTrace = 
                new PrintWriter(new FileWriter(baseFileName + ".reg"));
            final PrintWriter decoderTrace =
                new PrintWriter(new FileWriter(baseFileName + ".dec"));
            
            computer.getMemory().addMemoryListener(new MemoryListener() {
                public void memoryReadFrom(Memory memory, int address, String value) {
                    memoryTrace.println("READ," + address + "," + value);
                }

                public void memoryWroteTo(Memory memory, int address, String value) {
                    memoryTrace.println("WRITE," + address + "," + value);
                }

                public void memoryError(Memory memory, Throwable error) {
                    memoryTrace.println("ERROR: " + error.getMessage());
                    
                    printError(error.getMessage());
                }
            });
            
            computer.getRegisterStore().addRegisterListener(new RegisterListener() {
                public void registerReadFrom(RegisterStore registers, Register register, String value) {
                    registerTrace.println("READ," + register.getMnemonic() + "," + value);
                }

                public void registerWroteTo(RegisterStore registers, Register register, String value) {
                    registerTrace.println("WRITE," + register.getMnemonic() + "," + value);
                }
            });
            
            computer.getInstructionDecoder().addDecoderListener(new InstructionDecoderListener() {
                public void instructionDecoded(InstructionDecoder decoder, String instruction) {
                    decoderTrace.println(instruction);
                }

                public void decodeError(InstructionDecoder decoder, String instruction, Throwable error) {
                    decoderTrace.println("Decoding '" + instruction + "'...");
                    decoderTrace.print("ERROR: " + error.getMessage());
                    
                    printError(error.getMessage());
                }      
            });
            
            computer.addComputerListener(new ComputerAdapter() {
                public void computerStopped(Computer computer) {
                    // Close file output streams when execution is finished
                    memoryTrace.close();
                    registerTrace.close();
                    decoderTrace.close();
                }

                public void computerError(Computer computer, Throwable error) {
                    printError(error.getMessage());
                }   
            });
            
        }catch (IOException e) {
            printError(e.getMessage());
        }
    }
    
    private void print(String message) {
        System.out.println(message);
    }
    
    private void printError(String error) {
        print("ERROR: " + error);
        print("For help use <postroom.jar> -help");
        
        System.exit(-1);
    }
}
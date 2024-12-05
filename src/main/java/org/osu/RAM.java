package org.osu;

import java.util.ArrayList;
import java.util.List;

public class RAM {
    Tape inputTape;
    Tape outputTape;
    List<Integer> register;
    List<InstructionCommand> instructionCommands;

    public RAM(Tape inputTape, List<InstructionCommand> instructionCommands, int additionalRegistries) {
        this.inputTape = inputTape;
        this.instructionCommands = instructionCommands;
        this.outputTape = new Tape(0, new ArrayList<>());
        this.register = new ArrayList<>(List.of(0));
        for (int i = 0; i < additionalRegistries; i++) {
            register.add(0);
        }
    }

    int initialCommandIndex = 0;

    public void run() {
        boolean isRunning = true;

        while (isRunning) {
            InstructionCommand c = instructionCommands.get(initialCommandIndex);
            switch (c.instruction) {
                case READ -> read(c.operand);
                case WRITE -> write(c, c.operand);
                case LOAD -> load(c, c.operand);
                case STORE -> store(c, c.operand);
                case ADD -> add(c, c.operand);
                case SUB -> sub(c, c.operand);
                case MUL -> mul(c);
                case DIV -> div(c, c.operand);
                case JMP -> jump(c.value);
                case JZ -> jumpZero(c.value);
                case JGTZ -> jumpGreaterThanZero(c.value);
                case HALT -> isRunning = false;
                default -> throw new RuntimeException("Unknown command");
            }

            c.print();

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < register.size(); i++) {
                builder.append(register.get(i));
                if (i < register.size() - 1) {
                    builder.append(",");
                }
            }
            System.out.print("Registr: " + builder + " ");

            System.out.println("IN head position: " + inputTape.head);

            initialCommandIndex++;
        }

        System.out.print("\nOutput tape: ");
        outputTape.content.forEach(c -> System.out.print(c + " "));
    }

    private void read(String operand) {
        // Get the value at the current head position of the input tape
        int value = inputTape.content.get(inputTape.head);
        System.out.println("Reading value: " + value);

        if (operand.isEmpty()) {
            // If operand is empty, store the value in register[0]
            register.set(0, value);
        } else {
            // If operand is not empty, store the value in the specified register
            int registerIndex = Integer.parseInt(operand); // Convert operand to register index
            register.set(registerIndex, value);
        }

        // Move the tape head to the next symbol after reading
        inputTape.head++;
    }

    private void write(InstructionCommand instructionCommand, String operand) {
        if (operand.isEmpty()) {
            // Direct addressing: Write the value of register[instructionCommand.value] to output
            int valueToWrite = register.get(instructionCommand.value);
            outputTape.content.add(valueToWrite);
        } else if (operand.equals("=")) {
            // Constant addressing: Write the constant value from instructionCommand to output
            outputTape.content.add(instructionCommand.value);
        } else if (operand.equals("*")) {
            // Indirect addressing: Write the value in register[register[0] + instructionCommand.value] to output
            int address = register.get(0) + instructionCommand.value;
            if (address < 0 || address >= register.size()) {
                throw new IndexOutOfBoundsException("Invalid address: " + address);
            }
            outputTape.content.add(register.get(address));
        } else {
            throw new IllegalArgumentException("Invalid operand for WRITE instruction: " + operand);
        }

        outputTape.head++;
    }

    private void load(InstructionCommand instructionCommand, String operand) {
        if (operand == null || operand.isEmpty()) {
            // Indexed addressing: Load value from register[instructionCommand.value] into register[0]
            register.set(0, register.get(instructionCommand.value));
        } else if (operand.equals("=")) {
            // Constant addressing: Load constant value (instructionCommand.value) into register[0]
            register.set(0, instructionCommand.value);
        } else if (operand.equals("*")) {
            // Indirect addressing: Load value from the tape at the position specified by register[instructionCommand.value] into register[0]
            int address = register.get(instructionCommand.value); // Address in the tape
            int valueFromTape = inputTape.content.get(address);   // Value from the tape
            register.set(0, valueFromTape);                       // Load the value into register[0]
        } else {
            throw new IllegalArgumentException("Unknown operand type: " + operand);
        }
    }

    private void store(InstructionCommand instructionCommand, String operand) {
        if (operand == null || operand.equals("")) {
            // Direct addressing: Store register[0] value in register[operand]
            register.set(instructionCommand.value, register.get(0));
        } else if (operand.equals("*")) {
            // Indirect addressing: Store register[0] value at the address calculated by register[1] + operand
            int address = register.get(instructionCommand.value + register.get(1));
            register.set(address, register.get(0));
        } else if (operand.equals("=")) {
            // Constant addressing: Store register[0] value in register[operand]
            register.set(instructionCommand.value, register.get(0));
        } else {
            throw new IllegalArgumentException("Invalid operand for STORE instruction");
        }
    }

    private void add(InstructionCommand instructionCommand, String operand) {
        if (operand == null) {
            // Direct addressing: Add value from register[operand] to register[0]
            register.set(0, register.get(0) + register.get(instructionCommand.value));
        } else if (operand.equals("=")) {
            // Constant addressing: Add constant value to register[0]
            register.set(0, register.get(0) + instructionCommand.value);
        } else if (operand.equals("*")) {
            // Indirect addressing: Add value from tape at register[0] + value to register[0]
            register.set(0, register.get(0) + inputTape.content.get(register.get(0) + instructionCommand.value));
        }
    }


    private void sub(InstructionCommand instructionCommand, String operand) {
        if (operand == null || operand.equals("")) {
            // Direct addressing: Subtract value from register[operand] (indexed by instructionCommand.value)
            register.set(0, register.get(0) - register.get(instructionCommand.value));
        } else if (operand.equals("=")) {
            // Constant addressing: Subtract constant value from register[0]
            register.set(0, register.get(0) - instructionCommand.value);
        } else if (operand.equals("*")) {
            // Indirect addressing: Subtract value from memory at register[0] + value
            register.set(0, register.get(0) - inputTape.content.get(register.get(0) + instructionCommand.value));
        }
    }

    private void mul(InstructionCommand instructionCommand) {
        if (instructionCommand.operand.equals("")) {
            // Direct multiplication with the value in register
            register.set(0, register.get(0) * register.get(instructionCommand.value));
        } else if (instructionCommand.operand.equals("=")) {
            // Multiply with a constant value from instructionCommand
            register.set(0, register.get(0) * instructionCommand.value);
        } else if (instructionCommand.operand.equals("*")) {
            // Indirect addressing mode - Multiply register[0] by the value at the current tape position
            int address = inputTape.head - 1; // Use the current tape position
            if (address >= 0 && address < inputTape.content.size()) {
                // Accumulate result in register[0]
                register.set(0, register.get(0) * inputTape.content.get(address));
            } else {
                // Handle out of bounds or invalid index
                throw new RuntimeException("Invalid indirect address: " + address);
            }
        }
    }


    private void div(InstructionCommand instructionCommand, String operand) {
        if (operand == null) {
            // Direct addressing: Divide register[0] by value in register[operand]
            register.set(0, register.get(0) / register.get(instructionCommand.value));
        } else if (operand.equals("=")) {
            // Constant addressing: Divide register[0] by constant value
            register.set(0, register.get(0) / instructionCommand.value);
        } else if (operand.equals("*")) {
            // Indirect addressing: Divide register[0] by value from tape at register[0] + value
            register.set(0, register.get(0) / inputTape.content.get(register.get(0) + instructionCommand.value));
        }
    }

    private void jump(int value) {
        initialCommandIndex = value - 1;
    }

    private void jumpZero(int value) {
        if (register.get(0) == 0)
            initialCommandIndex = value - 1;
    }

    private void jumpGreaterThanZero(int value) {
        if (register.get(0) > 0)
            initialCommandIndex = value - 1;
    }
}

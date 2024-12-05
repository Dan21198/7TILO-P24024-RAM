package org.osu;

import java.util.Arrays;
import java.util.List;

/**
 * "" direct operand
 * "=" constant operand
 * "*" indirect operand
 */
public class Main {
    public static void main(String[] args) {
        // Task 1 fun(n_1, n_2, ... , n_m) = Π_{i=1}^{m} n_i | n_i ∈ N^+, m > 0
        List<InstructionCommand> task1Multiply = Arrays.asList(
                new InstructionCommand(Instruction.LOAD, "=", 1),    // Initialize register 1 with 1 (multiplicative identity)
                new InstructionCommand(Instruction.STORE, "=", 1),   // Store the value 1 in register 1
                new InstructionCommand(Instruction.READ, "*", 0),    // Read the first number from the tape into register 0
                new InstructionCommand(Instruction.JZ, "", 8),      // If the value is 0, jump to WRITE (end of input)
                new InstructionCommand(Instruction.MUL, "*", 0),     // Multiply the value in register 1 (accumulated product) with the value in register 0
                new InstructionCommand(Instruction.JZ, "", 8),      // If the value is 0, jump to WRITE (end of input)
                new InstructionCommand(Instruction.STORE, "=", 1),   // Store the accumulated result back into register 1
                new InstructionCommand(Instruction.JMP, "=", 2),     // Jump back to READ to process the next number
                new InstructionCommand(Instruction.WRITE, "*", 1),   // Write the final result (product) in register 1 to the output tape
                new InstructionCommand(Instruction.HALT, "=", 0)     // Halt the program when finished
        );

        Tape inputTape = new Tape(0, Arrays.asList(3, 10, 5, 20, 0));
        List<InstructionCommand> instructionCommands1 = task1Multiply;
        int additionalRegistries1 = 4;
        RAM RAM1 = new RAM(inputTape, instructionCommands1, additionalRegistries1);
        RAM1.run();

        System.out.println("\n");
        System.out.println("______________________________________________________________");

        // Task 2  {1^n 2^n 3^n | n >= 0}
        List<InstructionCommand> task2 = Arrays.asList(
                // 1^n
                new InstructionCommand(Instruction.READ, "", 0),
                new InstructionCommand(Instruction.JZ, "", 39),
                new InstructionCommand(Instruction.STORE, "", 4),
                new InstructionCommand(Instruction.LOAD, "", 1),
                new InstructionCommand(Instruction.ADD, "=", 1),
                new InstructionCommand(Instruction.STORE, "", 1),
                new InstructionCommand(Instruction.READ, "", 0),
                new InstructionCommand(Instruction.SUB, "", 4),
                new InstructionCommand(Instruction.JGTZ, "", 11),
                new InstructionCommand(Instruction.LOAD, "", 1),
                new InstructionCommand(Instruction.JMP, "", 4),
                // 2^n
                new InstructionCommand(Instruction.LOAD, "=", 2),
                new InstructionCommand(Instruction.STORE, "", 4),
                new InstructionCommand(Instruction.LOAD, "", 2),
                new InstructionCommand(Instruction.ADD, "=", 1),
                new InstructionCommand(Instruction.STORE, "", 2),
                new InstructionCommand(Instruction.READ, "", 0),
                new InstructionCommand(Instruction.SUB, "", 4),
                new InstructionCommand(Instruction.JGTZ, "", 21),
                new InstructionCommand(Instruction.LOAD, "", 2),
                new InstructionCommand(Instruction.JMP, "", 14),
                // 3^n
                new InstructionCommand(Instruction.LOAD, "=", 3),
                new InstructionCommand(Instruction.STORE, "", 4),
                new InstructionCommand(Instruction.LOAD, "", 3),
                new InstructionCommand(Instruction.ADD, "=", 1),
                new InstructionCommand(Instruction.STORE, "", 3),
                new InstructionCommand(Instruction.READ, "", 0),
                new InstructionCommand(Instruction.JZ, "", 31),
                new InstructionCommand(Instruction.SUB, "", 4),
                new InstructionCommand(Instruction.LOAD, "", 3),
                new InstructionCommand(Instruction.JMP, "", 24),
                new InstructionCommand(Instruction.LOAD, "", 1),
                new InstructionCommand(Instruction.SUB, "", 2),
                new InstructionCommand(Instruction.JZ, "", 35),
                new InstructionCommand(Instruction.JMP, "", 41),
                new InstructionCommand(Instruction.LOAD, "", 1),
                new InstructionCommand(Instruction.SUB, "", 3),
                new InstructionCommand(Instruction.JZ, "", 39),
                new InstructionCommand(Instruction.JMP, "", 41),
                new InstructionCommand(Instruction.WRITE, "=", 1),
                new InstructionCommand(Instruction.JMP, "", 42),
                new InstructionCommand(Instruction.WRITE, "=", 0),
                new InstructionCommand(Instruction.HALT, "", 0)
        );
        Tape inputTape2 = new Tape(0, Arrays.asList(1, 1, 1, 2, 2, 3, 3, 3, 0));
        int additionalRegistries2 = 4;
        RAM RAM2 = new RAM(inputTape2, task2, additionalRegistries2);
        RAM2.run();

    }
}
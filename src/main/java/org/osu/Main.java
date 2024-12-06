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
                new InstructionCommand(Instruction.LOAD, "=", 1),    // Initialize register 1 with 1
                new InstructionCommand(Instruction.STORE, "=", 1),   // Store the value 1 in register 1
                new InstructionCommand(Instruction.READ, "", 0),
                new InstructionCommand(Instruction.MUL, "", 1),
                new InstructionCommand(Instruction.JZ, "", 7),      // If the value is 0, jump to WRITE
                new InstructionCommand(Instruction.STORE, "=", 1),   // Store the accumulated result
                new InstructionCommand(Instruction.JMP, "=", 2),     // Jump back to READ to process the next number
                new InstructionCommand(Instruction.WRITE, "", 1),
                new InstructionCommand(Instruction.HALT, "=", 0)
        );

        Tape inputTape = new Tape(0, Arrays.asList(3, 10, 5, 20, 0));
        List<InstructionCommand> instructionCommands1 = task1Multiply;
        int additionalRegistries1 = 1;
        RAM RAM1 = new RAM(inputTape, instructionCommands1, additionalRegistries1);
        RAM1.run();

        System.out.println("\n");
        System.out.println("______________________________________________________________");

        // Task 2  {1^n 2^n 3^n | n >= 0}
        List<InstructionCommand> task2 = Arrays.asList(
                new InstructionCommand(Instruction.READ, "", 0),
                new InstructionCommand(Instruction.JZ, "", 23), // when 0 go to compare

                //decision tree
                //1
                new InstructionCommand(Instruction.SUB, "=", 1),
                new InstructionCommand(Instruction.JZ, "", 8),
                //2
                new InstructionCommand(Instruction.SUB, "=", 1),
                new InstructionCommand(Instruction.JZ, "", 13),
                //3
                new InstructionCommand(Instruction.SUB, "=", 1),
                new InstructionCommand(Instruction.JZ, "", 18),

                //branch for 1
                new InstructionCommand(Instruction.LOAD, "", 2),
                new InstructionCommand(Instruction.ADD, "=", 1),    //return value back to 1
                new InstructionCommand(Instruction.STORE, "", 2),   // Store 1's count in register 2
                new InstructionCommand(Instruction.MUL, "=", 0),    // rest first register to 0
                new InstructionCommand(Instruction.JZ, "", 0),

                //branch for 2
                new InstructionCommand(Instruction.LOAD, "", 3),
                new InstructionCommand(Instruction.ADD, "=", 1),    //return value back to 1
                new InstructionCommand(Instruction.STORE, "", 3),   // Store 2's count in register 3
                new InstructionCommand(Instruction.MUL, "=", 0),    // rest first register to 0
                new InstructionCommand(Instruction.JZ, "", 0),

                //branch for 3
                new InstructionCommand(Instruction.LOAD, "", 4),
                new InstructionCommand(Instruction.ADD, "=", 1),    //return value back to 1
                new InstructionCommand(Instruction.STORE, "", 4),   // Store 3's count in register 4
                new InstructionCommand(Instruction.MUL, "=", 0),    // rest first register to 0
                new InstructionCommand(Instruction.JZ, "", 0),

                //compare counts
                new InstructionCommand(Instruction.LOAD, "", 2),
                new InstructionCommand(Instruction.SUB, "", 3),
                new InstructionCommand(Instruction.JGTZ, "", 36),   //if result not zero write out 0 and halt
                new InstructionCommand(Instruction.LOAD, "", 3),
                new InstructionCommand(Instruction.SUB, "", 2),
                new InstructionCommand(Instruction.JGTZ, "", 36),   //if result not zero write out 0 and halt
                new InstructionCommand(Instruction.LOAD, "", 3),
                new InstructionCommand(Instruction.SUB, "", 4),
                new InstructionCommand(Instruction.JGTZ, "", 36),   //if result not zero write out 0 and halt
                new InstructionCommand(Instruction.MUL, "=", 0),    // rest first register to 0
                new InstructionCommand(Instruction.ADD, "=", 1),    //set return value to 1
                new InstructionCommand(Instruction.WRITE, "", 0),
                new InstructionCommand(Instruction.HALT, "", 0),
                new InstructionCommand(Instruction.MUL, "=", 0),    //set return value to 0
                new InstructionCommand(Instruction.WRITE, "", 0),
                new InstructionCommand(Instruction.HALT, "", 0)
        );


        Tape inputTape2 = new Tape(0, Arrays.asList( 1, 1, 1, 2, 2, 2, 3, 3, 3,  0));
        int additionalRegistries2 = 4;
        RAM RAM2 = new RAM(inputTape2, task2, additionalRegistries2);
        RAM2.run();

    }
}
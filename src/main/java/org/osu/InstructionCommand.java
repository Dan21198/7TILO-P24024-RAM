package org.osu;

public class InstructionCommand {
    Instruction instruction;
    String operand;
    int value;

    public InstructionCommand(Instruction instruction, String operand, int value) {
        if (!operand.equals("=") && !operand.equals("*") && !operand.matches("\\d*")) {
            throw new IllegalArgumentException("Invalid operand format");
        }
        this.instruction = instruction;
        this.operand = operand;
        this.value = value;
    }

    public void print() {
        System.out.println(instruction + " " + (operand != null ? operand : "") + value);
    }
}

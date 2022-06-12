package Core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import Misc.Utils;

public class Nes6502 implements Cpu8Bits
{
    public Nes6502()
    {
        A  = 0x00;
        X  = 0x00;
        Y  = 0x00;
        F  = 0x00;
        SP = 0x00;
        PC = 0x0000;

        lastFetch     = 0x00;
        addrAbs       = 0x0000;
        addrRel       = 0x0000;
        currentOpCode = 0x00;
        cycles        = 0;

        instructionSet = new Vector<Instruction>();
        instructionSet.add(new Instruction("BRK", this::BRK, this::IMM, (byte) 7));
        instructionSet.add(new Instruction("ORA", this::ORA, this::IZX, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 8));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 3));
        instructionSet.add(new Instruction("ORA", this::ORA, this::ZP0, (byte) 3));
        instructionSet.add(new Instruction("ASL", this::ASL, this::ZP0, (byte) 5));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 5));
        instructionSet.add(new Instruction("PHP", this::PHP, this::IMP, (byte) 3));
        instructionSet.add(new Instruction("ORA", this::ORA, this::IMM, (byte) 2));
        instructionSet.add(new Instruction("ASL", this::ASL, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("ORA", this::ORA, this::ABS, (byte) 4));
        instructionSet.add(new Instruction("ASL", this::ASL, this::ABS, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("BPL", this::BPL, this::REL, (byte) 2));
        instructionSet.add(new Instruction("ORA", this::ORA, this::IZY, (byte) 5));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 8));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("ORA", this::ORA, this::ZPX, (byte) 4));
        instructionSet.add(new Instruction("ASL", this::ASL, this::ZPX, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("CLC", this::CLC, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("ORA", this::ORA, this::ABY, (byte) 4));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 7));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("ORA", this::ORA, this::ABX, (byte) 4));
        instructionSet.add(new Instruction("ASL", this::ASL, this::ABX, (byte) 7));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 7));
        instructionSet.add(new Instruction("JSR", this::JSR, this::ABS, (byte) 6));
        instructionSet.add(new Instruction("AND", this::AND, this::IZX, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 8));
        instructionSet.add(new Instruction("BIT", this::BIT, this::ZP0, (byte) 3));
        instructionSet.add(new Instruction("AND", this::AND, this::ZP0, (byte) 3));
        instructionSet.add(new Instruction("ROL", this::ROL, this::ZP0, (byte) 5));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 5));
        instructionSet.add(new Instruction("PLP", this::PLP, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("AND", this::AND, this::IMM, (byte) 2));
        instructionSet.add(new Instruction("ROL", this::ROL, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("BIT", this::BIT, this::ABS, (byte) 4));
        instructionSet.add(new Instruction("AND", this::AND, this::ABS, (byte) 4));
        instructionSet.add(new Instruction("ROL", this::ROL, this::ABS, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("BMI", this::BMI, this::REL, (byte) 2));
        instructionSet.add(new Instruction("AND", this::AND, this::IZY, (byte) 5));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 8));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("AND", this::AND, this::ZPX, (byte) 4));
        instructionSet.add(new Instruction("ROL", this::ROL, this::ZPX, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("SEC", this::SEC, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("AND", this::AND, this::ABY, (byte) 4));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 7));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("AND", this::AND, this::ABX, (byte) 4));
        instructionSet.add(new Instruction("ROL", this::ROL, this::ABX, (byte) 7));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 7));
        instructionSet.add(new Instruction("RTI", this::RTI, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("EOR", this::EOR, this::IZX, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 8));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 3));
        instructionSet.add(new Instruction("EOR", this::EOR, this::ZP0, (byte) 3));
        instructionSet.add(new Instruction("LSR", this::LSR, this::ZP0, (byte) 5));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 5));
        instructionSet.add(new Instruction("PHA", this::PHA, this::IMP, (byte) 3));
        instructionSet.add(new Instruction("EOR", this::EOR, this::IMM, (byte) 2));
        instructionSet.add(new Instruction("LSR", this::LSR, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("JMP", this::JMP, this::ABS, (byte) 3));
        instructionSet.add(new Instruction("EOR", this::EOR, this::ABS, (byte) 4));
        instructionSet.add(new Instruction("LSR", this::LSR, this::ABS, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("BVC", this::BVC, this::REL, (byte) 2));
        instructionSet.add(new Instruction("EOR", this::EOR, this::IZY, (byte) 5));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 8));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("EOR", this::EOR, this::ZPX, (byte) 4));
        instructionSet.add(new Instruction("LSR", this::LSR, this::ZPX, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("CLI", this::CLI, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("EOR", this::EOR, this::ABY, (byte) 4));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 7));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("EOR", this::EOR, this::ABX, (byte) 4));
        instructionSet.add(new Instruction("LSR", this::LSR, this::ABX, (byte) 7));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 7));
        instructionSet.add(new Instruction("RTS", this::RTS, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("ADC", this::ADC, this::IZX, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 8));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 3));
        instructionSet.add(new Instruction("ADC", this::ADC, this::ZP0, (byte) 3));
        instructionSet.add(new Instruction("ROR", this::ROR, this::ZP0, (byte) 5));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 5));
        instructionSet.add(new Instruction("PLA", this::PLA, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("ADC", this::ADC, this::IMM, (byte) 2));
        instructionSet.add(new Instruction("ROR", this::ROR, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("JMP", this::JMP, this::IND, (byte) 5));
        instructionSet.add(new Instruction("ADC", this::ADC, this::ABS, (byte) 4));
        instructionSet.add(new Instruction("ROR", this::ROR, this::ABS, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("BVS", this::BVS, this::REL, (byte) 2));
        instructionSet.add(new Instruction("ADC", this::ADC, this::IZY, (byte) 5));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 8));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("ADC", this::ADC, this::ZPX, (byte) 4));
        instructionSet.add(new Instruction("ROR", this::ROR, this::ZPX, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("SEI", this::SEI, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("ADC", this::ADC, this::ABY, (byte) 4));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 7));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("ADC", this::ADC, this::ABX, (byte) 4));
        instructionSet.add(new Instruction("ROR", this::ROR, this::ABX, (byte) 7));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 7));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("STA", this::STA, this::IZX, (byte) 6));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("STY", this::STY, this::ZP0, (byte) 3));
        instructionSet.add(new Instruction("STA", this::STA, this::ZP0, (byte) 3));
        instructionSet.add(new Instruction("STX", this::STX, this::ZP0, (byte) 3));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 3));
        instructionSet.add(new Instruction("DEY", this::DEY, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("TXA", this::TXA, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("STY", this::STY, this::ABS, (byte) 4));
        instructionSet.add(new Instruction("STA", this::STA, this::ABS, (byte) 4));
        instructionSet.add(new Instruction("STX", this::STX, this::ABS, (byte) 4));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("BCC", this::BCC, this::REL, (byte) 2));
        instructionSet.add(new Instruction("STA", this::STA, this::IZY, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("STY", this::STY, this::ZPX, (byte) 4));
        instructionSet.add(new Instruction("STA", this::STA, this::ZPX, (byte) 4));
        instructionSet.add(new Instruction("STX", this::STX, this::ZPY, (byte) 4));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("TYA", this::TYA, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("STA", this::STA, this::ABY, (byte) 5));
        instructionSet.add(new Instruction("TXS", this::TXS, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 5));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 5));
        instructionSet.add(new Instruction("STA", this::STA, this::ABX, (byte) 5));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 5));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 5));
        instructionSet.add(new Instruction("LDY", this::LDY, this::IMM, (byte) 2));
        instructionSet.add(new Instruction("LDA", this::LDA, this::IZX, (byte) 6));
        instructionSet.add(new Instruction("LDX", this::LDX, this::IMM, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("LDY", this::LDY, this::ZP0, (byte) 3));
        instructionSet.add(new Instruction("LDA", this::LDA, this::ZP0, (byte) 3));
        instructionSet.add(new Instruction("LDX", this::LDX, this::ZP0, (byte) 3));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 3));
        instructionSet.add(new Instruction("TAY", this::TAY, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("LDA", this::LDA, this::IMM, (byte) 2));
        instructionSet.add(new Instruction("TAX", this::TAX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("LDY", this::LDY, this::ABS, (byte) 4));
        instructionSet.add(new Instruction("LDA", this::LDA, this::ABS, (byte) 4));
        instructionSet.add(new Instruction("LDX", this::LDX, this::ABS, (byte) 4));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("BCS", this::BCS, this::REL, (byte) 2));
        instructionSet.add(new Instruction("LDA", this::LDA, this::IZY, (byte) 5));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 5));
        instructionSet.add(new Instruction("LDY", this::LDY, this::ZPX, (byte) 4));
        instructionSet.add(new Instruction("LDA", this::LDA, this::ZPX, (byte) 4));
        instructionSet.add(new Instruction("LDX", this::LDX, this::ZPY, (byte) 4));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("CLV", this::CLV, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("LDA", this::LDA, this::ABY, (byte) 4));
        instructionSet.add(new Instruction("TSX", this::TSX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("LDY", this::LDY, this::ABX, (byte) 4));
        instructionSet.add(new Instruction("LDA", this::LDA, this::ABX, (byte) 4));
        instructionSet.add(new Instruction("LDX", this::LDX, this::ABY, (byte) 4));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("CPY", this::CPY, this::IMM, (byte) 2));
        instructionSet.add(new Instruction("CMP", this::CMP, this::IZX, (byte) 6));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 8));
        instructionSet.add(new Instruction("CPY", this::CPY, this::ZP0, (byte) 3));
        instructionSet.add(new Instruction("CMP", this::CMP, this::ZP0, (byte) 3));
        instructionSet.add(new Instruction("DEC", this::DEC, this::ZP0, (byte) 5));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 5));
        instructionSet.add(new Instruction("INY", this::INY, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("CMP", this::CMP, this::IMM, (byte) 2));
        instructionSet.add(new Instruction("DEX", this::DEX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("CPY", this::CPY, this::ABS, (byte) 4));
        instructionSet.add(new Instruction("CMP", this::CMP, this::ABS, (byte) 4));
        instructionSet.add(new Instruction("DEC", this::DEC, this::ABS, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("BNE", this::BNE, this::REL, (byte) 2));
        instructionSet.add(new Instruction("CMP", this::CMP, this::IZY, (byte) 5));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 8));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("CMP", this::CMP, this::ZPX, (byte) 4));
        instructionSet.add(new Instruction("DEC", this::DEC, this::ZPX, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("CLD", this::CLD, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("CMP", this::CMP, this::ABY, (byte) 4));
        instructionSet.add(new Instruction("NOP", this::NOP, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 7));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("CMP", this::CMP, this::ABX, (byte) 4));
        instructionSet.add(new Instruction("DEC", this::DEC, this::ABX, (byte) 7));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 7));
        instructionSet.add(new Instruction("CPX", this::CPX, this::IMM, (byte) 2));
        instructionSet.add(new Instruction("SBC", this::SBC, this::IZX, (byte) 6));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 8));
        instructionSet.add(new Instruction("CPX", this::CPX, this::ZP0, (byte) 3));
        instructionSet.add(new Instruction("SBC", this::SBC, this::ZP0, (byte) 3));
        instructionSet.add(new Instruction("INC", this::INC, this::ZP0, (byte) 5));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 5));
        instructionSet.add(new Instruction("INX", this::INX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("SBC", this::SBC, this::IMM, (byte) 2));
        instructionSet.add(new Instruction("NOP", this::NOP, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::SBC, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("CPX", this::CPX, this::ABS, (byte) 4));
        instructionSet.add(new Instruction("SBC", this::SBC, this::ABS, (byte) 4));
        instructionSet.add(new Instruction("INC", this::INC, this::ABS, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("BEQ", this::BEQ, this::REL, (byte) 2));
        instructionSet.add(new Instruction("SBC", this::SBC, this::IZY, (byte) 5));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 8));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("SBC", this::SBC, this::ZPX, (byte) 4));
        instructionSet.add(new Instruction("INC", this::INC, this::ZPX, (byte) 6));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 6));
        instructionSet.add(new Instruction("SED", this::SED, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("SBC", this::SBC, this::ABY, (byte) 4));
        instructionSet.add(new Instruction("NOP", this::NOP, this::IMP, (byte) 2));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 7));
        instructionSet.add(new Instruction("???", this::NOP, this::IMP, (byte) 4));
        instructionSet.add(new Instruction("SBC", this::SBC, this::ABX, (byte) 4));
        instructionSet.add(new Instruction("INC", this::INC, this::ABX, (byte) 7));
        instructionSet.add(new Instruction("???", this::XXX, this::IMP, (byte) 7));
    }

    @Override
    public void ConnectBus(Bus _bus)
    {
        bus = _bus;
    }

    @Override
    public void Clock()
    {
        if (cycles == 0)
        {
            currentOpCode = ReadByte(PC);
            SetFlag(Flags6502.Unused, true);
            PC++;

            Instruction currentInstruction = GetInstructionSet().get(currentOpCode & 0xFF);
            cycles = currentInstruction.GetCycles();
            byte additionCycle1 = currentInstruction.GetAddrMode().Execute();
            byte additionCycle2 = currentInstruction.GetOperation().Execute();
            cycles += additionCycle1 & additionCycle2;

            SetFlag(Flags6502.Unused, true);
        }

        cycles--;
    }

    @Override
    public void Reset()
    {
        addrAbs = 0xFFFC;
        byte lo = ReadByte(addrAbs + 0);
        byte hi = ReadByte(addrAbs + 1);

        PC = (hi << 8) | lo;

        A  = 0x01;
        X  = 0x00;
        Y  = 0x00;
        SP = (byte) 0xFD;
        F  = (byte) (0x00 | Flags6502.Unused.GetByte());

        addrRel   = 0x0000;
        addrAbs   = 0x0000;
        lastFetch = 0x00;
        cycles    = 0;
    }

    @Override
    public void InterruptRequest()
    {
        if (GetFlag(Flags6502.DisableInterrupts) == 0)
        {
            WriteByte(0x0100 + SP, (byte) ((PC >> 8) & 0x00FF));
            SP--;
            WriteByte(0x0100 + SP, (byte) (PC & 0x00FF));
            SP--;

            SetFlag(Flags6502.Break, false);
            SetFlag(Flags6502.Unused, true);
            SetFlag(Flags6502.DisableInterrupts, true);
            WriteByte(0x0100 + SP, F);
            SP--;

            addrAbs = 0xFFFE;
            byte lo = ReadByte(addrAbs + 0);
            byte hi = ReadByte(addrAbs + 1);
            PC = (hi << 8) | lo;

            cycles = 7;
        }
    }

    @Override
    public void NonMaskableInterrupt()
    {
        WriteByte(0x0100 + SP, (byte) ((PC >> 8) & 0x00FF));
        SP--;
        WriteByte(0x0100 + SP, (byte) (PC & 0x00FF));
        SP--;

        SetFlag(Flags6502.Break, false);
        SetFlag(Flags6502.Unused, true);
        SetFlag(Flags6502.DisableInterrupts, true);
        WriteByte(0x0100 + SP, F);
        SP--;

        addrAbs = 0xFFFA;
        byte lo = ReadByte(addrAbs + 0);
        byte hi = ReadByte(addrAbs + 1);
        PC = (hi << 8) | lo;

        cycles = 8;
    }

    @Override
    public byte FetchData()
    {
        Instruction currentInstruction = GetInstructionSet().get(currentOpCode & 0xFF);
        if (currentInstruction.GetAddrMode() != (ByteFPtrVoid) this::IMP)
        {
            lastFetch = ReadByte(addrAbs);
        }
        return lastFetch;
    }

    @Override
    public List<Instruction> GetInstructionSet()
    {
        return instructionSet;
    }

    private void WriteByte(int addr, byte data)
    {
        bus.WriteByte(addr, data);
    }
    private byte ReadByte(int addr)
    {
        return bus.ReadByte(addr, false);
    }

    private byte GetFlag(Flags6502 flag)
    {
        return (byte) ((F & flag.GetByte()) > 0 ? 1 : 0);
    }
    private void SetFlag(Flags6502 flag, boolean v)
    {
        if (v)
        {
            F |= flag.GetByte();
        }
        else
        {
            F &= ~flag.GetByte();
        }
    }

    public Map<Integer, String> Disassemble(int start, int stop)
    {
        Map<Integer, String> mapLines = new HashMap<Integer, String>();

        int addr = start;
        int lineAddr = 0;

        byte val = 0x00;
        byte hi  = 0x00;
        byte lo  = 0x00;

        while (addr <= stop)
        {
            lineAddr = addr;
            String sInst = "$" + Utils.Hex(addr, 4) + ": ";
            byte opcode = bus.ReadByte(addr, true);
            addr++;
            sInst += GetInstructionSet().get(opcode & 0xFF).GetName() + " ";

            ByteFPtrVoid addrMode = GetInstructionSet().get(opcode & 0xFF).GetAddrMode();
            if (addrMode == (ByteFPtrVoid) this::IMP)
            {
                sInst += " {IMP}";
            }
            else if (addrMode == (ByteFPtrVoid) this::IMM)
            {
                val = bus.ReadByte(addr, true);
                addr++;
                sInst += "#$" + Utils.Hex(val, 2) + " {IMM}";
            }
            else if (addrMode == (ByteFPtrVoid) this::ZP0)
            {
                lo = bus.ReadByte(addr, true);
                addr++;
                hi = 0x00;
                sInst += "$" + Utils.Hex(lo, 2) + " {ZP0}";
            }
            else if (addrMode == (ByteFPtrVoid) this::ZPX)
            {
                lo = bus.ReadByte(addr, true);
                addr++;
                hi = 0x00;
                sInst += "$" + Utils.Hex(lo, 2) + ", X {ZPX}";
            }
            else if (addrMode == (ByteFPtrVoid) this::ZPY)
            {
                lo = bus.ReadByte(addr, true);
                addr++;
                hi = 0x00;
                sInst += "$" + Utils.Hex(lo, 2) + ", Y {ZPY}";
            }
            else if (addrMode == (ByteFPtrVoid) this::IZX)
            {
                lo = bus.ReadByte(addr, true);
                addr++;
                hi = 0x00;
                sInst += "($" + Utils.Hex(lo, 2) + ", X) {IZX}";
            }
            else if (addrMode == (ByteFPtrVoid) this::IZY)
            {
                lo = bus.ReadByte(addr, true);
                addr++;
                hi = 0x00;
                sInst += "($" + Utils.Hex(lo, 2) + "), Y {IZY}";
            }
            else if (addrMode == (ByteFPtrVoid) this::ABS)
            {
                lo = bus.ReadByte(addr, true);
                addr++;
                hi = bus.ReadByte(addr, true);
                addr++;
                sInst += "$" + Utils.Hex((hi << 8) | lo, 4) + " {ABS}";
            }
            else if (addrMode == (ByteFPtrVoid) this::ABX)
            {
                lo = bus.ReadByte(addr, true);
                addr++;
                hi = bus.ReadByte(addr, true);
                addr++;
                sInst += "$" + Utils.Hex((hi << 8) | lo, 4) + ", X {ABX}";
            }
            else if (addrMode == (ByteFPtrVoid) this::ABY)
            {
                lo = bus.ReadByte(addr, true);
                addr++;
                hi = bus.ReadByte(addr, true);
                addr++;
                sInst += "$" + Utils.Hex((hi << 8) | lo, 4) + ", Y {ABY}";
            }
            else if (addrMode == (ByteFPtrVoid) this::IND)
            {
                lo = bus.ReadByte(addr, true);
                addr++;
                hi = bus.ReadByte(addr, true);
                addr++;
                sInst += "($" + Utils.Hex((hi << 8) | lo, 4) + ") {IND}";
            }
            else if (addrMode == (ByteFPtrVoid) this::REL)
            {
                val = bus.ReadByte(addr, true);
                addr++;
                sInst += "$" + Utils.Hex(val, 2) + " [$" + Utils.Hex(addr + val, 4) + "] {REL}";
            }

            mapLines.put(lineAddr, sInst);
        }

        return mapLines;
    }

    // Register Set
    public byte GetRegisterA()
    {
        return A;
    }
    public void SetRegisterA(byte _A)
    {
        A = _A;
    }
    public byte GetRegisterX()
    {
        return X;
    }
    public void SetRegisterX(byte _X)
    {
        X = _X;
    }
    public byte GetRegisterY()
    {
        return Y;
    }
    public void SetRegisterY(byte _Y)
    {
        Y = _Y;
    }
    public byte GetRegisterF()
    {
        return F;
    }
    public void SetRegisterF(byte _F)
    {
        F = _F;
    }
    public byte GetRegisterSP()
    {
        return SP;
    }
    public void SetRegisterSP(byte _SP)
    {
        SP = _SP;
    }
    public int GetRegisterPC()
    {
        return PC;
    }
    public void SetRegisterPC(byte _PC)
    {
        PC = _PC;
    }

    // Addressing Modes
    public byte IMP()
    {
        return (byte) 0x00;
    }
    public byte ZP0()
    {
        return (byte) 0x00;
    }
    public byte ZPY()
    {
        return (byte) 0x00;
    }
    public byte ABS()
    {
        return (byte) 0x00;
    }
    public byte ABY()
    {
        return (byte) 0x00;
    }
    public byte IZX()
    {
        return (byte) 0x00;
    }
    public byte IMM()
    {
        return (byte) 0x00;
    }
    public byte ZPX()
    {
        return (byte) 0x00;
    }
    public byte REL()
    {
        return (byte) 0x00;
    }
    public byte ABX()
    {
        return (byte) 0x00;
    }
    public byte IND()
    {
        return (byte) 0x00;
    }
    public byte IZY()
    {
        return (byte) 0x00;
    }

    // OpCodes
    public byte ADC()
    {
        return (byte) 0x00;
    }
    public byte BCS()
    {
        return (byte) 0x00;
    }
    public byte BNE()
    {
        return (byte) 0x00;
    }
    public byte BVS()
    {
        return (byte) 0x00;
    }
    public byte CLV()
    {
        return (byte) 0x00;
    }
    public byte DEC()
    {
        return (byte) 0x00;
    }
    public byte INC()
    {
        return (byte) 0x00;
    }
    public byte JSR()
    {
        return (byte) 0x00;
    }
    public byte LSR()
    {
        return (byte) 0x00;
    }
    public byte PHP()
    {
        return (byte) 0x00;
    }
    public byte ROR()
    {
        return (byte) 0x00;
    }
    public byte SEC()
    {
        return (byte) 0x00;
    }
    public byte STX()
    {
        return (byte) 0x00;
    }
    public byte TSX()
    {
        return (byte) 0x00;
    }
    public byte AND()
    {
        return (byte) 0x00;
    }
    public byte BEQ()
    {
        return (byte) 0x00;
    }
    public byte BPL()
    {
        return (byte) 0x00;
    }
    public byte CLC()
    {
        return (byte) 0x00;
    }
    public byte CMP()
    {
        return (byte) 0x00;
    }
    public byte DEX()
    {
        return (byte) 0x00;
    }
    public byte INX()
    {
        return (byte) 0x00;
    }
    public byte LDA()
    {
        return (byte) 0x00;
    }
    public byte NOP()
    {
        return (byte) 0x00;
    }
    public byte PLA()
    {
        return (byte) 0x00;
    }
    public byte RTI()
    {
        return (byte) 0x00;
    }
    public byte SED()
    {
        return (byte) 0x00;
    }
    public byte STY()
    {
        return (byte) 0x00;
    }
    public byte TXA()
    {
        return (byte) 0x00;
    }
    public byte ASL()
    {
        return (byte) 0x00;
    }
    public byte BIT()
    {
        return (byte) 0x00;
    }
    public byte BRK()
    {
        return (byte) 0x00;
    }
    public byte CLD()
    {
        return (byte) 0x00;
    }
    public byte CPX()
    {
        return (byte) 0x00;
    }
    public byte DEY()
    {
        return (byte) 0x00;
    }
    public byte INY()
    {
        return (byte) 0x00;
    }
    public byte LDX()
    {
        return (byte) 0x00;
    }
    public byte ORA()
    {
        return (byte) 0x00;
    }
    public byte PLP()
    {
        return (byte) 0x00;
    }
    public byte RTS()
    {
        return (byte) 0x00;
    }
    public byte SEI()
    {
        return (byte) 0x00;
    }
    public byte TAX()
    {
        return (byte) 0x00;
    }
    public byte TXS()
    {
        return (byte) 0x00;
    }
    public byte BCC()
    {
        return (byte) 0x00;
    }
    public byte BMI()
    {
        return (byte) 0x00;
    }
    public byte BVC()
    {
        return (byte) 0x00;
    }
    public byte CLI()
    {
        return (byte) 0x00;
    }
    public byte CPY()
    {
        return (byte) 0x00;
    }
    public byte EOR()
    {
        return (byte) 0x00;
    }
    public byte JMP()
    {
        return (byte) 0x00;
    }
    public byte LDY()
    {
        return (byte) 0x00;
    }
    public byte PHA()
    {
        return (byte) 0x00;
    }
    public byte ROL()
    {
        return (byte) 0x00;
    }
    public byte SBC()
    {
        return (byte) 0x00;
    }
    public byte STA()
    {
        return (byte) 0x00;
    }
    public byte TAY()
    {
        return (byte) 0x00;
    }
    public byte TYA()
    {
        return (byte) 0x00;
    }
    public byte XXX() // Don't Care
    {
        return (byte) 0x00;
    }

    // Registers
    private byte A;
    private byte X;
    private byte Y;
    private byte F;
    private byte SP;
    private int  PC;

    // State
    private byte lastFetch;
    private int  addrAbs;
    private int  addrRel;
    private byte currentOpCode;
    private byte cycles;

    // WARNING: this is NOT intended to be a data structure set, but a mathematical set instead
    List<Instruction> instructionSet;

    // Misc
    private Bus bus;
}

package Core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import Misc.Utils;
import Primitives.ByteFPtrVoid;

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
        instructionSet.add(new Instruction("BRK", this::BRK, AddrModes.IMM, this::IMM, (byte) 7)); // 0x00
        instructionSet.add(new Instruction("ORA", this::ORA, AddrModes.IZX, this::IZX, (byte) 6)); // 0x01
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0x02
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 8)); // 0x03
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 3)); // 0x04
        instructionSet.add(new Instruction("ORA", this::ORA, AddrModes.ZP0, this::ZP0, (byte) 3)); // 0x05
        instructionSet.add(new Instruction("ASL", this::ASL, AddrModes.ZP0, this::ZP0, (byte) 5)); // 0x06
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 5)); // 0x07
        instructionSet.add(new Instruction("PHP", this::PHP, AddrModes.IMP, this::IMP, (byte) 3)); // 0x08
        instructionSet.add(new Instruction("ORA", this::ORA, AddrModes.IMM, this::IMM, (byte) 2)); // 0x09
        instructionSet.add(new Instruction("ASL", this::ASL, AddrModes.IMP, this::IMP, (byte) 2)); // 0x0A
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0x0B
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 4)); // 0x0C
        instructionSet.add(new Instruction("ORA", this::ORA, AddrModes.ABS, this::ABS, (byte) 4)); // 0x0D
        instructionSet.add(new Instruction("ASL", this::ASL, AddrModes.ABS, this::ABS, (byte) 6)); // 0x0E
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 6)); // 0x0F
        instructionSet.add(new Instruction("BPL", this::BPL, AddrModes.REL, this::REL, (byte) 2)); // 0x10
        instructionSet.add(new Instruction("ORA", this::ORA, AddrModes.IZY, this::IZY, (byte) 5)); // 0x11
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0x12
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 8)); // 0x13
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 4)); // 0x14
        instructionSet.add(new Instruction("ORA", this::ORA, AddrModes.ZPX, this::ZPX, (byte) 4)); // 0x15
        instructionSet.add(new Instruction("ASL", this::ASL, AddrModes.ZPX, this::ZPX, (byte) 6)); // 0x16
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 6)); // 0x17
        instructionSet.add(new Instruction("CLC", this::CLC, AddrModes.IMP, this::IMP, (byte) 2)); // 0x18
        instructionSet.add(new Instruction("ORA", this::ORA, AddrModes.ABY, this::ABY, (byte) 4)); // 0x19
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 2)); // 0x1A
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 7)); // 0x1B
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 4)); // 0x1C
        instructionSet.add(new Instruction("ORA", this::ORA, AddrModes.ABX, this::ABX, (byte) 4)); // 0x1D
        instructionSet.add(new Instruction("ASL", this::ASL, AddrModes.ABX, this::ABX, (byte) 7)); // 0x1E
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 7)); // 0x1F
        instructionSet.add(new Instruction("JSR", this::JSR, AddrModes.ABS, this::ABS, (byte) 6)); // 0x20
        instructionSet.add(new Instruction("AND", this::AND, AddrModes.IZX, this::IZX, (byte) 6)); // 0x21
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0x22
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 8)); // 0x23
        instructionSet.add(new Instruction("BIT", this::BIT, AddrModes.ZP0, this::ZP0, (byte) 3)); // 0x24
        instructionSet.add(new Instruction("AND", this::AND, AddrModes.ZP0, this::ZP0, (byte) 3)); // 0x25
        instructionSet.add(new Instruction("ROL", this::ROL, AddrModes.ZP0, this::ZP0, (byte) 5)); // 0x26
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 5)); // 0x27
        instructionSet.add(new Instruction("PLP", this::PLP, AddrModes.IMP, this::IMP, (byte) 4)); // 0x28
        instructionSet.add(new Instruction("AND", this::AND, AddrModes.IMM, this::IMM, (byte) 2)); // 0x29
        instructionSet.add(new Instruction("ROL", this::ROL, AddrModes.IMP, this::IMP, (byte) 2)); // 0x2A
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0x2B
        instructionSet.add(new Instruction("BIT", this::BIT, AddrModes.ABS, this::ABS, (byte) 4)); // 0x2C
        instructionSet.add(new Instruction("AND", this::AND, AddrModes.ABS, this::ABS, (byte) 4)); // 0x2D
        instructionSet.add(new Instruction("ROL", this::ROL, AddrModes.ABS, this::ABS, (byte) 6)); // 0x2E
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 6)); // 0x2F
        instructionSet.add(new Instruction("BMI", this::BMI, AddrModes.REL, this::REL, (byte) 2)); // 0x30
        instructionSet.add(new Instruction("AND", this::AND, AddrModes.IZY, this::IZY, (byte) 5)); // 0x31
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0x32
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 8)); // 0x33
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 4)); // 0x34
        instructionSet.add(new Instruction("AND", this::AND, AddrModes.ZPX, this::ZPX, (byte) 4)); // 0x35
        instructionSet.add(new Instruction("ROL", this::ROL, AddrModes.ZPX, this::ZPX, (byte) 6)); // 0x36
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 6)); // 0x37
        instructionSet.add(new Instruction("SEC", this::SEC, AddrModes.IMP, this::IMP, (byte) 2)); // 0x38
        instructionSet.add(new Instruction("AND", this::AND, AddrModes.ABY, this::ABY, (byte) 4)); // 0x39
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 2)); // 0x3A
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 7)); // 0x3B
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 4)); // 0x3C
        instructionSet.add(new Instruction("AND", this::AND, AddrModes.ABX, this::ABX, (byte) 4)); // 0x3D
        instructionSet.add(new Instruction("ROL", this::ROL, AddrModes.ABX, this::ABX, (byte) 7)); // 0x3E
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 7)); // 0x3F
        instructionSet.add(new Instruction("RTI", this::RTI, AddrModes.IMP, this::IMP, (byte) 6)); // 0x40
        instructionSet.add(new Instruction("EOR", this::EOR, AddrModes.IZX, this::IZX, (byte) 6)); // 0x41
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0x42
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 8)); // 0x43
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 3)); // 0x44
        instructionSet.add(new Instruction("EOR", this::EOR, AddrModes.ZP0, this::ZP0, (byte) 3)); // 0x45
        instructionSet.add(new Instruction("LSR", this::LSR, AddrModes.ZP0, this::ZP0, (byte) 5)); // 0x46
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 5)); // 0x47
        instructionSet.add(new Instruction("PHA", this::PHA, AddrModes.IMP, this::IMP, (byte) 3)); // 0x48
        instructionSet.add(new Instruction("EOR", this::EOR, AddrModes.IMM, this::IMM, (byte) 2)); // 0x49
        instructionSet.add(new Instruction("LSR", this::LSR, AddrModes.IMP, this::IMP, (byte) 2)); // 0x4A
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0x4B
        instructionSet.add(new Instruction("JMP", this::JMP, AddrModes.ABS, this::ABS, (byte) 3)); // 0x4C
        instructionSet.add(new Instruction("EOR", this::EOR, AddrModes.ABS, this::ABS, (byte) 4)); // 0x4D
        instructionSet.add(new Instruction("LSR", this::LSR, AddrModes.ABS, this::ABS, (byte) 6)); // 0x4E
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 6)); // 0x4F
        instructionSet.add(new Instruction("BVC", this::BVC, AddrModes.REL, this::REL, (byte) 2)); // 0x50
        instructionSet.add(new Instruction("EOR", this::EOR, AddrModes.IZY, this::IZY, (byte) 5)); // 0x51
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0x52
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 8)); // 0x53
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 4)); // 0x54
        instructionSet.add(new Instruction("EOR", this::EOR, AddrModes.ZPX, this::ZPX, (byte) 4)); // 0x55
        instructionSet.add(new Instruction("LSR", this::LSR, AddrModes.ZPX, this::ZPX, (byte) 6)); // 0x56
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 6)); // 0x57
        instructionSet.add(new Instruction("CLI", this::CLI, AddrModes.IMP, this::IMP, (byte) 2)); // 0x58
        instructionSet.add(new Instruction("EOR", this::EOR, AddrModes.ABY, this::ABY, (byte) 4)); // 0x59
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 2)); // 0x5A
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 7)); // 0x5B
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 4)); // 0x5C
        instructionSet.add(new Instruction("EOR", this::EOR, AddrModes.ABX, this::ABX, (byte) 4)); // 0x5D
        instructionSet.add(new Instruction("LSR", this::LSR, AddrModes.ABX, this::ABX, (byte) 7)); // 0x5E
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 7)); // 0x5F
        instructionSet.add(new Instruction("RTS", this::RTS, AddrModes.IMP, this::IMP, (byte) 6)); // 0x60
        instructionSet.add(new Instruction("ADC", this::ADC, AddrModes.IZX, this::IZX, (byte) 6)); // 0x61
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0x62
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 8)); // 0x63
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 3)); // 0x64
        instructionSet.add(new Instruction("ADC", this::ADC, AddrModes.ZP0, this::ZP0, (byte) 3)); // 0x65
        instructionSet.add(new Instruction("ROR", this::ROR, AddrModes.ZP0, this::ZP0, (byte) 5)); // 0x66
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 5)); // 0x67
        instructionSet.add(new Instruction("PLA", this::PLA, AddrModes.IMP, this::IMP, (byte) 4)); // 0x68
        instructionSet.add(new Instruction("ADC", this::ADC, AddrModes.IMM, this::IMM, (byte) 2)); // 0x69
        instructionSet.add(new Instruction("ROR", this::ROR, AddrModes.IMP, this::IMP, (byte) 2)); // 0x6A
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0x6B
        instructionSet.add(new Instruction("JMP", this::JMP, AddrModes.IND, this::IND, (byte) 5)); // 0x6C
        instructionSet.add(new Instruction("ADC", this::ADC, AddrModes.ABS, this::ABS, (byte) 4)); // 0x6D
        instructionSet.add(new Instruction("ROR", this::ROR, AddrModes.ABS, this::ABS, (byte) 6)); // 0x6E
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 6)); // 0x6F
        instructionSet.add(new Instruction("BVS", this::BVS, AddrModes.REL, this::REL, (byte) 2)); // 0x70
        instructionSet.add(new Instruction("ADC", this::ADC, AddrModes.IZY, this::IZY, (byte) 5)); // 0x71
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0x72
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 8)); // 0x73
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 4)); // 0x74
        instructionSet.add(new Instruction("ADC", this::ADC, AddrModes.ZPX, this::ZPX, (byte) 4)); // 0x75
        instructionSet.add(new Instruction("ROR", this::ROR, AddrModes.ZPX, this::ZPX, (byte) 6)); // 0x76
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 6)); // 0x77
        instructionSet.add(new Instruction("SEI", this::SEI, AddrModes.IMP, this::IMP, (byte) 2)); // 0x78
        instructionSet.add(new Instruction("ADC", this::ADC, AddrModes.ABY, this::ABY, (byte) 4)); // 0x79
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 2)); // 0x7A
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 7)); // 0x7B
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 4)); // 0x7C
        instructionSet.add(new Instruction("ADC", this::ADC, AddrModes.ABX, this::ABX, (byte) 4)); // 0x7D
        instructionSet.add(new Instruction("ROR", this::ROR, AddrModes.ABX, this::ABX, (byte) 7)); // 0x7E
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 7)); // 0x7F
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 2)); // 0x80
        instructionSet.add(new Instruction("STA", this::STA, AddrModes.IZX, this::IZX, (byte) 6)); // 0x81
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 2)); // 0x82
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 6)); // 0x83
        instructionSet.add(new Instruction("STY", this::STY, AddrModes.ZP0, this::ZP0, (byte) 3)); // 0x84
        instructionSet.add(new Instruction("STA", this::STA, AddrModes.ZP0, this::ZP0, (byte) 3)); // 0x85
        instructionSet.add(new Instruction("STX", this::STX, AddrModes.ZP0, this::ZP0, (byte) 3)); // 0x86
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 3)); // 0x87
        instructionSet.add(new Instruction("DEY", this::DEY, AddrModes.IMP, this::IMP, (byte) 2)); // 0x88
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 2)); // 0x89
        instructionSet.add(new Instruction("TXA", this::TXA, AddrModes.IMP, this::IMP, (byte) 2)); // 0x8A
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0x8B
        instructionSet.add(new Instruction("STY", this::STY, AddrModes.ABS, this::ABS, (byte) 4)); // 0x8C
        instructionSet.add(new Instruction("STA", this::STA, AddrModes.ABS, this::ABS, (byte) 4)); // 0x8D
        instructionSet.add(new Instruction("STX", this::STX, AddrModes.ABS, this::ABS, (byte) 4)); // 0x8E
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 4)); // 0x8F
        instructionSet.add(new Instruction("BCC", this::BCC, AddrModes.REL, this::REL, (byte) 2)); // 0x90
        instructionSet.add(new Instruction("STA", this::STA, AddrModes.IZY, this::IZY, (byte) 6)); // 0x91
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0x92
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 6)); // 0x93
        instructionSet.add(new Instruction("STY", this::STY, AddrModes.ZPX, this::ZPX, (byte) 4)); // 0x94
        instructionSet.add(new Instruction("STA", this::STA, AddrModes.ZPX, this::ZPX, (byte) 4)); // 0x95
        instructionSet.add(new Instruction("STX", this::STX, AddrModes.ZPY, this::ZPY, (byte) 4)); // 0x96
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 4)); // 0x97
        instructionSet.add(new Instruction("TYA", this::TYA, AddrModes.IMP, this::IMP, (byte) 2)); // 0x98
        instructionSet.add(new Instruction("STA", this::STA, AddrModes.ABY, this::ABY, (byte) 5)); // 0x99
        instructionSet.add(new Instruction("TXS", this::TXS, AddrModes.IMP, this::IMP, (byte) 2)); // 0x9A
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 5)); // 0x9B
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 5)); // 0x9C
        instructionSet.add(new Instruction("STA", this::STA, AddrModes.ABX, this::ABX, (byte) 5)); // 0x9D
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 5)); // 0x9E
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 5)); // 0x9F
        instructionSet.add(new Instruction("LDY", this::LDY, AddrModes.IMM, this::IMM, (byte) 2)); // 0xA0
        instructionSet.add(new Instruction("LDA", this::LDA, AddrModes.IZX, this::IZX, (byte) 6)); // 0xA1
        instructionSet.add(new Instruction("LDX", this::LDX, AddrModes.IMM, this::IMM, (byte) 2)); // 0xA2
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 6)); // 0xA3
        instructionSet.add(new Instruction("LDY", this::LDY, AddrModes.ZP0, this::ZP0, (byte) 3)); // 0xA4
        instructionSet.add(new Instruction("LDA", this::LDA, AddrModes.ZP0, this::ZP0, (byte) 3)); // 0xA5
        instructionSet.add(new Instruction("LDX", this::LDX, AddrModes.ZP0, this::ZP0, (byte) 3)); // 0xA6
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 3)); // 0xA7
        instructionSet.add(new Instruction("TAY", this::TAY, AddrModes.IMP, this::IMP, (byte) 2)); // 0xA8
        instructionSet.add(new Instruction("LDA", this::LDA, AddrModes.IMM, this::IMM, (byte) 2)); // 0xA9
        instructionSet.add(new Instruction("TAX", this::TAX, AddrModes.IMP, this::IMP, (byte) 2)); // 0xAA
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0xAB
        instructionSet.add(new Instruction("LDY", this::LDY, AddrModes.ABS, this::ABS, (byte) 4)); // 0xAC
        instructionSet.add(new Instruction("LDA", this::LDA, AddrModes.ABS, this::ABS, (byte) 4)); // 0xAD
        instructionSet.add(new Instruction("LDX", this::LDX, AddrModes.ABS, this::ABS, (byte) 4)); // 0xAE
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 4)); // 0xAF
        instructionSet.add(new Instruction("BCS", this::BCS, AddrModes.REL, this::REL, (byte) 2)); // 0xB0
        instructionSet.add(new Instruction("LDA", this::LDA, AddrModes.IZY, this::IZY, (byte) 5)); // 0xB1
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0xB2
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 5)); // 0xB3
        instructionSet.add(new Instruction("LDY", this::LDY, AddrModes.ZPX, this::ZPX, (byte) 4)); // 0xB4
        instructionSet.add(new Instruction("LDA", this::LDA, AddrModes.ZPX, this::ZPX, (byte) 4)); // 0xB5
        instructionSet.add(new Instruction("LDX", this::LDX, AddrModes.ZPY, this::ZPY, (byte) 4)); // 0xB6
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 4)); // 0xB7
        instructionSet.add(new Instruction("CLV", this::CLV, AddrModes.IMP, this::IMP, (byte) 2)); // 0xB8
        instructionSet.add(new Instruction("LDA", this::LDA, AddrModes.ABY, this::ABY, (byte) 4)); // 0xB9
        instructionSet.add(new Instruction("TSX", this::TSX, AddrModes.IMP, this::IMP, (byte) 2)); // 0xBA
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 4)); // 0xBB
        instructionSet.add(new Instruction("LDY", this::LDY, AddrModes.ABX, this::ABX, (byte) 4)); // 0xBC
        instructionSet.add(new Instruction("LDA", this::LDA, AddrModes.ABX, this::ABX, (byte) 4)); // 0xBD
        instructionSet.add(new Instruction("LDX", this::LDX, AddrModes.ABY, this::ABY, (byte) 4)); // 0xBE
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 4)); // 0xBF
        instructionSet.add(new Instruction("CPY", this::CPY, AddrModes.IMM, this::IMM, (byte) 2)); // 0xC0
        instructionSet.add(new Instruction("CMP", this::CMP, AddrModes.IZX, this::IZX, (byte) 6)); // 0xC1
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 2)); // 0xC2
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 8)); // 0xC3
        instructionSet.add(new Instruction("CPY", this::CPY, AddrModes.ZP0, this::ZP0, (byte) 3)); // 0xC4
        instructionSet.add(new Instruction("CMP", this::CMP, AddrModes.ZP0, this::ZP0, (byte) 3)); // 0xC5
        instructionSet.add(new Instruction("DEC", this::DEC, AddrModes.ZP0, this::ZP0, (byte) 5)); // 0xC6
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 5)); // 0xC7
        instructionSet.add(new Instruction("INY", this::INY, AddrModes.IMP, this::IMP, (byte) 2)); // 0xC8
        instructionSet.add(new Instruction("CMP", this::CMP, AddrModes.IMM, this::IMM, (byte) 2)); // 0xC9
        instructionSet.add(new Instruction("DEX", this::DEX, AddrModes.IMP, this::IMP, (byte) 2)); // 0xCA
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0xCB
        instructionSet.add(new Instruction("CPY", this::CPY, AddrModes.ABS, this::ABS, (byte) 4)); // 0xCC
        instructionSet.add(new Instruction("CMP", this::CMP, AddrModes.ABS, this::ABS, (byte) 4)); // 0xCD
        instructionSet.add(new Instruction("DEC", this::DEC, AddrModes.ABS, this::ABS, (byte) 6)); // 0xCE
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 6)); // 0xCF
        instructionSet.add(new Instruction("BNE", this::BNE, AddrModes.REL, this::REL, (byte) 2)); // 0xD0
        instructionSet.add(new Instruction("CMP", this::CMP, AddrModes.IZY, this::IZY, (byte) 5)); // 0xD1
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0xD2
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 8)); // 0xD3
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 4)); // 0xD4
        instructionSet.add(new Instruction("CMP", this::CMP, AddrModes.ZPX, this::ZPX, (byte) 4)); // 0xD5
        instructionSet.add(new Instruction("DEC", this::DEC, AddrModes.ZPX, this::ZPX, (byte) 6)); // 0xD6
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 6)); // 0xD7
        instructionSet.add(new Instruction("CLD", this::CLD, AddrModes.IMP, this::IMP, (byte) 2)); // 0xD8
        instructionSet.add(new Instruction("CMP", this::CMP, AddrModes.ABY, this::ABY, (byte) 4)); // 0xD9
        instructionSet.add(new Instruction("NOP", this::NOP, AddrModes.IMP, this::IMP, (byte) 2)); // 0xDA
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 7)); // 0xDB
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 4)); // 0xDC
        instructionSet.add(new Instruction("CMP", this::CMP, AddrModes.ABX, this::ABX, (byte) 4)); // 0xDD
        instructionSet.add(new Instruction("DEC", this::DEC, AddrModes.ABX, this::ABX, (byte) 7)); // 0xDE
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 7)); // 0xDF
        instructionSet.add(new Instruction("CPX", this::CPX, AddrModes.IMM, this::IMM, (byte) 2)); // 0xE0
        instructionSet.add(new Instruction("SBC", this::SBC, AddrModes.IZX, this::IZX, (byte) 6)); // 0xE1
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 2)); // 0xE2
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 8)); // 0xE3
        instructionSet.add(new Instruction("CPX", this::CPX, AddrModes.ZP0, this::ZP0, (byte) 3)); // 0xE4
        instructionSet.add(new Instruction("SBC", this::SBC, AddrModes.ZP0, this::ZP0, (byte) 3)); // 0xE5
        instructionSet.add(new Instruction("INC", this::INC, AddrModes.ZP0, this::ZP0, (byte) 5)); // 0xE6
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 5)); // 0xE7
        instructionSet.add(new Instruction("INX", this::INX, AddrModes.IMP, this::IMP, (byte) 2)); // 0xE8
        instructionSet.add(new Instruction("SBC", this::SBC, AddrModes.IMM, this::IMM, (byte) 2)); // 0xE9
        instructionSet.add(new Instruction("NOP", this::NOP, AddrModes.IMP, this::IMP, (byte) 2)); // 0xEA
        instructionSet.add(new Instruction("???", this::SBC, AddrModes.IMP, this::IMP, (byte) 2)); // 0xEB
        instructionSet.add(new Instruction("CPX", this::CPX, AddrModes.ABS, this::ABS, (byte) 4)); // 0xEC
        instructionSet.add(new Instruction("SBC", this::SBC, AddrModes.ABS, this::ABS, (byte) 4)); // 0xED
        instructionSet.add(new Instruction("INC", this::INC, AddrModes.ABS, this::ABS, (byte) 6)); // 0xEE
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 6)); // 0xEF
        instructionSet.add(new Instruction("BEQ", this::BEQ, AddrModes.REL, this::REL, (byte) 2)); // 0xF0
        instructionSet.add(new Instruction("SBC", this::SBC, AddrModes.IZY, this::IZY, (byte) 5)); // 0xF1
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 2)); // 0xF2
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 8)); // 0xF3
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 4)); // 0xF4
        instructionSet.add(new Instruction("SBC", this::SBC, AddrModes.ZPX, this::ZPX, (byte) 4)); // 0xF5
        instructionSet.add(new Instruction("INC", this::INC, AddrModes.ZPX, this::ZPX, (byte) 6)); // 0xF6
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 6)); // 0xF7
        instructionSet.add(new Instruction("SED", this::SED, AddrModes.IMP, this::IMP, (byte) 2)); // 0xF8
        instructionSet.add(new Instruction("SBC", this::SBC, AddrModes.ABY, this::ABY, (byte) 4)); // 0xF9
        instructionSet.add(new Instruction("NOP", this::NOP, AddrModes.IMP, this::IMP, (byte) 2)); // 0xFA
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 7)); // 0xFB
        instructionSet.add(new Instruction("???", this::NOP, AddrModes.IMP, this::IMP, (byte) 4)); // 0xFC
        instructionSet.add(new Instruction("SBC", this::SBC, AddrModes.ABX, this::ABX, (byte) 4)); // 0xFD
        instructionSet.add(new Instruction("INC", this::INC, AddrModes.ABX, this::ABX, (byte) 7)); // 0xFE
        instructionSet.add(new Instruction("???", this::XXX, AddrModes.IMP, this::IMP, (byte) 7)); // 0xFF
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

            Instruction currentInstruction = GetInstruction(currentOpCode);
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

        A  = 0x00;
        X  = 0x00;
        Y  = 0x00;
        SP = (byte) 0xFD;
        F  = (byte) (0x00 | Flags6502.Unused.GetByte());

        addrRel   = 0x0000;
        addrAbs   = 0x0000;
        lastFetch = 0x00;
        cycles    = 8;
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
        Instruction currentInstruction = GetInstruction(currentOpCode);
        if (currentInstruction.GetAddrMode().GetMetaType() != AddrModes.IMP)
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

    @Override
    public Instruction GetInstruction(byte opCode)
    {
        return GetInstructionSet().get(opCode & 0xFF);
    }

    private void WriteByte(int addr, byte data)
    {
        bus.CpuWrite(addr, data);
    }
    private byte ReadByte(int addr)
    {
        return bus.CpuRead(addr, false);
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
            byte opcode = bus.CpuRead(addr, true);
            addr++;
            sInst += GetInstruction(opcode).GetName() + " ";

            AddrModes addrMode = GetInstruction(opcode).GetAddrMode().GetMetaType();
            if (addrMode == AddrModes.IMP)
            {
                sInst += " {IMP}";
            }
            else if (addrMode == AddrModes.IMM)
            {
                val = bus.CpuRead(addr, true);
                addr++;
                sInst += "#$" + Utils.Hex(val, 2) + " {IMM}";
            }
            else if (addrMode == AddrModes.ZP0)
            {
                lo = bus.CpuRead(addr, true);
                addr++;
                hi = 0x00;
                sInst += "$" + Utils.Hex(lo, 2) + " {ZP0}";
            }
            else if (addrMode == AddrModes.ZPX)
            {
                lo = bus.CpuRead(addr, true);
                addr++;
                hi = 0x00;
                sInst += "$" + Utils.Hex(lo, 2) + ", X {ZPX}";
            }
            else if (addrMode == AddrModes.ZPY)
            {
                lo = bus.CpuRead(addr, true);
                addr++;
                hi = 0x00;
                sInst += "$" + Utils.Hex(lo, 2) + ", Y {ZPY}";
            }
            else if (addrMode == AddrModes.IZX)
            {
                lo = bus.CpuRead(addr, true);
                addr++;
                hi = 0x00;
                sInst += "($" + Utils.Hex(lo, 2) + ", X) {IZX}";
            }
            else if (addrMode == AddrModes.IZY)
            {
                lo = bus.CpuRead(addr, true);
                addr++;
                hi = 0x00;
                sInst += "($" + Utils.Hex(lo, 2) + "), Y {IZY}";
            }
            else if (addrMode == AddrModes.ABS)
            {
                lo = bus.CpuRead(addr, true);
                addr++;
                hi = bus.CpuRead(addr, true);
                addr++;
                sInst += "$" + Utils.Hex((hi << 8) | lo, 4) + " {ABS}";
            }
            else if (addrMode == AddrModes.ABX)
            {
                lo = bus.CpuRead(addr, true);
                addr++;
                hi = bus.CpuRead(addr, true);
                addr++;
                sInst += "$" + Utils.Hex((hi << 8) | lo, 4) + ", X {ABX}";
            }
            else if (addrMode == AddrModes.ABY)
            {
                lo = bus.CpuRead(addr, true);
                addr++;
                hi = bus.CpuRead(addr, true);
                addr++;
                sInst += "$" + Utils.Hex((hi << 8) | lo, 4) + ", Y {ABY}";
            }
            else if (addrMode == AddrModes.IND)
            {
                lo = bus.CpuRead(addr, true);
                addr++;
                hi = bus.CpuRead(addr, true);
                addr++;
                sInst += "($" + Utils.Hex((hi << 8) | lo, 4) + ") {IND}";
            }
            else if (addrMode == AddrModes.REL)
            {
                val = bus.CpuRead(addr, true);
                addr++;
                sInst += "$" + Utils.Hex(val, 2) + " [$" + Utils.Hex(addr + (val & 0xFF), 4) + "] {REL}";
            }
            else
            {
                sInst += "???";
            }

            mapLines.put(lineAddr, sInst);
        }

        return mapLines;
    }

    public boolean Complete()
    {
        return cycles == 0;
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
        lastFetch = A;
        return (byte) 0x00;
    }
    public byte IMM()
    {
        addrAbs = PC++;
        return (byte) 0x00;
    }
    public byte ZP0()
    {
        addrAbs = ReadByte(PC);
        PC++;
        addrAbs &= 0x00FF;
        return (byte) 0x00;
    }
    public byte ZPX()
    {
        addrAbs = ReadByte(PC) + X;
        PC++;
        addrAbs &= 0x00FF;
        return (byte) 0x00;
    }
    public byte ZPY()
    {
        addrAbs = ReadByte(PC) + Y;
        PC++;
        addrAbs &= 0x00FF;
        return (byte) 0x00;
    }
    public byte REL()
    {
        addrRel = ReadByte(PC);
        PC++;
        if ((addrRel & 0x80) != 0)
        {
            addrRel |= 0xFF00;
        }
        return (byte) 0x00;
    }
    public byte ABS()
    {
        byte lo = ReadByte(PC);
        PC++;
        byte hi = ReadByte(PC);
        PC++;
        addrAbs = (hi << 8) | lo;
        return (byte) 0x00;
    }
    public byte ABX()
    {
        byte lo = ReadByte(PC);
        PC++;
        byte hi = ReadByte(PC);
        PC++;
        addrAbs = (hi << 8) | lo;
        addrAbs += X;

        return (addrAbs & 0xFF00) != (hi << 8) ? (byte) 1 : (byte) 0;
    }
    public byte ABY()
    {
        byte lo = ReadByte(PC);
        PC++;
        byte hi = ReadByte(PC);
        PC++;
        addrAbs = (hi << 8) | lo;
        addrAbs += Y;

        return (addrAbs & 0xFF00) != (hi << 8) ? (byte) 1 : (byte) 0;
    }
    public byte IND()
    {
        int lo = ReadByte(PC);
        PC++;
        int hi = ReadByte(PC);
        PC++;
        int ptr = (hi << 8) | lo;

        if (lo == 0x00FF)
        {
            addrAbs = (ReadByte(ptr & 0xFF00) << 8) | ReadByte(ptr + 0);
        }
        else
        {
            addrAbs = (ReadByte(ptr + 1) << 8) | ReadByte(ptr + 0);
        }

        return (byte) 0x00;
    }
    public byte IZX()
    {
        int t = ReadByte(PC);
        PC++;
        int lo = ReadByte((int) (t + (int) X) & 0x00FF);
        int hi = ReadByte((int) (t + (int) X + 1) & 0x00FF);
        addrAbs = (hi << 8) | lo;
        return (byte) 0x00;
    }
    public byte IZY()
    {
        int t = ReadByte(PC);
        PC++;
        int lo = ReadByte(t & 0x00FF);
        int hi = ReadByte((t + 1) & 0x00FF);
        addrAbs = (hi << 8) | lo;
        addrAbs += Y;
        return (addrAbs & 0xFF00) != (hi << 8) ? (byte) 1 : (byte) 0;
    }

    // OpCodes
    public byte ADC()
    {
        FetchData();
        int temp = (int) A + (int) lastFetch + (int) GetFlag(Flags6502.CarryBit);
        SetFlag(Flags6502.CarryBit, temp > 255);
        SetFlag(Flags6502.Zero, (temp & 0x00FF) == 0);
        byte hasOverflow = (byte) ((~((int) A ^ (int) lastFetch) & ((int) A ^ (int) temp)) & 0x0080);
        SetFlag(Flags6502.Overflow, hasOverflow != 0);
        SetFlag(Flags6502.Negative, (temp & 0x80) != 0);
        A = (byte) (temp & 0x00FF);
        return (byte) 0x01;
    }
    public byte SBC()
    {
        FetchData();
        int value = ((int)lastFetch) ^ 0x00FF;
        int temp = (int) A + value + (int) GetFlag(Flags6502.CarryBit);
        SetFlag(Flags6502.CarryBit, (temp & 0xFF00) != 0);
        SetFlag(Flags6502.Zero, ((temp & 0x00FF) == 0));
        byte hasOverflow = (byte) ((temp ^ (int) A) & (temp ^ value) & 0x0080);
        SetFlag(Flags6502.Overflow, hasOverflow != 0);
        SetFlag(Flags6502.Negative, (temp & 0x0080) != 0);
        A = (byte) (temp & 0x00FF);
        return (byte) 0x01;
    }
    public byte AND()
    {
        FetchData();
        A = (byte) (A & lastFetch);
        SetFlag(Flags6502.Zero, A == 0x00);
        SetFlag(Flags6502.Negative, (A & 0x80) != 0);
        return (byte) 0x01;
    }
    public byte ASL()
    {
        FetchData();
        int temp = (int) lastFetch << 1;
        SetFlag(Flags6502.CarryBit, (temp & 0xFF00) > 0);
        SetFlag(Flags6502.Zero, (temp & 0x00FF) == 0x00);
        SetFlag(Flags6502.Negative, (temp & 0x80) != 0);

        if (GetInstruction(currentOpCode).GetAddrMode().GetMetaType() == AddrModes.IMP)
        {
            A = (byte) (temp & 0x00FF);
        }
        else
        {
            WriteByte(addrAbs, (byte) (temp & 0x00FF));
        }

        return (byte) 0x00;
    }
    public byte BCC()
    {
        if (GetFlag(Flags6502.CarryBit) == 0x00)
        {
            cycles++;
            addrAbs = PC + addrRel;

            if ((addrAbs & 0xFF00) != (PC & 0xFF00))
            {
                cycles++;
            }

            PC = addrAbs;
        }

        return (byte) 0x00;
    }
    public byte BCS()
    {
        if (GetFlag(Flags6502.CarryBit) == 0x01)
        {
            cycles++;
            addrAbs = PC + addrRel;

            if ((addrAbs & 0xFF00) != (PC & 0xFF00))
            {
                cycles++;
            }

            PC = addrAbs;
        }

        return (byte) 0x00;
    }
    public byte BEQ()
    {
        if (GetFlag(Flags6502.Zero) == 0x01)
        {
            cycles++;
            addrAbs = PC + addrRel;

            if ((addrAbs & 0xFF00) != (PC & 0xFF00))
            {
                cycles++;
            }

            PC = addrAbs;
        }

        return (byte) 0x00;
    }
    public byte BIT()
    {
        FetchData();
        int temp = A & lastFetch;
        SetFlag(Flags6502.Zero, (temp & 0x00FF) == 0x00);
        SetFlag(Flags6502.Negative, (lastFetch & (1 << 7)) != 0);
        SetFlag(Flags6502.Overflow, (lastFetch & (1 << 6)) != 0);
        return (byte) 0x00;
    }
    public byte BMI()
    {
        if (GetFlag(Flags6502.Negative) == 0x01)
        {
            cycles++;
            addrAbs = PC + addrRel;

            if ((addrAbs & 0xFF00) != (PC & 0xFF00))
            {
                cycles++;
            }

            PC = addrAbs;
        }

        return (byte) 0x00;
    }
    public byte BNE()
    {
        if (GetFlag(Flags6502.Zero) == 0x00)
        {
            cycles++;
            addrAbs = PC + addrRel;

            if ((addrAbs & 0xFF00) != (PC & 0xFF00))
            {
                cycles++;
            }

            PC = addrAbs;
        }

        return (byte) 0x00;
    }
    public byte BPL()
    {
        if (GetFlag(Flags6502.Negative) == 0x00)
        {
            cycles++;
            addrAbs = PC + addrRel;

            if ((addrAbs & 0xFF00) != (PC & 0xFF00))
            {
                cycles++;
            }

            PC = addrAbs;
        }

        return (byte) 0x00;
    }
    public byte BRK()
    {
        PC++;
        SetFlag(Flags6502.DisableInterrupts, true);
        WriteByte(0x0100 + SP, (byte) ((PC >> 8) & 0x00FF));
        SP--;
        WriteByte(0x0100 + SP, (byte) (PC & 0x00FF));
        SP--;
        SetFlag(Flags6502.Break, true);
        WriteByte(0x0100 + SP, (byte) F);
        SP--;
        SetFlag(Flags6502.Break, false);
        PC = (int) ReadByte(0xFFFE) | ((int) ReadByte(0xFFFF) << 8);
        return (byte) 0x00;
    }
    public byte BVC()
    {
        if (GetFlag(Flags6502.Overflow) == 0x00)
        {
            cycles++;
            addrAbs = PC + addrRel;

            if ((addrAbs & 0xFF00) != (PC & 0xFF00))
            {
                cycles++;
            }

            PC = addrAbs;
        }

        return (byte) 0x00;
    }
    public byte BVS()
    {
        if (GetFlag(Flags6502.Overflow) == 0x01)
        {
            cycles++;
            addrAbs = PC + addrRel;

            if ((addrAbs & 0xFF00) != (PC & 0xFF00))
            {
                cycles++;
            }

            PC = addrAbs;
        }

        return (byte) 0x00;
    }
    public byte CLC()
    {
        SetFlag(Flags6502.CarryBit, false);
        return (byte) 0x00;
    }
    public byte CLD()
    {
        SetFlag(Flags6502.DecimalMode, false);
        return (byte) 0x00;
    }
    public byte CLI()
    {
        SetFlag(Flags6502.DisableInterrupts, false);
        return (byte) 0x00;
    }
    public byte CLV()
    {
        SetFlag(Flags6502.Overflow, false);
        return (byte) 0x00;
    }
    public byte CMP()
    {
        FetchData();
        int temp = (int) A - (int) lastFetch;
        SetFlag(Flags6502.CarryBit, A >= lastFetch);
        SetFlag(Flags6502.Zero, (temp & 0x00FF) == 0x0000);
        SetFlag(Flags6502.Negative, (temp & 0x0080) != 0);
        return (byte) 0x01;
    }
    public byte CPX()
    {
        FetchData();
        int temp = (int) X - (int) lastFetch;
        SetFlag(Flags6502.CarryBit, X >= lastFetch);
        SetFlag(Flags6502.Zero, (temp & 0x00FF) == 0x0000);
        SetFlag(Flags6502.Negative, (temp & 0x0080) != 0);
        return (byte) 0x00;
    }
    public byte CPY()
    {
        FetchData();
        int temp = (int) Y - (int) lastFetch;
        SetFlag(Flags6502.CarryBit, Y >= lastFetch);
        SetFlag(Flags6502.Zero, (temp & 0x00FF) == 0x0000);
        SetFlag(Flags6502.Negative, (temp & 0x0080) != 0);
        return (byte) 0x00;
    }
    public byte DEC()
    {
        FetchData();
        int temp = lastFetch - 1;
        WriteByte(addrAbs, (byte) (temp & 0x00FF));
        SetFlag(Flags6502.Zero, (temp & 0x00FF) == 0x0000);
        SetFlag(Flags6502.Negative, (temp & 0x0080) != 0);
        return (byte) 0x00;
    }
    public byte DEX()
    {
        X--;
        SetFlag(Flags6502.Zero, X == 0x00);
        SetFlag(Flags6502.Negative, (X & 0x80) != 0);
        return (byte) 0x00;
    }
    public byte DEY()
    {
        Y--;
        SetFlag(Flags6502.Zero, Y == 0x00);
        SetFlag(Flags6502.Negative, (Y & 0x80) != 0);
        return (byte) 0x00;
    }
    public byte EOR()
    {
        FetchData();
        A = (byte) (A ^ lastFetch);
        SetFlag(Flags6502.Zero, A == 0x00);
        SetFlag(Flags6502.Negative, (A & 0x80) != 0);
        return (byte) 0x01;
    }
    public byte INC()
    {
        FetchData();
        int temp = lastFetch + 1;
        WriteByte(addrAbs, (byte) (temp & 0x00FF));
        SetFlag(Flags6502.Zero, (temp & 0x00FF) == 0x0000);
        SetFlag(Flags6502.Negative, (temp & 0x0080) != 0);
        return (byte) 0x00;
    }
    public byte INX()
    {
        X++;
        SetFlag(Flags6502.Zero, X == 0x00);
        SetFlag(Flags6502.Negative, (X & 0x80) != 0);
        return (byte) 0x00;
    }
    public byte INY()
    {
        Y++;
        SetFlag(Flags6502.Zero, Y == 0x00);
        SetFlag(Flags6502.Negative, (Y & 0x80) != 0);
        return (byte) 0x00;
    }
    public byte JMP()
    {
        PC = addrAbs;
        return (byte) 0x00;
    }
    public byte JSR()
    {
        PC--;
        WriteByte(0x0100 + SP, (byte) ((PC >> 8) & 0x00FF));
        SP--;
        WriteByte(0x0100 + SP, (byte) (PC & 0x00FF));
        SP--;
        PC = addrAbs;
        return (byte) 0x00;
    }
    public byte LDA()
    {
        FetchData();
        A = lastFetch;
        SetFlag(Flags6502.Zero, A == 0x00);
        SetFlag(Flags6502.Negative, (A & 0x80) != 0);
        return (byte) 0x01;
    }
    public byte LDX()
    {
        FetchData();
        X = lastFetch;
        SetFlag(Flags6502.Zero, X == 0x00);
        SetFlag(Flags6502.Negative, (X & 0x80) != 0);
        return (byte) 0x01;
    }
    public byte LDY()
    {
        FetchData();
        Y = lastFetch;
        SetFlag(Flags6502.Zero, Y == 0x00);
        SetFlag(Flags6502.Negative, (Y & 0x80) != 0);
        return (byte) 0x01;
    }
    public byte LSR()
    {
        FetchData();
        SetFlag(Flags6502.CarryBit, (lastFetch & 0x0001) != 0);
        int temp = lastFetch >> 1;
        SetFlag(Flags6502.Zero, (temp & 0x00FF) == 0x0000);
        SetFlag(Flags6502.Negative, (temp & 0x0080) != 0);

        if (GetInstruction(currentOpCode).GetAddrMode().GetMetaType() == AddrModes.IMP)
        {
            A = (byte) (temp & 0x00FF);
        }
        else
        {
            WriteByte(addrAbs, (byte) (temp & 0x00FF));
        }

        return (byte) 0x00;
    }
    public byte NOP()
    {
        switch (currentOpCode)
        {
            case (byte) 0x1C:
            case (byte) 0x3C:
            case (byte) 0x5C:
            case (byte) 0x7C:
            case (byte) 0xDC:
            case (byte) 0xFC:
                return (byte) 0x01;
        }

        return (byte) 0x00;
    }
    public byte ORA()
    {
        FetchData();
        A = (byte) (A | lastFetch);
        SetFlag(Flags6502.Zero, A == 0x00);
        SetFlag(Flags6502.Negative, (A & 0x80) != 0);
        return (byte) 0x01;
    }
    public byte PHA()
    {
        WriteByte(0x0100 + SP, A);
        SP--;
        return (byte) 0x00;
    }
    public byte PHP()
    {
        WriteByte(0x0100 + SP, (byte) (F | Flags6502.Break.GetByte() | Flags6502.Unused.GetByte()));
        SetFlag(Flags6502.Break, false);
        SetFlag(Flags6502.Unused, false);
        SP--;
        return (byte) 0x00;
    }
    public byte PLA()
    {
        SP++;
        A = ReadByte(0x0100 + SP);
        SetFlag(Flags6502.Zero, A == 0x00);
        SetFlag(Flags6502.Negative, (A & 0x80) != 0);
        return (byte) 0x00;
    }
    public byte PLP()
    {
        SP++;
        F = ReadByte(0x0100 + SP);
        SetFlag(Flags6502.Unused, true);
        return (byte) 0x00;
    }
    public byte ROL()
    {
        FetchData();
        int temp = (int) (lastFetch << 1) | GetFlag(Flags6502.CarryBit);
        SetFlag(Flags6502.CarryBit, (temp & 0xFF00) != 0);
        SetFlag(Flags6502.Zero, (temp & 0x00FF) == 0x0000);
        SetFlag(Flags6502.Negative, (temp & 0x0080) != 0);

        if (GetInstruction(currentOpCode).GetAddrMode().GetMetaType() == AddrModes.IMP)
        {
            A = (byte) (temp & 0x00FF);
        }
        else
        {
            WriteByte(addrAbs, (byte) (temp & 0x00FF));
        }

        return (byte) 0x00;
    }
    public byte ROR()
    {
        FetchData();
        int temp = (int) (GetFlag(Flags6502.CarryBit) << 7) | (lastFetch >> 1);
        SetFlag(Flags6502.CarryBit, (lastFetch & 0x01) != 0);
        SetFlag(Flags6502.Zero, (temp & 0x00FF) == 0x00);
        SetFlag(Flags6502.Negative, (temp & 0x0080) != 0);

        if (GetInstruction(currentOpCode).GetAddrMode().GetMetaType() == AddrModes.IMP)
        {
            A = (byte) (temp & 0x00FF);
        }
        else
        {
            WriteByte(addrAbs, (byte) (temp & 0x00FF));
        }

        return (byte) 0x00;
    }
    public byte RTI()
    {
        SP++;
        F = ReadByte(0x0100 + SP);
        F &= ~Flags6502.Break.GetByte();
        F &= ~Flags6502.Unused.GetByte();
        SP++;
        PC = ReadByte(0x0100 + SP);
        SP++;
        PC |= ((int) ReadByte(0x0100 + SP)) << 8;
        return (byte) 0x00;
    }
    public byte RTS()
    {
        SP++;
        PC = ReadByte(0x0100 + SP);
        SP++;
        PC |= ((int) ReadByte(0x0100 + SP)) << 8;
        PC++;
        return (byte) 0x00;
    }
    public byte SEC()
    {
        SetFlag(Flags6502.CarryBit, true);
        return (byte) 0x00;
    }
    public byte SED()
    {
        SetFlag(Flags6502.DecimalMode, true);
        return (byte) 0x00;
    }
    public byte SEI()
    {
        SetFlag(Flags6502.DisableInterrupts, true);
        return (byte) 0x00;
    }
    public byte STA()
    {
        WriteByte(addrAbs, A);
        return (byte) 0x00;
    }
    public byte STX()
    {
        WriteByte(addrAbs, X);
        return (byte) 0x00;
    }
    public byte STY()
    {
        WriteByte(addrAbs, Y);
        return (byte) 0x00;
    }
    public byte TAX()
    {
        X = A;
        SetFlag(Flags6502.Zero, X == 0x00);
        SetFlag(Flags6502.Negative, (X & 0x80) != 0);
        return (byte) 0x00;
    }
    public byte TAY()
    {
        Y = A;
        SetFlag(Flags6502.Zero, Y == 0x00);
        SetFlag(Flags6502.Negative, (Y & 0x80) != 0);
        return (byte) 0x00;
    }
    public byte TSX()
    {
        X = SP;
        SetFlag(Flags6502.Zero, X == 0x00);
        SetFlag(Flags6502.Negative, (X & 0x80) != 0);
        return (byte) 0x00;
    }
    public byte TXA()
    {
        A = X;
        SetFlag(Flags6502.Zero, A == 0x00);
        SetFlag(Flags6502.Negative, (A & 0x80) != 0);
        return (byte) 0x00;
    }
    public byte TXS()
    {
        SP = X;
        return (byte) 0x00;
    }
    public byte TYA()
    {
        A = Y;
        SetFlag(Flags6502.Zero, A == 0x00);
        SetFlag(Flags6502.Negative, (A & 0x80) != 0);
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

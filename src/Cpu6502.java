public class Cpu6502
{
    public Cpu6502()
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
    }

    public void ConnectBus(Bus _bus)
    {
        bus = _bus;
    }

    public void Clock()
    {
    }

    public void Reset()
    {
    }

    public void InterruptRequest()
    {
    }

    public void NonMaskableInterrupt()
    {
    }

    public byte FetchData()
    {
        return (byte) 0x00;
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
        return (byte) 0x00;
    }
    private void SetFlag(Flags6502 flag, boolean v)
    {
    }

    // Registers
    private byte A;
    private byte X;
    private byte Y;
    private byte F;
    private byte SP;
    private int  PC;

    private byte lastFetch;
    private int  addrAbs;
    private int  addrRel;
    private byte currentOpCode;
    private byte cycles;

    private Bus bus;
}

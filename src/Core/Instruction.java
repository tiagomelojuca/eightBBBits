package Core;

import Primitives.ByteFPtrVoid;

public class Instruction
{
    public Instruction(
        String _name,
        ByteFPtrVoid _fptrOperation,
        AddrModes _addrMode,
        ByteFPtrVoid _fptrAddrMode,
        byte _cycles
    )
    {
        name = _name;
        fptrOperation = _fptrOperation;
        addrMode = new AddrMode(_addrMode, _fptrAddrMode);
        cycles = _cycles;
    }

    public String GetName()
    {
        return name;
    }
    public ByteFPtrVoid GetOperation()
    {
        return fptrOperation;
    }
    public AddrMode GetAddrMode()
    {
        return addrMode;
    }
    public byte GetCycles()
    {
        return cycles;
    }

    private String name;
    private ByteFPtrVoid fptrOperation;
    private AddrMode addrMode;
    private byte cycles;
}

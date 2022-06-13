package Core;

import Primitives.ByteFPtrVoid;

public class Instruction
{
    public Instruction(
        String _name,
        ByteFPtrVoid _fptrOperation,
        ByteFPtrVoid _fptrAddrMode,
        byte _cycles
    )
    {
        name = _name;
        fptrOperation = _fptrOperation;
        fptrAddrMode = _fptrAddrMode;
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
    public ByteFPtrVoid GetAddrMode()
    {
        return fptrAddrMode;
    }
    public byte GetCycles()
    {
        return cycles;
    }

    private String name;
    private ByteFPtrVoid fptrOperation;
    private ByteFPtrVoid fptrAddrMode;
    private byte cycles;
}

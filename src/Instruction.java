interface ByteFPtrVoid
{
    byte Execute();
}

public class Instruction
{
    private String name;
    private ByteFPtrVoid fptrOperation;
    private ByteFPtrVoid fptrAddrMode;
    private byte cycles;

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
}

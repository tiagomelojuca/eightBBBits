package Core;

import Primitives.ByteFPtrVoid;

public class AddrMode
{
    public AddrMode(AddrModes _addrMode, ByteFPtrVoid _fptr)
    {
        addrMode = _addrMode;
        fptr = _fptr;
    }

    public AddrModes GetMetaType()
    {
        return addrMode;
    }

    public byte Execute()
    {
        return fptr.Execute();
    }

    private AddrModes addrMode;
    private ByteFPtrVoid fptr;
}

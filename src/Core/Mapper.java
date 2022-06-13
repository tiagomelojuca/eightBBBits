package Core;

import Primitives.IntWrapper;

public abstract class Mapper
{
    public Mapper(byte _prgBanks, byte _chrBanks)
    {
        prgBanks = _prgBanks;
        chrBanks = _chrBanks;
    }

    public abstract boolean CpuMapRead(int addr, IntWrapper mapped_addr);
    public abstract boolean CpuMapWrite(int addr, IntWrapper mapped_addr);
    public abstract boolean PpuMapRead(int addr, IntWrapper mapped_addr);
    public abstract boolean PpuMapWrite(int addr, IntWrapper mapped_addr);

    protected byte prgBanks;
    protected byte chrBanks;
}

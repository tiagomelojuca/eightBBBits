package Core;

import Primitives.IntWrapper;

public class Mapper000 extends Mapper
{
    public Mapper000(byte _prgBanks, byte _chrBanks)
    {
        super(_prgBanks, _chrBanks);
    }

    @Override
    public boolean CpuMapRead(int addr, IntWrapper mapped_addr)
    {
        addr &= 0xFFFF;

        if (addr >= 0x8000 && addr <= 0xFFFF)
        {
            mapped_addr.SetValue(addr & (prgBanks > 1 ? 0x7FFF : 0x3FFF));
            return true;
        }

        return false;
    }

    @Override
    public boolean CpuMapWrite(int addr, IntWrapper mapped_addr)
    {
        addr &= 0xFFFF;

        if (addr >= 0x8000 && addr <= 0xFFFF)
        {
            mapped_addr.SetValue(addr & (prgBanks > 1 ? 0x7FFF : 0x3FFF));
            return true;
        }

        return false;
    }

    @Override
    public boolean PpuMapRead(int addr, IntWrapper mapped_addr)
    {
        addr &= 0xFFFF;

        if (addr >= 0x0000 && addr <= 0x1FFF)
        {
            mapped_addr.SetValue(addr);
            return true;
        }

        return false;
    }

    @Override
    public boolean PpuMapWrite(int addr, IntWrapper mapped_addr)
    {
        addr &= 0xFFFF;

        if (addr >= 0x0000 && addr <= 0x1FFF)
        {
            if (chrBanks == 0)
            {
                mapped_addr.SetValue(addr);
                return true;
            }
        }

        return false;
    }
}

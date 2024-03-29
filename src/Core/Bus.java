package Core;

import Primitives.ByteWrapper;public class Bus
{
    public Bus()
    {
        clockCounter = 0;

        final int RAM_SZE = 2 * 1024;
        cpuRam = new byte[RAM_SZE];

        for(int i = 0; i < RAM_SZE; i++)
        {
            cpuRam[i] = 0x00;
        }

        cpu = new Nes6502();
        ppu = new Ppu2C02();
        cpu.ConnectBus(this);
    }

    public void InsertCartridge(Cartridge cartridge)
    {
        rom = cartridge;
        ppu.ConnectCartridge(cartridge);
    }

    public void Reset()
    {
        cpu.Reset();
        clockCounter = 0;
    }

    public void Clock()
    {
        ppu.Clock();
        if (clockCounter % 3 == 0)
        {
            cpu.Clock();
        }

        clockCounter++;
    }

    public void CpuWrite(int addr, byte data)
    {
        addr &= 0xFFFF;
        if (rom.CpuWrite(addr, data))
        {
            // pass
        }
        else if(addr >= 0x0000 && addr <= 0x1FFF)
        {
            cpuRam[addr & 0x07FF] = data;
        }
        else if (addr >= 0x2000 && addr <= 0x3FFF)
        {
            ppu.CpuWrite(addr & 0x0007, data);
        }
    }

    public byte CpuRead(int addr, boolean readOnly)
    {
        byte data = 0x00;
        ByteWrapper wrappedData = new ByteWrapper(data);

        addr &= 0xFFFF;
        if (rom.CpuRead(addr, wrappedData))
        {
            data = wrappedData.GetValue();
        }
        else if(addr >= 0x0000 && addr <= 0x1FFF)
        {
            data = cpuRam[addr & 0x07FF];
        }
        else if (addr >= 0x2000 && addr <= 0x3FFF)
        {
            data = ppu.CpuRead(addr & 0x0007, readOnly);
        }

        return data;
    }

    public byte CpuRead(int addr)
    {
        return CpuRead(addr, false);
    }

    public Nes6502 GetNesCpu()
    {
        return cpu;
    }

    public Ppu2C02 GetNesPpu()
    {
        return ppu;
    }

    private int clockCounter;

    private Nes6502 cpu;
    private Ppu2C02 ppu;
    private byte[] cpuRam;
    private Cartridge rom;
}

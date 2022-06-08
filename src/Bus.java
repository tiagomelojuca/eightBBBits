public class Bus
{
    public Bus()
    {
        final int RAM_SZE = 64 * 1024;
        ram = new byte[RAM_SZE];

        for(int i = 0; i < RAM_SZE; i++)
        {
            ram[i] = 0x00;
        }

        cpu.ConnectBus(this);
    }

    public void WriteByte(int addr, byte data)
    {
        if(addr >= 0x0000 && addr <= 0xFFFF)
        {
            ram[addr] = data;
        }
    }

    public byte ReadByte(int addr, boolean readOnly)
    {
        if(addr >= 0x0000 && addr <= 0xFFFF)
        {
            return ram[addr];
        }

        return 0x00;
    }

    public byte ReadByte(int addr)
    {
        return ReadByte(addr, false);
    }

    private Cpu6502 cpu;
    private byte[] ram;
}

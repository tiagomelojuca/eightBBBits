package Core;

public class Ppu2C02
{
    public Ppu2C02()
    {
        tableName    = new byte[2][1024];
        tablePalette = new byte[32];
    }

    public void ConnectCartridge(Cartridge cartridge)
    {
        rom = cartridge;
    }

    public void Clock()
    {
        //
    }

    byte CpuRead(int addr, boolean readOnly)
    {
        byte data = 0x00;

        switch (addr & 0xFFFF)
        {
            case 0x0000: // Control
                break;
            case 0x0001: // Mask
                break;
            case 0x0002: // Status
                break;
            case 0x0003: // OAM Address
                break;
            case 0x0004: // OAM Data
                break;
            case 0x0005: // Scroll
                break;
            case 0x0006: // PPU Address
                break;
            case 0x0007: // PPU Data
                break;
        }

        return data;
    }
    void CpuWrite(int addr, byte data)
    {
        switch (addr & 0xFFFF)
        {
            case 0x0000: // Control
                break;
            case 0x0001: // Mask
                break;
            case 0x0002: // Status
                break;
            case 0x0003: // OAM Address
                break;
            case 0x0004: // OAM Data
                break;
            case 0x0005: // Scroll
                break;
            case 0x0006: // PPU Address
                break;
            case 0x0007: // PPU Data
                break;
        }
    }

    byte PpuRead(int addr, boolean readOnly)
    {
        byte data = 0x00;
        addr &= 0x3FFF;

        return data;
    }
    void PpuWrite(int addr, byte data)
    {
        addr &= 0x3FFF;
    }

    private Cartridge rom;
    private byte[][] tableName;
    private byte[]   tablePalette;
}

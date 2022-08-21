package Core;

import java.util.Random;

import Primitives.Pixel;
import Primitives.Sprite;

public class Ppu2C02
{
    public Ppu2C02()
    {
        tableName    = new byte[2][1024];
        tablePattern = new byte[2][4096];
        tablePalette = new byte[32];

        palScreen          = new Pixel[0x40];
        sprScreen          = new Sprite(256, 240);
        sprNameTable       = new Sprite[2];
        sprNameTable[0]    = new Sprite(256, 240);
        sprNameTable[1]    = new Sprite(256, 240);
        sprPatternTable    = new Sprite[2];
        sprPatternTable[0] = new Sprite(128, 128);
        sprPatternTable[1] = new Sprite(128, 128);

        scanline = 0;
        cycle    = 0;

        frameComplete = false;

        palScreen[0x00] = new Pixel(84, 84, 84);
        palScreen[0x01] = new Pixel(0, 30, 116);
        palScreen[0x02] = new Pixel(8, 16, 144);
        palScreen[0x03] = new Pixel(48, 0, 136);
        palScreen[0x04] = new Pixel(68, 0, 100);
        palScreen[0x05] = new Pixel(92, 0, 48);
        palScreen[0x06] = new Pixel(84, 4, 0);
        palScreen[0x07] = new Pixel(60, 24, 0);
        palScreen[0x08] = new Pixel(32, 42, 0);
        palScreen[0x09] = new Pixel(8, 58, 0);
        palScreen[0x0A] = new Pixel(0, 64, 0);
        palScreen[0x0B] = new Pixel(0, 60, 0);
        palScreen[0x0C] = new Pixel(0, 50, 60);
        palScreen[0x0D] = new Pixel(0, 0, 0);
        palScreen[0x0E] = new Pixel(0, 0, 0);
        palScreen[0x0F] = new Pixel(0, 0, 0);

        palScreen[0x10] = new Pixel(152, 150, 152);
        palScreen[0x11] = new Pixel(8, 76, 196);
        palScreen[0x12] = new Pixel(48, 50, 236);
        palScreen[0x13] = new Pixel(92, 30, 228);
        palScreen[0x14] = new Pixel(136, 20, 176);
        palScreen[0x15] = new Pixel(160, 20, 100);
        palScreen[0x16] = new Pixel(152, 34, 32);
        palScreen[0x17] = new Pixel(120, 60, 0);
        palScreen[0x18] = new Pixel(84, 90, 0);
        palScreen[0x19] = new Pixel(40, 114, 0);
        palScreen[0x1A] = new Pixel(8, 124, 0);
        palScreen[0x1B] = new Pixel(0, 118, 40);
        palScreen[0x1C] = new Pixel(0, 102, 120);
        palScreen[0x1D] = new Pixel(0, 0, 0);
        palScreen[0x1E] = new Pixel(0, 0, 0);
        palScreen[0x1F] = new Pixel(0, 0, 0);

        palScreen[0x20] = new Pixel(236, 238, 236);
        palScreen[0x21] = new Pixel(76, 154, 236);
        palScreen[0x22] = new Pixel(120, 124, 236);
        palScreen[0x23] = new Pixel(176, 98, 236);
        palScreen[0x24] = new Pixel(228, 84, 236);
        palScreen[0x25] = new Pixel(236, 88, 180);
        palScreen[0x26] = new Pixel(236, 106, 100);
        palScreen[0x27] = new Pixel(212, 136, 32);
        palScreen[0x28] = new Pixel(160, 170, 0);
        palScreen[0x29] = new Pixel(116, 196, 0);
        palScreen[0x2A] = new Pixel(76, 208, 32);
        palScreen[0x2B] = new Pixel(56, 204, 108);
        palScreen[0x2C] = new Pixel(56, 180, 204);
        palScreen[0x2D] = new Pixel(60, 60, 60);
        palScreen[0x2E] = new Pixel(0, 0, 0);
        palScreen[0x2F] = new Pixel(0, 0, 0);

        palScreen[0x30] = new Pixel(236, 238, 236);
        palScreen[0x31] = new Pixel(168, 204, 236);
        palScreen[0x32] = new Pixel(188, 188, 236);
        palScreen[0x33] = new Pixel(212, 178, 236);
        palScreen[0x34] = new Pixel(236, 174, 236);
        palScreen[0x35] = new Pixel(236, 174, 212);
        palScreen[0x36] = new Pixel(236, 180, 176);
        palScreen[0x37] = new Pixel(228, 196, 144);
        palScreen[0x38] = new Pixel(204, 210, 120);
        palScreen[0x39] = new Pixel(180, 222, 120);
        palScreen[0x3A] = new Pixel(168, 226, 144);
        palScreen[0x3B] = new Pixel(152, 226, 180);
        palScreen[0x3C] = new Pixel(160, 214, 228);
        palScreen[0x3D] = new Pixel(160, 162, 160);
        palScreen[0x3E] = new Pixel(0, 0, 0);
        palScreen[0x3F] = new Pixel(0, 0, 0);
    }

    public void ConnectCartridge(Cartridge cartridge)
    {
        rom = cartridge;
    }

    public void Clock()
    {
        Random generator = new Random();
        int posX = (cycle - 1) & 0xFF;
        int posY = scanline & 0xFF;
        if (posY >= 240) { posY = 239; }
        Pixel pixel = palScreen[(generator.nextInt() % 2) != 0 ? 0x3F : 0x30];

        sprScreen.SetPixel(posX, posY, pixel);

        cycle++;
        if (cycle >= 341)
        {
            cycle = 0;
            scanline++;
            if (scanline >= 261)
            {
                scanline = -1;
                frameComplete = true;
            }
        }
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
        if (addr >= 0x0000 && addr <= 0x1FFF)
        {
            data = tablePattern[(addr & 0x1000) >> 12][addr & 0x0FFF];
        }
        else if (addr >= 0x2000 && addr <= 0x3EFF)
        {
            //
        }
        else if (addr >= 0x3F00 && addr <= 0x3FFF)
        {
            addr &= 0x001F;
            if (addr == 0x0010) addr = 0x0000;
            if (addr == 0x0014) addr = 0x0004;
            if (addr == 0x0018) addr = 0x0008;
            if (addr == 0x001C) addr = 0x000C;
            data = tablePalette[addr];
        }

        return data;
    }
    byte PpuRead(int addr)
    {
        return PpuRead(addr, false);
    }
    void PpuWrite(int addr, byte data)
    {
        addr &= 0x3FFF;
        if (addr >= 0x0000 && addr <= 0x1FFF)
        {
            tablePattern[(addr & 0x1000) >> 12][addr & 0x0FFF] = data;
        }
        else if (addr >= 0x2000 && addr <= 0x3EFF)
        {
            //
        }
        else if (addr >= 0x3F00 && addr <= 0x3FFF)
        {
            addr &= 0x001F;
            if (addr == 0x0010) addr = 0x0000;
            if (addr == 0x0014) addr = 0x0004;
            if (addr == 0x0018) addr = 0x0008;
            if (addr == 0x001C) addr = 0x000C;
            tablePalette[addr] = data;
        }
    }

    public Pixel GetColorFromPaletteRAM(byte palette, byte pixel)
    {
        return palScreen[PpuRead(0x3F00 + (palette << 2) + pixel)];
    }

    // Debug
    public Sprite GetScreen()
    {
        return sprScreen;
    }
    public Sprite GetNameTable(byte idx)
    {
        return sprNameTable[idx];
    }
    public Sprite GetPatternTable(byte i, byte palette)
    {
        for (int nTileY = 0; nTileY < 16; nTileY++)
        {
            for (int nTileX = 0; nTileX < 16; nTileX++)
            {
                int offset = nTileY * 256 + nTileX * 16;
                for (int row = 0; row < 8; row++)
                {
                    byte tileLsb = PpuRead(i * 0x1000 + offset + row + 0);
                    byte tileMsb = PpuRead(i * 0x1000 + offset + row + 8);
                    for (int col = 0; col < 8; col++)
                    {
                        byte pixel = (byte) (((byte) (tileLsb & 0x01)) + ((byte) (tileMsb & 0x01)));
                        tileLsb >>= 1; tileMsb >>= 1;

                        sprPatternTable[i].SetPixel(
                            nTileX * 8 + (7 - col),
                            nTileY * 8 + row,
                            GetColorFromPaletteRAM(palette, pixel)
                        );
                    }
                }
            }
        }

        return sprPatternTable[i];
    }

    private Cartridge rom;
    private byte[][] tableName;
    private byte[][] tablePattern;
    private byte[]   tablePalette;

    private Pixel[]  palScreen;
    private Sprite   sprScreen;
    private Sprite[] sprNameTable;
    private Sprite[] sprPatternTable;

    private int scanline;
    private int cycle;

    public boolean frameComplete;
}

package Core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import Defs.ExitCodes;

import Primitives.IntWrapper;
import Primitives.ByteWrapper;

class MirrorMode
{
    public static final int HORIZONTAL    = 0;
    public static final int VERTICAL      = 1;
    public static final int ONE_SCREEN_LO = 2;
    public static final int ONE_SCREEN_HI = 3;
}

public class Cartridge
{
    public Cartridge(String filePath)
    {
        vPRGMem = new Vector<Byte>();
        vCHRMem = new Vector<Byte>();

        isValidImage = false;
        try
        {
            FileInputStream ifs = new FileInputStream(filePath);
            iNesHeader header = new iNesHeader();

            header.name[0]        = (byte) ifs.read();
            header.name[1]        = (byte) ifs.read();
            header.name[2]        = (byte) ifs.read();
            header.name[3]        = (byte) ifs.read();
            header.prg_rom_chunks = (byte) ifs.read();
            header.chr_rom_chunks = (byte) ifs.read();
            header.mapper1        = (byte) ifs.read();
            header.mapper2        = (byte) ifs.read();
            header.prg_ram_size   = (byte) ifs.read();
            header.tv_sys1        = (byte) ifs.read();
            header.tv_sys2        = (byte) ifs.read();
            header.unused[0]      = (byte) ifs.read();
            header.unused[1]      = (byte) ifs.read();
            header.unused[2]      = (byte) ifs.read();
            header.unused[3]      = (byte) ifs.read();
            header.unused[4]      = (byte) ifs.read();

            if ((header.mapper1 & 0x04) != 0)
            {
                ifs.getChannel().position(0);
                ifs.skipNBytes(512);
            }

            mapperId = (byte) ((((header.mapper2 >> 4) << 4) | (header.mapper1 >> 4)));
            mirrorMode = (header.mapper1 & 0x01) != 0 ? MirrorMode.VERTICAL : MirrorMode.HORIZONTAL;
            byte fileType = 1;

            if (fileType == 0)
            {
                // TODO
            }

            if (fileType == 1)
            {
                prgBanks = header.prg_rom_chunks;

                int prgSize = prgBanks * 16384;
                vPRGMem.setSize(prgSize);
                byte[] bArrPrg = new byte[prgSize];
                ifs.read(bArrPrg, 0, prgSize);
                for (int i = 0; i < bArrPrg.length; i++)
                {
                    vPRGMem.set(i, bArrPrg[i]);
                }

                chrBanks = header.chr_rom_chunks;

                int chrSize = chrBanks * 8192;
                vCHRMem.setSize(chrSize);
                byte[] bArrChr = new byte[chrSize];
                ifs.read(bArrChr, 0, chrSize);
                for (int i = 0; i < bArrChr.length; i++)
                {
                    vCHRMem.set(i, bArrChr[i]);
                }
            }

            if (fileType == 2)
            {
                // TODO
            }

            switch (mapperId)
            {
                case 0: mapper = new Mapper000(prgBanks, chrBanks); break;
            }

            ifs.close();
            isValidImage = true;
        }
        catch (IOException e) { System.exit(ExitCodes.ERROR_PARSING_ROM_STREAM); }
    }

    public boolean IsValidImage()
    {
        return isValidImage;
    }

    boolean CpuRead(int addr, ByteWrapper data)
    {
        IntWrapper mapped_addr = new IntWrapper(0);
        if (mapper.CpuMapRead(addr, mapped_addr))
        {
            data.SetValue(vPRGMem.get(mapped_addr.GetValue() & 0xFFFF));
            return true;
        }
        return false;
    }
    boolean CpuWrite(int addr, byte data)
    {
        IntWrapper mapped_addr = new IntWrapper(0);
        if (mapper.CpuMapRead(addr, mapped_addr))
        {
            vPRGMem.set(mapped_addr.GetValue() & 0xFFFF, data);
            return true;
        }
        return false;
    }

    boolean PpuRead(int addr, ByteWrapper data)
    {
        IntWrapper mapped_addr = new IntWrapper(0);
        if (mapper.PpuMapRead(addr, mapped_addr))
        {
            data.SetValue(vCHRMem.get(mapped_addr.GetValue() & 0xFFFF));
            return true;
        }
        return false;
    }
    boolean PpuWrite(int addr, byte data)
    {
        IntWrapper mapped_addr = new IntWrapper(0);
        if (mapper.PpuMapWrite(addr, mapped_addr))
        {
            vCHRMem.set(mapped_addr.GetValue() & 0xFFFF, data);
            return true;
        }
        return false;
    }

    private Vector<Byte> vPRGMem;
    private Vector<Byte> vCHRMem;

    byte mapperId;
    byte prgBanks;
    byte chrBanks;

    private Mapper mapper;

    private int mirrorMode;

    boolean isValidImage;
}

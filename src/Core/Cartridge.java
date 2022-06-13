package Core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import Core.Mapper;
import Core.Mapper000;

import Primitives.ByteWrapper;
import Primitives.IntWrapper;

public class Cartridge
{
    Cartridge(String filePath)
    {
        vPRGMem = new Vector<Byte>();
        vCHRMem = new Vector<Byte>();

        try
        {
            FileInputStream ifs = new FileInputStream(filePath);
            iNesHeader header = new iNesHeader();

            header.name[0]        = (byte) (ifs.read() & 0xFF);
            header.name[1]        = (byte) (ifs.read() & 0xFF);
            header.name[2]        = (byte) (ifs.read() & 0xFF);
            header.name[3]        = (byte) (ifs.read() & 0xFF);
            header.prg_rom_chunks = (byte) (ifs.read() & 0xFF);
            header.chr_rom_chunks = (byte) (ifs.read() & 0xFF);
            header.mapper1        = (byte) (ifs.read() & 0xFF);
            header.mapper2        = (byte) (ifs.read() & 0xFF);
            header.prg_ram_size   = (byte) (ifs.read() & 0xFF);
            header.tv_sys1        = (byte) (ifs.read() & 0xFF);
            header.tv_sys2        = (byte) (ifs.read() & 0xFF);
            header.unused[0]      = (byte) (ifs.read() & 0xFF);
            header.unused[1]      = (byte) (ifs.read() & 0xFF);
            header.unused[2]      = (byte) (ifs.read() & 0xFF);
            header.unused[3]      = (byte) (ifs.read() & 0xFF);
            header.unused[4]      = (byte) (ifs.read() & 0xFF);

            if ((header.mapper1 & 0x04) != 0)
            {
                ifs.getChannel().position(0);
                ifs.skipNBytes(512);
            }

            mapperId = (byte) (((header.mapper2 >> 4) << 4) | (header.mapper1 >> 4));

            byte fileType = 1;

            if (fileType == 0)
            {
                //
            }

            if (fileType == 1)
            {
                prgBanks = header.prg_rom_chunks;
                vPRGMem.setSize(prgBanks * 16384);
                byte[] bArrPrg = new byte[prgBanks * 16384];
                ifs.read(bArrPrg, 0, prgBanks * 16384);

                chrBanks = header.chr_rom_chunks;
                vCHRMem.setSize(chrBanks * 8192);
                byte[] bArrChr = new byte[chrBanks * 8192];
                ifs.read(bArrChr, 0, chrBanks * 8192);
            }

            if (fileType == 2)
            {
                //
            }

            switch (mapperId)
            {
                case 0:
                    mapper = new Mapper000(prgBanks, chrBanks);
                    break;
            }

            ifs.close();
        }
        catch (IOException e) { System.exit(1); }
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

    boolean PpuRead(int addr, boolean readOnly)
    {
        return false;
    }
    boolean PpuWrite(int addr, byte data)
    {
        IntWrapper mapped_addr = new IntWrapper(0);
        if (mapper.CpuMapRead(addr, mapped_addr))
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
}

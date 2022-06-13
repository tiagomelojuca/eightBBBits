package Core;

public class iNesHeader
{
    public byte[] name = new byte[4];
    public byte prg_rom_chunks;
    public byte chr_rom_chunks;
    public byte mapper1;
    public byte mapper2;
    public byte prg_ram_size;
    public byte tv_sys1;
    public byte tv_sys2;
    public byte[] unused = new byte[5];
} // 16 Bytes

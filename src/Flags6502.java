public enum Flags6502
{
    CarryBit((byte) (1 << 0)),
    Zero((byte) (1 << 1)),
    DisableInterrupts((byte) (1 << 2)),
    DecimalMode((byte) (1 << 3)),
    Break((byte) (1 << 4)),
    Unused((byte) (1 << 5)),
    Overflow((byte) (1 << 6)),
    Negative((byte) (1 << 7));

    Flags6502(byte _b)
    {
        b = _b;
    }

    public byte GetByte()
    {
        return b;
    }

    private byte b;
}

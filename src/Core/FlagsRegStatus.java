package Core;

public enum FlagsRegStatus
{
    Unused1((byte) (1 << 0)),
    Unused2((byte) (1 << 1)),
    Unused3((byte) (1 << 2)),
    Unused4((byte) (1 << 3)),
    Unused5((byte) (1 << 4)),
    SpriteOverflow((byte) (1 << 5)),
    SpriteZeroHit((byte) (1 << 6)),
    VerticalBlank((byte) (1 << 7));

    FlagsRegStatus(byte _b)
    {
        b = _b;
    }

    public byte GetByte()
    {
        return b;
    }

    private byte b;
}

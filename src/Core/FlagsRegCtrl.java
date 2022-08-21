package Core;

public enum FlagsRegCtrl
{
    NameTableX((byte) (1 << 0)),
    NameTableY((byte) (1 << 1)),
    IncrementMode((byte) (1 << 2)),
    PatternSprite((byte) (1 << 3)),
    PatternBackground((byte) (1 << 4)),
    SpriteSize((byte) (1 << 5)),
    SlaveMode((byte) (1 << 6)),
    EnableNmi((byte) (1 << 7));

    FlagsRegCtrl(byte _b)
    {
        b = _b;
    }

    public byte GetByte()
    {
        return b;
    }

    private byte b;
}

package Core;

public enum FlagsRegMask
{
    Grayscale((byte) (1 << 0)),
    RenderBackgroundLeft((byte) (1 << 1)),
    RenderSpritesLeft((byte) (1 << 2)),
    RenderBackground((byte) (1 << 3)),
    RenderSprites((byte) (1 << 4)),
    EnhanceRed((byte) (1 << 5)),
    EnahnceGreen((byte) (1 << 6)),
    EnahnceBlue((byte) (1 << 7));

    FlagsRegMask(byte _b)
    {
        b = _b;
    }

    public byte GetByte()
    {
        return b;
    }

    private byte b;
}

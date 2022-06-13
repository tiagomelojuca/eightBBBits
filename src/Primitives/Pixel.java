package Primitives;

public class Pixel
{
    public Pixel()
    {
        r = (byte) 0x00;
        g = (byte) 0x00;
        b = (byte) 0x00;
        a = (byte) 0xFF;
    }

    public Pixel(byte _r, byte _g, byte _b, byte _a)
    {
        r = _r;
        g = _g;
        b = _b;
        a = _a;
    }

    public Pixel(byte _r, byte _g, byte _b)
    {
        this(_r, _g, _b, (byte) 0xFF);
    }

    public Pixel(int _r, int _g, int _b, int _a)
    {
        this((byte) (_r & 0xFF), (byte) (_g & 0xFF), (byte) (_b & 0xFF), (byte) (_a & 0xFF));
    }

    public Pixel(int _r, int _g, int _b)
    {
        this(_r, _g, _b, 0xFF);
    }

    public byte GetRed()
    {
        return r;
    }
    public byte GetGreen()
    {
        return g;
    }
    public byte GetBlue()
    {
        return b;
    }
    public byte GetAlpha()
    {
        return a;
    }

    public void SetRed(byte _r)
    {
        r = _r;
    }
    public void SetGreen(byte _g)
    {
        g = _g;
    }
    public void SetBlue(byte _b)
    {
        b = _b;
    }
    public void SetAlpha(byte _a)
    {
        a = _a;
    }

    private byte r;
    private byte g;
    private byte b;
    private byte a;
}

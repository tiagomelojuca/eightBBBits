package Primitives;

public class ByteWrapper
{
    public ByteWrapper(byte _value)
    {
        value = _value;
    }

    public byte GetValue()
    {
        return value;
    }
    public void SetValue(byte _value)
    {
        value = _value;
    }

    private byte value = 0x00;
}

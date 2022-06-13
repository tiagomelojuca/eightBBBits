package Primitives;

public class IntWrapper
{
    public IntWrapper(int _value)
    {
        value = _value;
    }

    public int GetValue()
    {
        return value;
    }
    public void SetValue(int _value)
    {
        value = _value;
    }

    private int value = 0;
}

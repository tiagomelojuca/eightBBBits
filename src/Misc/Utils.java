package Misc;

public class Utils
{
    public static String Hex(int n, int d)
    {
        String s = "";
        for (int i = 0; i < d; i++) s += "0";

        for (int i = d - 1; i >= 0; i--, n >>= 4)
        {
            StringBuilder _s = new StringBuilder(s);
            _s.setCharAt(i, "0123456789ABCDEF".charAt(n & 0xF));
            s = _s.toString();
        }

        return s;
    }

    public static String Hex(byte b, int d)
    {
        return Hex(b & 0xFF, d);
    }
}

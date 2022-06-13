package GUI;

import java.util.Map;
import java.util.HashMap;

public enum VirtualKeys
{
    VK_W(119),
    VK_A(97),
    VK_S(115),
    VK_D(100),
    VK_O(111),
    VK_P(112),
    VK_ENTER(10),
    VK_BACKSPACE(8);

    VirtualKeys(int _keyCode)
    {
        keyCode = _keyCode;
    }

    public int GetKeyCode()
    {
        return keyCode;
    }

    private int keyCode;

    public static VirtualKeys GetKey(int code)
    {
        return keyCodesEnums.get(code);
    }

    private static Map<Integer, VirtualKeys> keyCodesEnums = new HashMap<>();
    static
    {
        for (VirtualKeys k : VirtualKeys.values())
        {
            keyCodesEnums.put(k.keyCode, k);
        }
    }
}

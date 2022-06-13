package GUI;

import java.util.Map;
import java.util.HashMap;

public class VirtualKeyboard
{
    public VirtualKeyboard()
    {
        keyMap = new HashMap<VirtualKeys, Boolean>();
        keyMap.put(VirtualKeys.VK_W, false);
        keyMap.put(VirtualKeys.VK_A, false);
        keyMap.put(VirtualKeys.VK_S, false);
        keyMap.put(VirtualKeys.VK_D, false);
        keyMap.put(VirtualKeys.VK_O, false);
        keyMap.put(VirtualKeys.VK_P, false);
        keyMap.put(VirtualKeys.VK_ENTER, false);
        keyMap.put(VirtualKeys.VK_BACKSPACE, false);
    }

    public boolean GetKeyPress(VirtualKeys vk)
    {
        return keyMap.get(vk);
    }
    public void SetKeyPress(VirtualKeys vk, boolean state)
    {
        keyMap.put(vk, state);
    }
    public void SetKeyPress(int keyCode, boolean state)
    {
        keyMap.put(VirtualKeys.GetKey(keyCode), state);
    }

    private Map<VirtualKeys, Boolean> keyMap;
}

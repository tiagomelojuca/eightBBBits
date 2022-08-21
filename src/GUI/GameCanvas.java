package GUI;

import java.awt.Color;
import java.awt.Font;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;

import Core.Bus;
import Core.Cartridge;
import Core.Flags6502;
import Core.Nes6502;
import Defs.ExitCodes;
import Misc.Globals;
import Misc.Utils;
import Primitives.Pixel;
import Primitives.Sprite;

public class GameCanvas extends DisplayCanvas
{
    @Override
    protected int DefaultWidth()  { return 640; }
    @Override
    protected int DefaultHeight() { return 480; }

    @Override
    protected void OnCreate()
    {
        rom = new Cartridge(Globals.GetRomPath());
        if (!rom.IsValidImage())
        {
            System.exit(ExitCodes.ERROR_INVALID_ROM);
        }

        bus = new Bus();
        bus.InsertCartridge(rom);
        mapAsm = bus.GetNesCpu().Disassemble(0x0000, 0xFFFF);

        isEmulationRunning = false;
        selectedPalette = 0x00;

        bus.GetNesCpu().Reset();
    }

    @Override
    protected void OnLoop()
    {
        InitScreen();

        //if (isEmulationRunning)
        //{
        //    do { bus.Clock(); } while (!bus.GetNesPpu().frameComplete);
        //    bus.GetNesPpu().frameComplete = false;
        //}
        if (CheckForKey(VirtualKeys.VK_A))
        {
            if (!lockInputKey1)
            {
                do { bus.Clock(); } while (!bus.GetNesCpu().Complete());
                do { bus.Clock(); } while (bus.GetNesCpu().Complete());
            }
            lockInputKey1 = true;
        }
        else
        {
            lockInputKey1 = false;
        }

        if (CheckForKey(VirtualKeys.VK_S))
        {
            if (!lockInputKey2)
            {
                do { bus.Clock(); } while (!bus.GetNesPpu().frameComplete);
                do { bus.Clock(); } while (!bus.GetNesCpu().Complete());
                bus.GetNesPpu().frameComplete = false;
            }
            lockInputKey2 = true;
        }
        else
        {
            lockInputKey2 = false;
        }

        if (CheckForKey(VirtualKeys.VK_O))
        {
            bus.Reset();
        }

        if (CheckForKey(VirtualKeys.VK_P))
        {
            if (!lockInputKey3)
            {
                isEmulationRunning = !isEmulationRunning;
            }
            lockInputKey3 = true;
        }
        else
        {
            lockInputKey3 = false;
        }

        if (CheckForKey(VirtualKeys.VK_BACKSPACE))
        {
            if (!lockInputKey4)
            {
                selectedPalette += 1;
                selectedPalette &= 0x07;
            }
            lockInputKey4 = true;
        }
        else
        {
            lockInputKey4 = false;
        }

        DrawCanvas();
    }

    private void InitScreen()
    {
        ClearScr(new Color(50, 60, 170));
        canvas.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
    }

    public void DrawCanvas()
    {
		DrawCpu(440, 20);
		DrawCode(440, 96, 20);
        DrawSprite(20, 20, bus.GetNesPpu().GetScreen());

        DrawSprite(370, 348, bus.GetNesPpu().GetPatternTable((byte) 0x00, selectedPalette));
        DrawSprite(508, 348, bus.GetNesPpu().GetPatternTable((byte) 0x01, selectedPalette));
    }

    private void DrawCpu(int x, int y)
    {
        Nes6502 nes = bus.GetNesCpu();
        DrawString(x, y, "STATUS:");
		DrawString(x + 64,  y, "N", GetColorFlagState(nes, Flags6502.Negative));
		DrawString(x + 80,  y, "V", GetColorFlagState(nes, Flags6502.Overflow));
		DrawString(x + 96,  y, "-", GetColorFlagState(nes, Flags6502.Unused));
		DrawString(x + 112, y, "B", GetColorFlagState(nes, Flags6502.Break));
		DrawString(x + 128, y, "D", GetColorFlagState(nes, Flags6502.DecimalMode));
		DrawString(x + 144, y, "I", GetColorFlagState(nes, Flags6502.DisableInterrupts));
		DrawString(x + 160, y, "Z", GetColorFlagState(nes, Flags6502.Zero));
		DrawString(x + 178, y, "C", GetColorFlagState(nes, Flags6502.CarryBit));
		DrawString(x, y + 10, "PC: $" +      Utils.Hex(nes.GetRegisterPC(), 4));
		DrawString(x, y + 20, "A: $"  +      Utils.Hex(nes.GetRegisterA(),  2) + "  [" + nes.GetRegisterA() + "]");
		DrawString(x, y + 30, "X: $"  +      Utils.Hex(nes.GetRegisterX(),  2) + "  [" + nes.GetRegisterX() + "]");
		DrawString(x, y + 40, "Y: $"  +      Utils.Hex(nes.GetRegisterY(),  2) + "  [" + nes.GetRegisterY() + "]");
		DrawString(x, y + 50, "Stack P: $" + Utils.Hex(nes.GetRegisterSP(), 4));
    }

    private void DrawCode(int x, int y, int lines)
    {
        List<Integer> keys = new Vector<Integer>();
        Iterator<Map.Entry<Integer, String>> itKeys = mapAsm.entrySet().iterator();
        while(itKeys.hasNext())
        {
            Map.Entry<Integer, String> entry = itKeys.next();
            keys.add(entry.getKey());
        }

        int lineY = (lines >> 1) * 10 + y;

        int searchedKey = bus.GetNesCpu().GetRegisterPC() & 0xFFFF;
        boolean containsKey = false;

        ListIterator<Integer> it = keys.listIterator();
        while (it.hasNext())
        {
            Integer key = it.next();
            if (key == searchedKey)
            {
                containsKey = true;
                break;
            }
        }

        if (containsKey)
        {
            DrawString(x, lineY, mapAsm.get(searchedKey), Color.CYAN);
            while (lineY < (lines * 10) + y) {
                lineY += 10;
                if (it.hasNext())
                {
                    Integer nextKey = it.next();
                    DrawString(x, lineY, mapAsm.get(nextKey));
                }
            }
        }

        lineY = (lines >> 1) * 10 + y;

        it = keys.listIterator();
        containsKey = false;
        while (it.hasNext())
        {
            Integer key = it.next();
            if (key == searchedKey)
            {
                containsKey = true;
                break;
            }
        }

        if (containsKey)
        {
            it.previous();
            while (lineY > y) {
                lineY -= 10;
                if (it.hasPrevious())
                {
                    Integer prevKey = it.previous();
                    DrawString(x, lineY, mapAsm.get(prevKey));
                }
            }
        }
    }

    private void DrawSprite(int x, int y, Sprite spr)
    {
        for(int i = 0; i < spr.GetWidth(); i++)
        {
            for(int j = 0; j < spr.GetHeight(); j++)
            {
                DrawPixel(x + i, y + j, spr.GetPixel(i, j));
            }
        }
    }

    private void DrawPixel(int x, int y, Pixel p)
    {
        java.awt.Color tmp = canvas.getColor();
        canvas.setColor(new Color(p.GetRed() & 0xFF, p.GetGreen() & 0xFF, p.GetBlue() & 0xFF));
        canvas.drawRect(x, y, 0, 0);
        canvas.setColor(tmp);
    }

    // Helpers
    private java.awt.Color GetColorFlagState(Nes6502 nes, Flags6502 f)
    {
        return (nes.GetRegisterF() & f.GetByte()) != 0 ? Color.GREEN : Color.RED;
    }

    private Bus bus;
    private Cartridge rom;
    private Map<Integer, String> mapAsm;

    private boolean isEmulationRunning;
    private byte selectedPalette;

    private boolean lockInputKey1;
    private boolean lockInputKey2;
    private boolean lockInputKey3;
    private boolean lockInputKey4;
}

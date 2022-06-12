package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Map;
import javax.swing.JPanel;

import Core.Bus;
import Core.Flags6502;
import Core.Nes6502;
import Defs.NesDisplay;
import Misc.Utils;

class CanvasProps
{
    public static final int WIDTH =  640;
    public static final int HEIGHT = 480;

    public static final Color DEFAULT_BG_COLOR = Color.BLUE;
    public static final Color DEFAULT_FG_COLOR = Color.WHITE;

    public static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 10);
}

public class GameCanvas extends JPanel
{
    private void OnCreate()
    {
        bus = new Bus();
        LoadROM("A2 0A 8E 00 00 A2 03 8E 01 00 AC 00 00 A9 00 18 6D 01 00 88 D0 FA 8D 02 00 EA EA EA");
    }

    private void OnLoop()
    {
        ClearScr(CanvasProps.DEFAULT_BG_COLOR);
        canvas.setFont(CanvasProps.DEFAULT_FONT);

        int offsetX = 10;
        int offsetY = 20;

        DrawRam(offsetX + 2, offsetY +   2, 0x0000, 16, 16);
		DrawRam(offsetX + 2, offsetY + 182, 0x8000, 16, 16);
		DrawCpu(offsetX + 428, offsetY + 2);
		DrawCode(offsetX + 428, offsetY + 72, 26);

        DrawString(offsetX, 370, "SPACE = Step Instruction    R = RESET    I = IRQ    N = NMI");
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(CanvasProps.WIDTH, CanvasProps.HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        canvas = g;
        super.paintComponent(canvas);

        if (!isInitialized)
        {
            OnCreate();
            isInitialized = true;
        }

        OnLoop();
        repaint();
    }

    private void LoadROM(String rom)
    {
        int offset = 0x8000;

        while (rom.length() > 0) {
            String bStr = rom.substring(0, 2);
            rom = rom.substring(rom.length() > 2 ? 3 : 2);

            byte b = (byte) Integer.parseInt(bStr, 16);
            bus.WriteByte(offset, b);
            offset++;

            bus.WriteByte(0xFFFC, (byte) 0x00);
            bus.WriteByte(0xFFFD, (byte) 0x00);

            mapAsm = bus.GetNesCpu().Disassemble(0x0000, 0xFFFF);

            bus.GetNesCpu().Reset();
        }
    }

    private void DrawString(int x, int y, String str, java.awt.Color color)
    {
        java.awt.Color old = canvas.getColor();
        canvas.setColor(color);
        canvas.drawString(str, x, y);
        canvas.setColor(old);
    }
    private void DrawString(int x, int y, String str)
    {
        DrawString(x, y, str, CanvasProps.DEFAULT_FG_COLOR);
    }

    private void DrawRam(int x, int y, int addr, int rows, int cols)
    {
        int nRamX = x;
        int nRamY = y;

        for (int row = 0; row < rows; row++)
        {
            String strOffset = "$" + Utils.Hex(addr, 4) + ":";
            for (int col = 0; col < cols; col++)
            {
                strOffset += " " + Utils.Hex(bus.ReadByte(addr, true), 2);
                addr += 1;
            }
            DrawString(nRamX, nRamY, strOffset);
            nRamY += 10;
        }
    }

    private java.awt.Color GetColorFlagState(Nes6502 nes, Flags6502 f)
    {
        return (nes.GetRegisterF() & f.GetByte()) != 0 ? Color.GREEN : Color.RED;
    }

    private void DrawCpu(int x, int y)
    {
        Nes6502 nes = bus.GetNesCpu();
        DrawString(x, y, "STATUS:");
		DrawString(x  + 64,  y, "N", GetColorFlagState(nes, Flags6502.Negative));
		DrawString(x  + 80,  y, "V", GetColorFlagState(nes, Flags6502.Overflow));
		DrawString(x  + 96,  y, "-", GetColorFlagState(nes, Flags6502.Unused));
		DrawString(x  + 112, y, "B", GetColorFlagState(nes, Flags6502.Break));
		DrawString(x  + 128, y, "D", GetColorFlagState(nes, Flags6502.DecimalMode));
		DrawString(x  + 144, y, "I", GetColorFlagState(nes, Flags6502.DisableInterrupts));
		DrawString(x  + 160, y, "Z", GetColorFlagState(nes, Flags6502.Zero));
		DrawString(x  + 178, y, "C", GetColorFlagState(nes, Flags6502.CarryBit));
		DrawString(x, y + 10, "PC: $" +      Utils.Hex(nes.GetRegisterPC(), 4));
		DrawString(x, y + 20, "A: $"  +      Utils.Hex(nes.GetRegisterA(),  2) + "  [" + nes.GetRegisterA() + "]");
		DrawString(x, y + 30, "X: $"  +      Utils.Hex(nes.GetRegisterX(),  2) + "  [" + nes.GetRegisterX() + "]");
		DrawString(x, y + 40, "Y: $"  +      Utils.Hex(nes.GetRegisterY(),  2) + "  [" + nes.GetRegisterY() + "]");
		DrawString(x, y + 50, "Stack P: $" + Utils.Hex(nes.GetRegisterSP(), 4));
    }

    private void DrawCode(int x, int y, int lines)
    {
        //
    }

    private void ClearScr(java.awt.Color color)
    {
        java.awt.Color old = canvas.getColor();
        canvas.setColor(color);
        canvas.fillRect(0, 0, CanvasProps.WIDTH, CanvasProps.HEIGHT);
        canvas.setColor(old);
    }

    private Graphics canvas;
    private boolean isInitialized;

    private Bus bus;
    private Map<Integer, String> mapAsm;
}

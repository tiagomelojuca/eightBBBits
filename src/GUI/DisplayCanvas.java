package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public abstract class DisplayCanvas extends JPanel
{
    protected abstract int DefaultWidth();
    protected abstract int DefaultHeight();

    protected abstract void OnCreate();
    protected abstract void OnLoop();

    public void Attach(VirtualKeyboard _vkb)
    {
        vkb = _vkb;
    }

    // Drawing Primitives
    protected void ClearScr(java.awt.Color color)
    {
        java.awt.Color old = canvas.getColor();
        canvas.setColor(color);
        canvas.fillRect(0, 0, DefaultWidth(), DefaultHeight());
        canvas.setColor(old);
    }
    protected void DrawString(int x, int y, String str, java.awt.Color color)
    {
        java.awt.Color old = canvas.getColor();
        canvas.setColor(color);
        canvas.drawString(str, x, y);
        canvas.setColor(old);
    }
    protected void DrawString(int x, int y, String str)
    {
        DrawString(x, y, str, Color.WHITE);
    }

    // Helpers
    protected boolean CheckForKey(VirtualKeys key)
    {
        return vkb.GetKeyPress(key);
    }

    // Overriding JPanel Behavior
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(DefaultWidth(), DefaultHeight());
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

    protected Graphics canvas;
    private VirtualKeyboard vkb;
    private boolean isInitialized;
}

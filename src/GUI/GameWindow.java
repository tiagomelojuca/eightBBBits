package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

class NesDisplayResolution
{
    public static final int WIDTH  = 256;
    public static final int HEIGHT = 240;
}

class GameCanvas extends JPanel
{
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(NesDisplayResolution.WIDTH, NesDisplayResolution.HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, NesDisplayResolution.WIDTH, NesDisplayResolution.HEIGHT);
    }
}

public class GameWindow
{
    public GameWindow() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try
                {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
                catch
                (
                    ClassNotFoundException |
                    InstantiationException |
                    IllegalAccessException |
                    UnsupportedLookAndFeelException e
                ) {}

                JFrame frame = new JFrame("eightBBBits");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new GameCanvas());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}

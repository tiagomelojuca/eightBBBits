package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

class KeyChecker extends KeyAdapter
{
    public KeyChecker(VirtualKeyboard _vkb)
    {
        super();
        vkb = _vkb;
    }

    @Override
    public void keyPressed(KeyEvent event)
    {
        vkb.SetKeyPress(event.getKeyChar(), true);
    }

    @Override
    public void keyReleased(KeyEvent event)
    {
        vkb.SetKeyPress(event.getKeyChar(), false);
    }

    private VirtualKeyboard vkb;
}

public class GameWindow
{
    public GameWindow() {
        VirtualKeyboard vkb = new VirtualKeyboard();

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

                GameCanvas canvas = new GameCanvas(vkb);

                JFrame frame = new JFrame("eightBBBits");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(canvas);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                frame.addKeyListener(new KeyChecker(vkb));
            }
        });
    }
}

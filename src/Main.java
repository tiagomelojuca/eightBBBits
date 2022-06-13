import GUI.GameWindow;
import GUI.DisplayCanvas;
import GUI.DisassemblerCanvas;

public class Main
{
    public static void main(String[] args)
    {
        DisplayCanvas canvas = new DisassemblerCanvas();
        GameWindow ui = new GameWindow(canvas);
        ui.Start();
    }
}

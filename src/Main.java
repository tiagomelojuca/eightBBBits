import Misc.Globals;

import GUI.CanvasFactory;
import GUI.DisplayCanvas;
import GUI.GameWindow;

public class Main
{
    public static void main(String[] args)
    {
        Globals.SetRomPath("C:\\Users\\tiago\\Projetos\\eightBBBits\\tests\\nestest.nes");
        DisplayCanvas canvas = CanvasFactory.CreateGameCanvas();
        GameWindow ui = new GameWindow(canvas);
        ui.Start();
    }
}

import GUI.GameWindow;
import GUI.DisplayCanvas;
import GUI.GameCanvas;

public class Main
{
    public static void main(String[] args)
    {
        DisplayCanvas canvas = new GameCanvas();
        GameWindow ui = new GameWindow(canvas);
        ui.Start();
    }
}

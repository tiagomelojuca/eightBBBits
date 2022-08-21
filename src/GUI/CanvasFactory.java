package GUI;

public class CanvasFactory
{
    public static DisplayCanvas CreateDisassemblerCanvas() { return new DisassemblerCanvas(); }
    public static DisplayCanvas CreateGameCanvas()         { return new GameCanvas(); }
}

package Primitives;

public class Sprite
{
    public Sprite(int width, int height)
    {
        w = width;
        h = height;
        pixels = new Pixel[w][h];

        for (int i = 0; i < w; i++)
        {
            for (int j = 0; j < h; j++)
            {
                pixels[i][j] = new Pixel();
            }
        }
    }

    public int GetWidth()
    {
        return w;
    }
    public int GetHeight()
    {
        return h;
    }

    public Pixel GetPixel(int x, int y)
    {
        return pixels[x][y];
    }
    public void SetPixel(int x, int y, Pixel p)
    {
        pixels[x][y] = p;
    }

    private int w;
    private int h;
    private Pixel[][] pixels;
}

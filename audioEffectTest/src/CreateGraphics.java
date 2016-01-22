import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.*;

public class CreateGraphics
{
    private static int scale = 1;

    public static int getScale()
    {
        return scale;
    }

    public static WritableImage generateWaveForm(double[] waveData, double offset, boolean showOffset)
    {
        long size = waveData.length;
        int incrementer = 1;
        scale = 1;
        while(size > Toolkit.getDefaultToolkit().getScreenSize().getWidth())
        {
            size = size / 2;
            incrementer = incrementer * 2;
            scale = scale * 2;
        }
        WritableImage wi = new WritableImage((int) size, 200);
        PixelWriter pi = wi.getPixelWriter();
        int counter = 0;
        for(int i = 0; counter < size; i = i + incrementer)
        {
            if(waveData[i] > 0)
            {
                for(int y = 0; y < waveData[i] * 100; y++)
                {
                    pi.setColor(counter, y + 100, Color.color(1, (double) y / 100, 0));
                }
            }
            if(waveData[i] <= 0)
            {
                pi.setColor(counter, 100, Color.color(1, 0, 0));
                for(int y = 0; y > waveData[i] * 100; y--)
                {
                    pi.setColor(counter, y + 100, Color.color(1, (double) -y / 100, 0));
                }
            }

            counter++;
        }
        if(showOffset == true)
        {
            for(int i = 0; i < wi.getWidth(); i++)
            {
                pi.setColor(i, 199, Color.color(0, 1, 0));
                pi.setColor(i, limit((int) (199 - offset * 100)), Color.color(0, 1, 0));
                pi.setColor(i, 0, Color.color(0, 0, 1));
                pi.setColor(i, limit((int) (offset * 100)), Color.color(0, 0, 1));
            }
        }
        return wi;
    }

    public static WritableImage createLogo()
    {
        double cDist = 0;
        double alpha = 0;
        WritableImage wi = new WritableImage(360, 210);
        PixelWriter pi = wi.getPixelWriter();
        for(int x = 0; x < 360; x++)
        {
            for(int y = 0; y < 210; y++)
            {
                double invert = -y * 0.65 + 210;
                cDist = Math.sqrt((x - 175)*(x - 175) + ((Math.sin(y * (((double) y / 420))) * ((double)y / 100)) + 0.008 * invert * invert * 1.5 - 250)*((Math.sin(y * (((double) y / 420))) * ((double)y / 100)) + 0.008 * invert * invert  * 1.5 - 250));
                alpha = Math.sin((cDist * 0.3) + (-0.01 * cDist + 2)) * (-0.01 * cDist + 1);
                if(alpha < 0)
                {
                    alpha = 0;
                }
                if(alpha > 1)
                {
                    alpha = 1;
                }
                pi.setColor(x, y, Color.color(Math.sin(cDist * 0.5) * ((double) y / 250) * 0.25 + 0.5, Math.cos(cDist * 0.3) * 0.5 + 0.5, 0.5 + (double) x / 720,  alpha * ((double) y / 250)));
            }
        }
        return wi;
    }

    public static WritableImage createGradient(int sizeX, int sizeY)
    {
        WritableImage wi = new WritableImage(sizeX, sizeY);
        PixelWriter pi = wi.getPixelWriter();
        for(int x = 0; x < sizeX; x++)
        {
            for(int y= 0; y < sizeY; y++)
            {
                pi.setColor(x, y, Color.color(0, 0, ((double)y / sizeY), ((double) y / sizeY) * 0.25));
            }
        }
        return wi;
    }
    public static int limit(int i)
    {
        if(i < 0)
        {
            return 0;
        }
        if(i > 199)
        {
            return 199;
        }
        return i;
    }
}

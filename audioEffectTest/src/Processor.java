import javafx.scene.image.WritableImage;

import java.io.File;
import java.util.ArrayList;

public class Processor
{
    static double[] sound = null;
    static double[] originalSound = null;
    static WritableImage waveForm = null;

    public static WritableImage loadFile(String filePath, double offset, boolean showOffset)
    {
        File file = new File(System.getProperty("user.dir") +"/src/"+filePath);
        originalSound = AudioLoader.loadAudioFile(file);
        sound = new double[originalSound.length];
        for(int i = 0; i < originalSound.length; i++)
        {
            sound[i] = originalSound[i];
        }
        if(originalSound != null)
        {
            waveForm = CreateGraphics.generateWaveForm(originalSound, offset, showOffset);
        }
        return waveForm;
    }

    public static WritableImage processWave(double multiplier, double offset, boolean bounce, boolean showOffset)
    {
        sound = new double[originalSound.length];
        for(int i = 0; i < originalSound.length; i++)
        {
            sound[i] = originalSound[i] * multiplier;
            if(offset > 0)
            {
                if(bounce == false)
                {
                    if (sound[i] > 1)
                    {
                        double temp = sound[i] - 1;
                        int count = (int) Math.floor(temp / offset);
                        sound[i] = sound[i] - offset * (count + 1);
                    }
                    if (sound[i] < -1)
                    {
                        double tempVal = -sound[i];
                        double temp = tempVal - 1;
                        int count = (int) Math.floor(temp / offset);
                        sound[i] = sound[i] + offset * (count + 1);
                    }
                }
                if(bounce == true)
                {
                    if (sound[i] > 1)
                    {
                        double temp = sound[i] - 1;
                        int count = (int) Math.floor(temp / offset);
                        temp = sound[i] - offset * (count + 1);
                        if(count % 2 != 0)
                        {
                            sound[i] = temp;
                        }
                        else
                        {
                            sound[i] = 2 - temp - offset;
                        }
                    }
                    if (sound[i] < -1)
                    {
                        double tempVal = -sound[i];
                        double temp = tempVal - 1;
                        int count = (int) Math.floor(temp / offset);
                        temp = sound[i] + offset * (count + 1);
                        if(count % 2 != 0)
                        {
                            sound[i] = temp;
                        }
                        else
                        {
                            sound[i] = - 2 - temp + offset;
                        }
                    }
                }
            }
            if(offset <= 0)
            {
                if(sound[i] > 1)
                {
                    sound[i] = 1;
                }
                if(sound[i] < -1)
                {
                    sound[i] = -1;
                }
            }
        }
        waveForm = CreateGraphics.generateWaveForm(sound, offset, showOffset);
        return waveForm;
    }
}

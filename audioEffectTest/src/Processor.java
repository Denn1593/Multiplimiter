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
        File file = new File(System.getProperty("user.dir") +"/" +filePath);
        originalSound = AudioLoader.loadAudioFile(file);
        sound = new double[originalSound.length];
        for(int i = 0; i < originalSound.length; i++)
        {
            sound[i] = originalSound[i];
        }
        waveForm = CreateGraphics.generateWaveForm(originalSound, offset, showOffset);
        return waveForm;
    }

    public static WritableImage processWave(double multiplier, double offset, boolean bounce, boolean showOffset, int skip)
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
            int crush = (int) Math.abs(((double) skip));

            if(crush > 0 && skip > 0)
            {
                for (int j = 0; j < crush && i + j < originalSound.length; j++)
                {
                    sound[i + j] = sound[i];
                }
                i = i + crush - 1;
            }
        }
        waveForm = CreateGraphics.generateWaveForm(sound, offset, showOffset);
        return waveForm;
    }

    public static ArrayList<String> sortSuggestions(ArrayList<String> a, String name)
    {
        ArrayList<Integer> remove = new ArrayList<>();
        for(int i = 0; i < a.size(); i++)
        {
            if(a.get(i).length() > 3)
            {
                String s = "";
                int g = a.get(i).length() - 4;
                for (int j = 0; j < 4; j++)
                {
                    s = s + a.get(i).charAt(g);
                    g++;
                }
                if (!s.equals(".wav"))
                {
                    remove.add(i);
                }
                else
                {
                    boolean found = false;
                    for(int j = 0; j < a.get(i).length() && found == false; j++)
                    {
                        if(a.get(i).charAt(j) == name.charAt(0))
                        {
                            String s2 = "";
                            for(int h = 0; h < name.length(); h++)
                            {
                                if(a.get(i).length() > j+h)
                                {
                                    s2 = s2 + a.get(i).charAt(j + h);
                                }
                            }
                            if(s2.equals(name))
                            {
                                found = true;
                            }
                        }
                    }
                    if(found == false)
                    {
                        remove.add(i);
                    }
                }
            }
            else
            {
                remove.add(i);
            }
        }
        for(int j = 0; j < remove.size(); j++)
        {
            a.remove(remove.get(j) - j);
        }
        for(int i = 0; i < a.size(); i++)
        {
            if(i < 9)
            {
                a.set(i, "(" + (i + 1) + ") " + a.get(i));
            }
            else
            {
                a.set(i, "    " + a.get(i));
            }
        }
        return a;
    }
}

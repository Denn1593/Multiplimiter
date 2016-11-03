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
        waveForm = CreateGraphics.generateWaveForm(originalSound, offset, showOffset, "reset");
        return waveForm;
    }

    public static WritableImage processWave(double multiplier, double offset, String mode, boolean showOffset)
    {
        sound = new double[originalSound.length];
        if(mode.equals("reset") || mode.equals("bounce"))
        {
            multiplier = multiplier * 99 + 1;
            offset = offset * 2;
        }
        for(int i = 0; i < originalSound.length; i++)
        {
            if(offset > 0)
            {
                if(mode.equals("reset"))
                {
                    sound[i] = originalSound[i] * multiplier;
                    System.out.println(offset);
                    System.out.println(multiplier);
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
                if(mode.equals("bounce"))
                {
                    sound[i] = originalSound[i] * multiplier;
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
            if(mode.equals("stretch"))
            {
                sound[i] = originalSound[i];
                if(sound[i] < Math.sin(sound[i] * 100 * multiplier) * offset || sound[i] > -Math.sin(sound[i] * 100 * multiplier) * offset)
                {
                    sound[i] = -sound[i];
                }
                else
                {

                }
                if(sound[i] > 1)
                {
                    sound[i] = 1;
                }
                if(sound[i] < -1)
                {
                    sound[i] = -1;
                }
            }
            if(mode.equals("stretch x2"))
            {
                sound[i] = originalSound[i];
                sound[i] = sound[i] * multiplier * 100;



                for(int j = 0; j < 100; j++)
                {
                    if (sound[i] < -offset)
                    {
                        sound[i] = -sound[i] - 1;
                    }

                    if (sound[i] > offset)
                    {
                        sound[i] = -sound[i] + 1;
                    }
                }


                if(sound[i] > 1)
                {
                    sound[i] = 1;
                }
                if(sound[i] < -1)
                {
                    sound[i] = -1;
                }


                /* deprecated
                sound[i] = originalSound[i];
                if (Math.abs(sound[i]) < Math.abs(multiplier))
                {
                    double temp = sound[i] / multiplier;
                    sound[i] = temp * offset;
                }
                else
                {
                    if(sound[i] > 0)
                    {
                        double temp = sound[i] - multiplier;
                        temp = (temp / (1 - multiplier)) * (1 - offset);
                        sound[i] = temp + offset;
                    }
                    if (sound[i] < 0)
                    {
                        double temp = - sound[i] - multiplier;
                        temp = (temp / (1 - multiplier)) * (1 - offset);
                        sound[i] = -(temp + offset);
                    }
                }*/
            }
            if(offset <= 0 && !mode.equals("stretch") && !mode.equals("stretch x2"))
            {
                sound[i] = originalSound[i] * multiplier;
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
        waveForm = CreateGraphics.generateWaveForm(sound, offset, showOffset, mode);
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

import javax.sound.sampled.*;
import java.io.*;

public class AudioLoader
{

    public static double[] loadAudioFile(File file)
    {
        byte[] data = readByte(file);
        int dataLength = data.length;
        double[] result = new double[dataLength / 4];

        for(int i = 0; i < dataLength / 4; i++)
        {
            result[i] = ((short) (((data[4*i+1] & 0xFF) << 8) | (data[4*i] & 0xFF))) / ((double) Short.MAX_VALUE);
        }
        return result;
    }

    public static byte[] readByte(File file)
    {
        byte[] data = null;
        AudioInputStream audioStream = null;
        try
        {
            if(file.exists())
            {
                audioStream = AudioSystem.getAudioInputStream(file);
                data = new byte[audioStream.available()];
                audioStream.read(data);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedAudioFileException e)
        {
            e.printStackTrace();
        }
        return data;
    }

    public static void saveAudioFile(double[] sound, String name) throws IOException
    {
        AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
        byte[] data = new byte[sound.length * 2];
        for(int i = 0; i < sound.length; i++)
        {
            int temp = (short) (sound[i] * Short.MAX_VALUE);
            data[2*i + 0] = (byte) temp;
            data[2*i + 1] = (byte) (temp >> 8);
        }
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        AudioInputStream audioStream = new AudioInputStream(byteStream, format, sound.length);
        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, new File(name+".wav"));
    }

    public static void playAudioFile(double[] sound) throws LineUnavailableException, IOException
    {
        AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
        byte[] data = new byte[sound.length * 2];
        for(int i = 0; i < sound.length; i++)
        {
            int temp = (short) (sound[i] * Short.MAX_VALUE);
            data[2*i + 0] = (byte) temp;
            data[2*i + 1] = (byte) (temp >> 8);
        }
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        AudioInputStream audioStream = new AudioInputStream(byteStream, format, sound.length);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }
}

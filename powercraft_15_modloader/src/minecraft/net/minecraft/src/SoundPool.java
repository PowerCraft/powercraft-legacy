package net.minecraft.src;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SoundPool
{
    /** The RNG used by SoundPool. */
    private Random rand = new Random();

    /**
     * Maps a name (can be sound/newsound/streaming/music/newmusic) to a list of SoundPoolEntry's.
     */
    private Map nameToSoundPoolEntriesMapping = new HashMap();

    /** A list of all SoundPoolEntries that have been loaded. */
    private List allSoundPoolEntries = new ArrayList();

    /**
     * The number of soundPoolEntry's. This value is computed but never used (should be equal to
     * allSoundPoolEntries.size()).
     */
    public int numberOfSoundPoolEntries = 0;
    public boolean isGetRandomSound = true;

    /**
     * Adds a sound to this sound pool.
     */
    public SoundPoolEntry addSound(String par1Str, File par2File)
    {
        try
        {
            return this.addSound(par1Str, par2File.toURI().toURL());
        }
        catch (MalformedURLException var4)
        {
            var4.printStackTrace();
            throw new RuntimeException(var4);
        }
    }

    public SoundPoolEntry addSound(String var1, URL var2)
    {
        String var3 = var1;
        var1 = var1.substring(0, var1.indexOf("."));

        if (this.isGetRandomSound)
        {
            while (Character.isDigit(var1.charAt(var1.length() - 1)))
            {
                var1 = var1.substring(0, var1.length() - 1);
            }
        }

        var1 = var1.replaceAll("/", ".");

        if (!this.nameToSoundPoolEntriesMapping.containsKey(var1))
        {
            this.nameToSoundPoolEntriesMapping.put(var1, new ArrayList());
        }

        SoundPoolEntry var4 = new SoundPoolEntry(var3, var2);
        ((List)this.nameToSoundPoolEntriesMapping.get(var1)).add(var4);
        this.allSoundPoolEntries.add(var4);
        ++this.numberOfSoundPoolEntries;
        return var4;
    }

    /**
     * gets a random sound from the specified (by name, can be sound/newsound/streaming/music/newmusic) sound pool.
     */
    public SoundPoolEntry getRandomSoundFromSoundPool(String par1Str)
    {
        List var2 = (List)this.nameToSoundPoolEntriesMapping.get(par1Str);
        return var2 == null ? null : (SoundPoolEntry)var2.get(this.rand.nextInt(var2.size()));
    }

    /**
     * Gets a random SoundPoolEntry.
     */
    public SoundPoolEntry getRandomSound()
    {
        return this.allSoundPoolEntries.isEmpty() ? null : (SoundPoolEntry)this.allSoundPoolEntries.get(this.rand.nextInt(this.allSoundPoolEntries.size()));
    }
}

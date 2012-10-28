package net.minecraft.src;

import net.minecraftforge.client.*;
import net.minecraftforge.client.event.sound.*;
import net.minecraftforge.common.MinecraftForge;
import static net.minecraftforge.client.event.sound.SoundEvent.*;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

@SideOnly(Side.CLIENT)
public class SoundManager
{
    /** A reference to the sound system. */
    public static SoundSystem sndSystem;

    /** Sound pool containing sounds. */
    public SoundPool soundPoolSounds = new SoundPool();

    /** Sound pool containing streaming audio. */
    public SoundPool soundPoolStreaming = new SoundPool();

    /** Sound pool containing music. */
    public SoundPool soundPoolMusic = new SoundPool();

    /**
     * The last ID used when a sound is played, passed into SoundSystem to give active sounds a unique ID
     */
    private int latestSoundID = 0;

    /** A reference to the game settings. */
    private GameSettings options;
    private Set field_82470_g = new HashSet();

    /** Set to true when the SoundManager has been initialised. */
    private static boolean loaded = false;

    /** RNG. */
    private Random rand = new Random();
    private int ticksBeforeMusic;

    public static int MUSIC_INTERVAL = 12000;

    public SoundManager()
    {
        this.ticksBeforeMusic = this.rand.nextInt(MUSIC_INTERVAL);
    }

    /**
     * Used for loading sound settings from GameSettings
     */
    public void loadSoundSettings(GameSettings par1GameSettings)
    {
        this.soundPoolStreaming.isGetRandomSound = false;
        this.options = par1GameSettings;

        if (!loaded && (par1GameSettings == null || par1GameSettings.soundVolume != 0.0F || par1GameSettings.musicVolume != 0.0F))
        {
            this.tryToSetLibraryAndCodecs();
        }
        ModCompatibilityClient.audioModLoad(this);
        MinecraftForge.EVENT_BUS.post(new SoundLoadEvent(this));
    }

    /**
     * Tries to add the paulscode library and the relevant codecs. If it fails, the volumes (sound and music) will be
     * set to zero in the options file.
     */
    private void tryToSetLibraryAndCodecs()
    {
        try
        {
            float var1 = this.options.soundVolume;
            float var2 = this.options.musicVolume;
            this.options.soundVolume = 0.0F;
            this.options.musicVolume = 0.0F;
            this.options.saveOptions();
            SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
            SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
            SoundSystemConfig.setCodec("mus", CodecMus.class);
            SoundSystemConfig.setCodec("wav", CodecWav.class);
            ModCompatibilityClient.audioModAddCodecs();
            MinecraftForge.EVENT_BUS.post(new SoundSetupEvent(this));
            sndSystem = new SoundSystem();
            this.options.soundVolume = var1;
            this.options.musicVolume = var2;
            this.options.saveOptions();
        }
        catch (Throwable var3)
        {
            var3.printStackTrace();
            System.err.println("error linking with the LibraryJavaSound plug-in");
        }

        loaded = true;
    }

    /**
     * Called when one of the sound level options has changed.
     */
    public void onSoundOptionsChanged()
    {
        if (!loaded && (this.options.soundVolume != 0.0F || this.options.musicVolume != 0.0F))
        {
            this.tryToSetLibraryAndCodecs();
        }

        if (loaded)
        {
            if (this.options.musicVolume == 0.0F)
            {
                sndSystem.stop("BgMusic");
            }
            else
            {
                sndSystem.setVolume("BgMusic", this.options.musicVolume);
            }
        }
    }

    /**
     * Called when Minecraft is closing down.
     */
    public void closeMinecraft()
    {
        if (loaded)
        {
            sndSystem.cleanup();
        }
    }

    /**
     * Adds a sounds with the name from the file. Args: name, file
     */
    public void addSound(String par1Str, File par2File)
    {
        this.soundPoolSounds.addSound(par1Str, par2File);
    }

    /**
     * Adds an audio file to the streaming SoundPool.
     */
    public void addStreaming(String par1Str, File par2File)
    {
        this.soundPoolStreaming.addSound(par1Str, par2File);
    }

    /**
     * Adds an audio file to the music SoundPool.
     */
    public void addMusic(String par1Str, File par2File)
    {
        this.soundPoolMusic.addSound(par1Str, par2File);
    }

    /**
     * If its time to play new music it starts it up.
     */
    public void playRandomMusicIfReady()
    {
        if (loaded && this.options.musicVolume != 0.0F)
        {
            if (!sndSystem.playing("BgMusic") && !sndSystem.playing("streaming"))
            {
                if (this.ticksBeforeMusic > 0)
                {
                    --this.ticksBeforeMusic;
                    return;
                }

                SoundPoolEntry var1 = this.soundPoolMusic.getRandomSound();
                var1 = ModCompatibilityClient.audioModPickBackgroundMusic(this, var1);
                var1 = SoundEvent.getResult(new PlayBackgroundMusicEvent(this, var1));

                if (var1 != null)
                {
                    this.ticksBeforeMusic = this.rand.nextInt(MUSIC_INTERVAL) + MUSIC_INTERVAL;
                    sndSystem.backgroundMusic("BgMusic", var1.soundUrl, var1.soundName, false);
                    sndSystem.setVolume("BgMusic", this.options.musicVolume);
                    sndSystem.play("BgMusic");
                }
            }
        }
    }

    /**
     * Sets the listener of sounds
     */
    public void setListener(EntityLiving par1EntityLiving, float par2)
    {
        if (loaded && this.options.soundVolume != 0.0F)
        {
            if (par1EntityLiving != null)
            {
                float var3 = par1EntityLiving.prevRotationYaw + (par1EntityLiving.rotationYaw - par1EntityLiving.prevRotationYaw) * par2;
                double var4 = par1EntityLiving.prevPosX + (par1EntityLiving.posX - par1EntityLiving.prevPosX) * (double)par2;
                double var6 = par1EntityLiving.prevPosY + (par1EntityLiving.posY - par1EntityLiving.prevPosY) * (double)par2;
                double var8 = par1EntityLiving.prevPosZ + (par1EntityLiving.posZ - par1EntityLiving.prevPosZ) * (double)par2;
                float var10 = MathHelper.cos(-var3 * 0.017453292F - (float)Math.PI);
                float var11 = MathHelper.sin(-var3 * 0.017453292F - (float)Math.PI);
                float var12 = -var11;
                float var13 = 0.0F;
                float var14 = -var10;
                float var15 = 0.0F;
                float var16 = 1.0F;
                float var17 = 0.0F;
                sndSystem.setListenerPosition((float)var4, (float)var6, (float)var8);
                sndSystem.setListenerOrientation(var12, var13, var14, var15, var16, var17);
            }
        }
    }

    public void func_82464_d()
    {
        Iterator var1 = this.field_82470_g.iterator();

        while (var1.hasNext())
        {
            String var2 = (String)var1.next();
            sndSystem.stop(var2);
        }

        this.field_82470_g.clear();
    }

    public void playStreaming(String par1Str, float par2, float par3, float par4)
    {
        if (loaded && (this.options.soundVolume != 0.0F || par1Str == null))
        {
            String var5 = "streaming";

            if (sndSystem.playing(var5))
            {
                sndSystem.stop(var5);
            }

            if (par1Str != null)
            {
                SoundPoolEntry var6 = this.soundPoolStreaming.getRandomSoundFromSoundPool(par1Str);
                var6 = SoundEvent.getResult(new PlayStreamingEvent(this, var6, par1Str, par2, par3, par4));

                if (var6 != null)
                {
                    if (sndSystem.playing("BgMusic"))
                    {
                        sndSystem.stop("BgMusic");
                    }

                    float var7 = 16.0F;
                    sndSystem.newStreamingSource(true, var5, var6.soundUrl, var6.soundName, false, par2, par3, par4, 2, var7 * 4.0F);
                    sndSystem.setVolume(var5, 0.5F * this.options.soundVolume);
                    MinecraftForge.EVENT_BUS.post(new PlayStreamingSourceEvent(this, var5, par2, par3, par4));
                    sndSystem.play(var5);
                }
            }
        }
    }

    public void func_82460_a(Entity par1Entity)
    {
        this.func_82462_a(par1Entity, par1Entity);
    }

    public void func_82462_a(Entity par1Entity, Entity par2Entity)
    {
        String var3 = "entity_" + par1Entity.entityId;

        if (this.field_82470_g.contains(var3))
        {
            if (sndSystem.playing(var3))
            {
                sndSystem.setPosition(var3, (float)par2Entity.posX, (float)par2Entity.posY, (float)par2Entity.posZ);
                sndSystem.setVelocity(var3, (float)par2Entity.motionX, (float)par2Entity.motionY, (float)par2Entity.motionZ);
            }
            else
            {
                this.field_82470_g.remove(var3);
            }
        }
    }

    public boolean func_82465_b(Entity par1Entity)
    {
        if (par1Entity != null && loaded && this.options.musicVolume != 0.0F)
        {
            String var2 = "entity_" + par1Entity.entityId;
            return sndSystem.playing(var2);
        }
        else
        {
            return false;
        }
    }

    public void func_82469_c(Entity par1Entity)
    {
        if (par1Entity != null && loaded && this.options.musicVolume != 0.0F)
        {
            String var2 = "entity_" + par1Entity.entityId;

            if (this.field_82470_g.contains(var2))
            {
                if (sndSystem.playing(var2))
                {
                    sndSystem.stop(var2);
                }

                this.field_82470_g.remove(var2);
            }
        }
    }

    public void func_82468_a(Entity par1Entity, float par2)
    {
        if (par1Entity != null && loaded && this.options.musicVolume != 0.0F)
        {
            if (loaded && this.options.soundVolume != 0.0F)
            {
                String var3 = "entity_" + par1Entity.entityId;

                if (sndSystem.playing(var3))
                {
                    sndSystem.setVolume(var3, par2 * this.options.soundVolume);
                }
            }
        }
    }

    public void func_82463_b(Entity par1Entity, float par2)
    {
        if (par1Entity != null && loaded && this.options.musicVolume != 0.0F)
        {
            if (loaded && this.options.soundVolume != 0.0F)
            {
                String var3 = "entity_" + par1Entity.entityId;

                if (sndSystem.playing(var3))
                {
                    sndSystem.setPitch(var3, par2);
                }
            }
        }
    }

    public void func_82467_a(String par1Str, Entity par2Entity, float par3, float par4, boolean par5)
    {
        if (par2Entity != null)
        {
            if (loaded && (this.options.soundVolume != 0.0F || par1Str == null))
            {
                String var6 = "entity_" + par2Entity.entityId;

                if (this.field_82470_g.contains(var6))
                {
                    this.func_82460_a(par2Entity);
                }
                else
                {
                    if (sndSystem.playing(var6))
                    {
                        sndSystem.stop(var6);
                    }

                    if (par1Str == null)
                    {
                        return;
                    }

                    SoundPoolEntry var7 = this.soundPoolSounds.getRandomSoundFromSoundPool(par1Str);

                    if (var7 != null && par3 > 0.0F)
                    {
                        float var8 = 16.0F;

                        if (par3 > 1.0F)
                        {
                            var8 *= par3;
                        }

                        sndSystem.newSource(par5, var6, var7.soundUrl, var7.soundName, false, (float)par2Entity.posX, (float)par2Entity.posY, (float)par2Entity.posZ, 2, var8);
                        sndSystem.setLooping(var6, true);
                        sndSystem.setPitch(var6, par4);

                        if (par3 > 1.0F)
                        {
                            par3 = 1.0F;
                        }

                        sndSystem.setVolume(var6, par3 * this.options.soundVolume);
                        sndSystem.setVelocity(var6, (float)par2Entity.motionX, (float)par2Entity.motionY, (float)par2Entity.motionZ);
                        sndSystem.play(var6);
                        this.field_82470_g.add(var6);
                    }
                }
            }
        }
    }

    /**
     * Plays a sound. Args: soundName, x, y, z, volume, pitch
     */
    public void playSound(String par1Str, float par2, float par3, float par4, float par5, float par6)
    {
        if (loaded && this.options.soundVolume != 0.0F)
        {
            SoundPoolEntry var7 = this.soundPoolSounds.getRandomSoundFromSoundPool(par1Str);
            var7 = SoundEvent.getResult(new PlaySoundEvent(this, var7, par1Str, par2, par3, par4, par5, par6));

            if (var7 != null && par5 > 0.0F)
            {
                this.latestSoundID = (this.latestSoundID + 1) % 256;
                String var8 = "sound_" + this.latestSoundID;
                float var9 = 16.0F;

                if (par5 > 1.0F)
                {
                    var9 *= par5;
                }

                sndSystem.newSource(par5 > 1.0F, var8, var7.soundUrl, var7.soundName, false, par2, par3, par4, 2, var9);
                sndSystem.setPitch(var8, par6);

                if (par5 > 1.0F)
                {
                    par5 = 1.0F;
                }

                sndSystem.setVolume(var8, par5 * this.options.soundVolume);
                MinecraftForge.EVENT_BUS.post(new PlaySoundSourceEvent(this, var8, par2, par3, par4));
                sndSystem.play(var8);
            }
        }
    }

    /**
     * Plays a sound effect with the volume and pitch of the parameters passed. The sound isn't affected by position of
     * the player (full volume and center balanced)
     */
    public void playSoundFX(String par1Str, float par2, float par3)
    {
        if (loaded && this.options.soundVolume != 0.0F)
        {
            SoundPoolEntry var4 = this.soundPoolSounds.getRandomSoundFromSoundPool(par1Str);
            var4 = SoundEvent.getResult(new PlaySoundEffectEvent(this, var4, par1Str, par2, par3));

            if (var4 != null)
            {
                this.latestSoundID = (this.latestSoundID + 1) % 256;
                String var5 = "sound_" + this.latestSoundID;
                sndSystem.newSource(false, var5, var4.soundUrl, var4.soundName, false, 0.0F, 0.0F, 0.0F, 0, 0.0F);

                if (par2 > 1.0F)
                {
                    par2 = 1.0F;
                }

                par2 *= 0.25F;
                sndSystem.setPitch(var5, par3);
                sndSystem.setVolume(var5, par2 * this.options.soundVolume);
                MinecraftForge.EVENT_BUS.post(new PlaySoundEffectSourceEvent(this, var5));
                sndSystem.play(var5);
            }
        }
    }

    public void func_82466_e()
    {
        Iterator var1 = this.field_82470_g.iterator();

        while (var1.hasNext())
        {
            String var2 = (String)var1.next();
            sndSystem.pause(var2);
        }
    }

    public void func_82461_f()
    {
        Iterator var1 = this.field_82470_g.iterator();

        while (var1.hasNext())
        {
            String var2 = (String)var1.next();
            sndSystem.play(var2);
        }
    }
}

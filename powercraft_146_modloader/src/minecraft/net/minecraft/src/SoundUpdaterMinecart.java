package net.minecraft.src;

public class SoundUpdaterMinecart implements IUpdatePlayerListBox
{
    private final SoundManager theSoundManager;

    /** Minecart which sound is being updated. */
    private final EntityMinecart theMinecart;

    /** The player that is getting the minecart sound updates. */
    private final EntityPlayerSP thePlayer;
    private boolean field_82473_d = false;
    private boolean field_82474_e = false;
    private boolean field_82471_f = false;
    private boolean field_82472_g = false;
    private float field_82480_h = 0.0F;
    private float field_82481_i = 0.0F;
    private float field_82478_j = 0.0F;
    private double field_82479_k = 0.0D;

    public SoundUpdaterMinecart(SoundManager par1SoundManager, EntityMinecart par2EntityMinecart, EntityPlayerSP par3EntityPlayerSP)
    {
        this.theSoundManager = par1SoundManager;
        this.theMinecart = par2EntityMinecart;
        this.thePlayer = par3EntityPlayerSP;
    }

    /**
     * Updates the JList with a new model.
     */
    public void update()
    {
        boolean var1 = false;
        boolean var2 = this.field_82473_d;
        boolean var3 = this.field_82474_e;
        boolean var4 = this.field_82471_f;
        float var5 = this.field_82481_i;
        float var6 = this.field_82480_h;
        float var7 = this.field_82478_j;
        double var8 = this.field_82479_k;
        this.field_82473_d = this.thePlayer != null && this.theMinecart.riddenByEntity == this.thePlayer;
        this.field_82474_e = this.theMinecart.isDead;
        this.field_82479_k = (double)MathHelper.sqrt_double(this.theMinecart.motionX * this.theMinecart.motionX + this.theMinecart.motionZ * this.theMinecart.motionZ);
        this.field_82471_f = this.field_82479_k >= 0.01D;

        if (var2 && !this.field_82473_d)
        {
            this.theSoundManager.stopEntitySound(this.thePlayer);
        }

        if (this.field_82474_e || !this.field_82472_g && this.field_82481_i == 0.0F && this.field_82478_j == 0.0F)
        {
            if (!var3)
            {
                this.theSoundManager.stopEntitySound(this.theMinecart);

                if (var2 || this.field_82473_d)
                {
                    this.theSoundManager.stopEntitySound(this.thePlayer);
                }
            }

            this.field_82472_g = true;

            if (this.field_82474_e)
            {
                return;
            }
        }

        if (!this.theSoundManager.isEntitySoundPlaying(this.theMinecart) && this.field_82481_i > 0.0F)
        {
            this.theSoundManager.playEntitySound("minecart.base", this.theMinecart, this.field_82481_i, this.field_82480_h, false);
            this.field_82472_g = false;
            var1 = true;
        }

        if (this.field_82473_d && !this.theSoundManager.isEntitySoundPlaying(this.thePlayer) && this.field_82478_j > 0.0F)
        {
            this.theSoundManager.playEntitySound("minecart.inside", this.thePlayer, this.field_82478_j, 1.0F, true);
            this.field_82472_g = false;
            var1 = true;
        }

        if (this.field_82471_f)
        {
            if (this.field_82480_h < 1.0F)
            {
                this.field_82480_h += 0.0025F;
            }

            if (this.field_82480_h > 1.0F)
            {
                this.field_82480_h = 1.0F;
            }

            float var10 = MathHelper.clamp_float((float)this.field_82479_k, 0.0F, 4.0F) / 4.0F;
            this.field_82478_j = 0.0F + var10 * 0.75F;
            var10 = MathHelper.clamp_float(var10 * 2.0F, 0.0F, 1.0F);
            this.field_82481_i = 0.0F + var10 * 0.7F;
        }
        else if (var4)
        {
            this.field_82481_i = 0.0F;
            this.field_82480_h = 0.0F;
            this.field_82478_j = 0.0F;
        }

        if (!this.field_82472_g)
        {
            if (this.field_82480_h != var6)
            {
                this.theSoundManager.setEntitySoundPitch(this.theMinecart, this.field_82480_h);
            }

            if (this.field_82481_i != var5)
            {
                this.theSoundManager.setEntitySoundVolume(this.theMinecart, this.field_82481_i);
            }

            if (this.field_82478_j != var7)
            {
                this.theSoundManager.setEntitySoundVolume(this.thePlayer, this.field_82478_j);
            }
        }

        if (!var1 && (this.field_82481_i > 0.0F || this.field_82478_j > 0.0F))
        {
            this.theSoundManager.updateSoundLocation(this.theMinecart);

            if (this.field_82473_d)
            {
                this.theSoundManager.updateSoundLocation(this.thePlayer, this.theMinecart);
            }
        }
        else
        {
            if (this.theSoundManager.isEntitySoundPlaying(this.theMinecart))
            {
                this.theSoundManager.stopEntitySound(this.theMinecart);
            }

            if (this.theSoundManager.isEntitySoundPlaying(this.thePlayer))
            {
                this.theSoundManager.stopEntitySound(this.thePlayer);
            }
        }
    }
}

package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class TextureClock extends TextureStitched
{
    private double field_94239_h;
    private double field_94240_i;

    public TextureClock()
    {
        super("compass");
    }

    public void func_94219_l()
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        double d0 = 0.0D;

        if (minecraft.theWorld != null && minecraft.thePlayer != null)
        {
            float f = minecraft.theWorld.getCelestialAngle(1.0F);
            d0 = (double)f;

            if (!minecraft.theWorld.provider.isSurfaceWorld())
            {
                d0 = Math.random();
            }
        }

        double d1;

        for (d1 = d0 - this.field_94239_h; d1 < -0.5D; ++d1)
        {
            ;
        }

        while (d1 >= 0.5D)
        {
            --d1;
        }

        if (d1 < -1.0D)
        {
            d1 = -1.0D;
        }

        if (d1 > 1.0D)
        {
            d1 = 1.0D;
        }

        this.field_94240_i += d1 * 0.1D;
        this.field_94240_i *= 0.8D;
        this.field_94239_h += this.field_94240_i;
        int i;

        for (i = (int)((this.field_94239_h + 1.0D) * (double)this.field_94226_b.size()) % this.field_94226_b.size(); i < 0; i = (i + this.field_94226_b.size()) % this.field_94226_b.size())
        {
            ;
        }

        if (i != this.field_94222_f)
        {
            this.field_94222_f = i;
            this.field_94228_a.func_94281_a(this.field_94224_d, this.field_94225_e, (Texture)this.field_94226_b.get(this.field_94222_f), this.field_94227_c);
        }
    }
}

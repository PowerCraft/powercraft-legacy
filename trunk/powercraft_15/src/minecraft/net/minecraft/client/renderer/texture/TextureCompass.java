package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class TextureCompass extends TextureStitched
{
    public static TextureCompass field_94243_h;
    public double field_94244_i;
    public double field_94242_j;

    public TextureCompass()
    {
        super("compass");
        field_94243_h = this;
    }

    public void func_94219_l()
    {
        Minecraft minecraft = Minecraft.getMinecraft();

        if (minecraft.theWorld != null && minecraft.thePlayer != null)
        {
            this.func_94241_a(minecraft.theWorld, minecraft.thePlayer.posX, minecraft.thePlayer.posZ, (double)minecraft.thePlayer.rotationYaw, false, false);
        }
        else
        {
            this.func_94241_a((World)null, 0.0D, 0.0D, 0.0D, true, false);
        }
    }

    public void func_94241_a(World par1World, double par2, double par4, double par6, boolean par8, boolean par9)
    {
        double d3 = 0.0D;

        if (par1World != null && !par8)
        {
            ChunkCoordinates chunkcoordinates = par1World.getSpawnPoint();
            double d4 = (double)chunkcoordinates.posX - par2;
            double d5 = (double)chunkcoordinates.posZ - par4;
            par6 %= 360.0D;
            d3 = -((par6 - 90.0D) * Math.PI / 180.0D - Math.atan2(d5, d4));

            if (!par1World.provider.isSurfaceWorld())
            {
                d3 = Math.random() * Math.PI * 2.0D;
            }
        }

        if (par9)
        {
            this.field_94244_i = d3;
        }
        else
        {
            double d6;

            for (d6 = d3 - this.field_94244_i; d6 < -Math.PI; d6 += (Math.PI * 2D))
            {
                ;
            }

            while (d6 >= Math.PI)
            {
                d6 -= (Math.PI * 2D);
            }

            if (d6 < -1.0D)
            {
                d6 = -1.0D;
            }

            if (d6 > 1.0D)
            {
                d6 = 1.0D;
            }

            this.field_94242_j += d6 * 0.1D;
            this.field_94242_j *= 0.8D;
            this.field_94244_i += this.field_94242_j;
        }

        int i;

        for (i = (int)((this.field_94244_i / (Math.PI * 2D) + 1.0D) * (double)this.field_94226_b.size()) % this.field_94226_b.size(); i < 0; i = (i + this.field_94226_b.size()) % this.field_94226_b.size())
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

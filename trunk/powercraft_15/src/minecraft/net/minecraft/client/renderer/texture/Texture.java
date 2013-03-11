package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

@SideOnly(Side.CLIENT)
public class Texture
{
    private int field_94293_a;
    private int field_94291_b;
    private int field_94292_c;
    private final int field_94289_d;
    private final int field_94290_e;
    private final int field_94287_f;
    private final int field_94288_g;
    private final int field_94300_h;
    private final int field_94301_i;
    private final int field_94298_j;
    private final int field_94299_k;
    private final boolean field_94296_l;
    private final String field_94297_m;
    private Rect2i field_94294_n;
    private boolean field_94295_o;
    private boolean field_94304_p;
    private boolean field_94303_q;
    private ByteBuffer field_94302_r;

    private Texture(String par1Str, int par2, int par3, int par4, int par5, int par6, int par7, int par8, int par9)
    {
        this.field_94297_m = par1Str;
        this.field_94292_c = par2;
        this.field_94289_d = par3;
        this.field_94290_e = par4;
        this.field_94287_f = par5;
        this.field_94288_g = par7;
        this.field_94301_i = par8;
        this.field_94298_j = par9;
        this.field_94299_k = par6;
        this.field_94294_n = new Rect2i(0, 0, par3, par4);

        if (par4 == 1 && par5 == 1)
        {
            this.field_94300_h = 3552;
        }
        else if (par5 == 1)
        {
            this.field_94300_h = 3553;
        }
        else
        {
            this.field_94300_h = 32879;
        }

        this.field_94296_l = par8 != 9728 && par8 != 9729 || par9 != 9728 && par9 != 9729;

        if (par2 != 2)
        {
            this.field_94293_a = GL11.glGenTextures();
            GL11.glBindTexture(this.field_94300_h, this.field_94293_a);
            GL11.glTexParameteri(this.field_94300_h, GL11.GL_TEXTURE_MIN_FILTER, par8);
            GL11.glTexParameteri(this.field_94300_h, GL11.GL_TEXTURE_MAG_FILTER, par9);
            GL11.glTexParameteri(this.field_94300_h, GL11.GL_TEXTURE_WRAP_S, par6);
            GL11.glTexParameteri(this.field_94300_h, GL11.GL_TEXTURE_WRAP_T, par6);
        }
        else
        {
            this.field_94293_a = -1;
        }

        this.field_94291_b = TextureManager.func_94267_b().func_94265_c();
    }

    public Texture(String par1Str, int par2, int par3, int par4, int par5, int par6, int par7, int par8, BufferedImage par9BufferedImage)
    {
        this(par1Str, par2, par3, par4, 1, par5, par6, par7, par8, par9BufferedImage);
    }

    public Texture(String par1Str, int par2, int par3, int par4, int par5, int par6, int par7, int par8, int par9, BufferedImage par10BufferedImage)
    {
        this(par1Str, par2, par3, par4, par5, par6, par7, par8, par9);

        if (par10BufferedImage == null)
        {
            if (par3 != -1 && par4 != -1)
            {
                byte[] abyte = new byte[par3 * par4 * par5 * 4];

                for (int i2 = 0; i2 < abyte.length; ++i2)
                {
                    abyte[i2] = 0;
                }

                this.field_94302_r = ByteBuffer.allocateDirect(abyte.length);
                this.field_94302_r.clear();
                this.field_94302_r.put(abyte);
                this.field_94302_r.position(0).limit(abyte.length);

                if (this.field_94304_p)
                {
                    this.func_94285_g();
                }
                else
                {
                    this.field_94303_q = false;
                }
            }
            else
            {
                this.field_94295_o = false;
            }
        }
        else
        {
            this.field_94295_o = true;
            this.func_94278_a(par10BufferedImage);

            if (par2 != 2)
            {
                this.func_94285_g();
                this.field_94304_p = false;
            }
        }
    }

    public final Rect2i func_94274_a()
    {
        return this.field_94294_n;
    }

    public void func_94272_a(Rect2i par1Rect2i, int par2)
    {
        if (this.field_94300_h != 32879)
        {
            Rect2i rect2i1 = new Rect2i(0, 0, this.field_94289_d, this.field_94290_e);
            rect2i1.func_94156_a(par1Rect2i);
            this.field_94302_r.position(0);

            for (int j = rect2i1.func_94160_b(); j < rect2i1.func_94160_b() + rect2i1.func_94157_d(); ++j)
            {
                int k = j * this.field_94289_d * 4;

                for (int l = rect2i1.func_94158_a(); l < rect2i1.func_94158_a() + rect2i1.func_94159_c(); ++l)
                {
                    this.field_94302_r.put(k + l * 4 + 0, (byte)(par2 >> 24 & 255));
                    this.field_94302_r.put(k + l * 4 + 1, (byte)(par2 >> 16 & 255));
                    this.field_94302_r.put(k + l * 4 + 2, (byte)(par2 >> 8 & 255));
                    this.field_94302_r.put(k + l * 4 + 3, (byte)(par2 >> 0 & 255));
                }
            }

            if (this.field_94304_p)
            {
                this.func_94285_g();
            }
            else
            {
                this.field_94303_q = false;
            }
        }
    }

    public void func_94279_c(String par1Str)
    {
        BufferedImage bufferedimage = new BufferedImage(this.field_94289_d, this.field_94290_e, 2);
        ByteBuffer bytebuffer = this.func_94273_h();
        byte[] abyte = new byte[this.field_94289_d * this.field_94290_e * 4];
        bytebuffer.position(0);
        bytebuffer.get(abyte);

        for (int i = 0; i < this.field_94289_d; ++i)
        {
            for (int j = 0; j < this.field_94290_e; ++j)
            {
                int k = j * this.field_94289_d * 4 + i * 4;
                byte b0 = 0;
                int l = b0 | (abyte[k + 2] & 255) << 0;
                l |= (abyte[k + 1] & 255) << 8;
                l |= (abyte[k + 0] & 255) << 16;
                l |= (abyte[k + 3] & 255) << 24;
                bufferedimage.setRGB(i, j, l);
            }
        }

        this.field_94302_r.position(this.field_94289_d * this.field_94290_e * 4);

        try
        {
            ImageIO.write(bufferedimage, "png", new File(Minecraft.getMinecraftDir(), par1Str));
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public void func_94281_a(int par1, int par2, Texture par3Texture, boolean par4)
    {
        if (this.field_94300_h != 32879)
        {
            ByteBuffer bytebuffer = par3Texture.func_94273_h();
            this.field_94302_r.position(0);
            bytebuffer.position(0);

            for (int k = 0; k < par3Texture.func_94276_e(); ++k)
            {
                int l = par2 + k;
                int i1 = k * par3Texture.func_94275_d() * 4;
                int j1 = l * this.field_94289_d * 4;

                if (par4)
                {
                    l = par2 + (par3Texture.func_94276_e() - k);
                }

                for (int k1 = 0; k1 < par3Texture.func_94275_d(); ++k1)
                {
                    int l1 = j1 + (k1 + par1) * 4;
                    int i2 = i1 + k1 * 4;

                    if (par4)
                    {
                        l1 = par1 + k1 * this.field_94289_d * 4 + l * 4;
                    }

                    this.field_94302_r.put(l1 + 0, bytebuffer.get(i2 + 0));
                    this.field_94302_r.put(l1 + 1, bytebuffer.get(i2 + 1));
                    this.field_94302_r.put(l1 + 2, bytebuffer.get(i2 + 2));
                    this.field_94302_r.put(l1 + 3, bytebuffer.get(i2 + 3));
                }
            }

            this.field_94302_r.position(this.field_94289_d * this.field_94290_e * 4);

            if (this.field_94304_p)
            {
                this.func_94285_g();
            }
            else
            {
                this.field_94303_q = false;
            }
        }
    }

    public void func_94278_a(BufferedImage par1BufferedImage)
    {
        if (this.field_94300_h != 32879)
        {
            int i = par1BufferedImage.getWidth();
            int j = par1BufferedImage.getHeight();

            if (i <= this.field_94289_d && j <= this.field_94290_e)
            {
                int[] aint = new int[] {3, 0, 1, 2};
                int[] aint1 = new int[] {3, 2, 1, 0};
                int[] aint2 = this.field_94288_g == 32993 ? aint1 : aint;
                int[] aint3 = new int[this.field_94289_d * this.field_94290_e];
                int k = par1BufferedImage.getTransparency();
                par1BufferedImage.getRGB(0, 0, this.field_94289_d, this.field_94290_e, aint3, 0, i);
                byte[] abyte = new byte[this.field_94289_d * this.field_94290_e * 4];

                for (int l = 0; l < this.field_94290_e; ++l)
                {
                    for (int i1 = 0; i1 < this.field_94289_d; ++i1)
                    {
                        int j1 = l * this.field_94289_d + i1;
                        int k1 = j1 * 4;
                        abyte[k1 + aint2[0]] = (byte)(aint3[j1] >> 24 & 255);
                        abyte[k1 + aint2[1]] = (byte)(aint3[j1] >> 16 & 255);
                        abyte[k1 + aint2[2]] = (byte)(aint3[j1] >> 8 & 255);
                        abyte[k1 + aint2[3]] = (byte)(aint3[j1] >> 0 & 255);
                    }
                }

                this.field_94302_r = ByteBuffer.allocateDirect(abyte.length);
                this.field_94302_r.clear();
                this.field_94302_r.put(abyte);
                this.field_94302_r.limit(abyte.length);

                if (this.field_94304_p)
                {
                    this.func_94285_g();
                }
                else
                {
                    this.field_94303_q = false;
                }
            }
            else
            {
                Minecraft.getMinecraft().func_98033_al().func_98236_b("transferFromImage called with a BufferedImage with dimensions (" + i + ", " + j + ") larger than the Texture dimensions (" + this.field_94289_d + ", " + this.field_94290_e + "). Ignoring.");
            }
        }
    }

    public int func_94284_b()
    {
        return this.field_94291_b;
    }

    public int func_94282_c()
    {
        return this.field_94293_a;
    }

    public int func_94275_d()
    {
        return this.field_94289_d;
    }

    public int func_94276_e()
    {
        return this.field_94290_e;
    }

    public String func_94280_f()
    {
        return this.field_94297_m;
    }

    public void func_94277_a(int par1)
    {
        if (this.field_94287_f == 1)
        {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
        else
        {
            GL11.glEnable(GL12.GL_TEXTURE_3D);
        }

        GL13.glActiveTexture(GL13.GL_TEXTURE0 + par1);
        GL11.glBindTexture(this.field_94300_h, this.field_94293_a);

        if (!this.field_94303_q)
        {
            this.func_94285_g();
        }
    }

    public void func_94285_g()
    {
        this.field_94302_r.flip();

        if (this.field_94290_e != 1 && this.field_94287_f != 1)
        {
            GL12.glTexImage3D(this.field_94300_h, 0, this.field_94288_g, this.field_94289_d, this.field_94290_e, this.field_94287_f, 0, this.field_94288_g, GL11.GL_UNSIGNED_BYTE, this.field_94302_r);
        }
        else if (this.field_94290_e != 1)
        {
            GL11.glTexImage2D(this.field_94300_h, 0, this.field_94288_g, this.field_94289_d, this.field_94290_e, 0, this.field_94288_g, GL11.GL_UNSIGNED_BYTE, this.field_94302_r);
        }
        else
        {
            GL11.glTexImage1D(this.field_94300_h, 0, this.field_94288_g, this.field_94289_d, 0, this.field_94288_g, GL11.GL_UNSIGNED_BYTE, this.field_94302_r);
        }

        this.field_94303_q = true;
    }

    public ByteBuffer func_94273_h()
    {
        return this.field_94302_r;
    }
}

package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.texturepacks.ITexturePack;
import net.minecraft.util.Icon;
import net.minecraft.util.Tuple;

@SideOnly(Side.CLIENT)
public class TextureStitched implements Icon
{
    private final String field_94235_h;
    protected Texture field_94228_a;
    protected List field_94226_b;
    private List field_94236_i;
    protected boolean field_94227_c;
    protected int field_94224_d;
    protected int field_94225_e;
    private int field_94233_j;
    private int field_94234_k;
    private float field_94231_l;
    private float field_94232_m;
    private float field_94229_n;
    private float field_94230_o;
    private float field_94238_p;
    private float field_94237_q;
    protected int field_94222_f = 0;
    protected int field_94223_g = 0;

    public static TextureStitched func_94220_a(String par0Str)
    {
        return (TextureStitched)("clock".equals(par0Str) ? new TextureClock() : ("compass".equals(par0Str) ? new TextureCompass() : new TextureStitched(par0Str)));
    }

    protected TextureStitched(String par1)
    {
        this.field_94235_h = par1;
    }

    public void func_94218_a(Texture par1Texture, List par2List, int par3, int par4, int par5, int par6, boolean par7)
    {
        this.field_94228_a = par1Texture;
        this.field_94226_b = par2List;
        this.field_94224_d = par3;
        this.field_94225_e = par4;
        this.field_94233_j = par5;
        this.field_94234_k = par6;
        this.field_94227_c = par7;
        this.field_94231_l = (float)par3 / (float)par1Texture.func_94275_d();
        this.field_94232_m = (float)(par3 + par5) / (float)par1Texture.func_94275_d();
        this.field_94229_n = (float)par4 / (float)par1Texture.func_94276_e();
        this.field_94230_o = (float)(par4 + par6) / (float)par1Texture.func_94276_e();
        this.field_94238_p = (float)par5 / 16.0F;
        this.field_94237_q = (float)par6 / 16.0F;
    }

    public void func_94217_a(TextureStitched par1TextureStitched)
    {
        this.func_94218_a(par1TextureStitched.field_94228_a, par1TextureStitched.field_94226_b, par1TextureStitched.field_94224_d, par1TextureStitched.field_94225_e, par1TextureStitched.field_94233_j, par1TextureStitched.field_94234_k, par1TextureStitched.field_94227_c);
    }

    public int func_94211_a()
    {
        return this.field_94224_d;
    }

    public int func_94216_b()
    {
        return this.field_94225_e;
    }

    public float func_94209_e()
    {
        return this.field_94231_l;
    }

    public float func_94212_f()
    {
        return this.field_94232_m - Float.MIN_VALUE;
    }

    public float func_94214_a(double par1)
    {
        float f = this.field_94232_m - this.field_94231_l;
        return this.field_94231_l + f * ((float)par1 / 16.0F) - Float.MIN_VALUE;
    }

    public float func_94206_g()
    {
        return this.field_94229_n;
    }

    public float func_94210_h()
    {
        return this.field_94230_o - Float.MIN_VALUE;
    }

    public float func_94207_b(double par1)
    {
        float f = this.field_94230_o - this.field_94229_n;
        return this.field_94229_n + f * ((float)par1 / 16.0F) - Float.MIN_VALUE;
    }

    public String func_94215_i()
    {
        return this.field_94235_h;
    }

    public int func_94213_j()
    {
        return this.field_94228_a.func_94275_d();
    }

    public int func_94208_k()
    {
        return this.field_94228_a.func_94276_e();
    }

    public void func_94219_l()
    {
        if (this.field_94236_i != null)
        {
            Tuple tuple = (Tuple)this.field_94236_i.get(this.field_94222_f);
            ++this.field_94223_g;

            if (this.field_94223_g >= ((Integer)tuple.getSecond()).intValue())
            {
                int i = ((Integer)tuple.getFirst()).intValue();
                this.field_94222_f = (this.field_94222_f + 1) % this.field_94236_i.size();
                this.field_94223_g = 0;
                tuple = (Tuple)this.field_94236_i.get(this.field_94222_f);
                int j = ((Integer)tuple.getFirst()).intValue();

                if (i != j && j >= 0 && j < this.field_94226_b.size())
                {
                    this.field_94228_a.func_94281_a(this.field_94224_d, this.field_94225_e, (Texture)this.field_94226_b.get(j), this.field_94227_c);
                }
            }
        }
        else
        {
            int k = this.field_94222_f;
            this.field_94222_f = (this.field_94222_f + 1) % this.field_94226_b.size();

            if (k != this.field_94222_f)
            {
                this.field_94228_a.func_94281_a(this.field_94224_d, this.field_94225_e, (Texture)this.field_94226_b.get(this.field_94222_f), this.field_94227_c);
            }
        }
    }

    public void func_94221_a(BufferedReader par1BufferedReader)
    {
        ArrayList arraylist = new ArrayList();

        try
        {
            for (String s = par1BufferedReader.readLine(); s != null; s = par1BufferedReader.readLine())
            {
                s = s.trim();

                if (s.length() > 0)
                {
                    String[] astring = s.split(",");
                    String[] astring1 = astring;
                    int i = astring.length;

                    for (int j = 0; j < i; ++j)
                    {
                        String s1 = astring1[j];
                        int k = s1.indexOf(42);

                        if (k > 0)
                        {
                            Integer integer = new Integer(s1.substring(0, k));
                            Integer integer1 = new Integer(s1.substring(k + 1));
                            arraylist.add(new Tuple(integer, integer1));
                        }
                        else
                        {
                            arraylist.add(new Tuple(new Integer(s1), Integer.valueOf(1)));
                        }
                    }
                }
            }
        }
        catch (Exception exception)
        {
            System.err.println("Failed to read animation info for " + this.field_94235_h + ": " + exception.getMessage());
        }

        if (!arraylist.isEmpty() && arraylist.size() < 600)
        {
            this.field_94236_i = arraylist;
        }
    }

    //===================================================================================================
    //                                           Forge Start
    //===================================================================================================
    /**
     * Called when texture packs are refreshed, from TextureManager.createNewTexture,
     * allows for finer control over loading the animation lists and verification of the image.
     * If the return value from this is true, no further loading will be done by vanilla code.
     * 
     * You need to add all Texture's to the textures argument. At the end of this function at least one
     * entry should be in that argument, or a error should of been thrown.
     * 
     * @param manager The invoking manager
     * @param texturepack Current texture pack
     * @param name The name of the texture
     * @param fileName Resource path for this texture
     * @param image Buffered image of the loaded resource
     * @param textures ArrayList of element type Texture, split textures should be added to this list for the stitcher to handle.  
     * @return Return true to skip further vanilla texture loading for this texture
     */
    public boolean loadTexture(TextureManager manager, ITexturePack texturepack, String name, String fileName, BufferedImage image, ArrayList textures)
    {
        return false;
    }
}

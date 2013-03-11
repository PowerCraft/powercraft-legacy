package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.texturepacks.ITexturePack;

@SideOnly(Side.CLIENT)
public class TextureManager
{
    private static TextureManager field_94271_a;
    private int field_94269_b = 0;
    private final HashMap field_94270_c = new HashMap();
    private final HashMap field_94268_d = new HashMap();

    public static void func_94263_a()
    {
        field_94271_a = new TextureManager();
    }

    public static TextureManager func_94267_b()
    {
        return field_94271_a;
    }

    public int func_94265_c()
    {
        return this.field_94269_b++;
    }

    public void func_94264_a(String par1Str, Texture par2Texture)
    {
        this.field_94268_d.put(par1Str, Integer.valueOf(par2Texture.func_94284_b()));

        if (!this.field_94270_c.containsKey(Integer.valueOf(par2Texture.func_94284_b())))
        {
            this.field_94270_c.put(Integer.valueOf(par2Texture.func_94284_b()), par2Texture);
        }
    }

    public void func_94259_a(Texture par1Texture)
    {
        if (this.field_94270_c.containsValue(par1Texture))
        {
            Minecraft.getMinecraft().func_98033_al().func_98236_b("TextureManager.registerTexture called, but this texture has already been registered. ignoring.");
        }
        else
        {
            this.field_94270_c.put(Integer.valueOf(par1Texture.func_94284_b()), par1Texture);
        }
    }

    public Stitcher func_94262_d(String par1Str)
    {
        int i = Minecraft.getGLMaximumTextureSize();
        return new Stitcher(par1Str, i, i, true);
    }

    public List func_94266_e(String par1Str)
    {
        return createNewTexture(par1Str, par1Str, null);
    }

    public List createNewTexture(String textureName, String textureFile, TextureStitched stitched)
    {
        String par1Str = textureFile;
        ArrayList arraylist = new ArrayList();
        ITexturePack itexturepack = Minecraft.getMinecraft().texturePackList.getSelectedTexturePack();

        try
        {
            BufferedImage bufferedimage = null;
            int i = 0;
            int j = 0;
            FileNotFoundException fnfe = null;
            try
            {
                bufferedimage = ImageIO.read(itexturepack.getResourceAsStream("/" + textureFile));
                i = bufferedimage.getHeight();
                j = bufferedimage.getWidth();
            }
            catch (FileNotFoundException e)
            {
                fnfe = e;
            }
            String s1 = textureName;

            if (stitched != null && stitched.loadTexture(this, itexturepack, textureName, textureFile, bufferedimage, arraylist))
            {
                ;
            }
            else if (fnfe != null)
            {
                throw fnfe;
            }
            else if (this.func_98147_a(par1Str, itexturepack))
            {
                int k = j;
                int l = j;
                int i1 = i / j;

                for (int j1 = 0; j1 < i1; ++j1)
                {
                    Texture texture = this.func_94261_a(s1, 2, k, l, 10496, 6408, 9728, 9728, false, bufferedimage.getSubimage(0, l * j1, k, l));
                    arraylist.add(texture);
                }
            }
            else if (j == i)
            {
                arraylist.add(this.func_94261_a(s1, 2, j, i, 10496, 6408, 9728, 9728, false, bufferedimage));
            }
            else
            {
                Minecraft.getMinecraft().func_98033_al().func_98236_b("TextureManager.createTexture: Skipping " + par1Str + " because of broken aspect ratio and not animation");
            }

            return arraylist;
        }
        catch (FileNotFoundException filenotfoundexception)
        {
            Minecraft.getMinecraft().func_98033_al().func_98236_b("TextureManager.createTexture called for file " + par1Str + ", but that file does not exist. Ignoring.");
        }
        catch (IOException ioexception)
        {
            Minecraft.getMinecraft().func_98033_al().func_98236_b("TextureManager.createTexture encountered an IOException when trying to read file " + par1Str + ". Ignoring.");
        }

        return arraylist;
    }

    private String func_98146_d(String par1Str)
    {
        File file1 = new File(par1Str);
        return file1.getName().substring(0, file1.getName().lastIndexOf(46));
    }

    private boolean func_98147_a(String par1Str, ITexturePack par2ITexturePack)
    {
        String s1 = "/" + par1Str.substring(0, par1Str.lastIndexOf(46)) + ".txt";
        boolean flag = par2ITexturePack.func_98138_b("/" + par1Str, false);
        return Minecraft.getMinecraft().texturePackList.getSelectedTexturePack().func_98138_b(s1, !flag);
    }

    public Texture func_94261_a(String par1Str, int par2, int par3, int par4, int par5, int par6, int par7, int par8, boolean par9, BufferedImage par10BufferedImage)
    {
        Texture texture = new Texture(par1Str, par2, par3, par4, par5, par6, par7, par8, par10BufferedImage);
        this.func_94259_a(texture);
        return texture;
    }

    public Texture func_98145_a(String par1Str, int par2, int par3, int par4, int par5)
    {
        return this.func_94261_a(par1Str, par2, par3, par4, 10496, par5, 9728, 9728, false, (BufferedImage)null);
    }
}

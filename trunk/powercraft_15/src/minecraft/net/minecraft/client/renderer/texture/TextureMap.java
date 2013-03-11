package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.texturepacks.ITexturePack;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;

@SideOnly(Side.CLIENT)
public class TextureMap implements IconRegister
{
    private final int field_94255_a;
    private final String field_94253_b;
    private final String field_94254_c;
    private final String field_94251_d;
    private final HashMap field_94252_e = new HashMap();
    private BufferedImage field_94249_f = new BufferedImage(64, 64, 2);
    private TextureStitched field_94250_g;
    private Texture field_94257_h;
    private final List field_94258_i = new ArrayList();
    private final Map field_94256_j = new HashMap();

    public TextureMap(int par1, String par2, String par3Str, BufferedImage par4BufferedImage)
    {
        this.field_94255_a = par1;
        this.field_94253_b = par2;
        this.field_94254_c = par3Str;
        this.field_94251_d = ".png";
        this.field_94249_f = par4BufferedImage;
    }

    public void func_94247_b()
    {
        this.field_94256_j.clear();
        int i;
        int j;

        if (this.field_94255_a == 0)
        {
            Block[] ablock = Block.blocksList;
            i = ablock.length;

            for (j = 0; j < i; ++j)
            {
                Block block = ablock[j];

                if (block != null)
                {
                    block.func_94332_a(this);
                }
            }

            Minecraft.getMinecraft().renderGlobal.func_94140_a(this);
            RenderManager.instance.func_94178_a(this);
        }

        Item[] aitem = Item.itemsList;
        i = aitem.length;

        for (j = 0; j < i; ++j)
        {
            Item item = aitem[j];

            if (item != null && item.func_94901_k() == this.field_94255_a)
            {
                item.func_94581_a(this);
            }
        }

        HashMap hashmap = new HashMap();
        Stitcher stitcher = TextureManager.func_94267_b().func_94262_d(this.field_94253_b);
        this.field_94252_e.clear();
        this.field_94258_i.clear();
        Texture texture = TextureManager.func_94267_b().func_94261_a("missingno", 2, this.field_94249_f.getWidth(), this.field_94249_f.getHeight(), 10496, 6408, 9728, 9728, false, this.field_94249_f);
        StitchHolder stitchholder = new StitchHolder(texture);
        stitcher.func_94312_a(stitchholder);
        hashmap.put(stitchholder, Arrays.asList(new Texture[] {texture}));

        for (Map.Entry<String, TextureStitched> entry : ((Map<String, TextureStitched>)field_94256_j).entrySet())
        {
            String name = entry.getKey();
            String path;
            if (name.indexOf(':') == -1)
            {
                path = this.field_94254_c + name + this.field_94251_d;
            }
            else
            {
                String domain = name.substring(0,name.indexOf(':'));
                String file = name.substring(name.indexOf(':')+1);
                path = "mods/" + domain +"/" + field_94254_c + file + field_94251_d;
            }
            List list = TextureManager.func_94267_b().createNewTexture(name, path, entry.getValue());

            if (!list.isEmpty())
            {
                StitchHolder stitchholder1 = new StitchHolder((Texture)list.get(0));
                stitcher.func_94312_a(stitchholder1);
                hashmap.put(stitchholder1, list);
            }
        }

        try
        {
            stitcher.func_94305_f();
        }
        catch (StitcherException stitcherexception)
        {
            throw stitcherexception;
        }

        this.field_94257_h = stitcher.func_94306_e();
        Iterator iterator = stitcher.func_94309_g().iterator();

        while (iterator.hasNext())
        {
            StitchSlot stitchslot = (StitchSlot)iterator.next();
            StitchHolder stitchholder2 = stitchslot.func_94183_a();
            Texture texture1 = stitchholder2.func_98150_a();
            String s2 = texture1.func_94280_f();
            List list1 = (List)hashmap.get(stitchholder2);
            TextureStitched texturestitched = (TextureStitched)this.field_94256_j.get(s2);
            boolean flag = false;

            if (texturestitched == null)
            {
                flag = true;
                texturestitched = TextureStitched.func_94220_a(s2);

                if (!s2.equals("missingno"))
                {
                    Minecraft.getMinecraft().func_98033_al().func_98236_b("Couldn\'t find premade icon for " + s2 + " doing " + this.field_94253_b);
                }
            }

            texturestitched.func_94218_a(this.field_94257_h, list1, stitchslot.func_94186_b(), stitchslot.func_94185_c(), stitchholder2.func_98150_a().func_94275_d(), stitchholder2.func_98150_a().func_94276_e(), stitchholder2.func_94195_e());
            this.field_94252_e.put(s2, texturestitched);

            if (!flag)
            {
                this.field_94256_j.remove(s2);
            }

            if (list1.size() > 1)
            {
                this.field_94258_i.add(texturestitched);
                String s3;
                if (s2.indexOf(':') == -1)
                {
                    s3 = field_94254_c + s2 + ".txt";
                }
                else
                {
                    String domain = s2.substring(0, s2.indexOf(':'));
                    String file = s2.substring(s2.indexOf(':') + 1);
                    s3 = "mods/" + domain + "/" + field_94254_c + file + ".txt";
                }
                ITexturePack itexturepack = Minecraft.getMinecraft().texturePackList.getSelectedTexturePack();
                boolean flag1 = !itexturepack.func_98138_b("/" + this.field_94254_c + s2 + ".png", false);

                try
                {
                    InputStream inputstream = itexturepack.func_98137_a("/" + s3, flag1);
                    Minecraft.getMinecraft().func_98033_al().func_98233_a("Found animation info for: " + s3);
                    texturestitched.func_94221_a(new BufferedReader(new InputStreamReader(inputstream)));
                }
                catch (IOException ioexception)
                {
                    ;
                }
            }
        }

        this.field_94250_g = (TextureStitched)this.field_94252_e.get("missingno");
        iterator = this.field_94256_j.values().iterator();

        while (iterator.hasNext())
        {
            TextureStitched texturestitched1 = (TextureStitched)iterator.next();
            texturestitched1.func_94217_a(this.field_94250_g);
        }

        this.field_94257_h.func_94279_c("debug.stitched_" + this.field_94253_b + ".png");
        this.field_94257_h.func_94285_g();
    }

    public void func_94248_c()
    {
        Iterator iterator = this.field_94258_i.iterator();

        while (iterator.hasNext())
        {
            TextureStitched texturestitched = (TextureStitched)iterator.next();
            texturestitched.func_94219_l();
        }
    }

    public Texture func_94246_d()
    {
        return this.field_94257_h;
    }

    public Icon func_94245_a(String par1Str)
    {
        if (par1Str == null)
        {
            (new RuntimeException("Don\'t register null!")).printStackTrace();
        }

        TextureStitched texturestitched = (TextureStitched)this.field_94256_j.get(par1Str);

        if (texturestitched == null)
        {
            texturestitched = TextureStitched.func_94220_a(par1Str);
            this.field_94256_j.put(par1Str, texturestitched);
        }

        return texturestitched;
    }

    public Icon func_96455_e()
    {
        return this.field_94250_g;
    }

    //===================================================================================================
    //                                           Forge Start
    //===================================================================================================
    /**
     * Grabs the registered entry for the specified name, returning null if there was not a entry. 
     * Opposed to func_94245_a, this will not instantiate the entry, useful to test if a maping exists.
     * 
     * @param name The name of the entry to find
     * @return The registered entry, null if nothing was registered.
     */
    public TextureStitched getTextureExtry(String name)
    {
        return (TextureStitched)field_94256_j.get(name);
    }

    /**
     * Adds a texture registry entry to this map for the specified name if one does not already exist.
     * Returns false if the map already contains a entry for the specified name. 
     * 
     * @param name Entry name
     * @param entry Entry instance
     * @return True if the entry was added to the map, false otherwise.
     */
    public boolean setTextureEntry(String name, TextureStitched entry)
    {
        if (!field_94256_j.containsKey(name))
        {
            field_94256_j.put(name, entry);
            return true;
        }
        return false;
    }
}

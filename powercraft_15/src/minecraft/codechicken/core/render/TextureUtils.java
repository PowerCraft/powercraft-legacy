package codechicken.core.render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Dimension;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.texture.Rect2i;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

@SuppressWarnings("unchecked")
public class TextureUtils
{
    private static class RegisterTextureCallback extends Render
    {
        @Override
        public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1)
        {
        }
        
        @Override
        public void func_94143_a(IconRegister par1IconRegister)
        {
            for(IIconRegister r : iconRegistrars)
                r.registerIcons(par1IconRegister);
        }
    }
    
    static
    {
        RenderManager.instance.entityRenderMap.put(null, new RegisterTextureCallback());
    }
    
    public static interface IIconRegister
    {
        public void registerIcons(IconRegister register);
    }
    
    private static HashMap<String, Dimension> textureDimensions = new HashMap<String, Dimension>();
    private static ArrayList<IIconRegister> iconRegistrars = new ArrayList<TextureUtils.IIconRegister>();
    
    public static void addIconRegistrar(IIconRegister registrar)
    {
        iconRegistrars.add(registrar);
    }
    
    public static Texture createTextureObject(String textureFile)
    {
        BufferedImage img = loadBufferedImage(textureFile);
        return new Texture(textureFile, 2, img.getWidth(), img.getHeight(), GL11.GL_CLAMP, GL11.GL_RGBA, GL11.GL_NEAREST, GL11.GL_NEAREST, img);
    }
    
    public static BufferedImage loadBufferedImage(String textureFile)
    {
        try
        {
            InputStream in = engine().texturePack.getSelectedTexturePack().getResourceAsStream(textureFile);
            if(in != null)
            {
                BufferedImage img = loadBufferedImage(in);
                if(img != null)
                    textureDimensions.put(textureFile, new Dimension(img.getWidth(), img.getHeight()));
                return img;
            }
        }
        catch(Exception e)
        {
            System.err.println("Failed to load texture file: "+textureFile);
            e.printStackTrace();
        }
        textureDimensions.put(textureFile, new Dimension(0, 0));
        return null;
    }

    public static Dimension getTextureDimension(String textureFile)
    {
        Dimension dim = textureDimensions.get(textureFile);
        if(dim == null)
        {
            loadBufferedImage(textureFile);
            dim = textureDimensions.get(textureFile);
        }
        return dim;
    }

    public static BufferedImage loadBufferedImage(InputStream in) throws IOException
    {
        BufferedImage img = ImageIO.read(in);
        in.close();
        return img;
    }

    public static RenderEngine engine()
    {
        return Minecraft.getMinecraft().renderEngine;
    }

    public static Texture createTextureObject(String name, int w, int h)
    {
        return new Texture(name, 2, w, h, GL11.GL_CLAMP, GL11.GL_RGBA, GL11.GL_NEAREST, GL11.GL_NEAREST, null);
    }

    public static void copySubImg(Texture fromTex, int fromX, int fromY, int width, int height, Texture toTex, int toX, int toY)
    {        
        int fromWidth = fromTex.func_94275_d();        
        int toWidth = toTex.func_94275_d();
        
        ByteBuffer from = fromTex.func_94273_h();
        ByteBuffer to = toTex.func_94273_h();
        from.position(0);
        to.position(0);
        
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++)
            {
                int fp = ((y+fromY)*fromWidth+x+fromX)*4;
                int tp = ((y+toY)*toWidth+x+toX)*4;

                to.put(tp, from.get(fp));
                to.put(tp+1, from.get(fp+1));
                to.put(tp+2, from.get(fp+2));
                to.put(tp+3, from.get(fp+3));
            }
        toTex.func_94272_a(new Rect2i(0, 0, 0, 0), 0);//set not generated
    }

    public static void write(byte[] data, int width, int height, Texture toTex, int toX, int toY)
    {    
        int toWidth = toTex.func_94275_d();
        
        ByteBuffer to = toTex.func_94273_h();
        to.position(0);
        
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++)
            {
                int fp = (y*width+x)*4;
                int tp = ((y+toY)*toWidth+x+toX)*4;

                to.put(tp, data[fp]);
                to.put(tp+1, data[fp+1]);
                to.put(tp+2, data[fp+2]);
                to.put(tp+3, data[fp+3]);
            }
        toTex.func_94272_a(new Rect2i(0, 0, 0, 0), 0);//set not generated
        to.position(toWidth * toTex.func_94276_e() * 4);
    }
    
    public static boolean refreshTexture(TextureMap map, String name)
    {
        if(map.getTextureExtry(name) == null)
        {
            map.setTextureEntry(name, new PlaceholderTexture(name));
            return true;
        }
        return false;
    }

    public static void bindItemTexture(ItemStack stack)
    {
        engine().func_98187_b(stack.func_94608_d() == 0 ? "/terrain.png" : "/gui/items.png");
    }

    public static void bindTexture(String string)
    {
        engine().func_98187_b(string);
    }

    public static Icon getIconFromTexture(String name, IconRegister iconRegister)
    {
        TextureMap textureMap = (TextureMap)iconRegister;
        TextureSpecial icon = new TextureSpecial(name).setTextureFile(name);
        textureMap.setTextureEntry(name, icon);
        return icon;
    }
}

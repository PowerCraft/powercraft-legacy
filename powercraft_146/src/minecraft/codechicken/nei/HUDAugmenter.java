package codechicken.nei;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import codechicken.nei.api.ItemInfo;
import codechicken.nei.forge.GuiContainerManager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class HUDAugmenter
{
	public static void renderOverlay()
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.currentScreen == null &&
				mc.theWorld != null &&
				NEIClientConfig.getBooleanSetting("options.inworld tooltips") && 
				mc.objectMouseOver != null && 
				mc.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE)
		{
			World world = mc.theWorld;
			ArrayList<ItemStack> items = ItemInfo.getIdentifierItems(world, mc.thePlayer, mc.objectMouseOver);
			
			String name = null;
			ItemStack stack = null;
			for(int i = 0; i < items.size(); i++)
			{
				try
				{
					String s = GuiContainerManager.itemDisplayNameShort(items.get(i));
					if(s != null && !s.endsWith("Unnamed"))
					{
						name = s;
						stack = items.get(i);
						break;
					}
				}
				catch(Exception e){}
			}
			if(name == null)
				return;

			ScaledResolution res = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	        RenderHelper.disableStandardItemLighting();
	        GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glDisable(GL11.GL_DEPTH_TEST);

	        int w = GuiContainerManager.getStringWidthNoColours(mc.fontRenderer, name)+22;
	        int h = 16;
	        int drawx = res.getScaledWidth()-w-5;
	        int drawy = 5;
	        
            int i4 = 0xf0100010;
            drawGradientRect(drawx - 3, drawy - 4, w + 6, 1, i4, i4, 0);
            drawGradientRect(drawx - 3, drawy + h + 3, w + 6, 1, i4, i4, 0);
            drawGradientRect(drawx - 3, drawy - 3, w + 6, h + 6, i4, i4, 0);
            drawGradientRect(drawx - 4, drawy - 3, 1, h + 6, i4, i4, 0);
            drawGradientRect(drawx + w + 3, drawy - 3, 1, h + 6, i4, i4, 0);
            int colour1 = 0x505000ff;
            int colour2 = (colour1 & 0xfefefe) >> 1 | colour1 & 0xff000000;
            drawGradientRect(drawx - 3, drawy - 2, 1, h + 4, colour1, colour2, 0);
            drawGradientRect(drawx + w + 2, drawy - 2, 1, h + 4, colour1, colour2, 0);
            drawGradientRect(drawx - 3, drawy - 3, w + 6, 1, colour1, colour1, 0);
            drawGradientRect(drawx - 3, drawy + h + 2, w + 6, 1, colour2, colour2, 0);
            
            mc.fontRenderer.drawStringWithShadow(name, drawx+20, 9, 0xFFA0A0A0);
						
	        RenderHelper.enableGUIStandardItemLighting();
	        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GuiContainerManager.drawItem(drawx+1, 5, stack, mc.fontRenderer, mc.renderEngine);
		}
	}

	public static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6, int zLevel)
    {
        float var7 = (float)(par5 >> 24 & 255) / 255.0F;
        float var8 = (float)(par5 >> 16 & 255) / 255.0F;
        float var9 = (float)(par5 >> 8 & 255) / 255.0F;
        float var10 = (float)(par5 & 255) / 255.0F;
        float var11 = (float)(par6 >> 24 & 255) / 255.0F;
        float var12 = (float)(par6 >> 16 & 255) / 255.0F;
        float var13 = (float)(par6 >> 8 & 255) / 255.0F;
        float var14 = (float)(par6 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator var15 = Tessellator.instance;
        var15.startDrawingQuads();
        var15.setColorRGBA_F(var8, var9, var10, var7);
        var15.addVertex(par3+par1, par2, zLevel);
        var15.addVertex(par1, par2, zLevel);
        var15.setColorRGBA_F(var12, var13, var14, var11);
        var15.addVertex(par1, par4+par2, zLevel);
        var15.addVertex(par3+par1, par4+par2, zLevel);
        var15.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}

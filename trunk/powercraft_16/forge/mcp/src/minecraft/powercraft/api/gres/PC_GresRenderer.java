package powercraft.api.gres;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PC_GresRenderer {

	private static Minecraft mc = Minecraft.getMinecraft();
	private static RenderItem itemRenderer = new RenderItem();
	private static FontRenderer fontRenderer;
	
	public static void setFontRenderer(FontRenderer fontRenderer){
		PC_GresRenderer.fontRenderer = fontRenderer;
	}
	
	public static void drawItemStack(ItemStack itemStack, int x, int y, String str){
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        itemRenderer.zLevel = 200.0F;
        FontRenderer font = null;
        if (itemStack != null) font = itemStack.getItem().getFontRenderer(itemStack);
        if (font == null) font = fontRenderer;
        itemRenderer.renderItemAndEffectIntoGUI(font, mc.getTextureManager(), itemStack, x, y);
        itemRenderer.renderItemOverlayIntoGUI(font, mc.getTextureManager(), itemStack, x, y, str);
        itemRenderer.zLevel = 0.0F;
	}
	
	
	public static void drawHorizontalLine(int x1, int x2, int y, int color){
        if (x2 < x1){
        	drawRect(x2, y, x1 + 1, y + 1, color);
        }else{
        	drawRect(x1, y, x2 + 1, y + 1, color);
        }
    }

	public static void drawVerticalLine(int x, int y1, int y2, int color){
        if (y2 < y1){
        	drawRect(x, y2, x + 1, y1 + 1, color);
        }else{
        	drawRect(x, y1, x + 1, y2 + 1, color);
        }

    }

    /**
     * Draws a solid color rectangle with the specified coordinates and color. Args: x1, y1, x2, y2, color
     */
    public static void drawRect(int x1, int y1, int x2, int y2, int color){
        int tmp;

        if (x1 < x2){
        	tmp = x1;
            x1 = x2;
            x2 = tmp;
        }

        if (y1 < y2){
        	tmp = y1;
        	y1 = y2;
        	y2 = tmp;
        }

        float alpha = (color >> 24 & 255) / 255.0F;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(red, green, blue, alpha);
        tessellator.startDrawingQuads();
        tessellator.addVertex(x1, y2, 0);
        tessellator.addVertex(x2, y2, 0);
        tessellator.addVertex(x2, y1, 0);
        tessellator.addVertex(x1, y1, 0);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
    
}

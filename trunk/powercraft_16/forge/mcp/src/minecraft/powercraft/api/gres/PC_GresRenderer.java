package powercraft.api.gres;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

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
        itemRenderer.renderItemAndEffectIntoGUI(font, mc.func_110434_K(), itemStack, x, y);
        itemRenderer.renderItemOverlayIntoGUI(font, mc.func_110434_K(), itemStack, x, y, str);
        itemRenderer.zLevel = 0.0F;
	}
	
	
	
}

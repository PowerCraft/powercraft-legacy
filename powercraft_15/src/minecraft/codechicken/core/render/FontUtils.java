package codechicken.core.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class FontUtils
{
    public static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    
    public static void drawCenteredString(String s, int xCenter, int y, int colour)
    {
        fontRenderer.drawString(s, xCenter-fontRenderer.getStringWidth(s)/2, y, colour);
    }
    
    public static void drawRightString(String s, int xRight, int y, int colour)
    {
        fontRenderer.drawString(s, xRight-fontRenderer.getStringWidth(s), y, colour);
    }
}

package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.net.URI;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiScreenDemo extends GuiScreen
{
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.controlList.clear();
        byte var1 = -16;
        this.controlList.add(new GuiButton(1, this.width / 2 - 116, this.height / 4 + 132 + var1, 114, 20, StatCollector.translateToLocal("demo.help.buy")));
        this.controlList.add(new GuiButton(2, this.width / 2 + 2, this.height / 4 + 132 + var1, 114, 20, StatCollector.translateToLocal("demo.help.later")));
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
            case 1:
                par1GuiButton.enabled = false;

                try
                {
                    Class var2 = Class.forName("java.awt.Desktop");
                    Object var3 = var2.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
                    var2.getMethod("browse", new Class[] {URI.class}).invoke(var3, new Object[] {new URI("http://www.minecraft.net/store?source=demo")});
                }
                catch (Throwable var4)
                {
                    var4.printStackTrace();
                }

                break;
            case 2:
                this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
    }

    /**
     * Draws either a gradient over the background screen (when it exists) or a flat gradient over background.png
     */
    public void drawDefaultBackground()
    {
        super.drawDefaultBackground();
        int var1 = this.mc.renderEngine.getTexture("/gui/demo_bg.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var1);
        int var2 = (this.width - 248) / 2;
        int var3 = (this.height - 166) / 2;
        this.drawTexturedModalRect(var2, var3, 0, 0, 248, 166);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        int var4 = (this.width - 248) / 2 + 10;
        this.fontRenderer.drawString(StatCollector.translateToLocal("demo.help.title"), var4, 44, 2039583);
        GameSettings var6 = this.mc.gameSettings;
        byte var7 = 60;
        String var5 = StatCollector.translateToLocal("demo.help.movementShort");
        var5 = String.format(var5, new Object[] {Keyboard.getKeyName(var6.keyBindForward.keyCode), Keyboard.getKeyName(var6.keyBindLeft.keyCode), Keyboard.getKeyName(var6.keyBindBack.keyCode), Keyboard.getKeyName(var6.keyBindRight.keyCode)});
        this.fontRenderer.drawString(var5, var4, var7, 5197647);
        var5 = StatCollector.translateToLocal("demo.help.movementMouse");
        this.fontRenderer.drawString(var5, var4, var7 + 12, 5197647);
        var5 = StatCollector.translateToLocal("demo.help.jump");
        var5 = String.format(var5, new Object[] {Keyboard.getKeyName(var6.keyBindJump.keyCode)});
        this.fontRenderer.drawString(var5, var4, var7 + 24, 5197647);
        var5 = StatCollector.translateToLocal("demo.help.inventory");
        var5 = String.format(var5, new Object[] {Keyboard.getKeyName(var6.keyBindInventory.keyCode)});
        this.fontRenderer.drawString(var5, var4, var7 + 36, 5197647);
        this.fontRenderer.drawSplitString(StatCollector.translateToLocal("demo.help.fullWrapped"), var4, var7 + 68, 218, 2039583);
        super.drawScreen(par1, par2, par3);
    }
}

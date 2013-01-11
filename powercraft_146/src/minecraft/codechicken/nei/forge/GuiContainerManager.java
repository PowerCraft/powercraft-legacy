package codechicken.nei.forge;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.inventory.Slot;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiContainerManager
{
    public GuiContainer window;

    public static RenderItem drawItems = new RenderItem();
    public static final LinkedList<IContainerTooltipHandler> tooltipHandlers = new LinkedList<IContainerTooltipHandler>();
    public static final LinkedList<IContainerInputHandler> inputHandlers = new LinkedList<IContainerInputHandler>();
    public static final LinkedList<IContainerDrawHandler> drawHandlers = new LinkedList<IContainerDrawHandler>();
    public static final LinkedList<IContainerObjectHandler> objectHandlers = new LinkedList<IContainerObjectHandler>();
    public static final LinkedList<IContainerSlotClickHandler> slotClickHandlers = new LinkedList<IContainerSlotClickHandler>();

    static
    {
        addSlotClickHandler(new DefaultSlotClickHandler());
    }

    public GuiContainerManager(GuiContainer screen)
    {
        window = screen;
    }

    /**
     * Register a new Tooltip render handler;
     * @param handler The handler to register
     */
    public static void addTooltipHandler(IContainerTooltipHandler handler)
    {
        tooltipHandlers.add(handler);
    }

    /**
     * Register a new Input handler;
     * @param handler The handler to register
     */
    public static void addInputHandler(IContainerInputHandler handler)
    {
        inputHandlers.add(handler);
    }

    /**
     * Register a new Drawing handler;
     * @param handler The handler to register
     */
    public static void addDrawHandler(IContainerDrawHandler handler)
    {
        drawHandlers.add(handler);
    }

    /**
     * Register a new Object handler;
     * @param handler The handler to register
     */
    public static void addObjectHandler(IContainerObjectHandler handler)
    {
        objectHandlers.add(handler);
    }

    /**
     * Care needs to be taken with this method. It will insert your handler at the start of the list to be called first. You may need to simply edit the list yourself.
     * @param handler The handler to register.
     */
    public static void addSlotClickHandler(IContainerSlotClickHandler handler)
    {
        slotClickHandlers.addFirst(handler);
    }

    public Point getMousePosition()
    {
        Minecraft mc = window.mc;
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int w = scaledresolution.getScaledWidth();
        int h = scaledresolution.getScaledHeight();
        return new Point((Mouse.getX() * w) / mc.displayWidth, h - (Mouse.getY() * h) / mc.displayHeight - 1);
    }

    public ItemStack getStackMouseOver()
    {
        Point mousePos = getMousePosition();
        ItemStack item = null;

        for(IContainerObjectHandler objectHandler : objectHandlers)
        {
            item = objectHandler.getStackUnderMouse(window, mousePos.x, mousePos.y);
            if(item != null)
                return item;
        }

        Slot slot = window.getSlotAtPosition(mousePos.x, mousePos.y);
        if(slot != null)
            item = slot.getStack();
        return item;
    }

    public int getStringWidth(String s)
    {
        if(s == null || s.equals(""))
        {
            return 0;
        } else
        {
            return getStringWidthNoColours(window.fontRenderer, s);
        }
    }

    public static int getStringWidthNoColours(FontRenderer fontRenderer, String s)
    {
        while(true)
        {
            int pos = s.indexOf('\247');
            if(pos == -1)
                break;
            s = s.substring(0, pos)+s.substring(pos+2);
        }
        return fontRenderer.getStringWidth(s);
    }

    /**
     * 	Extra lines are often used for more information. For example enchantments, potion effects and mob spawner contents.
     * @param itemstack The item to get the name for.
     * @param gui An instance of the currentscreen passed to tooltip handlers. If null, only gui inspecific handlers should respond
     * @param includeHandlers If true tooltip handlers will add to the item tip
     * @return A list of Strings representing the text to be displayed on each line of the tool tip.
     */
    @SuppressWarnings("unchecked")
    public static List<String> itemDisplayNameMultiline(ItemStack itemstack, GuiContainer gui, boolean includeHandlers)
    {
        List<String> namelist = null;
        try
        {
            namelist = itemstack.getTooltip(Minecraft.getMinecraft().thePlayer, includeHandlers && Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
        }
        catch(Exception exception) {}

        if(namelist == null)
            namelist = new ArrayList<String>();

        if(namelist.size() == 0)
            namelist.add("Unnamed");

        if(namelist.get(0) == null || namelist.get(0).equals(""))
            namelist.set(0, "Unnamed");

        if(includeHandlers)
        {
            for(IContainerTooltipHandler handler : tooltipHandlers)
            {
                namelist = handler.handleItemTooltip(gui, itemstack, namelist);
            }
        }

        namelist.set(0, "\247"+Integer.toHexString(itemstack.getRarity().rarityColor)+namelist.get(0));
        for(int i = 1; i < namelist.size(); i++)
            namelist.set(i, "\u00a77"+namelist.get(i));

        return namelist;
    }

    /**
     * The general name of this item.
     * @param itemstack The {@link ItemStack} to get the name for.
     * @return The first line of the multiline display name.
     */
    public static String itemDisplayNameShort(ItemStack itemstack)
    {
        List<String> list = itemDisplayNameMultiline(itemstack, null, false);
        return list.get(0);
    }

    /**
     * Concatenates the multiline display name into one line for easy searching using string and {@link Pattern} functions.
     * @param itemstack The stack to get the name for
     * @return The multiline display name of this item separated by '#'
     */
    public static String concatenatedDisplayName(ItemStack itemstack, boolean includeHandlers)
    {
        List<String> list = itemDisplayNameMultiline(itemstack, null, includeHandlers);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(String name : list)
        {
            if(first)
            {
                first = false;
            }
            else
            {
                sb.append("#");
            }
            sb.append(name);
        }
        String s = sb.toString();
        while(true)
        {
            int pos = s.indexOf('\247');
            if(pos == -1)
                break;
            s = s.substring(0, pos)+s.substring(pos+2);
        }
        return s;
    }

    public void drawRect(int x, int y, int w, int h, int colour)
    {
        window.drawGradientRect(x, y, x + w, y + h, colour, colour);
    }

    public void drawGradientRect(int x, int y, int w, int h, int colour1, int colour2)
    {
        window.drawGradientRect(x, y, x + w, y + h, colour1, colour2);
    }

    public void drawTexturedModalRect(int x, int y, int tx, int ty, int w, int h)
    {
        window.drawTexturedModalRect(x, y, tx, ty, w, h);
    }

    public void drawText(int x, int y, String text, int colour, boolean shadow)
    {
        if(shadow)
            window.fontRenderer.drawStringWithShadow(text, x, y, colour);
        else
            window.fontRenderer.drawString(text, x, y, colour);
    }

    public void drawTextCentered(int x, int y, int w, int h, String text, int colour, boolean shadow)
    {
        drawText(x + (w - getStringWidth(text)) / 2, y + (h - 8) / 2, text, colour, shadow);
    }

    public void drawTextCentered(String text, int x, int y, int colour, boolean shadow)
    {
        drawText(x - getStringWidth(text) / 2, y, text, colour, shadow);
    }

    public void drawText(int x, int y, String text, boolean shadow)
    {
        drawText(x, y, text, 0xFFFFFFFF, shadow);
    }

    public void drawText(int x, int y, String text, int colour)
    {
        drawText(x, y, text, colour, true);
    }

    public void drawTextCentered(int x, int y, int w, int h, String text, int colour)
    {
        drawText(x + (w - getStringWidth(text)) / 2, y + (h - 8) / 2, text, colour);
    }

    public void drawTextCentered(String text, int x, int y, int colour)
    {
        drawText(x - getStringWidth(text) / 2, y, text, colour);
    }

    public void drawText(int x, int y, String text)
    {
        drawText(x, y, text, 0xFFFFFFFF);
    }

    public void drawTip(int x, int y, String text)
    {
        ArrayList<String> temp = new ArrayList<String>();
        temp.add(text);
        drawMultilineTip(x, y, temp);
    }

    public void drawMultilineTip(int x, int y, List<String> list)
    {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        if(list.size() > 0)
        {
            int maxwidth = 0;
            for(int line = 0; line < list.size(); line++)
            {
                int swidth = getStringWidthNoColours(window.fontRenderer, list.get(line));
                if(swidth > maxwidth)
                {
                    maxwidth = swidth;
                }
            }

            if(x + maxwidth > window.width - 16)
            {
                x = window.width - maxwidth - 16;
            }

            if(y < 16)
            {
                y = 16;
            }

            int drawx = (x - window.guiLeft) + 12;
            int drawy = y - window.guiTop - 12;

            int w = maxwidth;
            int h = 8;
            if(list.size() > 1)
            {
                h += 2 + (list.size() - 1) * 10;
            }
            window.zLevel = 300;
            GuiContainer.itemRenderer.zLevel = 300F;
            int i4 = 0xf0100010;
            drawGradientRect(drawx - 3, drawy - 4, w + 6, 1, i4, i4);
            drawGradientRect(drawx - 3, drawy + h + 3, w + 6, 1, i4, i4);
            drawGradientRect(drawx - 3, drawy - 3, w + 6, h + 6, i4, i4);
            drawGradientRect(drawx - 4, drawy - 3, 1, h + 6, i4, i4);
            drawGradientRect(drawx + w + 3, drawy - 3, 1, h + 6, i4, i4);
            int colour1 = 0x505000ff;
            int colour2 = (colour1 & 0xfefefe) >> 1 | colour1 & 0xff000000;
            drawGradientRect(drawx - 3, drawy - 2, 1, h + 4, colour1, colour2);
            drawGradientRect(drawx + w + 2, drawy - 2, 1, h + 4, colour1, colour2);
            drawGradientRect(drawx - 3, drawy - 3, w + 6, 1, colour1, colour1);
            drawGradientRect(drawx - 3, drawy + h + 2, w + 6, 1, colour2, colour2);
            for(int i = 0; i < list.size(); i++)
            {
                String s = list.get(i);
                window.fontRenderer.drawStringWithShadow(s, drawx, drawy, -1);
                if(i == 0)
                {
                    drawy += 2;
                }
                drawy += 10;
            }

            window.zLevel = 0;
            GuiContainer.itemRenderer.zLevel = 0.0F;
        }
    }

    public void drawItem(int i, int j, ItemStack itemstack)
    {
        drawItem(i, j, itemstack, window.fontRenderer, window.mc.renderEngine);
    }

    public static void drawItem(int i, int j, ItemStack itemstack, FontRenderer fontRenderer, RenderEngine renderEngine)
    {
        enable3DRender();
        drawItems.zLevel += 100F;
        try
        {
            drawItems.renderItemAndEffectIntoGUI(fontRenderer, renderEngine, itemstack, i, j);
            drawItems.renderItemOverlayIntoGUI(fontRenderer, renderEngine, itemstack, i, j);
        }
        catch(RuntimeException runtimeexception)
        {
            drawItems.renderItemIntoGUI(fontRenderer, renderEngine, new ItemStack(51, 1, 0), i, j);
        }
        drawItems.zLevel -= 100F;
        enable2DRender();
        if(Tessellator.instance.isDrawing)
            Tessellator.instance.draw();
    }

    public void setColouredItemRender(boolean enable)
    {
        drawItems.field_77024_a = !enable;
    }

    public static void enable3DRender()
    {
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
    }

    public static void enable2DRender()
    {
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
    }

    public int getTexture(String s)
    {
        return window.mc.renderEngine.getTexture(s);
    }

    public void bindTexture(int texture)
    {
        window.mc.renderEngine.bindTexture(texture);
    }

    public void bindTextureByName(String s)
    {
        bindTexture(getTexture(s));
    }

    public void load()
    {
        for(IContainerObjectHandler objectHandler : objectHandlers)
        {
            objectHandler.load(window);
        }
    }

    public void refresh()
    {
        for(IContainerObjectHandler objectHandler : objectHandlers)
        {
            objectHandler.guiTick(window);
        }
    }

    public void guiTick()
    {
        for(IContainerObjectHandler objectHandler : objectHandlers)
        {
            objectHandler.guiTick(window);
        }
    }

    public boolean lastKeyTyped(int keyID, char keyChar)
    {
        for(IContainerInputHandler inputhander : inputHandlers)
        {
            if(inputhander.lastKeyTyped(window, keyChar, keyID))
                return true;
        }
        return false;
    }

    public boolean firstKeyTyped(int keyID, char keyChar)
    {
        for(IContainerInputHandler inputhander : inputHandlers)
        {
            inputhander.onKeyTyped(window, keyChar, keyID);
        }

        for(IContainerInputHandler inputhander : inputHandlers)
        {
            if(inputhander.keyTyped(window, keyChar, keyID))
                return true;
        }
        return false;
    }

    public boolean mouseClicked(int mousex, int mousey, int button)
    {
        for(IContainerInputHandler inputhander : inputHandlers)
        {
            inputhander.onMouseClicked(window, mousex, mousey, button);
        }

        for(IContainerInputHandler inputhander : inputHandlers)
        {
            if(inputhander.mouseClicked(window, mousex, mousey, button))
                return true;
        }
        return false;
    }

    public void mouseWheel(int scrolled)
    {
        Point mousepos = getMousePosition();

        for(IContainerInputHandler inputHandler : inputHandlers)
        {
            inputHandler.onMouseScrolled(window, mousepos.x, mousepos.y, scrolled);
        }

        for(IContainerInputHandler inputHandler : inputHandlers)
        {
            if(inputHandler.mouseScrolled(window, mousepos.x, mousepos.y, scrolled))
                return;
        }
    }

    public void mouseUp(int mousex, int mousey, int button)
    {
        for(IContainerInputHandler inputhander : inputHandlers)
        {
            inputhander.onMouseUp(window, mousex, mousey, button);
        }
    }

    public void preDraw()
    {
        for(IContainerDrawHandler drawHandler : drawHandlers)
        {
            drawHandler.onPreDraw(window);
        }
    }

    public void renderObjects(int mousex, int mousey)
    {
        for(IContainerDrawHandler drawHandler : drawHandlers)
        {
            drawHandler.renderObjects(window, mousex, mousey);
        }

        for(IContainerDrawHandler drawHandler : drawHandlers)
        {
            drawHandler.postRenderObjects(window, mousex, mousey);
        }
    }

    public void renderToolTips(int mousex, int mousey)
    {
        List<String> tooltip = window.handleTooltip(mousex, mousey, new LinkedList<String>());

        for(IContainerTooltipHandler handler : tooltipHandlers)
        {
            tooltip = handler.handleTooltipFirst(window, mousex, mousey, tooltip);
        }

        if(tooltip.isEmpty() && shouldShowTooltip())//mouseover tip, not holding an item
        {
            ItemStack stack = getStackMouseOver();
            if(stack != null)
            {
                tooltip = itemDisplayNameMultiline(stack, window, true);
            }
            tooltip = window.handleItemTooltip(stack, mousex, mousey, tooltip);
        }

        drawMultilineTip(mousex, mousey, tooltip);
    }

    public boolean shouldShowTooltip()
    {
        for(IContainerObjectHandler handler : objectHandlers)
            if(!handler.shouldShowTooltip(window))
                return false;

        return window.mc.thePlayer.inventory.getItemStack() == null;
    }

    public void renderSlotUnderlay(Slot slot)
    {
        for(IContainerDrawHandler drawHandler : drawHandlers)
        {
            drawHandler.renderSlotUnderlay(window, slot);
        }
    }

    public void renderSlotOverlay(Slot slot)
    {
        for(IContainerDrawHandler drawHandler : drawHandlers)
        {
            drawHandler.renderSlotOverlay(window, slot);
        }
    }

    public boolean objectUnderMouse(int mousex, int mousey)
    {
        for(IContainerObjectHandler objectHandler : objectHandlers)
        {
            if(objectHandler.objectUnderMouse(window, mousex, mousey))
                return true;
        }
        return false;
    }

    public void handleMouseClick(Slot slot, int slotIndex, int button, int modifier)
    {
        for(IContainerSlotClickHandler handler : slotClickHandlers)
            handler.beforeSlotClick(window, slotIndex, button, slot, modifier);

        boolean eventHandled = false;
        for(IContainerSlotClickHandler handler : slotClickHandlers)
            eventHandled = handler.handleSlotClick(window, slotIndex, button, slot, modifier, eventHandled);

        for(IContainerSlotClickHandler handler : slotClickHandlers)
            handler.afterSlotClick(window, slotIndex, button, slot, modifier);
    }
}

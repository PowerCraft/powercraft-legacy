package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiRepair extends GuiContainer implements ICrafting
{
    private ContainerRepair field_82327_o;
    private GuiTextField field_82326_p;
    private InventoryPlayer field_82325_q;

    public GuiRepair(InventoryPlayer par1, World par2World, int par3, int par4, int par5)
    {
        super(new ContainerRepair(par1, par2World, par3, par4, par5, Minecraft.getMinecraft().thePlayer));
        this.field_82325_q = par1;
        this.field_82327_o = (ContainerRepair)this.inventorySlots;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int var1 = (this.width - this.xSize) / 2;
        int var2 = (this.height - this.ySize) / 2;
        this.field_82326_p = new GuiTextField(this.fontRenderer, var1 + 62, var2 + 24, 103, 12);
        this.field_82326_p.setTextColor(-1);
        this.field_82326_p.func_82266_h(-1);
        this.field_82326_p.setEnableBackgroundDrawing(false);
        this.field_82326_p.setMaxStringLength(30);
        this.inventorySlots.removeCraftingFromCrafters(this);
        this.inventorySlots.addCraftingToCrafters(this);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        this.inventorySlots.removeCraftingFromCrafters(this);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.repair"), 60, 6, 4210752);

        if (this.field_82327_o.maximumCost > 0)
        {
            int var3 = 8453920;
            boolean var4 = true;
            String var5 = StatCollector.translateToLocalFormatted("container.repair.cost", new Object[] {Integer.valueOf(this.field_82327_o.maximumCost)});

            if (this.field_82327_o.maximumCost >= 40 && !this.mc.thePlayer.capabilities.isCreativeMode)
            {
                var5 = StatCollector.translateToLocal("container.repair.expensive");
                var3 = 16736352;
            }
            else if (!this.field_82327_o.getSlot(2).getHasStack())
            {
                var4 = false;
            }
            else if (!this.field_82327_o.getSlot(2).canTakeStack(this.field_82325_q.player))
            {
                var3 = 16736352;
            }

            if (var4)
            {
                int var6 = -16777216 | (var3 & 16579836) >> 2 | var3 & -16777216;
                int var7 = this.xSize - 8 - this.fontRenderer.getStringWidth(var5);
                byte var8 = 67;

                if (this.fontRenderer.getUnicodeFlag())
                {
                    drawRect(var7 - 3, var8 - 2, this.xSize - 7, var8 + 10, -16777216);
                    drawRect(var7 - 2, var8 - 1, this.xSize - 8, var8 + 9, -12895429);
                }
                else
                {
                    this.fontRenderer.drawString(var5, var7, var8 + 1, var6);
                    this.fontRenderer.drawString(var5, var7 + 1, var8, var6);
                    this.fontRenderer.drawString(var5, var7 + 1, var8 + 1, var6);
                }

                this.fontRenderer.drawString(var5, var7, var8, var3);
            }
        }

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (this.field_82326_p.textboxKeyTyped(par1, par2))
        {
            this.field_82327_o.func_82850_a(this.field_82326_p.getText());
            this.mc.thePlayer.sendQueue.addToSendQueue(new Packet250CustomPayload("MC|ItemName", this.field_82326_p.getText().getBytes()));
        }
        else
        {
            super.keyTyped(par1, par2);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        this.field_82326_p.mouseClicked(par1, par2, par3);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.field_82326_p.drawTextBox();
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int var4 = this.mc.renderEngine.getTexture("/gui/repair.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(var5 + 59, var6 + 20, 0, this.ySize + (this.field_82327_o.getSlot(0).getHasStack() ? 0 : 16), 110, 16);

        if ((this.field_82327_o.getSlot(0).getHasStack() || this.field_82327_o.getSlot(1).getHasStack()) && !this.field_82327_o.getSlot(2).getHasStack())
        {
            this.drawTexturedModalRect(var5 + 99, var6 + 45, this.xSize, 0, 28, 21);
        }
    }

    public void sendContainerAndContentsToPlayer(Container par1Container, List par2List)
    {
        this.sendSlotContents(par1Container, 0, par1Container.getSlot(0).getStack());
    }

    /**
     * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
     * contents of that slot. Args: Container, slot number, slot contents
     */
    public void sendSlotContents(Container par1Container, int par2, ItemStack par3ItemStack)
    {
        if (par2 == 0)
        {
            this.field_82326_p.setText(par3ItemStack == null ? "" : par3ItemStack.getDisplayName());
            this.field_82326_p.func_82265_c(par3ItemStack != null);

            if (par3ItemStack != null)
            {
                this.field_82327_o.func_82850_a(this.field_82326_p.getText());
                this.mc.thePlayer.sendQueue.addToSendQueue(new Packet250CustomPayload("MC|ItemName", this.field_82326_p.getText().getBytes()));
            }
        }
    }

    /**
     * Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress,
     * and enchanting level. Normally the first int identifies which variable to update, and the second contains the new
     * value. Both are truncated to shorts in non-local SMP.
     */
    public void sendProgressBarUpdate(Container par1Container, int par2, int par3) {}
}

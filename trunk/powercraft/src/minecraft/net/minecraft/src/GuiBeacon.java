package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiBeacon extends GuiContainer
{
    private TileEntityBeacon field_82323_o;
    private GuiBeaconButtonConfirm field_82322_p;
    private boolean field_82321_q;

    public GuiBeacon(InventoryPlayer par1, TileEntityBeacon par2)
    {
        super(new ContainerBeacon(par1, par2));
        this.field_82323_o = par2;
        this.xSize = 230;
        this.ySize = 219;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        this.controlList.add(this.field_82322_p = new GuiBeaconButtonConfirm(this, -1, this.guiLeft + 164, this.guiTop + 107));
        this.controlList.add(new GuiBeaconButtonCancel(this, -2, this.guiLeft + 190, this.guiTop + 107));
        this.field_82321_q = true;
        this.field_82322_p.enabled = false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();

        if (this.field_82321_q && this.field_82323_o.func_82130_k() >= 0)
        {
            this.field_82321_q = false;
            int var2;
            int var3;
            int var4;
            int var5;
            GuiBeaconButtonPower var6;

            for (int var1 = 0; var1 <= 2; ++var1)
            {
                var2 = TileEntityBeacon.field_82139_a[var1].length;
                var3 = var2 * 22 + (var2 - 1) * 2;

                for (var4 = 0; var4 < var2; ++var4)
                {
                    var5 = TileEntityBeacon.field_82139_a[var1][var4].id;
                    var6 = new GuiBeaconButtonPower(this, var1 << 8 | var5, this.guiLeft + 76 + var4 * 24 - var3 / 2, this.guiTop + 22 + var1 * 25, var5, var1);
                    this.controlList.add(var6);

                    if (var1 >= this.field_82323_o.func_82130_k())
                    {
                        var6.enabled = false;
                    }
                    else if (var5 == this.field_82323_o.func_82126_i())
                    {
                        var6.func_82254_b(true);
                    }
                }
            }

            byte var7 = 3;
            var2 = TileEntityBeacon.field_82139_a[var7].length + 1;
            var3 = var2 * 22 + (var2 - 1) * 2;

            for (var4 = 0; var4 < var2 - 1; ++var4)
            {
                var5 = TileEntityBeacon.field_82139_a[var7][var4].id;
                var6 = new GuiBeaconButtonPower(this, var7 << 8 | var5, this.guiLeft + 167 + var4 * 24 - var3 / 2, this.guiTop + 47, var5, var7);
                this.controlList.add(var6);

                if (var7 >= this.field_82323_o.func_82130_k())
                {
                    var6.enabled = false;
                }
                else if (var5 == this.field_82323_o.func_82132_j())
                {
                    var6.func_82254_b(true);
                }
            }

            if (this.field_82323_o.func_82126_i() > 0)
            {
                GuiBeaconButtonPower var8 = new GuiBeaconButtonPower(this, var7 << 8 | this.field_82323_o.func_82126_i(), this.guiLeft + 167 + (var2 - 1) * 24 - var3 / 2, this.guiTop + 47, this.field_82323_o.func_82126_i(), var7);
                this.controlList.add(var8);

                if (var7 >= this.field_82323_o.func_82130_k())
                {
                    var8.enabled = false;
                }
                else if (this.field_82323_o.func_82126_i() == this.field_82323_o.func_82132_j())
                {
                    var8.func_82254_b(true);
                }
            }
        }

        this.field_82322_p.enabled = this.field_82323_o.getStackInSlot(0) != null && this.field_82323_o.func_82126_i() > 0;
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.id == -2)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (par1GuiButton.id == -1)
        {
            String var2 = "MC|Beacon";
            ByteArrayOutputStream var3 = new ByteArrayOutputStream();
            DataOutputStream var4 = new DataOutputStream(var3);

            try
            {
                var4.writeInt(this.field_82323_o.func_82126_i());
                var4.writeInt(this.field_82323_o.func_82132_j());
                this.mc.getSendQueue().addToSendQueue(new Packet250CustomPayload(var2, var3.toByteArray()));
            }
            catch (Exception var6)
            {
                var6.printStackTrace();
            }

            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (par1GuiButton instanceof GuiBeaconButtonPower)
        {
            if (((GuiBeaconButtonPower)par1GuiButton).func_82255_b())
            {
                return;
            }

            int var7 = par1GuiButton.id;
            int var8 = var7 & 255;
            int var9 = var7 >> 8;

            if (var9 < 3)
            {
                this.field_82323_o.func_82128_d(var8);
            }
            else
            {
                this.field_82323_o.func_82127_e(var8);
            }

            this.controlList.clear();
            this.initGui();
            this.updateScreen();
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        RenderHelper.disableStandardItemLighting();
        this.drawCenteredString(this.fontRenderer, StatCollector.translateToLocal("tile.beacon.primary"), 62, 10, 14737632);
        this.drawCenteredString(this.fontRenderer, StatCollector.translateToLocal("tile.beacon.secondary"), 169, 10, 14737632);
        Iterator var3 = this.controlList.iterator();

        while (var3.hasNext())
        {
            GuiButton var4 = (GuiButton)var3.next();

            if (var4.func_82252_a())
            {
                var4.func_82251_b(par1 - this.guiLeft, par2 - this.guiTop);
                break;
            }
        }
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int var4 = this.mc.renderEngine.getTexture("/gui/beacon.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        itemRenderer.zLevel = 100.0F;
        itemRenderer.func_82406_b(this.fontRenderer, this.mc.renderEngine, new ItemStack(Item.emerald), var5 + 42, var6 + 109);
        itemRenderer.func_82406_b(this.fontRenderer, this.mc.renderEngine, new ItemStack(Item.diamond), var5 + 42 + 22, var6 + 109);
        itemRenderer.func_82406_b(this.fontRenderer, this.mc.renderEngine, new ItemStack(Item.ingotGold), var5 + 42 + 44, var6 + 109);
        itemRenderer.func_82406_b(this.fontRenderer, this.mc.renderEngine, new ItemStack(Item.ingotIron), var5 + 42 + 66, var6 + 109);
    }
}

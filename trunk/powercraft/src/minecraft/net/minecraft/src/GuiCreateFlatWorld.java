package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCreateFlatWorld extends GuiScreen
{
    private static RenderItem field_82282_a = new RenderItem();
    private final GuiCreateWorld field_82277_b;
    private FlatGeneratorInfo field_82279_c = FlatGeneratorInfo.func_82649_e();
    private String field_82276_d;
    private String field_82285_m;
    private String field_82283_n;
    private GuiCreateFlatWorldListSlot field_82284_o;
    private GuiButton field_82281_p;
    private GuiButton field_82280_q;
    private GuiButton field_82278_r;

    public GuiCreateFlatWorld(GuiCreateWorld par1, String par2Str)
    {
        this.field_82277_b = par1;
        this.func_82273_a(par2Str);
    }

    public String func_82275_e()
    {
        return this.field_82279_c.toString();
    }

    public void func_82273_a(String par1Str)
    {
        this.field_82279_c = FlatGeneratorInfo.func_82651_a(par1Str);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.controlList.clear();
        this.field_82276_d = StatCollector.translateToLocal("createWorld.customize.flat.title");
        this.field_82285_m = StatCollector.translateToLocal("createWorld.customize.flat.tile");
        this.field_82283_n = StatCollector.translateToLocal("createWorld.customize.flat.height");
        this.field_82284_o = new GuiCreateFlatWorldListSlot(this);
        this.controlList.add(this.field_82281_p = new GuiButton(2, this.width / 2 - 154, this.height - 52, 100, 20, StatCollector.translateToLocal("createWorld.customize.flat.addLayer") + " (NYI)"));
        this.controlList.add(this.field_82280_q = new GuiButton(3, this.width / 2 - 50, this.height - 52, 100, 20, StatCollector.translateToLocal("createWorld.customize.flat.editLayer") + " (NYI)"));
        this.controlList.add(this.field_82278_r = new GuiButton(4, this.width / 2 - 155, this.height - 52, 150, 20, StatCollector.translateToLocal("createWorld.customize.flat.removeLayer")));
        this.controlList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, StatCollector.translateToLocal("gui.done")));
        this.controlList.add(new GuiButton(5, this.width / 2 + 5, this.height - 52, 150, 20, StatCollector.translateToLocal("createWorld.customize.presets")));
        this.controlList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, StatCollector.translateToLocal("gui.cancel")));
        this.field_82281_p.drawButton = this.field_82280_q.drawButton = false;
        this.field_82279_c.func_82645_d();
        this.func_82270_g();
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        int var2 = this.field_82279_c.func_82650_c().size() - this.field_82284_o.field_82454_a - 1;

        if (par1GuiButton.id == 1)
        {
            this.mc.displayGuiScreen(this.field_82277_b);
        }
        else if (par1GuiButton.id == 0)
        {
            this.field_82277_b.field_82290_a = this.func_82275_e();
            this.mc.displayGuiScreen(this.field_82277_b);
        }
        else if (par1GuiButton.id == 5)
        {
            this.mc.displayGuiScreen(new GuiFlatPresets(this));
        }
        else if (par1GuiButton.id == 4 && this.func_82272_i())
        {
            this.field_82279_c.func_82650_c().remove(var2);
            this.field_82284_o.field_82454_a = Math.min(this.field_82284_o.field_82454_a, this.field_82279_c.func_82650_c().size() - 1);
        }

        this.field_82279_c.func_82645_d();
        this.func_82270_g();
    }

    public void func_82270_g()
    {
        boolean var1 = this.func_82272_i();
        this.field_82278_r.enabled = var1;
        this.field_82280_q.enabled = var1;
        this.field_82280_q.enabled = false;
        this.field_82281_p.enabled = false;
    }

    private boolean func_82272_i()
    {
        return this.field_82284_o.field_82454_a > -1 && this.field_82284_o.field_82454_a < this.field_82279_c.func_82650_c().size();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.field_82284_o.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRenderer, this.field_82276_d, this.width / 2, 8, 16777215);
        int var4 = this.width / 2 - 92 - 16;
        this.drawString(this.fontRenderer, this.field_82285_m, var4, 32, 16777215);
        this.drawString(this.fontRenderer, this.field_82283_n, var4 + 2 + 213 - this.fontRenderer.getStringWidth(this.field_82283_n), 32, 16777215);
        super.drawScreen(par1, par2, par3);
    }

    static RenderItem func_82274_h()
    {
        return field_82282_a;
    }

    static FlatGeneratorInfo func_82271_a(GuiCreateFlatWorld par0GuiCreateFlatWorld)
    {
        return par0GuiCreateFlatWorld.field_82279_c;
    }
}

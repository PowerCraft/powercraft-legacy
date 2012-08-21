package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

public class GuiSnooper extends GuiScreen
{
    private final GuiScreen field_74100_a;
    private final GameSettings field_74097_b;
    private final List field_74098_c = new ArrayList();
    private final List field_74096_d = new ArrayList();
    private String field_74103_m;
    private String[] field_74101_n;
    private GuiSnooperList field_74102_o;
    private GuiButton field_74099_p;

    public GuiSnooper(GuiScreen par1GuiScreen, GameSettings par2GameSettings)
    {
        this.field_74100_a = par1GuiScreen;
        this.field_74097_b = par2GameSettings;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.field_74103_m = StatCollector.translateToLocal("options.snooper.title");
        String var1 = StatCollector.translateToLocal("options.snooper.desc");
        ArrayList var2 = new ArrayList();
        Iterator var3 = this.fontRenderer.listFormattedStringToWidth(var1, this.width - 30).iterator();

        while (var3.hasNext())
        {
            String var4 = (String)var3.next();
            var2.add(var4);
        }

        this.field_74101_n = (String[])var2.toArray(new String[0]);
        this.field_74098_c.clear();
        this.field_74096_d.clear();
        this.controlList.add(this.field_74099_p = new GuiButton(1, this.width / 2 - 152, this.height - 30, 150, 20, this.field_74097_b.getKeyBinding(EnumOptions.SNOOPER_ENABLED)));
        this.controlList.add(new GuiButton(2, this.width / 2 + 2, this.height - 30, 150, 20, StatCollector.translateToLocal("gui.done")));
        boolean var6 = this.mc.getIntegratedServer() != null && this.mc.getIntegratedServer().func_80003_ah() != null;
        Iterator var7 = (new TreeMap(this.mc.getPlayerUsageSnooper().getCurrentStats())).entrySet().iterator();
        Entry var5;

        while (var7.hasNext())
        {
            var5 = (Entry)var7.next();
            this.field_74098_c.add((var6 ? "C " : "") + (String)var5.getKey());
            this.field_74096_d.add(this.fontRenderer.trimStringToWidth((String)var5.getValue(), this.width - 220));
        }

        if (var6)
        {
            var7 = (new TreeMap(this.mc.getIntegratedServer().func_80003_ah().getCurrentStats())).entrySet().iterator();

            while (var7.hasNext())
            {
                var5 = (Entry)var7.next();
                this.field_74098_c.add("S " + (String)var5.getKey());
                this.field_74096_d.add(this.fontRenderer.trimStringToWidth((String)var5.getValue(), this.width - 220));
            }
        }

        this.field_74102_o = new GuiSnooperList(this);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id == 2)
            {
                this.field_74097_b.saveOptions();
                this.field_74097_b.saveOptions();
                this.mc.displayGuiScreen(this.field_74100_a);
            }

            if (par1GuiButton.id == 1)
            {
                this.field_74097_b.setOptionValue(EnumOptions.SNOOPER_ENABLED, 1);
                this.field_74099_p.displayString = this.field_74097_b.getKeyBinding(EnumOptions.SNOOPER_ENABLED);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.field_74102_o.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRenderer, this.field_74103_m, this.width / 2, 8, 16777215);
        int var4 = 22;
        String[] var5 = this.field_74101_n;
        int var6 = var5.length;

        for (int var7 = 0; var7 < var6; ++var7)
        {
            String var8 = var5[var7];
            this.drawCenteredString(this.fontRenderer, var8, this.width / 2, var4, 8421504);
            var4 += this.fontRenderer.FONT_HEIGHT;
        }

        super.drawScreen(par1, par2, par3);
    }

    static List func_74095_a(GuiSnooper par0GuiSnooper)
    {
        return par0GuiSnooper.field_74098_c;
    }

    static List func_74094_b(GuiSnooper par0GuiSnooper)
    {
        return par0GuiSnooper.field_74096_d;
    }
}

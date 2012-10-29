package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiFlatPresets extends GuiScreen
{
    private static RenderItem field_82305_a = new RenderItem();
    private static final List field_82301_b = new ArrayList();
    private final GuiCreateFlatWorld field_82302_c;
    private String field_82300_d;
    private String field_82308_m;
    private String field_82306_n;
    private GuiFlatPresetsListSlot field_82307_o;
    private GuiButton field_82304_p;
    private GuiTextField field_82303_q;

    public GuiFlatPresets(GuiCreateFlatWorld par1)
    {
        this.field_82302_c = par1;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.controlList.clear();
        Keyboard.enableRepeatEvents(true);
        this.field_82300_d = StatCollector.translateToLocal("createWorld.customize.presets.title");
        this.field_82308_m = StatCollector.translateToLocal("createWorld.customize.presets.share");
        this.field_82306_n = StatCollector.translateToLocal("createWorld.customize.presets.list");
        this.field_82303_q = new GuiTextField(this.fontRenderer, 50, 40, this.width - 100, 20);
        this.field_82307_o = new GuiFlatPresetsListSlot(this);
        this.field_82303_q.setMaxStringLength(1230);
        this.field_82303_q.setText(this.field_82302_c.func_82275_e());
        this.controlList.add(this.field_82304_p = new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, StatCollector.translateToLocal("createWorld.customize.presets.select")));
        this.controlList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, StatCollector.translateToLocal("gui.cancel")));
        this.func_82296_g();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        this.field_82303_q.mouseClicked(par1, par2, par3);
        super.mouseClicked(par1, par2, par3);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (!this.field_82303_q.textboxKeyTyped(par1, par2))
        {
            super.keyTyped(par1, par2);
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.id == 0 && this.func_82293_j())
        {
            this.field_82302_c.func_82273_a(this.field_82303_q.getText());
            this.mc.displayGuiScreen(this.field_82302_c);
        }
        else if (par1GuiButton.id == 1)
        {
            this.mc.displayGuiScreen(this.field_82302_c);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.field_82307_o.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRenderer, this.field_82300_d, this.width / 2, 8, 16777215);
        this.drawString(this.fontRenderer, this.field_82308_m, 50, 30, 10526880);
        this.drawString(this.fontRenderer, this.field_82306_n, 50, 70, 10526880);
        this.field_82303_q.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.field_82303_q.updateCursorCounter();
        super.updateScreen();
    }

    public void func_82296_g()
    {
        boolean var1 = this.func_82293_j();
        this.field_82304_p.enabled = var1;
    }

    private boolean func_82293_j()
    {
        return this.field_82307_o.field_82459_a > -1 && this.field_82307_o.field_82459_a < field_82301_b.size() || this.field_82303_q.getText().length() > 1;
    }

    private static void func_82297_a(String par0Str, int par1, BiomeGenBase par2BiomeGenBase, FlatLayerInfo ... par3ArrayOfFlatLayerInfo)
    {
        func_82294_a(par0Str, par1, par2BiomeGenBase, (List)null, par3ArrayOfFlatLayerInfo);
    }

    private static void func_82294_a(String par0Str, int par1, BiomeGenBase par2BiomeGenBase, List par3List, FlatLayerInfo ... par4ArrayOfFlatLayerInfo)
    {
        FlatGeneratorInfo var5 = new FlatGeneratorInfo();

        for (int var6 = par4ArrayOfFlatLayerInfo.length - 1; var6 >= 0; --var6)
        {
            var5.func_82650_c().add(par4ArrayOfFlatLayerInfo[var6]);
        }

        var5.func_82647_a(par2BiomeGenBase.biomeID);
        var5.func_82645_d();

        if (par3List != null)
        {
            Iterator var8 = par3List.iterator();

            while (var8.hasNext())
            {
                String var7 = (String)var8.next();
                var5.func_82644_b().put(var7, new HashMap());
            }
        }

        field_82301_b.add(new GuiFlatPresetsItem(par1, par0Str, var5.toString()));
    }

    static RenderItem func_82299_h()
    {
        return field_82305_a;
    }

    static List func_82295_i()
    {
        return field_82301_b;
    }

    static GuiFlatPresetsListSlot func_82292_a(GuiFlatPresets par0GuiFlatPresets)
    {
        return par0GuiFlatPresets.field_82307_o;
    }

    static GuiTextField func_82298_b(GuiFlatPresets par0GuiFlatPresets)
    {
        return par0GuiFlatPresets.field_82303_q;
    }

    static
    {
        func_82294_a("Classic Flat", Block.grass.blockID, BiomeGenBase.plains, Arrays.asList(new String[] {"village"}), new FlatLayerInfo[] {new FlatLayerInfo(1, Block.grass.blockID), new FlatLayerInfo(2, Block.dirt.blockID), new FlatLayerInfo(1, Block.bedrock.blockID)});
        func_82294_a("Tunnelers\' Dream", Block.stone.blockID, BiomeGenBase.extremeHills, Arrays.asList(new String[] {"biome_1", "dungeon", "decoration", "stronghold", "mineshaft"}), new FlatLayerInfo[] {new FlatLayerInfo(1, Block.grass.blockID), new FlatLayerInfo(5, Block.dirt.blockID), new FlatLayerInfo(230, Block.stone.blockID), new FlatLayerInfo(1, Block.bedrock.blockID)});
        func_82294_a("Water World", Block.waterMoving.blockID, BiomeGenBase.plains, Arrays.asList(new String[] {"village", "biome_1"}), new FlatLayerInfo[] {new FlatLayerInfo(90, Block.waterStill.blockID), new FlatLayerInfo(5, Block.sand.blockID), new FlatLayerInfo(5, Block.dirt.blockID), new FlatLayerInfo(5, Block.stone.blockID), new FlatLayerInfo(1, Block.bedrock.blockID)});
        func_82294_a("Overworld", Block.tallGrass.blockID, BiomeGenBase.plains, Arrays.asList(new String[] {"village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon", "lake", "lava_lake"}), new FlatLayerInfo[] {new FlatLayerInfo(1, Block.grass.blockID), new FlatLayerInfo(3, Block.dirt.blockID), new FlatLayerInfo(59, Block.stone.blockID), new FlatLayerInfo(1, Block.bedrock.blockID)});
        func_82294_a("Snowy Kingdom", Block.snow.blockID, BiomeGenBase.icePlains, Arrays.asList(new String[] {"village", "biome_1"}), new FlatLayerInfo[] {new FlatLayerInfo(1, Block.snow.blockID), new FlatLayerInfo(1, Block.grass.blockID), new FlatLayerInfo(3, Block.dirt.blockID), new FlatLayerInfo(59, Block.stone.blockID), new FlatLayerInfo(1, Block.bedrock.blockID)});
        func_82294_a("Bottomless Pit", Item.feather.shiftedIndex, BiomeGenBase.plains, Arrays.asList(new String[] {"village", "biome_1"}), new FlatLayerInfo[] {new FlatLayerInfo(1, Block.grass.blockID), new FlatLayerInfo(3, Block.dirt.blockID), new FlatLayerInfo(2, Block.cobblestone.blockID)});
        func_82294_a("Desert", Block.sand.blockID, BiomeGenBase.desert, Arrays.asList(new String[] {"village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon"}), new FlatLayerInfo[] {new FlatLayerInfo(8, Block.sand.blockID), new FlatLayerInfo(52, Block.sandStone.blockID), new FlatLayerInfo(3, Block.stone.blockID), new FlatLayerInfo(1, Block.bedrock.blockID)});
        func_82297_a("Redstone Ready", Item.redstone.shiftedIndex, BiomeGenBase.desert, new FlatLayerInfo[] {new FlatLayerInfo(52, Block.sandStone.blockID), new FlatLayerInfo(3, Block.stone.blockID), new FlatLayerInfo(1, Block.bedrock.blockID)});
    }
}

package net.minecraft.src;

public class CreativeTabs
{
    public static final CreativeTabs[] field_78032_a = new CreativeTabs[12];
    public static final CreativeTabs field_78030_b = new CreativeTabBlock(0, "buildingBlocks");
    public static final CreativeTabs field_78031_c = new CreativeTabDeco(1, "decorations");
    public static final CreativeTabs field_78028_d = new CreativeTabRedstone(2, "redstone");
    public static final CreativeTabs field_78029_e = new CreativeTabTransport(3, "transportation");
    public static final CreativeTabs field_78026_f = new CreativeTabMisc(4, "misc");
    public static final CreativeTabs field_78027_g = (new CreativeTabSearch(5, "search")).func_78025_a("search.png");
    public static final CreativeTabs field_78039_h = new CreativeTabFood(6, "food");
    public static final CreativeTabs field_78040_i = new CreativeTabTools(7, "tools");
    public static final CreativeTabs field_78037_j = new CreativeTabCombat(8, "combat");
    public static final CreativeTabs field_78038_k = new CreativeTabBrewing(9, "brewing");
    public static final CreativeTabs field_78035_l = new CreativeTabMaterial(10, "materials");
    public static final CreativeTabs field_78036_m = (new CreativeTabInventory(11, "inventory")).func_78025_a("survival_inv.png").setNoScrollbar().setNoTitle();
    private final int tabIndex;
    private final String field_78034_o;

    /** Texture to use. */
    private String theTexture = "list_items.png";
    private boolean hasScrollbar = true;

    /** Whether to draw the title in the foreground of the creative GUI */
    private boolean drawTitle = true;

    public CreativeTabs(int par1, String par2Str)
    {
        this.tabIndex = par1;
        this.field_78034_o = par2Str;
        field_78032_a[par1] = this;
    }

    public CreativeTabs func_78025_a(String par1Str)
    {
        this.theTexture = par1Str;
        return this;
    }

    public CreativeTabs setNoTitle()
    {
        this.drawTitle = false;
        return this;
    }

    public CreativeTabs setNoScrollbar()
    {
        this.hasScrollbar = false;
        return this;
    }
}

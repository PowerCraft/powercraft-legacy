package net.minecraft.src;

public class ScreenChatOptions extends GuiScreen
{
    private static final EnumOptions[] field_73891_a = new EnumOptions[] {EnumOptions.CHAT_VISIBILITY, EnumOptions.CHAT_COLOR, EnumOptions.CHAT_LINKS, EnumOptions.CHAT_OPACITY, EnumOptions.CHAT_LINKS_PROMPT};
    private final GuiScreen field_73889_b;
    private final GameSettings field_73890_c;
    private String field_73888_d;

    public ScreenChatOptions(GuiScreen par1GuiScreen, GameSettings par2GameSettings)
    {
        this.field_73889_b = par1GuiScreen;
        this.field_73890_c = par2GameSettings;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        StringTranslate var1 = StringTranslate.getInstance();
        int var2 = 0;
        this.field_73888_d = var1.translateKey("options.chat.title");
        EnumOptions[] var3 = field_73891_a;
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5)
        {
            EnumOptions var6 = var3[var5];

            if (var6.getEnumFloat())
            {
                this.controlList.add(new GuiSlider(var6.returnEnumOrdinal(), this.width / 2 - 155 + var2 % 2 * 160, this.height / 6 + 24 * (var2 >> 1), var6, this.field_73890_c.getKeyBinding(var6), this.field_73890_c.getOptionFloatValue(var6)));
            }
            else
            {
                this.controlList.add(new GuiSmallButton(var6.returnEnumOrdinal(), this.width / 2 - 155 + var2 % 2 * 160, this.height / 6 + 24 * (var2 >> 1), var6, this.field_73890_c.getKeyBinding(var6)));
            }

            ++var2;
        }

        this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, var1.translateKey("gui.done")));
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id < 100 && par1GuiButton instanceof GuiSmallButton)
            {
                this.field_73890_c.setOptionValue(((GuiSmallButton)par1GuiButton).returnEnumOptions(), 1);
                par1GuiButton.displayString = this.field_73890_c.getKeyBinding(EnumOptions.getEnumOptions(par1GuiButton.id));
            }

            if (par1GuiButton.id == 200)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.field_73889_b);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.field_73888_d, this.width / 2, 20, 16777215);
        super.drawScreen(par1, par2, par3);
    }
}

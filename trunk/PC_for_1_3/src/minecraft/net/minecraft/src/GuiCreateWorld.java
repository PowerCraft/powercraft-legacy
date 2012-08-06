package net.minecraft.src;

import java.util.Random;
import org.lwjgl.input.Keyboard;

public class GuiCreateWorld extends GuiScreen
{
    private GuiScreen parentGuiScreen;
    private GuiTextField textboxWorldName;
    private GuiTextField textboxSeed;
    private String folderName;

    /** hardcore', 'creative' or 'survival */
    private String gameMode = "survival";
    private boolean field_73925_n = true;
    private boolean field_73926_o = false;
    private boolean field_73935_p = false;
    private boolean field_73934_q = false;
    private boolean field_73933_r = false;
    private boolean createClicked;

    /**
     * True if the extra options (Seed box, structure toggle button, world type button, etc.) are being shown
     */
    private boolean moreOptions;

    /** The GUIButton that you click to change game modes. */
    private GuiButton gameModeButton;

    /**
     * The GUIButton that you click to get to options like the seed when creating a world.
     */
    private GuiButton moreWorldOptions;

    /** The GuiButton in the 'More World Options' screen. Toggles ON/OFF */
    private GuiButton generateStructuresButton;
    private GuiButton field_73938_x;

    /**
     * the GUIButton in the more world options screen. It's currently greyed out and unused in minecraft 1.0.0
     */
    private GuiButton worldTypeButton;
    private GuiButton field_73936_z;

    /** The first line of text describing the currently selected game mode. */
    private String gameModeDescriptionLine1;

    /** The second line of text describing the currently selected game mode. */
    private String gameModeDescriptionLine2;

    /** The current textboxSeed text */
    private String seed;

    /** E.g. New World, Neue Welt, Nieuwe wereld, Neuvo Mundo */
    private String localizedNewWorldText;
    private int field_73916_E = 0;
    private static final String[] field_73917_F = new String[] {"CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};

    public GuiCreateWorld(GuiScreen par1GuiScreen)
    {
        this.parentGuiScreen = par1GuiScreen;
        this.seed = "";
        this.localizedNewWorldText = StatCollector.translateToLocal("selectWorld.newWorld");
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.textboxWorldName.updateCursorCounter();
        this.textboxSeed.updateCursorCounter();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        StringTranslate var1 = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, var1.translateKey("selectWorld.create")));
        this.controlList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, var1.translateKey("gui.cancel")));
        this.controlList.add(this.gameModeButton = new GuiButton(2, this.width / 2 - 75, 100, 150, 20, var1.translateKey("selectWorld.gameMode")));
        this.controlList.add(this.moreWorldOptions = new GuiButton(3, this.width / 2 - 75, 172, 150, 20, var1.translateKey("selectWorld.moreWorldOptions")));
        this.controlList.add(this.generateStructuresButton = new GuiButton(4, this.width / 2 - 155, 100, 150, 20, var1.translateKey("selectWorld.mapFeatures")));
        this.generateStructuresButton.drawButton = false;
        this.controlList.add(this.field_73938_x = new GuiButton(7, this.width / 2 + 5, 136, 150, 20, var1.translateKey("selectWorld.bonusItems")));
        this.field_73938_x.drawButton = false;
        this.controlList.add(this.worldTypeButton = new GuiButton(5, this.width / 2 + 5, 100, 150, 20, var1.translateKey("selectWorld.mapType")));
        this.worldTypeButton.drawButton = false;
        this.controlList.add(this.field_73936_z = new GuiButton(6, this.width / 2 - 155, 136, 150, 20, var1.translateKey("selectWorld.allowCommands")));
        this.field_73936_z.drawButton = false;
        this.textboxWorldName = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
        this.textboxWorldName.setFocused(true);
        this.textboxWorldName.setText(this.localizedNewWorldText);
        this.textboxSeed = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
        this.textboxSeed.setText(this.seed);
        this.makeUseableName();
        this.func_73914_h();
    }

    /**
     * Makes a the name for a world save folder based on your world name, replacing specific characters for _s and
     * appending -s to the end until a free name is available.
     */
    private void makeUseableName()
    {
        this.folderName = this.textboxWorldName.getText().trim();
        char[] var1 = ChatAllowedCharacters.invalidFilenameCharacters;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            char var4 = var1[var3];
            this.folderName = this.folderName.replace(var4, '_');
        }

        if (MathHelper.stringNullOrLengthZero(this.folderName))
        {
            this.folderName = "World";
        }

        this.folderName = func_73913_a(this.mc.getSaveLoader(), this.folderName);
    }

    private void func_73914_h()
    {
        StringTranslate var1 = StringTranslate.getInstance();
        this.gameModeButton.displayString = var1.translateKey("selectWorld.gameMode") + " " + var1.translateKey("selectWorld.gameMode." + this.gameMode);
        this.gameModeDescriptionLine1 = var1.translateKey("selectWorld.gameMode." + this.gameMode + ".line1");
        this.gameModeDescriptionLine2 = var1.translateKey("selectWorld.gameMode." + this.gameMode + ".line2");
        this.generateStructuresButton.displayString = var1.translateKey("selectWorld.mapFeatures") + " ";

        if (this.field_73925_n)
        {
            this.generateStructuresButton.displayString = this.generateStructuresButton.displayString + var1.translateKey("options.on");
        }
        else
        {
            this.generateStructuresButton.displayString = this.generateStructuresButton.displayString + var1.translateKey("options.off");
        }

        this.field_73938_x.displayString = var1.translateKey("selectWorld.bonusItems") + " ";

        if (this.field_73934_q && !this.field_73933_r)
        {
            this.field_73938_x.displayString = this.field_73938_x.displayString + var1.translateKey("options.on");
        }
        else
        {
            this.field_73938_x.displayString = this.field_73938_x.displayString + var1.translateKey("options.off");
        }

        this.worldTypeButton.displayString = var1.translateKey("selectWorld.mapType") + " " + var1.translateKey(WorldType.worldTypes[this.field_73916_E].getTranslateName());
        this.field_73936_z.displayString = var1.translateKey("selectWorld.allowCommands") + " ";

        if (this.field_73926_o && !this.field_73933_r)
        {
            this.field_73936_z.displayString = this.field_73936_z.displayString + var1.translateKey("options.on");
        }
        else
        {
            this.field_73936_z.displayString = this.field_73936_z.displayString + var1.translateKey("options.off");
        }
    }

    public static String func_73913_a(ISaveFormat par0ISaveFormat, String par1Str)
    {
        par1Str = par1Str.replaceAll("[\\./\"]", "_");
        String[] var2 = field_73917_F;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            String var5 = var2[var4];

            if (par1Str.equalsIgnoreCase(var5))
            {
                par1Str = "_" + par1Str + "_";
            }
        }

        while (par0ISaveFormat.getWorldInfo(par1Str) != null)
        {
            par1Str = par1Str + "-";
        }

        return par1Str;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id == 1)
            {
                this.mc.displayGuiScreen(this.parentGuiScreen);
            }
            else if (par1GuiButton.id == 0)
            {
                this.mc.displayGuiScreen((GuiScreen)null);

                if (this.createClicked)
                {
                    return;
                }

                this.createClicked = true;
                long var2 = (new Random()).nextLong();
                String var4 = this.textboxSeed.getText();

                if (!MathHelper.stringNullOrLengthZero(var4))
                {
                    try
                    {
                        long var5 = Long.parseLong(var4);

                        if (var5 != 0L)
                        {
                            var2 = var5;
                        }
                    }
                    catch (NumberFormatException var7)
                    {
                        var2 = (long)var4.hashCode();
                    }
                }

                EnumGameType var9 = EnumGameType.getByName(this.gameMode);
                WorldSettings var6 = new WorldSettings(var2, var9, this.field_73925_n, this.field_73933_r, WorldType.worldTypes[this.field_73916_E]);

                if (this.field_73934_q && !this.field_73933_r)
                {
                    var6.enableBonusChest();
                }

                if (this.field_73926_o && !this.field_73933_r)
                {
                    var6.enableCommands();
                }

                this.mc.launchIntegratedServer(this.folderName, this.textboxWorldName.getText().trim(), var6);
            }
            else if (par1GuiButton.id == 3)
            {
                this.moreOptions = !this.moreOptions;
                this.gameModeButton.drawButton = !this.moreOptions;
                this.generateStructuresButton.drawButton = this.moreOptions;
                this.field_73938_x.drawButton = this.moreOptions;
                this.worldTypeButton.drawButton = this.moreOptions;
                this.field_73936_z.drawButton = this.moreOptions;
                StringTranslate var8;

                if (this.moreOptions)
                {
                    var8 = StringTranslate.getInstance();
                    this.moreWorldOptions.displayString = var8.translateKey("gui.done");
                }
                else
                {
                    var8 = StringTranslate.getInstance();
                    this.moreWorldOptions.displayString = var8.translateKey("selectWorld.moreWorldOptions");
                }
            }
            else if (par1GuiButton.id == 2)
            {
                if (this.gameMode.equals("survival"))
                {
                    if (!this.field_73935_p)
                    {
                        this.field_73926_o = false;
                    }

                    this.field_73933_r = false;
                    this.gameMode = "hardcore";
                    this.field_73933_r = true;
                    this.field_73936_z.enabled = false;
                    this.field_73938_x.enabled = false;
                    this.func_73914_h();
                }
                else if (this.gameMode.equals("hardcore"))
                {
                    if (!this.field_73935_p)
                    {
                        this.field_73926_o = true;
                    }

                    this.field_73933_r = false;
                    this.gameMode = "creative";
                    this.func_73914_h();
                    this.field_73933_r = false;
                    this.field_73936_z.enabled = true;
                    this.field_73938_x.enabled = true;
                }
                else
                {
                    if (!this.field_73935_p)
                    {
                        this.field_73926_o = false;
                    }

                    this.gameMode = "survival";
                    this.func_73914_h();
                    this.field_73936_z.enabled = true;
                    this.field_73938_x.enabled = true;
                    this.field_73933_r = false;
                }

                this.func_73914_h();
            }
            else if (par1GuiButton.id == 4)
            {
                this.field_73925_n = !this.field_73925_n;
                this.func_73914_h();
            }
            else if (par1GuiButton.id == 7)
            {
                this.field_73934_q = !this.field_73934_q;
                this.func_73914_h();
            }
            else if (par1GuiButton.id == 5)
            {
                ++this.field_73916_E;

                if (this.field_73916_E >= WorldType.worldTypes.length)
                {
                    this.field_73916_E = 0;
                }

                while (WorldType.worldTypes[this.field_73916_E] == null || !WorldType.worldTypes[this.field_73916_E].getCanBeCreated())
                {
                    ++this.field_73916_E;

                    if (this.field_73916_E >= WorldType.worldTypes.length)
                    {
                        this.field_73916_E = 0;
                    }
                }

                this.func_73914_h();
            }
            else if (par1GuiButton.id == 6)
            {
                this.field_73935_p = true;
                this.field_73926_o = !this.field_73926_o;
                this.func_73914_h();
            }
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (this.textboxWorldName.isFocused() && !this.moreOptions)
        {
            this.textboxWorldName.textboxKeyTyped(par1, par2);
            this.localizedNewWorldText = this.textboxWorldName.getText();
        }
        else if (this.textboxSeed.isFocused() && this.moreOptions)
        {
            this.textboxSeed.textboxKeyTyped(par1, par2);
            this.seed = this.textboxSeed.getText();
        }

        if (par1 == 13)
        {
            this.actionPerformed((GuiButton)this.controlList.get(0));
        }

        ((GuiButton)this.controlList.get(0)).enabled = this.textboxWorldName.getText().length() > 0;
        this.makeUseableName();
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);

        if (this.moreOptions)
        {
            this.textboxSeed.mouseClicked(par1, par2, par3);
        }
        else
        {
            this.textboxWorldName.mouseClicked(par1, par2, par3);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        StringTranslate var4 = StringTranslate.getInstance();
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, var4.translateKey("selectWorld.create"), this.width / 2, 20, 16777215);

        if (this.moreOptions)
        {
            this.drawString(this.fontRenderer, var4.translateKey("selectWorld.enterSeed"), this.width / 2 - 100, 47, 10526880);
            this.drawString(this.fontRenderer, var4.translateKey("selectWorld.seedInfo"), this.width / 2 - 100, 85, 10526880);
            this.drawString(this.fontRenderer, var4.translateKey("selectWorld.mapFeatures.info"), this.width / 2 - 150, 122, 10526880);
            this.drawString(this.fontRenderer, var4.translateKey("selectWorld.allowCommands.info"), this.width / 2 - 150, 157, 10526880);
            this.textboxSeed.drawTextBox();
        }
        else
        {
            this.drawString(this.fontRenderer, var4.translateKey("selectWorld.enterName"), this.width / 2 - 100, 47, 10526880);
            this.drawString(this.fontRenderer, var4.translateKey("selectWorld.resultFolder") + " " + this.folderName, this.width / 2 - 100, 85, 10526880);
            this.textboxWorldName.drawTextBox();
            this.drawString(this.fontRenderer, this.gameModeDescriptionLine1, this.width / 2 - 100, 122, 10526880);
            this.drawString(this.fontRenderer, this.gameModeDescriptionLine2, this.width / 2 - 100, 134, 10526880);
        }

        super.drawScreen(par1, par2, par3);
    }
}

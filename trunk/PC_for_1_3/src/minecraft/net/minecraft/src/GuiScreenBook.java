package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiScreenBook extends GuiScreen
{
    private final EntityPlayer field_74169_a;
    private final ItemStack field_74167_b;
    private final boolean field_74168_c;
    private boolean field_74166_d;
    private boolean field_74172_m;
    private int field_74170_n;
    private int field_74171_o = 192;
    private int field_74180_p = 192;
    private int field_74179_q = 1;
    private int field_74178_r;
    private NBTTagList field_74177_s;
    private String field_74176_t = "";
    private GuiButtonNextPage field_74175_u;
    private GuiButtonNextPage field_74174_v;
    private GuiButton field_74173_w;
    private GuiButton field_74183_x;
    private GuiButton field_74182_y;
    private GuiButton field_74181_z;

    public GuiScreenBook(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack, boolean par3)
    {
        this.field_74169_a = par1EntityPlayer;
        this.field_74167_b = par2ItemStack;
        this.field_74168_c = par3;

        if (par2ItemStack.hasTagCompound())
        {
            NBTTagCompound var4 = par2ItemStack.getTagCompound();
            this.field_74177_s = var4.getTagList("pages");

            if (this.field_74177_s != null)
            {
                this.field_74177_s = (NBTTagList)this.field_74177_s.copy();
                this.field_74179_q = this.field_74177_s.tagCount();

                if (this.field_74179_q < 1)
                {
                    this.field_74179_q = 1;
                }
            }
        }

        if (this.field_74177_s == null && par3)
        {
            this.field_74177_s = new NBTTagList("pages");
            this.field_74177_s.appendTag(new NBTTagString("1", ""));
            this.field_74179_q = 1;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ++this.field_74170_n;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.controlList.clear();
        Keyboard.enableRepeatEvents(true);

        if (this.field_74168_c)
        {
            this.controlList.add(this.field_74183_x = new GuiButton(3, this.width / 2 - 100, 4 + this.field_74180_p, 98, 20, StatCollector.translateToLocal("book.signButton")));
            this.controlList.add(this.field_74173_w = new GuiButton(0, this.width / 2 + 2, 4 + this.field_74180_p, 98, 20, StatCollector.translateToLocal("gui.done")));
            this.controlList.add(this.field_74182_y = new GuiButton(5, this.width / 2 - 100, 4 + this.field_74180_p, 98, 20, StatCollector.translateToLocal("book.finalizeButton")));
            this.controlList.add(this.field_74181_z = new GuiButton(4, this.width / 2 + 2, 4 + this.field_74180_p, 98, 20, StatCollector.translateToLocal("gui.cancel")));
        }
        else
        {
            this.controlList.add(this.field_74173_w = new GuiButton(0, this.width / 2 - 100, 4 + this.field_74180_p, 200, 20, StatCollector.translateToLocal("gui.done")));
        }

        int var1 = (this.width - this.field_74171_o) / 2;
        byte var2 = 2;
        this.controlList.add(this.field_74175_u = new GuiButtonNextPage(1, var1 + 120, var2 + 154, true));
        this.controlList.add(this.field_74174_v = new GuiButtonNextPage(2, var1 + 38, var2 + 154, false));
        this.func_74161_g();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    private void func_74161_g()
    {
        this.field_74175_u.drawButton = !this.field_74172_m && (this.field_74178_r < this.field_74179_q - 1 || this.field_74168_c);
        this.field_74174_v.drawButton = !this.field_74172_m && this.field_74178_r > 0;
        this.field_74173_w.drawButton = !this.field_74168_c || !this.field_74172_m;

        if (this.field_74168_c)
        {
            this.field_74183_x.drawButton = !this.field_74172_m;
            this.field_74181_z.drawButton = this.field_74172_m;
            this.field_74182_y.drawButton = this.field_74172_m;
            this.field_74182_y.enabled = this.field_74176_t.trim().length() > 0;
        }
    }

    private void func_74163_a(boolean par1)
    {
        if (this.field_74168_c && this.field_74166_d)
        {
            if (this.field_74177_s != null)
            {
                while (this.field_74177_s.tagCount() > 1)
                {
                    NBTTagString var2 = (NBTTagString)this.field_74177_s.tagAt(this.field_74177_s.tagCount() - 1);

                    if (var2.data != null && var2.data.length() != 0)
                    {
                        break;
                    }

                    this.field_74177_s.func_74744_a(this.field_74177_s.tagCount() - 1);
                }

                if (this.field_74167_b.hasTagCompound())
                {
                    NBTTagCompound var7 = this.field_74167_b.getTagCompound();
                    var7.setTag("pages", this.field_74177_s);
                }
                else
                {
                    this.field_74167_b.func_77983_a("pages", this.field_74177_s);
                }

                String var8 = "MC|BEdit";

                if (par1)
                {
                    var8 = "MC|BSign";
                    this.field_74167_b.func_77983_a("author", new NBTTagString("author", this.field_74169_a.username));
                    this.field_74167_b.func_77983_a("title", new NBTTagString("title", this.field_74176_t.trim()));
                    this.field_74167_b.itemID = Item.writtenBook.shiftedIndex;
                }

                ByteArrayOutputStream var3 = new ByteArrayOutputStream();
                DataOutputStream var4 = new DataOutputStream(var3);

                try
                {
                    Packet.writeItemStack(this.field_74167_b, var4);
                    this.mc.getSendQueue().addToSendQueue(new Packet250CustomPayload(var8, var3.toByteArray()));
                }
                catch (Exception var6)
                {
                    var6.printStackTrace();
                }
            }
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id == 0)
            {
                this.mc.displayGuiScreen((GuiScreen)null);
                this.func_74163_a(false);
            }
            else if (par1GuiButton.id == 3 && this.field_74168_c)
            {
                this.field_74172_m = true;
            }
            else if (par1GuiButton.id == 1)
            {
                if (this.field_74178_r < this.field_74179_q - 1)
                {
                    ++this.field_74178_r;
                }
                else if (this.field_74168_c)
                {
                    this.func_74165_h();

                    if (this.field_74178_r < this.field_74179_q - 1)
                    {
                        ++this.field_74178_r;
                    }
                }
            }
            else if (par1GuiButton.id == 2)
            {
                if (this.field_74178_r > 0)
                {
                    --this.field_74178_r;
                }
            }
            else if (par1GuiButton.id == 5 && this.field_74172_m)
            {
                this.func_74163_a(true);
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            else if (par1GuiButton.id == 4 && this.field_74172_m)
            {
                this.field_74172_m = false;
            }

            this.func_74161_g();
        }
    }

    private void func_74165_h()
    {
        if (this.field_74177_s != null && this.field_74177_s.tagCount() < 50)
        {
            this.field_74177_s.appendTag(new NBTTagString("" + (this.field_74179_q + 1), ""));
            ++this.field_74179_q;
            this.field_74166_d = true;
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);

        if (this.field_74168_c)
        {
            if (this.field_74172_m)
            {
                this.func_74162_c(par1, par2);
            }
            else
            {
                this.func_74164_b(par1, par2);
            }
        }
    }

    private void func_74164_b(char par1, int par2)
    {
        switch (par1)
        {
            case 22:
                this.func_74160_b(GuiScreen.getClipboardString());
                return;

            default:
                switch (par2)
                {
                    case 14:
                        String var3 = this.func_74158_i();

                        if (var3.length() > 0)
                        {
                            this.func_74159_a(var3.substring(0, var3.length() - 1));
                        }

                        return;

                    case 28:
                        this.func_74160_b("\n");
                        return;

                    default:
                        if (ChatAllowedCharacters.isAllowedCharacter(par1))
                        {
                            this.func_74160_b(Character.toString(par1));
                        }
                }
        }
    }

    private void func_74162_c(char par1, int par2)
    {
        switch (par2)
        {
            case 14:
                if (this.field_74176_t.length() > 0)
                {
                    this.field_74176_t = this.field_74176_t.substring(0, this.field_74176_t.length() - 1);
                    this.func_74161_g();
                }

                return;

            case 28:
                if (this.field_74176_t.length() > 0)
                {
                    this.func_74163_a(true);
                    this.mc.displayGuiScreen((GuiScreen)null);
                }

                return;

            default:
                if (this.field_74176_t.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(par1))
                {
                    this.field_74176_t = this.field_74176_t + Character.toString(par1);
                    this.func_74161_g();
                    this.field_74166_d = true;
                }
        }
    }

    private String func_74158_i()
    {
        if (this.field_74177_s != null && this.field_74178_r >= 0 && this.field_74178_r < this.field_74177_s.tagCount())
        {
            NBTTagString var1 = (NBTTagString)this.field_74177_s.tagAt(this.field_74178_r);
            return var1.toString();
        }
        else
        {
            return "";
        }
    }

    private void func_74159_a(String par1Str)
    {
        if (this.field_74177_s != null && this.field_74178_r >= 0 && this.field_74178_r < this.field_74177_s.tagCount())
        {
            NBTTagString var2 = (NBTTagString)this.field_74177_s.tagAt(this.field_74178_r);
            var2.data = par1Str;
            this.field_74166_d = true;
        }
    }

    private void func_74160_b(String par1Str)
    {
        String var2 = this.func_74158_i();
        String var3 = var2 + par1Str;
        int var4 = this.fontRenderer.splitStringWidth(var3 + "\u00a70_", 118);

        if (var4 <= 118 && var3.length() < 256)
        {
            this.func_74159_a(var3);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        int var4 = this.mc.renderEngine.getTexture("/gui/book.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        int var5 = (this.width - this.field_74171_o) / 2;
        byte var6 = 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.field_74171_o, this.field_74180_p);
        String var7;
        String var8;
        int var9;

        if (this.field_74172_m)
        {
            var7 = this.field_74176_t;

            if (this.field_74168_c)
            {
                if (this.field_74170_n / 6 % 2 == 0)
                {
                    var7 = var7 + "\u00a70_";
                }
                else
                {
                    var7 = var7 + "\u00a77_";
                }
            }

            var8 = StatCollector.translateToLocal("book.editTitle");
            var9 = this.fontRenderer.getStringWidth(var8);
            this.fontRenderer.drawString(var8, var5 + 36 + (116 - var9) / 2, var6 + 16 + 16, 0);
            int var10 = this.fontRenderer.getStringWidth(var7);
            this.fontRenderer.drawString(var7, var5 + 36 + (116 - var10) / 2, var6 + 48, 0);
            String var11 = String.format(StatCollector.translateToLocal("book.byAuthor"), new Object[] {this.field_74169_a.username});
            int var12 = this.fontRenderer.getStringWidth(var11);
            this.fontRenderer.drawString("\u00a78" + var11, var5 + 36 + (116 - var12) / 2, var6 + 48 + 10, 0);
            String var13 = StatCollector.translateToLocal("book.finalizeWarning");
            this.fontRenderer.drawSplitString(var13, var5 + 36, var6 + 80, 116, 0);
        }
        else
        {
            var7 = String.format(StatCollector.translateToLocal("book.pageIndicator"), new Object[] {Integer.valueOf(this.field_74178_r + 1), Integer.valueOf(this.field_74179_q)});
            var8 = "";

            if (this.field_74177_s != null && this.field_74178_r >= 0 && this.field_74178_r < this.field_74177_s.tagCount())
            {
                NBTTagString var14 = (NBTTagString)this.field_74177_s.tagAt(this.field_74178_r);
                var8 = var14.toString();
            }

            if (this.field_74168_c)
            {
                if (this.fontRenderer.getBidiFlag())
                {
                    var8 = var8 + "_";
                }
                else if (this.field_74170_n / 6 % 2 == 0)
                {
                    var8 = var8 + "\u00a70_";
                }
                else
                {
                    var8 = var8 + "\u00a77_";
                }
            }

            var9 = this.fontRenderer.getStringWidth(var7);
            this.fontRenderer.drawString(var7, var5 - var9 + this.field_74171_o - 44, var6 + 16, 0);
            this.fontRenderer.drawSplitString(var8, var5 + 36, var6 + 16 + 16, 116, 0);
        }

        super.drawScreen(par1, par2, par3);
    }
}

package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiCommandBlock extends GuiScreen
{
    private GuiTextField field_82318_a;
    private final TileEntityCommandBlock field_82317_b;

    public GuiCommandBlock(TileEntityCommandBlock par1)
    {
        this.field_82317_b = par1;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.field_82318_a.updateCursorCounter();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        StringTranslate var1 = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("gui.done")));
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
        this.field_82318_a = new GuiTextField(this.fontRenderer, this.width / 2 - 150, 60, 300, 20);
        this.field_82318_a.setMaxStringLength(32767);
        this.field_82318_a.setFocused(true);
        this.field_82318_a.setText(this.field_82317_b.func_82353_c());
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
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            else if (par1GuiButton.id == 0)
            {
                String var2 = "MC|AdvCdm";
                ByteArrayOutputStream var3 = new ByteArrayOutputStream();
                DataOutputStream var4 = new DataOutputStream(var3);

                try
                {
                    var4.writeInt(this.field_82317_b.xCoord);
                    var4.writeInt(this.field_82317_b.yCoord);
                    var4.writeInt(this.field_82317_b.zCoord);
                    Packet.writeString(this.field_82318_a.getText(), var4);
                    this.mc.getSendQueue().addToSendQueue(new Packet250CustomPayload(var2, var3.toByteArray()));
                }
                catch (Exception var6)
                {
                    var6.printStackTrace();
                }

                this.mc.displayGuiScreen((GuiScreen)null);
            }
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        this.field_82318_a.textboxKeyTyped(par1, par2);
        ((GuiButton)this.controlList.get(0)).enabled = this.field_82318_a.getText().trim().length() > 0;

        if (par1 == 13)
        {
            this.actionPerformed((GuiButton)this.controlList.get(0));
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        this.field_82318_a.mouseClicked(par1, par2, par3);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        StringTranslate var4 = StringTranslate.getInstance();
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, var4.translateKey("advMode.setCommand"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
        this.drawString(this.fontRenderer, var4.translateKey("advMode.command"), this.width / 2 - 150, 47, 10526880);
        this.drawString(this.fontRenderer, var4.translateKey("advMode.nearestPlayer"), this.width / 2 - 150, 97, 10526880);
        this.drawString(this.fontRenderer, var4.translateKey("advMode.randomPlayer"), this.width / 2 - 150, 108, 10526880);
        this.drawString(this.fontRenderer, var4.translateKey("advMode.allPlayers"), this.width / 2 - 150, 119, 10526880);
        this.field_82318_a.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }
}

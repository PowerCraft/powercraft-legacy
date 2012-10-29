package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiMultiplayer extends GuiScreen
{
    /** Number of outstanding ThreadPollServers threads */
    private static int threadsPending = 0;

    /** Lock object for use with synchronized() */
    private static Object lock = new Object();

    /**
     * A reference to the screen object that created this. Used for navigating between screens.
     */
    private GuiScreen parentScreen;

    /** Slot container for the server list */
    private GuiSlotServer serverSlotContainer;
    private ServerList internetServerList;

    /** Index of the currently selected server */
    private int selectedServer = -1;

    /** The 'Edit' button */
    private GuiButton buttonEdit;

    /** The 'Join Server' button */
    private GuiButton buttonSelect;

    /** The 'Delete' button */
    private GuiButton buttonDelete;

    /** The 'Delete' button was clicked */
    private boolean deleteClicked = false;

    /** The 'Add server' button was clicked */
    private boolean addClicked = false;

    /** The 'Edit' button was clicked */
    private boolean editClicked = false;

    /** The 'Direct Connect' button was clicked */
    private boolean directClicked = false;

    /** This GUI's lag tooltip text or null if no lag icon is being hovered. */
    private String lagTooltip = null;

    /** Instance of ServerData. */
    private ServerData theServerData = null;
    private LanServerList localNetworkServerList;
    private ThreadLanServerFind localServerFindThread;
    private int field_74039_z;
    private boolean field_74024_A;
    private List field_74026_B = Collections.emptyList();

    public GuiMultiplayer(GuiScreen par1GuiScreen)
    {
        this.parentScreen = par1GuiScreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.controlList.clear();

        if (!this.field_74024_A)
        {
            this.field_74024_A = true;
            this.internetServerList = new ServerList(this.mc);
            this.internetServerList.loadServerList();
            this.localNetworkServerList = new LanServerList();

            try
            {
                this.localServerFindThread = new ThreadLanServerFind(this.localNetworkServerList);
                this.localServerFindThread.start();
            }
            catch (Exception var2)
            {
                System.out.println("Unable to start LAN server detection: " + var2.getMessage());
            }

            this.serverSlotContainer = new GuiSlotServer(this);
        }
        else
        {
            this.serverSlotContainer.func_77207_a(this.width, this.height, 32, this.height - 64);
        }

        this.initGuiControls();
    }

    /**
     * Populate the GuiScreen controlList
     */
    public void initGuiControls()
    {
        StringTranslate var1 = StringTranslate.getInstance();
        this.controlList.add(this.buttonEdit = new GuiButton(7, this.width / 2 - 154, this.height - 28, 70, 20, var1.translateKey("selectServer.edit")));
        this.controlList.add(this.buttonDelete = new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, var1.translateKey("selectServer.delete")));
        this.controlList.add(this.buttonSelect = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, var1.translateKey("selectServer.select")));
        this.controlList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, var1.translateKey("selectServer.direct")));
        this.controlList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, var1.translateKey("selectServer.add")));
        this.controlList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 70, 20, var1.translateKey("selectServer.refresh")));
        this.controlList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 75, 20, var1.translateKey("gui.cancel")));
        boolean var2 = this.selectedServer >= 0 && this.selectedServer < this.serverSlotContainer.getSize();
        this.buttonSelect.enabled = var2;
        this.buttonEdit.enabled = var2;
        this.buttonDelete.enabled = var2;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ++this.field_74039_z;

        if (this.localNetworkServerList.func_77553_a())
        {
            this.field_74026_B = this.localNetworkServerList.func_77554_c();
            this.localNetworkServerList.func_77552_b();
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);

        if (this.localServerFindThread != null)
        {
            this.localServerFindThread.interrupt();
            this.localServerFindThread = null;
        }
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
                String var2 = this.internetServerList.getServerData(this.selectedServer).serverName;

                if (var2 != null)
                {
                    this.deleteClicked = true;
                    StringTranslate var3 = StringTranslate.getInstance();
                    String var4 = var3.translateKey("selectServer.deleteQuestion");
                    String var5 = "\'" + var2 + "\' " + var3.translateKey("selectServer.deleteWarning");
                    String var6 = var3.translateKey("selectServer.deleteButton");
                    String var7 = var3.translateKey("gui.cancel");
                    GuiYesNo var8 = new GuiYesNo(this, var4, var5, var6, var7, this.selectedServer);
                    this.mc.displayGuiScreen(var8);
                }
            }
            else if (par1GuiButton.id == 1)
            {
                this.joinServer(this.selectedServer);
            }
            else if (par1GuiButton.id == 4)
            {
                this.directClicked = true;
                this.mc.displayGuiScreen(new GuiScreenServerList(this, this.theServerData = new ServerData(StatCollector.translateToLocal("selectServer.defaultName"), "")));
            }
            else if (par1GuiButton.id == 3)
            {
                this.addClicked = true;
                this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.theServerData = new ServerData(StatCollector.translateToLocal("selectServer.defaultName"), "")));
            }
            else if (par1GuiButton.id == 7)
            {
                this.editClicked = true;
                ServerData var9 = this.internetServerList.getServerData(this.selectedServer);
                this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.theServerData = new ServerData(var9.serverName, var9.serverIP)));
            }
            else if (par1GuiButton.id == 0)
            {
                this.mc.displayGuiScreen(this.parentScreen);
            }
            else if (par1GuiButton.id == 8)
            {
                this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
            }
            else
            {
                this.serverSlotContainer.actionPerformed(par1GuiButton);
            }
        }
    }

    public void confirmClicked(boolean par1, int par2)
    {
        if (this.deleteClicked)
        {
            this.deleteClicked = false;

            if (par1)
            {
                this.internetServerList.removeServerData(par2);
                this.internetServerList.saveServerList();
                this.selectedServer = -1;
            }

            this.mc.displayGuiScreen(this);
        }
        else if (this.directClicked)
        {
            this.directClicked = false;

            if (par1)
            {
                this.func_74002_a(this.theServerData);
            }
            else
            {
                this.mc.displayGuiScreen(this);
            }
        }
        else if (this.addClicked)
        {
            this.addClicked = false;

            if (par1)
            {
                this.internetServerList.addServerData(this.theServerData);
                this.internetServerList.saveServerList();
                this.selectedServer = -1;
            }

            this.mc.displayGuiScreen(this);
        }
        else if (this.editClicked)
        {
            this.editClicked = false;

            if (par1)
            {
                ServerData var3 = this.internetServerList.getServerData(this.selectedServer);
                var3.serverName = this.theServerData.serverName;
                var3.serverIP = this.theServerData.serverIP;
                this.internetServerList.saveServerList();
            }

            this.mc.displayGuiScreen(this);
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        int var3 = this.selectedServer;

        if (par2 == 59)
        {
            this.mc.gameSettings.hideServerAddress = !this.mc.gameSettings.hideServerAddress;
            this.mc.gameSettings.saveOptions();
        }
        else
        {
            if (isShiftKeyDown() && par2 == 200)
            {
                if (var3 > 0 && var3 < this.internetServerList.countServers())
                {
                    this.internetServerList.swapServers(var3, var3 - 1);
                    --this.selectedServer;

                    if (var3 < this.internetServerList.countServers() - 1)
                    {
                        this.serverSlotContainer.func_77208_b(-this.serverSlotContainer.slotHeight);
                    }
                }
            }
            else if (isShiftKeyDown() && par2 == 208)
            {
                if (var3 < this.internetServerList.countServers() - 1)
                {
                    this.internetServerList.swapServers(var3, var3 + 1);
                    ++this.selectedServer;

                    if (var3 > 0)
                    {
                        this.serverSlotContainer.func_77208_b(this.serverSlotContainer.slotHeight);
                    }
                }
            }
            else if (par1 == 13)
            {
                this.actionPerformed((GuiButton)this.controlList.get(2));
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.lagTooltip = null;
        StringTranslate var4 = StringTranslate.getInstance();
        this.drawDefaultBackground();
        this.serverSlotContainer.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRenderer, var4.translateKey("multiplayer.title"), this.width / 2, 20, 16777215);
        super.drawScreen(par1, par2, par3);

        if (this.lagTooltip != null)
        {
            this.func_74007_a(this.lagTooltip, par1, par2);
        }
    }

    /**
     * Join server by slot index
     */
    private void joinServer(int par1)
    {
        if (par1 < this.internetServerList.countServers())
        {
            this.func_74002_a(this.internetServerList.getServerData(par1));
        }
        else
        {
            par1 -= this.internetServerList.countServers();

            if (par1 < this.field_74026_B.size())
            {
                LanServer var2 = (LanServer)this.field_74026_B.get(par1);
                this.func_74002_a(new ServerData(var2.func_77487_a(), var2.func_77488_b()));
            }
        }
    }

    private void func_74002_a(ServerData par1ServerData)
    {
        this.mc.displayGuiScreen(new GuiConnecting(this.mc, par1ServerData));
    }

    private void func_74017_b(ServerData par1ServerData) throws IOException
    {
        ServerAddress var2 = ServerAddress.func_78860_a(par1ServerData.serverIP);
        Socket var3 = null;
        DataInputStream var4 = null;
        DataOutputStream var5 = null;

        try
        {
            var3 = new Socket();
            var3.setSoTimeout(3000);
            var3.setTcpNoDelay(true);
            var3.setTrafficClass(18);
            var3.connect(new InetSocketAddress(var2.getIP(), var2.getPort()), 3000);
            var4 = new DataInputStream(var3.getInputStream());
            var5 = new DataOutputStream(var3.getOutputStream());
            var5.write(254);

            if (var4.read() != 255)
            {
                throw new IOException("Bad message");
            }

            String var6 = Packet.readString(var4, 256);
            char[] var7 = var6.toCharArray();

            for (int var8 = 0; var8 < var7.length; ++var8)
            {
                if (var7[var8] != 167 && ChatAllowedCharacters.allowedCharacters.indexOf(var7[var8]) < 0)
                {
                    var7[var8] = 63;
                }
            }

            var6 = new String(var7);
            String[] var27 = var6.split("\u00a7");
            var6 = var27[0];
            int var9 = -1;
            int var10 = -1;

            try
            {
                var9 = Integer.parseInt(var27[1]);
                var10 = Integer.parseInt(var27[2]);
            }
            catch (Exception var25)
            {
                ;
            }

            par1ServerData.serverMOTD = "\u00a77" + var6;

            if (var9 >= 0 && var10 > 0)
            {
                par1ServerData.populationInfo = "\u00a77" + var9 + "\u00a78/\u00a77" + var10;
            }
            else
            {
                par1ServerData.populationInfo = "\u00a78???";
            }
        }
        finally
        {
            try
            {
                if (var4 != null)
                {
                    var4.close();
                }
            }
            catch (Throwable var24)
            {
                ;
            }

            try
            {
                if (var5 != null)
                {
                    var5.close();
                }
            }
            catch (Throwable var23)
            {
                ;
            }

            try
            {
                if (var3 != null)
                {
                    var3.close();
                }
            }
            catch (Throwable var22)
            {
                ;
            }
        }
    }

    protected void func_74007_a(String par1Str, int par2, int par3)
    {
        if (par1Str != null)
        {
            int var4 = par2 + 12;
            int var5 = par3 - 12;
            int var6 = this.fontRenderer.getStringWidth(par1Str);
            this.drawGradientRect(var4 - 3, var5 - 3, var4 + var6 + 3, var5 + 8 + 3, -1073741824, -1073741824);
            this.fontRenderer.drawStringWithShadow(par1Str, var4, var5, -1);
        }
    }

    static ServerList func_74006_a(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.internetServerList;
    }

    static List func_74003_b(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.field_74026_B;
    }

    static int func_74020_c(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.selectedServer;
    }

    static int func_74015_a(GuiMultiplayer par0GuiMultiplayer, int par1)
    {
        return par0GuiMultiplayer.selectedServer = par1;
    }

    /**
     * Return buttonSelect GuiButton
     */
    static GuiButton getButtonSelect(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.buttonSelect;
    }

    /**
     * Return buttonEdit GuiButton
     */
    static GuiButton getButtonEdit(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.buttonEdit;
    }

    /**
     * Return buttonDelete GuiButton
     */
    static GuiButton getButtonDelete(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.buttonDelete;
    }

    static void func_74008_b(GuiMultiplayer par0GuiMultiplayer, int par1)
    {
        par0GuiMultiplayer.joinServer(par1);
    }

    static int func_74010_g(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.field_74039_z;
    }

    static Object func_74011_h()
    {
        return lock;
    }

    static int func_74012_i()
    {
        return threadsPending;
    }

    static int func_74021_j()
    {
        return threadsPending++;
    }

    static void func_74013_a(GuiMultiplayer par0GuiMultiplayer, ServerData par1ServerData) throws IOException
    {
        par0GuiMultiplayer.func_74017_b(par1ServerData);
    }

    static int func_74018_k()
    {
        return threadsPending--;
    }

    static String func_74009_a(GuiMultiplayer par0GuiMultiplayer, String par1Str)
    {
        return par0GuiMultiplayer.lagTooltip = par1Str;
    }
}

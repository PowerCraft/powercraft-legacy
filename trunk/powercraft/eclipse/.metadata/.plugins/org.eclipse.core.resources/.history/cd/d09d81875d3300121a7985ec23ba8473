package powercraft.core;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Block;
import net.minecraft.src.CallableMinecraftVersion;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.Configuration;

import org.lwjgl.input.Keyboard;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "PowerCraft-Core", name = "PowerCraft-Core", version = "3.5.0AlphaB", dependencies = "before:*")
@NetworkMod(clientSideRequired = true, serverSideRequired = true, clientPacketHandlerSpec = @SidedPacketHandler(channels = {"PowerCraft"}, packetHandler = PC_ClientPacketHandler.class),
        serverPacketHandlerSpec = @SidedPacketHandler(channels = {"PowerCraft"}, packetHandler = PC_PacketHandler.class))
public class mod_PowerCraftCore extends PC_Module
{
    @SidedProxy(clientSide = "powercraft.core.PCco_ClientProxy", serverSide = "powercraft.core.PCco_CommonProxy")
    public static PCco_CommonProxy proxy;

    public static boolean guiOverlayer;

    public static boolean showUpdateWindow = false;

    public static boolean hackSplashes = true;

    public static String useUserName;
    
    public static PC_Block powerCrystal;
    public static PC_Item powerDust;
    public static PC_Item craftingTool;
    public static PC_Item oreSniffer;
    public static PC_Item activator;

    public static final String updateInfoPath = "https://dl.dropbox.com/s/axurfxp6cxkr3nv/Update.xml?dl=1";

    public static mod_PowerCraftCore getInstance()
    {
        return (mod_PowerCraftCore)PC_Module.getModule("PowerCraft-Core");
    }

    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.initUtils();
        PC_Logger.init(PC_Utils.getMCDirectory());
        preInit(event, proxy);
        getModMetadata().logoFile = getTextureDirectory() + "PowerCraft.png";
    }

    @Init
    public void init(FMLInitializationEvent event)
    {
        init();
        GameRegistry.registerWorldGenerator(new PC_WorldGenerator());
        GameRegistry.registerFuelHandler(new PC_FuelHandler());
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        new PCco_ThreadCheckUpdates(updateInfoPath).start();
        postInit();
    }

    @Override
    protected void initProperties(Configuration config)
    {
        PC_Utils.enableSound(PC_Utils.getConfigBool(config, Configuration.CATEGORY_GENERAL, "enableSounds", true));
        hackSplashes = PC_Utils.getConfigBool(config, Configuration.CATEGORY_GENERAL, "hackSplashes", true);
        PC_Utils.setReverseKey(config);
        useUserName = PC_Utils.getConfigString(config, Configuration.CATEGORY_GENERAL, "hackSetUserName", "");
    }

    @Override
    protected List<String> loadTextureFiles(List<String> textures)
    {
        textures.add(getTerrainFile());
        textures.add(getTextureDirectory() + "PowerCraft.png");
        return textures;
    }

    @Override
    protected void initLanguage()
    {
        PC_Utils.registerLanguage(this,
                "pc.gui.ok", "OK",
                "pc.gui.cancel", "Cancel",
                "pc.gui.close", "Close",
                "pc.gui.back", "Back",
                "pc.gui.craftingTool.title", "Crafting Tool",
                "pc.gui.craftingTool.trashAll", "Trash All",
                "pc.gui.craftingTool.search", "Search",
                "pc.gui.craftingTool.sort", "Sort",
                "pc.gui.update.title", "Mod Update Notification",
                "pc.gui.update.newVersionAvailable", "Update available!",
                "pc.gui.update.readMore", "Read more...",
                "pc.gui.update.version", "Using %1$s (%2$s), Available %3$s (%4$s)",
                "pc.gui.update.doNotShowAgain", "Don't show again",
                "pc.sniffer.desc", "Portable radar device",
                "pc.sniffer.distance", "Sniffing depth (blocks):"
                                 );
    }

    @Override
    protected void initBlocks()
    {
        powerCrystal = (PC_Block)PC_Utils.register(this, 456, PCco_BlockPowerCrystal.class, PCco_ItemBlockPowerCrystal.class);
    }

    @Override
    protected void initItems()
    {
        powerDust = (PC_Item)PC_Utils.register(this, 457, PCco_ItemPowerDust.class);
        craftingTool = (PC_Item)PC_Utils.register(this, 458, PCco_ItemCraftingTool.class);
        oreSniffer = (PC_Item)PC_Utils.register(this, 459, PCco_ItemOreSniffer.class);
        activator = (PC_Item)PC_Utils.register(this, 460, PCco_ItemActivator.class);
    }

    @Override
    protected void initRecipes()
    {
        PC_Utils.addRecipe(new PC_ItemStack(craftingTool),
                new Object[]
                {
                    " r ",
                    "rIr",
                    " r ",
                    'r', Item.redstone, 'I', Block.blockSteel
                });
        PC_Utils.addShapelessRecipe(new PC_ItemStack(powerDust, 24, 0), new Object[] { new PC_ItemStack(powerCrystal, 1, -1) });
        PC_Utils.addRecipe(new PC_ItemStack(oreSniffer),
                new Object[]
                {
                    " G ",
                    "GCG",
                    " G ",
                    'C', new PC_ItemStack(powerCrystal, 1, -1), 'G', Item.ingotGold
                });
        PC_Utils.addRecipe(new PC_ItemStack(activator, 1, 0),
                new Object[]
                {
                    "C",
                    "I",
                    'C', new PC_ItemStack(powerCrystal, 1, -1), 'I', Item.ingotIron
                });
    }

    @Override
    protected List<String> addSplashes(List<String> list)
    {
        list.add("Sniffing diamonds!");
        list.add("GRES");

        for (int i = 0; i < 10; i++)
        {
            list.add("Modded by MightyPork!");
        }

        for (int i = 0; i < 6; i++)
        {
            list.add("Modded by XOR!");
        }

        for (int i = 0; i < 5; i++)
        {
            list.add("Modded by Rapus95!");
        }

        for (int i = 0; i < 4; i++)
        {
            list.add("Reviewed by RxD");
        }

        list.add("Modded by masters!");

        for (int i = 0; i < 3; i++)
        {
            list.add("PowerCraft " + mod_PowerCraftCore.getInstance().getVersion());
        }

        list.add("Null Pointers included!");
        list.add("ArrayIndexOutOfBoundsException");
        list.add("Null Pointer loves you!");
        list.add("Unstable!");
        list.add("Buggy code!");
        list.add("Break it down!");
        list.add("Addictive!");
        list.add("Earth is flat!");
        list.add("Faster than Atari!");
        list.add("DAFUQ??");
        list.add("LWJGL");
        list.add("Don't press the button!");
        list.add("Press the button!");
        list.add("Ssssssssssssssss!");
        list.add("C'mon!");
        list.add("Redstone Wizzard!");
        list.add("Keep your mods up-to-date!");
        list.add("Read the changelog!");
        list.add("Read the log files!");
        list.add("Discoworld!");
        list.add("Also try ICE AGE mod!");
        list.add("Also try Backpack mod!");
        return list;
    }

    public static void onUpdateInfoDownloaded(String page)
    {
        PC_Logger.fine("\n\nUpdate information received from server.");

        try
        {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new ByteArrayInputStream(page.getBytes("UTF-8")));
            doc.getDocumentElement().normalize();
            NodeList node = doc.getElementsByTagName("Info");

            if (node.getLength() != 1)
            {
                PC_Logger.severe("No Info node found");
                return;
            }

            node = node.item(0).getChildNodes();

            for (int i = 0; i < node.getLength(); i++)
            {
                if (node.item(i).getNodeName().equalsIgnoreCase("update"))
                {
                    Element element = (Element)node.item(i);
                    String sModule = element.getAttribute("module");
                    String sModuleVersion = element.getAttribute("moduleVersion");
                    String sMinecraftVersion = element.getAttribute("minecraftVersion");
                    String sLangVersion = element.getAttribute("langVersion");
                    String sLangLink = element.getAttribute("langLink");
                    String sInfo = element.getTextContent();
                    PC_Module module = PC_Module.getModule(sModule);

                    if (module != null)
                    {
                        int langVersion = -1;

                        try
                        {
                            langVersion = Integer.parseInt(sLangVersion);
                        }
                        catch (NumberFormatException e) {}

                        if (new CallableMinecraftVersion(null).minecraftVersion().equalsIgnoreCase(sMinecraftVersion))
                        {
                            if (module.updateInfo(sModuleVersion, sInfo.trim()))
                            {
                                showUpdateWindow = true;
                            }
                        }

                        if (langVersion > module.getLangVersion())
                        {
                            new PCco_ThreadDownloadTranslations(sLangLink, module, langVersion).start();
                        }
                    }
                }
            }
        }
        catch (SAXParseException err)
        {
            PC_Logger.severe("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            PC_Logger.severe(" " + err.getMessage());
        }
        catch (SAXException e)
        {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        }
        catch (Throwable t)
        {
            PC_Logger.throwing("mod_PCcore", "onUpdateInfoDownloaded()", t);
            t.printStackTrace();
        }
    }
}

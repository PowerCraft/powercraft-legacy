package cpw.mods.fml.server;

import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Entity;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet131MapData;
import net.minecraft.src.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IFMLSidedHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.EntitySpawnAdjustmentPacket;
import cpw.mods.fml.common.network.EntitySpawnPacket;
import cpw.mods.fml.common.network.ModMissingPacket;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class FMLServerHandler implements IFMLSidedHandler
{
    private static final FMLServerHandler INSTANCE = new FMLServerHandler();

    private MinecraftServer server;

    private FMLServerHandler()
    {
        FMLCommonHandler.instance().beginLoading(this);
    }

    public void beginServerLoading(MinecraftServer minecraftServer)
    {
        server = minecraftServer;
        ObfuscationReflectionHelper.detectObfuscation(World.class);
        Loader.instance().loadMods();
    }

    public void finishServerLoading()
    {
        Loader.instance().initializeMods();
        LanguageRegistry.reloadLanguageTable();
    }

    @Override
    public void haltGame(String message, Throwable exception)
    {
        throw new RuntimeException(message, exception);
    }

    public MinecraftServer getServer()
    {
        return server;
    }

    public static FMLServerHandler instance()
    {
        return INSTANCE;
    }

    @Override
    public List<String> getAdditionalBrandingInformation()
    {
        return null;
    }

    @Override
    public Side getSide()
    {
        return Side.SERVER;
    }

    @Override
    public void showGuiScreen(Object clientGuiElement)
    {
    }

    @Override
    public Entity spawnEntityIntoClientWorld(EntityRegistration er, EntitySpawnPacket packet)
    {
        return null;
    }

    @Override
    public void adjustEntityLocationOnClient(EntitySpawnAdjustmentPacket entitySpawnAdjustmentPacket)
    {
    }
    @Override
    public void sendPacket(Packet packet)
    {
        throw new RuntimeException("You cannot send a bare packet without a target on the server!");
    }
    @Override
    public void displayMissingMods(ModMissingPacket modMissingPacket)
    {
    }
    @Override
    public void handleTinyPacket(NetHandler handler, Packet131MapData mapData)
    {
    }
    @Override
    public void setClientCompatibilityLevel(byte compatibilityLevel)
    {
    }
    @Override
    public byte getClientCompatibilityLevel()
    {
        return 0;
    }
}

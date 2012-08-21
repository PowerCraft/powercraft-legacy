package net.minecraft.src;

import java.io.File;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;

public abstract class ServerConfigurationManager
{
    private static final SimpleDateFormat field_72403_e = new SimpleDateFormat("yyyy-MM-dd \'at\' HH:mm:ss z");

    /** Reference to the logger. */
    public static final Logger logger = Logger.getLogger("Minecraft");

    /** Reference to the MinecraftServer object. */
    private final MinecraftServer mcServer;

    /** A list of player entities that exist on this server. */
    public final List playerEntityList = new ArrayList();
    private final BanList field_72401_g = new BanList(new File("banned-players.txt"));
    private final BanList field_72413_h = new BanList(new File("banned-ips.txt"));

    /** A set containing the OPs. */
    private Set ops = new HashSet();

    /** The Set of all whitelisted players */
    private Set whiteListedPlayers = new HashSet();

    /** Reference to the PlayerNBTManager object. */
    private IPlayerFileData playerNBTManagerObj;

    /**
     * Server setting to only allow OPs and whitelisted players to join the server.
     */
    private boolean whiteListEnforced;

    /** The maximum number of players that can be connected at a time. */
    protected int maxPlayers;
    protected int viewDistance;
    private EnumGameType field_72410_m;
    private boolean field_72407_n;

    /**
     * index into playerEntities of player to ping, updated every tick; currently hardcoded to max at 200 players
     */
    private int playerPingIndex = 0;

    public ServerConfigurationManager(MinecraftServer par1MinecraftServer)
    {
        this.mcServer = par1MinecraftServer;
        this.field_72401_g.setListActive(false);
        this.field_72413_h.setListActive(false);
        this.maxPlayers = 8;
    }

    public void func_72355_a(NetworkManager par1NetworkManager, EntityPlayerMP par2EntityPlayerMP)
    {
        this.readPlayerDataFromFile(par2EntityPlayerMP);
        par2EntityPlayerMP.setWorld(this.mcServer.getWorldManager(par2EntityPlayerMP.dimension));
        par2EntityPlayerMP.theItemInWorldManager.setWorld((WorldServer)par2EntityPlayerMP.worldObj);
        String var3 = "local";

        if (par1NetworkManager.getRemoteAddress() != null)
        {
            var3 = par1NetworkManager.getRemoteAddress().toString();
        }

        logger.info(par2EntityPlayerMP.username + "[" + var3 + "] logged in with entity id " + par2EntityPlayerMP.entityId + " at (" + par2EntityPlayerMP.posX + ", " + par2EntityPlayerMP.posY + ", " + par2EntityPlayerMP.posZ + ")");
        WorldServer var4 = this.mcServer.getWorldManager(par2EntityPlayerMP.dimension);
        ChunkCoordinates var5 = var4.getSpawnPoint();
        this.func_72381_a(par2EntityPlayerMP, (EntityPlayerMP)null, var4);
        NetServerHandler var6 = new NetServerHandler(this.mcServer, par1NetworkManager, par2EntityPlayerMP);
        var6.sendPacket(new Packet1Login(par2EntityPlayerMP.entityId, var4.getWorldInfo().getTerrainType(), par2EntityPlayerMP.theItemInWorldManager.getGameType(), var4.getWorldInfo().isHardcoreModeEnabled(), var4.provider.worldType, var4.difficultySetting, var4.getHeight(), this.getMaxPlayers()));
        var6.sendPacket(new Packet6SpawnPosition(var5.posX, var5.posY, var5.posZ));
        var6.sendPacket(new Packet202PlayerAbilities(par2EntityPlayerMP.capabilities));
        this.updateTimeAndWeather(par2EntityPlayerMP, var4);
        this.sendPacketToAllPlayers(new Packet3Chat("\u00a7e" + par2EntityPlayerMP.username + " joined the game."));
        this.playerLoggedIn(par2EntityPlayerMP);
        var6.teleportTo(par2EntityPlayerMP.posX, par2EntityPlayerMP.posY, par2EntityPlayerMP.posZ, par2EntityPlayerMP.rotationYaw, par2EntityPlayerMP.rotationPitch);
        this.mcServer.func_71212_ac().addPlayer(var6);
        var6.sendPacket(new Packet4UpdateTime(var4.getWorldTime()));

        if (this.mcServer.func_71202_P().length() > 0)
        {
            par2EntityPlayerMP.func_71115_a(this.mcServer.func_71202_P(), this.mcServer.func_71227_R());
        }

        Iterator var7 = par2EntityPlayerMP.getActivePotionEffects().iterator();

        while (var7.hasNext())
        {
            PotionEffect var8 = (PotionEffect)var7.next();
            var6.sendPacket(new Packet41EntityEffect(par2EntityPlayerMP.entityId, var8));
        }

        par2EntityPlayerMP.func_71116_b();
    }

    /**
     * Sets the NBT manager to the one for the worldserver given
     */
    public void setPlayerManager(WorldServer[] par1ArrayOfWorldServer)
    {
        this.playerNBTManagerObj = par1ArrayOfWorldServer[0].getSaveHandler().getPlayerNBTManager();
    }

    public void func_72375_a(EntityPlayerMP par1EntityPlayerMP, WorldServer par2WorldServer)
    {
        WorldServer var3 = par1EntityPlayerMP.func_71121_q();

        if (par2WorldServer != null)
        {
            par2WorldServer.func_73040_p().removePlayer(par1EntityPlayerMP);
        }

        var3.func_73040_p().addPlayer(par1EntityPlayerMP);
        var3.theChunkProviderServer.loadChunk((int)par1EntityPlayerMP.posX >> 4, (int)par1EntityPlayerMP.posZ >> 4);
    }

    public int func_72372_a()
    {
        return PlayerManager.func_72686_a(this.getViewDistance());
    }

    /**
     * called during player login. reads the player information from disk.
     */
    public void readPlayerDataFromFile(EntityPlayerMP par1EntityPlayerMP)
    {
        NBTTagCompound var2 = this.mcServer.theWorldServer[0].getWorldInfo().func_76072_h();

        if (par1EntityPlayerMP.getCommandSenderName().equals(this.mcServer.getServerOwner()) && var2 != null)
        {
            par1EntityPlayerMP.readFromNBT(var2);
        }
        else
        {
            this.playerNBTManagerObj.readPlayerData(par1EntityPlayerMP);
        }
    }

    protected void func_72391_b(EntityPlayerMP par1EntityPlayerMP)
    {
        this.playerNBTManagerObj.writePlayerData(par1EntityPlayerMP);
    }

    /**
     * Called when a player successfully logs in. Reads player data from disk and inserts the player into the world.
     */
    public void playerLoggedIn(EntityPlayerMP par1EntityPlayerMP)
    {
        this.sendPacketToAllPlayers(new Packet201PlayerInfo(par1EntityPlayerMP.username, true, 1000));
        this.playerEntityList.add(par1EntityPlayerMP);
        WorldServer var2 = this.mcServer.getWorldManager(par1EntityPlayerMP.dimension);

        while (!var2.getCollidingBoundingBoxes(par1EntityPlayerMP, par1EntityPlayerMP.boundingBox).isEmpty())
        {
            par1EntityPlayerMP.setPosition(par1EntityPlayerMP.posX, par1EntityPlayerMP.posY + 1.0D, par1EntityPlayerMP.posZ);
        }

        var2.spawnEntityInWorld(par1EntityPlayerMP);
        this.func_72375_a(par1EntityPlayerMP, (WorldServer)null);
        Iterator var3 = this.playerEntityList.iterator();

        while (var3.hasNext())
        {
            EntityPlayerMP var4 = (EntityPlayerMP)var3.next();
            par1EntityPlayerMP.playerNetServerHandler.sendPacket(new Packet201PlayerInfo(var4.username, true, var4.ping));
        }
    }

    /**
     * using player's dimension, update their movement when in a vehicle (e.g. cart, boat)
     */
    public void serverUpdateMountedMovingPlayer(EntityPlayerMP par1EntityPlayerMP)
    {
        par1EntityPlayerMP.func_71121_q().func_73040_p().updateMountedMovingPlayer(par1EntityPlayerMP);
    }

    /**
     * Called when a player disconnects from the game. Writes player data to disk and removes them from the world.
     */
    public void playerLoggedOut(EntityPlayerMP par1EntityPlayerMP)
    {
        this.func_72391_b(par1EntityPlayerMP);
        WorldServer var2 = par1EntityPlayerMP.func_71121_q();
        var2.setEntityDead(par1EntityPlayerMP);
        var2.func_73040_p().removePlayer(par1EntityPlayerMP);
        this.playerEntityList.remove(par1EntityPlayerMP);
        this.sendPacketToAllPlayers(new Packet201PlayerInfo(par1EntityPlayerMP.username, false, 9999));
    }

    public String func_72399_a(SocketAddress par1SocketAddress, String par2Str)
    {
        if (this.field_72401_g.func_73704_a(par2Str))
        {
            BanEntry var6 = (BanEntry)this.field_72401_g.func_73712_c().get(par2Str);
            String var7 = "You are banned from this server!\nReason: " + var6.func_73686_f();

            if (var6.func_73680_d() != null)
            {
                var7 = var7 + "\nYour ban will be removed on " + field_72403_e.format(var6.func_73680_d());
            }

            return var7;
        }
        else if (!this.isAllowedToLogin(par2Str))
        {
            return "You are not white-listed on this server!";
        }
        else
        {
            String var3 = par1SocketAddress.toString();
            var3 = var3.substring(var3.indexOf("/") + 1);
            var3 = var3.substring(0, var3.indexOf(":"));

            if (this.field_72413_h.func_73704_a(var3))
            {
                BanEntry var4 = (BanEntry)this.field_72413_h.func_73712_c().get(var3);
                String var5 = "Your IP address is banned from this server!\nReason: " + var4.func_73686_f();

                if (var4.func_73680_d() != null)
                {
                    var5 = var5 + "\nYour ban will be removed on " + field_72403_e.format(var4.func_73680_d());
                }

                return var5;
            }
            else
            {
                return this.playerEntityList.size() >= this.maxPlayers ? "The server is full!" : null;
            }
        }
    }

    public EntityPlayerMP func_72366_a(String par1Str)
    {
        ArrayList var2 = new ArrayList();
        Iterator var3 = this.playerEntityList.iterator();
        EntityPlayerMP var4;

        while (var3.hasNext())
        {
            var4 = (EntityPlayerMP)var3.next();

            if (var4.username.equalsIgnoreCase(par1Str))
            {
                var2.add(var4);
            }
        }

        var3 = var2.iterator();

        while (var3.hasNext())
        {
            var4 = (EntityPlayerMP)var3.next();
            var4.playerNetServerHandler.kickPlayer("You logged in from another location");
        }

        Object var5;

        if (this.mcServer.func_71242_L())
        {
            var5 = new DemoWorldManager(this.mcServer.getWorldManager(0));
        }
        else
        {
            var5 = new ItemInWorldManager(this.mcServer.getWorldManager(0));
        }

        return new EntityPlayerMP(this.mcServer, this.mcServer.getWorldManager(0), par1Str, (ItemInWorldManager)var5);
    }

    /**
     * Called on respawn
     */
    public EntityPlayerMP recreatePlayerEntity(EntityPlayerMP par1EntityPlayerMP, int par2, boolean par3)
    {
        par1EntityPlayerMP.func_71121_q().getEntityTracker().removeTrackedPlayerSymmetric(par1EntityPlayerMP);
        par1EntityPlayerMP.func_71121_q().getEntityTracker().untrackEntity(par1EntityPlayerMP);
        par1EntityPlayerMP.func_71121_q().func_73040_p().removePlayer(par1EntityPlayerMP);
        this.playerEntityList.remove(par1EntityPlayerMP);
        this.mcServer.getWorldManager(par1EntityPlayerMP.dimension).removeEntity(par1EntityPlayerMP);
        ChunkCoordinates var4 = par1EntityPlayerMP.getSpawnChunk();
        par1EntityPlayerMP.dimension = par2;
        Object var5;

        if (this.mcServer.func_71242_L())
        {
            var5 = new DemoWorldManager(this.mcServer.getWorldManager(par1EntityPlayerMP.dimension));
        }
        else
        {
            var5 = new ItemInWorldManager(this.mcServer.getWorldManager(par1EntityPlayerMP.dimension));
        }

        EntityPlayerMP var6 = new EntityPlayerMP(this.mcServer, this.mcServer.getWorldManager(par1EntityPlayerMP.dimension), par1EntityPlayerMP.username, (ItemInWorldManager)var5);
        var6.clonePlayer(par1EntityPlayerMP, par3);
        var6.entityId = par1EntityPlayerMP.entityId;
        var6.playerNetServerHandler = par1EntityPlayerMP.playerNetServerHandler;
        WorldServer var7 = this.mcServer.getWorldManager(par1EntityPlayerMP.dimension);
        this.func_72381_a(var6, par1EntityPlayerMP, var7);
        ChunkCoordinates var8;

        if (var4 != null)
        {
            var8 = EntityPlayer.verifyRespawnCoordinates(this.mcServer.getWorldManager(par1EntityPlayerMP.dimension), var4);

            if (var8 != null)
            {
                var6.setLocationAndAngles((double)((float)var8.posX + 0.5F), (double)((float)var8.posY + 0.1F), (double)((float)var8.posZ + 0.5F), 0.0F, 0.0F);
                var6.setSpawnChunk(var4);
            }
            else
            {
                var6.playerNetServerHandler.sendPacket(new Packet70GameEvent(0, 0));
            }
        }

        var7.theChunkProviderServer.loadChunk((int)var6.posX >> 4, (int)var6.posZ >> 4);

        while (!var7.getCollidingBoundingBoxes(var6, var6.boundingBox).isEmpty())
        {
            var6.setPosition(var6.posX, var6.posY + 1.0D, var6.posZ);
        }

        var6.playerNetServerHandler.sendPacket(new Packet9Respawn(var6.dimension, (byte)var6.worldObj.difficultySetting, var6.worldObj.getWorldInfo().getTerrainType(), var6.worldObj.getHeight(), var6.theItemInWorldManager.getGameType()));
        var8 = var7.getSpawnPoint();
        var6.playerNetServerHandler.teleportTo(var6.posX, var6.posY, var6.posZ, var6.rotationYaw, var6.rotationPitch);
        var6.playerNetServerHandler.sendPacket(new Packet6SpawnPosition(var8.posX, var8.posY, var8.posZ));
        this.updateTimeAndWeather(var6, var7);
        var7.func_73040_p().addPlayer(var6);
        var7.spawnEntityInWorld(var6);
        this.playerEntityList.add(var6);
        var6.func_71116_b();
        return var6;
    }

    /**
     * moves provided player from overworld to nether or vice versa
     */
    public void sendPlayerToOtherDimension(EntityPlayerMP par1EntityPlayerMP, int par2)
    {
        int var3 = par1EntityPlayerMP.dimension;
        WorldServer var4 = this.mcServer.getWorldManager(par1EntityPlayerMP.dimension);
        par1EntityPlayerMP.dimension = par2;
        WorldServer var5 = this.mcServer.getWorldManager(par1EntityPlayerMP.dimension);
        par1EntityPlayerMP.playerNetServerHandler.sendPacket(new Packet9Respawn(par1EntityPlayerMP.dimension, (byte)par1EntityPlayerMP.worldObj.difficultySetting, var5.getWorldInfo().getTerrainType(), var5.getHeight(), par1EntityPlayerMP.theItemInWorldManager.getGameType()));
        var4.removeEntity(par1EntityPlayerMP);
        par1EntityPlayerMP.isDead = false;
        double var6 = par1EntityPlayerMP.posX;
        double var8 = par1EntityPlayerMP.posZ;
        double var10 = 8.0D;

        if (par1EntityPlayerMP.dimension == -1)
        {
            var6 /= var10;
            var8 /= var10;
            par1EntityPlayerMP.setLocationAndAngles(var6, par1EntityPlayerMP.posY, var8, par1EntityPlayerMP.rotationYaw, par1EntityPlayerMP.rotationPitch);

            if (par1EntityPlayerMP.isEntityAlive())
            {
                var4.updateEntityWithOptionalForce(par1EntityPlayerMP, false);
            }
        }
        else if (par1EntityPlayerMP.dimension == 0)
        {
            var6 *= var10;
            var8 *= var10;
            par1EntityPlayerMP.setLocationAndAngles(var6, par1EntityPlayerMP.posY, var8, par1EntityPlayerMP.rotationYaw, par1EntityPlayerMP.rotationPitch);

            if (par1EntityPlayerMP.isEntityAlive())
            {
                var4.updateEntityWithOptionalForce(par1EntityPlayerMP, false);
            }
        }
        else
        {
            ChunkCoordinates var12 = var5.func_73054_j();
            var6 = (double)var12.posX;
            par1EntityPlayerMP.posY = (double)var12.posY;
            var8 = (double)var12.posZ;
            par1EntityPlayerMP.setLocationAndAngles(var6, par1EntityPlayerMP.posY, var8, 90.0F, 0.0F);

            if (par1EntityPlayerMP.isEntityAlive())
            {
                var4.updateEntityWithOptionalForce(par1EntityPlayerMP, false);
            }
        }

        if (var3 != 1)
        {
            var6 = (double)MathHelper.clamp_int((int)var6, -29999872, 29999872);
            var8 = (double)MathHelper.clamp_int((int)var8, -29999872, 29999872);

            if (par1EntityPlayerMP.isEntityAlive())
            {
                var5.spawnEntityInWorld(par1EntityPlayerMP);
                par1EntityPlayerMP.setLocationAndAngles(var6, par1EntityPlayerMP.posY, var8, par1EntityPlayerMP.rotationYaw, par1EntityPlayerMP.rotationPitch);
                var5.updateEntityWithOptionalForce(par1EntityPlayerMP, false);
                (new Teleporter()).placeInPortal(var5, par1EntityPlayerMP);
            }
        }

        par1EntityPlayerMP.setWorld(var5);
        this.func_72375_a(par1EntityPlayerMP, var4);
        par1EntityPlayerMP.playerNetServerHandler.teleportTo(par1EntityPlayerMP.posX, par1EntityPlayerMP.posY, par1EntityPlayerMP.posZ, par1EntityPlayerMP.rotationYaw, par1EntityPlayerMP.rotationPitch);
        par1EntityPlayerMP.theItemInWorldManager.setWorld(var5);
        this.updateTimeAndWeather(par1EntityPlayerMP, var5);
        this.func_72385_f(par1EntityPlayerMP);
        Iterator var14 = par1EntityPlayerMP.getActivePotionEffects().iterator();

        while (var14.hasNext())
        {
            PotionEffect var13 = (PotionEffect)var14.next();
            par1EntityPlayerMP.playerNetServerHandler.sendPacket(new Packet41EntityEffect(par1EntityPlayerMP.entityId, var13));
        }
    }

    /**
     * self explanitory
     */
    public void onTick()
    {
        if (++this.playerPingIndex > 600)
        {
            this.playerPingIndex = 0;
        }

        if (this.playerPingIndex < this.playerEntityList.size())
        {
            EntityPlayerMP var1 = (EntityPlayerMP)this.playerEntityList.get(this.playerPingIndex);
            this.sendPacketToAllPlayers(new Packet201PlayerInfo(var1.username, true, var1.ping));
        }
    }

    /**
     * sends a packet to all players
     */
    public void sendPacketToAllPlayers(Packet par1Packet)
    {
        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2)
        {
            ((EntityPlayerMP)this.playerEntityList.get(var2)).playerNetServerHandler.sendPacket(par1Packet);
        }
    }

    /**
     * Sends a packet to all players in the specified Dimension
     */
    public void sendPacketToAllPlayersInDimension(Packet par1Packet, int par2)
    {
        Iterator var3 = this.playerEntityList.iterator();

        while (var3.hasNext())
        {
            EntityPlayerMP var4 = (EntityPlayerMP)var3.next();

            if (var4.dimension == par2)
            {
                var4.playerNetServerHandler.sendPacket(par1Packet);
            }
        }
    }

    /**
     * returns a string containing a comma-seperated list of player names
     */
    public String getPlayerList()
    {
        String var1 = "";

        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2)
        {
            if (var2 > 0)
            {
                var1 = var1 + ", ";
            }

            var1 = var1 + ((EntityPlayerMP)this.playerEntityList.get(var2)).username;
        }

        return var1;
    }

    /**
     * Returns a list of usernames of all connected players
     */
    public String[] getPlayerNamesAsList()
    {
        String[] var1 = new String[this.playerEntityList.size()];

        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2)
        {
            var1[var2] = ((EntityPlayerMP)this.playerEntityList.get(var2)).username;
        }

        return var1;
    }

    public BanList getBannedPlayers()
    {
        return this.field_72401_g;
    }

    public BanList getBannedIPs()
    {
        return this.field_72413_h;
    }

    /**
     * This adds a username to the ops list, then saves the op list
     */
    public void addOp(String par1Str)
    {
        this.ops.add(par1Str.toLowerCase());
    }

    /**
     * This removes a username from the ops list, then saves the op list
     */
    public void removeOp(String par1Str)
    {
        this.ops.remove(par1Str.toLowerCase());
    }

    /**
     * Determine if the player is allowed to connect based on current server settings.
     */
    public boolean isAllowedToLogin(String par1Str)
    {
        par1Str = par1Str.trim().toLowerCase();
        return !this.whiteListEnforced || this.ops.contains(par1Str) || this.whiteListedPlayers.contains(par1Str);
    }

    /**
     * Returns true if the player is an OP, false otherwise.
     */
    public boolean isOp(String par1Str)
    {
        return this.ops.contains(par1Str.trim().toLowerCase()) || this.mcServer.isSinglePlayer() && this.mcServer.theWorldServer[0].getWorldInfo().areCommandsAllowed() && this.mcServer.getServerOwner().equalsIgnoreCase(par1Str) || this.field_72407_n;
    }

    /**
     * gets the player entity for the player with the name specified
     */
    public EntityPlayerMP getPlayerEntity(String par1Str)
    {
        Iterator var2 = this.playerEntityList.iterator();
        EntityPlayerMP var3;

        do
        {
            if (!var2.hasNext())
            {
                return null;
            }

            var3 = (EntityPlayerMP)var2.next();
        }
        while (!var3.username.equalsIgnoreCase(par1Str));

        return var3;
    }

    /**
     * sends a packet to players within d3 of point (x,y,z)
     */
    public void sendPacketToPlayersAroundPoint(double par1, double par3, double par5, double par7, int par9, Packet par10Packet)
    {
        this.func_72397_a((EntityPlayer)null, par1, par3, par5, par7, par9, par10Packet);
    }

    public void func_72397_a(EntityPlayer par1EntityPlayer, double par2, double par4, double par6, double par8, int par10, Packet par11Packet)
    {
        Iterator var12 = this.playerEntityList.iterator();

        while (var12.hasNext())
        {
            EntityPlayerMP var13 = (EntityPlayerMP)var12.next();

            if (var13 != par1EntityPlayer && var13.dimension == par10)
            {
                double var14 = par2 - var13.posX;
                double var16 = par4 - var13.posY;
                double var18 = par6 - var13.posZ;

                if (var14 * var14 + var16 * var16 + var18 * var18 < par8 * par8)
                {
                    var13.playerNetServerHandler.sendPacket(par11Packet);
                }
            }
        }
    }

    /**
     * Saves all of the player's states
     */
    public void savePlayerStates()
    {
        Iterator var1 = this.playerEntityList.iterator();

        while (var1.hasNext())
        {
            EntityPlayerMP var2 = (EntityPlayerMP)var1.next();
            this.func_72391_b(var2);
        }
    }

    /**
     * add the specified player to the white list
     */
    public void addToWhiteList(String par1Str)
    {
        this.whiteListedPlayers.add(par1Str);
    }

    /**
     * Remove the specified player from the whitelist.
     */
    public void removeFromWhitelist(String par1Str)
    {
        this.whiteListedPlayers.remove(par1Str);
    }

    /**
     * returns the set of whitelisted ip addresses
     */
    public Set getWhiteListedIPs()
    {
        return this.whiteListedPlayers;
    }

    public Set func_72376_i()
    {
        return this.ops;
    }

    /**
     * Either does nothing, or calls readWhiteList.
     */
    public void loadWhiteList() {}

    /**
     * Updates the time and weather for the given player to those of the given world
     */
    public void updateTimeAndWeather(EntityPlayerMP par1EntityPlayerMP, WorldServer par2WorldServer)
    {
        par1EntityPlayerMP.playerNetServerHandler.sendPacket(new Packet4UpdateTime(par2WorldServer.getWorldTime()));

        if (par2WorldServer.isRaining())
        {
            par1EntityPlayerMP.playerNetServerHandler.sendPacket(new Packet70GameEvent(1, 0));
        }
    }

    public void func_72385_f(EntityPlayerMP par1EntityPlayerMP)
    {
        par1EntityPlayerMP.func_71120_a(par1EntityPlayerMP.inventorySlots);
        par1EntityPlayerMP.func_71118_n();
    }

    /**
     * Returns the number of players on the server
     */
    public int playersOnline()
    {
        return this.playerEntityList.size();
    }

    /**
     * Returns maximum amount of players that can join the server
     */
    public int getMaxPlayers()
    {
        return this.maxPlayers;
    }

    public String[] func_72373_m()
    {
        return this.mcServer.theWorldServer[0].getSaveHandler().getPlayerNBTManager().getAvailablePlayerDat();
    }

    public boolean func_72383_n()
    {
        return this.whiteListEnforced;
    }

    public void setWhiteListEnabled(boolean par1)
    {
        this.whiteListEnforced = par1;
    }

    public List func_72382_j(String par1Str)
    {
        ArrayList var2 = new ArrayList();
        Iterator var3 = this.playerEntityList.iterator();

        while (var3.hasNext())
        {
            EntityPlayerMP var4 = (EntityPlayerMP)var3.next();

            if (var4.func_71114_r().equals(par1Str))
            {
                var2.add(var4);
            }
        }

        return var2;
    }

    /**
     * Gets the View Distance.
     */
    public int getViewDistance()
    {
        return this.viewDistance;
    }

    public MinecraftServer getServerInstance()
    {
        return this.mcServer;
    }

    public NBTTagCompound func_72378_q()
    {
        return null;
    }

    private void func_72381_a(EntityPlayerMP par1EntityPlayerMP, EntityPlayerMP par2EntityPlayerMP, World par3World)
    {
        if (par2EntityPlayerMP != null)
        {
            par1EntityPlayerMP.theItemInWorldManager.setGameType(par2EntityPlayerMP.theItemInWorldManager.getGameType());
        }
        else if (this.field_72410_m != null)
        {
            par1EntityPlayerMP.theItemInWorldManager.setGameType(this.field_72410_m);
        }

        par1EntityPlayerMP.theItemInWorldManager.func_73077_b(par3World.getWorldInfo().getGameType());
    }

    public void func_72392_r()
    {
        while (!this.playerEntityList.isEmpty())
        {
            ((EntityPlayerMP)this.playerEntityList.get(0)).playerNetServerHandler.kickPlayer("Server closed");
        }
    }
}

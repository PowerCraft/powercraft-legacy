package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

public class EntityPlayerMP extends EntityPlayer implements ICrafting
{
    private StringTranslate translator = new StringTranslate("en_US");

    public NetServerHandler playerNetServerHandler;

    public MinecraftServer mcServer;

    public ItemInWorldManager theItemInWorldManager;

    public double managedPosX;

    public double managedPosZ;

    public final List loadedChunks = new LinkedList();

    public final List destroyedItemsNetCache = new LinkedList();

    private int lastHealth = -99999999;

    private int lastFoodLevel = -99999999;

    private boolean wasHungry = true;

    private int lastExperience = -99999999;

    private int initialInvulnerability = 60;

    private int renderDistance = 0;
    private int chatVisibility = 0;
    private boolean chatColours = true;

    public int currentWindowId = 0;

    public boolean playerInventoryBeingManipulated;
    public int ping;

    public boolean playerConqueredTheEnd = false;

    public EntityPlayerMP(MinecraftServer par1MinecraftServer, World par2World, String par3Str, ItemInWorldManager par4ItemInWorldManager)
    {
        super(par2World);
        par4ItemInWorldManager.thisPlayerMP = this;
        this.theItemInWorldManager = par4ItemInWorldManager;
        this.renderDistance = par1MinecraftServer.getConfigurationManager().getViewDistance();
        ChunkCoordinates var5 = par2World.provider.getRandomizedSpawnPoint();
        int var6 = var5.posX;
        int var7 = var5.posZ;
        int var8 = var5.posY;
        this.setLocationAndAngles((double)var6 + 0.5D, (double)var8, (double)var7 + 0.5D, 0.0F, 0.0F);
        this.mcServer = par1MinecraftServer;
        this.stepHeight = 0.0F;
        this.username = par3Str;
        this.yOffset = 0.0F;
    }

    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);

        if (par1NBTTagCompound.hasKey("playerGameType"))
        {
            this.theItemInWorldManager.setGameType(EnumGameType.getByID(par1NBTTagCompound.getInteger("playerGameType")));
        }
    }

    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("playerGameType", this.theItemInWorldManager.getGameType().getID());
    }

    public void addExperienceLevel(int par1)
    {
        super.addExperienceLevel(par1);
        this.lastExperience = -1;
    }

    public void addSelfToInternalCraftingInventory()
    {
        this.craftingInventory.addCraftingToCrafters(this);
    }

    protected void resetHeight()
    {
        this.yOffset = 0.0F;
    }

    public float getEyeHeight()
    {
        return 1.62F;
    }

    public void onUpdate()
    {
        this.theItemInWorldManager.updateBlockRemoving();
        --this.initialInvulnerability;
        this.craftingInventory.updateCraftingResults();

        while (!this.destroyedItemsNetCache.isEmpty())
        {
            int var1 = Math.min(this.destroyedItemsNetCache.size(), 127);
            int[] var2 = new int[var1];
            Iterator var3 = this.destroyedItemsNetCache.iterator();
            int var4 = 0;

            while (var3.hasNext() && var4 < var1)
            {
                var2[var4++] = ((Integer)var3.next()).intValue();
                var3.remove();
            }

            this.playerNetServerHandler.sendPacketToPlayer(new Packet29DestroyEntity(var2));
        }

        if (!this.loadedChunks.isEmpty())
        {
            ArrayList var6 = new ArrayList();
            Iterator var7 = this.loadedChunks.iterator();
            ArrayList var8 = new ArrayList();

            while (var7.hasNext() && var6.size() < 5)
            {
                ChunkCoordIntPair var9 = (ChunkCoordIntPair)var7.next();
                var7.remove();

                if (var9 != null && this.worldObj.blockExists(var9.chunkXPos << 4, 0, var9.chunkZPos << 4))
                {
                    var6.add(this.worldObj.getChunkFromChunkCoords(var9.chunkXPos, var9.chunkZPos));
                    var8.addAll(((WorldServer)this.worldObj).getAllTileEntityInBox(var9.chunkXPos * 16, 0, var9.chunkZPos * 16, var9.chunkXPos * 16 + 15, 256, var9.chunkZPos * 16 + 15));
                }
            }

            if (!var6.isEmpty())
            {
                this.playerNetServerHandler.sendPacketToPlayer(new Packet56MapChunks(var6));
                Iterator var11 = var8.iterator();

                while (var11.hasNext())
                {
                    TileEntity var5 = (TileEntity)var11.next();
                    this.sendTileEntityToPlayer(var5);
                }

                var11 = var6.iterator();

                while (var11.hasNext())
                {
                    Chunk var10 = (Chunk)var11.next();
                    this.getServerForPlayer().getEntityTracker().func_85172_a(this, var10);
                }
            }
        }
    }

    public void onUpdateEntity()
    {
        super.onUpdate();

        for (int var1 = 0; var1 < this.inventory.getSizeInventory(); ++var1)
        {
            ItemStack var2 = this.inventory.getStackInSlot(var1);

            if (var2 != null && Item.itemsList[var2.itemID].isMap() && this.playerNetServerHandler.packetSize() <= 5)
            {
                Packet var3 = ((ItemMapBase)Item.itemsList[var2.itemID]).createMapDataPacket(var2, this.worldObj, this);

                if (var3 != null)
                {
                    this.playerNetServerHandler.sendPacketToPlayer(var3);
                }
            }
        }

        if (this.getHealth() != this.lastHealth || this.lastFoodLevel != this.foodStats.getFoodLevel() || this.foodStats.getSaturationLevel() == 0.0F != this.wasHungry)
        {
            this.playerNetServerHandler.sendPacketToPlayer(new Packet8UpdateHealth(this.getHealth(), this.foodStats.getFoodLevel(), this.foodStats.getSaturationLevel()));
            this.lastHealth = this.getHealth();
            this.lastFoodLevel = this.foodStats.getFoodLevel();
            this.wasHungry = this.foodStats.getSaturationLevel() == 0.0F;
        }

        if (this.experienceTotal != this.lastExperience)
        {
            this.lastExperience = this.experienceTotal;
            this.playerNetServerHandler.sendPacketToPlayer(new Packet43Experience(this.experience, this.experienceTotal, this.experienceLevel));
        }
    }

    public void onDeath(DamageSource par1DamageSource)
    {
        if (ForgeHooks.onLivingDeath(this, par1DamageSource))
        {
            return;
        }

        this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new Packet3Chat(par1DamageSource.getDeathMessage(this)));

        if (!this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
        {
            captureDrops = true;
            capturedDrops.clear();
            this.inventory.dropAllItems();
            captureDrops = false;
            PlayerDropsEvent event = new PlayerDropsEvent(this, par1DamageSource, capturedDrops, recentlyHit > 0);

            if (!MinecraftForge.EVENT_BUS.post(event))
            {
                for (EntityItem item : capturedDrops)
                {
                    joinEntityItemWithWorld(item);
                }
            }
        }
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (this.func_85032_ar())
        {
            return false;
        }
        else
        {
            boolean var3 = this.mcServer.isDedicatedServer() && this.mcServer.isPVPEnabled() && "fall".equals(par1DamageSource.damageType);

            if (!var3 && this.initialInvulnerability > 0)
            {
                return false;
            }
            else
            {
                if (!this.mcServer.isPVPEnabled() && par1DamageSource instanceof EntityDamageSource)
                {
                    Entity var4 = par1DamageSource.getEntity();

                    if (var4 instanceof EntityPlayer)
                    {
                        return false;
                    }

                    if (var4 instanceof EntityArrow)
                    {
                        EntityArrow var5 = (EntityArrow)var4;

                        if (var5.shootingEntity instanceof EntityPlayer)
                        {
                            return false;
                        }
                    }
                }

                return super.attackEntityFrom(par1DamageSource, par2);
            }
        }
    }

    protected boolean isPVPEnabled()
    {
        return this.mcServer.isPVPEnabled();
    }

    public void travelToDimension(int par1)
    {
        if (this.dimension == 1 && par1 == 1)
        {
            this.triggerAchievement(AchievementList.theEnd2);
            this.worldObj.setEntityDead(this);
            this.playerConqueredTheEnd = true;
            this.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(4, 0));
        }
        else
        {
            if (this.dimension == 1 && par1 == 0)
            {
                this.triggerAchievement(AchievementList.theEnd);
                ChunkCoordinates var2 = this.mcServer.worldServerForDimension(par1).getEntrancePortalLocation();

                if (var2 != null)
                {
                    this.playerNetServerHandler.setPlayerLocation((double)var2.posX, (double)var2.posY, (double)var2.posZ, 0.0F, 0.0F);
                }

                par1 = 1;
            }
            else
            {
                this.triggerAchievement(AchievementList.portal);
            }

            this.mcServer.getConfigurationManager().transferPlayerToDimension(this, par1);
            this.lastExperience = -1;
            this.lastHealth = -1;
            this.lastFoodLevel = -1;
        }
    }

    private void sendTileEntityToPlayer(TileEntity par1TileEntity)
    {
        if (par1TileEntity != null)
        {
            Packet var2 = par1TileEntity.getDescriptionPacket();

            if (var2 != null)
            {
                this.playerNetServerHandler.sendPacketToPlayer(var2);
            }
        }
    }

    public void onItemPickup(Entity par1Entity, int par2)
    {
        super.onItemPickup(par1Entity, par2);
        this.craftingInventory.updateCraftingResults();
    }

    public EnumStatus sleepInBedAt(int par1, int par2, int par3)
    {
        EnumStatus var4 = super.sleepInBedAt(par1, par2, par3);

        if (var4 == EnumStatus.OK)
        {
            Packet17Sleep var5 = new Packet17Sleep(this, 0, par1, par2, par3);
            this.getServerForPlayer().getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, var5);
            this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.playerNetServerHandler.sendPacketToPlayer(var5);
        }

        return var4;
    }

    public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
    {
        if (this.isPlayerSleeping())
        {
            this.getServerForPlayer().getEntityTracker().sendPacketToAllAssociatedPlayers(this, new Packet18Animation(this, 3));
        }

        super.wakeUpPlayer(par1, par2, par3);

        if (this.playerNetServerHandler != null)
        {
            this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }

    public void mountEntity(Entity par1Entity)
    {
        super.mountEntity(par1Entity);
        this.playerNetServerHandler.sendPacketToPlayer(new Packet39AttachEntity(this, this.ridingEntity));
        this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
    }

    protected void updateFallState(double par1, boolean par3) {}

    public void updateFlyingState(double par1, boolean par3)
    {
        super.updateFallState(par1, par3);
    }

    public void incrementWindowID()
    {
        this.currentWindowId = this.currentWindowId % 100 + 1;
    }

    public void displayGUIWorkbench(int par1, int par2, int par3)
    {
        this.incrementWindowID();
        this.playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(this.currentWindowId, 1, "Crafting", 9));
        this.craftingInventory = new ContainerWorkbench(this.inventory, this.worldObj, par1, par2, par3);
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.addCraftingToCrafters(this);
    }

    public void displayGUIEnchantment(int par1, int par2, int par3)
    {
        this.incrementWindowID();
        this.playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(this.currentWindowId, 4, "Enchanting", 9));
        this.craftingInventory = new ContainerEnchantment(this.inventory, this.worldObj, par1, par2, par3);
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.addCraftingToCrafters(this);
    }

    public void displayGUIAnvil(int par1, int par2, int par3)
    {
        this.incrementWindowID();
        this.playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(this.currentWindowId, 8, "Repairing", 9));
        this.craftingInventory = new ContainerRepair(this.inventory, this.worldObj, par1, par2, par3, this);
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.addCraftingToCrafters(this);
    }

    public void displayGUIChest(IInventory par1IInventory)
    {
        if (this.craftingInventory != this.inventorySlots)
        {
            this.closeScreen();
        }

        this.incrementWindowID();
        this.playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(this.currentWindowId, 0, par1IInventory.getInvName(), par1IInventory.getSizeInventory()));
        this.craftingInventory = new ContainerChest(this.inventory, par1IInventory);
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.addCraftingToCrafters(this);
    }

    public void displayGUIFurnace(TileEntityFurnace par1TileEntityFurnace)
    {
        this.incrementWindowID();
        this.playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(this.currentWindowId, 2, par1TileEntityFurnace.getInvName(), par1TileEntityFurnace.getSizeInventory()));
        this.craftingInventory = new ContainerFurnace(this.inventory, par1TileEntityFurnace);
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.addCraftingToCrafters(this);
    }

    public void displayGUIDispenser(TileEntityDispenser par1TileEntityDispenser)
    {
        this.incrementWindowID();
        this.playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(this.currentWindowId, 3, par1TileEntityDispenser.getInvName(), par1TileEntityDispenser.getSizeInventory()));
        this.craftingInventory = new ContainerDispenser(this.inventory, par1TileEntityDispenser);
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.addCraftingToCrafters(this);
    }

    public void displayGUIBrewingStand(TileEntityBrewingStand par1TileEntityBrewingStand)
    {
        this.incrementWindowID();
        this.playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(this.currentWindowId, 5, par1TileEntityBrewingStand.getInvName(), par1TileEntityBrewingStand.getSizeInventory()));
        this.craftingInventory = new ContainerBrewingStand(this.inventory, par1TileEntityBrewingStand);
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.addCraftingToCrafters(this);
    }

    public void displayGUIBeacon(TileEntityBeacon par1TileEntityBeacon)
    {
        this.incrementWindowID();
        this.playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(this.currentWindowId, 7, par1TileEntityBeacon.getInvName(), par1TileEntityBeacon.getSizeInventory()));
        this.craftingInventory = new ContainerBeacon(this.inventory, par1TileEntityBeacon);
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.addCraftingToCrafters(this);
    }

    public void displayGUIMerchant(IMerchant par1IMerchant)
    {
        this.incrementWindowID();
        this.craftingInventory = new ContainerMerchant(this.inventory, par1IMerchant, this.worldObj);
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.addCraftingToCrafters(this);
        InventoryMerchant var2 = ((ContainerMerchant)this.craftingInventory).getMerchantInventory();
        this.playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(this.currentWindowId, 6, var2.getInvName(), var2.getSizeInventory()));
        MerchantRecipeList var3 = par1IMerchant.getRecipes(this);

        if (var3 != null)
        {
            try
            {
                ByteArrayOutputStream var4 = new ByteArrayOutputStream();
                DataOutputStream var5 = new DataOutputStream(var4);
                var5.writeInt(this.currentWindowId);
                var3.writeRecipiesToStream(var5);
                this.playerNetServerHandler.sendPacketToPlayer(new Packet250CustomPayload("MC|TrList", var4.toByteArray()));
            }
            catch (IOException var6)
            {
                var6.printStackTrace();
            }
        }
    }

    public void updateCraftingInventorySlot(Container par1Container, int par2, ItemStack par3ItemStack)
    {
        if (!(par1Container.getSlot(par2) instanceof SlotCrafting))
        {
            if (!this.playerInventoryBeingManipulated)
            {
                this.playerNetServerHandler.sendPacketToPlayer(new Packet103SetSlot(par1Container.windowId, par2, par3ItemStack));
            }
        }
    }

    public void sendContainerToPlayer(Container par1Container)
    {
        this.sendContainerAndContentsToPlayer(par1Container, par1Container.getInventory());
    }

    public void sendContainerAndContentsToPlayer(Container par1Container, List par2List)
    {
        this.playerNetServerHandler.sendPacketToPlayer(new Packet104WindowItems(par1Container.windowId, par2List));
        this.playerNetServerHandler.sendPacketToPlayer(new Packet103SetSlot(-1, -1, this.inventory.getItemStack()));
    }

    public void updateCraftingInventoryInfo(Container par1Container, int par2, int par3)
    {
        this.playerNetServerHandler.sendPacketToPlayer(new Packet105UpdateProgressbar(par1Container.windowId, par2, par3));
    }

    public void closeScreen()
    {
        this.playerNetServerHandler.sendPacketToPlayer(new Packet101CloseWindow(this.craftingInventory.windowId));
        this.closeInventory();
    }

    public void sendInventoryToPlayer()
    {
        if (!this.playerInventoryBeingManipulated)
        {
            this.playerNetServerHandler.sendPacketToPlayer(new Packet103SetSlot(-1, -1, this.inventory.getItemStack()));
        }
    }

    public void closeInventory()
    {
        this.craftingInventory.onCraftGuiClosed(this);
        this.craftingInventory = this.inventorySlots;
    }

    public void addStat(StatBase par1StatBase, int par2)
    {
        if (par1StatBase != null)
        {
            if (!par1StatBase.isIndependent)
            {
                while (par2 > 100)
                {
                    this.playerNetServerHandler.sendPacketToPlayer(new Packet200Statistic(par1StatBase.statId, 100));
                    par2 -= 100;
                }

                this.playerNetServerHandler.sendPacketToPlayer(new Packet200Statistic(par1StatBase.statId, par2));
            }
        }
    }

    public void mountEntityAndWakeUp()
    {
        if (this.ridingEntity != null)
        {
            this.mountEntity(this.ridingEntity);
        }

        if (this.riddenByEntity != null)
        {
            this.riddenByEntity.mountEntity(this);
        }

        if (this.sleeping)
        {
            this.wakeUpPlayer(true, false, false);
        }
    }

    public void setPlayerHealthUpdated()
    {
        this.lastHealth = -99999999;
    }

    public void addChatMessage(String par1Str)
    {
        StringTranslate var2 = StringTranslate.getInstance();
        String var3 = var2.translateKey(par1Str);
        this.playerNetServerHandler.sendPacketToPlayer(new Packet3Chat(var3));
    }

    protected void onItemUseFinish()
    {
        this.playerNetServerHandler.sendPacketToPlayer(new Packet38EntityStatus(this.entityId, (byte)9));
        super.onItemUseFinish();
    }

    public void setItemInUse(ItemStack par1ItemStack, int par2)
    {
        super.setItemInUse(par1ItemStack, par2);

        if (par1ItemStack != null && par1ItemStack.getItem() != null && par1ItemStack.getItem().getItemUseAction(par1ItemStack) == EnumAction.eat)
        {
            this.getServerForPlayer().getEntityTracker().sendPacketToAllAssociatedPlayers(this, new Packet18Animation(this, 5));
        }
    }

    public void clonePlayer(EntityPlayer par1EntityPlayer, boolean par2)
    {
        super.clonePlayer(par1EntityPlayer, par2);
        this.lastExperience = -1;
        this.lastHealth = -1;
        this.lastFoodLevel = -1;
        this.destroyedItemsNetCache.addAll(((EntityPlayerMP)par1EntityPlayer).destroyedItemsNetCache);
    }

    protected void onNewPotionEffect(PotionEffect par1PotionEffect)
    {
        super.onNewPotionEffect(par1PotionEffect);
        this.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(this.entityId, par1PotionEffect));
    }

    protected void onChangedPotionEffect(PotionEffect par1PotionEffect)
    {
        super.onChangedPotionEffect(par1PotionEffect);
        this.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(this.entityId, par1PotionEffect));
    }

    protected void onFinishedPotionEffect(PotionEffect par1PotionEffect)
    {
        super.onFinishedPotionEffect(par1PotionEffect);
        this.playerNetServerHandler.sendPacketToPlayer(new Packet42RemoveEntityEffect(this.entityId, par1PotionEffect));
    }

    public void setPositionAndUpdate(double par1, double par3, double par5)
    {
        this.playerNetServerHandler.setPlayerLocation(par1, par3, par5, this.rotationYaw, this.rotationPitch);
    }

    public void onCriticalHit(Entity par1Entity)
    {
        this.getServerForPlayer().getEntityTracker().sendPacketToAllAssociatedPlayers(this, new Packet18Animation(par1Entity, 6));
    }

    public void onEnchantmentCritical(Entity par1Entity)
    {
        this.getServerForPlayer().getEntityTracker().sendPacketToAllAssociatedPlayers(this, new Packet18Animation(par1Entity, 7));
    }

    public void sendPlayerAbilities()
    {
        if (this.playerNetServerHandler != null)
        {
            this.playerNetServerHandler.sendPacketToPlayer(new Packet202PlayerAbilities(this.capabilities));
        }
    }

    public WorldServer getServerForPlayer()
    {
        return (WorldServer)this.worldObj;
    }

    public void sendGameTypeToPlayer(EnumGameType par1EnumGameType)
    {
        this.theItemInWorldManager.setGameType(par1EnumGameType);
        this.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(3, par1EnumGameType.getID()));
    }

    public void sendChatToPlayer(String par1Str)
    {
        this.playerNetServerHandler.sendPacketToPlayer(new Packet3Chat(par1Str));
    }

    public boolean canCommandSenderUseCommand(int par1, String par2Str)
    {
        return "seed".equals(par2Str) && !this.mcServer.isDedicatedServer() ? true : (!"tell".equals(par2Str) && !"help".equals(par2Str) && !"me".equals(par2Str) ? this.mcServer.getConfigurationManager().areCommandsAllowed(this.username) : true);
    }

    public String func_71114_r()
    {
        String var1 = this.playerNetServerHandler.netManager.getSocketAddress().toString();
        var1 = var1.substring(var1.indexOf("/") + 1);
        var1 = var1.substring(0, var1.indexOf(":"));
        return var1;
    }

    public void updateClientInfo(Packet204ClientInfo par1Packet204ClientInfo)
    {
        if (this.translator.getLanguageList().containsKey(par1Packet204ClientInfo.getLanguage()))
        {
            this.translator.setLanguage(par1Packet204ClientInfo.getLanguage());
        }

        int var2 = 256 >> par1Packet204ClientInfo.getRenderDistance();

        if (var2 > 3 && var2 < 15)
        {
            this.renderDistance = var2;
        }

        this.chatVisibility = par1Packet204ClientInfo.getChatVisibility();
        this.chatColours = par1Packet204ClientInfo.getChatColours();

        if (this.mcServer.isSinglePlayer() && this.mcServer.getServerOwner().equals(this.username))
        {
            this.mcServer.setDifficultyForAllWorlds(par1Packet204ClientInfo.getDifficulty());
        }

        this.func_82239_b(1, !par1Packet204ClientInfo.func_82563_j());
    }

    public StringTranslate getTranslator()
    {
        return this.translator;
    }

    public int getChatVisibility()
    {
        return this.chatVisibility;
    }

    public void requestTexturePackLoad(String par1Str, int par2)
    {
        String var3 = par1Str + "\u0000" + par2;
        this.playerNetServerHandler.sendPacketToPlayer(new Packet250CustomPayload("MC|TPack", var3.getBytes()));
    }

    public ChunkCoordinates getPlayerCoordinates()
    {
        return new ChunkCoordinates(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY + 0.5D), MathHelper.floor_double(this.posZ));
    }
}

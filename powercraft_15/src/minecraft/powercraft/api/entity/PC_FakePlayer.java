package powercraft.api.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PC_FakePlayer extends EntityPlayer
{
    public PC_FakePlayer(World world)
    {
        super(world);
        inventory = new InventoryPlayer(this);
        inventory.currentItem = 0;
        inventory.setInventorySlotContents(0, new ItemStack(Item.pickaxeDiamond, 1, 0));
        foodStats = new FoodStats();
        flyToggleTimer = 0;
        isSwingInProgress = false;
        swingProgressInt = 0;
        xpCooldown = 0;
        timeUntilPortal = 20;
        inPortal = false;
        capabilities = new PlayerCapabilities();
        speedOnGround = 0.1F;
        speedInAir = 0.02F;
        fishEntity = null;
        inventoryContainer = new ContainerPlayer(inventory, !world.isRemote, this);
        openContainer = inventoryContainer;
        yOffset = 1.62F;
        entityType = "humanoid";
        field_70741_aB = 180.0F;
        fireResistance = 20;
        texture = "";
        username = "";
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
    }

    @Override
    public void onUpdate() {}

    @Override
    public void updateCloak() {}

    @Override
    public void preparePlayerToSpawn() {}

    @Override
    protected void updateEntityActionState() {}

    @Override
    public void onLivingUpdate() {}

    @Override
    public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
    }

    @Override
    public EntityItem dropPlayerItem(ItemStack itemstack)
    {
        return null;
    }

    @Override
    public EntityItem dropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag)
    {
        return null;
    }

    @Override
    public void joinEntityItemWithWorld(EntityItem entityitem) {}

    @Override
    public boolean canHarvestBlock(Block block)
    {
        return true;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {}

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {}

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        return false;
    }

    @Override
    protected int applyPotionDamageCalculations(DamageSource damagesource, int i)
    {
        return 0;
    }

    @Override
    protected void alertWolves(EntityLiving entityliving, boolean flag) {}

    @Override
    protected void damageArmor(int i) {}

    @Override
    public int getTotalArmorValue()
    {
        return 0;
    }

    @Override
    protected void damageEntity(DamageSource damagesource, int i) {}

    @Override
    public void displayGUIFurnace(TileEntityFurnace tileentityfurnace) {}

    @Override
    public void displayGUIDispenser(TileEntityDispenser tileentitydispenser) {}

    @Override
    public void displayGUIEditSign(TileEntity tileentitysign) {}

    @Override
    public void displayGUIBrewingStand(TileEntityBrewingStand tileentitybrewingstand) {}

    @Override
    public ItemStack getCurrentEquippedItem()
    {
        return null;
    }

    @Override
    public void destroyCurrentEquippedItem() {}

    @Override
    public double getYOffset()
    {
        return 0;
    }

    @Override
    public void swingItem() {}

    @Override
    public void attackTargetEntityWithCurrentItem(Entity entity) {}

    @Override
    public void onCriticalHit(Entity entity) {}

    @Override
    public void onEnchantmentCritical(Entity entity) {}

    @Override
    public void respawnPlayer() {}

    @Override
    public void setDead()
    {
        super.setDead();
    }

    @Override
    public boolean isEntityInsideOpaqueBlock()
    {
        return false;
    }

    @Override
    public EnumStatus sleepInBedAt(int i, int j, int k)
    {
        return EnumStatus.OK;
    }

    @Override
    public void wakeUpPlayer(boolean flag, boolean flag1, boolean flag2) {}

    @Override
    public float getBedOrientationInDegrees()
    {
        return 0.0F;
    }

    @Override
    public boolean isPlayerSleeping()
    {
        return sleeping;
    }

    @Override
    public boolean isPlayerFullyAsleep()
    {
        return false;
    }

    @Override
    public int getSleepTimer()
    {
        return 0;
    }

    @Override
    public void addChatMessage(String s) {}

    @Override
    public void triggerAchievement(StatBase statbase) {}

    @Override
    public void addStat(StatBase statbase, int i) {}

    @Override
    public void addToPlayerScore(Entity par1Entity, int par2) {}

    @Override
    protected void jump() {}

    @Override
    public void moveEntityWithHeading(float f, float f1) {}

    @Override
    public void addMovementStat(double d, double d1, double d2) {}

    @Override
    protected void fall(float f) {}

    @Override
    public void onKillEntity(EntityLiving entityliving) {}

    @Override
    public void setInPortal() {}

    @Override
    public int xpBarCap()
    {
        return 1000;
    }

    @Override
    public void addExhaustion(float f) {}

    @Override
    public FoodStats getFoodStats()
    {
        return foodStats;
    }

    @Override
    public boolean canEat(boolean flag)
    {
        return false;
    }

    @Override
    public boolean shouldHeal()
    {
        return false;
    }

    @Override
    public void setItemInUse(ItemStack itemstack, int i) {}

    @Override
    public boolean canPlayerEdit(int i, int j, int k, int par4, ItemStack par5ItemStack)
    {
        return true;
    }

    @Override
    protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return 0;
    }

    @Override
    protected boolean isPlayer()
    {
        return true;
    }

    @Override
    public void travelToDimension(int i) {}

    @Override
    public void sendChatToPlayer(String var1)
    {
    }

    @Override
    public boolean canCommandSenderUseCommand(int var1, String var2)
    {
        return false;
    }

    @Override
    public ChunkCoordinates getBedLocation()
    {
        return new ChunkCoordinates(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY + 0.5D), MathHelper.floor_double(this.posZ));
    }

    @Override
    public ChunkCoordinates getPlayerCoordinates()
    {
        return new ChunkCoordinates(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY + 0.5D), MathHelper.floor_double(this.posZ));
    }
}

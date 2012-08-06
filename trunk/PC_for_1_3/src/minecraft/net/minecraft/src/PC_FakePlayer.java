// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst

package net.minecraft.src;


/**
 * Fake player entity, which is used in calls requiring player instance. All
 * methods are modified to do nothing, to prevent modification of player stats.
 * 
 * @author MightyPork
 * @copy 2012
 */
public class PC_FakePlayer extends EntityPlayer {
	/**
	 * Create fake player in world
	 * 
	 * @param world the world
	 */
	public PC_FakePlayer(World world) {
		super(world);
		inventory = new InventoryPlayer(this);
		inventory.currentItem = 0;
		inventory.setInventorySlotContents(0, new ItemStack(Item.pickaxeDiamond, 1, 0));
		foodStats = new FoodStats();
		flyToggleTimer = 0;
		score = 0;
		isSwinging = false;
		swingProgressInt = 0;
		xpCooldown = 0;
		timeUntilPortal = 20;
		inPortal = false;
		capabilities = new PlayerCapabilities();
		speedOnGround = 0.1F;
		speedInAir = 0.02F;
		fishEntity = null;
		inventorySlots = new ContainerPlayer(inventory, !world.isRemote);
		craftingInventory = inventorySlots;
		yOffset = 1.62F;
		entityType = "humanoid";
		field_70741_aB = 180.0F;
		fireResistance = 20;
		texture = "";
		username = "";
	}

	@Override
	protected void entityInit() {
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
	public void onDeath(DamageSource damagesource) {
		super.onDeath(damagesource);
	}

	@Override
	public EntityItem dropPlayerItem(ItemStack itemstack) {
		return null;
	}

	@Override
	public EntityItem dropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag) {
		return null;
	}

	@Override
	protected void joinEntityItemWithWorld(EntityItem entityitem) {}

	@Override
	public float getCurrentPlayerStrVsBlock(Block block) {
		return 20;
	}

	@Override
	public boolean canHarvestBlock(Block block) {
		return true;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, int i) {
		return false;
	}

	@Override
	protected int applyPotionDamageCalculations(DamageSource damagesource, int i) {
		return 0;
	}

	@Override
	protected boolean isPVPEnabled() {
		return false;
	}

	@Override
	protected void alertWolves(EntityLiving entityliving, boolean flag) {}

	@Override
	protected void damageArmor(int i) {}

	@Override
	public int getTotalArmorValue() {
		return 0;
	}

	@Override
	protected void damageEntity(DamageSource damagesource, int i) {}

	@Override
	public void displayGUIFurnace(TileEntityFurnace tileentityfurnace) {}

	@Override
	public void displayGUIDispenser(TileEntityDispenser tileentitydispenser) {}

	@Override
	public void displayGUIEditSign(TileEntitySign tileentitysign) {}

	@Override
	public void displayGUIBrewingStand(TileEntityBrewingStand tileentitybrewingstand) {}

	@Override
	public ItemStack getCurrentEquippedItem() {
		return null;
	}

	@Override
	public void destroyCurrentEquippedItem() {}

	@Override
	public double getYOffset() {
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
	public void setDead() {
		super.setDead();
	}

	@Override
	public boolean isEntityInsideOpaqueBlock() {
		return false;
	}

	@Override
	public EnumStatus sleepInBedAt(int i, int j, int k) {
		return EnumStatus.OK;
	}

	@Override
	public void wakeUpPlayer(boolean flag, boolean flag1, boolean flag2) {}

	@Override
	public float getBedOrientationInDegrees() {
		return 0.0F;
	}

	@Override
	public boolean isPlayerSleeping() {
		return sleeping;
	}

	@Override
	public boolean isPlayerFullyAsleep() {
		return false;
	}

	@Override
	public int getSleepTimer() {
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
	public int getItemIcon(ItemStack itemstack, int i) {
		return 0;
	}

	@Override
	public void setInPortal() {}

	@Override
	public int xpBarCap() {
		return 1000;
	}

	@Override
	public void addExhaustion(float f) {}

	@Override
	public FoodStats getFoodStats() {
		return foodStats;
	}

	@Override
	public boolean canEat(boolean flag) {
		return false;
	}

	@Override
	public boolean shouldHeal() {
		return false;
	}

	@Override
	public void setItemInUse(ItemStack itemstack, int i) {}

	@Override
	public boolean canPlayerEdit(int i, int j, int k) {
		return true;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer) {
		return 0;
	}

	@Override
	protected boolean isPlayer() {
		return true;
	}

	@Override
	public void travelToTheEnd(int i) {}

	@Override
	public void sendChatToPlayer(String var1) {
	}

	@Override
	public boolean canCommandSenderUseCommand(String var1) {
		return false;
	}
}

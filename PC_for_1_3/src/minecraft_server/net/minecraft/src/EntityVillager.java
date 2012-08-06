package net.minecraft.src;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EntityVillager extends EntityAgeable implements INpc, IMerchant
{
    private int randomTickDivider;
    private boolean isMating;
    private boolean isPlaying;
    Village villageObj;

    /** This villager's current customer. */
    private EntityPlayer buyingPlayer;

    /** Initialises the MerchantRecipeList.java */
    private MerchantRecipeList buyingList;
    private int field_70961_j;
    private boolean field_70959_by;
    private int field_70956_bz;

    /** Recipes for buying things from Villagers. */
    private MerchantRecipe sellingRecipeList;

    /** Selling list of Villagers items. */
    private static final Map villagersSellingList = new HashMap();

    /** Selling list of Blacksmith items. */
    private static final Map blacksmithSellingList = new HashMap();

    public EntityVillager(World par1World)
    {
        this(par1World, 0);
    }

    public EntityVillager(World par1World, int par2)
    {
        super(par1World);
        this.randomTickDivider = 0;
        this.isMating = false;
        this.isPlaying = false;
        this.villageObj = null;
        this.setProfession(par2);
        this.texture = "/mob/villager/villager.png";
        this.moveSpeed = 0.5F;
        this.getNavigator().setBreakDoors(true);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.3F, 0.35F));
        this.tasks.addTask(1, new EntityAITradePlayer(this));
        this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTwardsRestriction(this, 0.3F));
        this.tasks.addTask(6, new EntityAIVillagerMate(this));
        this.tasks.addTask(7, new EntityAIFollowGolem(this));
        this.tasks.addTask(8, new EntityAIPlay(this, 0.32F));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityVillager.class, 5.0F, 0.02F));
        this.tasks.addTask(9, new EntityAIWander(this, 0.3F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    protected void updateAITick()
    {
        if (--this.randomTickDivider <= 0)
        {
            this.worldObj.villageCollectionObj.addVillagerPosition(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
            this.randomTickDivider = 70 + this.rand.nextInt(50);
            this.villageObj = this.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 32);

            if (this.villageObj == null)
            {
                this.detachHome();
            }
            else
            {
                ChunkCoordinates var1 = this.villageObj.getCenter();
                this.setHomeArea(var1.posX, var1.posY, var1.posZ, this.villageObj.getVillageRadius());
            }
        }

        if (!this.isTrading() && this.field_70961_j > 0)
        {
            --this.field_70961_j;

            if (this.field_70961_j <= 0)
            {
                if (this.field_70959_by)
                {
                    this.func_70950_c(1);
                    this.field_70959_by = false;
                }

                if (this.sellingRecipeList != null)
                {
                    this.buyingList.remove(this.sellingRecipeList);
                    this.sellingRecipeList = null;
                }

                this.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
            }
        }

        super.updateAITick();
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        if (this.isEntityAlive() && !this.isTrading() && !this.isChild())
        {
            if (!this.worldObj.isRemote)
            {
                this.setCustomer(par1EntityPlayer);
                par1EntityPlayer.func_71030_a(this);
            }

            return true;
        }
        else
        {
            return super.interact(par1EntityPlayer);
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Integer.valueOf(0));
    }

    public int getMaxHealth()
    {
        return 20;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("Profession", this.getProfession());
        par1NBTTagCompound.setInteger("Riches", this.field_70956_bz);

        if (this.buyingList != null)
        {
            par1NBTTagCompound.setCompoundTag("Offers", this.buyingList.func_77202_a());
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.setProfession(par1NBTTagCompound.getInteger("Profession"));
        this.field_70956_bz = par1NBTTagCompound.getInteger("Riches");

        if (par1NBTTagCompound.hasKey("Offers"))
        {
            NBTTagCompound var2 = par1NBTTagCompound.getCompoundTag("Offers");
            this.buyingList = new MerchantRecipeList(var2);
        }
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return false;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.villager.default";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.villager.defaulthurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.villager.defaultdeath";
    }

    public void setProfession(int par1)
    {
        this.dataWatcher.updateObject(16, Integer.valueOf(par1));
    }

    public int getProfession()
    {
        return this.dataWatcher.getWatchableObjectInt(16);
    }

    public boolean isMating()
    {
        return this.isMating;
    }

    public void setMating(boolean par1)
    {
        this.isMating = par1;
    }

    public void setPlaying(boolean par1)
    {
        this.isPlaying = par1;
    }

    public boolean isPlaying()
    {
        return this.isPlaying;
    }

    public void setRevengeTarget(EntityLiving par1EntityLiving)
    {
        super.setRevengeTarget(par1EntityLiving);

        if (this.villageObj != null && par1EntityLiving != null)
        {
            this.villageObj.addOrRenewAgressor(par1EntityLiving);
        }
    }

    public void setCustomer(EntityPlayer par1EntityPlayer)
    {
        this.buyingPlayer = par1EntityPlayer;
    }

    public EntityPlayer getCustomer()
    {
        return this.buyingPlayer;
    }

    public boolean isTrading()
    {
        return this.buyingPlayer != null;
    }

    public void func_70933_a(MerchantRecipe par1MerchantRecipe)
    {
        par1MerchantRecipe.func_77399_f();

        if (par1MerchantRecipe.func_77393_a((MerchantRecipe)this.buyingList.get(this.buyingList.size() - 1)))
        {
            this.field_70961_j = 60;
            this.field_70959_by = true;
        }
        else if (this.buyingList.size() > 1)
        {
            int var2 = this.rand.nextInt(6) + this.rand.nextInt(6) + 3;

            if (var2 <= par1MerchantRecipe.func_77392_e())
            {
                this.field_70961_j = 20;
                this.sellingRecipeList = par1MerchantRecipe;
            }
        }

        if (par1MerchantRecipe.getItemToBuy().itemID == Item.emerald.shiftedIndex)
        {
            this.field_70956_bz += par1MerchantRecipe.getItemToBuy().stackSize;
        }
    }

    public MerchantRecipeList func_70934_b(EntityPlayer par1EntityPlayer)
    {
        if (this.buyingList == null)
        {
            this.func_70950_c(1);
        }

        return this.buyingList;
    }

    private void func_70950_c(int par1)
    {
        MerchantRecipeList var2;
        var2 = new MerchantRecipeList();
        label44:

        switch (this.getProfession())
        {
            case 0:
                func_70948_a(var2, Item.wheat.shiftedIndex, this.rand, 0.9F);
                func_70948_a(var2, Block.cloth.blockID, this.rand, 0.5F);
                func_70948_a(var2, Item.chickenRaw.shiftedIndex, this.rand, 0.5F);
                func_70948_a(var2, Item.fishCooked.shiftedIndex, this.rand, 0.4F);
                addMerchantItem(var2, Item.bread.shiftedIndex, this.rand, 0.9F);
                addMerchantItem(var2, Item.melon.shiftedIndex, this.rand, 0.3F);
                addMerchantItem(var2, Item.appleRed.shiftedIndex, this.rand, 0.3F);
                addMerchantItem(var2, Item.cookie.shiftedIndex, this.rand, 0.3F);
                addMerchantItem(var2, Item.shears.shiftedIndex, this.rand, 0.3F);
                addMerchantItem(var2, Item.flintAndSteel.shiftedIndex, this.rand, 0.3F);
                addMerchantItem(var2, Item.chickenCooked.shiftedIndex, this.rand, 0.3F);
                addMerchantItem(var2, Item.arrow.shiftedIndex, this.rand, 0.5F);

                if (this.rand.nextFloat() < 0.5F)
                {
                    var2.add(new MerchantRecipe(new ItemStack(Block.gravel, 10), new ItemStack(Item.emerald), new ItemStack(Item.flint.shiftedIndex, 2 + this.rand.nextInt(2), 0)));
                }

                break;

            case 1:
                func_70948_a(var2, Item.paper.shiftedIndex, this.rand, 0.8F);
                func_70948_a(var2, Item.book.shiftedIndex, this.rand, 0.8F);
                func_70948_a(var2, Item.field_77823_bG.shiftedIndex, this.rand, 0.3F);
                addMerchantItem(var2, Block.bookShelf.blockID, this.rand, 0.8F);
                addMerchantItem(var2, Block.glass.blockID, this.rand, 0.2F);
                addMerchantItem(var2, Item.compass.shiftedIndex, this.rand, 0.2F);
                addMerchantItem(var2, Item.pocketSundial.shiftedIndex, this.rand, 0.2F);
                break;

            case 2:
                addMerchantItem(var2, Item.eyeOfEnder.shiftedIndex, this.rand, 0.3F);
                addMerchantItem(var2, Item.expBottle.shiftedIndex, this.rand, 0.2F);
                addMerchantItem(var2, Item.redstone.shiftedIndex, this.rand, 0.4F);
                addMerchantItem(var2, Block.glowStone.blockID, this.rand, 0.3F);
                int[] var3 = new int[] {Item.swordSteel.shiftedIndex, Item.swordDiamond.shiftedIndex, Item.plateSteel.shiftedIndex, Item.plateDiamond.shiftedIndex, Item.axeSteel.shiftedIndex, Item.axeDiamond.shiftedIndex, Item.pickaxeSteel.shiftedIndex, Item.pickaxeDiamond.shiftedIndex};
                int[] var4 = var3;
                int var5 = var3.length;
                int var6 = 0;

                while (true)
                {
                    if (var6 >= var5)
                    {
                        break label44;
                    }

                    int var7 = var4[var6];

                    if (this.rand.nextFloat() < 0.1F)
                    {
                        var2.add(new MerchantRecipe(new ItemStack(var7, 1, 0), new ItemStack(Item.emerald, 2 + this.rand.nextInt(3), 0), EnchantmentHelper.addRandomEnchantment(this.rand, new ItemStack(var7, 1, 0), 5 + this.rand.nextInt(15))));
                    }

                    ++var6;
                }

            case 3:
                func_70948_a(var2, Item.coal.shiftedIndex, this.rand, 0.7F);
                func_70948_a(var2, Item.ingotIron.shiftedIndex, this.rand, 0.5F);
                func_70948_a(var2, Item.ingotGold.shiftedIndex, this.rand, 0.5F);
                func_70948_a(var2, Item.diamond.shiftedIndex, this.rand, 0.5F);
                addMerchantItem(var2, Item.swordSteel.shiftedIndex, this.rand, 0.5F);
                addMerchantItem(var2, Item.swordDiamond.shiftedIndex, this.rand, 0.5F);
                addMerchantItem(var2, Item.axeSteel.shiftedIndex, this.rand, 0.3F);
                addMerchantItem(var2, Item.axeDiamond.shiftedIndex, this.rand, 0.3F);
                addMerchantItem(var2, Item.pickaxeSteel.shiftedIndex, this.rand, 0.5F);
                addMerchantItem(var2, Item.pickaxeDiamond.shiftedIndex, this.rand, 0.5F);
                addMerchantItem(var2, Item.shovelSteel.shiftedIndex, this.rand, 0.2F);
                addMerchantItem(var2, Item.shovelDiamond.shiftedIndex, this.rand, 0.2F);
                addMerchantItem(var2, Item.hoeSteel.shiftedIndex, this.rand, 0.2F);
                addMerchantItem(var2, Item.hoeDiamond.shiftedIndex, this.rand, 0.2F);
                addMerchantItem(var2, Item.bootsSteel.shiftedIndex, this.rand, 0.2F);
                addMerchantItem(var2, Item.bootsDiamond.shiftedIndex, this.rand, 0.2F);
                addMerchantItem(var2, Item.helmetSteel.shiftedIndex, this.rand, 0.2F);
                addMerchantItem(var2, Item.helmetDiamond.shiftedIndex, this.rand, 0.2F);
                addMerchantItem(var2, Item.plateSteel.shiftedIndex, this.rand, 0.2F);
                addMerchantItem(var2, Item.plateDiamond.shiftedIndex, this.rand, 0.2F);
                addMerchantItem(var2, Item.legsSteel.shiftedIndex, this.rand, 0.2F);
                addMerchantItem(var2, Item.legsDiamond.shiftedIndex, this.rand, 0.2F);
                addMerchantItem(var2, Item.bootsChain.shiftedIndex, this.rand, 0.1F);
                addMerchantItem(var2, Item.helmetChain.shiftedIndex, this.rand, 0.1F);
                addMerchantItem(var2, Item.plateChain.shiftedIndex, this.rand, 0.1F);
                addMerchantItem(var2, Item.legsChain.shiftedIndex, this.rand, 0.1F);
                break;

            case 4:
                func_70948_a(var2, Item.coal.shiftedIndex, this.rand, 0.7F);
                func_70948_a(var2, Item.porkRaw.shiftedIndex, this.rand, 0.5F);
                func_70948_a(var2, Item.beefRaw.shiftedIndex, this.rand, 0.5F);
                addMerchantItem(var2, Item.saddle.shiftedIndex, this.rand, 0.1F);
                addMerchantItem(var2, Item.plateLeather.shiftedIndex, this.rand, 0.3F);
                addMerchantItem(var2, Item.bootsLeather.shiftedIndex, this.rand, 0.3F);
                addMerchantItem(var2, Item.helmetLeather.shiftedIndex, this.rand, 0.3F);
                addMerchantItem(var2, Item.legsLeather.shiftedIndex, this.rand, 0.3F);
                addMerchantItem(var2, Item.porkCooked.shiftedIndex, this.rand, 0.3F);
                addMerchantItem(var2, Item.beefCooked.shiftedIndex, this.rand, 0.3F);
        }

        if (var2.isEmpty())
        {
            func_70948_a(var2, Item.ingotGold.shiftedIndex, this.rand, 1.0F);
        }

        Collections.shuffle(var2);

        if (this.buyingList == null)
        {
            this.buyingList = new MerchantRecipeList();
        }

        for (int var8 = 0; var8 < par1 && var8 < var2.size(); ++var8)
        {
            this.buyingList.func_77205_a((MerchantRecipe)var2.get(var8));
        }
    }

    private static void func_70948_a(MerchantRecipeList par0MerchantRecipeList, int par1, Random par2Random, float par3)
    {
        if (par2Random.nextFloat() < par3)
        {
            par0MerchantRecipeList.add(new MerchantRecipe(func_70951_a(par1, par2Random), Item.emerald));
        }
    }

    private static ItemStack func_70951_a(int par0, Random par1Random)
    {
        return new ItemStack(par0, func_70944_b(par0, par1Random), 0);
    }

    private static int func_70944_b(int par0, Random par1Random)
    {
        Tuple var2 = (Tuple)villagersSellingList.get(Integer.valueOf(par0));
        return var2 == null ? 1 : (((Integer)var2.getFirst()).intValue() >= ((Integer)var2.getSecond()).intValue() ? ((Integer)var2.getFirst()).intValue() : ((Integer)var2.getFirst()).intValue() + par1Random.nextInt(((Integer)var2.getSecond()).intValue() - ((Integer)var2.getFirst()).intValue()));
    }

    private static void addMerchantItem(MerchantRecipeList par0MerchantRecipeList, int par1, Random par2Random, float par3)
    {
        if (par2Random.nextFloat() < par3)
        {
            int var4 = func_70943_c(par1, par2Random);
            ItemStack var5;
            ItemStack var6;

            if (var4 < 0)
            {
                var5 = new ItemStack(Item.emerald.shiftedIndex, 1, 0);
                var6 = new ItemStack(par1, -var4, 0);
            }
            else
            {
                var5 = new ItemStack(Item.emerald.shiftedIndex, var4, 0);
                var6 = new ItemStack(par1, 1, 0);
            }

            par0MerchantRecipeList.add(new MerchantRecipe(var5, var6));
        }
    }

    private static int func_70943_c(int par0, Random par1Random)
    {
        Tuple var2 = (Tuple)blacksmithSellingList.get(Integer.valueOf(par0));
        return var2 == null ? 1 : (((Integer)var2.getFirst()).intValue() >= ((Integer)var2.getSecond()).intValue() ? ((Integer)var2.getFirst()).intValue() : ((Integer)var2.getFirst()).intValue() + par1Random.nextInt(((Integer)var2.getSecond()).intValue() - ((Integer)var2.getFirst()).intValue()));
    }

    static
    {
        villagersSellingList.put(Integer.valueOf(Item.coal.shiftedIndex), new Tuple(Integer.valueOf(16), Integer.valueOf(24)));
        villagersSellingList.put(Integer.valueOf(Item.ingotIron.shiftedIndex), new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
        villagersSellingList.put(Integer.valueOf(Item.ingotGold.shiftedIndex), new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
        villagersSellingList.put(Integer.valueOf(Item.diamond.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
        villagersSellingList.put(Integer.valueOf(Item.paper.shiftedIndex), new Tuple(Integer.valueOf(19), Integer.valueOf(30)));
        villagersSellingList.put(Integer.valueOf(Item.book.shiftedIndex), new Tuple(Integer.valueOf(12), Integer.valueOf(15)));
        villagersSellingList.put(Integer.valueOf(Item.field_77823_bG.shiftedIndex), new Tuple(Integer.valueOf(1), Integer.valueOf(1)));
        villagersSellingList.put(Integer.valueOf(Item.enderPearl.shiftedIndex), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
        villagersSellingList.put(Integer.valueOf(Item.eyeOfEnder.shiftedIndex), new Tuple(Integer.valueOf(2), Integer.valueOf(3)));
        villagersSellingList.put(Integer.valueOf(Item.porkRaw.shiftedIndex), new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
        villagersSellingList.put(Integer.valueOf(Item.beefRaw.shiftedIndex), new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
        villagersSellingList.put(Integer.valueOf(Item.chickenRaw.shiftedIndex), new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
        villagersSellingList.put(Integer.valueOf(Item.fishCooked.shiftedIndex), new Tuple(Integer.valueOf(9), Integer.valueOf(13)));
        villagersSellingList.put(Integer.valueOf(Item.seeds.shiftedIndex), new Tuple(Integer.valueOf(34), Integer.valueOf(48)));
        villagersSellingList.put(Integer.valueOf(Item.melonSeeds.shiftedIndex), new Tuple(Integer.valueOf(30), Integer.valueOf(38)));
        villagersSellingList.put(Integer.valueOf(Item.pumpkinSeeds.shiftedIndex), new Tuple(Integer.valueOf(30), Integer.valueOf(38)));
        villagersSellingList.put(Integer.valueOf(Item.wheat.shiftedIndex), new Tuple(Integer.valueOf(18), Integer.valueOf(22)));
        villagersSellingList.put(Integer.valueOf(Block.cloth.blockID), new Tuple(Integer.valueOf(14), Integer.valueOf(22)));
        villagersSellingList.put(Integer.valueOf(Item.rottenFlesh.shiftedIndex), new Tuple(Integer.valueOf(36), Integer.valueOf(64)));
        blacksmithSellingList.put(Integer.valueOf(Item.flintAndSteel.shiftedIndex), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
        blacksmithSellingList.put(Integer.valueOf(Item.shears.shiftedIndex), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
        blacksmithSellingList.put(Integer.valueOf(Item.swordSteel.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(11)));
        blacksmithSellingList.put(Integer.valueOf(Item.swordDiamond.shiftedIndex), new Tuple(Integer.valueOf(12), Integer.valueOf(14)));
        blacksmithSellingList.put(Integer.valueOf(Item.axeSteel.shiftedIndex), new Tuple(Integer.valueOf(6), Integer.valueOf(8)));
        blacksmithSellingList.put(Integer.valueOf(Item.axeDiamond.shiftedIndex), new Tuple(Integer.valueOf(9), Integer.valueOf(12)));
        blacksmithSellingList.put(Integer.valueOf(Item.pickaxeSteel.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(9)));
        blacksmithSellingList.put(Integer.valueOf(Item.pickaxeDiamond.shiftedIndex), new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
        blacksmithSellingList.put(Integer.valueOf(Item.shovelSteel.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
        blacksmithSellingList.put(Integer.valueOf(Item.shovelDiamond.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
        blacksmithSellingList.put(Integer.valueOf(Item.hoeSteel.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
        blacksmithSellingList.put(Integer.valueOf(Item.hoeDiamond.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
        blacksmithSellingList.put(Integer.valueOf(Item.bootsSteel.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
        blacksmithSellingList.put(Integer.valueOf(Item.bootsDiamond.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
        blacksmithSellingList.put(Integer.valueOf(Item.helmetSteel.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
        blacksmithSellingList.put(Integer.valueOf(Item.helmetDiamond.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
        blacksmithSellingList.put(Integer.valueOf(Item.plateSteel.shiftedIndex), new Tuple(Integer.valueOf(10), Integer.valueOf(14)));
        blacksmithSellingList.put(Integer.valueOf(Item.plateDiamond.shiftedIndex), new Tuple(Integer.valueOf(16), Integer.valueOf(19)));
        blacksmithSellingList.put(Integer.valueOf(Item.legsSteel.shiftedIndex), new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
        blacksmithSellingList.put(Integer.valueOf(Item.legsDiamond.shiftedIndex), new Tuple(Integer.valueOf(11), Integer.valueOf(14)));
        blacksmithSellingList.put(Integer.valueOf(Item.bootsChain.shiftedIndex), new Tuple(Integer.valueOf(5), Integer.valueOf(7)));
        blacksmithSellingList.put(Integer.valueOf(Item.helmetChain.shiftedIndex), new Tuple(Integer.valueOf(5), Integer.valueOf(7)));
        blacksmithSellingList.put(Integer.valueOf(Item.plateChain.shiftedIndex), new Tuple(Integer.valueOf(11), Integer.valueOf(15)));
        blacksmithSellingList.put(Integer.valueOf(Item.legsChain.shiftedIndex), new Tuple(Integer.valueOf(9), Integer.valueOf(11)));
        blacksmithSellingList.put(Integer.valueOf(Item.bread.shiftedIndex), new Tuple(Integer.valueOf(-4), Integer.valueOf(-2)));
        blacksmithSellingList.put(Integer.valueOf(Item.melon.shiftedIndex), new Tuple(Integer.valueOf(-8), Integer.valueOf(-4)));
        blacksmithSellingList.put(Integer.valueOf(Item.appleRed.shiftedIndex), new Tuple(Integer.valueOf(-8), Integer.valueOf(-4)));
        blacksmithSellingList.put(Integer.valueOf(Item.cookie.shiftedIndex), new Tuple(Integer.valueOf(-10), Integer.valueOf(-7)));
        blacksmithSellingList.put(Integer.valueOf(Block.glass.blockID), new Tuple(Integer.valueOf(-5), Integer.valueOf(-3)));
        blacksmithSellingList.put(Integer.valueOf(Block.bookShelf.blockID), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
        blacksmithSellingList.put(Integer.valueOf(Item.plateLeather.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(5)));
        blacksmithSellingList.put(Integer.valueOf(Item.bootsLeather.shiftedIndex), new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
        blacksmithSellingList.put(Integer.valueOf(Item.helmetLeather.shiftedIndex), new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
        blacksmithSellingList.put(Integer.valueOf(Item.legsLeather.shiftedIndex), new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
        blacksmithSellingList.put(Integer.valueOf(Item.saddle.shiftedIndex), new Tuple(Integer.valueOf(6), Integer.valueOf(8)));
        blacksmithSellingList.put(Integer.valueOf(Item.expBottle.shiftedIndex), new Tuple(Integer.valueOf(-4), Integer.valueOf(-1)));
        blacksmithSellingList.put(Integer.valueOf(Item.redstone.shiftedIndex), new Tuple(Integer.valueOf(-4), Integer.valueOf(-1)));
        blacksmithSellingList.put(Integer.valueOf(Item.compass.shiftedIndex), new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
        blacksmithSellingList.put(Integer.valueOf(Item.pocketSundial.shiftedIndex), new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
        blacksmithSellingList.put(Integer.valueOf(Block.glowStone.blockID), new Tuple(Integer.valueOf(-3), Integer.valueOf(-1)));
        blacksmithSellingList.put(Integer.valueOf(Item.porkCooked.shiftedIndex), new Tuple(Integer.valueOf(-7), Integer.valueOf(-5)));
        blacksmithSellingList.put(Integer.valueOf(Item.beefCooked.shiftedIndex), new Tuple(Integer.valueOf(-7), Integer.valueOf(-5)));
        blacksmithSellingList.put(Integer.valueOf(Item.chickenCooked.shiftedIndex), new Tuple(Integer.valueOf(-8), Integer.valueOf(-6)));
        blacksmithSellingList.put(Integer.valueOf(Item.eyeOfEnder.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(11)));
        blacksmithSellingList.put(Integer.valueOf(Item.arrow.shiftedIndex), new Tuple(Integer.valueOf(-5), Integer.valueOf(-19)));
    }
}

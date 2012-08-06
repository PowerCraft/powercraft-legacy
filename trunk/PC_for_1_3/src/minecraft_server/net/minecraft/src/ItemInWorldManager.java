package net.minecraft.src;

public class ItemInWorldManager
{
    /** The world object that this object is connected to. */
    public World theWorld;

    /** The EntityPlayerMP object that this object is connected to. */
    public EntityPlayerMP thisPlayerMP;
    private EnumGameType gameType;
    private boolean field_73088_d;
    private int initialDamage;
    private int curBlockX;
    private int curBlockY;
    private int curBlockZ;
    private int curblockDamage;
    private boolean field_73097_j;
    private int field_73098_k;
    private int field_73095_l;
    private int field_73096_m;
    private int field_73093_n;
    private int field_73094_o;

    public ItemInWorldManager(World par1World)
    {
        this.gameType = EnumGameType.NOT_SET;
        this.field_73094_o = -1;
        this.theWorld = par1World;
    }

    public void setGameType(EnumGameType par1EnumGameType)
    {
        this.gameType = par1EnumGameType;
        par1EnumGameType.configurePlayerCapabilities(this.thisPlayerMP.capabilities);
        this.thisPlayerMP.sendPlayerAbilities();
    }

    public EnumGameType getGameType()
    {
        return this.gameType;
    }

    /**
     * Get if we are in creative game mode.
     */
    public boolean isCreative()
    {
        return this.gameType.isCreative();
    }

    public void func_73077_b(EnumGameType par1EnumGameType)
    {
        if (this.gameType == EnumGameType.NOT_SET)
        {
            this.gameType = par1EnumGameType;
        }

        this.setGameType(this.gameType);
    }

    public void updateBlockRemoving()
    {
        ++this.curblockDamage;
        int var1;
        float var4;
        int var5;

        if (this.field_73097_j)
        {
            var1 = this.curblockDamage - this.field_73093_n;
            int var2 = this.theWorld.getBlockId(this.field_73098_k, this.field_73095_l, this.field_73096_m);

            if (var2 == 0)
            {
                this.field_73097_j = false;
            }
            else
            {
                Block var3 = Block.blocksList[var2];
                var4 = var3.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_73098_k, this.field_73095_l, this.field_73096_m) * (float)(var1 + 1);
                var5 = (int)(var4 * 10.0F);

                if (var5 != this.field_73094_o)
                {
                    this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.entityId, this.field_73098_k, this.field_73095_l, this.field_73096_m, var5);
                    this.field_73094_o = var5;
                }

                if (var4 >= 1.0F)
                {
                    this.field_73097_j = false;
                    this.tryHarvestBlock(this.field_73098_k, this.field_73095_l, this.field_73096_m);
                }
            }
        }
        else if (this.field_73088_d)
        {
            var1 = this.theWorld.getBlockId(this.curBlockX, this.curBlockY, this.curBlockZ);
            Block var6 = Block.blocksList[var1];

            if (var6 == null)
            {
                this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.entityId, this.curBlockX, this.curBlockY, this.curBlockZ, -1);
                this.field_73094_o = -1;
                this.field_73088_d = false;
            }
            else
            {
                int var7 = this.curblockDamage - this.initialDamage;
                var4 = var6.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.curBlockX, this.curBlockY, this.curBlockZ) * (float)(var7 + 1);
                var5 = (int)(var4 * 10.0F);

                if (var5 != this.field_73094_o)
                {
                    this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.entityId, this.curBlockX, this.curBlockY, this.curBlockZ, var5);
                    this.field_73094_o = var5;
                }
            }
        }
    }

    public void blockClicked(int par1, int par2, int par3, int par4)
    {
        if (!this.gameType.isAdventure())
        {
            if (this.isCreative())
            {
                if (!this.theWorld.extinguishFire((EntityPlayer)null, par1, par2, par3, par4))
                {
                    this.tryHarvestBlock(par1, par2, par3);
                }
            }
            else
            {
                this.theWorld.extinguishFire(this.thisPlayerMP, par1, par2, par3, par4);
                this.initialDamage = this.curblockDamage;
                float var5 = 1.0F;
                int var6 = this.theWorld.getBlockId(par1, par2, par3);

                if (var6 > 0)
                {
                    Block.blocksList[var6].onBlockClicked(this.theWorld, par1, par2, par3, this.thisPlayerMP);
                    var5 = Block.blocksList[var6].getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, par1, par2, par3);
                }

                if (var6 > 0 && var5 >= 1.0F)
                {
                    this.tryHarvestBlock(par1, par2, par3);
                }
                else
                {
                    this.field_73088_d = true;
                    this.curBlockX = par1;
                    this.curBlockY = par2;
                    this.curBlockZ = par3;
                    int var7 = (int)(var5 * 10.0F);
                    this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.entityId, par1, par2, par3, var7);
                    this.field_73094_o = var7;
                }
            }
        }
    }

    public void blockRemoving(int par1, int par2, int par3)
    {
        if (par1 == this.curBlockX && par2 == this.curBlockY && par3 == this.curBlockZ)
        {
            int var4 = this.curblockDamage - this.initialDamage;
            int var5 = this.theWorld.getBlockId(par1, par2, par3);

            if (var5 != 0)
            {
                Block var6 = Block.blocksList[var5];
                float var7 = var6.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, par1, par2, par3) * (float)(var4 + 1);

                if (var7 >= 0.7F)
                {
                    this.field_73088_d = false;
                    this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.entityId, par1, par2, par3, -1);
                    this.tryHarvestBlock(par1, par2, par3);
                }
                else if (!this.field_73097_j)
                {
                    this.field_73088_d = false;
                    this.field_73097_j = true;
                    this.field_73098_k = par1;
                    this.field_73095_l = par2;
                    this.field_73096_m = par3;
                    this.field_73093_n = this.initialDamage;
                }
            }
        }
    }

    public void func_73073_c(int par1, int par2, int par3)
    {
        this.field_73088_d = false;
        this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.entityId, this.curBlockX, this.curBlockY, this.curBlockZ, -1);
    }

    /**
     * Removes a block and triggers the appropriate events
     */
    private boolean removeBlock(int par1, int par2, int par3)
    {
        Block var4 = Block.blocksList[this.theWorld.getBlockId(par1, par2, par3)];
        int var5 = this.theWorld.getBlockMetadata(par1, par2, par3);

        if (var4 != null)
        {
            var4.onBlockHarvested(this.theWorld, par1, par2, par3, var5, this.thisPlayerMP);
        }

        boolean var6 = this.theWorld.setBlockWithNotify(par1, par2, par3, 0);

        if (var4 != null && var6)
        {
            var4.onBlockDestroyedByPlayer(this.theWorld, par1, par2, par3, var5);
        }

        return var6;
    }

    /**
     * Attempts to harvest a block at the given coordinate
     */
    public boolean tryHarvestBlock(int par1, int par2, int par3)
    {
        if (this.gameType.isAdventure())
        {
            return false;
        }
        else
        {
            int var4 = this.theWorld.getBlockId(par1, par2, par3);
            int var5 = this.theWorld.getBlockMetadata(par1, par2, par3);
            this.theWorld.playAuxSFXAtEntity(this.thisPlayerMP, 2001, par1, par2, par3, var4 + (this.theWorld.getBlockMetadata(par1, par2, par3) << 12));
            boolean var6 = this.removeBlock(par1, par2, par3);

            if (this.isCreative())
            {
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new Packet53BlockChange(par1, par2, par3, this.theWorld));
            }
            else
            {
                ItemStack var7 = this.thisPlayerMP.getCurrentEquippedItem();
                boolean var8 = this.thisPlayerMP.canHarvestBlock(Block.blocksList[var4]);

                if (var7 != null)
                {
                    var7.func_77941_a(this.theWorld, var4, par1, par2, par3, this.thisPlayerMP);

                    if (var7.stackSize == 0)
                    {
                        this.thisPlayerMP.destroyCurrentEquippedItem();
                    }
                }

                if (var6 && var8)
                {
                    Block.blocksList[var4].harvestBlock(this.theWorld, this.thisPlayerMP, par1, par2, par3, var5);
                }
            }

            return var6;
        }
    }

    /**
     * Attempts to right-click use an item by the given EntityPlayer in the given World
     */
    public boolean tryUseItem(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack)
    {
        int var4 = par3ItemStack.stackSize;
        int var5 = par3ItemStack.getItemDamage();
        ItemStack var6 = par3ItemStack.useItemRightClick(par2World, par1EntityPlayer);

        if (var6 == par3ItemStack && (var6 == null || var6.stackSize == var4) && (var6 == null || var6.getMaxItemUseDuration() <= 0))
        {
            return false;
        }
        else
        {
            par1EntityPlayer.inventory.mainInventory[par1EntityPlayer.inventory.currentItem] = var6;

            if (this.isCreative())
            {
                var6.stackSize = var4;
                var6.setItemDamage(var5);
            }

            if (var6.stackSize == 0)
            {
                par1EntityPlayer.inventory.mainInventory[par1EntityPlayer.inventory.currentItem] = null;
            }

            return true;
        }
    }

    /**
     * Activate the clicked on block, otherwise use the held item. Args: player, world, itemStack, x, y, z, side,
     * xOffset, yOffset, zOffset
     */
    public boolean activateBlockOrUseItem(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        int var11 = par2World.getBlockId(par4, par5, par6);

        if (var11 > 0 && Block.blocksList[var11].onBlockActivated(par2World, par4, par5, par6, par1EntityPlayer, par7, par8, par9, par10))
        {
            return true;
        }
        else if (par3ItemStack == null)
        {
            return false;
        }
        else if (this.isCreative())
        {
            int var12 = par3ItemStack.getItemDamage();
            int var13 = par3ItemStack.stackSize;
            boolean var14 = par3ItemStack.func_77943_a(par1EntityPlayer, par2World, par4, par5, par6, par7, par8, par9, par10);
            par3ItemStack.setItemDamage(var12);
            par3ItemStack.stackSize = var13;
            return var14;
        }
        else
        {
            return par3ItemStack.func_77943_a(par1EntityPlayer, par2World, par4, par5, par6, par7, par8, par9, par10);
        }
    }

    /**
     * Sets the world instance.
     */
    public void setWorld(WorldServer par1WorldServer)
    {
        this.theWorld = par1WorldServer;
    }
}

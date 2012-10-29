package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.registry.GameRegistry;

import java.util.Random;

public class BlockDispenser extends BlockContainer
{
    private Random random = new Random();

    protected BlockDispenser(int par1)
    {
        super(par1, Material.rock);
        this.blockIndexInTexture = 45;
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate()
    {
        return 4;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.dispenser.blockID;
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
        this.setDispenserDefaultDirection(par1World, par2, par3, par4);
    }

    /**
     * sets Dispenser block direction so that the front faces an non-opaque block; chooses west to be direction if all
     * surrounding blocks are opaque.
     */
    private void setDispenserDefaultDirection(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
            int var5 = par1World.getBlockId(par2, par3, par4 - 1);
            int var6 = par1World.getBlockId(par2, par3, par4 + 1);
            int var7 = par1World.getBlockId(par2 - 1, par3, par4);
            int var8 = par1World.getBlockId(par2 + 1, par3, par4);
            byte var9 = 3;

            if (Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var6])
            {
                var9 = 3;
            }

            if (Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var5])
            {
                var9 = 2;
            }

            if (Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var8])
            {
                var9 = 5;
            }

            if (Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var7])
            {
                var9 = 4;
            }

            par1World.setBlockMetadataWithNotify(par2, par3, par4, var9);
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public int getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (par5 == 1)
        {
            return this.blockIndexInTexture + 17;
        }
        else if (par5 == 0)
        {
            return this.blockIndexInTexture + 17;
        }
        else
        {
            int var6 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
            return par5 == var6 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
        }
    }

    /**
     * Returns the block texture based on the side being looked at.  Args: side
     */
    public int getBlockTextureFromSide(int par1)
    {
        return par1 == 1 ? this.blockIndexInTexture + 17 : (par1 == 0 ? this.blockIndexInTexture + 17 : (par1 == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture));
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityDispenser var10 = (TileEntityDispenser)par1World.getBlockTileEntity(par2, par3, par4);

            if (var10 != null)
            {
                par5EntityPlayer.displayGUIDispenser(var10);
            }

            return true;
        }
    }

    /**
     * dispenses an item from a randomly selected item stack from the blocks inventory into the game world.
     */
    private void dispenseItem(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        int var6 = par1World.getBlockMetadata(par2, par3, par4);
        byte var9 = 0;
        byte var10 = 0;

        if (var6 == 3)
        {
            var10 = 1;
        }
        else if (var6 == 2)
        {
            var10 = -1;
        }
        else if (var6 == 5)
        {
            var9 = 1;
        }
        else
        {
            var9 = -1;
        }

        TileEntityDispenser var11 = (TileEntityDispenser)par1World.getBlockTileEntity(par2, par3, par4);

        if (var11 != null)
        {
            int var12 = var11.getRandomStackFromInventory();

            if (var12 < 0)
            {
                par1World.playAuxSFX(1001, par2, par3, par4, 0);
            }
            else
            {
                double var13 = (double)par2 + (double)var9 * 0.6D + 0.5D;
                double var15 = (double)par3 + 0.5D;
                double var17 = (double)par4 + (double)var10 * 0.6D + 0.5D;
                ItemStack var19 = var11.getStackInSlot(var12);
                int var20 = spawnEntityWithAction(var11, par1World, var19, par5Random, par2, par3, par4, var9, var10, var13, var15, var17);

                if (var20 == 1)
                {
                    var11.decrStackSize(var12, 1);
                }
                else if (var20 == 0)
                {
                    var19 = var11.decrStackSize(var12, 1);
                    dispenseEntityFromStack(par1World, var19, par5Random, 6, var9, var10, var13, var15, var17);
                    par1World.playAuxSFX(1000, par2, par3, par4, 0);
                }

                par1World.playAuxSFX(2000, par2, par3, par4, var9 + 1 + (var10 + 1) * 3);
            }
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (par5 > 0 && Block.blocksList[par5].canProvidePower())
        {
            boolean var6 = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4) || par1World.isBlockIndirectlyGettingPowered(par2, par3 + 1, par4);

            if (var6)
            {
                par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate());
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote && (par1World.isBlockIndirectlyGettingPowered(par2, par3, par4) || par1World.isBlockIndirectlyGettingPowered(par2, par3 + 1, par4)))
        {
            this.dispenseItem(par1World, par2, par3, par4, par5Random);
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityDispenser();
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving)
    {
        int var6 = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (var6 == 0)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2);
        }

        if (var6 == 1)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 5);
        }

        if (var6 == 2)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3);
        }

        if (var6 == 3)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 4);
        }
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntityDispenser var7 = (TileEntityDispenser)par1World.getBlockTileEntity(par2, par3, par4);

        if (var7 != null)
        {
            for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
            {
                ItemStack var9 = var7.getStackInSlot(var8);

                if (var9 != null)
                {
                    float var10 = this.random.nextFloat() * 0.8F + 0.1F;
                    float var11 = this.random.nextFloat() * 0.8F + 0.1F;
                    float var12 = this.random.nextFloat() * 0.8F + 0.1F;

                    while (var9.stackSize > 0)
                    {
                        int var13 = this.random.nextInt(21) + 10;

                        if (var13 > var9.stackSize)
                        {
                            var13 = var9.stackSize;
                        }

                        var9.stackSize -= var13;
                        EntityItem var14 = new EntityItem(par1World, (double)((float)par2 + var10), (double)((float)par3 + var11), (double)((float)par4 + var12), new ItemStack(var9.itemID, var13, var9.getItemDamage()));

                        if (var9.hasTagCompound())
                        {
                            var14.item.setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                        }

                        float var15 = 0.05F;
                        var14.motionX = (double)((float)this.random.nextGaussian() * var15);
                        var14.motionY = (double)((float)this.random.nextGaussian() * var15 + 0.2F);
                        var14.motionZ = (double)((float)this.random.nextGaussian() * var15);
                        par1World.spawnEntityInWorld(var14);
                    }
                }
            }
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    private static void dispenseEntityFromStack(World par0World, ItemStack par1ItemStack, Random par2Random, int par3, int par4, int par5, double par6, double par8, double par10)
    {
        EntityItem var12 = new EntityItem(par0World, par6, par8 - 0.3D, par10, par1ItemStack);
        double var13 = par2Random.nextDouble() * 0.1D + 0.2D;
        var12.motionX = (double)par4 * var13;
        var12.motionY = 0.20000000298023224D;
        var12.motionZ = (double)par5 * var13;
        var12.motionX += par2Random.nextGaussian() * 0.007499999832361937D * (double)par3;
        var12.motionY += par2Random.nextGaussian() * 0.007499999832361937D * (double)par3;
        var12.motionZ += par2Random.nextGaussian() * 0.007499999832361937D * (double)par3;
        par0World.spawnEntityInWorld(var12);
    }

    /**
     * arrows are fired, eggs are thrown, buckets create liquid blocks ...
     */
    private static int spawnEntityWithAction(TileEntityDispenser par0TileEntityDispenser, World par1World, ItemStack par2ItemStack, Random par3Random, int par4, int par5, int par6, int par7, int par8, double par9, double par11, double par13)
    {
        float var15 = 1.1F;
        byte var16 = 6;
        int modDispense = GameRegistry.tryDispense(par1World, par4, par5, par6, par7, par8, par2ItemStack, par3Random, par9, par11, par13);
        if (modDispense > -1)
        {
            return modDispense;
        }
        if (par2ItemStack.itemID == Item.arrow.shiftedIndex)
        {
            EntityArrow var28 = new EntityArrow(par1World, par9, par11, par13);
            var28.setArrowHeading((double)par7, 0.10000000149011612D, (double)par8, var15, (float)var16);
            var28.canBePickedUp = 1;
            par1World.spawnEntityInWorld(var28);
            par1World.playAuxSFX(1002, par4, par5, par6, 0);
            return 1;
        }
        else if (par2ItemStack.itemID == Item.egg.shiftedIndex)
        {
            EntityEgg var29 = new EntityEgg(par1World, par9, par11, par13);
            var29.setThrowableHeading((double)par7, 0.10000000149011612D, (double)par8, var15, (float)var16);
            par1World.spawnEntityInWorld(var29);
            par1World.playAuxSFX(1002, par4, par5, par6, 0);
            return 1;
        }
        else if (par2ItemStack.itemID == Item.snowball.shiftedIndex)
        {
            EntitySnowball var24 = new EntitySnowball(par1World, par9, par11, par13);
            var24.setThrowableHeading((double)par7, 0.10000000149011612D, (double)par8, var15, (float)var16);
            par1World.spawnEntityInWorld(var24);
            par1World.playAuxSFX(1002, par4, par5, par6, 0);
            return 1;
        }
        else if (par2ItemStack.itemID == Item.potion.shiftedIndex && ItemPotion.isSplash(par2ItemStack.getItemDamage()))
        {
            EntityPotion var25 = new EntityPotion(par1World, par9, par11, par13, par2ItemStack.getItemDamage());
            var25.setThrowableHeading((double)par7, 0.10000000149011612D, (double)par8, var15 * 1.25F, (float)var16 * 0.5F);
            par1World.spawnEntityInWorld(var25);
            par1World.playAuxSFX(1002, par4, par5, par6, 0);
            return 1;
        }
        else if (par2ItemStack.itemID == Item.expBottle.shiftedIndex)
        {
            EntityExpBottle var26 = new EntityExpBottle(par1World, par9, par11, par13);
            var26.setThrowableHeading((double)par7, 0.10000000149011612D, (double)par8, var15 * 1.25F, (float)var16 * 0.5F);
            par1World.spawnEntityInWorld(var26);
            par1World.playAuxSFX(1002, par4, par5, par6, 0);
            return 1;
        }
        else if (par2ItemStack.itemID == Item.monsterPlacer.shiftedIndex)
        {
            ItemMonsterPlacer.spawnCreature(par1World, par2ItemStack.getItemDamage(), par9 + (double)par7 * 0.3D, par11 - 0.3D, par13 + (double)par8 * 0.3D);
            par1World.playAuxSFX(1002, par4, par5, par6, 0);
            return 1;
        }
        else if (par2ItemStack.itemID == Item.fireballCharge.shiftedIndex)
        {
            EntitySmallFireball var27 = new EntitySmallFireball(par1World, par9 + (double)par7 * 0.3D, par11, par13 + (double)par8 * 0.3D, (double)par7 + par3Random.nextGaussian() * 0.05D, par3Random.nextGaussian() * 0.05D, (double)par8 + par3Random.nextGaussian() * 0.05D);
            par1World.spawnEntityInWorld(var27);
            par1World.playAuxSFX(1009, par4, par5, par6, 0);
            return 1;
        }
        else if (par2ItemStack.itemID != Item.bucketLava.shiftedIndex && par2ItemStack.itemID != Item.bucketWater.shiftedIndex)
        {
            if (par2ItemStack.itemID == Item.bucketEmpty.shiftedIndex)
            {
                int var21 = par4 + par7;
                int var18 = par6 + par8;
                Material var19 = par1World.getBlockMaterial(var21, par5, var18);
                int var20 = par1World.getBlockMetadata(var21, par5, var18);

                if (var19 == Material.water && var20 == 0)
                {
                    par1World.setBlockWithNotify(var21, par5, var18, 0);

                    if (--par2ItemStack.stackSize == 0)
                    {
                        par2ItemStack.itemID = Item.bucketWater.shiftedIndex;
                        par2ItemStack.stackSize = 1;
                    }
                    else if (par0TileEntityDispenser.func_70360_a(new ItemStack(Item.bucketWater)) < 0)
                    {
                        dispenseEntityFromStack(par1World, new ItemStack(Item.bucketWater), par3Random, 6, par7, par8, par9, par11, par13);
                    }

                    return 2;
                }
                else if (var19 == Material.lava && var20 == 0)
                {
                    par1World.setBlockWithNotify(var21, par5, var18, 0);

                    if (--par2ItemStack.stackSize == 0)
                    {
                        par2ItemStack.itemID = Item.bucketLava.shiftedIndex;
                        par2ItemStack.stackSize = 1;
                    }
                    else if (par0TileEntityDispenser.func_70360_a(new ItemStack(Item.bucketLava)) < 0)
                    {
                        dispenseEntityFromStack(par1World, new ItemStack(Item.bucketLava), par3Random, 6, par7, par8, par9, par11, par13);
                    }

                    return 2;
                }
                else
                {
                    return 0;
                }
            }
            else if (par2ItemStack.getItem() instanceof ItemMinecart)
            {
                par9 = (double)par4 + (par7 < 0 ? (double)par7 * 0.8D : (double)((float)par7 * 1.8F)) + (double)((float)Math.abs(par8) * 0.5F);
                par13 = (double)par6 + (par8 < 0 ? (double)par8 * 0.8D : (double)((float)par8 * 1.8F)) + (double)((float)Math.abs(par7) * 0.5F);

                if (BlockRail.isRailBlockAt(par1World, par4 + par7, par5, par6 + par8))
                {
                    par11 = (double)((float)par5 + 0.5F);
                }
                else
                {
                    if (!par1World.isAirBlock(par4 + par7, par5, par6 + par8) || !BlockRail.isRailBlockAt(par1World, par4 + par7, par5 - 1, par6 + par8))
                    {
                        return 0;
                    }

                    par11 = (double)((float)par5 - 0.5F);
                }

                EntityMinecart var22 = new EntityMinecart(par1World, par9, par11, par13, ((ItemMinecart)par2ItemStack.getItem()).minecartType);
                par1World.spawnEntityInWorld(var22);
                par1World.playAuxSFX(1000, par4, par5, par6, 0);
                return 1;
            }
            else if (par2ItemStack.itemID == Item.boat.shiftedIndex)
            {
                par9 = (double)par4 + (par7 < 0 ? (double)par7 * 0.8D : (double)((float)par7 * 1.8F)) + (double)((float)Math.abs(par8) * 0.5F);
                par13 = (double)par6 + (par8 < 0 ? (double)par8 * 0.8D : (double)((float)par8 * 1.8F)) + (double)((float)Math.abs(par7) * 0.5F);

                if (par1World.getBlockMaterial(par4 + par7, par5, par6 + par8) == Material.water)
                {
                    par11 = (double)((float)par5 + 1.0F);
                }
                else
                {
                    if (!par1World.isAirBlock(par4 + par7, par5, par6 + par8) || par1World.getBlockMaterial(par4 + par7, par5 - 1, par6 + par8) != Material.water)
                    {
                        return 0;
                    }

                    par11 = (double)par5;
                }

                EntityBoat var23 = new EntityBoat(par1World, par9, par11, par13);
                par1World.spawnEntityInWorld(var23);
                par1World.playAuxSFX(1000, par4, par5, par6, 0);
                return 1;
            }
            else
            {
                return 0;
            }
        }
        else
        {
            ItemBucket var17 = (ItemBucket)par2ItemStack.getItem();

            if (var17.func_77875_a(par1World, (double)par4, (double)par5, (double)par6, par4 + par7, par5, par6 + par8))
            {
                par2ItemStack.itemID = Item.bucketEmpty.shiftedIndex;
                par2ItemStack.stackSize = 1;
                return 2;
            }
            else
            {
                return 0;
            }
        }
    }
}

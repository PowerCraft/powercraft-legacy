package net.minecraft.dispenser;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

final class DispenserBehaviorFire extends BehaviorDefaultDispenseItem
{
    private boolean field_96466_b = true;

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    protected ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        EnumFacing enumfacing = BlockDispenser.func_100009_j_(par1IBlockSource.func_82620_h());
        World world = par1IBlockSource.getWorld();
        int i = par1IBlockSource.getXInt() + enumfacing.getFrontOffsetX();
        int j = par1IBlockSource.getYInt() + enumfacing.func_96559_d();
        int k = par1IBlockSource.getZInt() + enumfacing.getFrontOffsetZ();

        if (world.isAirBlock(i, j, k))
        {
            world.func_94575_c(i, j, k, Block.fire.blockID);

            if (par2ItemStack.func_96631_a(1, world.rand))
            {
                par2ItemStack.stackSize = 0;
            }
        }
        else if (world.getBlockId(i, j, k) == Block.tnt.blockID)
        {
            Block.tnt.onBlockDestroyedByPlayer(world, i, j, k, 1);
            world.func_94571_i(i, j, k);
        }
        else
        {
            this.field_96466_b = false;
        }

        return par2ItemStack;
    }

    /**
     * Play the dispense sound from the specified block.
     */
    protected void playDispenseSound(IBlockSource par1IBlockSource)
    {
        if (this.field_96466_b)
        {
            par1IBlockSource.getWorld().playAuxSFX(1000, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(), par1IBlockSource.getZInt(), 0);
        }
        else
        {
            par1IBlockSource.getWorld().playAuxSFX(1001, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(), par1IBlockSource.getZInt(), 0);
        }
    }
}

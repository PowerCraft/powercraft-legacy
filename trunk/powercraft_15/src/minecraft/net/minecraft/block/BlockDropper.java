package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class BlockDropper extends BlockDispenser
{
    private final IBehaviorDispenseItem field_96474_cR = new BehaviorDefaultDispenseItem();

    protected BlockDropper(int par1)
    {
        super(par1);
    }

    @SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister par1IconRegister)
    {
        this.field_94336_cN = par1IconRegister.func_94245_a("furnace_side");
        this.field_94463_c = par1IconRegister.func_94245_a("furnace_top");
        this.field_94462_cO = par1IconRegister.func_94245_a("dropper_front");
        this.field_96473_e = par1IconRegister.func_94245_a("dropper_front_vertical");
    }

    protected IBehaviorDispenseItem func_96472_a(ItemStack par1ItemStack)
    {
        return this.field_96474_cR;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityDropper();
    }

    protected void dispense(World par1World, int par2, int par3, int par4)
    {
        BlockSourceImpl blocksourceimpl = new BlockSourceImpl(par1World, par2, par3, par4);
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser)blocksourceimpl.func_82619_j();

        if (tileentitydispenser != null)
        {
            int l = tileentitydispenser.getRandomStackFromInventory();

            if (l < 0)
            {
                par1World.playAuxSFX(1001, par2, par3, par4, 0);
            }
            else
            {
                ItemStack itemstack = tileentitydispenser.getStackInSlot(l);
                int i1 = par1World.getBlockMetadata(par2, par3, par4) & 7;
                IInventory iinventory = TileEntityHopper.func_96117_b(par1World, (double)(par2 + Facing.offsetsXForSide[i1]), (double)(par3 + Facing.offsetsYForSide[i1]), (double)(par4 + Facing.offsetsZForSide[i1]));
                ItemStack itemstack1;

                if (iinventory != null)
                {
                    itemstack1 = TileEntityHopper.func_94117_a(iinventory, itemstack.copy().splitStack(1), Facing.faceToSide[i1]);

                    if (itemstack1 == null)
                    {
                        itemstack1 = itemstack.copy();

                        if (--itemstack1.stackSize == 0)
                        {
                            itemstack1 = null;
                        }
                    }
                    else
                    {
                        itemstack1 = itemstack.copy();
                    }
                }
                else
                {
                    itemstack1 = this.field_96474_cR.dispense(blocksourceimpl, itemstack);

                    if (itemstack1 != null && itemstack1.stackSize == 0)
                    {
                        itemstack1 = null;
                    }
                }

                tileentitydispenser.setInventorySlotContents(l, itemstack1);
            }
        }
    }
}

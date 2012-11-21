package powercraft.light;

import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.core.PC_Block;
import powercraft.core.PC_CoordI;
import powercraft.core.PC_ICraftingToolDisplayer;
import powercraft.core.PC_MathHelper;
import powercraft.core.PC_Utils;

public class PCli_BlockLaser extends PC_Block implements PC_ICraftingToolDisplayer
{
    public PCli_BlockLaser(int id)
    {
        super(id, Material.ground);
        setStepSound(Block.soundMetalFootstep);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setHardness(0.7F);
        setResistance(10.0F);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public String getDefaultName()
    {
        return "Laser";
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new PCli_TileEntityLaser();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return AxisAlignedBB.getBoundingBox(i, j, k, (double) i + 1, (double) j + 0.7F, (double) k + 1);
    }

    @Override
    public void setBlockBoundsForItemRender()
    {
        setBlockBounds(0.3F, 0.3F, 0.2F, 0.7F, 0.7F, 0.8F);
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
    {
        int l = PC_MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        boolean reverse = false;

        if (entityliving instanceof EntityPlayer)
        {
            PC_Utils.isPlacingReversed((EntityPlayer)entityliving);
        }

        if (reverse)
        {
            l = PC_Utils.reverseSide(l);
        }

        if (l == 0)
        {
            l = 2;
        }
        else if (l == 1)
        {
            l = 5;
        }
        else if (l == 2)
        {
            l = 3;
        }
        else if (l == 3)
        {
            l = 4;
        }

        world.setBlockMetadataWithNotify(i, j, k, l);
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public boolean isPoweringTo(IBlockAccess world, int i, int j, int k, int l)
    {
        return ((PCli_TileEntityLaser) PC_Utils.getTE(world, i, j, k)).isActive();
    }

    @Override
    public boolean isIndirectlyPoweringTo(IBlockAccess world, int i, int j, int k, int l)
    {
        return isPoweringTo(world, i, j, k, l);
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int metadata, EntityPlayer player)
    {
        if (!PC_Utils.isCreative(player))
        {
            PCli_TileEntityLaser te = PC_Utils.getTE(world, x, y, z, blockID);

            if (te != null)
            {
                PC_Utils.dropItemStack(world, new ItemStack(mod_PowerCraftLight.getInstance().laserComposition, 1, te.getType()), new PC_CoordI(x, y, z));
            }
        }
    }

    @Override
    public void breakBlock(World world, int i, int j, int k, int par5, int par6)
    {
        PC_Utils.hugeUpdate(world, i, j, k, blockID);
        super.breakBlock(world, i, j, k, par5, par6);
    }

	@Override
	public String getCraftingToolModule() {
		return mod_PowerCraftLight.getInstance().getNameWithoutPowerCraft();
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}
}

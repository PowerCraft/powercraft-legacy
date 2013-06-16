package powercraft.deco;

import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityEnderEye;
import net.minecraft.src.EntityFireworkRocket;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_Utils;

@PC_BlockInfo(name="Iron Frame", tileEntity=PCde_TileEntityIronFrame.class)
public class PCde_BlockIronFrame extends PC_Block implements PC_IItemInfo {

	public PCde_BlockIronFrame(int id) {
		super(id, Material.rock, "ironframe");
		setHardness(1.5F);
		setResistance(50.0F);
		setStepSound(Block.soundMetalFootstep);
		setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}

	public boolean renderInventoryBlock(int metadata, Object renderer) {

		float s = 0.1875F;

		// pillars
		setBlockBounds(0, 0, 0, s, 1, s);
		PC_Renderer.renderInvBox(renderer, this, 0);
		setBlockBounds(1 - s, 0, 0, 1, 1, s);
		PC_Renderer.renderInvBox(renderer, this, 0);
		setBlockBounds(0, 0, 1 - s, s, 1, 1);
		PC_Renderer.renderInvBox(renderer, this, 0);
		setBlockBounds(1 - s, 0, 1 - s, 1, 1, 1);
		PC_Renderer.renderInvBox(renderer, this, 0);

		// x-sticks
		setBlockBounds(s, 0, 0, 1 - s, s, s);
		PC_Renderer.renderInvBox(renderer, this, 0);
		setBlockBounds(s, 0, 1 - s, 1 - s, s, 1);
		PC_Renderer.renderInvBox(renderer, this, 0);
		setBlockBounds(s, 1 - s, 0, 1 - s, 1, s);
		PC_Renderer.renderInvBox(renderer, this, 0);
		setBlockBounds(s, 1 - s, 1 - s, 1 - s, 1, 1);
		PC_Renderer.renderInvBox(renderer, this, 0);

		// z-sticks
		setBlockBounds(0, 0, s, s, s, 1 - s);
		PC_Renderer.renderInvBox(renderer, this, 0);
		setBlockBounds(0, 1 - s, s, s, 1, 1 - s);
		PC_Renderer.renderInvBox(renderer, this, 0);

		setBlockBounds(1 - s, 0, s, 1, s, 1 - s);
		PC_Renderer.renderInvBox(renderer, this, 0);
		setBlockBounds(1 - s, 1 - s, s, 1, 1, 1 - s);
		PC_Renderer.renderInvBox(renderer, this, 0);
		
		setBlockBounds(0, 0, 0, 1, 1, 1);
		
		return true;
		
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Object renderer) {
		return true;
	}
	
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity) {
		if(entity instanceof EntityEnderEye)
			return;
		if(entity instanceof EntityFireworkRocket)
			return;
		if(entity instanceof EntityItem)
			return;
		if(entity instanceof EntityXPOrb)
			return;
		if(PC_Utils.isEntityFX(entity))
			return;
		if(entity==null)
			return;
		setBlockBounds(0, 0, 0, 1, 1, 1);
		AxisAlignedBB axisalignedbb1 = super.getCollisionBoundingBoxFromPool(world, x, y, z);

        if (axisalignedbb1 != null && axisAlignedBB.intersectsWith(axisalignedbb1))
        {
        	list.add(axisalignedbb1);
        }
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
	}
	
}

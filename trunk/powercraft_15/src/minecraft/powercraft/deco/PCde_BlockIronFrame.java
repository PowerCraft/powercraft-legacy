package powercraft.deco;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

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
	
}

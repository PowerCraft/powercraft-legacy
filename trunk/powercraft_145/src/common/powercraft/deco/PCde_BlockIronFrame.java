package powercraft.deco;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils;

public class PCde_BlockIronFrame extends PC_Block implements PC_ICraftingToolDisplayer, PC_IBlockRenderer {

	public PCde_BlockIronFrame(int id) {
		super(22, Material.rock);
		setHardness(1.5F);
		setResistance(50.0F);
		setStepSound(Block.soundMetalFootstep);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public String getDefaultName() {
		return "Iron Frame";
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new PCde_TileEntityIronFrame();
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public String getCraftingToolModule() {
		return PCde_App.getInstance().getNameWithoutPowerCraft();
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer) {
		boolean swapped = PC_Renderer.swapTerrain(block);

		float s = 0.1875F;

		// pillars
		block.setBlockBounds(0, 0, 0, s, 1, s);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		block.setBlockBounds(1 - s, 0, 0, 1, 1, s);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		block.setBlockBounds(0, 0, 1 - s, s, 1, 1);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		block.setBlockBounds(1 - s, 0, 1 - s, 1, 1, 1);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);

		// x-sticks
		block.setBlockBounds(s, 0, 0, 1 - s, s, s);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		block.setBlockBounds(s, 0, 1 - s, 1 - s, s, 1);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		block.setBlockBounds(s, 1 - s, 0, 1 - s, 1, s);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		block.setBlockBounds(s, 1 - s, 1 - s, 1 - s, 1, 1);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);

		// z-sticks
		block.setBlockBounds(0, 0, s, s, s, 1 - s);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		block.setBlockBounds(0, 1 - s, s, s, 1, 1 - s);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);

		block.setBlockBounds(1 - s, 0, s, 1, s, 1 - s);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		block.setBlockBounds(1 - s, 1 - s, s, 1, 1, 1 - s);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		PC_Renderer.resetTerrain(swapped);
		
		block.setBlockBounds(0, 0, 0, 1, 1, 1);
		
	}

	@Override
	public void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer) {
		
	}

	@Override
	public List<String> getBlockFlags(World world, PC_CoordI pos, List<String> list) {

		list.add(PC_Utils.NO_HARVEST);
		list.add(PC_Utils.NO_PICKUP);
		list.add(PC_Utils.PASSIVE);

		return list;
	}

	@Override
	public List<String> getItemFlags(ItemStack stack, List<String> list) {
		list.add(PC_Utils.NO_BUILD);
		return list;
	}
	
}

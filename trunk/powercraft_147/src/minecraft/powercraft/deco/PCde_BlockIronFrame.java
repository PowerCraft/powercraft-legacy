package powercraft.deco;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;
import powercraft.management.annotation.PC_BlockInfo;
import powercraft.management.block.PC_Block;
import powercraft.management.item.PC_IItemInfo;
import powercraft.management.registry.PC_MSGRegistry;
import powercraft.management.renderer.PC_Renderer;

@PC_BlockInfo(tileEntity=PCde_TileEntityIronFrame.class)
public class PCde_BlockIronFrame extends PC_Block implements PC_IItemInfo {

	public PCde_BlockIronFrame(int id) {
		super(id, 22, Material.rock);
		setHardness(1.5F);
		setResistance(50.0F);
		setStepSound(Block.soundMetalFootstep);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PCde_TileEntityIronFrame();
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

	public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer) {
		boolean swapped = PC_Renderer.swapTerrain(block);

		float s = 0.1875F;

		// pillars
		ValueWriting.setBlockBounds(block, 0, 0, 0, s, 1, s);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		ValueWriting.setBlockBounds(block, 1 - s, 0, 0, 1, 1, s);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		ValueWriting.setBlockBounds(block, 0, 0, 1 - s, s, 1, 1);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		ValueWriting.setBlockBounds(block, 1 - s, 0, 1 - s, 1, 1, 1);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);

		// x-sticks
		ValueWriting.setBlockBounds(block, s, 0, 0, 1 - s, s, s);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		ValueWriting.setBlockBounds(block, s, 0, 1 - s, 1 - s, s, 1);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		ValueWriting.setBlockBounds(block, s, 1 - s, 0, 1 - s, 1, s);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		ValueWriting.setBlockBounds(block, s, 1 - s, 1 - s, 1 - s, 1, 1);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);

		// z-sticks
		ValueWriting.setBlockBounds(block, 0, 0, s, s, s, 1 - s);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		ValueWriting.setBlockBounds(block, 0, 1 - s, s, s, 1, 1 - s);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);

		ValueWriting.setBlockBounds(block, 1 - s, 0, s, 1, s, 1 - s);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		ValueWriting.setBlockBounds(block, 1 - s, 1 - s, s, 1, 1, 1 - s);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 21);
		PC_Renderer.resetTerrain(swapped);
		
		ValueWriting.setBlockBounds(block, 0, 0, 0, 1, 1, 1);
		
	}

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			return "Iron Frame";
		case PC_MSGRegistry.MSG_RENDER_INVENTORY_BLOCK:
			renderInventoryBlock((Block)obj[0], (Integer)obj[1], (Integer)obj[2], obj[3]);
			break;
		case PC_MSGRegistry.MSG_RENDER_WORLD_BLOCK:
			break;
		case PC_MSGRegistry.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
			list.add(PC_Utils.NO_HARVEST);
			list.add(PC_Utils.NO_PICKUP);
			list.add(PC_Utils.PASSIVE);
			return list;
		}
		case PC_MSGRegistry.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}
		case PC_MSGRegistry.MSG_CONDUCTIVITY:
			return 0.7f;
		default:
			return null;
		}
		return true;
	}
	
}

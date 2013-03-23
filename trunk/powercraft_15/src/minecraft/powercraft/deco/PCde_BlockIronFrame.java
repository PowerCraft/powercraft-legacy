package powercraft.deco;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_Utils;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.PC_VecI;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.renderer.PC_Renderer;

@PC_BlockInfo(tileEntity=PCde_TileEntityIronFrame.class)
public class PCde_BlockIronFrame extends PC_Block implements PC_IItemInfo {

	public PCde_BlockIronFrame(int id) {
		super(id, Material.rock, "ironframe");
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

		float s = 0.1875F;

		// pillars
		block.setBlockBounds(0, 0, 0, s, 1, s);
		PC_Renderer.renderInvBox(renderer, block, 0);
		block.setBlockBounds(1 - s, 0, 0, 1, 1, s);
		PC_Renderer.renderInvBox(renderer, block, 0);
		block.setBlockBounds(0, 0, 1 - s, s, 1, 1);
		PC_Renderer.renderInvBox(renderer, block, 0);
		block.setBlockBounds(1 - s, 0, 1 - s, 1, 1, 1);
		PC_Renderer.renderInvBox(renderer, block, 0);

		// x-sticks
		block.setBlockBounds(s, 0, 0, 1 - s, s, s);
		PC_Renderer.renderInvBox(renderer, block, 0);
		block.setBlockBounds(s, 0, 1 - s, 1 - s, s, 1);
		PC_Renderer.renderInvBox(renderer, block, 0);
		block.setBlockBounds(s, 1 - s, 0, 1 - s, 1, s);
		PC_Renderer.renderInvBox(renderer, block, 0);
		block.setBlockBounds(s, 1 - s, 1 - s, 1 - s, 1, 1);
		PC_Renderer.renderInvBox(renderer, block, 0);

		// z-sticks
		block.setBlockBounds(0, 0, s, s, s, 1 - s);
		PC_Renderer.renderInvBox(renderer, block, 0);
		block.setBlockBounds(0, 1 - s, s, s, 1, 1 - s);
		PC_Renderer.renderInvBox(renderer, block, 0);

		block.setBlockBounds(1 - s, 0, s, 1, s, 1 - s);
		PC_Renderer.renderInvBox(renderer, block, 0);
		block.setBlockBounds(1 - s, 1 - s, s, 1, 1, 1 - s);
		PC_Renderer.renderInvBox(renderer, block, 0);
		
		block.setBlockBounds(0, 0, 0, 1, 1, 1);
		
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

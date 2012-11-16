package powercraft.machines;

import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.core.PC_Block;
import powercraft.core.PC_IBlockRenderer;
import powercraft.core.PC_ICraftingToolDisplayer;
import powercraft.core.PC_IPowerReceiver;
import powercraft.core.PC_Renderer;
import powercraft.core.PC_Utils;

public class PCma_BlockTransmutabox extends PC_Block implements PC_IPowerReceiver, PC_IBlockRenderer, PC_ICraftingToolDisplayer {

	public PCma_BlockTransmutabox(int id) {
		super(id, 0, Material.rock);
		setHardness(1.5F);
		setResistance(50.0F);
		setStepSound(Block.soundMetalFootstep);
		setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
	public void receivePower(World world, int x, int y, int z, float power) {
		PCma_TileEntityTransmutabox te = PC_Utils.getTE(world, x, y, z, blockID);
		if(te!=null && power>200)
			te.change((int)(power-100)/10);
	}

	@Override
	public String getDefaultName() {
		return "Transmutabox";
	}

	@Override
	public boolean canBeHarvest() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new PCma_TileEntityTransmutabox();
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, 
			int z, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		
		PC_Utils.openGres("Transmutabox", player, x, y, z);
		
		return true;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer) {
		PC_Renderer.swapTerrain(block);

		block.setBlockBounds(0.1f, 0.1f, 0.1f, 0.9f, 0.9f, 0.9f);
		PC_Renderer.renderInvBox(renderer, block, metadata);
		
		block.setBlockBounds(0.0f, 0.0f, 0.0f, 0.2f, 0.2f, 0.2f);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 1);
		
		block.setBlockBounds(0.8f, 0.0f, 0.0f, 1.0f, 0.2f, 0.2f);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 1);
		
		block.setBlockBounds(0.8f, 0.8f, 0.0f, 1.0f, 1.0f, 0.2f);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 1);
		
		block.setBlockBounds(0.8f, 0.8f, 0.8f, 1.0f, 1.0f, 1.0f);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 1);
		
		block.setBlockBounds(0.0f, 0.8f, 0.8f, 0.2f, 1.0f, 1.0f);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 1);
		
		block.setBlockBounds(0.0f, 0.0f, 0.8f, 0.2f, 0.2f, 1.0f);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 1);
		
		block.setBlockBounds(0.0f, 0.8f, 0.0f, 0.2f, 1.0f, 0.2f);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 1);
		
		block.setBlockBounds(0.8f, 0.0f, 0.8f, 1.0f, 0.2f, 1.0f);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 1);
		
		block.setBlockBounds(0, 0, 0, 1, 1, 1);
		PC_Renderer.resetTerrain(true);
	}

	@Override
	public void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer) {
		PC_Renderer.tessellatorDraw();
		PC_Renderer.swapTerrain(block);
		PC_Renderer.tessellatorStartDrawingQuads();
		
		block.setBlockBounds(0.1f, 0.1f, 0.1f, 0.9f, 0.9f, 0.9f);
		PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
		
		block.setBlockBounds(0.0f, 0.0f, 0.0f, 0.2f, 0.2f, 0.2f);
		block.blockIndexInTexture = 1;
		PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
		
		block.setBlockBounds(0.8f, 0.0f, 0.0f, 1.0f, 0.2f, 0.2f);
		PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
		
		block.setBlockBounds(0.8f, 0.8f, 0.0f, 1.0f, 1.0f, 0.2f);
		PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
		
		block.setBlockBounds(0.8f, 0.8f, 0.8f, 1.0f, 1.0f, 1.0f);
		PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
		
		block.setBlockBounds(0.0f, 0.8f, 0.8f, 0.2f, 1.0f, 1.0f);
		PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
		
		block.setBlockBounds(0.0f, 0.0f, 0.8f, 0.2f, 0.2f, 1.0f);
		PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
		
		block.setBlockBounds(0.0f, 0.8f, 0.0f, 0.2f, 1.0f, 0.2f);
		PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
		
		block.setBlockBounds(0.8f, 0.0f, 0.8f, 1.0f, 0.2f, 1.0f);
		PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
		
		block.blockIndexInTexture = 0;
		
		PC_Renderer.tessellatorDraw();
		PC_Renderer.resetTerrain(true);
		PC_Renderer.tessellatorStartDrawingQuads();
	}

	@Override
	public String getCraftingToolModule() {
		return mod_PowerCraftMachines.getInstance().getNameWithoutPowerCraft();
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}
	
}

package powercraft.api;


import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import powercraft.api.blocks.PC_Block;
import powercraft.api.items.PC_Item;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class PC_Renderer implements ISimpleBlockRenderingHandler, IItemRenderer {

	private static int renderId;
	private static PC_Renderer renderer;

	private PC_Renderer() {

		renderId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(this);
		renderer = this;
		
	}


	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {

		renderInventoryBlock(block, metadata, renderer);
	}


	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

		return renderWorldBlock(world, x, y, z, block, renderer);
	}


	@Override
	public boolean shouldRender3DInInventory() {

		return true;
	}


	@Override
	public int getRenderId() {

		return renderId;
	}


	@Override
	public boolean handleRenderType(ItemStack itemStack, ItemRenderType type) {
		Item item = itemStack.getItem();
		if(item instanceof PC_Item){
			return ((PC_Item)item).handleRenderType(itemStack, type);
		}
		return false;
	}


	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack itemStack, ItemRendererHelper helper) {
		Item item = itemStack.getItem();
		if(item instanceof PC_Item){
			return ((PC_Item)item).shouldUseRenderHelper(itemStack, type, helper);
		}
		return false;
	}


	@Override
	public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
		Item item = itemStack.getItem();
		if(item instanceof PC_Item){
			((PC_Item)item).renderItem(itemStack, type, data);
		}
	}
	
	public static void create() {

		if (renderId == 0) {
			getRenderer();
		}
	}


	public static int getRenderType() {

		return renderId;
	}


	public static void renderInventoryBlock(Block block, int metadata, RenderBlocks renderer) {

		if (block instanceof PC_Block) {
			if (((PC_Block) block).renderInventoryBlock(metadata, renderer)) {
				return;
			}
		}
		renderInvBox(block, metadata, renderer);
	}


	public static boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer) {

		if (block instanceof PC_Block) {
			((PC_Block) block).setupRotation(world, x, y, z, renderer);
			if (((PC_Block) block).renderWorldBlock(world, x, y, z, renderer)) {
				resetRotation(renderer);
				return true;
			}
		}
		renderer.renderStandardBlock(block, x, y, z);
		resetRotation(renderer);
		return true;
	}

	public static void resetRotation(RenderBlocks renderer){
		renderer.uvRotateBottom = 0;
		renderer.uvRotateEast = 0;
		renderer.uvRotateNorth = 0;
		renderer.uvRotateSouth = 0;
		renderer.uvRotateTop = 0;
		renderer.uvRotateWest = 0;
	}

	public static void renderInvBox(Block block, int metadata, RenderBlocks renderer) {

		Tessellator tessellator = Tessellator.instance;

		Icon[] textures = new Icon[6];
		for (int a = 0; a < 6; a++) {
			textures[a] = block.getIcon(a, metadata);
		}

		block.setBlockBoundsForItemRender();
		renderer.setRenderBoundsFromBlock(block);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		if (textures[0] != null) {
			tessellator.setNormal(0.0F, -1F, 0.0F);
			renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, textures[0]);
		}
		if (textures[1] != null) {
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, textures[1]);
		}
		if (textures[2] != null) {
			tessellator.setNormal(0.0F, 0.0F, -1F);
			renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, textures[2]);
		}
		if (textures[3] != null) {
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, textures[3]);
		}
		if (textures[4] != null) {
			tessellator.setNormal(-1F, 0.0F, 0.0F);
			renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, textures[4]);
		}
		if (textures[5] != null) {
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, textures[5]);
		}
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		renderer.unlockBlockBounds();
	}


	public static PC_Renderer getRenderer() {
		if(renderer==null){
			renderer = new PC_Renderer();
		}
		return renderer;
	}

}

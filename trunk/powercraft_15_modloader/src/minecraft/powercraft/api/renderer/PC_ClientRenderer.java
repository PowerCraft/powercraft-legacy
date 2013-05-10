package powercraft.api.renderer;

import net.minecraft.src.Block;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.ModLoader;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderManager;
import net.minecraft.src.Tessellator;
import net.minecraft.src.mod_PowerCraft;

import org.lwjgl.opengl.GL11;

import powercraft.api.block.PC_Block;
import powercraft.api.interfaces.PC_IMSG;
import powercraft.api.inventory.PC_ISpecialInventoryTextures;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.utils.PC_ClientUtils;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecF;

public class PC_ClientRenderer extends PC_Renderer {
	
	public PC_ClientRenderer(boolean render3d) {
		super(render3d);
		if (render3d)
			render3dId = ModLoader.getUniqueBlockModelID(mod_PowerCraft.getInstance(), true);
		else
			render2dId = ModLoader.getUniqueBlockModelID(mod_PowerCraft.getInstance(), false);
	}
	
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		if (block instanceof PC_Block) {
			PC_Block pcBlock = (PC_Block) block;
			if (!pcBlock.renderInventoryBlock(metadata, renderer)) {
				iRenderInvBlockRotatedBox(block, metadata, modelID, renderer);
			}
		}
	}
	
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if (block instanceof PC_Block) {
			PC_Block pcBlock = (PC_Block) block;
			if (!pcBlock.renderWorldBlock(world, x, y, z, renderer)) {
				iRenderBlockRotatedBox(world, x, y, z, block, modelId, renderer);
			}
			return true;
		}
		return false;
	}
	
	public boolean shouldRender3DInInventory() {
		return render3d;
	}
	
	public int getRenderId() {
		if (render3d)
			return render3dId;
		return render2dId;
	}
	
	@Override
	protected void iTessellatorDraw() {
		Tessellator.instance.draw();
	}
	
	@Override
	protected void iTessellatorStartDrawingQuads() {
		Tessellator.instance.startDrawingQuads();
	}
	
	@Override
	protected void iTessellatorStartDrawing(int i) {
		Tessellator.instance.startDrawing(i);
	}
	
	@Override
	protected void iTessellatorSetColorOpaque_I(int i) {
		Tessellator.instance.setColorOpaque_I(i);
	}
	
	@Override
	protected void iTessellatorSetColor(int r, int g, int b, int a) {
		Tessellator.instance.setColorRGBA(r, g, b, a);
	}
	
	@Override
	protected void iTessellatorAddVertex(double x, double y, double z) {
		Tessellator.instance.addVertex(x, y, z);
	}
	
	private RenderEngine getRenderEngine() {
		return PC_ClientUtils.mc().renderEngine;
	}
	
	@Override
	protected void iBindTexture(String texture) {
		RenderEngine re = getRenderEngine();
		re.bindTexture(texture);
	}
	
	@Override
	protected void iRenderStandardBlock(Object renderer, Block block, int x, int y, int z) {
		RenderBlocks renderblocks = (RenderBlocks) renderer;
		renderblocks.setRenderBoundsFromBlock(block);
		renderblocks.renderStandardBlock(block, x, y, z);
		renderblocks.unlockBlockBounds();
	}
	
	@Override
	protected void iRenderInvBox(Object renderer, Block block, int metadata) {
		Tessellator tessellator = Tessellator.instance;
		RenderBlocks renderblocks = (RenderBlocks) renderer;
		
		Icon[] textures = new Icon[6];
		if (block instanceof PC_ISpecialInventoryTextures) {
			for (int a = 0; a < 6; a++) {
				textures[a] = ((PC_ISpecialInventoryTextures) block).getInvTexture(a, metadata);
			}
		} else {
			for (int a = 0; a < 6; a++) {
				textures[a] = block.getIcon(a, metadata);
			}
		}
		
		block.setBlockBoundsForItemRender();
		renderblocks.setRenderBoundsFromBlock(block);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, textures[0]);
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, textures[1]);
		tessellator.setNormal(0.0F, 0.0F, -1F);
		renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, textures[2]);
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, textures[3]);
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, textures[4]);
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, textures[5]);
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		renderblocks.unlockBlockBounds();
	}
	
	@Override
	protected void iRenderBlockRotatedBox(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer) {
		if (block instanceof PC_Block) {
			RenderBlocks renderblocks = (RenderBlocks) renderer;
			Tessellator tessellator = Tessellator.instance;
			int metaAt = PC_Utils.getMD(world, x, y, z);
			block.setBlockBoundsBasedOnState(world, x, y, z);
			renderblocks.setRenderBoundsFromBlock(block);
			PC_Direction dir = ((PC_Block) block).getRotation(metaAt);
			renderblocks.uvRotateTop = ((PC_Direction) dir).getMCSide();
			renderblocks.renderStandardBlock(block, x, y, z);
			renderblocks.uvRotateTop = 0;
		}
		
	}
	
	@Override
	protected void iRenderInvBlockRotatedBox(Block block, int metadata, int modelID, Object renderer) {
		RenderBlocks renderblocks = (RenderBlocks) renderer;
		Tessellator tessellator = Tessellator.instance;
		
		if (block instanceof PC_IMSG) {
			
			Object o = ((PC_IMSG) block).msg(PC_MSGRegistry.MSG_RENDER_ITEM_HORIZONTAL);
			if (o instanceof Boolean) {
				boolean renderOnSide = (Boolean) o;
				
				if (renderOnSide) {
					block.setBlockBoundsForItemRender();
				} else {
					PC_Utils.setBlockBounds(block, -0.1F, -0.1F, 0.4F, 1.1F, 1.1F, 0.6F);
				}
				
				renderblocks.setRenderBoundsFromBlock(block);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1F, 0.0F);
				renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(renderOnSide ? 0 : 0, metadata));
				tessellator.setNormal(0.0F, 1.0F, 0.0F);
				renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(renderOnSide ? 1 : 0, metadata));
				tessellator.setNormal(0.0F, 0.0F, -1F);
				renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(renderOnSide ? 2 : 0, metadata));
				tessellator.setNormal(0.0F, 0.0F, 1.0F);
				renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(renderOnSide ? 3 : 1, metadata));
				tessellator.setNormal(-1F, 0.0F, 0.0F);
				renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(renderOnSide ? 4 : 0, metadata));
				tessellator.setNormal(1.0F, 0.0F, 0.0F);
				renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(renderOnSide ? 5 : 0, metadata));
				tessellator.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				renderblocks.unlockBlockBounds();
			} else {
				iRenderInvBox(renderer, block, metadata);
			}
			return;
		}
	}
	
	protected void iRenderBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		block.setBlockBoundsBasedOnState(world, x, y, z);
		iRenderStandardBlock(renderer, block, x, y, z);
	}
	
	protected void iRenderInvBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		RenderBlocks renderblocks = (RenderBlocks) renderer;
		Tessellator tessellator = Tessellator.instance;
		
		block.setBlockBoundsForItemRender();
		renderblocks.setRenderBoundsFromBlock(block);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1F, 0.0F);
		renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
		tessellator.setNormal(0.0F, 0.0F, -1F);
		renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, metadata));
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, metadata));
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, metadata));
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, metadata));
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		renderblocks.unlockBlockBounds();
		
	}
	
	@Override
	protected void iRenderInvBoxWithTexture(Object renderer, Block block, Icon icon) {
		RenderBlocks renderblocks = (RenderBlocks) renderer;
		Tessellator tessellator = Tessellator.instance;
		renderblocks.setRenderBoundsFromBlock(block);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1F, 0.0F);
		renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon);
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon);
		tessellator.setNormal(0.0F, 0.0F, -1F);
		renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		renderblocks.unlockBlockBounds();
	}
	
	@Override
	protected void iRenderInvBoxWithTextures(Object renderer, Block block, Icon[] icon) {
		RenderBlocks renderblocks = (RenderBlocks) renderer;
		Tessellator tessellator = Tessellator.instance;
		renderblocks.setRenderBoundsFromBlock(block);
		
		block.setBlockBoundsForItemRender();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		if (icon[0] != null) {
			tessellator.setNormal(0.0F, -1F, 0.0F);
			renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon[0]);
		}
		if (icon[1] != null) {
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon[1]);
		}
		if (icon[2] != null) {
			tessellator.setNormal(0.0F, 0.0F, -1F);
			renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon[2]);
		}
		if (icon[3] != null) {
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon[3]);
		}
		if (icon[4] != null) {
			tessellator.setNormal(-1F, 0.0F, 0.0F);
			renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon[4]);
		}
		if (icon[5] != null) {
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon[5]);
		}
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		
		renderblocks.unlockBlockBounds();
	}
	
	@Override
	protected void iRenderBlockByRenderType(Object renderer, Block block, int x, int y, int z) {
		RenderBlocks renderblocks = (RenderBlocks) renderer;
		renderblocks.renderBlockByRenderType(block, x, y, z);
	}
	
	@Override
	protected void iglColor4f(float r, float g, float b, float a) {
		GL11.glColor4f(r, g, b, a);
	}
	
	@Override
	protected void iglPushMatrix() {
		GL11.glPushMatrix();
	}
	
	@Override
	protected void iglPopMatrix() {
		GL11.glPopMatrix();
	}
	
	@Override
	protected void iglTranslatef(float x, float y, float z) {
		GL11.glTranslatef(x, y, z);
	}
	
	@Override
	protected void iglRotatef(float angel, float x, float y, float z) {
		GL11.glRotatef(angel, x, y, z);
	}
	
	@Override
	protected void iglScalef(float x, float y, float z) {
		GL11.glScalef(x, y, z);
	}
	
	@Override
	protected void iglEnable(int i) {
		GL11.glEnable(i);
	}
	
	@Override
	protected void iglDisable(int i) {
		GL11.glDisable(i);
	}
	
	@Override
	protected void iglBlendFunc(int i, int j) {
		GL11.glBlendFunc(i, j);
	}
	
	@Override
	protected void iglNormal3f(float x, float y, float z) {
		GL11.glNormal3f(x, y, z);
	}
	
	@Override
	protected void iglDepthMask(boolean state) {
		GL11.glDepthMask(state);
	}
	
	@Override
	protected void irenderEntityLabelAt(String label, PC_VecF realPos, int viewDistance, float yOffset, double x, double y, double z) {
		RenderManager renderManager = RenderManager.instance;
		label = label.trim();
		
		if (renderManager.livingPlayer == null)
			return;
		float f = (float) renderManager.livingPlayer.getDistance(realPos.x + 0.5D, realPos.y + 0.5D, realPos.z + 0.5D);
		
		if (f > viewDistance) {
			return;
		}
		
		FontRenderer fontrenderer = PC_ClientUtils.mc().fontRenderer;
		float f1 = 1.0F; // 1.6F;
		float f2 = 0.01666667F * f1;
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + yOffset, (float) z + 0.5F);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-f2, -f2, f2);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		// GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator = Tessellator.instance;
		byte byte0 = 0;
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		tessellator.startDrawingQuads();
		float i = (fontrenderer.getStringWidth(label) / 2) * 1.12F;
		tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.5F);
		tessellator.addVertex(-i - 1, -1 + byte0, 0.0D);
		tessellator.addVertex(-i - 1, 8 + byte0, 0.0D);
		tessellator.addVertex(i + 1, 8 + byte0, 0.0D);
		tessellator.addVertex(i + 1, -1 + byte0, 0.0D);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		fontrenderer.drawString(label, -fontrenderer.getStringWidth(label) / 2, byte0, 0xffffffff);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
	
	@Override
	protected FontRenderer igetFontRenderer() {
		return PC_ClientUtils.mc().fontRenderer;
	}
	
}

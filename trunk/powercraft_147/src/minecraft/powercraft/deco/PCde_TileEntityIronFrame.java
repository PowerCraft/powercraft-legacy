package powercraft.deco;

import net.minecraft.block.Block;
import powercraft.management.PC_ITileEntityRenderer;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_VecI;
import powercraft.management.registry.PC_MSGRegistry;
import powercraft.management.registry.PC_ModuleRegistry;
import powercraft.management.registry.PC_TextureRegistry;

public class PCde_TileEntityIronFrame extends PC_TileEntity implements PC_ITileEntityRenderer {

	private PCde_ModelIronFrame model = new PCde_ModelIronFrame();

	@Override
	public void renderTileEntityAt(double x, double y, double z, float rot) {

		PC_Renderer.glPushMatrix();
		float f = 1.0F;

		PC_Renderer.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

		PC_Renderer.bindTexture(PC_TextureRegistry.getTextureDirectory(PC_ModuleRegistry.getModule("Deco"))+"block_deco.png");

		PC_Renderer.glPushMatrix();
		PC_Renderer.glScalef(f, -f, -f);

		model.setFrameParts(0, needsFullFace(getCoord().offset(0, 1, 0)));
		model.setFrameParts(1, needsFullFace(getCoord().offset(1, 0, 0)));
		model.setFrameParts(2, needsFullFace(getCoord().offset(0, 0, 1)));
		model.setFrameParts(3, needsFullFace(getCoord().offset(-1, 0, 0)));
		model.setFrameParts(4, needsFullFace(getCoord().offset(0, 0, -1)));
		model.render();

		PC_Renderer.glPopMatrix();

		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		PC_Renderer.glPopMatrix();
	}

	private boolean needsFullFace(PC_VecI pos) {
		int id = GameInfo.getBID(worldObj, pos);
		if (id == Block.torchWood.blockID) return true;
		if (id == Block.torchRedstoneActive.blockID) return true;
		if (id == Block.torchRedstoneIdle.blockID) return true;
		if (id == Block.lever.blockID) return true;
		if (id == Block.stoneButton.blockID) return true;
		if (id == Block.woodenButton.blockID) return true;
		if (PC_MSGRegistry.hasFlag(worldObj, pos, "ATTACHED")) return true;
		return false;

	}
	
	
}

package powercraft.deco;

import net.minecraft.src.Block;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.registry.PC_TextureRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.tileentity.PC_ITileEntityRenderer;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public class PCde_TileEntityIronFrame extends PC_TileEntity implements PC_ITileEntityRenderer {

	private PCde_ModelIronFrame model = new PCde_ModelIronFrame();

	@Override
	public void renderTileEntityAt(double x, double y, double z, float rot) {

		float f = 1.0F;

		PC_Renderer.bindTexture(PC_TextureRegistry.getPowerCraftImageDir()+PC_TextureRegistry.getTextureName(PCde_App.instance, "block_deco.png"));

		PC_Renderer.glPushMatrix();
		PC_Renderer.glScalef(f, -f, -f);

		model.setFrameParts(0, needsFullFace(getCoord().offset(0, 1, 0)));
		model.setFrameParts(1, needsFullFace(getCoord().offset(-1, 0, 0)));
		model.setFrameParts(2, needsFullFace(getCoord().offset(0, 0, -1)));
		model.setFrameParts(3, needsFullFace(getCoord().offset(1, 0, 0)));
		model.setFrameParts(4, needsFullFace(getCoord().offset(0, 0, 1)));
		model.render();

		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		PC_Renderer.glPopMatrix();
	}

	private boolean needsFullFace(PC_VecI pos) {
		int id = PC_Utils.getBID(worldObj, pos);
		if (id == Block.torchWood.blockID) return true;
		if (id == Block.torchRedstoneActive.blockID) return true;
		if (id == Block.torchRedstoneIdle.blockID) return true;
		if (id == Block.lever.blockID) return true;
		if (id == Block.stoneButton.blockID) return true;
		if (id == Block.woodenButton.blockID) return true;
		if (PC_MSGRegistry.hasFlag(worldObj, pos, PC_MSGRegistry.DECO_FRAME_ATTACHED)) return true;
		return false;

	}
	
	
}

package powercraft.weasel;

import powercraft.api.registry.PC_TextureRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.tileentity.PC_ITileEntityRenderer;
import powercraft.api.tileentity.PC_TileEntity;

public class PCws_TileEntityWeaselDiskManager extends PC_TileEntity implements PC_ITileEntityRenderer {

	private static PCws_WeaselModelDiskManager model = new PCws_WeaselModelDiskManager();
	
	@Override
	public void renderTileEntityAt(double x, double y, double z, float rot) {

		float f = 1.0F;
		
		PC_Renderer.glTranslatef(0, -0.5f, 0);
		
		PC_Renderer.bindTexture(PC_TextureRegistry.getPowerCraftImageDir()+PC_TextureRegistry.getTextureName(PCws_App.instance, "block_chip.png"));

		// push 2
		PC_Renderer.glPushMatrix();

		PC_Renderer.glScalef(f, -f, -f);
		
		model.renderDevice();

		// pop 2
		PC_Renderer.glPopMatrix();



		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

}

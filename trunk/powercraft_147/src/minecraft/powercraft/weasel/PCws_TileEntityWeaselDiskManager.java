package powercraft.weasel;

import powercraft.management.registry.PC_ModuleRegistry;
import powercraft.management.registry.PC_TextureRegistry;
import powercraft.management.renderer.PC_Renderer;
import powercraft.management.tileentity.PC_ITileEntityRenderer;
import powercraft.management.tileentity.PC_TileEntity;

public class PCws_TileEntityWeaselDiskManager extends PC_TileEntity implements PC_ITileEntityRenderer {

	private static PCws_WeaselModelDiskManager model = new PCws_WeaselModelDiskManager();
	
	@Override
	public void renderTileEntityAt(double x, double y, double z, float rot) {

		// push 1
		PC_Renderer.glPushMatrix();
		float f = 1.0F;
		
		PC_Renderer.glTranslatef((float) x + 0.5F, ((float) y), (float) z + 0.5F);

		PC_Renderer.bindTexture(PC_TextureRegistry.getTextureDirectory(PC_ModuleRegistry.getModule("Weasel")) + "block_chip.png");

		// push 2
		PC_Renderer.glPushMatrix();

		PC_Renderer.glScalef(f, -f, -f);
		
		model.renderDevice();

		// pop 2
		PC_Renderer.glPopMatrix();



		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		// pop1
		PC_Renderer.glPopMatrix();
	}

}

package powercraft.light;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_ITileEntityRenderer;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.annotation.PC_ClientServerSync;
import powercraft.management.registry.PC_ModuleRegistry;
import powercraft.management.registry.PC_TextureRegistry;

public class PCli_TileEntityPrism extends PC_TileEntity implements PC_ITileEntityRenderer {

	private static PCli_ModelPrism modelPrism = new PCli_ModelPrism();
	
	/**
	 * List of prism's sides, flags whether there are attached glass panels.
	 * starts with up and down, but the order does not really matter here.
	 */
	@PC_ClientServerSync
	private boolean[] prismSides = { false, false, false, false, false, false, false, false, false, false };
	
	public boolean getPrismSide(int side) {
		if (side < 0 || side > 9) {
			return false;
		}
		return prismSides[side];
	}

	public void setPrismSide(int side, boolean state) {
		if (side < 0 || side> 9) {
			return;
		}
		if(prismSides[side]!=state){
			prismSides[side]=state;
			notifyChanges("prismSides");
		}
	}

	@Override
	public void renderTileEntityAt(double x, double y, double z, float rot) {
		modelPrism.mainCrystal.showModel = true;

		for (int a = 0; a <= 9; a++) {
			modelPrism.sides[a].showModel = getPrismSide(a);
		}

		PC_Renderer.glPushMatrix();
		float f = 1.0F;

		PC_Renderer.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

		PC_Renderer.bindTexture(PC_TextureRegistry.getTextureDirectory(PC_ModuleRegistry.getModule("Light"))+"prism.png");

		PC_Renderer.glPushMatrix();
		PC_Renderer.glScalef(f, -f, -f);

		PC_Renderer.glEnable(3042 /* GL_BLEND */);
		PC_Renderer.glDisable(3008 /* GL_ALPHA_TEST */);
		PC_Renderer.glEnable(2977 /* GL_NORMALIZE */);
		PC_Renderer.glBlendFunc(770 /* GL_SRC_ALPHA */, 771 /* GL_ONE_MINUS_SRC_ALPHA */);

		modelPrism.renderPrism();

		PC_Renderer.glDisable(2977 /* GL_NORMALIZE */);
		PC_Renderer.glDisable(3042 /* GL_BLEND */);
		PC_Renderer.glEnable(3008 /* GL_ALPHA_TEST */);

		PC_Renderer.glPopMatrix();

		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		PC_Renderer.glPopMatrix();
	}
	
}

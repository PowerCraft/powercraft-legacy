package powercraft.light;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.NBTTagCompound;
import powercraft.core.PC_ITileEntityRenderer;
import powercraft.core.PC_PacketHandler;
import powercraft.core.PC_Renderer;
import powercraft.core.PC_TileEntity;

public class PCli_TileEntityPrism extends PC_TileEntity implements PC_ITileEntityRenderer {

	private static PCma_ModelPrism modelPrism = new PCma_ModelPrism();
	
	/**
	 * List of prism's sides, flags whether there are attached glass panels.
	 * starts with up and down, but the order does not really matter here.
	 */
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
		prismSides[side] = state;
		PC_PacketHandler.setTileEntity(this, "prismSides", prismSides);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);

		for(int i=0; i<prismSides.length; i++){
			tag.setBoolean("prismSide"+i, prismSides[i]);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		for(int i=0; i<prismSides.length; i++){
			prismSides[i] = tag.getBoolean("prismSide"+i);
		}
	}
	
	@Override
	public void setData(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("prismSides"))
				prismSides = (boolean[])o[p++];
		}
	}

	@Override
	public Object[] getData() {
		return new Object[]{
				"prismSides",
				prismSides
		};
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

		PC_Renderer.bindTexture(PCli_App.getInstance().getTextureDirectory()+"prism.png");

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

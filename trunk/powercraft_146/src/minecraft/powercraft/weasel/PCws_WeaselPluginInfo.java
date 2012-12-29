package powercraft.weasel;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_Color;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;

public abstract class PCws_WeaselPluginInfo {

	private Class<? extends PCws_WeaselPlugin> c;
	private String defaultName;
	
	public PCws_WeaselPluginInfo(Class<? extends PCws_WeaselPlugin> c, String defaultName){
		this.c = c;
		this.defaultName = defaultName;
	}
	
	public PCws_WeaselPlugin createPlugin(){
		try {
			return ValueWriting.createClass(c, new Class[0], new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	public PCws_WeaselPlugin createPlugin(NBTTagCompound nbtTag) {
		try {
			return ValueWriting.createClass(c, new Class[]{NBTTagCompound.class}, new Object[]{nbtTag});
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public Class<? extends PCws_WeaselPlugin> getPluginClass() {
		return c;
	}
	
	public String getDefaultName(){
		return defaultName;
	}

	public String getKey() {
		return c.getSimpleName();
	}

	public abstract void renderInventoryBlock(Block block, Object renderer);
	
	public float[] getBounds(){
		return new float[]{ 0, 0, 0, 1, 0.5F, 1 };
	}

	public abstract PCws_WeaselModelBase getModel();
	
	public void renderPluginAt(PCws_TileEntityWeasel te, double x, double y, double z, float rot){

		PCws_WeaselModelBase model = getModel();

		PC_Color color = (PC_Color)te.getData("color");
		if(color==null)
			color = new PC_Color(0.3f, 0.3f, 0.3f);

		// push 1
		PC_Renderer.glPushMatrix();
		float f = 1.0F;
		
		PC_Renderer.glTranslatef((float) x + 0.5F, ((float) y), (float) z + 0.5F);

		PC_Renderer.bindTexture(ModuleInfo.getTextureDirectory(ModuleInfo.getModule("Weasel")) + "block_chip.png");

		// push 2
		PC_Renderer.glPushMatrix();

		PC_Renderer.glScalef(f, -f, -f);
		
		float f1 = 0;
		PC_Renderer.glRotatef(90 * (GameInfo.getMD(te.worldObj, te.getCoord()) & 3), 0, 1, 0);
		model.renderDevice();

		PC_Renderer.glColor4f(color.x, color.y, color.z, 1f);

		model.renderColorMark();

		// pop 2
		PC_Renderer.glPopMatrix();



		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		PC_Renderer.glRotatef(-f1, 0.0F, 1.0F, 0.0F);
		model.renderText(te);

		// pop1
		PC_Renderer.glPopMatrix();
	}

	public void getServerMsg(PCws_TileEntityWeasel te, String msg, Object obj) {
		
	}
	
}

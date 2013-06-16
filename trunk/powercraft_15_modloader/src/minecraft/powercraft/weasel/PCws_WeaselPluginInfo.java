package powercraft.weasel;

import net.minecraft.src.Icon;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.registry.PC_TextureRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_Color;
import powercraft.api.utils.PC_Direction;

public abstract class PCws_WeaselPluginInfo {

	private Class<? extends PCws_WeaselPlugin> c;
	private String defaultName;
	protected Icon[] icons;
	private String[] iconNames;
	
	public PCws_WeaselPluginInfo(Class<? extends PCws_WeaselPlugin> c, String defaultName, String...iconNames){
		this.c = c;
		this.defaultName = defaultName;
		this.iconNames = iconNames;
		this.icons = new Icon[4+iconNames.length];
	}
	
	public void onIconLoading(){
		icons[0] = PC_TextureRegistry.registerIcon(PCws_App.instance, "weasel_down");
		icons[1] = PC_TextureRegistry.registerIcon(PCws_App.instance, "weasel_side");
		icons[2] = PC_TextureRegistry.registerIcon(PCws_App.instance, "weasel_top");
		icons[3] = PC_TextureRegistry.registerIcon(PCws_App.instance, "weasel_top_empty");
		for(int i=0; i<iconNames.length; i++){
			icons[i+4] = PC_TextureRegistry.registerIcon(PCws_App.instance, iconNames[i]);
		}
	}
	
	public PCws_WeaselPlugin createPlugin(){
		try {
			return PC_ReflectHelper.create(c);
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
	
	public abstract void renderInventoryBlock(PCws_BlockWeasel block, Object renderer);
	
	public float[] getBounds(){
		return new float[]{ 0, 0, 0, 1, 0.5F, 1 };
	}

	public abstract PCws_WeaselModelBase getModel();
	
	public void renderPluginAt(PCws_TileEntityWeasel te, double x, double y, double z, float rot){

		PCws_WeaselModelBase model = getModel();

		PC_Color color = (PC_Color)te.getData("color");
		if(color==null)
			color = new PC_Color(0.3f, 0.3f, 0.3f);

		float f = 1.0F;

		if(hasSpecialRot()){
			PC_Renderer.glPopMatrix();
			PC_Renderer.glPushMatrix();
			PC_Renderer.glTranslatef((float)(x+0.5), (float)(y), (float)(z+0.5));
		}else{
			PC_Renderer.glRotatef(-90, 0.0F, 1.0F, 0.0F);
			PC_Renderer.glTranslatef(0, -0.5f, 0);
		}
		
		PC_Renderer.bindTexture(PC_TextureRegistry.getPowerCraftImageDir() + PC_TextureRegistry.getTextureName(PCws_App.instance, "block_chip.png"));

		// push 2
		PC_Renderer.glPushMatrix();

		PC_Renderer.glScalef(f, -f, -f);
		
		float f1 = 0;
		if(hasSpecialRot()){
			Object o = te.getData("specialRot");
			if(o instanceof Integer){
				f1 = (Integer)o * 360 / 16F;
				PC_Renderer.glRotatef(f1, 0.0F, 1.0F, 0.0F);
			}
		}
		model.renderDevice(te);

		PC_Renderer.glColor4f(color.x, color.y, color.z, 1f);

		model.renderColorMark(te);


		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		PC_Renderer.glRotatef(-f1, 0.0F, 1.0F, 0.0F);
		PC_Renderer.glPushMatrix();
		PC_Renderer.glDisable(0xb50); //GL_LIGHTING
		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		PC_Renderer.glNormal3f(0.0F, 0.0F, -0.01111111f);
		
		model.renderText(te, PC_Renderer.getFontRenderer());
		
		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		PC_Renderer.glEnable(0xb50); //GL_LIGHTING
		PC_Renderer.glPopMatrix();
		
		// pop1
		PC_Renderer.glPopMatrix();
	}

	public boolean hasSpecialRot(){
		return false;
	}
	
	public void getServerMsg(PCws_TileEntityWeasel te, String msg, Object[] obj) {
		
	}
	
	public int inventorySize(){
		return 0;
	}
	
	public int inventoryStackLimit(){
		return 64;
	}

	public abstract Icon getTexture(PC_Direction side);
	
}

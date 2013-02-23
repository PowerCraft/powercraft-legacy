package powercraft.light;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import powercraft.management.PC_Color;
import powercraft.management.PC_ITileEntityRenderer;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.SaveHandler;
import powercraft.management.annotation.PC_ClientServerSync;
import powercraft.management.registry.PC_ModuleRegistry;
import powercraft.management.registry.PC_TextureRegistry;

public class PCli_TileEntityLight extends PC_TileEntity implements PC_ITileEntityRenderer{
	
	private static PCli_ModelLight model = new PCli_ModelLight();
	
	@PC_ClientServerSync
	private PC_Color color = new PC_Color(1.0f, 1.0f, 1.0f);
	@PC_ClientServerSync
	private boolean isStable;
	@PC_ClientServerSync
    private boolean isHuge;
    
    public void setColor(PC_Color c) {
    	color = c.copy();
    	notifyChanges("color");
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public PC_Color getColor(){
        return color;
    }

    public void setStable(boolean stable){
    	if(isStable!=stable){
    		isStable = stable;
    		notifyChanges("isStable");
    		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    	}
   }

    public boolean isStable(){
        return isStable;
    }

    public void setHuge(boolean huge){
    	if(isHuge != huge){
    		isHuge = huge;
    		notifyChanges("isHuge");
    	}
    }

    public boolean isHuge() {
        return isHuge;
    }

    public boolean isActive()
    {
        return GameInfo.getBID(worldObj, xCoord, yCoord, zCoord) == PCli_BlockLight.on.blockID;
    }

	@Override
	public void renderTileEntityAt(double x, double y, double z, float rot) {

		PC_Renderer.glPushMatrix();
		float f = 1.0F;

		PC_Renderer.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		
		PC_Renderer.bindTexture(PC_TextureRegistry.getTextureDirectory(PC_ModuleRegistry.getModule("Light")) + "block_light.png");

		PC_Renderer.glPushMatrix();
		PC_Renderer.glScalef(f, -f, -f);

		PC_Color clr = getColor();
		if(clr!=null)
			PC_Renderer.glColor4f(clr.x, clr.y, clr.z, 1.0f);
		else
			PC_Renderer.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		int meta = GameInfo.getMD(worldObj, getCoord());
		switch (meta) {
			case 0:
				break;
			case 1:
				PC_Renderer.glRotatef(-90, 1, 0, 0);
				break;
			case 2:
				PC_Renderer.glRotatef(90, 1, 0, 0);
				break;
			case 3:
				PC_Renderer.glRotatef(-90, 0, 0, 1);
				break;
			case 4:
				PC_Renderer.glRotatef(90, 0, 0, 1);
				break;
			case 5:
				PC_Renderer.glRotatef(180, 1, 0, 0);
				break;
		}

		if (isHuge()) {
			model.renderHuge();
		} else {
			model.renderNormal();
		}

		PC_Renderer.glPopMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
}

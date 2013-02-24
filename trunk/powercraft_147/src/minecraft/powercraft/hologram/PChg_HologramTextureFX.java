package powercraft.hologram;

import java.util.Random;

import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.texturefx.TextureFX;

import org.lwjgl.opengl.GL11;

import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.registry.PC_ModuleRegistry;

public class PChg_HologramTextureFX extends TextureFX {

	private int tick=0;
	
	public PChg_HologramTextureFX() {
		super(0);
		Random rand = new Random();
		for(int i=0; i<imageData.length; i+=4){
			if(rand.nextBoolean() && rand.nextBoolean()){
				double gray = Math.random()/2+0.5;
				double blue = Math.random();
				imageData[i] = (byte)(gray*blue*256);
				imageData[i+1] = (byte)(gray*blue*256);
				imageData[i+2] = (byte)(gray*256);
				imageData[i+3] = -1;
			}
		}
	}
	
	@Override
	public void bindImage(RenderEngine par1RenderEngine) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1RenderEngine.getTexture(ModuleInfo.getTerrainFile(PC_ModuleRegistry.getModule("Hologram"))));
	}

	@Override
	public void onTick() {
		tick++;
		if(tick%20==0){
			byte b1 = imageData[0], b2 = imageData[1], b3 = imageData[2], b4 = imageData[3];
			int l = imageData.length-4;
			for(int i=0; i<l; i+=4){
				imageData[i] = imageData[i+4];
				imageData[i+1] = imageData[i+5];
				imageData[i+2] = imageData[i+6];
				imageData[i+3] = imageData[i+7];
			}
			imageData[l] = b1;
			imageData[l+1] = b2;
			imageData[l+2] = b3;
			imageData[l+3] = b4;
		}
	}
	
}

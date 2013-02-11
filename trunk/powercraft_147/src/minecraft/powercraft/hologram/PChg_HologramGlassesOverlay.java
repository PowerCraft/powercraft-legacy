package powercraft.hologram;

import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkCache;
import powercraft.management.PC_ClientUtils;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_ItemArmor;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;
import powercraft.management.mod_PowerCraft;

public class PChg_HologramGlassesOverlay implements PC_IMSG {

	@Override
	public Object msg(int msg, Object... obj) {
		if(msg==PC_Utils.MSG_RENDER_OVERLAY)
			onRenderOverlay((GuiIngame)obj[0]);
		return null;
	}

	private void onRenderOverlay(GuiIngame gi) {
		EntityPlayer player = PC_ClientUtils.mc().thePlayer;
		ItemStack helmet = player.inventory.armorItemInSlot(3);
		if(helmet!=null && helmet.itemID == Item.helmetSteel.itemID){
			drawArea(PC_ClientUtils.mc(), player);
		}
	}

	private void drawArea(Minecraft mc, EntityPlayer player){
		ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();
		PC_VecI pos = new PC_VecI();
		pos.x = (int)Math.round(player.posX);
		pos.y = (int)Math.round(player.posY);
		pos.z = (int)Math.round(player.posZ);
		ChunkCache cc = new ChunkCache(player.worldObj, pos.x-10, pos.y-10, pos.z-10, pos.x+9, pos.y+9, pos.z+9);
		RenderBlocks renderer = new PChg_HologramRenderBlocks(cc);
		GL11.glPushAttrib(-1);
		int scale = 100*sr.getScaleFactor();
		GL11.glViewport(0, mc.displayHeight-scale, scale, scale);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GLU.gluPerspective(60, width/height, 1, 100);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glTranslatef(0, 0, -16);
		GL11.glRotatef(player.rotationPitch, -1.0F, 0.0F, 0.0F);
		GL11.glRotatef(player.rotationYaw, 0, 1, 0);
		GL11.glTranslatef(-(float)player.posX, -(float)player.posY, -(float)player.posZ);
		
		PC_Renderer.resetTerrain(true);
		PC_Renderer.tessellatorStartDrawingQuads();
		for(int yy=-8; yy<8; yy++){
			for(int xx=-8; xx<8; xx++){
				for(int zz=-8; zz<8; zz++){
					Block block = Block.blocksList[cc.getBlockId(pos.x+xx, pos.y+yy, pos.z+zz)];
					if(block!=null){
						PC_Renderer.renderBlockByRenderType(renderer, block, pos.x+xx, pos.y+yy, pos.z+zz);
					}
				}
			}
		}
		PC_Renderer.tessellatorDraw();
		
		for(int yy=-8; yy<8; yy++){
			for(int xx=-8; xx<8; xx++){
				for(int zz=-8; zz<8; zz++){
					Block block = Block.blocksList[cc.getBlockId(pos.x+xx, pos.y+yy, pos.z+zz)];
					if(block!=null){
						TileEntity tileEntity = cc.getBlockTileEntity(pos.x+xx, pos.y+yy, pos.z+zz);
						if(tileEntity!=null && !(tileEntity instanceof PChg_TileEntityHologramField)){
							TileEntityRenderer.instance.renderTileEntityAt(tileEntity, pos.x+xx, pos.y+yy, pos.z+zz, 1);
						}
					}
				}
			}
		}
		
		double rpx = RenderManager.renderPosX;
		double rpy = RenderManager.renderPosY;
		double rpz = RenderManager.renderPosZ;
        
		RenderManager.renderPosX = 0;
		RenderManager.renderPosY = 0;
		RenderManager.renderPosZ = 0;
		
        List var5 = player.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(pos.x-7, pos.y-7, pos.z-7, pos.x+8, pos.y+8, pos.z+8));
        int var6;
        Entity var7;

        for (var6 = 0; var6 < var5.size(); ++var6)
        {
        	GL11.glPushAttrib(-1);
            var7 = (Entity)var5.get(var6);
            RenderManager.instance.renderEntity(var7, 1);
            GL11.glPopAttrib();
        }
        RenderManager.renderPosX = rpx;
		RenderManager.renderPosY = rpy;
		RenderManager.renderPosZ = rpz;
        
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopAttrib();
	}
	
}

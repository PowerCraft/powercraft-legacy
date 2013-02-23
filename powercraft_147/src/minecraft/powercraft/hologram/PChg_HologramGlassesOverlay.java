package powercraft.hologram;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkCache;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import powercraft.management.PC_ClientUtils;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_VecI;
import powercraft.management.registry.PC_MSGRegistry;

public class PChg_HologramGlassesOverlay implements PC_IMSG {

	private static int glList;
	private static int tick=0;
	private static boolean update=true;
	public static PChg_TileEntityHologramField fieldToUpdate;
	
	@Override
	public Object msg(int msg, Object... obj) {
		if(msg==PC_MSGRegistry.MSG_RENDER_OVERLAY){
			onRenderOverlay((GuiIngame)obj[0]);
		}else if(msg==PC_MSGRegistry.MSG_TICK_EVENT){
			onTick();
		}
		return null;
	}

	private void onTick() {
		if(tick%20==0){
			update=true;
		}else{
			if(PChg_TileEntityHologramField.mapToUpdate.size()>0){
				fieldToUpdate = PChg_TileEntityHologramField.mapToUpdate.get(0);
				PChg_TileEntityHologramField.mapToUpdate.remove(0);
			}
		}
		tick++;
	}

	private static void onRenderOverlay(GuiIngame gi) {
		EntityPlayer player = PC_ClientUtils.mc().thePlayer;
		ItemStack helmet = player.inventory.armorItemInSlot(3);
		if(helmet!=null && helmet.itemID == PChg_App.hologramGlasses.itemID){
			drawArea(PC_ClientUtils.mc(), player);
		}
	}

	private static void drawArea(Minecraft mc, EntityPlayer player){
		ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();
		PC_VecI pos = new PC_VecI();
		pos.x = (int)Math.round(player.posX);
		pos.y = (int)Math.round(player.posY);
		pos.z = (int)Math.round(player.posZ);
		ChunkCache cc = new ChunkCache(player.worldObj, pos.x-10, pos.y-10, pos.z-10, pos.x+9, pos.y+9, pos.z+9);
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
		
		if(glList==0){
			glList = GL11.glGenLists(1);
		}
		if(update){
			GL11.glNewList(glList, GL11.GL_COMPILE_AND_EXECUTE);
			RenderBlocks renderer = new PChg_HologramRenderBlocks(cc);
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
			GL11.glEndList();
			update = false;
		}else{
			GL11.glCallList(glList);
		}
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		for(int yy=-8; yy<8; yy++){
			for(int xx=-8; xx<8; xx++){
				for(int zz=-8; zz<8; zz++){
					Block block = Block.blocksList[cc.getBlockId(pos.x+xx, pos.y+yy, pos.z+zz)];
					if(block!=null){
						TileEntity tileEntity = cc.getBlockTileEntity(pos.x+xx, pos.y+yy, pos.z+zz);
						if(tileEntity!=null && !(tileEntity instanceof PChg_TileEntityHologramField)){
							if(TileEntityRenderer.instance.getFontRenderer()!=null){
								GL11.glPushAttrib(-1);
								TileEntityRenderer.instance.renderTileEntityAt(tileEntity, pos.x+xx, pos.y+yy, pos.z+zz, 1);
								GL11.glPopAttrib();
							}
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

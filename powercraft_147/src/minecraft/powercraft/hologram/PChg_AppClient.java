package powercraft.hologram;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCache;
import powercraft.management.PC_ClientUtils;
import powercraft.management.PC_IClientModule;
import powercraft.management.PC_LangEntry;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_VecI;

public class PChg_AppClient extends PChg_App implements PC_IClientModule {
	
	@Override
	public void init() {
		PC_ClientUtils.mc().renderEngine.registerTextureFX(new PChg_HologramTextureFX());
		ModuleInfo.registerMSGObject(new PChg_HologramGlassesOverlay());
	}

	@Override
	public List<PC_LangEntry> initLanguage(List<PC_LangEntry> lang) {
		return null;
	}

	@Override
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add(ModuleInfo.getTerrainFile(this));
		textures.add(ModuleInfo.getTextureDirectory(this)+"glasses.png");
		return textures;
	}

	@Override
	public List<String> addSplashes(List<String> list) {
		return null;
	}

	@Override
	public List<PC_Struct2<Class<? extends Entity>, Render>> registerEntityRender(
			List<PC_Struct2<Class<? extends Entity>, Render>> list) {
		return null;
	}
	
	@Override
	public void renderHologramField(PChg_TileEntityHologramField te, double x, double y, double z){
		RenderHelper.disableStandardItemLighting();
		PC_VecI offset = te.getOffset();
		if(offset==null)
			offset = new PC_VecI();
		offset = offset.offset(te.getCoord());
		Minecraft mc = PC_ClientUtils.mc();
		mc.entityRenderer.disableLightmap(0);
		ChunkCache cc = new ChunkCache(te.worldObj, offset.x-18, offset.y-18, offset.z-18, offset.x+17, offset.y+17, offset.z+17);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		PC_Renderer.glPushMatrix();
		PC_Renderer.glTranslatef((float)x+0.5f, (float)y+1.5f, (float)z+0.5f);
		PC_Renderer.glScalef(1/16.0f, 1/16.0f, 1/16.0f);
		PC_Renderer.glTranslatef(-offset.x, -offset.y, -offset.z);
		
		if(te.glList==0){
			te.glList = GL11.glGenLists(1);
		}
		if(te.update){
			GL11.glNewList(te.glList, GL11.GL_COMPILE_AND_EXECUTE);
			RenderBlocks renderer = new PChg_HologramRenderBlocks(cc);
			PC_Renderer.resetTerrain(true);
			PC_Renderer.tessellatorStartDrawingQuads();
			for(int yy=-16; yy<16; yy++){
				for(int xx=-16; xx<16; xx++){
					for(int zz=-16; zz<16; zz++){
						Block block = Block.blocksList[cc.getBlockId(offset.x+xx, offset.y+yy, offset.z+zz)];
						if(block!=null){
							PC_Renderer.renderBlockByRenderType(renderer, block, offset.x+xx, offset.y+yy, offset.z+zz);
						}
					}
				}
			}
			PC_Renderer.tessellatorDraw();
			GL11.glEndList();
			te.update = false;
		}else{
			GL11.glCallList(te.glList);
		}
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		for(int yy=-16; yy<16; yy++){
			for(int xx=-16; xx<16; xx++){
				for(int zz=-16; zz<16; zz++){
					Block block = Block.blocksList[cc.getBlockId(offset.x+xx, offset.y+yy, offset.z+zz)];
					if(block!=null){
						TileEntity tileEntity = cc.getBlockTileEntity(offset.x+xx, offset.y+yy, offset.z+zz);
						if(tileEntity!=null && !(tileEntity instanceof PChg_TileEntityHologramField)){
							GL11.glPushAttrib(-1);
							TileEntityRenderer.instance.renderTileEntityAt(tileEntity, offset.x+xx, offset.y+yy, offset.z+zz, 1);
							GL11.glPopAttrib();
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
		
        List var5 = te.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(offset.x-15, offset.y-15, offset.z-15, offset.x+16, offset.y+16, offset.z+16));
        int var6;
        Entity var7;

        for (var6 = 0; var6 < var5.size(); ++var6)
        {
        	GL11.glPushAttrib(-1);
            var7 = (Entity)var5.get(var6);
            RenderManager.instance.renderEntity(var7, 1);
            GL11.glPopAttrib();
        }
        RenderHelper.enableStandardItemLighting();
        mc.entityRenderer.enableLightmap(0);
        GL11.glDisable(GL11.GL_BLEND);
        RenderManager.renderPosX = rpx;
		RenderManager.renderPosY = rpy;
		RenderManager.renderPosZ = rpz;
        
		PC_Renderer.glPopMatrix();
	}
	
}

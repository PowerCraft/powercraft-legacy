package codechicken.nei;

import org.lwjgl.opengl.GL11;

import codechicken.core.alg.MathHelper;
import codechicken.core.render.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class WorldOverlayRenderer
{
    @ForgeSubscribe
    public void onWorldRenderLast(RenderWorldLastEvent event)
    {
        GL11.glPushMatrix();
            Entity entity = event.context.mc.renderViewEntity;
            RenderUtils.translateToWorldCoords(entity, event.partialTicks);
            
            renderChunkBounds(entity);
            renderMobSpawnOverlay(entity);
        GL11.glPopMatrix();
    }

    private void renderMobSpawnOverlay(Entity entity)
    {
        if(ClientHandler.instance().mobSpawnOverlay == 0)
            return;
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glLineWidth(1.5F);
        GL11.glBegin(GL11.GL_LINES);
        
        GL11.glColor4f(1, 0, 0, 1);

        World world = entity.worldObj;
        int x1 = (int) entity.posX;
        int z1 = (int) entity.posZ;
        int y1 = (int) MathHelper.clip(entity.posY, 16, world.getHeight()-16);
        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
        
        for(int x = x1-16; x <= x1+16; x++)
            for(int z = z1-16; z <= z1+16; z++)
            {
                Chunk chunk = world.getChunkFromBlockCoords(x, z);
                for(int y = y1-16; y < y1+16; y++)
                {
                    if(!SpawnerAnimals.canCreatureTypeSpawnAtLocation(EnumCreatureType.monster, entity.worldObj, x, y, z) ||
                            chunk.getSavedLightValue(EnumSkyBlock.Block, x & 15, y, z & 15) >= 8)
                        continue;

                    aabb.minX = x+0.2;
                    aabb.maxX = x+0.8;
                    aabb.minY = y+0.01;
                    aabb.maxY = y+1.8;
                    aabb.minZ = z+0.2;
                    aabb.maxZ = z+0.8;
                    if(!world.checkIfAABBIsClear(aabb) ||
                            !world.getAllCollidingBoundingBoxes(aabb).isEmpty() ||
                            world.isAnyLiquid(aabb))
                        continue;

                    GL11.glVertex3d(x, y+0.001, z);
                    GL11.glVertex3d(x+1, y+0.001, z+1);
                    GL11.glVertex3d(x+1, y+0.001, z);
                    GL11.glVertex3d(x, y+0.001, z+1);
                }
            }
        
        GL11.glEnd();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void renderChunkBounds(Entity entity)
    {
        if(ClientHandler.instance().chunkOverlay == 0)
            return;
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glLineWidth(1.5F);
        GL11.glBegin(GL11.GL_LINES);
        
        for(int cx = -4; cx <= 4; cx++) 
            for(int cz = -4; cz <= 4; cz++)
        {
            double x1 = (entity.chunkCoordX+cx)<<4;
            double z1 = (entity.chunkCoordZ+cz)<<4;
            double x2 = x1+16;
            double z2 = z1+16;

            double dy = 128;
            double y1 = Math.floor(entity.posY-dy/2);
            double y2 = y1+dy;
            if(y1 < 0)
            {
                y1 = 0; y2 = dy;
            }
            
            if(y1 > entity.worldObj.getHeight())
            {
                y2 = entity.worldObj.getHeight(); y1 = y2-dy;
            }
            
            double dist = Math.pow(1.5, -(cx*cx+cz*cz));
            
            GL11.glColor4d(0.9, 0, 0, dist);
            if(cx >= 0 && cz>=0)
            {
                GL11.glVertex3d(x2, y1, z2);
                GL11.glVertex3d(x2, y2, z2);
            }
            if(cx >= 0 && cz<=0)
            {
                GL11.glVertex3d(x2, y1, z1);
                GL11.glVertex3d(x2, y2, z1);
            }
            if(cx <= 0 && cz>=0)
            {
                GL11.glVertex3d(x1, y1, z2);
                GL11.glVertex3d(x1, y2, z2);
            }
            if(cx <= 0 && cz<=0)
            {
                GL11.glVertex3d(x1, y1, z1);
                GL11.glVertex3d(x1, y2, z1);
            }
            
            if(ClientHandler.instance().chunkOverlay == 2 && cx == 0 && cz == 0)
            {
                dy = 32;
                y1 = Math.floor(entity.posY-dy/2);
                y2 = y1+dy;
                if(y1 < 0)
                {
                    y1 = 0; y2 = dy;
                }
                
                if(y1 > entity.worldObj.getHeight())
                {
                    y2 = entity.worldObj.getHeight(); y1 = y2-dy;
                }
                
                GL11.glColor4d(0, 0.9, 0, 0.4);                
                for(double y = (int)y1; y <= y2; y++)
                {
                    GL11.glVertex3d(x2, y, z1);
                    GL11.glVertex3d(x2, y, z2);
                    GL11.glVertex3d(x1, y, z1);
                    GL11.glVertex3d(x1, y, z2);
                    GL11.glVertex3d(x1, y, z2);
                    GL11.glVertex3d(x2, y, z2);
                    GL11.glVertex3d(x1, y, z1);
                    GL11.glVertex3d(x2, y, z1);
                }
                for(double h = 1; h<=15; h++)
                {
                    GL11.glVertex3d(x1+h, y1, z1);
                    GL11.glVertex3d(x1+h, y2, z1);
                    GL11.glVertex3d(x1+h, y1, z2);
                    GL11.glVertex3d(x1+h, y2, z2);
                    GL11.glVertex3d(x1, y1, z1+h);
                    GL11.glVertex3d(x1, y2, z1+h);
                    GL11.glVertex3d(x2, y1, z1+h);
                    GL11.glVertex3d(x2, y2, z1+h);
                }
            }
        }
        
        GL11.glEnd();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
}

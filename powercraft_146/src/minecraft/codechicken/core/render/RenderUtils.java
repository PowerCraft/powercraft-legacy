package codechicken.core.render;

import org.lwjgl.opengl.GL11;

import codechicken.core.BlockCoord;
import codechicken.core.Vector3;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;

public class RenderUtils
{
    static Vector3[] vectors = new Vector3[8];
    
    static
    {
        for(int i = 0; i < vectors.length; i++)
            vectors[i] = new Vector3();
    }
            
	public static int bindLiquidTexture(int liquidID, int liquidMeta)
	{
		if(liquidID < Block.blocksList.length)
		{
	    	Block liquidBlock = Block.blocksList[liquidID];
	    	ForgeHooksClient.bindTexture(liquidBlock.getTextureFile(), 0);
	    	return liquidBlock.getBlockTextureFromSideAndMetadata(0, liquidMeta);
		}
		else
		{
	    	Item liquidItem = Item.itemsList[liquidID];
	    	ForgeHooksClient.bindTexture(liquidItem.getTextureFile(), 0);
	    	return liquidItem.getIconFromDamage(liquidMeta);
		}
	}
	
	public static void renderLiquidQuad(Vector3 point1, Vector3 point2, Vector3 point3, Vector3 point4, int texIndex, double res)
	{
    	double tx1 = (texIndex%16)/16D;
    	double ty1 = (texIndex/16)/16D;
    	double ty2 = ty1+0.0625;
		
		Vector3 wide = vectors[0].set(point4).subtract(point1);
		Vector3 high = vectors[1].set(point1).subtract(point2);
		
		double wlen = wide.mag();
		double hlen = high.mag();
		
		double x = 0;
		while(x < wlen)
		{
			double rx = wlen - x;
    		if(rx > res)
    			rx = res;

    		double y = 0;
    		while(y < hlen)
    		{
    			double ry = hlen-y;
    			if(ry > res)
    				ry = res;

    			Vector3 dx1 = vectors[2].set(wide).multiply(x/wlen);
    			Vector3 dx2 = vectors[3].set(wide).multiply((x+rx)/wlen);    
    			Vector3 dy1 = vectors[4].set(high).multiply(y/hlen);    
    			Vector3 dy2 = vectors[5].set(high).multiply((y+ry)/hlen);

    			Tessellator.instance.addVertexWithUV(point2.x+dx1.x+dy2.x, point2.y+dx1.y+dy2.y, point2.z+dx1.z+dy2.z, tx1, ty2-ry/res*0.0625);
    			Tessellator.instance.addVertexWithUV(point2.x+dx1.x+dy1.x, point2.y+dx1.y+dy1.y, point2.z+dx1.z+dy1.z, tx1, ty2);
    			Tessellator.instance.addVertexWithUV(point2.x+dx2.x+dy1.x, point2.y+dx2.y+dy1.y, point2.z+dx2.z+dy1.z, tx1+rx/res*0.0625, ty2);
    			Tessellator.instance.addVertexWithUV(point2.x+dx2.x+dy2.x, point2.y+dx2.y+dy2.y, point2.z+dx2.z+dy2.z, tx1+rx/res*0.0625, ty2-ry/res*0.0625);
    			
    			y+=ry;
    		}
    		
    		x+=rx;
		}
	}

	public static void disableLightMapTexture()
	{
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public static void enableLightMapTexture()
	{
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
    
	public static void translateToWorldCoords(Entity entity, float frame)
    {       
        double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)frame;
        double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)frame;
        double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)frame;
        
        GL11.glTranslated(-interpPosX, -interpPosY, -interpPosZ);
    }
	
	public static void drawOutlinedBoundingBox(AxisAlignedBB par1AxisAlignedBB)
    {
        Tessellator var2 = Tessellator.instance;
        var2.startDrawing(3);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.draw();
        var2.startDrawing(3);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.draw();
        var2.startDrawing(1);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var2.draw();
    }

    public static void setupBrightness(World world, int x, int y, int z)
    {
        int var3 = world.getLightBrightnessForSkyBlocks(x, y, z, 0);        
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var3 % 0xFFFF, var3 / 0xFFFF);
    }

    public static void renderLiquidCuboid(double[] bound, int texIndex, double res)
    {
        RenderUtils.renderLiquidQuad(//bottom
                new Vector3(bound[0], bound[2], bound[4]),
                new Vector3(bound[1], bound[2], bound[4]),
                new Vector3(bound[1], bound[2], bound[5]),
                new Vector3(bound[0], bound[2], bound[5]), 
                texIndex, res);
        RenderUtils.renderLiquidQuad(//top
                new Vector3(bound[0], bound[3], bound[4]),
                new Vector3(bound[0], bound[3], bound[5]),
                new Vector3(bound[1], bound[3], bound[5]),
                new Vector3(bound[1], bound[3], bound[4]), 
                texIndex, res);
        RenderUtils.renderLiquidQuad(//-x
                new Vector3(bound[0], bound[3], bound[4]),
                new Vector3(bound[0], bound[2], bound[4]),
                new Vector3(bound[0], bound[2], bound[5]),
                new Vector3(bound[0], bound[3], bound[5]), 
                texIndex, res);
        RenderUtils.renderLiquidQuad(//+x
                new Vector3(bound[1], bound[3], bound[5]),
                new Vector3(bound[1], bound[2], bound[5]),
                new Vector3(bound[1], bound[2], bound[4]),
                new Vector3(bound[1], bound[3], bound[4]), 
                texIndex, res);
        RenderUtils.renderLiquidQuad(//-z
                new Vector3(bound[1], bound[3], bound[4]),
                new Vector3(bound[1], bound[2], bound[4]),
                new Vector3(bound[0], bound[2], bound[4]),
                new Vector3(bound[0], bound[3], bound[4]), 
                texIndex, res);
        RenderUtils.renderLiquidQuad(//+z
                new Vector3(bound[0], bound[3], bound[5]),
                new Vector3(bound[0], bound[2], bound[5]),
                new Vector3(bound[1], bound[2], bound[5]),
                new Vector3(bound[1], bound[3], bound[5]), 
                texIndex, res);
    }
    
    public static void renderBlockOverlaySide(int x, int y, int z, int side, double tx1, double tx2, double ty1, double ty2)
    {
        double[] points = new double[]{x - 0.009, x + 1.009, y - 0.009, y + 1.009, z - 0.009, z + 1.009};

        Tessellator tessellator = Tessellator.instance;
        switch(side)
        {
            case 0:
                tessellator.addVertexWithUV(points[0], points[2], points[4], tx1, ty1);
                tessellator.addVertexWithUV(points[1], points[2], points[4], tx2, ty1);
                tessellator.addVertexWithUV(points[1], points[2], points[5], tx2, ty2);
                tessellator.addVertexWithUV(points[0], points[2], points[5], tx1, ty2);
            break;
            case 1:
                tessellator.addVertexWithUV(points[1], points[3], points[4], tx2, ty1);
                tessellator.addVertexWithUV(points[0], points[3], points[4], tx1, ty1);
                tessellator.addVertexWithUV(points[0], points[3], points[5], tx1, ty2);
                tessellator.addVertexWithUV(points[1], points[3], points[5], tx2, ty2);
            break;
            case 2:
                tessellator.addVertexWithUV(points[0], points[3], points[4], tx2, ty1);
                tessellator.addVertexWithUV(points[1], points[3], points[4], tx1, ty1);
                tessellator.addVertexWithUV(points[1], points[2], points[4], tx1, ty2);
                tessellator.addVertexWithUV(points[0], points[2], points[4], tx2, ty2);
            break;
            case 3:
                tessellator.addVertexWithUV(points[1], points[3], points[5], tx2, ty1);
                tessellator.addVertexWithUV(points[0], points[3], points[5], tx1, ty1);
                tessellator.addVertexWithUV(points[0], points[2], points[5], tx1, ty2);
                tessellator.addVertexWithUV(points[1], points[2], points[5], tx2, ty2);
            break;
            case 4:
                tessellator.addVertexWithUV(points[0], points[3], points[5], tx2, ty1);
                tessellator.addVertexWithUV(points[0], points[3], points[4], tx1, ty1);
                tessellator.addVertexWithUV(points[0], points[2], points[4], tx1, ty2);
                tessellator.addVertexWithUV(points[0], points[2], points[5], tx2, ty2);
            break;
            case 5:
                tessellator.addVertexWithUV(points[1], points[3], points[4], tx2, ty1);
                tessellator.addVertexWithUV(points[1], points[3], points[5], tx1, ty1);
                tessellator.addVertexWithUV(points[1], points[2], points[5], tx1, ty2);
                tessellator.addVertexWithUV(points[1], points[2], points[4], tx2, ty2);
            break;
        }
    }
}
